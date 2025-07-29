package output;


import bank.Bank;
import branch.AssistantManager;
import branch.Branch;
import branch.BranchManager;
import branch.Teller;
import javafx.scene.Scene;
import javafx.stage.Stage;
import person.Customer;

import java.util.HashMap;


public class SceneManager {
    private static Stage mainStage;
    private static final HashMap<String, Scene> scenes = new HashMap<>();

    public static void addScene(String name, Scene scene) {
        scenes.put(name, scene);
    }

    public static void switchTo(String name) {
        if (scenes.containsKey(name)) {
            mainStage.setScene(scenes.get(name));
            mainStage.show();
        } else {
            System.out.println("Scene not found: " + name);
        }
    }

    public static void setStage(Stage stage) {
        mainStage = stage;
    }
}
