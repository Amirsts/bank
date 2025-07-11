package pages;

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
import request.Request;
import request.RequestType;
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
            System.out.println("\n--- Main Menu ---");

            String[] menuItems = {
                    "1. Customer Operations (Customer Login)",
                    "2. Branch Assistant",
                    "3. Teller",
                    "4. Branch Manager",
                    "5. Bank Director",
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
                    processAssistantManager(scanner, currentBranch ); // AssistantManager's menu
                    break;
                case 3:
                    Teller selectedTeller = selectTeller(scanner , branch); // Teller's enter point
                    if (selectedTeller != null) {
                        processTeller(scanner,selectedTeller , currentBranch ,bank ); // Teller's menu
                    }
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
                    System.out.print("Transfer amount: ");
                    int amount = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Password: ");
                    String password = scanner.nextLine();
                    try {
                        bank.transferBetweenCustomers(fromAccount, toAccount, amount, password); // Assigning values
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
                                loan.getLoanAmount(), loan.getPaidAmount(), loan.getRemainingAmount(), loan.getfDuration(), (int) loan.installmentPerMonth()));

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

    // متد ثبت مشتری جدید (ثبت نام مشتری از طریق منوی اصلی)
    static void processNewCustomer(Scanner scanner, Bank bank , Branch branch) {
        try {
            System.out.println("\n--- ثبت مشتری جدید ---");
            System.out.print("نام: ");
            String firstName = scanner.nextLine();
            System.out.print("نام خانوادگی: ");
            String lastName = scanner.nextLine();
            System.out.print("تاریخ تولد (YYYY-MM-DD): ");
            String birthDate = scanner.nextLine();
            System.out.print("کد ملی: ");
            String nationalCode = scanner.nextLine();

            while (!bank.isNationalCodeUnique(nationalCode)) {
                System.out.print("کد ملی وارد شده تکراری است\n" + "کد ملی را وارد کنید: ");
                nationalCode = scanner.nextLine();
            }

            System.out.print("آدرس: ");
            String address = scanner.nextLine();
            System.out.print("شماره تلفن: ");
            String phone = scanner.nextLine();

            while (!bank.isPhoneNumberUnique(phone)) {
                System.out.println("شماره تلفن وارد شده تکراری است\n" + "شماره تلفن را وارد کنید: ");
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
            System.out.println("مشتری جدید با شناسه " + customerId + " ثبت شد.");
        }catch (InvalidNationalCodeException e){
            System.out.println(e.getMessage());
        }catch (InvalidPhoneNumberException e){
            System.out.println(e.getMessage());
        }
    }

    // منوی دستیار شعبه: بررسی درخواست‌های بستن حساب و وام، نمایش اطلاعات شعبه
    static void processAssistantManager(Scanner scanner, Branch curentBranch) {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n--- منوی دستیار شعبه ---");
            System.out.println("1. بررسی درخواست‌های وام");
            System.out.println("2. نمایش اطلاعات شعبه");
            System.out.println("3. بازگشت به منوی اصلی");
            System.out.print("انتخاب شما: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch(choice) {

                case 1:
                    List<Request> loanRequests = curentBranch.getAssistantManager().getMessageBox().getRequestsByType(RequestType.LOAN_REQUEST);
                    if (loanRequests.isEmpty()) {
                        System.out.println("هیچ درخواست وام معلقی پیدا نشد.");
                    } else {
                        System.out.println(" مشتری های درحال انتظار برای دریافت وام");
                        for (int i = 0 ; i < loanRequests.size() ; i++){
                            Request tmpLR = loanRequests.get(i);
                            System.out.println((i + 1)  + "." +tmpLR.getSender().getFullName());
                        }
                        System.out.println("انتخاب شما:");
                        int chose = scanner.nextInt();
                        Customer selectedCustomer = loanRequests.get(chose -1).getSender();
                        Request slcRequest = selectedCustomer.getMessageBox().getRequestsByType(RequestType.LOAN_REQUEST).get(0);
                        Request asistantRequest = curentBranch.getAssistantManager().getMessageBox().getRequestsByType(RequestType.LOAN_REQUEST).get(chose-1);
                        Customer slcCustomer = slcRequest.getSender();

                        if (curentBranch.getAssistantManager().isEligibleForLoan(selectedCustomer)){
                            System.out.println("مشتری دارای وام فعال نیست." + "\nدرخواست وام برای رئیس شعبه ارسال شد");
                            curentBranch.getBranchManager().receiveRequest(slcRequest);
                            curentBranch.getAssistantManager().clearMessageBox(asistantRequest);
                            slcRequest.setStatus("درخواست وام شما برای رئیس شعبه ارسال شد");
                        }else {
                            System.out.println("وام های فعال مشتری:");
                            for (int i = 0; i < slcCustomer.getActiveLoans().size(); i++) {
                                slcCustomer.getActiveLoans().get(i).toString();
                            }
                            curentBranch.getAssistantManager().clearMessageBox(asistantRequest);
                            slcRequest.setStatus("شما دارای وام فعال هستید درخواست شما رد شد.");
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
                    System.out.println("گزینه نامعتبر.");
            }
        }
    }

    // منوی تحویل‌دار: پردازش واریز/برداشت (با استفاده از secureWithdraw) و ارجاع درخواست معلق
    static void processTeller(Scanner scanner , Teller selectedTeller , Branch curentBranch , Bank bank ) {
        boolean exit = false;
        while (!exit) {
            System.out.println("\nمنوی تحویل‌دار ---" + selectedTeller.getFullName() + "---");
            System.out.println("1. پردازش واریز/برداشت");
            System.out.println("2. ارجاع درخواست وام به معاون شعبه");
            System.out.println("3. تایید درخواست بازکردن حساب");
            System.out.println("4.تایید درخواست بستن حساب");
            System.out.println("5. بازگشت به منوی اصلی");
            System.out.print("انتخاب شما: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch(choice) {
                case 1:
                    System.out.println("انتخاب نوع عملیات: 1. واریز  2. برداشت");
                    int op = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("شماره حساب: ");
                    String accNum = scanner.nextLine();
                    System.out.print("مبلغ: ");
                    int amt = scanner.nextInt();
                    scanner.nextLine();
                    CurrentAccount acc = (CurrentAccount) curentBranch.findAccount(accNum);
                    if (acc == null) {
                        System.out.println("حساب مورد نظر پیدا نشد.");
                    } else {
                        if (op == 1) {
                            acc.deposit(amt);
                            System.out.println("واریز موفق. موجودی جدید: " + acc.getBalanceForBank());
                        } else if (op == 2) {
                            System.out.print("رمز عبور: ");
                            String pwd = scanner.nextLine();
                            try {
                                acc.secureWithdraw(amt, pwd);
                            } catch (IncorrectPasswordException | InvalidAmountException | InsufficientBalanceException ex) {
                                System.out.println("خطا در برداشت: " + ex.getMessage());
                            }
                        } else {
                            System.out.println("عملیات نامعتبر.");
                        }
                    }
                    break;
                case 2:
                    List<Request> loanRequests = selectedTeller.getMessageBox().getRequestsByType(RequestType.LOAN_REQUEST);
                    if (loanRequests.isEmpty()){
                        System.out.println("مشتری در صف دریافت وام یافت نشد");
                        break;
                    }else {
                        System.out.println(" مشتری های درحال انتظار برای دریافت وام");
                        for (int i = 0 ; i < loanRequests.size() ; i++){
                            Request tmpLR = loanRequests.get(i);
                            System.out.println((i + 1)  + "." +tmpLR.getSender().getFullName());
                        }
                        System.out.println("انتخاب شما:");
                        int chose = scanner.nextInt();
                        Customer selectedCustomer = loanRequests.get(chose -1).getSender();
                        Request slcRequest = selectedCustomer.getMessageBox().getRequestsByType(RequestType.LOAN_REQUEST).get(0);
                        Request sltRequest = selectedTeller.getMessageBox().getRequestsByType(RequestType.LOAN_REQUEST).get(chose-1);
                        selectedTeller.clearMessageBox(sltRequest);
                        curentBranch.getAssistantManager().receiveRequest(slcRequest);
                        slcRequest.setStatus("درخواست به معاون شعبه ارجاع داده شد"+ selectedTeller.getFullName()  +"تحویلدار ");
                        System.out.println("درخواست به معاون شعبه ارجاع داده شد");

                    }
                    break;
                case 3:

                   List<Request> opRequests = selectedTeller.getMessageBox().getRequestsByType(RequestType.OPEN_ACCOUNT);
                   if (opRequests.isEmpty()){
                       System.out.println("مشتری در صف انتظار یافت نشد");
                       break;
                       }else {
                            System.out.println(" مشتری های درحال انتظار برای باز شدن حساب");
                            for (int i = 0 ; i < opRequests.size() ; i++){
                                Request tmpR = opRequests.get(i);
                                System.out.println((i + 1)  + "." +tmpR.getSender().getFullName());
                            }
                            System.out.println("انتخاب شما:");
                            int chose = scanner.nextInt();
                            Customer selectedCustomer = opRequests.get(chose -1).getSender();
                            Request slcRequest = selectedCustomer.getMessageBox().getRequestsByType(RequestType.OPEN_ACCOUNT).get(0);
                            Request sltRequest = selectedTeller.getMessageBox().getRequestsByType(RequestType.OPEN_ACCOUNT).get(chose-1);

                            System.out.println("پیام مشتری: " + slcRequest.getMessage() + "\n" + "عملیات باز کردن حساب در حال اجرا...");

                            System.out.print("لطفاً مبلغ اولیه واریز را وارد کنید: ");
                            int initialDeposit = scanner.nextInt();
                            scanner.nextLine();
                            System.out.print("لطفاً رمز عبور حساب را وارد کنید: ");
                            String accountPassword = scanner.nextLine();

                            selectedTeller.clearMessageBox(sltRequest);

                            char accountType = slcRequest.getMessage().charAt(0);
                            String newAccountNumber = bank.accountNumberCreator(accountType);
                            if (accountType == '1'){

                                CurrentAccount newAccount = new CurrentAccount(newAccountNumber , selectedCustomer , initialDeposit , accountPassword);
                                bank.addAccount(newAccount);
                                curentBranch.addAccount(newAccount);
                                selectedCustomer.openAccount(newAccount);
                                slcRequest.setStatus("مشتزی گرامی:" + selectedCustomer.getFullName() +"حساب جاری شما با رمز:" + accountPassword + " شماره حساب:" + newAccountNumber + "افتتاح شد");
                                System.out.print("حساب جاری " + selectedCustomer.getFullName() + "افتتاح شد");
                            }else if (accountType == '2'){
                                System.out.println("تارخ را وارد کنید مثلا (07/05/2025):");
                                String input = scanner.nextLine();
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                                LocalDate dateOpenAccount = LocalDate.parse(input , formatter);

                                ShortTermAccount newAccount = new ShortTermAccount(newAccountNumber , selectedCustomer , initialDeposit , accountPassword , dateOpenAccount);
                                bank.addAccount(newAccount);
                                curentBranch.addAccount(newAccount);
                                selectedCustomer.openAccount(newAccount);
                                selectedCustomer.openShortTermAccount(newAccount);
                                slcRequest.setStatus("مشتزی گرامی:" + selectedCustomer.getFullName() +"حساب کوتاه مدت شما با رمز:" + accountPassword + " شماره حساب:" + newAccountNumber + "افتتاح شد");
                                System.out.print("حساب کوتاه مدت " + selectedCustomer.getFullName() + "افتتاح شد");
                            }else if (accountType == '3') {
                                QarzAlHasanehAccount newAccount = new QarzAlHasanehAccount(newAccountNumber , selectedCustomer , initialDeposit , accountPassword);
                                bank.addAccount(newAccount);
                                curentBranch.addAccount(newAccount);
                                selectedCustomer.openAccount(newAccount);
                                slcRequest.setStatus("مشتزی گرامی:" + selectedCustomer.getFullName() +"حساب قرض الحسنه شما با رمز:" + accountPassword + " شماره حساب:" + newAccountNumber + "افتتاح شد");
                                System.out.print("حساب قرض الحسنه " + selectedCustomer.getFullName() + "افتتاح شد");
                            }
                       }
                    break;
                case 4:
                    List<Request> clRequest = selectedTeller.getMessageBox().getRequestsByType(RequestType.CLOSE_ACCOUNT);
                    if (clRequest.isEmpty()){
                        System.out.println("مشتری در صف انتظار یافت نشد");
                        break;
                    }else {
                        System.out.println(" مشتری های درحال انتظار برای باز شدن حساب");
                        for (int i = 0 ; i < clRequest.size() ; i++){
                            Request tmpR = clRequest.get(i);
                            System.out.println((i + 1)  + "." +tmpR.getSender().getFullName());
                        }
                        System.out.println("انتخاب شما:");
                        int chose = scanner.nextInt();
                        Customer selectedCustomer = clRequest.get(chose -1).getSender();
                        Request slcRequest = selectedCustomer.getMessageBox().getRequestsByType(RequestType.CLOSE_ACCOUNT).get(0);
                        Request sltRequest = selectedTeller.getMessageBox().getRequestsByType(RequestType.CLOSE_ACCOUNT).get(chose-1);
                        String accountID = slcRequest.getMessage();

                        //checking Customer has active loan
                        if (selectedCustomer.hasActiveLoan()){
                            System.out.println("مشتری دارای وام فعال است");
                            slcRequest.setStatus("مشتری گرامی شما دارای وام فعال هستید امکان بسته شدن حساب شما وجود ندارد.");
                            selectedTeller.clearMessageBox(sltRequest);
                            break;
                        }
                        System.out.println("مشتری فاقد وام فعال است حساب با موفقیت بسته شد");
                        selectedCustomer.removeAccount(accountID);
                        slcRequest.setStatus("حساب شما با شماره:" + accountID + "با موفقیت بسته شد.");
                        selectedTeller.clearMessageBox(sltRequest);
                    }
                    break;

                case 5:
                    exit = true;
                    break;
                default:
                    System.out.println("گزینه نامعتبر.");
            }
        }
    }

    // منوی مدیر شعبه: تایید یا رد درخواست‌های معلق و مشاهده عملکرد کلی شعبه
    static void processManager(Scanner scanner, Branch branch, Customer customer ,Bank bank) {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n--- منوی مدیر شعبه ---");
            System.out.println("1.مشاهده درخواست های وام");
            System.out.println("2. مشاهده عملکرد کلی شعبه");
            System.out.println("3. بازگشت به منوی اصلی");
            System.out.print("انتخاب شما: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch(choice) {
                case 1:
                    List<Request> mRequests = branch.getBranchManager().getMessageBox().getRequestsByType(RequestType.LOAN_REQUEST);
                    if (mRequests.isEmpty()) {
                        System.out.println("هیچ درخواست معلقی پیدا نشد.");
                    } else {
                        System.out.println(" مشتری های درحال انتظار برای دریافت وام");
                        for (int i = 0 ; i < mRequests.size() ; i++){
                            Request tmpLR = mRequests.get(i);
                            System.out.println((i + 1)  + "." +tmpLR.getSender().getFullName());
                        }
                        System.out.println("انتخاب شما:");
                        int chose = scanner.nextInt();
                        scanner.nextLine();
                        Customer selectedCustomer = mRequests.get(chose -1).getSender();
                        Request slcRequest = selectedCustomer.getMessageBox().getRequestsByType(RequestType.LOAN_REQUEST).get(0);
                        Request managerRequest = branch.getBranchManager().getMessageBox().getRequestsByType(RequestType.LOAN_REQUEST).get(chose-1);
                        Customer slcCustomer = slcRequest.getSender();
                        int loanType = Integer.parseInt(slcRequest.getMessage());

                        //Selecting customer's account for loan
                        Account slcAccount = slcCustomer.findAccount(slcRequest.getAccountNumber());

                        switch (loanType) {
                            case 1:
                                if ( (slcRequest.getLoanAmount() < slcCustomer.getNormalLoanCeiling()) &&  //The loan amount was checked to ensure it did not exceed the loan ceiling.
                                     (slcRequest.getLoanAmount() < branch.getCurrentShortTermBalance())) {//The bank was checked to ensure it had sufficient financial resources.

                                    System.out.println("تعداد اقصات ماهانه را واردکنید:");
                                    int duration = scanner.nextInt();
                                    scanner.nextLine();
                                    System.out.print("تاریخ را وارد کنید (مثلاً 05/07/2025): ");
                                    String input = scanner.nextLine();
                                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                                    LocalDate date = LocalDate.parse(input, formatter);

                                    NormalLoan normalLoan = new NormalLoan(slcRequest.getLoanAmount() , duration , date ,slcCustomer);
                                    slcCustomer.addLoan(normalLoan);
                                    slcAccount.deposit((int) slcRequest.getLoanAmount());
                                    slcRequest.setStatus("مشتری گرامی درخواست وام عادی شما تایید شد و مبلغ:" +slcRequest.getLoanAmount() + " به حساب شما واریز شد\n" +
                                            "شما موظف به باز پرداخت وام در طی" + duration + "ماه و در هر قسط به میزان:" + normalLoan.installmentPerMonth() + "هستید" );
                                    System.out.println("وام به حساب مشتری واریز شد" + slcRequest.getLoanAmount());
                                    branch.getBranchManager().clearMessageBox(managerRequest);

                                }else {
                                    System.out.println("something went wrong");
                                }
                                break;

                            case 2:
                                if ((slcRequest.getLoanAmount() < slcCustomer.getTashilatCeiling()) &&  //The loan amount was checked to ensure it did not exceed the loan ceiling.
                                     (slcRequest.getLoanAmount() < branch.getQarzAlhasanehBalance())){ //The bank was checked to ensure it had sufficient financial resources.

                                    System.out.println("تعداد اقصات ماهانه را واردکنید:");
                                    int duration = scanner.nextInt();
                                    scanner.nextLine();
                                    System.out.print("تاریخ را وارد کنید (مثلاً 05/07/2025): ");
                                    String input = scanner.nextLine();
                                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                                    LocalDate date = LocalDate.parse(input, formatter);

                                    TashilatLoan tashilatLoan = new TashilatLoan(slcRequest.getLoanAmount() , duration , date ,slcCustomer);
                                    slcCustomer.addLoan(tashilatLoan);
                                    slcAccount.deposit((int) slcRequest.getLoanAmount());
                                    slcRequest.setStatus("مشتری گرامی درخواست وام تسهیلات شما تایید شد و مبلغ:" +slcRequest.getLoanAmount() + " به حساب شما واریز شد\n" +
                                            "شما موظف به باز پرداخت وام در طی" + duration + "ماه و در هر قسط به میزان:" + tashilatLoan.installmentPerMonth() + "هستید" );
                                    System.out.println("وام به حساب مشتری واریز شد" + slcRequest.getLoanAmount());
                                    branch.getBranchManager().clearMessageBox(managerRequest);

                                }else {
                                    System.out.println("something went wrong");
                                }
                                break;

                            default:
                                System.out.println("گزینه نامعتبر.");
                        }
                    }
                    break;
                case 2:
                    bank.displayInfo();
                    break;
                case 3:
                    exit = true;
                    break;
                default:
                    System.out.println("گزینه نامعتبر.");
            }
        }
    }

    // منوی رئیس بانک: ایجاد تحویل‌دار و معاون شعبه
    static void processBankManager(Scanner scanner, Branch branch ,Bank bank) {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n--- منوی رئیس بانک ---");
            System.out.println("1. ایجاد تحویل‌دار جدید");
            System.out.println("2. ایجاد معاون شعبه جدید");
            System.out.println("3.ایجاد رئیس شعبه جدید");
            System.out.println("4.ایجاد شعبه جدید");
            System.out.println("5. نمایش اطلاعات شعبه");
            System.out.println("6. بازگشت به منوی اصلی");
            System.out.print("انتخاب شما: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch(choice) {
                case 1:
                    try{
                        System.out.println("ایجاد تحویل‌دار جدید...");
                        System.out.print("ID تحویل‌دار: ");
                        String tellerId = scanner.nextLine();
                        System.out.print("نام: ");
                        String tellerFirstName = scanner.nextLine();
                        System.out.print("نام خانوادگی: ");
                        String tellerLastName = scanner.nextLine();
                        System.out.print("تاریخ تولد: ");
                        String tellerBirthDay = scanner.nextLine();
                        System.out.print("کد ملی: ");
                        String tellerNationalCode = scanner.nextLine();

                        while (!bank.isNationalCodeUnique(tellerNationalCode)){
                            System.out.print( "کد ملی وارد شده تکراری است\n" + "کد ملی را وارد کنید: ");
                            tellerNationalCode = scanner.nextLine();
                        }

                        System.out.print("آدرس: ");
                        String tellerAddress = scanner.nextLine();
                        System.out.print("شماره تلفن: ");
                        String tellerPhone = scanner.nextLine();

                        while (!bank.isPhoneNumberUnique(tellerPhone)){
                            System.out.println("شماره تلفن وارد شده تکراری است\n" + "شماره تلفن را وارد کنید: ");
                            tellerPhone = scanner.nextLine();
                        }

                        System.out.println("رمز ورود");
                        String passWord = scanner.nextLine();

                        Teller newTeller = new Teller(tellerFirstName, tellerLastName, tellerBirthDay,
                                tellerNationalCode, tellerAddress, tellerPhone, tellerId, passWord);
                        branch.addTeller(newTeller);
                        bank.addEmployee(newTeller);
                        System.out.println("تحویل‌دار جدید با شناسه " + tellerId + " ایجاد شد.");
                    }catch (InvalidNationalCodeException e){
                        System.out.println( e.getMessage());
                    }catch (InvalidPhoneNumberException e){
                        System.out.println( e.getMessage() );
                    }
                    break;
                case 2:
                    try {

                        System.out.println("ایجاد معاون شعبه جدید...");
                        System.out.print("شناسه معاون شعبه: ");
                        String amId = scanner.nextLine();
                        System.out.print("نام: ");
                        String amFirstName = scanner.nextLine();
                        System.out.print("نام خانوادگی: ");
                        String amLastName = scanner.nextLine();
                        System.out.print("تاریخ تولد: ");
                        String amBirthDay = scanner.nextLine();
                        System.out.print("کد ملی: ");
                        String amNationalCode = scanner.nextLine();

                        while (!bank.isNationalCodeUnique(amNationalCode)) {
                            System.out.print("کد ملی وارد شده تکراری است\n" + "کد ملی را وارد کنید: ");
                            amNationalCode = scanner.nextLine();
                        }

                        System.out.print("آدرس: ");
                        String amAddress = scanner.nextLine();
                        System.out.print("شماره تلفن: ");
                        String amPhone = scanner.nextLine();

                        while (!bank.isPhoneNumberUnique(amPhone)) {
                            System.out.println("شماره تلفن وارد شده تکراری است\n" + "شماره تلفن را وارد کنید: ");
                            amPhone = scanner.nextLine();
                        }
                        System.out.println("رمز ورود");
                        String passWord = scanner.nextLine();

                        AssistantManager newAM = new AssistantManager(amFirstName, amLastName, amBirthDay,
                                amNationalCode, amAddress, amPhone, amId , passWord);
                        branch.setAssistantManager(newAM);
                        bank.addEmployee(newAM);
                        System.out.println("معاون شعبه جدید با شناسه " + amId + " ایجاد شد.");
                    }catch (InvalidNationalCodeException e){
                        System.out.println(e.getMessage());
                    }catch (InvalidPhoneNumberException e){
                        System.out.println(e.getMessage());
                    }
                    break;
                case 3:
                    try {
                    System.out.println("ایجاد رئیس شعبه جدید...");
                    System.out.print("شناسه رئیس شعبه: ");
                    String amId = scanner.nextLine();
                    System.out.print("نام: ");
                    String amFirstName = scanner.nextLine();
                    System.out.print("نام خانوادگی: ");
                    String amLastName = scanner.nextLine();
                    System.out.print("تاریخ تولد: ");
                    String amBirthDay = scanner.nextLine();
                    System.out.print("کد ملی: ");
                    String amNationalCode = scanner.nextLine();

                    while (!bank.isNationalCodeUnique(amNationalCode)) {
                        System.out.print("کد ملی وارد شده تکراری است\n" + "کد ملی را وارد کنید: ");
                        amNationalCode = scanner.nextLine();
                    }

                    System.out.print("آدرس: ");
                    String amAddress = scanner.nextLine();
                    System.out.print("شماره تلفن: ");
                    String amPhone = scanner.nextLine();

                    while (!bank.isPhoneNumberUnique(amPhone)) {
                        System.out.println("شماره تلفن وارد شده تکراری است\n" + "شماره تلفن را وارد کنید: ");
                        amPhone = scanner.nextLine();
                    }
                    System.out.println("رمز ورود");
                    String passWord = scanner.nextLine();

                    BranchManager Bmanager = new BranchManager(amFirstName, amLastName, amBirthDay,
                            amNationalCode, amAddress, amPhone, amId , passWord);
                    branch.setBranchManager(Bmanager);
                    bank.addEmployee(Bmanager);
                    System.out.println("معاون شعبه جدید با شناسه " + amId + " ایجاد شد.");
            }catch (InvalidNationalCodeException e){
                System.out.println(e.getMessage());
            }catch (InvalidPhoneNumberException e){
                System.out.println(e.getMessage());
            }
                    break;
                case 4:
                    System.out.println("شماره شعبه را وارد کنید:");
                    String branchNumber = scanner.nextLine();
                    if ( !bank.isBranchNumberUnique(branchNumber)){
                        System.out.println("شماره شعبه تکراری است دوباره سعی کنید:");
                        branchNumber = scanner.nextLine();
                    }
                    Branch branch1 = new Branch(branchNumber);
                    System.out.println("شعبه با شماره: " + branchNumber + "ایجاد شد");
                    break;
                case 5:
                    branch.displayInfo();
                    break;
                case 6:
                    exit = true;
                    break;
                default:
                    System.out.println("گزینه نامعتبر.");
            }
        }
    }

}
