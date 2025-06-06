package person;

import java.util.List;
import java.util.ArrayList;
import account.Account;
import loan.BaseLoan;
import interfaces.*;
import request.*;


public class Customer extends Person implements Displayable , Loanable , Payable {
    private String customerId;
    private List<Account> accounts ;
    private List<Request> messageBox;
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

    public void openAccount(Account account1) {
        accounts.add(account1);

        Request req = new Request(
                RequestType.OPEN_ACCOUNT,
                "Request to open an account at: " + account1.getAccountNumber(),
                this
        );

        messageBox.add(req);
        System.out.println("The account opening request has been registered.");
    }

    public void closeAccount(String accountNumber) {
        Account temp = findAccount(accountNumber);

        if (temp != null) {
            Request req = new Request(
                    RequestType.CLOSE_ACCOUNT,
                    "Request to close account number: " + accountNumber,
                    this
            );

            messageBox.add(req);
            System.out.println("The request to close the account has been registered.");
        } else {
            System.out.println("Error: The requested account could not be found.");
        }
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
