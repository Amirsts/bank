//import account.Account;
//import account.CurrentAccount;
//import account.ShortTermAccount;
//
//public class Main {
//    public static void main(String[] args) {
//        Account acc1 = new CurrentAccount("0100000000001", 1_000_000);
//        Account acc2 = new ShortTermAccount("0200000000001", 500_000);
//
//        System.out.println("Balance acc1: " + acc1.getBalance());
//        System.out.println("Balance acc2: " + acc2.getBalance());
//
//        acc1.transfer(acc2, 200_000);
//
//        System.out.println("Balance acc1 after transfer: " + acc1.getBalance());
//        System.out.println("Balance acc2 after transfer: " + acc2.getBalance());
//
//        System.out.println("Interest for acc1: " + acc1.calculateInterest());
//        System.out.println("Interest for acc2: " + acc2.calculateInterest());
//    }
//}
import loan.NormalLoan;
import loan.TashilatLoan;
import person.Customer;

public class Main {
    public static void main(String[] args) {
        Customer customer = new Customer("Ali", "Karimi");

        NormalLoan normalLoan = new NormalLoan(1_000_000, 12, customer);
        TashilatLoan tashilatLoan = new TashilatLoan(2_000_000, 24, customer);

        System.out.println("Normal Loan:");
        System.out.println("Repayment: " + normalLoan.calculateTotalRepayment());
        System.out.println("Penalty for 2 months delay: " + normalLoan.calculatePenalty(2));

        System.out.println("\nTashilat Loan:");
        System.out.println("Repayment: " + tashilatLoan.calculateTotalRepayment());
        System.out.println("Penalty for 1 month delay: " + tashilatLoan.calculatePenalty(1));
    }
}

