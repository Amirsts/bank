package bank;

import branch.Branch;
import loan.BaseLoan;
import person.*;
import account.Account;
import interfaces.Displayable;
import exceptions.*;


import javax.swing.plaf.PanelUI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Bank implements Displayable{

    private final String name = "Bit Bank";
    private List<Branch> branches;
    private List<Employee> employees;
    private List<Customer> customers;
    private List<String> logs;
    private LocalDate currentDate;

    public Bank (){
        this.branches = new ArrayList<>();
        this.employees = new ArrayList<>();
        this.customers = new ArrayList<>();
        this.logs = new ArrayList<>();
        this.currentDate =LocalDate.of(2025,6,2);
    }

    //add and collect

    public void addBranch(Branch branch){
        if (findBranch(branch.getBranchNumber()) != null ){
            System.out.println("A branch with this number is already registered.");
            return;
        }
        branches.add(branch);
        log("branch" + branch.getBranchNumber() + "registered.");
    }

    public void addEmployee(Employee employee){
        if (findEmployee(employee.getEmployeeId()) != null){
            System.out.println("Repetitive employee");
            return;
        }
        employees.add(employee);
        log("employee" + employee.getEmployeeId() + "added .");
    }

    public void addCustomer(Customer customer){
        if (findCustomer(customer.getCustomerId()) != null){
            System.out.println("Duplicate national code");
            return;
        }
        customers.add(customer);
        log("customer" + customer.getCustomerId() + "added");
    }
    //Uniqueness check
    public boolean isNationalIdDuplicate(String nationalId) {
        return customers.stream().anyMatch(c -> c.getNationalCode().equals(nationalId));
    }

    // ---- search----
    public Branch findBranch(String branchNumber){
        return branches.stream()
                .filter(b -> b.getBranchNumber().equals(branchNumber))
                .findFirst().orElse(null);
    }

    public Employee findEmployee(String employeeId){
        return employees.stream()
                .filter(e -> e.getEmployeeId().equals(employeeId))
                .findFirst().orElse(null);
    }

    public Customer findCustomer(String nationalId){
        return customers.stream()
                .filter(c -> c.getNationalCode().equals(nationalId))
                .findFirst().orElse(null);
    }

    // -----  balance of all accounts  -----
    public int getTotalBankBalance(){
        return branches.stream()
                .flatMap(b -> b.getAccounts().stream())
                .mapToInt(Account::getBalanceForbank)
                .sum();
    }

    //reports
    public List<Branch> getBranches(){
        return branches;
    }
    public void log(String message){
        logs.add("'" + currentDate + "'" + message);
        System.out.println(message);
    }

    public List<String> getLogs(){
        return logs;
    }


    //unique check unit

    public boolean isNationalCodeUnique(String nationalCode){
        for (Customer customer : customers){
            if (customer.getNationalCode().equals(nationalCode)){ return false;}
        }
        for (Employee employee : employees){
            if (employee.getNationalCode().equals(nationalCode)){return false;}
        }
        return true;
    }

    public boolean  isPhoneNumberUnique(String phoneNumber){
        for (Customer c : customers){
            if (c.getPhoneNum().equals(phoneNumber)){return false;}
        }

        for (Employee e : employees){
            if (e.getPhoneNum().equals(phoneNumber)){return false;}
        }
        return true;
    }

    public boolean isCustomerIdUnique(String customerId){
        for (Customer c : customers){
            if (c.getCustomerId().equals(customerId)){return false;}
        }
        return true;
    }

    public boolean isEmployeeIdUnique(String employeeId){
        for (Employee e :employees){
            if (e.getEmployeeId().equals(employeeId)){return false;}
        }
        return true;
    }

    public boolean isAccountIdUnique(String accountId){
        for (Branch branch:branches){
            for (Account a : branch.getAccounts()){
                if (a.getAccountId().equals(accountId)){return false;} //The nested for was inevitable , AmirMohammad is sad
            }
        }
        return true;
    }

    public boolean isBranchNumberUnique(String branchNumber){
        for (Branch b : branches){
            if (b.getBranchNumber().equals(branchNumber)){return false;}
        }
        return true;
    }

    public Account findAccountGlobal(String accountNumber) {
        for (Branch branch : branches) {
            for (Account a : branch.getAccounts()) {
                if (a.getAccountId().equals(accountNumber)) {
                    return a;
                }
            }
        }
        return null;
    }




    public void transferBetweenCustomers(String fromAccountNumber, String toAccountNumber, int amount, String password)
            throws AccountNotFoundException, IncorrectPasswordException, InvalidAmountException, InsufficientBalanceException {

        Account from = findAccountGlobal(fromAccountNumber);
        Account to = findAccountGlobal(toAccountNumber);

        if (from == null || to == null)
            throw new AccountNotFoundException("One of the accounts was not found.");

        from.secureWithdraw(amount, password);
        to.deposit(amount);

            System.out.println(" Successful transfer between customers: " + amount + " Tooman");
    }

    //time
    public LocalDate getCurrentDate() {
        return currentDate;
    }

    public void addedDays( int days){
        currentDate = currentDate.plusDays(days);
        log("time updated :"+ currentDate);
    }


    //display
    public void displayBranches(){
        System.out.println(",,, ,,, BANK DISPLAY INFO ,,, ,,,");
        System.out.println("Branches of "+ name +":\n");
        for (Branch b : branches){
            System.out.println("Branch: " + b.getBranchNumber());
            System.out.println("Manager: " + (b.getBranchManager() != null ? b.getBranchManager().getFullName() : "not modified "));
            System.out.println("Assistant manager: " + ( b.getAssistantManager() != null ? b.getAssistantManager().getFullName() : "not modified"));
            System.out.println("Tellers: "+( b.getTellers().size()));
            System.out.println("...........................");
        }
        System.out.println("------END OF BANK DISPLAY INFO------");
    }


    public void displayCustomers(){
        System.out.println("Bsnk customers: ");
        for (Customer c : customers){
            System.out.println(c.getFirstName() + " " + c.getLastName() + " " + c.getNationalCode() );
        }
    }

    //Get all active loans

    public List<BaseLoan> getAllActiveLoans() {
        List<BaseLoan> allLoans = new ArrayList<>();
        for (Customer c : customers) {
            allLoans.addAll(c.getActiveLoans());
        }
        return allLoans;
    }

    @Override
    public void displayInfo(){
        System.out.println("Number of branches: " + branches.size());
        System.out.println("Number of employees: " + employees.size());
        System.out.println("Number of customers: " + customers.size());
        System.out.println("Total bank balance: " + getTotalBankBalance());
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }
}
