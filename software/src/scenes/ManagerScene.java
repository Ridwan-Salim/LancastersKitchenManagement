package scenes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class ManagerScene {

    final String IDLE_BUTTON_STYLE = "-fx-background-color:  #0077CC; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 16px; -fx-padding: 3 50;";
    final String HOVERED_BUTTON_STYLE = "-fx-background-color: #025692; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 16px; -fx-padding: 3 50; ";
    final String CLICKED_BUTTON_STYLE = "-fx-background-color: #002540; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 16px; -fx-padding: 3 50;";
    private SceneManager sceneManager = SceneManager.getInstance();
    public Scene createScene() {
        Label label = new Label("Welcome Manager!");
        StackPane layout = new StackPane(label);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        Button loginButton = new Button("Sign out");
        loginButton.setStyle(IDLE_BUTTON_STYLE);
        loginButton.setOnMouseEntered(e -> loginButton.setStyle(HOVERED_BUTTON_STYLE));
        loginButton.setOnMouseExited(e -> loginButton.setStyle(IDLE_BUTTON_STYLE));
        loginButton.setOnMouseClicked(e -> loginButton.setStyle(CLICKED_BUTTON_STYLE));

        loginButton.setOnAction(event -> {
            sceneManager.showScene("LOGIN");
            System.out.println("Back to login page");
        });

        return new Scene(layout, 600, 400);
    }
}
