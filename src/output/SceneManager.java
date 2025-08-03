package output;

import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.function.Supplier;

public class SceneManager {
    private static Stage mainStage;
    private static final HashMap<String, Supplier<Scene>> scenes = new HashMap<>();

    public static void addScene(String name, Supplier<Scene> sceneSupplier) {
        scenes.put(name, sceneSupplier);
    }

    public static void switchTo(String name) {
        if (scenes.containsKey(name)) {
            mainStage.setScene(scenes.get(name).get());
            mainStage.show();
        } else {
            System.out.println("Scene not found: " + name);
        }
    }

    public static void setStage(Stage stage) {
        mainStage = stage;
    }
}
