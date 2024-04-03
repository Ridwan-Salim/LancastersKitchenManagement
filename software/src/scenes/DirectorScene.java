package scenes;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class DirectorScene extends PersonalizableScene {

    public Scene createScene() {
        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(20));

        Label greetingLabel = new Label("Welcome Director " + employeeName + "!\uD83D\uDC4B");
        greetingLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        BorderPane.setAlignment(greetingLabel, Pos.TOP_LEFT);
        BorderPane.setMargin(greetingLabel, new Insets(10, 0, 0, 10));

        Timeline labelAnimation = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(greetingLabel.opacityProperty(), 0)),
                new KeyFrame(Duration.seconds(1), new KeyValue(greetingLabel.opacityProperty(), 1))
        );
        labelAnimation.play();

        ComboBox<String> roleDropdown = createRoleDropdown();
        roleDropdown.setStyle("-fx-font-size: 16px; -fx-background-color: #f0f0f0; -fx-border-color: #999999;-fx-background-radius: 20");
        BorderPane.setAlignment(roleDropdown, Pos.TOP_RIGHT);
        BorderPane.setMargin(roleDropdown, new Insets(-30, 10, 0, 0));

        Button logoutButton = createLogoutButton();
        BorderPane.setAlignment(logoutButton, Pos.BOTTOM_RIGHT);
        BorderPane.setMargin(logoutButton, new Insets(100, 10, 10, 100));

        layout.setTop(greetingLabel);
        layout.setRight(roleDropdown);
        layout.setBottom(logoutButton);

        return new Scene(layout, SCREEN_RES_WIDTH, SCREEN_RES_HEIGHT);
    }

    private ComboBox<String> createRoleDropdown() {
        ComboBox<String> roleDropdown = new ComboBox<>();
        roleDropdown.getItems().addAll("Lancaster", "Manager", "General Staff");
        roleDropdown.setValue("Lancaster");
        roleDropdown.setStyle("-fx-background-radius: 20;");

        roleDropdown.setOnMouseEntered(e -> roleDropdown.setStyle("-fx-font-size: 16px; -fx-background-color: #e0e0e0; -fx-border-color: #999999"));
        roleDropdown.setOnMouseExited(e -> roleDropdown.setStyle("-fx-font-size: 16px; -fx-background-color: #f0f0f0; -fx-border-color: #999999"));


        roleDropdown.setOnAction(event -> {
            String selectedRole = roleDropdown.getValue();
            if (selectedRole.equals("Manager")) {
                SceneManager.getInstance().showScene("MANAGER-" + employeeName);
            } else if (selectedRole.equals("General Staff")) {
                SceneManager.getInstance().showScene("GENERAL-" + employeeName);
            }
        });
        return roleDropdown;
    }
}