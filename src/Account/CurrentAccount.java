package Account;

public class CurrentAccount extends Account {

    public CurrentAccount(String accountNumber, int balance) {
        super(accountNumber, balance);
        if (!accountNumber.startsWith("01")) {
            throw new IllegalArgumentException("Current account number must start with '01'.");
        }
    }

    @Override
    public int calculateInterest() {

        return 0;
    }
}
