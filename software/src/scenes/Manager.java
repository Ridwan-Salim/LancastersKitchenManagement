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
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Manager extends Personalisable {
    public Scene createScene() {
        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(20));


        Label greetingLabel = new Label("Welcome " + employeeName + "!");
        greetingLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        BorderPane.setAlignment(greetingLabel, Pos.TOP_LEFT);
        BorderPane.setMargin(greetingLabel, new Insets(10, 0, 0, 10));

        Timeline labelAnimation = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(greetingLabel.opacityProperty(), 0)),
                new KeyFrame(Duration.seconds(1), new KeyValue(greetingLabel.opacityProperty(), 1))
        );
        labelAnimation.play();

        VBox buttonLayout = new VBox(20);
        buttonLayout.setAlignment(Pos.CENTER);
        buttonLayout.setPadding(new Insets(50));
        Button clockButton = new Button("Clock In/Out");
        clockButton.setPrefWidth(INPUT_FIELD_WIDTH);
        clockButton.setOnAction(event -> clockInOut());
        clockButton.setStyle(IDLE_BUTTON_STYLE);
        clockButton.setOnMouseEntered(e -> clockButton.setStyle(HOVERED_BUTTON_STYLE));
        clockButton.setOnMouseExited(e -> clockButton.setStyle(IDLE_BUTTON_STYLE));
        clockButton.setOnMouseClicked(e -> clockButton.setStyle(CLICKED_BUTTON_STYLE));

        Button staffHours = new Button("Staff Hours");
        staffHours.setPrefWidth(INPUT_FIELD_WIDTH);
        staffHours.setOnAction(event -> staffHours());
        staffHours.setStyle(IDLE_BUTTON_STYLE);
        staffHours.setOnMouseEntered(e -> staffHours.setStyle(HOVERED_BUTTON_STYLE));
        staffHours.setOnMouseExited(e -> staffHours.setStyle(IDLE_BUTTON_STYLE));
        staffHours.setOnMouseClicked(e -> staffHours.setStyle(CLICKED_BUTTON_STYLE));

        Button sickHolidayRequests = new Button("Sick/Holiday requests");
        sickHolidayRequests.setPrefWidth(INPUT_FIELD_WIDTH);
        sickHolidayRequests.setOnAction(event -> sickHolidayRequests());
        sickHolidayRequests.setStyle(IDLE_BUTTON_STYLE);
        sickHolidayRequests.setOnMouseEntered(e -> sickHolidayRequests.setStyle(HOVERED_BUTTON_STYLE));
        sickHolidayRequests.setOnMouseExited(e -> sickHolidayRequests.setStyle(IDLE_BUTTON_STYLE));
        sickHolidayRequests.setOnMouseClicked(e -> sickHolidayRequests.setStyle(CLICKED_BUTTON_STYLE));

        Button createSendOrder = new Button("Create/Send order");
        createSendOrder.setPrefWidth(INPUT_FIELD_WIDTH);
        createSendOrder.setOnAction(event -> createSendOrder());
        createSendOrder.setStyle(IDLE_BUTTON_STYLE);
        createSendOrder.setOnMouseEntered(e -> createSendOrder.setStyle(HOVERED_BUTTON_STYLE));
        createSendOrder.setOnMouseExited(e -> createSendOrder.setStyle(IDLE_BUTTON_STYLE));
        createSendOrder.setOnMouseClicked(e -> createSendOrder.setStyle(CLICKED_BUTTON_STYLE));

        Button editMenu = new Button("Edit Menu");
        editMenu.setPrefWidth(INPUT_FIELD_WIDTH);
        editMenu.setOnAction(event -> editMenu());
        editMenu.setStyle(IDLE_BUTTON_STYLE);
        editMenu.setOnMouseEntered(e -> editMenu.setStyle(HOVERED_BUTTON_STYLE));
        editMenu.setOnMouseExited(e -> editMenu.setStyle(IDLE_BUTTON_STYLE));
        editMenu.setOnMouseClicked(e -> editMenu.setStyle(CLICKED_BUTTON_STYLE));

        Button bookingPredictions = new Button("Booking Predictions");
        bookingPredictions.setPrefWidth(INPUT_FIELD_WIDTH);
        bookingPredictions.setOnAction(event -> bookingPredictions());
        bookingPredictions.setStyle(IDLE_BUTTON_STYLE);
        bookingPredictions.setOnMouseEntered(e -> bookingPredictions.setStyle(HOVERED_BUTTON_STYLE));
        bookingPredictions.setOnMouseExited(e -> bookingPredictions.setStyle(IDLE_BUTTON_STYLE));
        bookingPredictions.setOnMouseClicked(e -> bookingPredictions.setStyle(CLICKED_BUTTON_STYLE));

        Button salesAnalysis = new Button("Sales analysis");
        salesAnalysis.setPrefWidth(INPUT_FIELD_WIDTH);
        salesAnalysis.setOnAction(event -> salesAnalysis());
        salesAnalysis.setStyle(IDLE_BUTTON_STYLE);
        salesAnalysis.setOnMouseEntered(e -> salesAnalysis.setStyle(HOVERED_BUTTON_STYLE));
        salesAnalysis.setOnMouseExited(e -> salesAnalysis.setStyle(IDLE_BUTTON_STYLE));
        salesAnalysis.setOnMouseClicked(e -> salesAnalysis.setStyle(CLICKED_BUTTON_STYLE));

        buttonLayout.getChildren().addAll(clockButton, staffHours, sickHolidayRequests, createSendOrder, editMenu, bookingPredictions, salesAnalysis);
        ComboBox<String> roleDropdown = createRoleDropdown();
        roleDropdown.setStyle("-fx-font-size: 16px; -fx-background-color: #f0f0f0; -fx-border-color: #999999;-fx-background-radius: 20");
        roleDropdown.setOnMouseEntered(e -> roleDropdown.setStyle("-fx-font-size: 16px; -fx-background-color: #e0e0e0; -fx-border-color: #999999"));
        roleDropdown.setOnMouseExited(e -> roleDropdown.setStyle("-fx-font-size: 16px; -fx-background-color: #f0f0f0; -fx-border-color: #999999"));
        BorderPane.setAlignment(roleDropdown, Pos.TOP_RIGHT);
        BorderPane.setMargin(roleDropdown, new Insets(-30, 10, 0, 0));

        Button logoutButton = createLogoutButton();
        BorderPane.setAlignment(logoutButton, Pos.BOTTOM_RIGHT);
        BorderPane.setMargin(logoutButton, new Insets(0, 10, 10, 100));

        layout.setTop(greetingLabel);
        layout.setRight(roleDropdown);
        layout.setBottom(logoutButton);
        layout.setCenter(buttonLayout);

        return new Scene(layout, SCREEN_RES_WIDTH, SCREEN_RES_HEIGHT);
    }

    private void salesAnalysis() {
        SceneManager sceneManager = SceneManager.getInstance();
        sceneManager.showScene(SalesAnalysis.class.getSimpleName());
    }

    private void bookingPredictions() {
        SceneManager sceneManager = SceneManager.getInstance();
        sceneManager.showScene(BookingPrediction.class.getSimpleName());
    }

    private void editMenu() {
        SceneManager sceneManager = SceneManager.getInstance();
        sceneManager.showScene(EditMenu.class.getSimpleName());
    }

    private void createSendOrder() {
        SceneManager sceneManager = SceneManager.getInstance();
        sceneManager.showScene(CreateSendOrder.class.getSimpleName());
    }

    private void sickHolidayRequests() {
        SceneManager sceneManager = SceneManager.getInstance();
        sceneManager.showScene(SickHolidayRequests.class.getSimpleName());
    }

    private void staffHours() {
        SceneManager sceneManager = SceneManager.getInstance();
        sceneManager.showScene(StaffHours.class.getSimpleName());
    }

    private void clockInOut() {
        SceneManager sceneManager = SceneManager.getInstance();
        sceneManager.showScene(ClockIn.class.getSimpleName());
    }

    public Scene createScene(boolean toggleManager) {
        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(20));


        Label greetingLabel = new Label("Welcome " + employeeName + "!");
        greetingLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        BorderPane.setAlignment(greetingLabel, Pos.TOP_LEFT);
        BorderPane.setMargin(greetingLabel, new Insets(10, 0, 0, 10));

        Timeline labelAnimation = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(greetingLabel.opacityProperty(), 0)),
                new KeyFrame(Duration.seconds(1), new KeyValue(greetingLabel.opacityProperty(), 1))
        );
        labelAnimation.play();


        VBox buttonLayout = new VBox(20);
        buttonLayout.setAlignment(Pos.CENTER);
        buttonLayout.setPadding(new Insets(50));
        Button clockButton = new Button("Clock In/Out");
        clockButton.setPrefWidth(INPUT_FIELD_WIDTH);
        clockButton.setOnAction(event -> clockInOut());
        clockButton.setStyle(IDLE_BUTTON_STYLE);
        clockButton.setOnMouseEntered(e -> clockButton.setStyle(HOVERED_BUTTON_STYLE));
        clockButton.setOnMouseExited(e -> clockButton.setStyle(IDLE_BUTTON_STYLE));
        clockButton.setOnMouseClicked(e -> clockButton.setStyle(CLICKED_BUTTON_STYLE));

        Button staffHours = new Button("Staff Hours");
        staffHours.setPrefWidth(INPUT_FIELD_WIDTH);
        staffHours.setOnAction(event -> staffHours());
        staffHours.setStyle(IDLE_BUTTON_STYLE);
        staffHours.setOnMouseEntered(e -> staffHours.setStyle(HOVERED_BUTTON_STYLE));
        staffHours.setOnMouseExited(e -> staffHours.setStyle(IDLE_BUTTON_STYLE));
        staffHours.setOnMouseClicked(e -> staffHours.setStyle(CLICKED_BUTTON_STYLE));

        Button sickHolidayRequests = new Button("Sick/Holiday requests");
        sickHolidayRequests.setPrefWidth(INPUT_FIELD_WIDTH);
        sickHolidayRequests.setOnAction(event -> sickHolidayRequests());
        sickHolidayRequests.setStyle(IDLE_BUTTON_STYLE);
        sickHolidayRequests.setOnMouseEntered(e -> sickHolidayRequests.setStyle(HOVERED_BUTTON_STYLE));
        sickHolidayRequests.setOnMouseExited(e -> sickHolidayRequests.setStyle(IDLE_BUTTON_STYLE));
        sickHolidayRequests.setOnMouseClicked(e -> sickHolidayRequests.setStyle(CLICKED_BUTTON_STYLE));

        Button createSendOrder = new Button("Create/Send order");
        createSendOrder.setPrefWidth(INPUT_FIELD_WIDTH);
        createSendOrder.setOnAction(event -> createSendOrder());
        createSendOrder.setStyle(IDLE_BUTTON_STYLE);
        createSendOrder.setOnMouseEntered(e -> createSendOrder.setStyle(HOVERED_BUTTON_STYLE));
        createSendOrder.setOnMouseExited(e -> createSendOrder.setStyle(IDLE_BUTTON_STYLE));
        createSendOrder.setOnMouseClicked(e -> createSendOrder.setStyle(CLICKED_BUTTON_STYLE));

        Button editMenu = new Button("Edit Menu");
        editMenu.setPrefWidth(INPUT_FIELD_WIDTH);
        editMenu.setOnAction(event -> editMenu());
        editMenu.setStyle(IDLE_BUTTON_STYLE);
        editMenu.setOnMouseEntered(e -> editMenu.setStyle(HOVERED_BUTTON_STYLE));
        editMenu.setOnMouseExited(e -> editMenu.setStyle(IDLE_BUTTON_STYLE));
        editMenu.setOnMouseClicked(e -> editMenu.setStyle(CLICKED_BUTTON_STYLE));

        Button bookingPredictions = new Button("Booking Predictions");
        bookingPredictions.setPrefWidth(INPUT_FIELD_WIDTH);
        bookingPredictions.setOnAction(event -> bookingPredictions());
        bookingPredictions.setStyle(IDLE_BUTTON_STYLE);
        bookingPredictions.setOnMouseEntered(e -> bookingPredictions.setStyle(HOVERED_BUTTON_STYLE));
        bookingPredictions.setOnMouseExited(e -> bookingPredictions.setStyle(IDLE_BUTTON_STYLE));
        bookingPredictions.setOnMouseClicked(e -> bookingPredictions.setStyle(CLICKED_BUTTON_STYLE));

        Button salesAnalysis = new Button("Sales analysis");
        salesAnalysis.setPrefWidth(INPUT_FIELD_WIDTH);
        salesAnalysis.setOnAction(event -> salesAnalysis());
        salesAnalysis.setStyle(IDLE_BUTTON_STYLE);
        salesAnalysis.setOnMouseEntered(e -> salesAnalysis.setStyle(HOVERED_BUTTON_STYLE));
        salesAnalysis.setOnMouseExited(e -> salesAnalysis.setStyle(IDLE_BUTTON_STYLE));
        salesAnalysis.setOnMouseClicked(e -> salesAnalysis.setStyle(CLICKED_BUTTON_STYLE));

        buttonLayout.getChildren().addAll(clockButton, staffHours, sickHolidayRequests, createSendOrder, editMenu, bookingPredictions, salesAnalysis);


        ComboBox<String> roleDropdown = createRoleDropdownDirector();
        roleDropdown.setStyle("-fx-font-size: 16px; -fx-background-color: #f0f0f0; -fx-border-color: #999999;-fx-background-radius: 20");
        BorderPane.setAlignment(roleDropdown, Pos.TOP_RIGHT);
        BorderPane.setMargin(roleDropdown, new Insets(-30, 10, 0, 0));
        roleDropdown.setOnMouseEntered(e -> roleDropdown.setStyle("-fx-font-size: 16px; -fx-background-color: #e0e0e0; -fx-border-color: #999999"));
        roleDropdown.setOnMouseExited(e -> roleDropdown.setStyle("-fx-font-size: 16px; -fx-background-color: #f0f0f0; -fx-border-color: #999999"));

        Button logoutButton = createLogoutButton();
        BorderPane.setAlignment(logoutButton, Pos.BOTTOM_RIGHT);
        BorderPane.setMargin(logoutButton, new Insets(0, 10, 10, 100));

        layout.setTop(greetingLabel);
        layout.setRight(roleDropdown);
        layout.setBottom(logoutButton);
        layout.setCenter(buttonLayout);

        return new Scene(layout, SCREEN_RES_WIDTH, SCREEN_RES_HEIGHT);
    }

    private ComboBox<String> createRoleDropdownDirector() {
        ComboBox<String> roleDropdown = new ComboBox<>();
        roleDropdown.getItems().addAll("Manager", "General Staff", "Sommelier");
        roleDropdown.setValue("Manager");
        roleDropdown.setOnAction(event -> {
            String selectedRole = roleDropdown.getValue();
            if (selectedRole.equals("General Staff")) {
                SceneManager.getInstance().showScene(GeneralStaff.class.getSimpleName());
            }
            if (selectedRole.equals("Sommelier")) {
                //String userInfo = getUserInfo(employeeName);
                SceneManager.getInstance().showScene(Sommelier.class.getSimpleName());
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
                SceneManager.getInstance().showScene(GeneralStaff.class.getSimpleName());
            }
        });
        return roleDropdown;
    }
}

