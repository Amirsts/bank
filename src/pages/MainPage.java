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
import java.util.stream.Collectors;

public class MainPage {
    public static void firstPage() {
        Scanner scanner = new Scanner(System.in);

        // ۱. ایجاد بانک و شعبه‌ها
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
        // ========================
        // ۲. ایجاد مشتری و حساب‌ها (برای تست اولیه)
        // ========================
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
        // ایجاد حساب جاری برای مشتری
        Teller teller = new Teller("Ali","asghari","1986-12-20", "0965656654","mashad" , "09064563232","2", "2");
        bank.addEmployee(teller);
        branch.addTeller(teller);



        System.out.println("\n---- Final Display Info ----");
        branch.displayInfo();
        customer.displayInfo();
        bank.displayBranches();
        bank.displayCustomers();

        // ========================
        // منوی اصلی با قابلیت انتخاب نقش، تغییر شعبه، ثبت مشتری جدید
        // ========================
        boolean exitSystem = false;
        Branch currentBranch = branch; // شعبه جاری اولیه
        while (!exitSystem) {
            System.out.println("\n--- منوی اصلی ---");
            System.out.println("1. عملیات مشتری (ورود مشتری)");
            System.out.println("2. دستیار شعبه");
            System.out.println("3. تحویل‌دار");
            System.out.println("4. مدیر شعبه");
            System.out.println("5. رئیس بانک");
            System.out.println("6. تغییر شعبه");
            System.out.println("7. ثبت مشتری جدید");
            System.out.println("8. خروج از سیستم");
            System.out.print("انتخاب شما: ");
            int mainChoice = scanner.nextInt();
            scanner.nextLine(); // پاکسازی newline

            switch (mainChoice) {
                case 1:
                    // انتخاب مشتری موجود از بانک
                    Customer selectedCustomer = selectCustomer(scanner, bank);
                    if (selectedCustomer != null) {
                        processCustomer(scanner, selectedCustomer, currentBranch, bank);
                    }
                    break;
                case 2:
                    processAssistantManager(scanner, currentBranch, customer, assistantManager);
                    break;
                case 3:
                    Teller selectedTeller = selectTeller(scanner , branch);
                    if (selectedTeller != null) {
                        processTeller(scanner,bank , currentBranch, customer ,selectedTeller , assistantManager);
                    }
                    break;
                case 4:
                    processManager(scanner, currentBranch, customer , bank);
                    break;
                case 5:
                    processBankManager(scanner, currentBranch , bank);
                    break;
                case 6:
                    currentBranch = selectBranch(scanner, bank);
                    System.out.println("شعبه جاری تغییر یافت به: " + currentBranch.getBranchNumber());
                    break;
                case 7:
                    processNewCustomer(scanner, bank , branch);
                    break;
                case 8:
                    exitSystem = true;
                    System.out.println("خروج از سیستم. خداحافظ!");
                    break;
                default:
                    System.out.println("گزینه نامعتبر. لطفاً دوباره تلاش کنید.");
            }
        }
        scanner.close();
        }

    public static Branch selectBranch(Scanner scanner, Bank bank) {
        List<Branch> branches = bank.getBranches();
        System.out.println("لطفاً شعبه مورد نظر خود را انتخاب کنید:");
        for (int i = 0; i < branches.size(); i++) {
            System.out.println((i + 1) + ". شعبه شماره: " + branches.get(i).getBranchNumber());
        }
        System.out.print("انتخاب شما: ");
        int branchChoice = scanner.nextInt();
        scanner.nextLine();
        if (branchChoice >= 1 && branchChoice <= branches.size()) {
            return branches.get(branchChoice - 1);
        } else {
            System.out.println("انتخاب نامعتبر. شعبه پیش‌فرض انتخاب می‌شود.");
            return branches.get(0);
        }
    }

    // متد انتخاب مشتری از بین مشتریان بانک

