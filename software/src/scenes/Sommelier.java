package scenes;

import core.SceneManager;
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
import javafx.scene.layout.HBox;
import javafx.util.Duration;

public class Sommelier extends Personalisable {

    public Scene createScene() {
        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(20));

        Label greetingLabel = new Label("Welcome " + employeeName + "!\uD83D\uDC4B");
        greetingLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        BorderPane.setAlignment(greetingLabel, Pos.TOP_LEFT);
        BorderPane.setMargin(greetingLabel, new Insets(10, 0, 0, 10));

        Timeline labelAnimation = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(greetingLabel.opacityProperty(), 0)),
                new KeyFrame(Duration.seconds(1), new KeyValue(greetingLabel.opacityProperty(), 1))
        );
        labelAnimation.play();

        HBox buttonLayout = new HBox(20);
        buttonLayout.setAlignment(Pos.CENTER);
        buttonLayout.setPadding(new Insets(50));

        Button wineCellarButton = new Button("Your wine cellar");
        wineCellarButton.setPrefWidth(INPUT_FIELD_WIDTH);
        wineCellarButton.setOnAction(event -> wineCellar());
        wineCellarButton.setStyle(IDLE_BUTTON_STYLE);
        wineCellarButton.setOnMouseEntered(e -> wineCellarButton.setStyle(HOVERED_BUTTON_STYLE));
        wineCellarButton.setOnMouseExited(e -> wineCellarButton.setStyle(IDLE_BUTTON_STYLE));
        wineCellarButton.setOnMouseClicked(e -> wineCellarButton.setStyle(CLICKED_BUTTON_STYLE));

        Button addWineMenuButton = new Button("Add wine to menu");
        addWineMenuButton.setPrefWidth(INPUT_FIELD_WIDTH);
        addWineMenuButton.setOnAction(event -> addWineMenu());
        addWineMenuButton.setStyle(IDLE_BUTTON_STYLE);
        addWineMenuButton.setOnMouseEntered(e -> addWineMenuButton.setStyle(HOVERED_BUTTON_STYLE));
        addWineMenuButton.setOnMouseExited(e -> addWineMenuButton.setStyle(IDLE_BUTTON_STYLE));
        addWineMenuButton.setOnMouseClicked(e -> addWineMenuButton.setStyle(CLICKED_BUTTON_STYLE));


        buttonLayout.getChildren().addAll(wineCellarButton, addWineMenuButton);
        buttonLayout.setSpacing(200);
        buttonLayout.setAlignment(Pos.CENTER);

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
        layout.setCenter(buttonLayout);

        return new Scene(layout, SCREEN_RES_WIDTH, SCREEN_RES_HEIGHT);
    }

    private void addWineMenu() {
        SceneManager sceneManager = SceneManager.getInstance();
        sceneManager.showScene(AddWineMenu.class.getSimpleName());
    }

    private void wineCellar() {
        SceneManager sceneManager = SceneManager.getInstance();
        sceneManager.showScene(WineCellar.class.getSimpleName());
    }

    private ComboBox<String> createRoleDropdown() {
        ComboBox<String> roleDropdown = new ComboBox<>();
        roleDropdown.getItems().addAll("Sommelier", "Manager", "General Staff");
        roleDropdown.setValue("Sommelier");
        roleDropdown.setStyle("-fx-background-radius: 20;");

        roleDropdown.setOnMouseEntered(e -> roleDropdown.setStyle("-fx-font-size: 16px; -fx-background-color: #e0e0e0; -fx-border-color: #999999"));
        roleDropdown.setOnMouseExited(e -> roleDropdown.setStyle("-fx-font-size: 16px; -fx-background-color: #f0f0f0; -fx-border-color: #999999"));


        roleDropdown.setOnAction(event -> {
            String selectedRole = roleDropdown.getValue();
            if (selectedRole.equals("Manager")) {
                SceneManager.getInstance().showScene(Manager.class.getSimpleName());
            } else if (selectedRole.equals("General Staff")) {
                SceneManager.getInstance().showScene(GeneralStaff.class.getSimpleName());
            }
        });
        return roleDropdown;
    }
}