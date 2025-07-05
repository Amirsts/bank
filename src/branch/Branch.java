package branch;

import account.Account;
import person.Customer;

import java.util.ArrayList;
import java.util.List;
import interfaces.Displayable;

public class Branch implements Displayable {

    private String branchNumber;
    private BranchManager branchManager;
    private AssistantManager assistantManager;
    private List<Teller> tellers;
    private List<Account> accounts;
    private List<Customer> customers;

    public Branch(String branchNumber){
        this.branchNumber = branchNumber;
        this.tellers = new ArrayList<>();
        this.accounts = new ArrayList<>();
        this.customers = new ArrayList<>();
    }

    //getters & setters

    public String getBranchNumber(){
        return branchNumber;
    }

    public BranchManager getBranchManager() {
        return branchManager;
    }

    public void setBranchManager(BranchManager branchManager){
        this.branchManager  = branchManager ;
        branchManager.setAssignedBranch(this);
    }

    public AssistantManager getAssistantManager (){
        return assistantManager;
    }

    public void setAssistantManager(AssistantManager assistantManager){
        this.assistantManager = assistantManager;
        assistantManager.setAssignedBranch(this);
    }


    public List<Teller> getTellers(){
        return tellers;
    }

    public List<Account>  getAccounts(){
        return accounts;
    }

    public List<Customer> getCustomers(){
        return customers;
    }

    public double getCurrentShortTermBalance() {
        double balance = 0;

        for (int i = 0; i < accounts.size(); i++) {
            String accId = accounts.get(i).getAccountId();
            if (accId.startsWith("01") || accId.startsWith("02")) {
                balance += accounts.get(i).getBalance();
            }
        }

        return balance;
    }


    public double getQarzAlhasanehBalance(){
        double balance = 0;
        for (int i = 0 ; i < accounts.size() ; i++ ){
            if (accounts.get(i).getAccountId().startsWith("03")){
                balance += accounts.get(i).getBalance();
            }
        }

        return balance;
    }

    //methods
    public void addTeller(Teller teller){
        tellers.add(teller);
        teller.setAssignedBranch(this);
    }

    boolean removeTeller(String tellerId){
      return tellers.removeIf(t-> t.getEmployeeId().equals(tellerId) );
    }

    public void addAccount(Account account){
        accounts.add(account);
    }

    public Account findAccount(String accountnumber){
        for (Account temp : accounts){
            if (temp.getAccountId().equals(accountnumber)){
                return  temp;
            }
        }
        return null;
    }

    public void addCustomer(Customer customer){
        customers.add(customer);
    }

    public Customer findCustomer(String nationalCode){
        for (Customer cu : customers){
            if (cu.getNationalCode().equals(nationalCode)){
                return cu;
            }
        }
        return null;
    }

    public void removeAccount(String accountNumber){
        accounts.removeIf(acc -> acc.getAccountId().equals(accountNumber));
        System.out.println("Account" + accountNumber + "deleted from the branch's accounts");
    }

    public Teller getSolitudeTeller(){
        int tmp1 , tmp2 ;
        int indexMin = 0;
        int min = tellers.get(0).getMessageBox().size();

        for (int i = 0 ; i < tellers.size() ; i++){
             tmp2 = tellers.get(i).getMessageBox().size();
             if (min > tmp2 ) {
                 min = tmp2;
                 indexMin = i;
             }
        }
        return tellers.get(indexMin);
    }

    @Override
    public void displayInfo(){
        System.out.println("... ... BRANCH DISPLAY INFO ... ...");
        System.out.println("Branch number: " + branchNumber);
        System.out.println("Manager " + (branchManager != null ? branchManager.getFullName() : " not identified!?!"));
        System.out.println("Cuostomers: " + customers.size() + " people\n");
    }

    public void displayCustomers(){
        System.out.println("Customers list:"+ branchNumber + ":");
        for (Customer cus : customers){
            System.out.println(cus.getFirstName() + " " + cus.getLastName() + "nationalCode:" + cus.getNationalCode());
        }
    }
}
