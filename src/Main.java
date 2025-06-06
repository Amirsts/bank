import account.Account;
import account.CurrentAccount;
import loan.NormalLoan;
import loan.*;
import person.Customer;
import request.Request;

import bank.Bank;
import branch.*;
import request.RequestType;

public class Main {
    public static void main(String[] args) {
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
        Account acc1 = new CurrentAccount("0100012345678" , customer ,1_000_000);
        customer.openAccount(acc1);
        branch.addAccount(acc1);

        // 5. درخواست وام
        BaseLoan loan = new NormalLoan(300_000_000, 12 , customer); // وام عادی

        Request req = new Request(RequestType.LOAN_REQUEST  , loan.toString() , customer );
        teller.handleRequest(req);


        // 6. بررسی وام توسط معاون
        if (!am.getMessageBox().isEmpty()) {
            Request amRequest = am.getMessageBox().get(0);
            am.handleRequest(amRequest);
        }

        // 7. بررسی نهایی توسط رئیس
        if (!bm.getMessageBox().isEmpty()) {
            Request bmRequest = bm.getMessageBox().get(0);
            bm.handleRequest(bmRequest);
        }

        // 8. نمایش وام‌های فعال مشتری
        System.out.println("\n📌 وام‌های فعال مشتری:");
        for (BaseLoan l : customer.getActiveLoans()) {
            System.out.println(l);
        }

        // 9. نمایش موجودی کل بانک
        System.out.println("\n💰 موجودی کل بانک: " + bank.getTotalBankBalance() + " تومان");

        // 10. نمایش خلاصه
        System.out.println("\n📊 اطلاعات شعبه و مشتریان:");
        bank.displayBranches();
        bank.displayCustomers();

    }
}


