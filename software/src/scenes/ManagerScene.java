package scenes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ManagerScene extends PersonalizableScene {
    public Scene createScene() {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        Label greetingLabel = new Label("Welcome Manager " + employeeName + "!");
        greetingLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Button logoutButton = createLogoutButton();

        layout.getChildren().addAll(greetingLabel, createRoleDropdown(), logoutButton);

        return new Scene(layout, SCREEN_RES_WIDTH, SCREEN_RES_HEIGHT);
    }
    public Scene createScene(boolean toggleManager) {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        Label greetingLabel = new Label("Welcome Manager " + employeeName + "!");
        greetingLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Button logoutButton = createLogoutButton();

        layout.getChildren().addAll(greetingLabel, createRoleDropdownDirector(), logoutButton);

        return new Scene(layout, SCREEN_RES_WIDTH, SCREEN_RES_HEIGHT);
    }

    private ComboBox<String> createRoleDropdownDirector() {
        ComboBox<String> roleDropdown = new ComboBox<>();
        roleDropdown.getItems().addAll("Manager", "General Staff", "Lancaster");
        roleDropdown.setValue("Manager");
        roleDropdown.setOnAction(event -> {
            String selectedRole = roleDropdown.getValue();
            if (selectedRole.equals("General Staff")) {
                SceneManager.getInstance().showScene("GENERAL-" + employeeName);
            }
            if (selectedRole.equals("Lancaster")) {
                String userInfo = getUserInfo(employeeName);
                SceneManager.getInstance().showScene(userInfo.split(" - ")[0]);
            }
        });
        return roleDropdown;
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

    private ComboBox<String> createRoleDropdown() {
        ComboBox<String> roleDropdown = new ComboBox<>();
        roleDropdown.getItems().addAll("Manager", "General Staff");
        roleDropdown.setValue("Manager");
        roleDropdown.setOnAction(event -> {
            String selectedRole = roleDropdown.getValue();
            if (selectedRole.equals("General Staff")) {
                SceneManager.getInstance().showScene("GENERAL-" + employeeName);
            }
        });
        return roleDropdown;
    }
}

