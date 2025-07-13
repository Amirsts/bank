package person;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import account.Account;
import account.ShortTermAccount;
import exceptions.*;
import loan.BaseLoan;
import interfaces.*;
import request.*;
import message.MessageBox;

public class Customer extends Person implements Displayable , Loanable , FindAccount  {
    private String customerId;
    private List<Account> accounts ;
    private List<ShortTermAccount> shortTermAccounts;
    private MessageBox messageBox;
    private List<BaseLoan> loans ;
    private boolean isActive;
    private Map<LocalDate, Integer> dailyTransfers ;



    public Customer(String firstName ,String lastName , String birthDay , String nationalId
            , String address , String phoneNum , String customerId  ){
        super(firstName ,lastName , birthDay, nationalId , address , phoneNum);

        this.customerId = customerId;
        this.accounts = new ArrayList<>();
        this.shortTermAccounts = new ArrayList<>();
        this.messageBox = new MessageBox();
        this.loans = new ArrayList<>();
        this.dailyTransfers = new HashMap<>();
    }

    //getters
    public String getCustomerId(){
        return customerId;
    }

    public List<Account>   getAccounts(){
        return accounts;
    }

    public List<ShortTermAccount> getShortTermAccounts() {
        return shortTermAccounts;
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
    public boolean hasActiveLoan(){
        List<BaseLoan> activeLoans = new ArrayList<>();
        for (BaseLoan loan : loans) {
            if (loan.isActive()) {
                return true;
            }
        }
        return false;
    }

    public MessageBox getMessageBox() {
        return messageBox;
    }


    //methods
    public void openAccount(Account account1) {
        accounts.add(account1);
    }

    public void openShortTermAccount(ShortTermAccount shortTermAccount) {
        shortTermAccounts.add(shortTermAccount);
    }

    public void closeAccount(String accountNumber) {
        Account temp = findAccount(accountNumber);

        if (temp != null) {
            messageBox.addRequest(new Request(
                    RequestType.CLOSE_ACCOUNT,
                    this,
                    "Request to close account number: " + accountNumber,
                    accountNumber
            ));

        } else {
            System.out.println("Error: The requested account could not be found.");
        }
    }


    public void addLoan(BaseLoan loan) {
        loans.add(loan);
    }

    public void removeAccount(String accountNumber){
        accounts.removeIf( acc -> acc.getAccountId().equals(accountNumber));
        System.out.println("Account" + accountNumber + "deleted from customer's accounts");
    }

    @Override
    public double getNormalLoanCeiling(){
        return 500_000_000;
    }

    @Override
    public double getTashilatCeiling(){
        return 1_000_000_000;
    }

    @Override
    public void displayInfo(){
        System.out.println("Customer: " + getFullName() + "National Code: " + getNationalCode());
        for (Account account : accounts) {
            System.out.printf("Account number: %sBalance: %d%n", account.getAccountId(), account.getBalance());
        }
    }
    @Override
    public Account findAccount(String accountNumber){
        for (Account temp : accounts){
            if (temp.getAccountId().trim().equals(accountNumber.trim())) {
                return temp;
            }
        }
        return null;
    }


    @Override
    public String toString(){
        return super.toString()+"\nPerson.Customer ID: " +customerId +
                "\n number of accounts: " + accounts.size() +
                "\n Number of messages: " + messageBox.size();
    }
}