    static Customer selectCustomer(Scanner scanner, Bank bank) {
        List<Customer> customers = bank.getCustomers();
        if (customers.isEmpty()) {
            System.out.println("هیچ مشتری ثبت‌شده‌ای وجود ندارد.");
            return null;
        }
        System.out.println("لطفاً Customer ID خود را وارد کنید:");
        String cID = scanner.nextLine();

        for (int i = 0; i < customers.size(); i++) {
           Customer tempC = customers.get(i);

           if (tempC.getCustomerId().equals(cID)){
               return customers.get(i);
           }
        }
        System.out.println("مشتری یافت نشد لطفا دوباره تلاش کنید");
        return null;
    }

    static Teller selectTeller(Scanner scanner, Branch branch) {
        List<Teller> tellers = branch.getTellers();
        if (tellers.isEmpty()) {
            System.out.println("هیچ تحویلداری ثبت‌شده‌ای وجود ندارد.");
            return null;
        }
        System.out.println("لطفاً Teller ID خود را وارد کنید:");
        String tID = scanner.nextLine();
        System.out.println("لطفاً رمز خود را وارد کنید:");
        String tPass = scanner.nextLine();

        for (int i = 0; i < tellers.size(); i++) {
            Teller tempT = tellers.get(i);

            if (tempT.getEmployeeId().equals(tID) && tempT.isPassWordTrue(tPass)){
                return tellers.get(i);
            }
        }
        System.out.println("تحویلدار یافت نشد لطفا دوباره تلاش کنید");
        return null;
    }

