package account;

public class ShortTermAccount extends Account {

    public ShortTermAccount(String accountNumber, int balance) {
        super(accountNumber, balance);
        if (!accountNumber.startsWith("02")) {
            throw new IllegalArgumentException("Short-term account number must start with '02'.");
        }
    }

    @Override
    public int calculateInterest() {
        if (balance > 100_000) {
            return (int)(balance * 0.05);
        }
        return 0;
    }
}
