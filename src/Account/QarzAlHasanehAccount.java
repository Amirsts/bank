package Account;

public class QarzAlHasanehAccount extends Account {

    public QarzAlHasanehAccount(String accountNumber, int balance) {
        super(accountNumber, balance);
        if (!accountNumber.startsWith("03")) {
            throw new IllegalArgumentException("Qarz-al-Hasaneh account number must start with '03'.");
        }
    }

    @Override
    public int calculateInterest() {
        return 0;
    }
}
