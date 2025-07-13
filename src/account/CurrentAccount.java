package account;

import person.Customer;

public class CurrentAccount extends Account {

    public CurrentAccount(String accountNumber, Customer owner, int balance , String passWord) {
        super(accountNumber, owner, balance , passWord);
        if (!accountNumber.startsWith("01")) {
            throw new IllegalArgumentException("Current account number must start with '01'.");
        }
    }

}
