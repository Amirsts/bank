package output.pages;

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
import output.SceneManager;
import person.Customer;

public class LoginPage {
    static Customer selectedCustomer ;

    public static Scene getLoginScene() {

        Font.loadFont(LoginPage.class.getResource("/fonts/Vazirmatn-Light.ttf").toExternalForm(), 14);

        Image logoImage = new Image(LoginPage.class.getResource("/assets/logo.png").toExternalForm());
        ImageView logoView = new ImageView(logoImage);
        logoView.setFitWidth(100);
        logoView.setPreserveRatio(true);

        // start of input box
        VBox inputBox = new VBox(0);
        inputBox.getStyleClass().add("login-box");

        TextField userNameField = new TextField();
        userNameField.setPromptText("نام کاربری");
        userNameField.setId("username");

        TextField passwordField = new TextField();
        passwordField.setPromptText("رمز عبور");
        passwordField.setId("password");

        inputBox.getChildren().addAll(userNameField, passwordField);
        // END of input box

        Button loginButton = new Button("ورود به بیت بانک اصلی");
        loginButton.setOnAction(e -> {
            String user = userNameField.getText();
            String pass = passwordField.getText();
            if (SubMainPage.bank.findCustomerByID(user) != null ) {
                selectedCustomer = SubMainPage.selectCustomer(user);
                selectedCustomer.displayInfo();
                SceneManager.switchTo("customerMenu");
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "نام کاربری و رمز عبور را وارد کنید");
                alert.showAndWait();
            }
        });
        loginButton.setId("loginButton");
        // نسخه
        Text versionText = new Text("Version 3.0.1");
        versionText.getStyleClass().add("versionText");
        versionText.setFill(Color.GRAY);

        //creating new customer
        Hyperlink createNewCustomer = new Hyperlink("ایجاد مشتری جدید");
        createNewCustomer.getStyleClass().add("hyperlink");

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
}
