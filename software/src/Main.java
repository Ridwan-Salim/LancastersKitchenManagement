import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.Objects;
import scenes.*;

public class Main extends Application {

    private static final double ASPECT_RATIO = 16.0/9.0;
    private static final int SCREEN_RES_WIDTH = 1280;
    private static final int SCREEN_RES_HEIGHT = 720;

    private SceneManager sceneManager = SceneManager.getInstance();


    private static DBConnect dbConnect;
    public static void main(String[] args) throws SQLException {

        String line = connectToDatabase();
        if (Objects.equals(line, "") == false){
            System.out.println(line);
        }

        launch(args);

    }

    private static String connectToDatabase() throws SQLException{
        dbConnect = new DBConnect();
        // This is where you put the credentials Martin gave in email, use admin for DDL perms
        //String adminUsername = "in2033t09_a";
        //String adminPassword = "C9byIeKbkE0";

        //connect to your local mySQL server
        String adminUsername = "root";
        String adminPassword = "";

        String error = "";

        try{
            dbConnect.connectToAndQueryDatabase(adminUsername, adminPassword);
        }
        catch (SQLException sqlException){
            //sqlException.printStackTrace();
            error = sqlException.getMessage();
        }

        return error;

    }

    private void setAspectRatio(Stage stage){

        // set height

        stage.widthProperty().addListener((observable, oldValue, newValue) -> {
            double newHeight = newValue.doubleValue() / Main.ASPECT_RATIO;
            stage.setHeight(newHeight);
        });

        // set width

        stage.heightProperty().addListener((observable, oldValue, newValue) -> {
            double newWidth = newValue.doubleValue() * Main.ASPECT_RATIO;
            stage.setWidth(newWidth);
        });
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        sceneManager.setPrimaryStage(primaryStage);
        sceneManager.showScene("LOGIN");


        Login loginScene = new Login();
        StackPane root = loginScene.createLoginScene(primaryStage); // Assuming createLoginScene() returns the root of the login scene
        Scene scene = new Scene(root, SCREEN_RES_WIDTH, SCREEN_RES_HEIGHT);
        primaryStage.setScene(scene);

        setAspectRatio(primaryStage);
        primaryStage.setResizable(false);

        primaryStage.setTitle("Lancaster's Kitchen: Management Software");
        Image iconImage = new Image(getClass().getResourceAsStream("dat/img/chartIcon.png"));
        primaryStage.getIcons().add(iconImage);


        primaryStage.show();
    }
}