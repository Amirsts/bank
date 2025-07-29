package output.pages;

import bank.Bank;
import branch.Branch;
import branch.BranchManager;
import branch.AssistantManager;
import branch.Teller;
import person.Customer;

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
                "Shiraz, Iran", "09134567890", "mo1234"
        );
        bankS.addCustomer(customer);
        branchS.addCustomer(customer);

        Customer customer1 = new Customer(
                "Amirmohammad", "Mohammadi", "1985-07-20", "1029384756",
                "Shiraz, Iran", "09134567890", "1"
        );
        bankS.addCustomer(customer1);
        branchS.addCustomer(customer1);

        // Creating Account for customer
        Teller teller = new Teller("Ali","asghari","1986-12-20", "0965656654","mashad" , "09064563232","2", "2");
        bankS.addEmployee(teller);
        branchS.addTeller(teller);
    }
}
