package account;

import exceptions.*;
import person.Customer;
import interfaces.IsPassWordTrue;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;


public abstract class Account implements IsPassWordTrue{
    private String accountId;
    int balance;
    private Customer owner;
    private String passWord;
    private Map<LocalDate, Integer> dailyTransfers ;

    public Account(String accountId, Customer owner, int balance ,String passWord) {
        if (accountId == null || !accountId.matches("\\d{13}")) {
            throw new IllegalArgumentException("Account number must be 13 digits.");
        }
        if (balance < 0) {
            throw new IllegalArgumentException("Balance cannot be negative.");
        }
        this.accountId = accountId;
        this.owner = owner;
        this.balance = balance;
        this.passWord = passWord;
        this.dailyTransfers = new HashMap<>();
    }

    public String getAccountId() {
        return accountId;
    }

    public int getBalance() {
        balance -= 1000;
        return balance;
    }
    public int getBalanceForBank() {
        return balance;
    }

    public Customer getOwner(){
        return owner;
    }

    public void setOwner(Customer owner) {
        this.owner = owner;
    }

    public void deposit(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive.");
        }
        balance += amount;
    }

    public void secureWithdraw(int amount, String inputPassword)
            throws IncorrectPasswordException, InvalidAmountException, InsufficientBalanceException {

        if (!isPassWordTrue(inputPassword)) {
            throw new IncorrectPasswordException("The password entered is incorrect.");
        }

        if (amount <= 0) {
            throw new InvalidAmountException("amount should be more than zero");
        }

        if (amount > balance) {
            throw new InsufficientBalanceException("balance is not enough");
        }

        balance -= amount;
        System.out.println("💸 successful withdraw " + amount + " balance Tooman " + balance);
    }


    public void secureWithdrawForLoan(int amount, String inputPassword)
            throws IncorrectPasswordException, InvalidAmountException, InsufficientBalanceException {

        if (!isPassWordTrue(inputPassword)) {
            throw new IncorrectPasswordException("The password entered is incorrect.");
        }

        if (amount <= 0) {
            throw new InvalidAmountException("amount should be more than zero");
        }

        if (amount > balance) {
            throw new InsufficientBalanceException("balance is not enough");
        }

        balance -= amount;
        System.out.println("💸 successful withdraw " + amount + " balance Tooman " + balance);
    }

    public void recordTransfer(int amount, LocalDate date) throws DailyTransferLimitExceededException {
        int transferred = dailyTransfers.getOrDefault(date, 0);

        if (transferred + amount > 10_000_000) {
            throw new DailyTransferLimitExceededException("The daily transfer limit of 10 million Tomans has been exceeded.");
        }

        dailyTransfers.put(date, transferred + amount);
    }

    @Override
    public boolean isPassWordTrue(String inpPassWord){
        if (this.passWord.equals(inpPassWord)){
            return true;
        }
        return false;
    }


    public void transfer(Account toAccount, int amount , String passWord) throws IncorrectPasswordException, InvalidAmountException, InsufficientBalanceException {
        this.secureWithdraw(amount , passWord );
        toAccount.deposit(amount);
        System.out.println("Amount " + amount + " from account " + this.accountId +
                " to account " + toAccount.getAccountId() + " transferred successfully.");
    }

    public abstract int calculateInterest();
}


