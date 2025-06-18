package pages;

import account.Account;
import account.CurrentAccount;
import bank.Bank;
import branch.Branch;
import branch.BranchManager;
import branch.AssistantManager;
import branch.Teller;
import person.Customer;
import request.Request;
import request.RequestType;
import loan.NormalLoan;
import exceptions.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.Scanner;

public class MainPage {
    public static void firstPage() {
        Scanner scanner = new Scanner(System.in);

        // ۱. ایجاد بانک و شعبه‌ها
        Bank bank = new Bank();

        Branch branch = new Branch("001");

        BranchManager branchManager = new BranchManager(
                "Ali", "Rezayi", "1990-01-01", "1234567890",
                "Tehran, Iran", "09121234567", "BM001"
        );
        branch.setBranchManager(branchManager);
        bank.addEmployee(branchManager);
        bank.addBranch(branch);

        // ========================
        // ۲. ایجاد مشتری و حساب‌ها (برای تست اولیه)
        // ========================
        Customer customer = new Customer(
                "Reza", "Moazeni", "1985-07-20", "1029384756",
                "Shiraz, Iran", "09134567890", "CUST001"
        );
        bank.addCustomer(customer);
        branch.addCustomer(customer);
        // ایجاد حساب جاری برای مشتری
        CurrentAccount currentAccount = new CurrentAccount(
                "0101234567892", customer, 1000000, "pass123"
        );
        branch.addAccount(currentAccount);
        customer.openAccount(currentAccount);

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
            System.out.println("1. عملیات مشتری (انتخاب مشتری موجود)");
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
                    processAssistantManager(scanner, currentBranch, customer);
                    break;
                case 3:
                    processTeller(scanner, currentBranch, customer);
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
        System.out.println("لطفاً مشتری مورد نظر خود را انتخاب کنید:");
        for (int i = 0; i < customers.size(); i++) {
            System.out.println((i + 1) + ". " + customers.get(i).getFullName() + " (شناسه: " + customers.get(i).getCustomerId() + ")");
        }
        System.out.print("انتخاب شما: ");
        int custChoice = scanner.nextInt();
        scanner.nextLine();
        if (custChoice >= 1 && custChoice <= customers.size()) {
            return customers.get(custChoice - 1);
        } else {
            System.out.println("انتخاب نامعتبر. مشتری پیش‌فرض انتخاب می‌شود.");
            return customers.get(0);
        }
    }

