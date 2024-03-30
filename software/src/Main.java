import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.Objects;
import scenes.*;

public class Main extends Application {

    private SceneManager sceneManager = SceneManager.getInstance();

    private static DBConnect dbConnect;

    public static void main(String[] args) throws SQLException {
        String line = connectToDatabase();
        if (!Objects.equals(line, "")) {
            System.out.println(line);
        }

        launch(args);
    }

    private static String connectToDatabase() throws SQLException {
        String adminUsername = "in2033t09_a";
        String adminPassword = "C9byIeKbkE0";

        String error = "";

        try {
            DBConnect dbConnect = new DBConnect(adminUsername, adminPassword);
        } catch (SQLException sqlException) {
            error = sqlException.getMessage();
        }

        return error;
    }

    private void setAspectRatio(Stage stage) {
        stage.widthProperty().addListener((observable, oldValue, newValue) -> {
            double newHeight = newValue.doubleValue() / scenes.Scene.ASPECT_RATIO;
            stage.setHeight(newHeight);
        });

        stage.heightProperty().addListener((observable, oldValue, newValue) -> {
            double newWidth = newValue.doubleValue() * scenes.Scene.ASPECT_RATIO;
            stage.setWidth(newWidth);
        });
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        sceneManager.setPrimaryStage(primaryStage);
        sceneManager.showScene("LOGIN");

        setAspectRatio(primaryStage);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Lancaster's Kitchen: Management Software");

        Image iconImage = new Image(getClass().getResourceAsStream("dat/img/chartIcon.png"));
        primaryStage.getIcons().add(iconImage);

        primaryStage.show();
    }
}