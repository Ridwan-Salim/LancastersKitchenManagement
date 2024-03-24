package scenes;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class SceneManager {
    private static SceneManager instance;
    private Map<String, Scene> scenes;

    private Stage primaryStage;

    private SceneManager() {
        scenes = new HashMap<>();
        scenes.put("general-staff", new GeneralStaffScene().createScene());
        scenes.put("manager", new ManagerScene().createScene());
        scenes.put("director", new DirectorScene().createScene());
        scenes.put("LOGIN", new Login().createLoginScene());
    }

    public static SceneManager getInstance() {
        if (instance == null) {
            instance = new SceneManager();
        }
        return instance;
    }

    public void showScene(String password) {
        Scene scene = scenes.get(password);
        if (scene != null) {
            primaryStage.setScene(scene);
        } else {
            showAlert("Invalid Password", "Invalid password entered. Please try again.", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}
