package scenes;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javafx.geometry.Pos;

public abstract class PersonalizableScene {
    protected String employeeName = "";
    public Scene createScene(){return null;}
    public Scene createScene(boolean b){return null;}
    public Scene createScene(String name){return null;}
    public String getEmployeeName() {
        return employeeName;
    }
    protected String BOSS_NAME = "Lancaster";

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }
    public static final double ASPECT_RATIO = 16.0/9.0;
    protected static final int SCREEN_RES_WIDTH = 1280;
    protected static final int SCREEN_RES_HEIGHT = 720;
    final String IDLE_BUTTON_STYLE = "-fx-background-color:  #0077CC; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 16px; -fx-padding: 3 50;";
    final String HOVERED_BUTTON_STYLE = "-fx-background-color: #025692; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 16px; -fx-padding: 3 50; ";
    final String CLICKED_BUTTON_STYLE = "-fx-background-color: #002540; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 16px; -fx-padding: 3 50;";
    protected int INPUT_FIELD_WIDTH = 265;
    protected Button createLogoutButton()
    {
        Button logoutButton = new Button("Sign out");
        logoutButton.setOnAction(event -> SceneManager.getInstance().showScene("LOGIN"));
        logoutButton.setStyle(IDLE_BUTTON_STYLE);
        logoutButton.setPrefWidth(255);
        logoutButton.setOnMouseEntered(e -> logoutButton.setStyle(HOVERED_BUTTON_STYLE));
        logoutButton.setOnMouseExited(e -> logoutButton.setStyle(IDLE_BUTTON_STYLE));
        logoutButton.setOnMouseClicked(e -> logoutButton.setStyle(CLICKED_BUTTON_STYLE));
        return logoutButton;

    }
    protected Button createButton(String buttonText, EventHandler<ActionEvent> customAction) {
        Button doneButton = new Button(buttonText);
        doneButton.setAlignment(Pos.CENTER);
        doneButton.setOnAction(customAction);
        doneButton.setStyle("-fx-font-size: 14px; -fx-padding: 8px 16px; -fx-background-radius: 5px; -fx-text-fill: white; -fx-background-color: #2196f3;");
        doneButton.setStyle(IDLE_BUTTON_STYLE);
        doneButton.setOnMouseEntered(e -> doneButton.setStyle(HOVERED_BUTTON_STYLE));
        doneButton.setOnMouseExited(e -> doneButton.setStyle(IDLE_BUTTON_STYLE));
        doneButton.setOnMouseClicked(e -> doneButton.setStyle(CLICKED_BUTTON_STYLE));
        return doneButton;
    }
    protected Button createButton(String buttonText, EventHandler<ActionEvent> customAction, int s1, int s2, int s3) {
        Button doneButton = new Button(buttonText);
        doneButton.setAlignment(Pos.CENTER);
        doneButton.setOnAction(customAction);
        String buttonStyle = "-fx-background-color: #2196f3; -fx-text-fill: white; -fx-background-radius: 2; -fx-font-size: 14px; -fx-padding: 3 10;";
        doneButton.setStyle(buttonStyle);
        doneButton.setStyle("-fx-background-color: #2196f3; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 14px; -fx-padding: 3 10;");
        doneButton.setOnMouseEntered(e -> doneButton.setStyle("-fx-background-color: #025692; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 14px; -fx-padding: 3 10;"));
        doneButton.setOnMouseExited(e -> doneButton.setStyle("-fx-background-color: #2196f3; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 14px; -fx-padding: 3 10;"));
        doneButton.setOnMouseClicked(e -> doneButton.setStyle("-fx-background-color: #002540; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 14px; -fx-padding: 3 10;"));
        return doneButton;
    }
    protected Button createBackButtonManager()
    {
        Button back = new Button("Back");
        back.setPrefWidth(INPUT_FIELD_WIDTH);
        back.setOnAction(event -> backButtonManager());
        back.setStyle(IDLE_BUTTON_STYLE);
        back.setOnMouseEntered(e -> back.setStyle(HOVERED_BUTTON_STYLE));
        back.setOnMouseExited(e -> back.setStyle(IDLE_BUTTON_STYLE));
        back.setOnMouseClicked(e -> back.setStyle(CLICKED_BUTTON_STYLE));
        return back;
    }
    protected Button createBackButtonDirector()
    {
        Button back = new Button("Back");
        back.setPrefWidth(INPUT_FIELD_WIDTH);
        back.setOnAction(event -> backButtonManager(true));
        back.setStyle(IDLE_BUTTON_STYLE);
        back.setOnMouseEntered(e -> back.setStyle(HOVERED_BUTTON_STYLE));
        back.setOnMouseExited(e -> back.setStyle(IDLE_BUTTON_STYLE));
        back.setOnMouseClicked(e -> back.setStyle(CLICKED_BUTTON_STYLE));
        return back;
    }
    private void backButtonManager(boolean toggle) {
        SceneManager sceneManager = SceneManager.getInstance();
        String userInfo = getUserInfo(this.employeeName);
        sceneManager.showScene(userInfo.split(" - ")[0]);
    }
    private void backButtonManager() {
        SceneManager sceneManager = SceneManager.getInstance();
        if (this.employeeName.equals(BOSS_NAME)) {
            String userInfo = getUserInfoBoss(BOSS_NAME);
            sceneManager.showScene(userInfo.split(" - ")[0]);
        }
        else {
            String userInfo = getUserInfo(this.employeeName);
            sceneManager.showScene(userInfo.split(" - ")[0]);
        }
    }
    private String getUserInfoBoss(String name) {
        String filePath = "D:\\homework\\Lancasters\\vpp\\LancastersKitchenMgmt\\software\\src\\scenes\\usersInfo\\users_info.txt";
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" - ");
                if (parts.length > 1) {
                    if (parts[0].equals("MANAGER-"+name)) {
                        return line;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
    private String getUserInfo(String name) {
        String filePath = "D:\\homework\\Lancasters\\vpp\\LancastersKitchenMgmt\\software\\src\\scenes\\usersInfo\\users_info.txt";
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" - ");
                if (parts.length > 1) {
                    if (parts[1].equals(name)) {
                        return line;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}
