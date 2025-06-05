package person;

import java.util.List;
import java.util.ArrayList;
import account.Account;
import loan.BaseLoan;
import interfaces.*;

public class Customer extends Person implements Displayable , Loanable , Payable {
    private String customerId;
    private List<Account> accounts ;
    private List<String> messageBox;
    private List<BaseLoan> loans ;
    private boolean isActive;

    public Customer(String firstName ,String lastName , String birthDay , String nationalId
            , String address , String phoneNum , String customerId  ){
        super(firstName ,lastName , birthDay, nationalId , address , phoneNum);

        this.customerId = customerId;
        this.accounts = new ArrayList<>();
        this.messageBox = new ArrayList<>();
        this.loans = new ArrayList<>();
    }

    //getters
    public String getCustomerId(){
        return customerId;
    }

    public List<Account> getAccounts(){
        return accounts;
    }

    public List<BaseLoan> getActiveLoans() {
        List<BaseLoan> activeLoans = new ArrayList<>();
        for (BaseLoan loan : loans) {
            if (loan.isActive()) {
                activeLoans.add(loan);
            }
        }
        return activeLoans;
    }


    //methods
    public Account findAccount(String accountNumber){
        for (Account temp : accounts){
            if (temp.getAccountNumber().equals(accountNumber)){
                return temp;
            }
        }
        return null;
    }

    public void openAccount(Account account1){
        accounts.add(account1);
        messageBox.add("Request to open an account with the number:" + account1.getAccountNumber() + "registered");
    }

    public void closeAccount(String accountNumber){
        Account temp1 = findAccount(accountNumber);
        if (temp1 != null){
            messageBox.add("Request to close thr account:" + accountNumber +"registered");
        }
        messageBox.add("Error: The requested account was not found.");
    }

    public void addLoan(BaseLoan loan) {
        loans.add(loan);
    }

    @Override
    public boolean isEligibleForLoan(){
        return getActiveLoans().isEmpty();
    }

    @Override
    public double getLoanCeiling(){
        return 500_000_000;
    }

    @Override
    public void displayInfo(){
        System.out.println("Customer: " + getFullName() + "National Code: " + getNationalCode());
    }


    @Override
    public void pay(double amount){
        System.out.println("customer:" + getFullName() + "is paying" + amount + "Tomans");

        for (BaseLoan loan : loans){
            if(amount <= 0) break;
            double due = loan.getRemainingAmount();

            if (amount >= due){
                loan.pay(due);
                amount -= due;
            }else {
                loan.pay(amount);
                amount = 0;
            }
        }

        if (amount > 0) {
            if (!accounts.isEmpty()) {
                accounts.get(0).deposit(amount);
                System.out.println(" The excess amount was returned to the customer's account..");
            } else {
                System.out.println("Customer does not have a bank account. Additional amount could not be saved.");
            }
        }
    }

    @Override
    public double getDueAmount(){
        double totalDue = 0 ;
        for (BaseLoan loan : loans){
            totalDue += loan.getRemainingAmount();
        }
        return totalDue;
    }
    //toString

    @Override
    public String toString(){
        return super.toString()+"\nPerson.Customer ID: " +customerId +
                "\n number of accounts: " + accounts.size() +
                "\n Number of messages: " + messageBox.size();
    }
}
