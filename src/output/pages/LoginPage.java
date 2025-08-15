package output.pages;

import branch.AssistantManager;
import branch.BranchManager;
import branch.Teller;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import output.SceneManager;
import person.Customer;

public class LoginPage {
    static Customer selectedCustomer ;
    static Teller selectedTeller ;
    static AssistantManager selectedAssistant;
    static BranchManager selectedManager;

    public static Scene getLoginScene() {

        Font.loadFont(LoginPage.class.getResource("/fonts/Vazirmatn-Light.ttf").toExternalForm(), 14);

        Image logoImage = new Image(LoginPage.class.getResource("/assets/logo.png").toExternalForm());
        ImageView logoView = new ImageView(logoImage);
        logoView.setFitWidth(100);
        logoView.setPreserveRatio(true);

        // START of input box
        VBox inputBox = new VBox(0);
        inputBox.getStyleClass().add("login-box");


        TextField userNameField = new TextField();
        userNameField.setPromptText("نام کاربری");
        userNameField.setId("username");


        TextField passwordField = new TextField();
        passwordField.setPromptText("رمز عبور");
        passwordField.setId("password");
        passwordField.setOnAction(e -> {
            if (userNameField.getText().isEmpty()) {
                Methods.showErrorAlert("لطفا نام کاربری خود را وارد کنید");
            } else {

                String user = userNameField.getText();
                String pass = passwordField.getText();
                String employeeType = user.substring(0, 1);


                switch (employeeType) {

                    case "T":
                        handleTellerLogin(pass, user);
                        break;

                    case "A":
                        handleAssistantLogin(pass, user);
                        break;

                    case "M":
                        handleManagerLogin(pass, user);
                        break;
                    case "B":
                        handleBankManagerLogin(pass, user);
                        break;
                    default:
                        handleCustomerLogin(pass, user);
                        break;
                }
            }
        });

        inputBox.getChildren().addAll(userNameField, passwordField);
        // END of input box




        Button loginButton = new Button("ورود به بیت بانک");
        loginButton.setId("loginButton");
        loginButton.setOnAction(e -> {
            if (userNameField.getText().isEmpty()) {
                Methods.showErrorAlert("لطفا نام کاربری خود را وارد کنید");
            } else {

                String user = userNameField.getText();
                String pass = passwordField.getText();
                String employeeType = user.substring(0, 1);


                switch (employeeType) {

                    case "T":
                        handleTellerLogin(pass, user);
                        break;

                    case "A":
                        handleAssistantLogin(pass, user);
                        break;

                    case "M":
                        handleManagerLogin(pass, user);
                        break;
                    case "B":
                        handleBankManagerLogin(pass, user);
                        break;
                    default:
                        handleCustomerLogin(pass, user);
                        break;
                }
            }
        });



        // Version
        Text versionText = new Text("Version 3.0.1");
        versionText.getStyleClass().add("versionText");
        versionText.setFill(Color.GRAY);

        //creating new customer
        Hyperlink createNewCustomer = new Hyperlink("ایجاد مشتری جدید");
        createNewCustomer.getStyleClass().add("hyperlink");
        createNewCustomer.setOnAction(e -> SceneManager.switchTo("processNewCustomer"));

        VBox centerBox = new VBox(10, logoView,inputBox, loginButton, createNewCustomer);
        centerBox.setAlignment(Pos.CENTER);

        HBox bottomBox = new HBox(versionText);
        bottomBox.setAlignment(Pos.BOTTOM_CENTER);
        bottomBox.setPadding(new Insets(10));


        BorderPane root = new BorderPane();
        root.setCenter(centerBox);
        root.setBottom(bottomBox);
        root.setPadding(new Insets(20));

        Scene scene = new Scene(root, 360, 640);
        scene.getStylesheets().add(LoginPage.class.getResource("/assets/style.css").toExternalForm());


        return scene;
    }




    /* Login methods */

    private static void handleTellerLogin(String pass , String user) {
        if (SubMainPage.bank.findEmployee(user) != null ) {

            selectedTeller = (Teller) SubMainPage.bank.findEmployee(user);
            selectedTeller.displayInfo();
            if (selectedTeller.isPassWordTrue(pass)) {
                SceneManager.switchTo("getTellerMenu");
            }else {
                Methods.showErrorAlert("رمز وارد شده صحیح نمی باشد");
            }
        } else {
            Methods.showErrorAlert("نام کاربری تحویلدار وارد شده موجود نمی باشد");
        }
    }

    private static void handleAssistantLogin(String pass , String user) {
        if (SubMainPage.bank.findEmployee(user) != null ) {
            selectedAssistant = (AssistantManager) SubMainPage.bank.findEmployee(user);
            selectedAssistant.displayInfo();
            if (selectedAssistant.isPassWordTrue(pass)) {
                SceneManager.switchTo("getAssistantMenu");
            }else {
                Methods.showErrorAlert("رمز وارد شده صحیح نمی باشد");
            }
        } else {
            Methods.showErrorAlert("نام کاربری معاون شعبه وارد شده موجود نمی باشد");
        }
    }

    private static void handleManagerLogin(String pass , String user ) {
        if (SubMainPage.bank.findEmployee(user) != null ) {

            selectedManager = (BranchManager) SubMainPage.bank.findEmployee(user);
            selectedManager.displayInfo();
            if (selectedManager.isPassWordTrue(pass)) {
                SceneManager.switchTo("getManagerMenu");
            }else {
                Methods.showErrorAlert("رمز وارد شده صحیح نمی باشد");
            }
        } else {
            Methods.showErrorAlert("نام کاربری رئیس شعبه وارد شده موجود نمی باشد");
        }
    }

    private static void handleBankManagerLogin(String pass , String user ) {
        if (SubMainPage.bank.findEmployee(user) != null ) {

            selectedManager = (BranchManager) SubMainPage.bank.findEmployee(user);
            selectedManager.displayInfo();
            if (selectedManager.isPassWordTrue(pass)) {
                SceneManager.switchTo("getBankManagerMenu");
            }else {
                Methods.showErrorAlert("رمز وارد شده صحیح نمی باشد");
            }
        } else {
            Methods.showErrorAlert("نام کاربری  ووارد شده موجود نمی باشد");
        }
    }

    private static void handleCustomerLogin(String pass , String user) {
        if (SubMainPage.bank.findCustomerByID(user) != null ) {

            selectedCustomer = SubMainPage.bank.findCustomerByID(user);
            selectedCustomer.displayInfo();
            if (selectedCustomer.isPassWordTrue(pass)) {
                SceneManager.switchTo("customerMenu");
            }else {
                Methods.showErrorAlert("رمز وارد شده صحیح نمی باشد");
            }
        } else {
            Methods.showErrorAlert("نام کاربری وارد شدهS موجود نمی باشد");
        }
    }
}
