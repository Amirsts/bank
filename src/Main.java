import account.CurrentAccount;
import bank.Bank;
import branch.Branch;
import branch.BranchManager;
import branch.AssistantManager;
import branch.Teller;
import person.Customer;
import request.Request;
import request.RequestType;
import loan.NormalLoan;
import exceptions.*;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        // ========================
        // ۱. ایجاد بانک و شعبه‌ها
        // ========================
        Bank bank = new Bank();

        // ایجاد شعبه با شماره "001"
        Branch branch = new Branch("001");

        // ایجاد مدیر شعبه
        BranchManager branchManager = new BranchManager(
                "Ali", "Rezayi", "1990-01-01", "1234567890",
                "Tehran, Iran", "09121234567", "BM001"
        );
        branch.setBranchManager(branchManager);

        // ایجاد دستیار شعبه
        AssistantManager assistantManager = new AssistantManager(
                "Sara", "Karimi", "1988-03-22", "1122334455",
                "Isfahan, Iran", "09120123456", "AM001"
        );
        branch.setAssistantManager(assistantManager);

        // ایجاد تحویل‌دار (Teller) و اختصاص به شعبه
        Teller teller = new Teller(
                "Mohammad", "Ahmadi", "1992-05-14", "0987654321",
                "Mashhad, Iran", "09127834567", "TL001"
        );
        branch.addTeller(teller);

        // افزودن شعبه به بانک
        bank.addBranch(branch);

        // ========================
        // ۲. ایجاد مشتری و حساب‌ها
        // ========================
        Customer customer = new Customer(
                "Reza", "Moazeni", "1985-07-20", "1029384756",
                "Shiraz, Iran", "09134567890", "CUST001"
        );
        bank.addCustomer(customer);

        // ایجاد حساب جاری برای مشتری
        CurrentAccount currentAccount = new CurrentAccount(
                "0101234567892", customer, 1000000, "pass123"
        );
        branch.addAccount(currentAccount);
        customer.openAccount(currentAccount);

        // ایجاد یک حساب دیگر برای تست انتقال بین حساب‌های مشتری
        CurrentAccount secondAccount = new CurrentAccount(
                "0109876543212", customer, 5000000, "pass456"
        );
        branch.addAccount(secondAccount);
        customer.openAccount(secondAccount);

        // ========================
        // ۳. تست انتقال بین حساب‌های مشتری
        // ========================
        System.out.println("---- Testing transfer between own accounts ----");
        try {
            customer.transferBetweenOwnAccounts("0101234567892", "0109876543212", 500000, "pass123");
        } catch (AccountNotFoundException | IncorrectPasswordException | InvalidAmountException |
                 InsufficientBalanceException | DailyTransferLimitExceededException ex) {
            System.out.println("Transfer failed: " + ex.getMessage());
        }

        // ========================
        // ۴. تست درخواست بستن حساب
        // ========================
        System.out.println("\n---- Testing close account request ----");
        customer.closeAccount("0101234567892");

        // در اینجا درخواست بستن حساب به جعبه پیام مشتری اضافه شده است.
        // شبیه‌سازی پردازش درخواست توسط تحویل‌دار (Teller)
        Request closeRequest = customer.getMessageBox().getAllRequests().stream()
                .filter(r -> r.getType() == RequestType.CLOSE_ACCOUNT)
                .findFirst().orElse(null);
        if (closeRequest != null) {
            teller.handleRequest(closeRequest);
        } else {
            System.out.println("No close account request found.");
        }

        // ========================
        // ۵. تست درخواست وام
        // ========================
        System.out.println("\n---- Testing loan request ----");
        Request loanRequest = new Request(RequestType.LOAN_REQUEST, "Please approve my loan", customer);
        // ثبت درخواست وام در جعبه پیام مشتری
        customer.getMessageBox().addRequest(loanRequest);
        // تحویل‌دار درخواست وام را دریافت کرده و آن را به دستیار شعبه ارجاع می‌دهد.
        teller.handleRequest(loanRequest);

        // ========================
        // ۶. تست انتشار و پردازش پرداخت وام
        // ========================
        System.out.println("\n---- Testing loan payment ----");
        NormalLoan normalLoan = new NormalLoan(300_000_000, 12, customer);
        customer.addLoan(normalLoan);
        System.out.println("Due amount before payment: " + customer.getDueAmount());
        // پرداخت مبلغی به وام
        customer.pay(10_000_000);
        System.out.println("Due amount after payment: " + customer.getDueAmount());

        // ========================
        // ۷. تست محدودیت انتقال روزانه
        // ========================
        System.out.println("\n---- Testing daily transfer limit ----");
        try {
            // انتقالاتی به روز جاری
            customer.recordTransfer(9_000_000, LocalDate.now());
            // این انتقال باعث تجاوز از سقف 10 میلیون تومان می‌شود
            customer.recordTransfer(2_000_000, LocalDate.now());
        } catch (DailyTransferLimitExceededException e) {
            System.out.println("Daily transfer limit exceeded: " + e.getMessage());
        }

        // ========================
        // ۸. نمایش پیام‌ها و اطلاعات نهایی
        // ========================
        System.out.println("\n---- Customer's MessageBox ----");
        customer.getMessageBox().printAll();

        System.out.println("\n---- Final Display Info ----");
        branch.displayInfo();
        customer.displayInfo();
        bank.displayBranches();
        bank.displayCustomers();
    }
}
