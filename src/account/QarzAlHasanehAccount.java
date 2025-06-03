package account;

import person.Customer;

public class QarzAlHasanehAccount extends Account {

    public QarzAlHasanehAccount(String accountNumber, Customer owner,  int balance) {
        super(accountNumber, owner , balance);
        if (!accountNumber.startsWith("03")) {
            throw new IllegalArgumentException("Qarz-al-Hasaneh account number must start with '03'.");
        }
    }

    @Override
    public int calculateInterest() {
        return 0;
    }
}