    // منوی مشتری: شامل ایجاد حساب جدید، انتقال وجه درون مشتری، ارسال درخواست وام، نمایش پیام‌ها،
    // انتقال وجه به حساب مشتری دیگر و نمایش موجودی تمامی حساب‌های مشتری.
    static void processCustomer(Scanner scanner, Customer customer, Branch currentBranch, Bank bank) {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n--- منوی مشتری (" + customer.getFullName() + ") ---");
            System.out.println("1. باز کردن حساب جدید");
            System.out.println("2. انتقال وجه ");
            System.out.println("3. ارسال درخواست وام");
            System.out.println("4.پرداخت اقساط وام");
            System.out.println("5. نمایش پیام‌ها");
            System.out.println("6. نمایش موجودی حساب‌های من");
            System.out.println("7.بستن حساب");
            System.out.println("8. بازگشت به منوی اصلی");
            System.out.print("انتخاب شما: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch(choice) {

                case 1:
                    System.out.println("ارسال درخواست ایجاد حساب...");
                    System.out.println("نوع حساب را وارد کنید:\n" +  "1.حساب جاری\n" + "2.حساب کوتاه مدت\n" + "3.حساب قرض الحسنه");
                    String text = scanner.nextLine();
                    while (!(text.charAt(0) == '1' ||text.charAt(0) == '2' ||text.charAt(0) == '3')){
                        System.out.println("لطفا فقط عدد گزینه مورد نظر را وارد کنید:");
                        text = scanner.nextLine();
                    }
                    Request openAccountRequest = new Request(RequestType.OPEN_ACCOUNT, text, customer);
                    customer.getMessageBox().addRequest(openAccountRequest);
                    currentBranch.getSolitudeTeller().receiveRequest(openAccountRequest);
                    System.out.println("درخواست ایجاد حساب شما ثبت شد.");
                    break;
                case 2:
                    System.out.println("انتقال وجه بین حساب‌های خود...");
                    System.out.print("شماره حساب مبدا: ");
                    String fromAccount = scanner.nextLine();
                    System.out.print("شماره حساب مقصد: ");
                    String toAccount = scanner.nextLine();
                    System.out.print("مبلغ انتقال: ");
                    int amount = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("رمز عبور: ");
                    String password = scanner.nextLine();
                    try {
                        bank.transferBetweenCustomers(fromAccount, toAccount, amount, password);
                        System.out.println("انتقال وجه با موفقیت انجام شد.");
                    } catch (Exception e) {
                        System.out.println("خطا در انتقال وجه: " + e.getMessage());
                    }
                    break;
                case 3:
                    System.out.println("ارسال درخواست وام...");
                    System.out.println("نوع وام را انتخاب کنید:\n" +  "1.وام عادی\n" + "2.وام تسهیلات\n");
                    String loanType = scanner.nextLine();
                    while (!(loanType.charAt(0) == '1' ||loanType.charAt(0) == '2')){
                        System.out.println("لطفا فقط عدد گزینه مورد نظر را وارد کنید:");
                        loanType = scanner.nextLine();
                    }
                    System.out.println("حساب های شما:");
                    List<Account> accounts1 = customer.getAccounts();
                    for (int i = 0 ; i < accounts1.size() ; i++){
                        Account account = accounts1.get(i);
                        System.out.println( (i + 1) + "." + account.getAccountId());
                    }
                    System.out.println("لطفا حساب مورد نظر را انتخاب کنید:");
                    int slcAccount = scanner.nextInt();
                    while (slcAccount > accounts1.size()){
                        System.out.println("لطفا فقط عدد گزینه مورد نظر را وارد کنید:");
                        slcAccount = scanner.nextInt();
                    }
                    System.out.println("میزان وام درخواستی را وارد کنید:");
                    double loanAmount = scanner.nextDouble();
                    Request loanRequest = new Request(RequestType.LOAN_REQUEST,customer , loanType ,accounts1.get((slcAccount - 1)).getAccountId(), loanAmount);
                    customer.getMessageBox().addRequest(loanRequest);
                    currentBranch.getSolitudeTeller().receiveRequest(loanRequest);
                    System.out.println("درخواست وام شما ثبت شد.");
                    break;
                case 4:
                    if (customer.getActiveLoans().isEmpty()){
                        System.out.println("شما وام فعالی ندارید");
                    }else {
                        BaseLoan loan = customer.getActiveLoans().get(0);
                        System.out.println(MessageFormat.format("مشخصات وام:\nمبلغ کل وام :{0}\nمیزان کل باز پرداخت شده توسط مشتری:{1}\nمیزان کل باقیمانده اقسط:{2}\nتعداد اقساط باقیمانده:{3} مبلغ قسط :{4}",
                                loan.getLoanAmount(), loan.getPaidAmount(), loan.getRemainingAmount(), loan.getfDuration(), (int) loan.installmentPerMonth()));

                        System.out.println("1.پرداخت قسط" + "\n2.بازگشت" + "\nانتخاب شما:");
                        int chose = scanner.nextInt();
                        scanner.nextLine();

                        switch (chose) {
                            case 1:
                                System.out.println("تاریخ پرداخت را وارد نمایید مثلا(07/05/2025) :");
                                String input = scanner.nextLine();
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                                LocalDate datePay = LocalDate.parse(input, formatter);


                                System.out.println("شماره حساب خود را وارد کنید:");
                                String accountNumber = scanner.nextLine();
                                System.out.println("لطفا رمز خود را وارد کنید:");
                                String accountPassWord = scanner.nextLine();
                                Account cAccount = customer.findAccount(accountNumber);
                                try {
                                    cAccount.secureWithdrawForLoan((int) loan.installmentPerMonth(), accountPassWord);
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
                    System.out.println("نمایش درخواست ‌های مشتری:");
                    customer.getMessageBox().printAll();
                    break;
                case 6:
                    System.out.println("نمایش موجودی حساب‌های " + customer.getFullName() + ":");
                    System.out.print("تاریخ را وارد کنید (مثلاً 05/07/2025): ");
                    String input = scanner.nextLine();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDate dateCheckAmount = LocalDate.parse(input, formatter);

                    // فرض بر این است که Customer دارای متد getAccounts() است که لیست حساب‌ها را برمی‌گرداند
                    List<Account> accounts = customer.getAccounts();
                    List<ShortTermAccount> shortTermAccounts = customer.getShortTermAccounts();
                    if (accounts == null || accounts.isEmpty()) {
                        System.out.println("هیچ حسابی برای این مشتری ثبت نشده است.");
                    } else {
                        for (Account accItem : accounts) {
                            if (accItem.getAccountId().startsWith("02"))
                                continue;
                            System.out.println("شماره حساب: " + accItem.getAccountId() + " - موجودی: " + accItem.getBalance());
                        }
                        for (ShortTermAccount shortTermAccount :shortTermAccounts) {
                            shortTermAccount.profitCheck(dateCheckAmount);
                            System.out.println("شماره حساب: " + shortTermAccount.getAccountId() + " - موجودی: " + shortTermAccount.getBalance());
                        }
                    }
                    break;

                case 7:
                    System.out.println("ارسال درخواست بستن حساب..." + "\nشماره حساب مورد نظر را وارد کنید:" );
                    String acNumber = scanner.nextLine();
                    if (customer.findAccount(acNumber) == null ){
                        System.out.println("حساب مورد نظر معتبر نیست");
                        break;
                    }
                    Request closeAccountRequest = new Request(RequestType.CLOSE_ACCOUNT, acNumber, customer);
                    customer.getMessageBox().addRequest(closeAccountRequest);
                    currentBranch.getSolitudeTeller().receiveRequest(closeAccountRequest);
                    System.out.println("درخواست بستن حساب شما ثبت شد.");
                    break;

                case 8:
                    exit = true;
                    break;
                default:
                    System.out.println("گزینه نامعتبر. لطفاً دوباره تلاش کنید.");
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
    static void processAssistantManager(Scanner scanner, Branch branch, Customer customer , AssistantManager assistantManager) {
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
                    List<Request> loanRequests = assistantManager.getMessageBox().getRequestsByType(RequestType.LOAN_REQUEST);
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
                        Request asistantRequest = assistantManager.getMessageBox().getRequestsByType(RequestType.LOAN_REQUEST).get(chose-1);
                        Customer slcCustomer = slcRequest.getSender();

                        if (slcCustomer.isEligibleForLoan()){
                            System.out.println("مشتری دارای وام فعال نیست." + "\nدرخواست وام برای رئیس شعبه ارسال شد");
                            branch.getBranchManager().receiveRequest(slcRequest);
                            assistantManager.clearMessageBox(asistantRequest);
                            slcRequest.setStatus("درخواست وام شما برای رئیس شعبه ارسال شد");
                        }else {
                            System.out.println("وام های فعال مشتری:");
                            for (int i = 0; i < slcCustomer.getActiveLoans().size(); i++) {
                                slcCustomer.getActiveLoans().get(i).toString();
                            }
                            assistantManager.clearMessageBox(asistantRequest);
                            slcRequest.setStatus("شما دارای وام فعال هستید درخواست شما رد شد.");
                        }
                    }
                    break;
                case 2:
                    branch.displayInfo();
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
    static void processTeller(Scanner scanner,Bank bank ,Branch branch, Customer customer , Teller selectedTeller , AssistantManager assistantManager) {
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
                    CurrentAccount acc = (CurrentAccount) customer.findAccount(accNum);
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
                        assistantManager.receiveRequest(slcRequest);
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

                                CurrentAccount newAccount = new CurrentAccount(newAccountNumber, customer, initialDeposit, accountPassword);
                                bank.addAccount(newAccount);
                                branch.addAccount(newAccount);
                                selectedCustomer.openAccount(newAccount);
                                slcRequest.setStatus("مشتزی گرامی:" + selectedCustomer.getFullName() +"حساب جاری شما با رمز:" + accountPassword + " شماره حساب:" + newAccountNumber + "افتتاح شد");
                                System.out.print("حساب جاری " + selectedCustomer.getFullName() + "افتتاح شد");
                            }else if (accountType == '2'){
                                System.out.println("تارخ را وارد کنید مثلا (07/05/2025):");
                                String input = scanner.nextLine();
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                                LocalDate dateOpenAccount = LocalDate.parse(input , formatter);

                                ShortTermAccount newAccount = new ShortTermAccount(newAccountNumber, customer, initialDeposit, accountPassword , dateOpenAccount);
                                bank.addAccount(newAccount);
                                branch.addAccount(newAccount);
                                selectedCustomer.openAccount(newAccount);
                                selectedCustomer.openShortTermAccount(newAccount);
                                slcRequest.setStatus("مشتزی گرامی:" + selectedCustomer.getFullName() +"حساب کوتاه مدت شما با رمز:" + accountPassword + " شماره حساب:" + newAccountNumber + "افتتاح شد");
                                System.out.print("حساب کوتاه مدت " + selectedCustomer.getFullName() + "افتتاح شد");
                            }else if (accountType == '3') {
                                QarzAlHasanehAccount newAccount = new QarzAlHasanehAccount(newAccountNumber, customer, initialDeposit, accountPassword);
                                bank.addAccount(newAccount);
                                branch.addAccount(newAccount);
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
