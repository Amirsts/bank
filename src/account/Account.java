package account;

import exceptions.IncorrectPasswordException;
import exceptions.InsufficientBalanceException;
import exceptions.InvalidAmountException;
import person.Customer;


public abstract class Account {
    private String accountId;
    int balance;
    private Customer owner;
    private String passWord;

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
    }

    public String getAccountId() {
        return accountId;
    }

    public int getBalance() {
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

    public void secureWithdraw(double amount, String inputPassword)
            throws IncorrectPasswordException, InvalidAmountException, InsufficientBalanceException {

        if (!this.passWord.equals(inputPassword)) {
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


   /* public void transfer(Account toAccount, int amount) {
        this.secureWithdraw(amount);
        toAccount.deposit(amount);
        System.out.println("Amount " + amount + " from account " + this.accountId +
                " to account " + toAccount.getAccountId() + " transferred successfully.");
    }*/

    public abstract int calculateInterest();
}


