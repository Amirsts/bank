package bank;

import branch.Branch;
import loan.BaseLoan;
import person.Customer;
import person.Employee;
import person.Person;
import account.Account;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Bank {

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
                .mapToInt(Account::getBalance)
                .sum();
    }

    //reports
    public void log(String message){
        logs.add("'" + currentDate + "'" + message);
        System.out.println(message);
    }

    public List<String> getLogs(){
        return logs;
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
        System.out.println("Branches of "+ name +":");
        for (Branch b : branches){
            System.out.println("Branch: " + b.getBranchNumber());
            System.out.println("Manager: " + b.getBranchManager() != null ? b.getBranchManager().getFirstName() : "not modified ");
            System.out.println("Assistant manager: " + b.getAssistantManager() != null ? b.getAssistantManager().getFirstName() : "not modified");
            System.out.println("Tellers: "+ b.getTellers().size());
            System.out.println("---------------------------------");
        }
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
}
