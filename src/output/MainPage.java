package output;

import account.Account;
import account.CurrentAccount;
import account.QarzAlHasanehAccount;
import account.ShortTermAccount;
import bank.Bank;
import branch.Branch;
import branch.BranchManager;
import branch.AssistantManager;
import branch.Teller;
import loan.BaseLoan;
import loan.NormalLoan;
import loan.TashilatLoan;
import person.Customer;
import request.*;
import exceptions.*;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;


public class MainPage {
    public static void firstPage() {
        Scanner scanner = new Scanner(System.in);

        // Creating a bank and branches
        Bank bank = new Bank();

        Branch branch = new Branch("001");

        BranchManager branchManager = new BranchManager(
                "Ali", "Rezayi", "1990-01-01", "1234567890",
                "Tehran, Iran", "09121234567", "BM001" , "1234"
        );
        branch.setBranchManager(branchManager);
        bank.addEmployee(branchManager);
        bank.addBranch(branch);

        AssistantManager assistantManager = new AssistantManager("pouriya" , "Farahmand" , "1990-05-13" , "0986565654" , "Iran" ,"09896543298" , "pr1234"  , "1234");
        bank.addEmployee(assistantManager);
        branch.setAssistantManager(assistantManager);

        // Creating customers & teller for test
        Customer customer = new Customer(
                "Mobin", "Rangsaz", "1985-07-20", "1029384756",
                "Shiraz, Iran", "09134567890", "mo1234"
        );
        bank.addCustomer(customer);
        branch.addCustomer(customer);

        Customer customer1 = new Customer(
                "Amirmohammad", "Mohammadi", "1985-07-20", "1029384756",
                "Shiraz, Iran", "09134567890", "1"
        );
        bank.addCustomer(customer1);
        branch.addCustomer(customer1);

        // Creating Account for customer
        Teller teller = new Teller("Ali","asghari","1986-12-20", "0965656654","mashad" , "09064563232","2", "2");
        bank.addEmployee(teller);
        branch.addTeller(teller);



        // Main menu
        boolean exitSystem = false;
        Branch currentBranch = branch; // Input Branch (several usage)
        while (!exitSystem) {
            System.out.println("\n--- " + bank.name + " ---\nMain Menu: ");

            String[] menuItems = {
                    "1. Customer Operations (Customer Login)",
                    "2. Teller",
                    "3. Assistant Manager",
                    "4. Branch Manager",
                    "5. Bank Manager",
                    "6. Change Branch",
                    "7. Register New Customer",
                    "8. Exit System"
            };

            for (String item : menuItems) {
                System.out.println(item);
            }

            System.out.print("Your choice: ");

            int mainChoice = scanner.nextInt();
            scanner.nextLine(); // Cleaning new line

            switch (mainChoice) {
                case 1:
                    Customer selectedCustomer = selectCustomer(scanner, bank); // Customer's enter point
                    if (selectedCustomer != null) {
                        processCustomer(scanner, selectedCustomer, currentBranch, bank); // Customer's menu
                    }
                    break;
                case 2:
                    Teller selectedTeller = selectTeller(scanner , branch); // Teller's enter point
                    if (selectedTeller != null) {
                        processTeller(scanner,selectedTeller , currentBranch ,bank ); // Teller's menu
                    }
                    break;
                case 3:
                    processAssistantManager(scanner, currentBranch ); // AssistantManager's menu
                    break;
                case 4:
                    processManager(scanner, currentBranch, customer , bank); // BranchManager's menu
                    break;
                case 5:
                    processBankManager(scanner, currentBranch , bank); // BankManager's menu
                    break;
                case 6:
                    currentBranch = selectBranch(scanner, bank); // Choosing a branch
                    System.out.println("The current branch has been changed to: " + currentBranch.getBranchNumber());
                    break;
                case 7:
                    processNewCustomer(scanner, bank , branch); // Creating a new customer
                    break;
                case 8:
                    exitSystem = true; // Exit point
                    System.out.println("Log out. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
        scanner.close();
        }



    // Process of choosing a branch
    public static Branch selectBranch(Scanner scanner, Bank bank) {
        List<Branch> branches = bank.getBranches(); //Creating a list of branches of Bank
            System.out.println("Please select your desired branch: ");
        for (int i = 0; i < branches.size(); i++) {
            System.out.println((i + 1) + "Branch number" + branches.get(i).getBranchNumber()); // Print all BRanches number
        }
        System.out.print("Your choice: ");
        int branchChoice = scanner.nextInt(); // chose one Branch
        scanner.nextLine();

        if (branchChoice >= 1 && branchChoice <= branches.size()) {
            return branches.get(branchChoice - 1);
        } else {
            System.out.println("Invalid selection. Default branch is selected.");
            return branches.get(0);
        }
    }



    // Customer selection among bank customers
    static Customer selectCustomer(Scanner scanner, Bank bank) {
        List<Customer> customers = bank.getCustomers(); // Creating a list from customers of bank
        if (customers.isEmpty()) {
            System.out.println("There are no registered customers.");
            return null;
        }
            System.out.println("Please enter your Customer ID: ");
        String cID = scanner.nextLine();

        for (int i = 0; i < customers.size(); i++) {
           Customer tempC = customers.get(i);

           if (tempC.getCustomerId().equals(cID)){
               return customers.get(i);
           }
        }
        System.out.println("Customer not found, please try again.");
        return null;
    }



    // Teller selection among tellers of branch
    static Teller selectTeller(Scanner scanner, Branch branch) {
        List<Teller> tellers = branch.getTellers(); // Creating a list from tellers of branch
        if (tellers.isEmpty()) {
            System.out.println("There is no registered custodian.");
            return null;
        }
        System.out.println("Please enter your Teller ID: ");
        String tID = scanner.nextLine();
        System.out.println("Please enter your password: ");
        String tPass = scanner.nextLine();

        for (int i = 0; i < tellers.size(); i++) {
            Teller tempT = tellers.get(i);

            if (tempT.getEmployeeId().equals(tID) && tempT.isPassWordTrue(tPass)){
                return tellers.get(i);
            }
        }
        System.out.println("Delivery person not found, please try again.");
        return null;
    }

    // Customer menu & process
    static void processCustomer(Scanner scanner, Customer customer, Branch currentBranch, Bank bank) {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n--- Customer Menu (" + customer.getFullName() + ") ---");

            String[] customerMenuItems = {
                    "1. Open New Account",
                    "2. Transfer Funds",
                    "3. Request a Loan",
                    "4. Pay Loan Installments",
                    "5. View Messages",
                    "6. Show My Account Balances",
                    "7. Close an Account",
                    "8. Return to Main Menu"
            };

            for (String item : customerMenuItems) {
                System.out.println(item);
            }

            System.out.print("Your choice: "); // choosing operations
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch(choice) {

                case 1:
                    System.out.println("Submit an account request...");
                    System.out.println("Enter account type:" + "\n1.Current account" + "\n2.Short-term account" + "\n3.Qarz al-Hasanah account");
                    String text = scanner.nextLine(); // choosing account type

                    while (!(text.charAt(0) == '1' ||text.charAt(0) == '2' ||text.charAt(0) == '3')){
                        System.out.println("Please enter only the number of the desired option: ");
                        text = scanner.nextLine();
                    }

                    Request openAccountRequest = new Request(RequestType.OPEN_ACCOUNT, text, customer); // Creating a new account request
                    customer.getMessageBox().addRequest(openAccountRequest); // Add the new account request in the customer's messagebox
                    currentBranch.getSolitudeTeller().receiveRequest(openAccountRequest); // Add the new account request in the teller's messagebox
                    System.out.println("Your account creation request has been registered.");
                    break;

                case 2:
                    System.out.println("Transfer funds between your accounts...");
                    System.out.print("Originating account number: ");
                    String fromAccount = scanner.nextLine(); // Receive inputs for the money transfer method
                    System.out.print("Destination account number: ");
                    String toAccount = scanner.nextLine();
                    String name = bank.findAccount(toAccount).getOwner().getFullName();
                    System.out.print("Destination customer is: " + name + "\nTransfer amount: ");
                    int amount = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("Enter the payment date, for example (07/05/2025):");
                    String inp = scanner.nextLine();
                    DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDate dateTransfer = LocalDate.parse(inp, format);
                    System.out.print("Password: ");
                    String password = scanner.nextLine();
                    try {
                        bank.transferBetweenCustomers(fromAccount, toAccount, amount, password , dateTransfer); // Assigning values
                        System.out.println("The transfer was successful.");
                    } catch (Exception e) {
                        System.out.println("Error in money transfer: " + e.getMessage());
                    }
                    break;

                case 3:
                    System.out.println("Send a loan request...");
                    System.out.println("\nSelect loan type:" + "\n1.Regular loan" + "\n2.Facility loan");
                    String loanType = scanner.nextLine();
                    while (!(loanType.charAt(0) == '1' ||loanType.charAt(0) == '2')){
                        System.out.println("Please enter only the desired option number: ");
                        loanType = scanner.nextLine();
                    }

                    System.out.println("Your accounts: ");
                    List<Account> accounts1 = customer.getAccounts();  // Checking customer accounts
                    for (int i = 0 ; i < accounts1.size() ; i++){
                        Account account = accounts1.get(i);
                        System.out.println( (i + 1) + "." + account.getAccountId());
                    }

                    System.out.println("Please select the desired account: "); // Choosing an account
                    int slcAccount = scanner.nextInt();
                    while (slcAccount > accounts1.size()){
                        System.out.println("Please enter only the desired option number: ");
                        slcAccount = scanner.nextInt();
                    }

                    System.out.println("Enter the loan amount requested:"); // Getting loan amount
                    double loanAmount = scanner.nextDouble();
                    // Assigning values
                    Request loanRequest = new Request(RequestType.LOAN_REQUEST,customer , loanType ,accounts1.get((slcAccount - 1)).getAccountId(), loanAmount);
                    customer.getMessageBox().addRequest(loanRequest);
                    currentBranch.getSolitudeTeller().receiveRequest(loanRequest);
                    System.out.println("Your loan application has been registered.");
                    break;

                case 4:
                    if (customer.getActiveLoans().isEmpty()){
                        System.out.println("You do not have an active loan.");
                    }else {
                        // Show loan details
                        BaseLoan loan = customer.getActiveLoans().get(0);
                        System.out.println(MessageFormat.format("Loan Details:\nTotal Loan Amount:{0}\nTotal Amount Repaid by Customer:{1}\nTotal Amount Remaining in Installments:{2}\nNumber of Remaining Installments:{3} Installment Amount:{4}",
                                ((int) loan.getLoanAmount()), ((int) loan.getPaidAmount()), ((int) loan.getRemainingAmount()), loan.getfDuration(), (int) loan.installmentPerMonth()));

                        System.out.println("1. Installment Payment" + "\n2. Return" + "\nYour choice:");
                        int chose = scanner.nextInt();
                        scanner.nextLine();

                        switch (chose) {
                            case 1:
                                System.out.println("Enter the payment date, for example (07/05/2025):");
                                String input = scanner.nextLine();
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                                LocalDate datePay = LocalDate.parse(input, formatter);


                                System.out.println("Enter your account number:");
                                String accountNumber = scanner.nextLine();
                                System.out.println("Please enter your password:");
                                String accountPassWord = scanner.nextLine();
                                Account cAccount = customer.findAccount(accountNumber);
                                try {
                                    cAccount.secureWithdrawForLoan((int) loan.installmentPerMonth(), accountPassWord); // Paying monthly installment
                                } catch (IncorrectPasswordException e) {
                                    System.out.println(e.getMessage());
                                } catch (InvalidAmountException e) {
                                    System.out.println(e.getMessage());
                                } catch (InsufficientBalanceException e) {
                                    System.out.println(e.getMessage());
                                }

                                //decrease amount from loan & date of last time pay
                                loan.pay(loan.installmentPerMonth() , datePay);
                                loan.payInstallment();


                                break;
                            case 2:
                                break;
                            default:
                                break;
                        }

                    }
                    break;
                case 5:
                    System.out.println("Display customer requests:");
                    customer.getMessageBox().printAll();
                    break;
                case 6:
                    System.out.println("View account balances " + customer.getFullName() + ":");
                    System.out.print("Enter the date (e.g. 05/07/2025):");
                    String input = scanner.nextLine();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDate dateCheckAmount = LocalDate.parse(input, formatter);


                    // It is assumed that Customer has a getAccounts() method that returns a list of accounts.
                    List<Account> accounts = customer.getAccounts();
                    List<ShortTermAccount> shortTermAccounts = customer.getShortTermAccounts();
                    if (accounts == null || accounts.isEmpty()) {
                        System.out.println("No account has been registered for this customer.");
                    } else {
                        for (Account accItem : accounts) {
                            if (accItem.getAccountId().startsWith("02")) { // These accounts aren't Profited than we continue
                                continue;
                            }
                            System.out.println("Account number: " + accItem.getAccountId() + " - balance: " + accItem.getBalance());
                        }
                        for (ShortTermAccount shortTermAccount :shortTermAccounts) {
                            shortTermAccount.profitCheck(dateCheckAmount); // Checking short term accounts for monthly profit
                            System.out.println("Account number: " + shortTermAccount.getAccountId() + " - balance: " + shortTermAccount.getBalance());
                        }
                    }
                    break;

                case 7:
                    System.out.println("Send account closure request..." + "\nEnter the desired account number:" );
                    String acNumber = scanner.nextLine();
                    if (customer.findAccount(acNumber) == null ){
                        System.out.println("The requested account is not valid.");
                        break;
                    }
                    Request closeAccountRequest = new Request(RequestType.CLOSE_ACCOUNT, acNumber, customer); // Assigning values
                    customer.getMessageBox().addRequest(closeAccountRequest);
                    currentBranch.getSolitudeTeller().receiveRequest(closeAccountRequest);
                    System.out.println("Your account closure request has been registered.");
                    break;

                case 8:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    // New customer registration method (customer registration via the main menu)
    static void processNewCustomer(Scanner scanner, Bank bank , Branch branch) {
        try {
            System.out.println("\n--- New Customer Registration ---");
            System.out.print("First name: ");
            String firstName = scanner.nextLine();
            System.out.print("Last name: ");
            String lastName = scanner.nextLine();
            System.out.print("Date of birth (YYYY-MM-DD): ");
            String birthDate = scanner.nextLine();
            System.out.print("National Code: ");
            String nationalCode = scanner.nextLine();

            while (!bank.isNationalCodeUnique(nationalCode)) {
                System.out.print("The national code entered is duplicate\n" + "Enter the national code: ");
                nationalCode = scanner.nextLine();
            }

            System.out.print("Address: ");
            String address = scanner.nextLine();
            System.out.print("Phone number: ");
            String phone = scanner.nextLine();

            while (!bank.isPhoneNumberUnique(phone)) {
                System.out.println("The phone number entered is a duplicate\n" + "Enter phone number: ");
                phone = scanner.nextLine();
            }

            System.out.print("Customer ID: ");
            String customerId = scanner.nextLine();
            while (! bank.isCustomerIdUnique(customerId) ) {
                System.out.print("Your Customer ID is repetitive enter Customer ID again: ");
                customerId = scanner.nextLine();
            }

            Customer newCustomer = new Customer(firstName, lastName, birthDate, nationalCode, address, phone, customerId);
            bank.addCustomer(newCustomer);
            branch.addCustomer(newCustomer);
            System.out.println("A new customer with ID " + customerId + " was registered.");
        }catch (InvalidNationalCodeException e){
            System.out.println(e.getMessage());
        }catch (InvalidPhoneNumberException e){
            System.out.println(e.getMessage());
        }
    }

    // Branch Assistant Menu: Review account and loan closure requests, display branch information
    static void processAssistantManager(Scanner scanner, Branch curentBranch) {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n--- Assistant Manager Menu ---");

            String[] assistantMenuItems = {
                    "1. Review Loan Requests",
                    "2. View Branch Information",
                    "3. Return to Main Menu"
            };

            for (String item : assistantMenuItems) {
                System.out.println(item);
            }

            System.out.print("Your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();
            switch(choice) {

                case 1:
                    List<Request> loanRequests = curentBranch.getAssistantManager().getMessageBox().getRequestsByType(RequestType.LOAN_REQUEST);
                    if (loanRequests.isEmpty()) {
                        System.out.println("No pending loan applications found.");
                    } else {
                        System.out.println("Customers waiting to receive a loan");
                        for (int i = 0 ; i < loanRequests.size() ; i++){
                            Request tmpLR = loanRequests.get(i);
                            System.out.println((i + 1)  + "." +tmpLR.getSender().getFullName());
                        }
                        System.out.println("Your choice:");
                        int chose = scanner.nextInt();
                        Customer selectedCustomer = loanRequests.get(chose -1).getSender();
                        Request selectedRequest = curentBranch.getAssistantManager().getMessageBox().getRequestsByType(RequestType.LOAN_REQUEST).get(chose-1);
                        Customer slcCustomer = selectedRequest.getSender();

                        if (curentBranch.getAssistantManager().isEligibleForLoan(selectedCustomer)){
                            System.out.println("Customer does not have an active loan." + "\nLoan request sent to branch manager");
                            curentBranch.getBranchManager().receiveRequest(selectedRequest);
                            curentBranch.getAssistantManager().clearMessageBox(selectedRequest);
                            selectedRequest.setStatus("Your loan application has been sent to the Branch Manager. || Assistant Manager: " + curentBranch.getAssistantManager().getFullName());
                        }else {
                            System.out.println("Customer has an active loan:");
                            curentBranch.getAssistantManager().clearMessageBox(selectedRequest);
                            selectedRequest.setStatus("Your request REJECTED ,Yuo have active loan. || Assistant Manager: " + curentBranch.getAssistantManager().getFullName());
                        }
                    }
                    break;
                case 2:
                    curentBranch.displayInfo();
                    break;
                case 3:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    // Teller menu
    static void processTeller(Scanner scanner , Teller selectedTeller , Branch curentBranch , Bank bank ) {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n--- Teller Menu --- " + selectedTeller.getFullName() + " ---");

            String[] tellerMenuItems = {
                    "1. Process Deposit/Withdrawal",
                    "2. Forward Loan Request to Assistant Manager",
                    "3. Approve Account Opening Request",
                    "4. Approve Account Closure Request",
                    "5. Return to Main Menu"
            };

            for (String item : tellerMenuItems) {
                System.out.println(item);
            }

            System.out.print("Your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();
            switch(choice) {
                case 1:
                    System.out.println("Select operation type: 1. Deposit 2. Withdrawal");
                    int op = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Account number: ");
                    String accNum = scanner.nextLine();
                    System.out.print("Amount: ");
                    int amt = scanner.nextInt();
                    scanner.nextLine();
                    CurrentAccount acc = (CurrentAccount) curentBranch.findAccount(accNum);
                    if (acc == null) {
                        System.out.println("The requested account was not found.");
                    } else {
                        if (op == 1) {
                            acc.deposit(amt);
                            System.out.println("Successful deposit. New balance: " + acc.getBalanceForBank());
                        } else if (op == 2) {
                            System.out.print("Password: ");
                            String pwd = scanner.nextLine();
                            try {
                                acc.secureWithdraw(amt, pwd);
                            } catch (IncorrectPasswordException | InvalidAmountException | InsufficientBalanceException ex) {
                                System.out.println("Error in collection: " + ex.getMessage());
                            }
                        } else {
                            System.out.println("Invalid operation.");
                        }
                    }
                    break;
                case 2:
                    List<Request> loanRequests = selectedTeller.getMessageBox().getRequestsByType(RequestType.LOAN_REQUEST);
                    if (loanRequests.isEmpty()){
                        System.out.println("No customer found in loan queue");
                        break;
                    }else {
                        System.out.println("Customers waiting to receive a loan");
                        for (int i = 0 ; i < loanRequests.size() ; i++){
                            Request tmpLR = loanRequests.get(i);
                            System.out.println((i + 1)  + "." +tmpLR.getSender().getFullName());
                        }
                        System.out.println("Your choice:");
                        int chose = scanner.nextInt();
                        Request selectedRequest = selectedTeller.getMessageBox().getRequestsByType(RequestType.LOAN_REQUEST).get(chose-1);
                        selectedTeller.clearMessageBox(selectedRequest);
                        curentBranch.getAssistantManager().receiveRequest(selectedRequest);
                            selectedRequest.setStatus("Request referred to Assistant Manager || Teller: "+ selectedTeller.getFullName());
                        System.out.println("Request referred to the Assistant manager");

                    }
                    break;
                case 3:

                   List<Request> opRequests = selectedTeller.getMessageBox().getRequestsByType(RequestType.OPEN_ACCOUNT);
                   if (opRequests.isEmpty()){
                       System.out.println("No customer found in the waiting queue for Account opening");
                       break;
                       }else {
                            System.out.println("Customers waiting for account opening: ");
                            for (int i = 0 ; i < opRequests.size() ; i++){
                                Request tmpR = opRequests.get(i);
                                System.out.println((i + 1)  + "." +tmpR.getSender().getFullName());
                            }
                            System.out.println("Your choice:");
                            int chose = scanner.nextInt();
                            Customer selectedCustomer = opRequests.get(chose -1).getSender();
                            Request selectedRequest = selectedTeller.getMessageBox().getRequestsByType(RequestType.OPEN_ACCOUNT).get(chose-1);

                            System.out.println("Client Message: " + selectedRequest.getMessage() + "\n" + "Account opening operation in progress...");

                            System.out.print("Please enter the initial deposit amount: ");
                            long initialDeposit = scanner.nextLong();
                            scanner.nextLine();
                            System.out.print("Please enter the account password: ");
                            String accountPassword = scanner.nextLine();

                            selectedTeller.clearMessageBox(selectedRequest);

                            char accountType = selectedRequest.getMessage().charAt(0);
                            String newAccountNumber = bank.accountNumberCreator(accountType);
                            if (accountType == '1'){

                                CurrentAccount newAccount = new CurrentAccount(newAccountNumber , selectedCustomer , initialDeposit , accountPassword);
                                bank.addAccount(newAccount);
                                curentBranch.addAccount(newAccount);
                                selectedCustomer.openAccount(newAccount);
                                selectedRequest.setStatus("Dear Customer: " + selectedCustomer.getFullName() +" || Your current account with password: " + accountPassword + " || Account Number: " + newAccountNumber + " has been opened");
                                System.out.print("Current account " + selectedCustomer.getFullName() + " opened");
                            }else if (accountType == '2'){
                                System.out.println("Enter the date, for example (07/05/2025):");
                                String input = scanner.nextLine();
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                                LocalDate dateOpenAccount = LocalDate.parse(input , formatter);

                                ShortTermAccount newAccount = new ShortTermAccount(newAccountNumber , selectedCustomer , initialDeposit , accountPassword , dateOpenAccount);
                                bank.addAccount(newAccount);
                                curentBranch.addAccount(newAccount);
                                selectedCustomer.openAccount(newAccount);
                                selectedCustomer.openShortTermAccount(newAccount);
                                selectedRequest.setStatus("Dear Customer: " + selectedCustomer.getFullName() +" || Your short-term account with password: " + accountPassword + " || Account Number: " + newAccountNumber + " has been opened");
                                System.out.print("Short-term account " + selectedCustomer.getFullName() + "opened");
                            }else if (accountType == '3') {
                                QarzAlHasanehAccount newAccount = new QarzAlHasanehAccount(newAccountNumber , selectedCustomer , initialDeposit , accountPassword);
                                bank.addAccount(newAccount);
                                curentBranch.addAccount(newAccount);
                                selectedCustomer.openAccount(newAccount);
                                selectedRequest.setStatus("Dear Customer: " + selectedCustomer.getFullName() +" || Your Qarz Al-Hasanah account with password: " + accountPassword + " || Account Number: " + newAccountNumber + " has been opened");
                                System.out.print("Qarz Al-Hasanah Account " + selectedCustomer.getFullName() + " Opened");
                            }
                       }
                    break;
                case 4:
                    List<Request> clRequest = selectedTeller.getMessageBox().getRequestsByType(RequestType.CLOSE_ACCOUNT);
                    if (clRequest.isEmpty()){
                        System.out.println("No customer found in the waiting queue");
                        break;
                    }else {
                        System.out.println("Customers waiting for account closing");
                        for (int i = 0 ; i < clRequest.size() ; i++){
                            Request tmpR = clRequest.get(i);
                            System.out.println((i + 1)  + "." +tmpR.getSender().getFullName());
                        }
                        System.out.println("Your choice:");
                        int chose = scanner.nextInt();
                        Customer selectedCustomer = clRequest.get(chose -1).getSender();
                        Request selectedRequest = selectedTeller.getMessageBox().getRequestsByType(RequestType.CLOSE_ACCOUNT).get(chose-1);
                        String accountID = selectedRequest.getMessage();

                        //checking Customer has active loan
                        if (selectedCustomer.hasActiveLoan()){
                            System.out.println("Customer has an active loan");
                            selectedRequest.setStatus("Dear customer, you have an active loan. Your account cannot be closed.|| Teller: "+ selectedTeller.getFullName());
                            selectedTeller.clearMessageBox(selectedRequest);
                            break;
                        }
                        System.out.println("Customer has no active loan, account closed successfully");
                        selectedCustomer.removeAccount(accountID);
                        curentBranch.removeAccount(accountID);
                        bank.removeAccount(accountID);
                        selectedRequest.setStatus("Your account with number: " + accountID + " has been successfully closed.|| Teller: "+ selectedTeller.getFullName());
                        selectedTeller.clearMessageBox(selectedRequest);
                    }
                    break;

                case 5:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    // Branch Manager Menu: Approve or reject pending requests and view overall branch performance
    static void processManager(Scanner scanner, Branch curentBranch, Customer customer ,Bank bank) {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n--- Branch Manager Menu ---");

            String[] managerMenuItems = {
                    "1. View Loan Requests",
                    "2. View Overall Branch Performance",
                    "3. Return to Main Menu"
            };

            for (String item : managerMenuItems) {
                System.out.println(item);
            }

            System.out.print("Your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();
            switch(choice) {
                case 1:
                    List<Request> mRequests = curentBranch.getBranchManager().getMessageBox().getRequestsByType(RequestType.LOAN_REQUEST);
                    if (mRequests.isEmpty()) {
                        System.out.println("No pending requests found.");
                    } else {
                        System.out.println("Customers waiting to receive a loan");
                        for (int i = 0 ; i < mRequests.size() ; i++){
                            Request tmpLR = mRequests.get(i);
                            System.out.println((i + 1)  + "." +tmpLR.getSender().getFullName());
                        }
                        System.out.println("Your choice: ");
                        int chose = scanner.nextInt();
                        scanner.nextLine();
                        Customer selectedCustomer = mRequests.get(chose -1).getSender();
                        Request selectedRequest = curentBranch.getBranchManager().getMessageBox().getRequestsByType(RequestType.LOAN_REQUEST).get(chose-1);
                        int loanType = Integer.parseInt(selectedRequest.getMessage());

                        //Selecting customer's account for loan
                        Account slcAccount = selectedCustomer.findAccount(selectedRequest.getAccountNumber());

                        switch (loanType) {
                            case 1:
                                if ( (selectedRequest.getLoanAmount() > selectedCustomer.getNormalLoanCeiling()) ) {//The loan amount was checked to ensure it did not exceed the loan ceiling.
                                    System.out.println("your loan amount is more than Normal loan ceiling");
                                    selectedRequest.setStatus("your request REJECTED:  your loan amount is more than Normal loan ceiling || Branch Manager: " + curentBranch.getBranchManager().getFullName());
                                    curentBranch.getBranchManager().clearMessageBox(selectedRequest);
                                    break;
                                }
                                if ((selectedRequest.getLoanAmount() < curentBranch.getCurrentShortTermBalance())) {//The bank was checked to ensure it had sufficient financial resources.

                                    System.out.println("Enter the number of monthly installments:");
                                    int duration = scanner.nextInt();
                                    scanner.nextLine();
                                    System.out.print("Enter the date (e.g. 05/07/2025): ");
                                    String input = scanner.nextLine();
                                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                                    LocalDate date = LocalDate.parse(input, formatter);

                                    NormalLoan normalLoan = new NormalLoan(selectedRequest.getLoanAmount() , duration , date ,selectedCustomer);
                                    selectedCustomer.addLoan(normalLoan);
                                    slcAccount.deposit((int) selectedRequest.getLoanAmount());
                                    selectedRequest.setStatus("Dear Customer, your normal loan request has been approved and the amount: (" +((int) selectedRequest.getLoanAmount()) + ")has been deposited into your account\n" +
                                            "You are required to repay the loan during the: " + duration + "months || In each installment of: " + ((int) normalLoan.installmentPerMonth()) + "");
                                    System.out.println("Loan credited to customer's account: " + ((int) selectedRequest.getLoanAmount()));
                                    curentBranch.getBranchManager().clearMessageBox(selectedRequest);

                                }else {
                                    System.out.println("Bit bank balance  of midterm & short term accounts is not enough");
                                    selectedRequest.setStatus("your request REJECTED:  Bit bank balance  of midterm & short term accounts is not enough. || Branch Manager: " + curentBranch.getBranchManager().getFullName());
                                    curentBranch.getBranchManager().clearMessageBox(selectedRequest);
                                }
                                break;

                            case 2:
                                if ((selectedRequest.getLoanAmount() > selectedCustomer.getTashilatCeiling()) ) {  //The loan amount was checked to ensure it did not exceed the loan ceiling.
                                    System.out.println("your loan amount is more than Tashilat loan ceiling");
                                    selectedRequest.setStatus("your request REJECTED:  your loan amount is more than Tashilat loan ceiling. || Branch Manager: " + curentBranch.getBranchManager().getFullName());
                                    curentBranch.getBranchManager().clearMessageBox(selectedRequest);
                                    break;
                                }
                                if ((selectedRequest.getLoanAmount() < curentBranch.getQarzAlhasanehBalance())) { //The bank was checked to ensure it had sufficient financial resources.

                                    System.out.println("Enter the number of monthly installments:");
                                    int duration = scanner.nextInt();
                                    scanner.nextLine();
                                    System.out.print("Enter the date (e.g. 05/07/2025): ");
                                    String input = scanner.nextLine();
                                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                                    LocalDate date = LocalDate.parse(input, formatter);

                                    TashilatLoan tashilatLoan = new TashilatLoan(selectedRequest.getLoanAmount() , duration , date ,selectedCustomer);
                                    selectedCustomer.addLoan(tashilatLoan);
                                    slcAccount.deposit((int) selectedRequest.getLoanAmount());
                                    selectedRequest.setStatus("Dear Customer, your tashilat loan request has been approved and the amount: (" +((int) selectedRequest.getLoanAmount()) + ")has been deposited into your account\n" +
                                            "You are required to repay the loan during the: " + duration + "months || In each installment of: " + ((int) tashilatLoan.installmentPerMonth()) + "");
                                    System.out.println("Loan credited to customer's account: " + ((int) selectedRequest.getLoanAmount()));
                                    curentBranch.getBranchManager().clearMessageBox(selectedRequest);

                                }else {
                                    System.out.println("Bit bank balance  of Qarzalhasaneh accounts is not enough");
                                    selectedRequest.setStatus("your request REJECTED:  Bit bank balance  of Qarzalhasaneh accounts is not enough|| Branch Manager: " + curentBranch.getBranchManager().getFullName());
                                    curentBranch.getBranchManager().clearMessageBox(selectedRequest);
                                }
                                break;

                            default:
                                System.out.println("Invalid option.");
                        }
                    }
                    break;
                case 2:
                    curentBranch.displayInfo();
                    break;
                case 3:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    // Bank manager menu: Create cashier and branch assistant
    static void processBankManager(Scanner scanner, Branch branch ,Bank bank) {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n--- Bank Manager Menu ---");

            String[] directorMenuItems = {
                    "1. Create New Teller",
                    "2. Create New Branch Assistant",
                    "3. Create New Branch Manager",
                    "4. Create New Branch",
                    "5. View Bank Information",
                    "6. Return to Main Menu"
            };

            for (String item : directorMenuItems) {
                System.out.println(item);
            }

            System.out.print("Your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch(choice) {
                case 1:
                    try{
                        System.out.println("Creating a new recipient...");
                        System.out.print("Teller ID: ");
                        String tellerId = scanner.nextLine();
                        while (!bank.isEmployeeIdUnique(tellerId)){
                            System.out.print( "The Employee ID entered is duplicate\n" + "Enter the Employee ID: ");
                            tellerId = scanner.nextLine();
                        }

                        System.out.print("First name: ");
                        String tellerFirstName = scanner.nextLine();
                        System.out.print("Last name: ");
                        String tellerLastName = scanner.nextLine();
                        System.out.print("Birthday: ");
                        String tellerBirthDay = scanner.nextLine();
                        System.out.print("National Code: ");
                        String tellerNationalCode = scanner.nextLine();
                        while (!bank.isNationalCodeUnique(tellerNationalCode)){
                            System.out.print( "The national code entered is duplicate\n" + "Enter the national code: ");
                            tellerNationalCode = scanner.nextLine();
                        }

                        System.out.print("Address: ");
                        String tellerAddress = scanner.nextLine();
                        System.out.print("Phone Number: ");
                        String tellerPhone = scanner.nextLine();
                        while (!bank.isPhoneNumberUnique(tellerPhone)){
                            System.out.println("The phone number entered is a duplicate\n" + "Enter phone number: ");
                            tellerPhone = scanner.nextLine();
                        }

                        System.out.println("Password: ");
                        String passWord = scanner.nextLine();

                        Teller newTeller = new Teller(tellerFirstName, tellerLastName, tellerBirthDay,
                                tellerNationalCode, tellerAddress, tellerPhone, tellerId, passWord);
                        branch.addTeller(newTeller);
                        bank.addEmployee(newTeller);
                        System.out.println("New teller with ID " + tellerId + " was created.");
                    }catch (InvalidNationalCodeException e){
                        System.out.println( e.getMessage());
                    }catch (InvalidPhoneNumberException e){
                        System.out.println( e.getMessage() );
                    }
                    break;
                case 2:
                    try {

                        System.out.println("Creating a new branch assistant...");
                        System.out.print("Branch Deputy ID: ");
                        String amId = scanner.nextLine();
                        while (!bank.isEmployeeIdUnique(amId)){
                            System.out.print( "The Employee ID code entered is duplicate\n" + "Enter the Employee ID: ");
                            amId = scanner.nextLine();
                        }

                        System.out.print("First name: ");
                        String amFirstName = scanner.nextLine();
                        System.out.print("Last name: ");
                        String amLastName = scanner.nextLine();
                        System.out.print("Birthday: ");
                        String amBirthDay = scanner.nextLine();
                        System.out.print("National Code: ");
                        String amNationalCode = scanner.nextLine();
                        while (!bank.isNationalCodeUnique(amNationalCode)) {
                            System.out.print("The entered national code is duplicate\n" + "Enter the national code: ");
                            amNationalCode = scanner.nextLine();
                        }

                        System.out.print("Address: ");
                        String amAddress = scanner.nextLine();
                        System.out.print("Phone number: ");
                        String amPhone = scanner.nextLine();

                        while (!bank.isPhoneNumberUnique(amPhone)) {
                            System.out.println("The phone number entered is a duplicate\n" + "Enter the phone number: ");
                            amPhone = scanner.nextLine();
                        }
                        System.out.println("Password: ");
                        String passWord = scanner.nextLine();

                        AssistantManager newAM = new AssistantManager(amFirstName, amLastName, amBirthDay,
                                amNationalCode, amAddress, amPhone, amId , passWord);
                        branch.setAssistantManager(newAM);
                        bank.addEmployee(newAM);
                        System.out.println("New branch assistant created with ID " + amId + ".");
                    }catch (InvalidNationalCodeException e){
                        System.out.println(e.getMessage());
                    }catch (InvalidPhoneNumberException e){
                        System.out.println(e.getMessage());
                    }
                    break;
                case 3:
                    try {
                    System.out.println("Creating a new branch manager...");
                    System.out.print("Branch Manager ID: ");
                    String bmId = scanner.nextLine();
                    while (!bank.isEmployeeIdUnique(bmId)){
                        System.out.print( "The Employee ID entered is duplicate\n" + "Enter the Employee ID: ");
                        bmId = scanner.nextLine();
                    }

                    System.out.print("First name: ");
                    String amFirstName = scanner.nextLine();
                    System.out.print("Last name: ");
                    String amLastName = scanner.nextLine();
                    System.out.print("Birthday: ");
                    String amBirthDay = scanner.nextLine();
                    System.out.print("National Code: ");
                    String amNationalCode = scanner.nextLine();

                    while (!bank.isNationalCodeUnique(amNationalCode)) {
                        System.out.print("The entered national code is duplicate\n" + "Enter the national code: ");
                        amNationalCode = scanner.nextLine();
                    }

                    System.out.print("Address: ");
                    String amAddress = scanner.nextLine();
                    System.out.print("Phone number: ");
                    String amPhone = scanner.nextLine();

                    while (!bank.isPhoneNumberUnique(amPhone)) {
                        System.out.println("The phone number entered is a duplicate\n" + "Enter the phone number: ");
                        amPhone = scanner.nextLine();
                    }
                    System.out.println("Password");
                    String passWord = scanner.nextLine();

                    BranchManager Bmanager = new BranchManager(amFirstName, amLastName, amBirthDay,
                            amNationalCode, amAddress, amPhone, bmId , passWord);
                    branch.setBranchManager(Bmanager);
                    bank.addEmployee(Bmanager);
                        System.out.println("New branch assistant created with ID " + bmId + ".");
            }catch (InvalidNationalCodeException e){
                System.out.println(e.getMessage());
            }catch (InvalidPhoneNumberException e){
                System.out.println(e.getMessage());
            }
                    break;
                case 4:
                    System.out.println("Enter the branch number:");
                    String branchNumber = scanner.nextLine();
                    if ( !bank.isBranchNumberUnique(branchNumber)){
                        System.out.println("The branch number is duplicate, try again:");
                        branchNumber = scanner.nextLine();
                    }
                    Branch branch1 = new Branch(branchNumber);
                    System.out.println("Branch with number: " + branchNumber + "created");
                    break;
                case 5:
                    bank.displayInfo();
                    break;
                case 6:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

}
