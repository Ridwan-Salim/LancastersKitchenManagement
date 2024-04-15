package core;

import javafx.scene.control.Alert;
import javafx.stage.Stage;
import scenes.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class SceneManager {
    private static SceneManager instance;
    private Map<String, Personalisable> scenes;

    private Stage primaryStage;

    private String name = "";
    private String role = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    private SceneManager() {
        scenes = new HashMap<>();
        HashSet<Personalisable> nameSet = new HashSet<>();

        Personalisable generalStaffScene = new GeneralStaff();
        nameSet.add(generalStaffScene);

        Personalisable managerScene = new Manager();
        nameSet.add(managerScene);

        Personalisable sommelierScene = new Sommelier();
        nameSet.add(sommelierScene);

        Personalisable bookingPredictionScene = new BookingPrediction();
        nameSet.add(bookingPredictionScene);

        Personalisable clockInScene = new ClockIn();
        nameSet.add(clockInScene);

        Personalisable createSendOrderScene = new CreateSendOrder();
        nameSet.add(createSendOrderScene);

        Personalisable editMenuScene = new EditMenu();
        nameSet.add(editMenuScene);

        Personalisable salesAnalysisScene = new SalesAnalysis();
        nameSet.add(salesAnalysisScene);

        Personalisable sickHolidayRequests = new SickHolidayRequests();
        nameSet.add(sickHolidayRequests);

        Personalisable staffHoursScene = new StaffHours();
        nameSet.add(staffHoursScene);

        Personalisable wineMenu = new AddWineMenu();
        nameSet.add(wineMenu);

        Personalisable wineCellar = new WineCellar();
        nameSet.add(wineCellar);

        Personalisable loginScene = new Login();
        nameSet.add(loginScene);

        for (Personalisable i : nameSet){
            scenes.put(i.getClass().getSimpleName(),i);
        }
    }

    public static SceneManager getInstance() {
        if (instance == null) {
            instance = new SceneManager();
        }
        return instance;
    }

    public void resetCurrentStaff(){
        name = "";
        role = "";
    }

    public void showScene(String newScene) {

        if (newScene != null) {
            // test the param scene name for one in the hashmap
            // set the scene to the one in the map
            // use the new set name

            Personalisable scene;

            if (newScene.equals("Director")){
                scene = scenes.get(Sommelier.class.getSimpleName());
            }
            else{
                scene = scenes.get(newScene);
            }
            if (newScene.contains("General Staff")){
                scene = scenes.get(GeneralStaff.class.getSimpleName());
            }
            else{
                scene = scenes.get(newScene);
            }

            scene.setEmployeeName(name);
            scene.setEmployeeRole(role);

            if (newScene.equals(Login.class.getSimpleName())) {
                resetCurrentStaff();
                primaryStage.setScene(scene.createScene());
                return;
            }

            System.out.println(name + " : " + role);

            if (role.contains("General Staff")) {
                if (scene instanceof GeneralStaff) {
                    primaryStage.setScene(scene.createScene());
                }
                else{
                    return;
                }
            } else if (role.equals("Manager")) {
                if (scene instanceof Manager) {
                    primaryStage.setScene(scene.createScene());
                } else if (scene instanceof GeneralStaff) {
                    primaryStage.setScene(scene.createScene(true));
                }
                else{
                    return;
                }
            } else if (role.equals("Director")) {
                    primaryStage.setScene(scene.createScene(true));
            } else {
                showAlert("Invalid Role", "Invalid role specified for the user.", Alert.AlertType.ERROR);
            }

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
