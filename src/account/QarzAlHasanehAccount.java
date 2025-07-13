package account;

import person.Customer;

public class QarzAlHasanehAccount extends Account {

    public QarzAlHasanehAccount(String accountNumber, Customer owner,  int balance , String passWord) {
        super(accountNumber, owner , balance , passWord);
        if (!accountNumber.startsWith("03")) {
            throw new IllegalArgumentException("Qarz-al-Hasaneh account number must start with '03'.");
        }
    }


}
