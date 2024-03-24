package scenes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class GeneralStaffScene {

    final String IDLE_BUTTON_STYLE = "-fx-background-color:  #0077CC; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 16px; -fx-padding: 3 50;";
    final String HOVERED_BUTTON_STYLE = "-fx-background-color: #025692; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 16px; -fx-padding: 3 50; ";
    final String CLICKED_BUTTON_STYLE = "-fx-background-color: #002540; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 16px; -fx-padding: 3 50;";
    public Scene createScene(Stage primaryStage) {
        Label label = new Label("Welcome General Staff!");
        StackPane layout = new StackPane(label);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        return new Scene(layout, 600, 400);
    }
}
