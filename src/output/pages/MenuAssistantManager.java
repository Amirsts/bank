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
import javafx.scene.text.TextAlignment;
import output.SceneManager;
import request.Request;
import request.RequestType;

import java.util.List;

public class MenuAssistantManager {
    public static Scene getAssistantMenu() {

        //Presenting the font
        Font.loadFont(LoginPage.class.getResource("/fonts/Vazirmatn-Light.ttf").toExternalForm(), 14);

        // Root of page
        VBox root = new VBox(15);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #1c1f2e;");



        // Title
        Text title = new Text("منو تحویلدار\n" + LoginPage.selectedAssistant.getFullName());
        title.setFill(Color.LIGHTGRAY);
        title.setTextAlignment(TextAlignment.CENTER);
        title.setFont(Font.font("Vazirmatn", 20));

        //Buttons
        VBox buttonBox = new VBox(12);
        buttonBox.setAlignment(Pos.CENTER);

        Button loanRequests = new Button("ارسال درخواست وام به رئیس شعبه");
        loanRequests.setPrefWidth(260);
        loanRequests.setId("normal-buttons");
        loanRequests.setOnAction(e -> SceneManager.switchTo("forwardLoanAs"));

        Button lastPage = new Button("خروج از حساب");
        lastPage.setPrefWidth(260);
        lastPage.setId("normal-buttons");
        lastPage.setOnAction(e -> SceneManager.switchTo("login"));

        buttonBox.getChildren().addAll(loanRequests , lastPage);

        root.getChildren().addAll(title, buttonBox);
        Scene scene = new Scene(root, 360, 640);
        scene.getStylesheets().add(LoginPage.class.getResource("/assets/style.css").toExternalForm());
        return scene;
    }

    public static Scene forwardLoanAs() {
        // Presenting the font
        Font.loadFont(LoginPage.class.getResource("/fonts/Vazirmatn-Light.ttf").toExternalForm(), 14);

        // Root
        VBox root = new VBox(15);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #1c1f2e;");

        VBox handleInfo = new VBox(12); // نمایش بازخورد برای هر درخواست

        // Title
        Text title = new Text("منو معاون شعبه\n" + LoginPage.selectedAssistant.getFullName());
        title.setFill(Color.LIGHTGRAY);
        title.setFont(Font.font("Vazirmatn", 20));

        // Buttons
        VBox buttonBox = new VBox(12);
        buttonBox.setAlignment(Pos.CENTER);

        List<Request> loanRequests = LoginPage.selectedAssistant.getMessageBox().getRequestsByType(RequestType.LOAN_REQUEST);

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
                    handleInfo.getChildren().add(Methods.buttonRequestLoanAs(request));
                });

                buttonBox.getChildren().add(btn);
            }
        }

        Button lastPage = new Button("بازگشت به صفحه قبلی");
        lastPage.setId("normal-buttons");
        lastPage.setPrefWidth(260);
        lastPage.setOnAction(e -> SceneManager.switchTo("getAssistantMenu"));

        root.getChildren().addAll(title, buttonBox, handleInfo, lastPage);

        Scene scene = new Scene(root, 360, 640);
        scene.getStylesheets().add(LoginPage.class.getResource("/assets/style.css").toExternalForm());
        return scene;
    }


}
