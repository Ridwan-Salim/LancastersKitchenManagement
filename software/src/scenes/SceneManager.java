package scenes;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class SceneManager {
    private static SceneManager instance;
    private Map<String, PersonalizableScene> scenes;

    private Stage primaryStage;

    private SceneManager() {
        scenes = new HashMap<>();

        PersonalizableScene regularScene = new GeneralStaffScene();

        PersonalizableScene managerScene = new ManagerScene();

        PersonalizableScene directorScene = new DirectorScene();

        PersonalizableScene bookingPredictionScene = new BookingPredictionScene();

        PersonalizableScene clockInScene = new ClockInScene();

        PersonalizableScene createSendOrderScene = new CreateSendOrderScene();

        PersonalizableScene editMenuScene = new EditMenuScene();

        PersonalizableScene salesAnalysisScene = new SalesAnalysisScene();

        PersonalizableScene sickHolidayRequestsScene = new SickHolidayRequestsScene();

        PersonalizableScene staffHoursScene = new StaffHoursScene();

        PersonalizableScene addWineMenuScene = new AddWineMenuScene();

        PersonalizableScene cellarScene = new WineCellarScene();


        scenes.put("regular", regularScene);
        scenes.put("manager", managerScene);
        scenes.put("director", directorScene);
        scenes.put("bookingprediction", bookingPredictionScene);
        scenes.put("clockin", clockInScene);
        scenes.put("createorder", createSendOrderScene);
        scenes.put("editmenuscene", editMenuScene);
        scenes.put("salesanalysis", salesAnalysisScene);
        scenes.put("sickholiday", sickHolidayRequestsScene);
        scenes.put("staffhoursscene", staffHoursScene);
        scenes.put("addwinemenu", addWineMenuScene);
        scenes.put("winecellar", cellarScene);
        scenes.put("login", new Login());
    }

    public static SceneManager getInstance() {
        if (instance == null) {
            instance = new SceneManager();
        }
        return instance;
    }

    public void showScene(String password) {
        String userInfo = getUserInfo(password);
        if (!userInfo.isEmpty()) {
            String[] userInfoParts = userInfo.split(" - ");
            String role = userInfoParts[2].toLowerCase(); // Convert role to lowercase for consistency
            String employeeName = userInfoParts[1];

            if (scenes.containsKey(role)) {
                PersonalizableScene scene = scenes.get(role);
                scene.setEmployeeName(employeeName);
                if (userInfoParts[0].contains("GENERAL") && userInfoParts[0].contains("Lancaster"))
                    primaryStage.setScene(scene.createScene(false));
                else if (userInfoParts[0].contains("GENERAL") && !userInfoParts[0].endsWith("chef"))
                    primaryStage.setScene(scene.createScene(true));
                else if (userInfoParts[0].contains("MANAGER-"))
                    primaryStage.setScene(scene.createScene(false));
                else
                    primaryStage.setScene(scene.createScene());
            } else {
                showAlert("Invalid Role", "Invalid role specified for the user.", Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Invalid Password", "Invalid password entered. Please try again.", Alert.AlertType.ERROR);
        }
    }

    // NOTE: you need to specify the full path to the file
    private String getUserInfo(String password) {
        String filePath = "D:\\homework\\Lancasters\\vpp\\LancastersKitchenMgmt\\software\\src\\scenes\\usersInfo\\users_info.txt";
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" - ");
                if (parts[0].equals(password)) {
                    return line;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}
