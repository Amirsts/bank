import account.Account;
import account.CurrentAccount;
import person.Customer;
import request.Request;
import request.RequestType;

import bank.Bank;
import branch.*;

public class Main {
    public static void main(String[] args) {

        System.out.println("🏦 ساخت بانک و اطلاعات اولیه...");

        // 1. ساخت بانک و شعبه
        Bank bank = new Bank();
        Branch branch = new Branch("101");
        bank.addBranch(branch);

        // 2. افزودن کارمندان
        BranchManager bm = new BranchManager("زهرا", "کریمی", "1985-02-12", "0011223344", "تهران", "09120000000", "BM101");
        AssistantManager am = new AssistantManager("رضا", "احمدی", "1987-05-20", "1122334455", "تهران", "09121234567", "AM101");
        Teller teller = new Teller("سارا", "محمدی", "1990-01-01", "2233445566", "تهران", "09128889999", "T101");

        branch.setBranchManager(bm);
        branch.setAssistantManager(am);
        branch.addTeller(teller);

        bank.addEmployee(bm);
        bank.addEmployee(am);
        bank.addEmployee(teller);

        // 3. افزودن مشتری
        Customer customer = new Customer("علی", "رضایی", "1995-10-10", "3344556677", "تهران", "09121112222", "C001");
        branch.addCustomer(customer);
        bank.addCustomer(customer);

        // 4. باز کردن حساب برای مشتری
        Account acc1 = new CurrentAccount("0100012345678", customer, 1_000_000);
        customer.openAccount(acc1);
        branch.addAccount(acc1);

        // 5. ارسال درخواست بستن حساب توسط مشتری
        System.out.println("\n📥 مرحله 1: ارسال درخواست بستن حساب توسط مشتری...");
        customer.closeAccount(acc1.getAccountId());

        // 6. بررسی توسط Teller
        if (!customer.getMessageBox().isEmpty()) {
            Request closeRequest = null;
            for (Request req : customer.getMessageBox()) {
                if (req.getType() == RequestType.CLOSE_ACCOUNT) {
                    closeRequest = req;
                    break;
                }
            }

            if (closeRequest != null) {
                teller.handleRequest(closeRequest);
            } else {
                System.out.println("❗ هیچ درخواست بستن حسابی در messageBox یافت نشد.");
            }
        }

        // 7. بررسی توسط Assistant Manager
        if (!am.getMessageBox().isEmpty()) {
            Request amRequest = am.getMessageBox().remove(0); // حذف بعد از پردازش
            am.handleRequest(amRequest);
        }

        // 8. بررسی نهایی توسط Branch Manager
        if (!bm.getMessageBox().isEmpty()) {
            Request bmRequest = bm.getMessageBox().remove(0);
            bm.handleRequest(bmRequest);
        }

        // 9. بررسی نتیجه نهایی
        System.out.println("\n🔍 بررسی لیست حساب‌های مشتری:");
        if (customer.getAccounts().isEmpty()) {
            System.out.println("✅ حساب مشتری با موفقیت بسته شد.");
        } else {
            for (Account acc : customer.getAccounts()) {
                System.out.println("🔸 " + acc.getAccountId());
            }
        }

        System.out.println("\n🔍 بررسی لیست حساب‌های شعبه:");
        if (branch.getAccounts().isEmpty()) {
            System.out.println("✅ حساب از لیست شعبه نیز حذف شد.");
        } else {
            for (Account acc : branch.getAccounts()) {
                System.out.println("🔸 " + acc.getAccountId());
            }
        }

        // 10. نمایش موجودی کل بانک
        System.out.println("\n💰 موجودی کل بانک: " + bank.getTotalBankBalance() + " تومان");

        // 11. نمایش خلاصه اطلاعات
        System.out.println("\n📊 اطلاعات شعبه و مشتریان:");
        bank.displayBranches();
        bank.displayCustomers();
    }
}
