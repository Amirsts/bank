package output.pages;

import account.Account;
import exceptions.InvalidNationalCodeException;
import exceptions.InvalidPhoneNumberException;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import output.SceneManager;
import person.Customer;

public class NewCustomer {

    public static Scene processNewCustomer() {

        Font.loadFont(LoginPage.class.getResource("/fonts/Vazirmatn-Light.ttf").toExternalForm(), 14);

        // start of information box
        VBox infoBox = new VBox(0);
        infoBox.getStyleClass().add("login-box");

        TextField firstName = new TextField();
        firstName.setPromptText("نام: ");
        firstName.setId("from-account");

        TextField lastName = new TextField();
        lastName.setPromptText("نام خانوادگی: ");
        lastName.setId("to-account");

        TextField birthDay = new TextField();
        birthDay.setId("DAName");
        birthDay.setPromptText("تاریخ تولد: ");

        TextField nationalCode = new TextField();
        nationalCode.setPromptText("شماره ملی: ");
        nationalCode.setId("amount");

        TextField address = new TextField();
        address.setPromptText("آدرس: ");
        address.setId("date");

        TextField phone = new TextField();
        phone.setPromptText("شماره تلفن: ");
        phone.setId("date");

        TextField customerID = new TextField();
        customerID.setPromptText("نام کاربری: ");
        customerID.setId("date");

        TextField passWord = new TextField();
        passWord.setPromptText("رمز عبور: ");
        passWord.setId("password");


        infoBox.getChildren().addAll(firstName, lastName , birthDay, nationalCode, address, phone, customerID, passWord);
        // END of information box


        Button register = new Button("ثبت اطلاعات");
        register.setId("loginButton");
        register.setOnAction(e -> {

            if (!SubMainPage.bank.isNationalCodeUnique(nationalCode.getText())) {
                showErrorAlert("کد ملی وارد شده تکراری است");
            } else if (nationalCode.getText() == null || !nationalCode.getText().matches("\\d{10}")) {
                showErrorAlert("کد ملی وارد صحیح نمی باشد");
            } else if (!SubMainPage.bank.isPhoneNumberUnique(phone.getText())) {
                showErrorAlert("شماره تلفن وارد شده تکراری است");
            } else if (phone.getText() == null || !phone.getText().matches("09\\d{9}")) {
                showErrorAlert("شماره تلفن وارد شده صحیح نمی باشد");
            } else if (! SubMainPage.bank.isCustomerIdUnique(customerID.getText()) ) {
                showErrorAlert("نام کاربری وارد شده تکراری است");
            }else {
                try {
                    Customer newCustomer = new Customer(firstName.getText(), lastName.getText(), birthDay.getText(), nationalCode.getText(), address.getText(), phone.getText(), customerID.getText(),passWord.getText() );
                    SubMainPage.bank.addCustomer(newCustomer);
                    SubMainPage.currentBranch.addCustomer(newCustomer);

                    TextField attention = new TextField();
                    attention.setPromptText(firstName.getText() + "  عزیز به بیت بانک خوش آمدید");
                    attention.setId("password");
                    attention.setAlignment(Pos.CENTER);
                    infoBox.getChildren().add(attention);

                } catch (InvalidNationalCodeException a) {
                    System.out.println(a.getMessage());
                } catch (InvalidPhoneNumberException a) {
                    System.out.println(a.getMessage());
                }
            }
        });


        Button buttonReturn = new Button("بازگشت به صفحه قبلی");
        buttonReturn.setId("buttonReturn");
        buttonReturn.setOnAction(e ->  SceneManager.switchTo("login"));

        VBox centerBox = new VBox(10,infoBox, register, buttonReturn );
        centerBox.setAlignment(Pos.CENTER);


        BorderPane root = new BorderPane();
        root.setCenter(centerBox);
        root.setPadding(new Insets(20));

        Scene scene = new Scene(root, 360, 640);
        scene.getStylesheets().add(LoginPage.class.getResource("/assets/style.css").toExternalForm());

        return scene;
    }




    protected static void showErrorAlert(String message) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setId("custom-alert");
        dialogPane.getStylesheets().add(LoginPage.class.getResource("/assets/style.css").toExternalForm());

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(LoginPage.class.getResource("/assets/logo.png").toExternalForm()));

        alert.showAndWait();
    }
}
