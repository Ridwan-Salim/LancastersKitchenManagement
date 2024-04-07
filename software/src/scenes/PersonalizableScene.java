package scenes;

import javafx.scene.Scene;
import javafx.scene.control.Button;

public abstract class PersonalizableScene {
    protected String employeeName = "";
    public Scene createScene(){return null;};
    public Scene createScene(boolean b){return null;};
    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }
    public static final double ASPECT_RATIO = 16.0/9.0;
    protected static final int SCREEN_RES_WIDTH = 1280;
    protected static final int SCREEN_RES_HEIGHT = 720;
    final String IDLE_BUTTON_STYLE = "-fx-background-color:  #0077CC; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 16px; -fx-padding: 3 50;";
    final String HOVERED_BUTTON_STYLE = "-fx-background-color: #025692; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 16px; -fx-padding: 3 50; ";
    final String CLICKED_BUTTON_STYLE = "-fx-background-color: #002540; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 16px; -fx-padding: 3 50;";
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
}
