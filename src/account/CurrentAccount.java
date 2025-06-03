package account;

import person.Customer;

public class CurrentAccount extends Account {

    public CurrentAccount(String accountNumber, Customer owner, int balance) {
        super(accountNumber, owner, balance);
        if (!accountNumber.startsWith("01")) {
            throw new IllegalArgumentException("Current account number must start with '01'.");
        }
    }

    @Override
    public int calculateInterest() {

        return 0;
    }
}
