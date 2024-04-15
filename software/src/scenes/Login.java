package scenes;

import core.DBConnect;
import core.SceneManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Login extends Personalisable {

    public Scene createScene() {
        Label titleLabel = new Label("Enter your password");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        PasswordField passwordField = new PasswordField();
        passwordField.setStyle("-fx-background-radius: 20");
        passwordField.setPrefHeight(30);
        StackPane passwordPane = new StackPane(passwordField);
        passwordPane.setMaxWidth(210);

        Button loginButton = new Button("Login");
        loginButton.setStyle(IDLE_BUTTON_STYLE);
        loginButton.setOnMouseEntered(e -> loginButton.setStyle(HOVERED_BUTTON_STYLE));
        loginButton.setOnMouseExited(e -> loginButton.setStyle(IDLE_BUTTON_STYLE));
        loginButton.setOnMouseClicked(e -> loginButton.setStyle(CLICKED_BUTTON_STYLE));

        loginButton.setOnAction(event -> {
            String password = passwordField.getText();
            String namerole = DBConnect.login(password);
            String[] split = namerole.split(":");
            SceneManager.getInstance().setName(split[0]);
            SceneManager.getInstance().setRole(split[1]);
            if (split[1].equals("Director")){
                SceneManager.getInstance().showScene(Sommelier.class.getSimpleName());
            }
            else{
                SceneManager.getInstance().showScene(split[1]);
            }
            System.out.println("Login button clicked with password: " + password);
        });

        // Load image
        Image image = new Image(getClass().getResourceAsStream("../dat/img/logo.png"));
        ImageView imageView = new ImageView(image);

        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        layout.getChildren().addAll(imageView, titleLabel, passwordPane, loginButton);

        StackPane root = new StackPane();
        root.getChildren().addAll(layout);
        StackPane.setAlignment(layout, Pos.CENTER);

        Scene scene = new Scene(root);
        return scene;
    }
}
