package account;

import person.Customer;

public class ShortTermAccount extends Account {

    public ShortTermAccount(String accountNumber , Customer owner , int balance , String passWord) {
        super(accountNumber, owner , balance , passWord);
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
