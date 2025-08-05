package output.pages;

import account.CurrentAccount;
import bank.Bank;
import branch.Branch;
import branch.BranchManager;
import branch.AssistantManager;
import branch.Teller;
import loan.NormalLoan;
import person.Customer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class SubMainPage {

    static Bank bank = new Bank();
    static Branch branch = new Branch("001");

    static Branch currentBranch = branch;

    public void setCurrentBranch (Branch newBranch) {
        currentBranch = newBranch;
    }

    public Branch getBranch() {
        return currentBranch;
    }

    public Bank getBank() {
        return bank;
    }


    public static void subMainPage(){
        // Creating a bankS and branches
        Bank bankS = bank;

        Branch branchS = branch;

        BranchManager branchManager = new BranchManager(
                "Ali", "Rezayi", "1990-01-01", "1234567890",
                "Tehran, Iran", "09121234567", "BM001" , "1234"
        );
        branchS.setBranchManager(branchManager);
        bankS.addEmployee(branchManager);
        bankS.addBranch(branchS);

        AssistantManager assistantManager = new AssistantManager("pouriya" , "Farahmand" , "1990-05-13" , "0986565654" , "Iran" ,"09896543298" , "pr1234"  , "1234");
        bankS.addEmployee(assistantManager);
        branchS.setAssistantManager(assistantManager);

        // Creating customers & teller for test
        Customer customer = new Customer(
                "Mobin", "Rangsaz", "1985-07-20", "1029384756",
                "Shiraz, Iran", "09134567890", "mo1234", "1385"
        );
        bankS.addCustomer(customer);
        branchS.addCustomer(customer);
        CurrentAccount accountMobin = new CurrentAccount("0113269874987",customer,500000000, "1384");
        customer.openAccount(accountMobin);
        bankS.addAccount(accountMobin);
        branchS.addAccount(accountMobin);

        Customer customer1 = new Customer(
                "Amirmohammad", "Mohammadi", "1985-07-20", "1029384756",
                "Shiraz, Iran", "09134567890", "1", "1384"
        );
        bankS.addCustomer(customer1);
        branchS.addCustomer(customer1);
        CurrentAccount accountAmir = new CurrentAccount("0101919244970",customer1,500000000, "1384");
        customer1.openAccount(accountAmir);
        bankS.addAccount(accountAmir);
        branchS.addAccount(accountAmir);
        CurrentAccount accountAmir2 = new CurrentAccount("0101919244972",customer1,500000000, "1384");
        customer1.openAccount(accountAmir2);
        bankS.addAccount(accountAmir2);
        branchS.addAccount(accountAmir2);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate date = LocalDate.parse("01/01/1404", formatter);
        NormalLoan normalLoan = new NormalLoan(1000000 , 12 , date ,customer1);
        customer1.addLoan(normalLoan);


        // Creating Account for customer
        Teller teller = new Teller("Ali","asghari","1986-12-20", "0965656654","mashad" , "09064563232","2", "2");
        bankS.addEmployee(teller);
        branchS.addTeller(teller);
    }

    // Customer selection among bank customers
    static Customer selectCustomer(String customerID) {
        List<Customer> customers = bank.getCustomers(); // Creating a list from customers of bank
        if (customers.isEmpty()) {
            System.out.println("There are no registered customers.");
            return null;
        }
        String cID = customerID;

        for (int i = 0; i < customers.size(); i++) {
            Customer tempC = customers.get(i);

            if (tempC.getCustomerId().equals(cID)){
                return customers.get(i);
            }
        }
        System.out.println("Customer not found, please try again.");
        return null;
    }
}
