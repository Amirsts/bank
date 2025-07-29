package output.pages;


import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ServicesPage {
    public static Scene getServicesScene() {
        VBox root = new VBox(new Label("🎉 به صفحه خدمات خوش آمدید"));
        root.setStyle("-fx-alignment: center; -fx-padding: 50; -fx-font-size: 18px;");
        return new Scene(root, 360, 640);
    }
}

