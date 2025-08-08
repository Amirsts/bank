package output.pages;

import account.Account;
import branch.AssistantManager;
import branch.Branch;
import branch.BranchManager;
import branch.Teller;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import output.SceneManager;
import request.Request;
import request.RequestType;

import java.util.List;

public class MenuBankManager {
    public static Scene getBankManagerMenu() {

        //Presenting the font
        Font.loadFont(LoginPage.class.getResource("/fonts/Vazirmatn-Light.ttf").toExternalForm(), 14);

        // Root of page
        VBox root = new VBox(15);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #1c1f2e;");



        // Title
        Text title = new Text("منو رئیس بانک\n" + LoginPage.selectedManager.getFullName() + "\nشعبه درحال اجرا: " + SubMainPage.currentBranch.getBranchNumber());
        title.setFill(Color.LIGHTGRAY);
        title.setTextAlignment(TextAlignment.CENTER);
        title.setFont(Font.font("Vazirmatn", 20));

        //Buttons List
        String[] actions = {
                "ایجاد شعبه جدید",
                "تغییر شعبه",
                "ایجاد تحویلدار جدید",
                "ایجاد معاون شعبه جدید",
                "ایجاد رئیس شعبه جدید",
                "مشاهده اطلاعات بانک",
                "خروج از حساب"
        };


        VBox buttonBox = new VBox(12);
        buttonBox.setAlignment(Pos.CENTER);

        for (String action : actions) {

            Button btn = new Button(action);
            btn.setPrefWidth(260);
            btn.setId("normal-buttons");

            switch (action) {
                case "ایجاد شعبه جدید" :
                    btn.setOnAction(e -> SceneManager.switchTo("createNewBranch"));
                    break;
                case "تغییر شعبه" :
                    btn.setOnAction(e -> SceneManager.switchTo("changeBranch"));
                    break;
                case "ایجاد تحویلدار جدید" :
                    btn.setOnAction(e -> SceneManager.switchTo("newTeller"));
                    break;
                case "ایجاد معاون شعبه جدید" :
                    btn.setOnAction(e -> SceneManager.switchTo("newAssistant"));
                    break;
                case "ایجاد رئیس شعبه جدید" :
                    btn.setOnAction(e -> SceneManager.switchTo("newBranchManager"));
                    break;
                case "مشاهده اطلاعات بانک" :
                    btn.setOnAction(e -> SceneManager.switchTo("bankInfo"));
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





    public static Scene createNewBranch() {
        // Presenting the font
        Font.loadFont(LoginPage.class.getResource("/fonts/Vazirmatn-Light.ttf").toExternalForm(), 14);

        // Root
        VBox root = new VBox(15);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #1c1f2e;");

        VBox infoBox = new VBox(12);

        // Title
        Text title = new Text("ایجاد شعبه");
        title.setFill(Color.LIGHTGRAY);
        title.setTextAlignment(TextAlignment.CENTER);
        title.setFont(Font.font("Vazirmatn", 20));

        TextField branchCode = new TextField();
        branchCode.setPromptText("کد شعبه: ");
        branchCode.setId("information");


        Button register = new Button("تایید درخواست");
        register.setId("loginButton");
        register.setPrefWidth(260);
        register.setOnAction(e -> {
            Branch newBranch = new Branch(branchCode.getText());
            SubMainPage.bank.addBranch(newBranch);
            infoBox.getChildren().add(Methods.information("حتما پس از ایجاد شعبه اقدام به مشخص کردن مسئولان آن نمایید"));
        });

        infoBox.getChildren().addAll(branchCode , register);

        Button lastPage = new Button("بازگشت به صفحه قبلی");
        lastPage.setId("normal-buttons");
        lastPage.setPrefWidth(260);
        lastPage.setOnAction(e -> SceneManager.switchTo("getBankManagerMenu"));

        root.getChildren().addAll(title, infoBox, lastPage);

        Scene scene = new Scene(root, 360, 640);
        scene.getStylesheets().add(LoginPage.class.getResource("/assets/style.css").toExternalForm());
        return scene;
    }




    public static Scene changeBranch() {
        // Presenting the font
        Font.loadFont(LoginPage.class.getResource("/fonts/Vazirmatn-Light.ttf").toExternalForm(), 14);

        // Root
        VBox root = new VBox(15);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #1c1f2e;");

        VBox infoBox = new VBox(12);

        // Title
        Text title = new Text("تغییر شعبه جدید");
        title.setFill(Color.LIGHTGRAY);
        title.setTextAlignment(TextAlignment.CENTER);
        title.setFont(Font.font("Vazirmatn", 20));

        TextField branchCode = new TextField();
        branchCode.setPromptText("کد شعبه: ");
        branchCode.setId("information");


        Button register = new Button("تایید درخواست");
        register.setId("loginButton");
        register.setPrefWidth(260);
        register.setOnAction(e -> {
            if (SubMainPage.bank.findBranch(branchCode.getText()) == null){
                infoBox.getChildren().add(Methods.information("شعبه وارد شده موجود نمی باشد"));
            }else {
                SubMainPage.setCurrentBranch(SubMainPage.bank.findBranch(branchCode.getText()));
                infoBox.getChildren().add(Methods.information("شعبه درحال اجرا: " + branchCode.getText()));
            }
        });

        infoBox.getChildren().addAll(branchCode , register);

        Button lastPage = new Button("بازگشت به صفحه قبلی");
        lastPage.setId("normal-buttons");
        lastPage.setPrefWidth(260);
        lastPage.setOnAction(e -> SceneManager.switchTo("getBankManagerMenu"));

        root.getChildren().addAll(title, infoBox, lastPage);

        Scene scene = new Scene(root, 360, 640);
        scene.getStylesheets().add(LoginPage.class.getResource("/assets/style.css").toExternalForm());
        return scene;
    }





    public static Scene newTeller() {

        Font.loadFont(LoginPage.class.getResource("/fonts/Vazirmatn-Light.ttf").toExternalForm(), 14);

        // start of information box
        VBox infoBox = new VBox(0);
        infoBox.getStyleClass().add("login-box");

        // Title
        Text title = new Text("افزودن تحویلدار جدید");
        title.setFill(Color.LIGHTGRAY);
        title.setTextAlignment(TextAlignment.CENTER);
        title.setFont(Font.font("Vazirmatn", 20));


        TextField firstName = new TextField();
        firstName.setPromptText("نام:");
        firstName.setId("from-account");

        TextField lastName = new TextField();
        lastName.setPromptText("نام خانوادگی: ");
        lastName.setId("to-account");

        TextField birthday = new TextField();
        birthday.setPromptText("تاریخ تولد: ");
        birthday.setId("date");

        TextField nationalCode = new TextField();
        nationalCode.setPromptText("کد ملی: ");
        nationalCode.setId("amount");

        TextField address = new TextField();
        address.setPromptText("آدرس: ");
        address.setId("amount");

        TextField phoneNumber = new TextField();
        phoneNumber.setPromptText("شماره تلفن: ");
        phoneNumber.setId("amount");

        TextField passWord = new TextField();
        passWord.setPromptText("رمز: ");
        passWord.setId("password");

        infoBox.getChildren().addAll(firstName , lastName , nationalCode , address , phoneNumber , birthday , passWord);
        // END of information box


        Button register = new Button("ثبت");
        register.setId("loginButton");
        register.setOnAction(e -> {
            String tellerId = SubMainPage.bank.tellerIdCreator();
            Teller newTeller = new Teller(firstName.getText(), lastName.getText() , birthday.getText() , nationalCode.getText() ,
                    address.getText() , phoneNumber.getText() , tellerId , passWord.getText() );
            SubMainPage.bank.addEmployee(newTeller);
            SubMainPage.currentBranch.addTeller(newTeller);
            infoBox.getChildren().add(Methods.information("تحویدار جدید به شعبه: " + SubMainPage.currentBranch.getBranchNumber() + "افزوده شد"));
        });

        Button buttonReturn = new Button("بازگشت به صفحه قبلی");
        buttonReturn.setId("buttonReturn");
        buttonReturn.setOnAction(e ->  SceneManager.switchTo("getBankManagerMenu"));

        register.setOnAction(e -> {

        });
        VBox centerBox = new VBox(10,title , infoBox, register ,buttonReturn);
        centerBox.setAlignment(Pos.CENTER);


        BorderPane root = new BorderPane();
        root.setCenter(centerBox);
        root.setPadding(new Insets(20));

        Scene scene = new Scene(root, 360, 640);
        scene.getStylesheets().add(LoginPage.class.getResource("/assets/style.css").toExternalForm());

        return scene;
    }

    public static Scene newAssistant() {

        Font.loadFont(LoginPage.class.getResource("/fonts/Vazirmatn-Light.ttf").toExternalForm(), 14);

        // start of information box
        VBox infoBox = new VBox(0);
        infoBox.getStyleClass().add("login-box");

        // Title
        Text title = new Text("افزودن معاون شعبه جدید");
        title.setFill(Color.LIGHTGRAY);
        title.setTextAlignment(TextAlignment.CENTER);
        title.setFont(Font.font("Vazirmatn", 20));

        TextField firstName = new TextField();
        firstName.setPromptText("نام:");
        firstName.setId("from-account");

        TextField lastName = new TextField();
        lastName.setPromptText("نام خانوادگی: ");
        lastName.setId("to-account");

        TextField birthday = new TextField();
        birthday.setPromptText("تاریخ تولد: ");
        birthday.setId("date");

        TextField nationalCode = new TextField();
        nationalCode.setPromptText("کد ملی: ");
        nationalCode.setId("amount");

        TextField address = new TextField();
        address.setPromptText("آدرس: ");
        address.setId("amount");

        TextField phoneNumber = new TextField();
        phoneNumber.setPromptText("شماره تلفن: ");
        phoneNumber.setId("amount");

        TextField passWord = new TextField();
        passWord.setPromptText("رمز: ");
        passWord.setId("password");

        infoBox.getChildren().addAll(firstName , lastName , nationalCode , address , phoneNumber , birthday , passWord);
        // END of information box


        Button register = new Button("ثبت");
        register.setId("loginButton");
        register.setOnAction(e -> {
            String assistantId = SubMainPage.bank.assistantManagerIdCreator();
            AssistantManager newAssistant = new AssistantManager(firstName.getText(), lastName.getText() , birthday.getText() , nationalCode.getText() ,
                    address.getText() , phoneNumber.getText() , assistantId , passWord.getText() );
            SubMainPage.bank.addEmployee(newAssistant);
            SubMainPage.currentBranch.setAssistantManager(newAssistant);
            infoBox.getChildren().add(Methods.information("معاون جدید به شعبه: " + SubMainPage.currentBranch.getBranchNumber() + "افزوده شد"));
        });

        Button buttonReturn = new Button("بازگشت به صفحه قبلی");
        buttonReturn.setId("buttonReturn");
        buttonReturn.setOnAction(e ->  SceneManager.switchTo("getBankManagerMenu"));

        register.setOnAction(e -> {

        });
        VBox centerBox = new VBox(10,title , infoBox, register ,buttonReturn);
        centerBox.setAlignment(Pos.CENTER);


        BorderPane root = new BorderPane();
        root.setCenter(centerBox);
        root.setPadding(new Insets(20));

        Scene scene = new Scene(root, 360, 640);
        scene.getStylesheets().add(LoginPage.class.getResource("/assets/style.css").toExternalForm());

        return scene;
    }





    public static Scene newBranchManager() {

        Font.loadFont(LoginPage.class.getResource("/fonts/Vazirmatn-Light.ttf").toExternalForm(), 14);

        // start of information box
        VBox infoBox = new VBox(0);
        infoBox.getStyleClass().add("login-box");

        // Title
        Text title = new Text("افزودن رئیس شعبه جدید");
        title.setFill(Color.LIGHTGRAY);
        title.setTextAlignment(TextAlignment.CENTER);
        title.setFont(Font.font("Vazirmatn", 20));

        TextField firstName = new TextField();
        firstName.setPromptText("نام:");
        firstName.setId("from-account");

        TextField lastName = new TextField();
        lastName.setPromptText("نام خانوادگی: ");
        lastName.setId("to-account");

        TextField birthday = new TextField();
        birthday.setPromptText("تاریخ تولد: ");
        birthday.setId("date");

        TextField nationalCode = new TextField();
        nationalCode.setPromptText("کد ملی: ");
        nationalCode.setId("amount");

        TextField address = new TextField();
        address.setPromptText("آدرس: ");
        address.setId("amount");

        TextField phoneNumber = new TextField();
        phoneNumber.setPromptText("شماره تلفن: ");
        phoneNumber.setId("amount");

        TextField passWord = new TextField();
        passWord.setPromptText("رمز: ");
        passWord.setId("password");

        infoBox.getChildren().addAll(firstName , lastName , nationalCode , address , phoneNumber , birthday , passWord);
        // END of information box


        Button register = new Button("ثبت");
        register.setId("loginButton");
        register.setOnAction(e -> {
            String BranchManagerId = SubMainPage.bank.managerIdCreator();
            BranchManager newBranchManager = new BranchManager(firstName.getText(), lastName.getText() , birthday.getText() , nationalCode.getText() ,
                    address.getText() , phoneNumber.getText() , BranchManagerId , passWord.getText() );
            SubMainPage.bank.addEmployee(newBranchManager);
            SubMainPage.currentBranch.setBranchManager(newBranchManager);
            infoBox.getChildren().add(Methods.information("رئیس جدید به شعبه: " + SubMainPage.currentBranch.getBranchNumber() + "افزوده شد"));
        });

        Button buttonReturn = new Button("بازگشت به صفحه قبلی");
        buttonReturn.setId("buttonReturn");
        buttonReturn.setOnAction(e ->  SceneManager.switchTo("getBankManagerMenu"));

        register.setOnAction(e -> {

        });
        VBox centerBox = new VBox(10,title , infoBox, register ,buttonReturn);
        centerBox.setAlignment(Pos.CENTER);


        BorderPane root = new BorderPane();
        root.setCenter(centerBox);
        root.setPadding(new Insets(20));

        Scene scene = new Scene(root, 360, 640);
        scene.getStylesheets().add(LoginPage.class.getResource("/assets/style.css").toExternalForm());

        return scene;
    }





    public static Scene bankInfo() {
        // Presenting the font
        Font.loadFont(LoginPage.class.getResource("/fonts/Vazirmatn-Light.ttf").toExternalForm(), 14);

        // Root
        VBox root = new VBox(15);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #1c1f2e;");

        VBox infoBox = new VBox(12);
        infoBox.setId("info-box");

        infoBox.getChildren().addAll(Methods.information("تعداد شعب : " + SubMainPage.bank.getBranches().size()),
                Methods.information("نعداد رئیسان بانک: " + SubMainPage.bank.getBranchManagers().size()),
                Methods.information("نعداد معاونان بانک: " + SubMainPage.bank.getAssistantManagers().size()),
                Methods.information("نعداد تحویلداران بانک: " + SubMainPage.bank.getTellers().size()),
                Methods.information("تعداد مشتریان بانک: " + SubMainPage.bank.getCustomers().size() + "  تعداد حساب های بانک: " + SubMainPage.bank.getAccounts().size()),
                Methods.information("موجودی حساب های کوتاه مدت و جاری بانک: " +  SubMainPage.bank.getAllBalanceSC()),
                Methods.information("موجودی حساب های قرض الحسنه بانک: " + SubMainPage.bank.getAllBalanceQ()),
                Methods.information("موجودی کل بانک: " + SubMainPage.bank.getAllBalance() ));



        // Title
        Text title = new Text("مشخصات بانک\n");
        title.setTextAlignment(TextAlignment.CENTER);
        title.setFill(Color.LIGHTGRAY);
        title.setFont(Font.font("Vazirmatn", 20));



        Button lastPage = new Button("بازگشت به صفحه قبلی");
        lastPage.setId("normal-buttons");
        lastPage.setPrefWidth(260);
        lastPage.setOnAction(e -> SceneManager.switchTo("getBankManagerMenu"));

        root.getChildren().addAll(title, infoBox, lastPage);

        Scene scene = new Scene(root, 360, 640);
        scene.getStylesheets().add(LoginPage.class.getResource("/assets/style.css").toExternalForm());
        return scene;
    }

}
