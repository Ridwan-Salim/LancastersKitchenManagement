package scenes;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class ClockInScene extends ManagerScene{

    TextField clockInField;
    TextField clockOutField;

    ComboBox<String> employeeComboBox;

    public Scene createScene() {
        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(20));

        Label greetingLabel = new Label("Let's clock someone in/out, " + employeeName + ".");
        greetingLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        BorderPane.setAlignment(greetingLabel, Pos.TOP_LEFT);
        BorderPane.setMargin(greetingLabel, new Insets(10, 0, 0, 10));
        Timeline labelAnimation = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(greetingLabel.opacityProperty(), 0)),
                new KeyFrame(Duration.seconds(1), new KeyValue(greetingLabel.opacityProperty(), 1))
        );
        labelAnimation.play();

        VBox pane = new VBox(20);
        pane.setAlignment(Pos.CENTER);
        pane.setPadding(new Insets(10));

        // Creating ComboBox for employee selection
        ObservableList<String> employeeNames = FXCollections.observableArrayList(extractNamesFromFile("D:\\homework\\Lancasters\\vpp\\LancastersKitchenMgmt\\software\\src\\scenes\\usersInfo\\users_info.txt"));
        employeeComboBox = new ComboBox<>(employeeNames);
        employeeComboBox.setPrefWidth(INPUT_FIELD_WIDTH);
        employeeComboBox.setPromptText("Select Employee");
        employeeComboBox.setStyle("-fx-font-size: 16px; -fx-background-color: #f0f0f0; -fx-border-color: #999999;-fx-background-radius: 20");
        ComboBox<String> finalRoleDropdown2 = employeeComboBox;
        employeeComboBox.setOnMouseEntered(e -> finalRoleDropdown2.setStyle("-fx-font-size: 16px; -fx-background-color: #e0e0e0; -fx-border-color: #999999"));
        ComboBox<String> finalRoleDropdown3 = employeeComboBox;
        employeeComboBox.setOnMouseExited(e -> finalRoleDropdown3.setStyle("-fx-font-size: 16px; -fx-background-color: #f0f0f0; -fx-border-color: #999999"));

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

        pane.getChildren().addAll(employeeComboBox, clockInField, clockOutField, clockButton);

        Button backButton = createBackButtonManager();
        layout.setLeft(greetingLabel);
        layout.setBottom(backButton);
        layout.setCenter(pane);
        return new Scene(layout, SCREEN_RES_WIDTH, SCREEN_RES_HEIGHT);
    }




    private boolean isValidTimeFormat(String startTime, String endTime) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

            // Check for null values
            if (startTime.isEmpty() && endTime.isEmpty()) {
                return false;
            }

            // If start time is null, only validate end time
            if (startTime.isEmpty()) {
                LocalTime parsedEndTime = LocalTime.parse(endTime, formatter);
                return (parsedEndTime.getHour() >= 0 && parsedEndTime.getHour() <= 23 &&
                        parsedEndTime.getMinute() >= 0 && parsedEndTime.getMinute() <= 59) &&
                        (parsedEndTime.isBefore(LocalTime.of(2, 0)) || parsedEndTime.equals(LocalTime.of(2, 0)));
            }

            // If end time is null, only validate start time
            if (endTime.isEmpty()) {
                LocalTime parsedStartTime = LocalTime.parse(startTime, formatter);
                return (parsedStartTime.getHour() >= 0 && parsedStartTime.getHour() <= 23 &&
                        parsedStartTime.getMinute() >= 0 && parsedStartTime.getMinute() <= 59) &&
                        (parsedStartTime.isAfter(LocalTime.of(0, 0)) || parsedStartTime.equals(LocalTime.of(0, 0)));
            }

            // Both start and end times are not null, validate both
            LocalTime parsedStartTime = LocalTime.parse(startTime, formatter);
            LocalTime parsedEndTime = LocalTime.parse(endTime, formatter);

            // Validate start and end times
            if ((parsedStartTime.getHour() >= 0 && parsedStartTime.getHour() <= 23 &&
                    parsedStartTime.getMinute() >= 0 && parsedStartTime.getMinute() <= 59) &&
                    (parsedEndTime.getHour() >= 0 && parsedEndTime.getHour() <= 23 &&
                            parsedEndTime.getMinute() >= 0 && parsedEndTime.getMinute() <= 59)) {

                if (parsedEndTime.isBefore(parsedStartTime) || parsedEndTime.equals(parsedStartTime)) {
                    // If end time is before or equal to start time, check if end time is between 00:00 and 02:00 on the next day
                    return (parsedEndTime.isAfter(LocalTime.of(0, 0)) && parsedEndTime.isBefore(LocalTime.of(2, 0))) ||
                            parsedEndTime.equals(LocalTime.of(0, 0)) || parsedEndTime.equals(LocalTime.of(2, 0));
                } else {
                    // If end time is after start time, simply check if it's after the start time
                    return parsedEndTime.isAfter(parsedStartTime);
                }
            } else {
                return false;
            }
        } catch (DateTimeParseException e) {
            return false;
        }
    }
    private void clockInOut() {
        String clockInTime = clockInField.getText();
        String clockOutTime = clockOutField.getText();

        if (!clockInTime.isEmpty() && clockOutTime.isEmpty()) {
            writeClockTime(clockInTime, "");
            return;
        }

        if (isValidTimeFormat(clockInTime, clockOutTime)) {
            writeClockTime(clockInTime, clockOutTime);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Incorrect time format!");
            alert.setContentText("Please enter time in the format HH:mm.");
            alert.showAndWait();
        }
    }

    private void writeClockTime(String clockInTime, String clockOutTime) {

        String csvFilePath = "D:\\homework\\Lancasters\\vpp\\LancastersKitchenMgmt\\software\\src\\scenes\\utils\\clock-ins.csv";
        try {
            List<String> lines = Files.readAllLines(Paths.get(csvFilePath));
            LocalDate currentDate = LocalDate.now();
            String recordToSearch = employeeComboBox.getValue() + ";" + currentDate + ";" + clockInTime + ";";
            boolean recordFound = false;
            String clockInTimeFromRecord = "";
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                String[] parts = line.split(";");
                if (parts.length >= 3 && parts[0].equals(employeeComboBox.getValue()) && parts[1].equals(currentDate.toString())) {
                    // Found the corresponding clock-in record
                    clockInTimeFromRecord = parts[2]; // Get the value of clockInTime from the record
                    if (clockOutTime.isEmpty()) {
                        lines.set(i, line + ";;");
                    } else {
                        lines.set(i, line + clockOutTime);
                    }
                    recordFound = true;
                    break;
                }
            }

            if (!recordFound && !clockInTime.isEmpty()) {
                String newRecord = employeeComboBox.getValue() + ";" + currentDate + ";" + clockInTime + ";" + clockOutTime;
                lines.add(newRecord);
            } else if (!recordFound && clockInTime.isEmpty()) {
                return;
            }

            Files.write(Paths.get(csvFilePath), lines, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Confirmation");
            if (clockInTime.isEmpty() && !clockOutTime.isEmpty())
                alert.setHeaderText("You successfully added clock-out time.");
            else if (!clockInTime.isEmpty() && clockOutTime.isEmpty())
                alert.setHeaderText("You successfully added clock-in time.");
            else
                alert.setHeaderText("You successfully added clock-in and clock-out time.");
            alert.showAndWait();
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
    public static List<String> extractNamesFromFile(String filePath) {
        List<String> names = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] columns = line.split(" - ");
                if (columns.length > 1) {
                    String name = columns[1].trim();
                    // Check if the name is not in all uppercase
                    if (!name.equals(name.toUpperCase())) {
                        if (!names.contains(name)) {
                            names.add(name);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return names;
    }
}
