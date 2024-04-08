package scenes;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.format.DateTimeParseException;

public class GeneralStaffScene extends PersonalizableScene {
    private TextField focusedField;
    private DatePicker fromDateField;
    private DatePicker toDateField;
    private TextArea reasonTextArea;
    private int INPUT_FIELD_WIDTH = 255;
    TextField clockInField;
    TextField clockOutField;
    public Scene createScene() {
        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(20));

        Label greetingLabel = new Label("Welcome General Staff " + employeeName + "!");
        greetingLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        BorderPane.setAlignment(greetingLabel, Pos.TOP_LEFT);
        BorderPane.setMargin(greetingLabel, new Insets(10, 0, 0, 10));

        Timeline labelAnimation = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(greetingLabel.opacityProperty(), 0)),
                new KeyFrame(Duration.seconds(1), new KeyValue(greetingLabel.opacityProperty(), 1))
        );
        labelAnimation.play();

        ComboBox<String> roleDropdown = createRoleDropdown();
        roleDropdown.setStyle("-fx-font-size: 16px; -fx-background-color: #f0f0f0; -fx-border-color: #999999;-fx-background-radius: 20");
        BorderPane.setAlignment(roleDropdown, Pos.TOP_RIGHT);
        BorderPane.setMargin(roleDropdown, new Insets(-30, 10, 0, 0));

        VBox leftPane = new VBox(20);
        leftPane.setAlignment(Pos.BOTTOM_LEFT); // Aligning to the bottom left
        leftPane.setPadding(new Insets(10));

        clockInField = new TextField();
        clockInField.setStyle("-fx-font-size: 12px; -fx-background-color: #f0f0f0; -fx-border-color: #999999;-fx-background-radius: 20");
        clockInField.setMaxWidth(INPUT_FIELD_WIDTH);
        clockInField.setPromptText("Clock In (HH:mm)");

        clockOutField = new TextField();
        clockOutField.setStyle("-fx-font-size: 12px; -fx-background-color: #f0f0f0; -fx-border-color: #999999;-fx-background-radius: 20");
        clockOutField.setMaxWidth(INPUT_FIELD_WIDTH);
        clockOutField.setPromptText("Clock Out (HH:mm)");


        Button clockButton = new Button("Clock In/Out");
        clockButton.setPrefWidth(INPUT_FIELD_WIDTH);
        clockButton.setOnAction(event -> clockInOut());
        clockButton.setStyle(IDLE_BUTTON_STYLE);
        clockButton.setOnMouseEntered(e -> clockButton.setStyle(HOVERED_BUTTON_STYLE));
        clockButton.setOnMouseExited(e -> clockButton.setStyle(IDLE_BUTTON_STYLE));
        clockButton.setOnMouseClicked(e -> clockButton.setStyle(CLICKED_BUTTON_STYLE));


        DatePicker fromDateField = new DatePicker();
        fromDateField.setPromptText("From Date");
        fromDateField.setStyle("-fx-font-size: 12px; -fx-background-color: #f0f0f0; -fx-border-color: #999999;-fx-background-radius: 20");
        fromDateField.setMaxWidth(INPUT_FIELD_WIDTH);
        DatePicker toDateField = new DatePicker();
        toDateField.setPromptText("To Date");
        toDateField.setStyle("-fx-font-size: 12px; -fx-background-color: #f0f0f0; -fx-border-color: #999999;-fx-background-radius: 20");
        toDateField.setMaxWidth(INPUT_FIELD_WIDTH);


        reasonTextArea = new TextArea();
        reasonTextArea.setStyle("-fx-font-size: 12px; -fx-background-color: #f0f0f0; -fx-border-color: #999999;-fx-background-radius: 20");
        reasonTextArea.setMaxWidth(INPUT_FIELD_WIDTH);
        reasonTextArea.setPromptText("Reason for Leave");

        Button submitLeaveButton = new Button("Submit Leave Request");
        submitLeaveButton.setOnAction(event -> submitLeaveRequest(fromDateField.getValue(), toDateField.getValue(), reasonTextArea.getText()));
        submitLeaveButton.setStyle(IDLE_BUTTON_STYLE);
        submitLeaveButton.setOnMouseEntered(e -> submitLeaveButton.setStyle(HOVERED_BUTTON_STYLE));
        submitLeaveButton.setOnMouseExited(e -> submitLeaveButton.setStyle(IDLE_BUTTON_STYLE));
        submitLeaveButton.setOnMouseClicked(e -> submitLeaveButton.setStyle(CLICKED_BUTTON_STYLE));

        leftPane.getChildren().addAll(clockInField, clockOutField, clockButton, fromDateField, toDateField, reasonTextArea, submitLeaveButton);

        Button logoutButton = createLogoutButton();
        BorderPane.setAlignment(logoutButton, Pos.BOTTOM_LEFT); // Aligning to the bottom left
        BorderPane.setMargin(logoutButton, new Insets(10, 10, 10, 10));

        layout.setTop(greetingLabel);
        layout.setRight(roleDropdown);
        layout.setLeft(leftPane);
        layout.setBottom(logoutButton);

        return new Scene(layout, SCREEN_RES_WIDTH, SCREEN_RES_HEIGHT);
    }
    private void clockInOut() {
            if (isValidTimeFormat(clockInField.getText(), clockOutField.getText())) {
                writeClockTime("clock-ins.txt", "Clock In", time);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("Incorrect time format!");
                alert.setContentText("Please enter time in the format HH:mm.");
                alert.showAndWait();
            }
    }
    public Scene createScene(boolean toggleManager) {
        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(20));

        Label greetingLabel = new Label("Welcome General Staff " + employeeName + "!");
        greetingLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        BorderPane.setAlignment(greetingLabel, Pos.TOP_LEFT);
        BorderPane.setMargin(greetingLabel, new Insets(10, 0, 0, 10));

        Timeline labelAnimation = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(greetingLabel.opacityProperty(), 0)),
                new KeyFrame(Duration.seconds(1), new KeyValue(greetingLabel.opacityProperty(), 1))
        );
        labelAnimation.play();

        Button logoutButton = createLogoutButton();
        BorderPane.setAlignment(logoutButton, Pos.BOTTOM_RIGHT);
        BorderPane.setMargin(logoutButton, new Insets(100, 10, 10, 100));
        ComboBox<String> roleDropdown = new ComboBox<>();
        if (toggleManager){
            roleDropdown = createRoleDropdownManager();
            roleDropdown.setStyle("-fx-font-size: 16px; -fx-background-color: #f0f0f0; -fx-border-color: #999999;-fx-background-radius: 20");
            BorderPane.setAlignment(roleDropdown, Pos.TOP_RIGHT);
            BorderPane.setMargin(roleDropdown, new Insets(-30, 10, 0, 0));
        }
        else {
            roleDropdown = createRoleDropdownDirector();
            roleDropdown.setStyle("-fx-font-size: 16px; -fx-background-color: #f0f0f0; -fx-border-color: #999999;-fx-background-radius: 20");
            BorderPane.setAlignment(roleDropdown, Pos.TOP_RIGHT);
            BorderPane.setMargin(roleDropdown, new Insets(-30, 10, 0, 0));
        }

        layout.setTop(greetingLabel);
        layout.setRight(roleDropdown);
        layout.setBottom(logoutButton);

        return new Scene(layout, SCREEN_RES_WIDTH, SCREEN_RES_HEIGHT);
    }
    private void clockIn(String time) {
        if (isValidTimeFormat(time)) {
            writeClockTime("clock-ins.txt", "Clock In", time);
        } else {
            // Handle invalid time format
        }
    }

    private void submitLeaveRequest(LocalDate fromDate, LocalDate toDate, String reason) {
//        if (isValidDateFormat(fromDate) && isValidDateFormat(toDate)) {
//            String leaveRequest = String.format("%s-%s-%s-ExceedsQuota", employeeName, fromDate, toDate, reason);
//            try (BufferedWriter writer = new BufferedWriter(new FileWriter("leave-requests.txt", true))) {
//                writer.write(leaveRequest);
//                writer.newLine();
//            } catch (IOException e) {
//                e.printStackTrace();
//                // Handle file write error
//            }
//        } else {
//            // Handle invalid date format
//        }
    }

    private boolean isValidTimeFormat(String startTime, String endTime) {
        try {
            // Parse the start time using the specified format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime parsedStartTime = LocalTime.parse(startTime, formatter);
            LocalTime parsedEndTime = LocalTime.parse(endTime, formatter);

            // Check if both parsed times are within the valid range (00:00 to 23:59)
            if ((parsedStartTime.getHour() >= 0 && parsedStartTime.getHour() <= 23 &&
                    parsedStartTime.getMinute() >= 0 && parsedStartTime.getMinute() <= 59) &&
                    (parsedEndTime.getHour() >= 0 && parsedEndTime.getHour() <= 23 &&
                            parsedEndTime.getMinute() >= 0 && parsedEndTime.getMinute() <= 59)) {
                // Check if the end time is after the start time
                if (parsedEndTime.isAfter(parsedStartTime)) {
                    return true; // Both times are valid and in the correct order
                } else {
                    return false; // End time is not after the start time
                }
            } else {
                return false; // Invalid time format (out of range)
            }
        } catch (DateTimeParseException e) {
            return false; // Parsing failed, invalid time format
        }
    }

    private void writeClockTime(String fileName, String eventType, String time) {
        String clockTimeEntry = String.format("%s-%s-%s", employeeName, eventType, time);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(clockTimeEntry);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle file write error
        }
    }
    private ComboBox<String> createRoleDropdown() {
        ComboBox<String> roleDropdown = new ComboBox<>();
        roleDropdown.getItems().addAll("General Staff");
        roleDropdown.setValue("General Staff");
        roleDropdown.setDisable(true);
        return roleDropdown;
    }
    private ComboBox<String> createRoleDropdownManager() {
        ComboBox<String> roleDropdown = new ComboBox<>();
        roleDropdown.getItems().addAll("General Staff", "Manager");
        roleDropdown.setValue("General Staff");
        roleDropdown.setOnAction(event -> {
            String selectedRole = roleDropdown.getValue();
            if (selectedRole.equals("Manager")) {
                String userInfo = getUserInfo(employeeName);
                SceneManager.getInstance().showScene(userInfo.split(" - ")[0]);
            }
        });
        return roleDropdown;
    }
    private ComboBox<String> createRoleDropdownDirector() {
        ComboBox<String> roleDropdown = new ComboBox<>();
        roleDropdown.getItems().addAll("General Staff", "Manager", "Lancaster");
        roleDropdown.setValue("General Staff");
        roleDropdown.setOnAction(event -> {
            String selectedRole = roleDropdown.getValue();
            if (selectedRole.equals("Lancaster")) {
                String userInfo = getUserInfo(employeeName);
                SceneManager.getInstance().showScene(userInfo.split(" - ")[0]);
            }
            if (selectedRole.equals("Manager")) {
                String userInfo = getUserInfo(employeeName, "MANAGER");
                SceneManager.getInstance().showScene(userInfo.split(" - ")[0]);
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
    private String getUserInfo(String name, String role) {
        String filePath = "D:\\homework\\Lancasters\\vpp\\LancastersKitchenMgmt\\software\\src\\scenes\\usersInfo\\users_info.txt";
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" - ");
                if (parts.length > 1) {
                    if (parts[0].equals(role + "-" + name)) {
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
