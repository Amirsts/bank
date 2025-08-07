package output.pages;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import output.SceneManager;
import request.Request;
import request.RequestType;

import java.util.List;

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
                    btn.setOnAction(e -> SceneManager.switchTo("forwardLoan"));
                    break;
                case "تایید درخواست افتتاح حساب" :
                    btn.setOnAction(e -> SceneManager.switchTo("openAccountRequest"));
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
                Methods.buttonRegisterDeposit(infoBox , accountNumber.getText(), amount.getText());
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
                Methods.buttonRegisterWithdraw(infoBox , accountNumber.getText() , amount.getText() , passWord.getText());
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




    public static Scene forwardLoan() {
        // Presenting the font
        Font.loadFont(LoginPage.class.getResource("/fonts/Vazirmatn-Light.ttf").toExternalForm(), 14);

        // Root
        VBox root = new VBox(15);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #1c1f2e;");

        VBox handleInfo = new VBox(12); // نمایش بازخورد برای هر درخواست

        // Title
        Text title = new Text("منو تحویلدار\n" + LoginPage.selectedTeller.getFullName());
        title.setFill(Color.LIGHTGRAY);
        title.setFont(Font.font("Vazirmatn", 20));

        // Buttons
        VBox buttonBox = new VBox(12);
        buttonBox.setAlignment(Pos.CENTER);

        List<Request> loanRequests = LoginPage.selectedTeller.getMessageBox().getRequestsByType(RequestType.LOAN_REQUEST);

        if (loanRequests.isEmpty()) {
            TextField noRequest = new TextField("هیچ درخواستی برای وام وجود ندارد");
            noRequest.setId("repayInfo");
            noRequest.setEditable(false);
            noRequest.setAlignment(Pos.CENTER);
            buttonBox.getChildren().add(noRequest);
        } else {
            for (Request request : loanRequests) {

                Button btn = new Button(request.getSender().getFullName());
                btn.setPrefWidth(260);
                btn.setId("normal-buttons");

                btn.setOnAction(e -> {
                    handleInfo.getChildren().clear();
                    handleInfo.getChildren().add(Methods.buttonRequestLo(request));
                });

                buttonBox.getChildren().add(btn);
            }
        }

        Button lastPage = new Button("بازگشت به صفحه قبلی");
        lastPage.setId("normal-buttons");
        lastPage.setPrefWidth(260);
        lastPage.setOnAction(e -> SceneManager.switchTo("getTellerMenu"));

        root.getChildren().addAll(title, buttonBox, handleInfo, lastPage);

        Scene scene = new Scene(root, 360, 640);
        scene.getStylesheets().add(LoginPage.class.getResource("/assets/style.css").toExternalForm());
        return scene;
    }




    public static Scene openAccountRequest() {
        // Presenting the font
        Font.loadFont(LoginPage.class.getResource("/fonts/Vazirmatn-Light.ttf").toExternalForm(), 14);

        // Root
        VBox root = new VBox(15);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #1c1f2e;");

        VBox handleInfo = new VBox(12); // نمایش بازخورد برای هر درخواست

        // Title
        Text title = new Text("منو تحویلدار\n" + LoginPage.selectedTeller.getFullName());
        title.setFill(Color.LIGHTGRAY);
        title.setFont(Font.font("Vazirmatn", 20));

        // Buttons
        VBox buttonBox = new VBox(12);
        buttonBox.setAlignment(Pos.CENTER);

        List<Request> openAccountRequests = LoginPage.selectedTeller.getMessageBox().getRequestsByType(RequestType.OPEN_ACCOUNT);

        if (openAccountRequests.isEmpty()) {
            TextField noRequest = new TextField("هیچ درخواستی برای باز کردن حساب وجود ندارد");
            noRequest.setId("password");
            noRequest.setEditable(false);
            noRequest.setAlignment(Pos.CENTER);
            buttonBox.getChildren().add(noRequest);
        } else {
            title.setText("مشتریان در صف انتظار");
            for (Request request : openAccountRequests) {

                Button btn = new Button(request.getSender().getFullName());
                btn.setPrefWidth(260);
                btn.setId("normal-buttons");

                btn.setOnAction(e -> {
                    handleInfo.getChildren().clear();
                    handleInfo.getChildren().add(Methods.buttonRequestOp(request));
                });

                buttonBox.getChildren().add(btn);
            }
        }

        Button lastPage = new Button("بازگشت به صفحه قبلی");
        lastPage.setId("normal-buttons");
        lastPage.setPrefWidth(260);
        lastPage.setOnAction(e -> SceneManager.switchTo("getTellerMenu"));

        root.getChildren().addAll(title, buttonBox, handleInfo, lastPage);

        Scene scene = new Scene(root, 360, 640);
        scene.getStylesheets().add(LoginPage.class.getResource("/assets/style.css").toExternalForm());
        return scene;
    }
}
