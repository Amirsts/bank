package account;

public abstract class Account {
    protected String accountNumber;
    protected int balance;

    public Account(String accountNumber, int balance) {
        if (accountNumber == null || !accountNumber.matches("\\d{13}")) {
            throw new IllegalArgumentException("Account number must be 13 digits.");
        }
        if (balance < 0) {
            throw new IllegalArgumentException("Balance cannot be negative.");
        }
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public int getBalance() {
        return balance;
    }

    public void deposit(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive.");
        }
        balance += amount;
    }

    public void withdraw(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdraw amount must be positive.");
        }
        if (amount > balance) {
            throw new IllegalArgumentException("Insufficient balance.");
        }
        balance -= amount;
    }

    public void transfer(Account toAccount, int amount) {
        this.withdraw(amount);
        toAccount.deposit(amount);
        System.out.println("Amount " + amount + " from account " + this.accountNumber +
                " to account " + toAccount.getAccountNumber() + " transferred successfully.");
    }

    public abstract int calculateInterest();
}