    // منوی مشتری: شامل ایجاد حساب جدید، انتقال وجه درون مشتری، ارسال درخواست وام، نمایش پیام‌ها،
    // انتقال وجه به حساب مشتری دیگر و نمایش موجودی تمامی حساب‌های مشتری.
    static void processCustomer(Scanner scanner, Customer customer, Branch currentBranch, Bank bank) {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n--- منوی مشتری (" + customer.getFullName() + ") ---");
            System.out.println("1. باز کردن حساب جدید");
            System.out.println("2. انتقال وجه بین حساب‌های خود");
            System.out.println("3. ارسال درخواست وام");
            System.out.println("4. نمایش پیام‌ها");
            System.out.println("5. انتقال وجه به حساب مشتری دیگر");
            System.out.println("6. نمایش موجودی حساب‌های من");
            System.out.println("7. بازگشت به منوی اصلی");
            System.out.print("انتخاب شما: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch(choice) {
                case 1:
                    boolean accountCreated = false;
                    while (!accountCreated) {
                        try {
                            System.out.println("عملیات باز کردن حساب در حال اجرا...");
                            System.out.print("لطفاً شماره حساب جدید (13 رقمی) را وارد کنید: ");
                            String newAccountNumber = scanner.nextLine();
                            System.out.print("لطفاً مبلغ اولیه واریز را وارد کنید: ");
                            int initialDeposit = scanner.nextInt();
                            scanner.nextLine();
                            System.out.print("لطفاً رمز عبور حساب را وارد کنید: ");
                            String accountPassword = scanner.nextLine();
                            CurrentAccount newAccount = new CurrentAccount(newAccountNumber, customer, initialDeposit, accountPassword);
                            currentBranch.addAccount(newAccount);
                            customer.openAccount(newAccount);
                            System.out.println("حساب جدید با شماره " + newAccountNumber + " ایجاد و به مشتری افزوده شد.");
                            accountCreated = true;
                        } catch (IllegalArgumentException ex) {
                            System.out.println("خطا: " + ex.getMessage());
                            System.out.println("لطفاً ورودی‌های خود را بررسی کرده و دوباره تلاش کنید.");
                        }
                    }
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
                        customer.transferBetweenOwnAccounts(fromAccount, toAccount, amount, password);
                        System.out.println("انتقال وجه با موفقیت انجام شد.");
                    } catch (Exception e) {
                        System.out.println("خطا در انتقال وجه: " + e.getMessage());
                    }
                    break;
                case 3:
                    System.out.println("ارسال درخواست وام...");
                    System.out.print("متن درخواست وام را وارد کنید: ");
                    String loanText = scanner.nextLine();
                    Request loanRequest = new Request(RequestType.LOAN_REQUEST, loanText, customer);
                    customer.getMessageBox().addRequest(loanRequest);
                    System.out.println("درخواست وام شما ثبت شد.");
                    break;
                case 4:
                    System.out.println("نمایش پیام‌های مشتری:");
                    customer.getMessageBox().printAll();
                    break;
                case 5:
                    System.out.println("انتقال وجه به حساب مشتری دیگر...");
                    System.out.print("شماره حساب مبدا: ");
                    String srcAccount = scanner.nextLine();
                    System.out.print("مبلغ انتقال: ");
                    int transferAmount = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("رمز عبور حساب مبدا: ");
                    String srcPwd = scanner.nextLine();
                    System.out.println("انتخاب مشتری مقصد:");
                    Customer destCustomer = selectCustomer(scanner, bank);
                    if(destCustomer == null) {
                        System.out.println("مشتری مقصد پیدا نشد.");
                        break;
                    }
                    System.out.print("شماره حساب مقصد: ");
                    String destAccount = scanner.nextLine();

                    CurrentAccount sourceAcc = (CurrentAccount) customer.findAccount(srcAccount);
                    CurrentAccount destinationAcc = (CurrentAccount) destCustomer.findAccount(destAccount);

                    if(sourceAcc == null) {
                        System.out.println("حساب مبدا پیدا نشد.");
                    } else if(destinationAcc == null) {
                        System.out.println("حساب مقصد در مشتری " + destCustomer.getFullName() + " پیدا نشد.");
                    } else {
                        try {
                            sourceAcc.secureWithdraw(transferAmount, srcPwd);
                            destinationAcc.deposit(transferAmount);
                            System.out.println("انتقال وجه از " + customer.getFullName() + " به " +
                                    destCustomer.getFullName() + " با موفقیت انجام شد.");
                        } catch (IncorrectPasswordException | InvalidAmountException | InsufficientBalanceException ex) {
                            System.out.println("خطا در انتقال وجه: " + ex.getMessage());
                        }
                    }
                    break;
                case 6:
                    System.out.println("نمایش موجودی حساب‌های " + customer.getFullName() + ":");
                    // فرض بر این است که Customer دارای متد getAccounts() است که لیست حساب‌ها را برمی‌گرداند
                    List<Account> accounts = customer.getAccounts();
                    if (accounts == null || accounts.isEmpty()) {
                        System.out.println("هیچ حسابی برای این مشتری ثبت نشده است.");
                    } else {
                        for (Account accItem : accounts) {
                            System.out.println("شماره حساب: " + accItem.getAccountId() + " - موجودی: " + accItem.getBalance());
                        }
                    }
                    break;
                case 7:
                    exit = true;
                    break;
                default:
                    System.out.println("گزینه نامعتبر. لطفاً دوباره تلاش کنید.");
            }
        }
    }

    // متد ثبت مشتری جدید (ثبت نام مشتری از طریق منوی اصلی)
    static void processNewCustomer(Scanner scanner, Bank bank , Branch branch) {
        System.out.println("\n--- ثبت مشتری جدید ---");
        System.out.print("نام: ");
        String firstName = scanner.nextLine();
        System.out.print("نام خانوادگی: ");
        String lastName = scanner.nextLine();
        System.out.print("تاریخ تولد (YYYY-MM-DD): ");
        String birthDate = scanner.nextLine();
        System.out.print("کد ملی: ");
        String nationalCode = scanner.nextLine();
        System.out.print("آدرس: ");
        String address = scanner.nextLine();
        System.out.print("شماره تلفن: ");
        String phone = scanner.nextLine();
        System.out.print("شناسه مشتری: ");
        String customerId = scanner.nextLine();

        Customer newCustomer = new Customer(firstName, lastName, birthDate, nationalCode, address, phone, customerId);
        bank.addCustomer(newCustomer);
        branch.addCustomer(newCustomer);
        System.out.println("مشتری جدید با شناسه " + customerId + " ثبت شد.");
    }

    // منوی دستیار شعبه: بررسی درخواست‌های بستن حساب و وام، نمایش اطلاعات شعبه
    static void processAssistantManager(Scanner scanner, Branch branch, Customer customer) {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n--- منوی دستیار شعبه ---");
            System.out.println("1. بررسی درخواست‌های بستن حساب");
            System.out.println("2. بررسی درخواست‌های وام");
            System.out.println("3. نمایش اطلاعات شعبه");
            System.out.println("4. بازگشت به منوی اصلی");
            System.out.print("انتخاب شما: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch(choice) {
                case 1:
                    List<Request> closeRequests = customer.getMessageBox().getAllRequests()
                            .stream()
                            .filter(r -> r.getType() == RequestType.CLOSE_ACCOUNT && r.getStatus().equals("pending"))
                            .collect(Collectors.toList());
                    if (closeRequests.isEmpty()) {
                        System.out.println("هیچ درخواست بستن حساب معلقی پیدا نشد.");
                    } else {
                        Request req = closeRequests.get(0);
                        System.out.println("درخواست بستن حساب: " + req.toString());
                        System.out.print("آیا این درخواست تایید شود؟ (1 = تایید، 2 = رد): ");
                        int decision = scanner.nextInt();
                        scanner.nextLine();
                        if (decision == 1) {
                            req.setStatus("approved");
                            System.out.println("درخواست تأیید شد.");
                        } else {
                            req.setStatus("rejected");
                            System.out.println("درخواست رد شد.");
                        }
                    }
                    break;
                case 2:
                    List<Request> loanRequests = customer.getMessageBox().getAllRequests()
                            .stream()
                            .filter(r -> r.getType() == RequestType.LOAN_REQUEST && r.getStatus().equals("pending"))
                            .collect(Collectors.toList());
                    if (loanRequests.isEmpty()) {
                        System.out.println("هیچ درخواست وام معلقی پیدا نشد.");
                    } else {
                        Request loanReq = loanRequests.get(0);
                        System.out.println("درخواست وام: " + loanReq.toString());
                        System.out.print("آیا این درخواست تایید شود؟ (1 = تایید، 2 = رد): ");
                        int decision = scanner.nextInt();
                        scanner.nextLine();
                        if (decision == 1) {
                            loanReq.setStatus("approved");
                            System.out.println("درخواست تأیید شد.");
                        } else {
                            loanReq.setStatus("rejected");
                            System.out.println("درخواست رد شد.");
                        }
                    }
                    break;
                case 3:
                    branch.displayInfo();
                    break;
                case 4:
                    exit = true;
                    break;
                default:
                    System.out.println("گزینه نامعتبر.");
            }
        }
    }

    // منوی تحویل‌دار: پردازش واریز/برداشت (با استفاده از secureWithdraw) و ارجاع درخواست معلق
    static void processTeller(Scanner scanner, Branch branch, Customer customer) {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n--- منوی تحویل‌دار ---");
            System.out.println("1. پردازش واریز/برداشت");
            System.out.println("2. ارجاع اولین درخواست معلق به دستیار شعبه");
            System.out.println("3. نمایش اطلاعات شعبه");
            System.out.println("4. بازگشت به منوی اصلی");
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
                            System.out.println("واریز موفق. موجودی جدید: " + acc.getBalanceForbank());
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
                    List<Request> pending = customer.getMessageBox().getAllRequests()
                            .stream()
                            .filter(r -> r.getStatus().equals("pending"))
                            .collect(Collectors.toList());
                    if (pending.isEmpty()) {
                        System.out.println("هیچ درخواست معلقی وجود ندارد.");
                    } else {
                        Request req = pending.get(0);
                        req.setStatus("forwarded to assistant");
                        System.out.println("درخواست " + req.toString() + " به دستیار شعبه ارجاع شد.");
                    }
                    break;
                case 3:
                    branch.displayInfo();
                    break;
                case 4:
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
            System.out.println("1. تایید یا رد تمام درخواست‌های معلق");
            System.out.println("2. مشاهده عملکرد کلی شعبه");
            System.out.println("3. بازگشت به منوی اصلی");
            System.out.print("انتخاب شما: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch(choice) {
                case 1:
                    List<Request> allPending = customer.getMessageBox().getAllRequests()
                            .stream()
                            .filter(r -> r.getStatus().equals("pending") || r.getStatus().equals("forwarded to assistant"))
                            .collect(Collectors.toList());
                    if (allPending.isEmpty()) {
                        System.out.println("هیچ درخواست معلقی وجود ندارد.");
                    } else {
                        for (Request req : allPending) {
                            System.out.println("درخواست: " + req.toString());
                            System.out.print("آیا تایید شود؟ (1 = تایید، 2 = رد): ");
                            int decision = scanner.nextInt();
                            scanner.nextLine();
                            if (decision == 1) {
                                req.setStatus("approved");
                                System.out.println("درخواست تأیید شد.");
                            } else {
                                req.setStatus("rejected");
                                System.out.println("درخواست رد شد.");
                            }
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
            System.out.println("3. نمایش اطلاعات شعبه");
            System.out.println("4. بازگشت به منوی اصلی");
            System.out.print("انتخاب شما: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch(choice) {
                case 1:
                    System.out.println("ایجاد تحویل‌دار جدید...");
                    System.out.print("شماره تحویل‌دار: ");
                    String tellerId = scanner.nextLine();
                    System.out.print("نام: ");
                    String tellerFirstName = scanner.nextLine();
                    System.out.print("نام خانوادگی: ");
                    String tellerLastName = scanner.nextLine();
                    System.out.print("تاریخ تولد: ");
                    String tellerBirthDay = scanner.nextLine();
                    System.out.print("کد ملی: ");
                    String tellerNationalCode = scanner.nextLine();
                    System.out.print("آدرس: ");
                    String tellerAddress = scanner.nextLine();
                    System.out.print("شماره تلفن: ");
                    String tellerPhone = scanner.nextLine();

                    Teller newTeller = new Teller(tellerFirstName, tellerLastName, tellerBirthDay,
                            tellerNationalCode, tellerAddress, tellerPhone, tellerId);
                    branch.addTeller(newTeller);
                    bank.addEmployee(newTeller);
                    System.out.println("تحویل‌دار جدید با شناسه " + tellerId + " ایجاد شد.");
                    break;
                case 2:
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
                    System.out.print("آدرس: ");
                    String amAddress = scanner.nextLine();
                    System.out.print("شماره تلفن: ");
                    String amPhone = scanner.nextLine();

                    AssistantManager newAM = new AssistantManager(amFirstName, amLastName, amBirthDay,
                            amNationalCode, amAddress, amPhone, amId);
                    branch.setAssistantManager(newAM);
                    bank.addEmployee(newAM);
                    System.out.println("معاون شعبه جدید با شناسه " + amId + " ایجاد شد.");
                    break;
                case 3:
                    branch.displayInfo();
                    break;
                case 4:
                    exit = true;
                    break;
                default:
                    System.out.println("گزینه نامعتبر.");
            }
        }
    }

}
