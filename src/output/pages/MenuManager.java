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

public class MenuManager {
    public static Scene getManagerMenu() {

        //Presenting the font
        Font.loadFont(LoginPage.class.getResource("/fonts/Vazirmatn-Light.ttf").toExternalForm(), 14);

        // Root of page
        VBox root = new VBox(15);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #1c1f2e;");



        // Title
        Text title = new Text("منو رئیس شعبه\n" + LoginPage.selectedManager.getFullName());
        title.setFill(Color.LIGHTGRAY);
        title.setTextAlignment(TextAlignment.CENTER);
        title.setFont(Font.font("Vazirmatn", 20));

        //Buttons List
        String[] actions = {
                "تایید درخواست وام",
                "اطلاعات شعبه",
                "خروج از حساب"
        };


        VBox buttonBox = new VBox(12);
        buttonBox.setAlignment(Pos.CENTER);

        for (String action : actions) {

            Button btn = new Button(action);
            btn.setPrefWidth(260);
            btn.setId("normal-buttons");

            switch (action) {
                case "تایید درخواست وام" :
                    btn.setOnAction(e -> SceneManager.switchTo("forwardLoanMa"));
                    break;
                case "اطلاعات شعبه" :
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




    public static Scene forwardLoanMa() {
        // Presenting the font
        Font.loadFont(LoginPage.class.getResource("/fonts/Vazirmatn-Light.ttf").toExternalForm(), 14);

        // Root
        VBox root = new VBox(15);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #1c1f2e;");

        VBox handleInfo = new VBox(12); // نمایش بازخورد برای هر درخواست

        // Title
        Text title = new Text("منو رئیس شعبه\n" + LoginPage.selectedManager.getFullName());
        title.setFill(Color.LIGHTGRAY);
        title.setTextAlignment(TextAlignment.CENTER);
        title.setFont(Font.font("Vazirmatn", 20));

        // Buttons
        VBox buttonBox = new VBox(12);
        buttonBox.setAlignment(Pos.CENTER);

        List<Request> loanRequests = LoginPage.selectedManager.getMessageBox().getRequestsByType(RequestType.LOAN_REQUEST);

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
                    handleInfo.getChildren().add(Methods.buttonRequestLoanMa(request));
                });

                buttonBox.getChildren().add(btn);
            }
        }

        Button lastPage = new Button("بازگشت به صفحه قبلی");
        lastPage.setId("normal-buttons");
        lastPage.setPrefWidth(260);
        lastPage.setOnAction(e -> SceneManager.switchTo("getManagerMenu"));

        root.getChildren().addAll(title, buttonBox, handleInfo, lastPage);

        Scene scene = new Scene(root, 360, 640);
        scene.getStylesheets().add(LoginPage.class.getResource("/assets/style.css").toExternalForm());
        return scene;
    }





    public static Scene branchInfo() {
        // Presenting the font
        Font.loadFont(LoginPage.class.getResource("/fonts/Vazirmatn-Light.ttf").toExternalForm(), 14);

        // Root
        VBox root = new VBox(15);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #1c1f2e;");

        VBox infoBox = new VBox(12);
        infoBox.setId("info-box");

        infoBox.getChildren().addAll(Methods.information("شعبه: " + SubMainPage.currentBranch.getBranchNumber()),
                Methods.information("معاون شعبه: " + SubMainPage.currentBranch.getAssistantManager().getFullName()),
                Methods.information("تحویلداران شعبه: " + SubMainPage.currentBranch.getTellersName()),
                Methods.information("تعداد مشتریان شعبه: " + SubMainPage.currentBranch.getCustomers().size() + "  تعداد حساب های شعبه: " + SubMainPage.currentBranch.getAccounts().size()),
                Methods.information("موجودی حساب های کوتاه مدت و جاری شعبه: " +  SubMainPage.currentBranch.getCurrentShortTermBalance()),
                Methods.information("موجودی حساب های جاری شعبه: " + SubMainPage.currentBranch.getQarzAlhasanehBalance()),
                Methods.information("موجودی کل شعبه: " + SubMainPage.currentBranch.getBranchBalance() ));



        // Title
        Text title = new Text("مشخصات شعبه\n" + LoginPage.selectedManager.getFullName());
        title.setTextAlignment(TextAlignment.CENTER);
        title.setFill(Color.LIGHTGRAY);
        title.setFont(Font.font("Vazirmatn", 20));



        Button lastPage = new Button("بازگشت به صفحه قبلی");
        lastPage.setId("normal-buttons");
        lastPage.setPrefWidth(260);
        lastPage.setOnAction(e -> SceneManager.switchTo("getManagerMenu"));

        root.getChildren().addAll(title, infoBox, lastPage);

        Scene scene = new Scene(root, 360, 640);
        scene.getStylesheets().add(LoginPage.class.getResource("/assets/style.css").toExternalForm());
        return scene;
    }
}
