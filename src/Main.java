import Account.Account;
import Account.CurrentAccount;
import Account.ShortTermAccount;

public class Main {
    public static void main(String[] args) {
        Account acc1 = new CurrentAccount("0100000000001", 1_000_000);
        Account acc2 = new ShortTermAccount("0200000000001", 500_000);

        System.out.println("Balance acc1: " + acc1.getBalance());
        System.out.println("Balance acc2: " + acc2.getBalance());

        acc1.transfer(acc2, 200_000);

        System.out.println("Balance acc1 after transfer: " + acc1.getBalance());
        System.out.println("Balance acc2 after transfer: " + acc2.getBalance());

        System.out.println("Interest for acc1: " + acc1.calculateInterest());
        System.out.println("Interest for acc2: " + acc2.calculateInterest());
    }
}
