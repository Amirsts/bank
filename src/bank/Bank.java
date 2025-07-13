package bank;

import branch.Branch;
import interfaces.FindAccount;
import loan.BaseLoan;
import person.*;
import account.Account;
import interfaces.Displayable;
import exceptions.*;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.security.SecureRandom;

import static java.lang.System.out;

public class Bank implements Displayable , FindAccount {

    public final String name = "Bit Bank";
    private List<Branch> branches;
    private List<Employee> employees;
    private List<Customer> customers;
    private List<Account> accounts;
    private List<String> logs;
    private LocalDate currentDate;
    private SecureRandom secureRandom;


    public Bank (){
        this.branches = new ArrayList<>();
        this.accounts = new ArrayList<>();
        this.customers = new ArrayList<>();
        this.employees = new ArrayList<>();
        this.logs = new ArrayList<>();
        this.currentDate =LocalDate.of(2025,6,2);
        this.secureRandom = new SecureRandom();
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

    public void addAccount(Account account){accounts.add(account);}

    public void removeAccount(String accountNumber){
        accounts.removeIf(acc -> acc.getAccountId().equals(accountNumber));
        out.println("Account" + accountNumber + "deleted from the branch's accounts");
    }

    public String accountNumberCreator(char accountType){
        long randomPart = (long) (secureRandom.nextDouble() * 1000_000_00000L);
        String randomPartStr =String.format("%011d" , randomPart);

        if (accountType == '1'){
            String randomAccountNumber = "01" + randomPartStr;
            return randomAccountNumber;
        }else if (accountType == '2'){
            String randomAccountNumber = "02" + randomPartStr;
            return randomAccountNumber;
        }else if (accountType == '3'){
            String randomAccountNumber = "03" + randomPartStr;
            return randomAccountNumber;
        }
        return null;
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
                .mapToInt(Account::getBalanceForBank)
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



    public void transferBetweenCustomers(String fromAccountNumber, String toAccountNumber, int amount, String password)
            throws AccountNotFoundException, IncorrectPasswordException, InvalidAmountException, InsufficientBalanceException, DailyTransferLimitExceededException {

        Account from = findAccount(fromAccountNumber);
        Account to = findAccount(toAccountNumber);

        if (from == null || to == null)
            throw new AccountNotFoundException("One of the accounts was not found.");

        LocalDate testDate = LocalDate.of(2025,6,9);
        from.recordTransfer(amount , testDate );

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

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }



    @Override
    public void displayInfo(){
        for (Branch branch : branches){
            branch.branchInfo();
        }
    }

    @Override
    public Account findAccount(String accountNumber) {
        for (Branch branch : branches) {
            for (Account a : branch.getAccounts()) {
                if (a.getAccountId().equals(accountNumber)) {
                    return a;
                }
            }
        }
        return null;
    }


}
