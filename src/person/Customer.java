package person;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import account.Account;
import exceptions.*;
import loan.BaseLoan;
import interfaces.*;
import request.*;
import message.MessageBox;

public class Customer extends Person implements Displayable , Loanable , Payable {
    private String customerId;
    private List<Account> accounts ;
    private MessageBox messageBox;
    private List<BaseLoan> loans ;
    private boolean isActive;
    private Map<LocalDate, Integer> dailyTransfers ;



    public Customer(String firstName ,String lastName , String birthDay , String nationalId
            , String address , String phoneNum , String customerId  ){
        super(firstName ,lastName , birthDay, nationalId , address , phoneNum);

        this.customerId = customerId;
        this.accounts = new ArrayList<>();
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


    public List<BaseLoan> getActiveLoans() {
        List<BaseLoan> activeLoans = new ArrayList<>();
        for (BaseLoan loan : loans) {
            if (loan.isActive()) {
                activeLoans.add(loan);
            }
        }
        return activeLoans;
    }

    public MessageBox getMessageBox() {
        return messageBox;
    }


    //methods

    public void recordTransfer(int amount, LocalDate date) throws DailyTransferLimitExceededException {
        int transferred = dailyTransfers.getOrDefault(date, 0);

        if (transferred + amount > 10_000_000) {
            throw new DailyTransferLimitExceededException("The daily transfer limit of 10 million Tomans has been exceeded.");
        }

        dailyTransfers.put(date, transferred + amount);
    }



    public Account findAccount(String accountNumber){
        for (Account temp : accounts){
            if (temp.getAccountId().trim().equals(accountNumber.trim())) {
                return temp;
            }
        }
        return null;
    }

    public void openAccount(Account account1) {
        accounts.add(account1);

        messageBox.addRequest(new Request(
                RequestType.OPEN_ACCOUNT,
                "Request to open an account at: " + account1.getAccountId(),
                this
        ));

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
    public boolean isEligibleForLoan(){
        return getActiveLoans().isEmpty();
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
    }


    @Override
    public void pay(int amount){
        System.out.println("customer: " + getFullName() + " is paying " + amount + "Tomans");

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
