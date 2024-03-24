package scenes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class ManagerScene extends scenes.Scene {

    public Scene createScene() {
        VBox layout = new VBox(20); // VBox layout to stack nodes vertically
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        Label greetingLabel = new Label("Welcome Manager!");
        greetingLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Button logoutButton = new Button("Sign out");
        logoutButton.setOnAction(event -> SceneManager.getInstance().showScene("LOGIN"));
        logoutButton.setStyle(IDLE_BUTTON_STYLE);
        logoutButton.setOnMouseEntered(e -> logoutButton.setStyle(HOVERED_BUTTON_STYLE));
        logoutButton.setOnMouseExited(e -> logoutButton.setStyle(IDLE_BUTTON_STYLE));
        logoutButton.setOnMouseClicked(e -> logoutButton.setStyle(CLICKED_BUTTON_STYLE));

        layout.getChildren().addAll(greetingLabel, logoutButton);

        return new Scene(layout, SCREEN_RES_WIDTH, SCREEN_RES_HEIGHT);
    }
}
