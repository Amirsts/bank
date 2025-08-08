package output.pages;

import branch.Branch;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
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
        Text title = new Text("منو رئیس بانک\n" + LoginPage.selectedManager.getFullName());
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
                    btn.setOnAction(e -> SceneManager.switchTo("branchInfo"));
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
        Text title = new Text("ایجاد شعبه جدید");
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
}
