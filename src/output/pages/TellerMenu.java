package output.pages;

import account.CurrentAccount;
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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import output.SceneManager;

public class TellerMenu {
    public static Scene getTellerMenu() {

        //Presenting the font
        Font.loadFont(LoginPage.class.getResource("/fonts/Vazirmatn-Light.ttf").toExternalForm(), 14);

        // Root of page
        VBox root = new VBox(15);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #1c1f2e;");



        // Title
        Text title = new Text("منو تحویلدار\n" + LoginPage.selectedTeller.getFullName());
        title.setFill(Color.LIGHTGRAY);
        title.setFont(Font.font("Vazirmatn", 20));

        //Buttons List
        String[] actions = {
                "واریز / برداشت",
                "ارسال درخواست وام به معاون شعبه",
                "تایید درخواست افتتاح حساب",
                "تایید درخواست بستن حساب",
                "خروج از حساب"
        };



        VBox buttonBox = new VBox(12);
        buttonBox.setAlignment(Pos.CENTER);

        for (String action : actions) {

            Button btn = new Button(action);
            btn.setPrefWidth(260);
            btn.setId("normal-buttons");

            switch (action) {
                case "واریز / برداشت" :
                    btn.setOnAction(e -> SceneManager.switchTo("depositWithdraw"));
                    break;
                case "ارسال درخواست وام به معاون شعبه" :
                    btn.setOnAction(e -> SceneManager.switchTo("transfer"));
                    break;
                case "تایید درخواست افتتاح حساب" :
                    btn.setOnAction(e -> SceneManager.switchTo("loanRequest"));
                    break;
                case "تایید درخواست بستن حساب":
                    break;
                case "خروج از حساب" :
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




    public static Scene depositWithdraw() {

        VBox root = new VBox(15);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #1c1f2e;");

        VBox handleInfo = new VBox(12);


        Font.loadFont(CustomerMenu.class.getResource("/fonts/Vazirmatn-Light.ttf").toExternalForm(), 16);

        // Title
        Text title = new Text("نوع عملیات را وارد کنید");
        title.setFill(Color.LIGHTGRAY);
        title.setFont(Font.font("Vazirmatn", 20));

        // Buttons
        Button deposit = new Button("واریز");
        deposit.setId("normal-buttons");
        deposit.setPrefWidth(260);
        deposit.setOnAction(e -> {
            handleInfo.getChildren().clear(); // پاک‌سازی برای جلوگیری از تکرار

            VBox infoBox = new VBox(10);
            infoBox.getStyleClass().add("login-box");

            TextField accountNumber = new TextField();
            accountNumber.setPromptText("شماره حساب:");
            accountNumber.setId("from-account");

            TextField amount = new TextField();
            amount.setPromptText("مبلغ:");
            amount.setId("password");

            Button register = new Button("واریز");
            register.setId("loginButton");
            register.setPrefWidth(260);

            register.setOnAction(a -> {
                infoBox.getChildren().clear();

                CurrentAccount currentAccount = (CurrentAccount) SubMainPage.currentBranch.findAccount(accountNumber.getText());
                if (currentAccount == null) {
                    NewCustomer.showErrorAlert("شماره حساب وارد شده موجود نمی‌باشد");
                } else {
                    currentAccount.deposit(Integer.parseInt(amount.getText()));

                    TextField reaction = new TextField();
                    reaction.setPromptText("مبلغ " + amount.getText() + "\nبه حساب " + currentAccount.getOwner().getFullName() + " واریز شد");
                    reaction.setId("password");
                    reaction.setEditable(false);

                    infoBox.getChildren().add(reaction);
                }
            });

            infoBox.getChildren().addAll(accountNumber, amount, register);
            handleInfo.getChildren().add(infoBox);
        });


        Button withdraw = new Button("برداشت");
        withdraw.setId("normal-buttons");
        withdraw.setPrefWidth(260);
        withdraw.setOnAction(e -> {
            handleInfo.getChildren().clear();

            VBox infoBox = new VBox(10);
            infoBox.getStyleClass().add("login-box");

            TextField accountNumber = new TextField();
            accountNumber.setPromptText("شماره حساب:");
            accountNumber.setId("from-account");

            TextField amount = new TextField();
            amount.setPromptText("مبلغ:");
            amount.setId("from-account");

            TextField passWord = new TextField();
            passWord.setPromptText("رمز:");
            passWord.setId("password");

            Button register = new Button("پرداخت");
            register.setId("loginButton");
            register.setPrefWidth(260);

            register.setOnAction(a -> {
                infoBox.getChildren().clear();

                CurrentAccount currentAccount = (CurrentAccount) SubMainPage.currentBranch.findAccount(accountNumber.getText());
                if (currentAccount == null) {
                    NewCustomer.showErrorAlert("شماره حساب وارد شده موجود نمی‌باشد");
                } else {
                    try {
                        currentAccount.secureWithdraw(Integer.parseInt(amount.getText()), passWord.getText());
                    } catch (IncorrectPasswordException | InvalidAmountException |
                             InsufficientBalanceException ex) {
                        System.out.println("Error in collection: " + ex.getMessage());
                    }

                    TextField reaction = new TextField();
                    reaction.setPromptText("مبلغ " + amount.getText() + "\nاز حساب " + currentAccount.getOwner().getFullName() + " برداشت شد");
                    reaction.setId("password");
                    reaction.setEditable(false);

                    infoBox.getChildren().add(reaction);
                }
            });

            infoBox.getChildren().addAll(accountNumber, amount, passWord, register);
            handleInfo.getChildren().add(infoBox);
        });

        Button lastPage = new Button("بازگشت به صفحه قبلی");
        lastPage.setId("normal-buttons");
        lastPage.setPrefWidth(260);
        lastPage.setOnAction(e -> SceneManager.switchTo("getTellerMenu"));

        root.getChildren().addAll(title, deposit, withdraw, handleInfo, lastPage);

        Scene scene = new Scene(root, 360, 640);
        scene.getStylesheets().add(LoginPage.class.getResource("/assets/style.css").toExternalForm());

        return scene;
    }

}
