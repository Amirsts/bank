package output.pages;


import bank.Bank;
import branch.Branch;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import output.SceneManager;
import person.Customer;

public class CustomerMenu {
    Bank bank = SubMainPage.bank;
    Branch branch = SubMainPage.branch;
    Customer customer = LoginPage.selectedCustomer;

    public static Scene getCustomerMenu() {
        // ریشه صفحه
        VBox root = new VBox(15);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #1c1f2e;");

        // فونت دلخواه (اختیاری)
        Font font = Font.loadFont(CustomerMenu.class.getResource("/fonts/Vazirmatn-Light.ttf").toExternalForm(), 16);

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
            btn.setStyle(
                    "-fx-background-color: #2a2d3e; " +
                            "-fx-text-fill: #ffffff; " +
                            "-fx-background-radius: 10; " +
                            "-fx-font-size: 14px;"
            );
            switch (action) {
                case "افتتاح حساب جدید" :
                    btn.setOnAction(e -> SceneManager.switchTo("creatingNewAccount"));
                    break;
                case "انتقال وجه" :
                    btn.setOnAction(e -> System.out.println("انتخاب شد: " + action));
                    break;
                case "درخواست وام" :
                    btn.setOnAction(e -> System.out.println("انتخاب شد: " + action));
                    break;
                case "پرداخت اقساط وام" :
                    btn.setOnAction(e -> System.out.println("انتخاب شد: " + action));
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

        return new Scene(root, 360, 640);
    }




    public static Scene creatingNewAccount() {
        // ریشه صفحه
        VBox root = new VBox(15);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #1c1f2e;");

        // فونت دلخواه (اختیاری)
        Font font = Font.loadFont(CustomerMenu.class.getResource("/fonts/Vazirmatn-Light.ttf").toExternalForm(), 16);

        // عنوان
        Text title = new Text("نوع حساب خود را انتخاب کنید");
        title.setFill(Color.LIGHTGRAY);
        title.setFont(Font.font("Vazirmatn", 20));

        // لیست دکمه‌ها
        String[] actions = {
                "حساب جاری",
                "حساب کوتاه مدت",
                "حساب قرض الحسنه"
                ,"بازگشت به منوی قبلی"
        };

        VBox buttonBox = new VBox(12);
        buttonBox.setAlignment(Pos.CENTER);

        for (String action : actions) {
            Button btn = new Button(action);
            btn.setPrefWidth(260);
            btn.setStyle(
                    "-fx-background-color: #2a2d3e; " +
                            "-fx-text-fill: #ffffff; " +
                            "-fx-background-radius: 10; " +
                            "-fx-font-size: 14px;"
            );
            switch (action) {
                case "حساب جاری" :
                    btn.setOnAction(e -> System.out.println("انتخاب شد: " + action));
                    break;
                case " حساب کوتاه مدت" :
                    btn.setOnAction(e -> System.out.println("انتخاب شد: " + action));
                    break;
                case "حساب قرض الحسنه" :
                    btn.setOnAction(e -> System.out.println("انتخاب شد: " + action));
                    break;
                case "بازگشت به منوی قبلی" :
                    btn.setOnAction(e -> SceneManager.switchTo("customerMenu"));
                    break;
                default:
                    System.out.println("دکمه ناموجود انتخاب شده است");
            }

            buttonBox.getChildren().add(btn);
        }

        root.getChildren().addAll(title, buttonBox);

        return new Scene(root, 360, 640);
    }
}

