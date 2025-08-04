package output.pages;


import account.Account;
import exceptions.IncorrectPasswordException;
import exceptions.InsufficientBalanceException;
import exceptions.InvalidAmountException;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import loan.BaseLoan;
import output.SceneManager;
import request.Request;
import request.RequestType;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CustomerMenu {
    public static BaseLoan baseLoan ;
    public static Scene getCustomerMenu() {
        // فونت دلخواه (اختیاری)
        Font.loadFont(LoginPage.class.getResource("/fonts/Vazirmatn-Light.ttf").toExternalForm(), 14);

        // ریشه صفحه
        VBox root = new VBox(15);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #1c1f2e;");



        // عنوان
        Text title = new Text("منوی خدمات");
        title.setFill(Color.LIGHTGRAY);
        title.setFont(Font.font("Vazirmatn", 20));

        // لیست دکمه‌ها
        String[] actions = {
                "افتتاح حساب جدید",
                "انتقال وجه",
                "درخواست وام",
                "پرداخت اقساط وام",
                "مشاهده پیام‌ها",
                "نمایش موجودی حساب من",
                "بستن حساب",
                "بازگشت به منوی اصلی"
        };



        VBox buttonBox = new VBox(12);
        buttonBox.setAlignment(Pos.CENTER);

        for (String action : actions) {
            Button btn = new Button(action);
            btn.setPrefWidth(260);
            btn.setId("normal-buttons");
            switch (action) {
                case "افتتاح حساب جدید" :
                    btn.setOnAction(e -> SceneManager.switchTo("creatingNewAccount"));
                    break;
                case "انتقال وجه" :
                    btn.setOnAction(e -> SceneManager.switchTo("transfer"));
                    break;
                case "درخواست وام" :
                    btn.setOnAction(e -> SceneManager.switchTo("loanRequest"));
                    break;
                case "پرداخت اقساط وام":
                    btn.setOnAction(e -> {
                        if (
                                LoginPage.selectedCustomer == null ||
                                        LoginPage.selectedCustomer.getActiveLoans() == null ||
                                        LoginPage.selectedCustomer.getActiveLoans().isEmpty()
                        ) {
                            System.out.println("56");
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle(null);
                            alert.setHeaderText(null);
                            alert.setContentText("شما دارای وام فعال نیستید.");

                            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                            stage.getIcons().clear();

                            DialogPane dialogPane = alert.getDialogPane();
                            dialogPane.setId("custom-alert");
                            dialogPane.getStylesheets().add(LoginPage.class.getResource("/assets/style.css").toExternalForm());

                            alert.showAndWait();
                        } else {
                            SceneManager.switchTo("loanRepayInfo");
                        }
                    });

                    break;

                case "مشاهده پیام‌ها" :
                    btn.setOnAction(e -> System.out.println("انتخاب شد: " + action));
                    break;
                case "نمایش موجودی حساب من" :
                    btn.setOnAction(e -> System.out.println("انتخاب شد: " + action));
                    break;
                case "بستن حساب" :
                    btn.setOnAction(e -> System.out.println("انتخاب شد: " + action));
                    break;
                case "بازگشت به منوی اصلی" :
                    btn.setOnAction(e -> SceneManager.switchTo("login"));
                    break;
                default:
                    System.out.println("دکمه ناموجود انتخاب شده است");
            }

            buttonBox.getChildren().add(btn);
        }

        root.getChildren().addAll(title, buttonBox);
        Scene scene = new Scene(root, 360, 640);
        scene.getStylesheets().add(LoginPage.class.getResource("/assets/style.css").toExternalForm());
        return scene;
    }




    public static Scene creatingNewAccount() {
        // ریشه صفحه
        VBox root = new VBox(15);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #1c1f2e;");

        // فونت دلخواه (اختیاری)
        Font.loadFont(CustomerMenu.class.getResource("/fonts/Vazirmatn-Light.ttf").toExternalForm(), 16);

        // عنوان
        Text title = new Text("نوع حساب خود را انتخاب کنید");
        title.setFill(Color.LIGHTGRAY);
        title.setFont(Font.font("Vazirmatn", 20));

        // لیست دکمه‌ها
        String[] actions = {
                "حساب جاری",
                "حساب کوتاه مدت",
                "حساب قرض الحسنه",
                "بازگشت به منوی قبلی",
        };

        VBox buttonBox = new VBox(12);
        buttonBox.setAlignment(Pos.CENTER);

        for (String action : actions) {
            Button btn = new Button(action);
            btn.setPrefWidth(260);
            btn.setId("normal-buttons");
            switch (action) {
                case "حساب جاری", "حساب کوتاه مدت", "حساب قرض الحسنه":
                    btn.setOnAction(e -> createRequest(action));
                    break;
                case "بازگشت به منوی قبلی" :
                    btn.setOnAction(e -> SceneManager.switchTo("customerMenu"));
                    break;
            }

            buttonBox.getChildren().add(btn);
        }


        root.getChildren().addAll(title, buttonBox);
        Scene scene = new Scene(root, 360, 640);
        scene.getStylesheets().add(LoginPage.class.getResource("/assets/style.css").toExternalForm());
        return scene;
    }

    public static void createRequest(String text) {
        if (LoginPage.selectedCustomer == null) {
            System.err.println("Customer is not selected!");
            return;
        }

        if (LoginPage.selectedCustomer.getMessageBox() == null) {

            System.err.println("MessageBox is not initialized for customer!");
            return;
        }

        if (SubMainPage.currentBranch == null || SubMainPage.currentBranch.getSolitudeTeller() == null) {
            System.err.println("Branch or Teller is not initialized!");
            return;
        }

        Request openAccountRequest = new Request(RequestType.OPEN_ACCOUNT, text, LoginPage.selectedCustomer);
        LoginPage.selectedCustomer.getMessageBox().addRequest(openAccountRequest);
        SubMainPage.currentBranch.getSolitudeTeller().receiveRequest(openAccountRequest);
        System.out.println("Your account creation request has been registered.");
        LoginPage.selectedCustomer.displayInfo();
        // نمایش پیام به کاربر
    }




    public static Scene transfer() {

        Font.loadFont(LoginPage.class.getResource("/fonts/Vazirmatn-Light.ttf").toExternalForm(), 14);

        // start of information box
        VBox infoBox = new VBox(0);
        infoBox.getStyleClass().add("login-box");

        TextField fromAccount = new TextField();
        fromAccount.setPromptText("شماره حساب مبدا");
        fromAccount.setId("from-account");

        TextField toAccount = new TextField();
        toAccount.setPromptText("شماره حساب مقصد");
        toAccount.setId("to-account");

        TextField destinationAccountName = new TextField();
        destinationAccountName.setEditable(false);
        destinationAccountName.setId("DAName");
        toAccount.setOnAction(e -> {
            String accNumber = toAccount.getText();
            Account acc = SubMainPage.bank.findAccount(accNumber);

            if (acc != null) {
                destinationAccountName.setPromptText("واریز به: " + acc.getOwner().getFullName());
            } else {
                destinationAccountName.setPromptText("حساب یافت نشد");
            }
        });

        TextField amount = new TextField();
        amount.setPromptText("مبلغ");
        amount.setId("amount");

        TextField date = new TextField();
        date.setPromptText("تاریخ را وارد کنید (01/01/1404)");
        date.setId("date");

        TextField passWord = new TextField();
        passWord.setPromptText("رمز");
        passWord.setId("password");

        infoBox.getChildren().addAll(fromAccount, toAccount , destinationAccountName, amount, date, passWord);
        // END of information box


        Button register = new Button("پرداخت");
        register.setId("loginButton");

        Button buttonReturn = new Button("بازگشت به صفحه قبلی");
        buttonReturn.setId("buttonReturn");
        buttonReturn.setOnAction(e ->  SceneManager.switchTo("customerMenu"));

        register.setOnAction(e -> {
            if (transferBetweenAccounts(fromAccount.getText(), toAccount.getText(), Integer.valueOf(amount.getText()), date.getText(), passWord.getText())) {
                 SceneManager.switchTo("customerMenu");
            } else {

                Alert alert = new Alert(Alert.AlertType.ERROR, "تراکنش انجام نشد");
                alert.showAndWait();
            }
        });
        VBox centerBox = new VBox(10,infoBox, register ,buttonReturn);
        centerBox.setAlignment(Pos.CENTER);


        BorderPane root = new BorderPane();
        root.setCenter(centerBox);
        root.setPadding(new Insets(20));

        Scene scene = new Scene(root, 360, 640);
        scene.getStylesheets().add(LoginPage.class.getResource("/assets/style.css").toExternalForm());

        return scene;
    }


    public static boolean transferBetweenAccounts(String FromAccount , String ToAccount , int Amount , String date , String PassWord) {

        System.out.println("Transfer funds between your accounts...");
        System.out.print("Originating account number: ");
        String fromAccount = FromAccount; // Receive inputs for the money transfer method
        System.out.print("Destination account number: ");
        String toAccount = ToAccount;
        String name = SubMainPage.bank.findAccount(toAccount).getOwner().getFullName();
        System.out.print("Destination customer is: " + name + "\nTransfer amount: ");
        int amount = Amount;
        System.out.println("Enter the payment date, for example (07/05/2025):");
        String inp = date;
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dateTransfer = LocalDate.parse(inp, format);
        System.out.print("Password: ");
        String password = PassWord;
        try {
            SubMainPage.bank.transferBetweenCustomers(fromAccount, toAccount, amount, password , dateTransfer); // Assigning values
            System.out.println("The transfer was successful.");
            return true;

        } catch (Exception e) {
            System.out.println("Error in money transfer: " + e.getMessage());
            return false;
        }

    }


    public static String loanType;
    public static String selectedAccount;
    public static double loanAmount;
    public static Scene loanRequest() {
        // ریشه صفحه
        VBox root = new VBox(15);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #1c1f2e;");

        // فونت دلخواه (اختیاری)
        Font.loadFont(CustomerMenu.class.getResource("/fonts/Vazirmatn-Light.ttf").toExternalForm(), 16);

        // عنوان
        Text title = new Text("نوع وام را انتخاب کنید");
        title.setFill(Color.LIGHTGRAY);
        title.setFont(Font.font("Vazirmatn", 20));

        // لیست دکمه‌ها
        String[] actions = {
                "وام عادی",
                "وام تسهیلات",
                "بازگشت به منوی قبلی",
        };


        VBox buttonBox = new VBox(12);
        buttonBox.setAlignment(Pos.CENTER);

        // باکس دکمه‌های حساب‌ها (در ابتدا خالی است)
        VBox accountButtonsBox = new VBox(10);
        accountButtonsBox.setAlignment(Pos.CENTER);


        for (String action : actions) {
            Button btn = new Button(action);
            btn.setPrefWidth(260);
            btn.setId("normal-buttons");


            switch (action) {
                case "وام عادی":
                    btn.setOnAction(e -> {
                        // هنگام کلیک، دکمه‌های حساب بارگذاری می‌شن
                        accountButtonsBox.getChildren().clear();
                        accountButtonsBox.getChildren().addAll(loanAccountsButton().getChildren());
                        loanType = "1";
                    });
                    break;
                case "وام تسهیلات" :
                    btn.setOnAction(e -> {
                        // هنگام کلیک، دکمه‌های حساب بارگذاری می‌شن
                        accountButtonsBox.getChildren().clear();
                        accountButtonsBox.getChildren().addAll(loanAccountsButton().getChildren());
                        loanType = "2";
                    });
                    break;
                case "بازگشت به منوی قبلی":
                    btn.setOnAction(e -> SceneManager.switchTo("customerMenu"));
                    break;
            }

            buttonBox.getChildren().add(btn);
        }


        root.getChildren().addAll(title, buttonBox ,accountButtonsBox);
        Scene scene = new Scene(root, 360, 640);
        scene.getStylesheets().add(LoginPage.class.getResource("/assets/style.css").toExternalForm());
        return scene;
    }

    public static void loanReq(String LoanType , String SelectedAccount, double LoanAmount){
        System.out.println("Send a loan request...");
        System.out.println("\nSelect loan type:" + "\n1.Regular loan" + "\n2.Facility loan");
        String loanType = LoanType;

        System.out.println("Enter the loan amount requested:"); // Getting loan amount
        double loanAmount = LoanAmount;
        // Assigning values
        Request loanRequest = new Request(RequestType.LOAN_REQUEST,LoginPage.selectedCustomer , loanType ,SelectedAccount, loanAmount);
        LoginPage.selectedCustomer.getMessageBox().addRequest(loanRequest);
        SubMainPage.currentBranch.getSolitudeTeller().receiveRequest(loanRequest);
        System.out.println("Your loan application has been registered.");
    }

    public static VBox loanAccountsButton() {
        VBox accBtn = new VBox(10);
        accBtn.setAlignment(Pos.CENTER);

        Text title = new Text("حساب مورد نظر را انتخاب کنید:");
        title.setFill(Color.LIGHTGRAY);
        title.setFont(Font.font("Vazirmatn", 15));
        title.setId("title-account");

        accBtn.getChildren().add(title);


        TextField amountField = new TextField();
        amountField.setPromptText("مبلغ درخواستی را وارد کنید:");
        amountField.setId("amount");
        amountField.setVisible(false);

        // loan accepting button
        Button submitLoan = new Button("ثبت درخواست وام");
        submitLoan.setId("loginButton");
        submitLoan.setVisible(false);

        submitLoan.setOnAction(e -> {
            String amount = amountField.getText();
            System.out.println("Loan requested with amount: " + amount);
            System.out.println(loanType);
            loanReq(loanType , selectedAccount ,Double.parseDouble(amountField.getText()));
        });

        List<Account> accounts = LoginPage.selectedCustomer.getAccounts();
        for (Account account : accounts) {
            Button accountButton = new Button(account.getAccountId());
            accountButton.setPrefWidth(260);
            accountButton.setId("normal-buttons");

            accountButton.setOnAction(e -> {
                System.out.println("Account selected: " + account.getAccountId());
                amountField.setVisible(true);
                submitLoan.setVisible(true);
                selectedAccount = account.getAccountId();

            });

            accBtn.getChildren().add(accountButton);
        }

        // در انتها، فیلد و دکمه را اضافه می‌کنیم
        accBtn.getChildren().addAll(amountField, submitLoan);

        return accBtn;
    }



    public static Scene loanRepayInfo() {
        Font.loadFont(LoginPage.class.getResource("/fonts/Vazirmatn-Light.ttf").toExternalForm(), 14);
        

        BaseLoan loan = LoginPage.selectedCustomer.getActiveLoans().get(0);

        // ساخت فرم اطلاعات
        VBox infoBox = new VBox(10);
        infoBox.getStyleClass().add("login-box");

        // فیلدهای اطلاعاتی
        String[] actions = {
                "Total loan amount",
                "Total amount refunded by customer",
                "Total amount of remaining installments",
                "Number of remaining installments",
                "Installment amount"
        };

        for (String action : actions) {
            TextField textField = new TextField();
            textField.setEditable(false);
            textField.setId("repayInfo");

            switch (action) {
                case "Total loan amount":
                    textField.setPromptText("مبلغ کل وام:  " + ((int) loan.getLoanAmount()));
                    break;
                case "Total amount refunded by customer":
                    textField.setPromptText("مبلغ کل بازپرداخت‌شده:  " + ((int) loan.getPaidAmount()));
                    break;
                case "Total amount of remaining installments":
                    textField.setPromptText("مبلغ اقساط باقیمانده:  " + ((int) loan.getRemainingAmount()));
                    break;
                case "Number of remaining installments":
                    textField.setPromptText("تعداد اقساط باقیمانده:  " + loan.getfDuration());
                    break;
                case "Installment amount":
                    textField.setId("password");
                    textField.setPromptText("مبلغ هر قسط:  " + ((int) loan.installmentPerMonth()));
                    break;
            }

            infoBox.getChildren().add(textField);
        }


        /* This page Buttons */

        Button nextStep = new Button("پرداخت قسط");
        nextStep.setId("loginButton");
        nextStep.setOnAction(e -> SceneManager.switchTo("loanRepayment"));

        Button buttonReturn = new Button("بازگشت به صفحه قبلی");
        buttonReturn.setId("buttonReturn");
        buttonReturn.setOnAction(e -> SceneManager.switchTo("customerMenu"));

        VBox centerBox = new VBox(10, infoBox, nextStep, buttonReturn);
        centerBox.setAlignment(Pos.CENTER);

        BorderPane root = new BorderPane();
        root.setCenter(centerBox);
        root.setPadding(new Insets(20));

        Scene scene = new Scene(root, 360, 640);
        scene.getStylesheets().add(LoginPage.class.getResource("/assets/style.css").toExternalForm());

        return scene;
    }

    public static Scene loanRepayment() {
        Font.loadFont(LoginPage.class.getResource("/fonts/Vazirmatn-Light.ttf").toExternalForm(), 14);


        BaseLoan loan = LoginPage.selectedCustomer.getActiveLoans().get(0);

        // getting information
        VBox infoBox = new VBox(10);
        infoBox.getStyleClass().add("login-box");

        TextField dateRepay = new TextField();
        dateRepay.setPromptText("تاریخ را وارد کنید(مثال 17/05/1402) : ");
        dateRepay.setId("repayInfo");

        TextField accountNumber = new TextField();
        accountNumber.setPromptText("شماره حساب خود را وارد کنید: ");
        accountNumber.setId("repayInfo");

        TextField passWord = new TextField();
        passWord.setPromptText("رمز خود را وارد کنید : ");
        passWord.setId("password");

        infoBox.getChildren().addAll(dateRepay, accountNumber,passWord );


        /* This page Buttons */

        Button register = new Button("پرداخت قسط");
        register.setId("loginButton");
        register.setOnAction(e -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate datePay = LocalDate.parse(dateRepay.getText() , formatter);

            Account cAccount = LoginPage.selectedCustomer.findAccount(accountNumber.getText());
            try {
                cAccount.secureWithdrawForLoan((int) loan.installmentPerMonth(), passWord.getText()); // Paying monthly installment
            } catch (IncorrectPasswordException a) {
                System.out.println(a.getMessage());
            } catch (InvalidAmountException b) {
                System.out.println(b.getMessage());
            } catch (InsufficientBalanceException c) {
                System.out.println(c.getMessage());
            }
        });

        Button buttonReturn = new Button("بازگشت به صفحه قبلی");
        buttonReturn.setId("buttonReturn");
        buttonReturn.setOnAction(e -> SceneManager.switchTo("loanRepayInfo"));

        VBox centerBox = new VBox(10, infoBox, register, buttonReturn);
        centerBox.setAlignment(Pos.CENTER);

        BorderPane root = new BorderPane();
        root.setCenter(centerBox);
        root.setPadding(new Insets(20));

        Scene scene = new Scene(root, 360, 640);
        scene.getStylesheets().add(LoginPage.class.getResource("/assets/style.css").toExternalForm());

        return scene;
    }

}




