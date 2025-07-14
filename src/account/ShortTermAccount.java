package account;

import person.Customer;

import java.time.LocalDate;

public class ShortTermAccount extends Account {

    private LocalDate dateOpenAccount;


    public ShortTermAccount(String accountNumber , Customer owner , long balance , String passWord ,LocalDate dateOpenAccount) {
        super(accountNumber, owner , balance , passWord);
        if (!accountNumber.startsWith("02")) {
            throw new IllegalArgumentException("Short-term account number must start with '02'.");
        }
        this.dateOpenAccount = dateOpenAccount ;
    }

    public int profitCheck(LocalDate dateCheckBalance){
        int monthsSinceStart = dateCheckBalance.getMonthValue() - dateOpenAccount.getMonthValue() +
                12 * (dateCheckBalance.getYear() - dateOpenAccount.getYear());

        dateOpenAccount = dateCheckBalance;
        if (getBalanceForBank() > 100_000 && monthsSinceStart >= 1 ){
            int profit = (int)(monthsSinceStart * 0.00416  * getBalanceForBank());
            System.out.println("Your previous balance:" + balance + "Your profit amount" + profit );
            balance += profit;
            return profit ;
        }
        return 0;
    }

}
