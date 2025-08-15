package account;

import bank.Bank;
import exceptions.*;
import loan.BaseLoan;
import person.Customer;
import interfaces.IsPassWordTrue;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;


public abstract class Account implements IsPassWordTrue{
    private String accountId;
    long balance;
    private Customer owner;
    private String passWord;
    private Map<LocalDate, Integer> dailyTransfers ;


    public Account(String accountId, Customer owner, long balance ,String passWord) {
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

    public long getBalance() {
        balance -= 1000;
        return balance;
    }
    public long getBalanceForBank() {
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
            throw new   InvalidAmountException("amount should be more than zero");
        }

        if (amount > balance) {
            throw new InsufficientBalanceException("balance is not enough");
        }

        balance -= amount;
        System.out.println(" successful withdraw: " + amount + " ||     balance Tooman: " + balance);
    }


    public void secureWithdrawForLoan(int amount, String inputPassword, BaseLoan loan, LocalDate datePay )
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
        System.out.println(" successful withdraw " + amount + " Tooman || balance: " + balance);

        //decrease amount from loan & date of last time pay
        loan.pay(loan.installmentPerMonth() , datePay);
        loan.payInstallment();
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
}


