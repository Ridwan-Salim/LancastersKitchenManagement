package scenes;

import core.SceneManager;
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
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

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
import java.util.*;
import java.util.ArrayList;
import java.util.List;


public class ClockIn extends Manager {

    TextField clockInField;
    TextField clockOutField;
    Button checkShiftsButton;
    Button checkMissedShiftsButton;

    ComboBox<String> employeeComboBox;
    Label shiftsCoveredLabel;
    Label missedShiftsLabel;

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

        shiftsCoveredLabel = new Label("");
        shiftsCoveredLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: green;");

        missedShiftsLabel = new Label("");
        missedShiftsLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: red;");

        HBox textBox = new HBox();
        textBox.getChildren().addAll(shiftsCoveredLabel, missedShiftsLabel);

        Button clockButton = new Button("Clock In/Out");
        clockButton.setPrefWidth(INPUT_FIELD_WIDTH);
        clockButton.setOnAction(event -> clockInOut());
        clockButton.setStyle(IDLE_BUTTON_STYLE);
        clockButton.setOnMouseEntered(e -> clockButton.setStyle(HOVERED_BUTTON_STYLE));
        clockButton.setOnMouseExited(e -> clockButton.setStyle(IDLE_BUTTON_STYLE));
        clockButton.setOnMouseClicked(e -> clockButton.setStyle(CLICKED_BUTTON_STYLE));
        checkShiftsButton = new Button("Check Shifts");
        checkShiftsButton.setPrefWidth(INPUT_FIELD_WIDTH);
        checkShiftsButton.setOnAction(event -> checkShifts());
        checkShiftsButton.setStyle(IDLE_BUTTON_STYLE);
        checkShiftsButton.setOnMouseEntered(e -> checkShiftsButton.setStyle(HOVERED_BUTTON_STYLE));
        checkShiftsButton.setOnMouseExited(e -> checkShiftsButton.setStyle(IDLE_BUTTON_STYLE));
        checkShiftsButton.setOnMouseClicked(e -> checkShiftsButton.setStyle(CLICKED_BUTTON_STYLE));

        checkMissedShiftsButton = new Button("Check Missed Shifts");
        checkMissedShiftsButton.setPrefWidth(INPUT_FIELD_WIDTH);
        checkMissedShiftsButton.setOnAction(event -> checkMissedShifts());
        checkMissedShiftsButton.setStyle(IDLE_BUTTON_STYLE);
        checkMissedShiftsButton.setOnMouseEntered(e -> checkMissedShiftsButton.setStyle(HOVERED_BUTTON_STYLE));
        checkMissedShiftsButton.setOnMouseExited(e -> checkMissedShiftsButton.setStyle(IDLE_BUTTON_STYLE));
        checkMissedShiftsButton.setOnMouseClicked(e -> checkMissedShiftsButton.setStyle(CLICKED_BUTTON_STYLE));

        pane.getChildren().addAll(employeeComboBox, clockInField, clockOutField, clockButton, checkShiftsButton, checkMissedShiftsButton);

        VBox infoPane = new VBox(10);
        infoPane.getChildren().addAll(shiftsCoveredLabel, missedShiftsLabel);
        infoPane.setAlignment(Pos.CENTER_LEFT);

        VBox leftPart = new VBox();
        leftPart.getChildren().addAll(greetingLabel, infoPane);
        Button backButton = createBackButtonManager();
        layout.setLeft(leftPart);
        leftPart.setSpacing(30);
        layout.setBottom(backButton);
        layout.setCenter(pane);
        // Apply styles
        pane.setStyle("-fx-background-color: #f0f0f0;");
        shiftsCoveredLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2E8B57; -fx-background-color: #F0FFF0; -fx-padding: 10px;");
        missedShiftsLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #FF6347; -fx-background-color: #FFE4E1; -fx-padding: 10px;");
        // Make labels scrollable
        ScrollPane shiftsScrollPane = new ScrollPane();
        shiftsScrollPane.setContent(shiftsCoveredLabel);
        shiftsScrollPane.setFitToWidth(true);
        shiftsScrollPane.setPrefHeight(200);

        ScrollPane missedShiftsScrollPane = new ScrollPane();
        missedShiftsScrollPane.setContent(missedShiftsLabel);
        missedShiftsScrollPane.setFitToWidth(true);
        missedShiftsScrollPane.setPrefHeight(200);

        infoPane.getChildren().addAll(shiftsScrollPane, missedShiftsScrollPane);

        // Apply styles to buttons
        backButton.setStyle(IDLE_BUTTON_STYLE);
        backButton.setOnMouseEntered(e -> backButton.setStyle(HOVERED_BUTTON_STYLE));
        backButton.setOnMouseExited(e -> backButton.setStyle(IDLE_BUTTON_STYLE));
        clockButton.setStyle(IDLE_BUTTON_STYLE);
        clockButton.setOnMouseEntered(e -> clockButton.setStyle(HOVERED_BUTTON_STYLE));
        clockButton.setOnMouseExited(e -> clockButton.setStyle(IDLE_BUTTON_STYLE));
        checkShiftsButton.setStyle(IDLE_BUTTON_STYLE);
        checkShiftsButton.setOnMouseEntered(e -> checkShiftsButton.setStyle(HOVERED_BUTTON_STYLE));
        checkShiftsButton.setOnMouseExited(e -> checkShiftsButton.setStyle(IDLE_BUTTON_STYLE));
        checkMissedShiftsButton.setStyle(IDLE_BUTTON_STYLE);
        checkMissedShiftsButton.setOnMouseEntered(e -> checkMissedShiftsButton.setStyle(HOVERED_BUTTON_STYLE));
        checkMissedShiftsButton.setOnMouseExited(e -> checkMissedShiftsButton.setStyle(IDLE_BUTTON_STYLE));

        // Apply styles to ComboBox and text fields
        employeeComboBox.setStyle("-fx-font-size: 16px; -fx-background-color: #f0f0f0; -fx-border-color: #999999;");
        employeeComboBox.setOnMouseEntered(e -> employeeComboBox.setStyle("-fx-font-size: 16px; -fx-background-color: #e0e0e0; -fx-border-color: #999999"));
        employeeComboBox.setOnMouseExited(e -> employeeComboBox.setStyle("-fx-font-size: 16px; -fx-background-color: #f0f0f0; -fx-border-color: #999999"));

        clockInField.setStyle("-fx-font-size: 12px; -fx-background-color: #f0f0f0; -fx-border-color: #999999;");
        clockOutField.setStyle("-fx-font-size: 12px; -fx-background-color: #f0f0f0; -fx-border-color: #999999;");
        return new Scene(layout, SCREEN_RES_WIDTH, SCREEN_RES_HEIGHT);
    }
    public Scene createScene(boolean toggle) {
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

        shiftsCoveredLabel = new Label("");
        shiftsCoveredLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: green;");

        missedShiftsLabel = new Label("");
        missedShiftsLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: red;");

        HBox textBox = new HBox();
        textBox.getChildren().addAll(shiftsCoveredLabel, missedShiftsLabel);

        Button clockButton = new Button("Clock In/Out");
        clockButton.setPrefWidth(INPUT_FIELD_WIDTH);
        clockButton.setOnAction(event -> clockInOut());
        clockButton.setStyle(IDLE_BUTTON_STYLE);
        clockButton.setOnMouseEntered(e -> clockButton.setStyle(HOVERED_BUTTON_STYLE));
        clockButton.setOnMouseExited(e -> clockButton.setStyle(IDLE_BUTTON_STYLE));
        clockButton.setOnMouseClicked(e -> clockButton.setStyle(CLICKED_BUTTON_STYLE));
        checkShiftsButton = new Button("Check Shifts");
        checkShiftsButton.setPrefWidth(INPUT_FIELD_WIDTH);
        checkShiftsButton.setOnAction(event -> checkShifts());
        checkShiftsButton.setStyle(IDLE_BUTTON_STYLE);
        checkShiftsButton.setOnMouseEntered(e -> checkShiftsButton.setStyle(HOVERED_BUTTON_STYLE));
        checkShiftsButton.setOnMouseExited(e -> checkShiftsButton.setStyle(IDLE_BUTTON_STYLE));
        checkShiftsButton.setOnMouseClicked(e -> checkShiftsButton.setStyle(CLICKED_BUTTON_STYLE));

        checkMissedShiftsButton = new Button("Check Missed Shifts");
        checkMissedShiftsButton.setPrefWidth(INPUT_FIELD_WIDTH);
        checkMissedShiftsButton.setOnAction(event -> checkMissedShifts());
        checkMissedShiftsButton.setStyle(IDLE_BUTTON_STYLE);
        checkMissedShiftsButton.setOnMouseEntered(e -> checkMissedShiftsButton.setStyle(HOVERED_BUTTON_STYLE));
        checkMissedShiftsButton.setOnMouseExited(e -> checkMissedShiftsButton.setStyle(IDLE_BUTTON_STYLE));
        checkMissedShiftsButton.setOnMouseClicked(e -> checkMissedShiftsButton.setStyle(CLICKED_BUTTON_STYLE));

        pane.getChildren().addAll(employeeComboBox, clockInField, clockOutField, clockButton, checkShiftsButton, checkMissedShiftsButton);

        VBox infoPane = new VBox(10);
        infoPane.getChildren().addAll(shiftsCoveredLabel, missedShiftsLabel);
        infoPane.setAlignment(Pos.CENTER_LEFT);

        VBox leftPart = new VBox();
        leftPart.getChildren().addAll(greetingLabel, infoPane);
        Button backButton = createBackButtonManager();
        layout.setLeft(leftPart);
        leftPart.setSpacing(30);
        layout.setBottom(backButton);
        layout.setCenter(pane);
        // Apply styles
        pane.setStyle("-fx-background-color: #f0f0f0;");
        shiftsCoveredLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2E8B57; -fx-background-color: #F0FFF0; -fx-padding: 10px;");
        missedShiftsLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #FF6347; -fx-background-color: #FFE4E1; -fx-padding: 10px;");
        // Make labels scrollable
        ScrollPane shiftsScrollPane = new ScrollPane();
        shiftsScrollPane.setContent(shiftsCoveredLabel);
        shiftsScrollPane.setFitToWidth(true);
        shiftsScrollPane.setPrefHeight(200);

        ScrollPane missedShiftsScrollPane = new ScrollPane();
        missedShiftsScrollPane.setContent(missedShiftsLabel);
        missedShiftsScrollPane.setFitToWidth(true);
        missedShiftsScrollPane.setPrefHeight(200);

        infoPane.getChildren().addAll(shiftsScrollPane, missedShiftsScrollPane);

        // Apply styles to buttons
        backButton.setStyle(IDLE_BUTTON_STYLE);
        backButton.setOnMouseEntered(e -> backButton.setStyle(HOVERED_BUTTON_STYLE));
        backButton.setOnMouseExited(e -> backButton.setStyle(IDLE_BUTTON_STYLE));
        clockButton.setStyle(IDLE_BUTTON_STYLE);
        clockButton.setOnMouseEntered(e -> clockButton.setStyle(HOVERED_BUTTON_STYLE));
        clockButton.setOnMouseExited(e -> clockButton.setStyle(IDLE_BUTTON_STYLE));
        checkShiftsButton.setStyle(IDLE_BUTTON_STYLE);
        checkShiftsButton.setOnMouseEntered(e -> checkShiftsButton.setStyle(HOVERED_BUTTON_STYLE));
        checkShiftsButton.setOnMouseExited(e -> checkShiftsButton.setStyle(IDLE_BUTTON_STYLE));
        checkMissedShiftsButton.setStyle(IDLE_BUTTON_STYLE);
        checkMissedShiftsButton.setOnMouseEntered(e -> checkMissedShiftsButton.setStyle(HOVERED_BUTTON_STYLE));
        checkMissedShiftsButton.setOnMouseExited(e -> checkMissedShiftsButton.setStyle(IDLE_BUTTON_STYLE));

        // Apply styles to ComboBox and text fields
        employeeComboBox.setStyle("-fx-font-size: 16px; -fx-background-color: #f0f0f0; -fx-border-color: #999999;");
        employeeComboBox.setOnMouseEntered(e -> employeeComboBox.setStyle("-fx-font-size: 16px; -fx-background-color: #e0e0e0; -fx-border-color: #999999"));
        employeeComboBox.setOnMouseExited(e -> employeeComboBox.setStyle("-fx-font-size: 16px; -fx-background-color: #f0f0f0; -fx-border-color: #999999"));

        clockInField.setStyle("-fx-font-size: 12px; -fx-background-color: #f0f0f0; -fx-border-color: #999999;");
        clockOutField.setStyle("-fx-font-size: 12px; -fx-background-color: #f0f0f0; -fx-border-color: #999999;");
        return new Scene(layout, SCREEN_RES_WIDTH, SCREEN_RES_HEIGHT);
    }

    private void checkShifts() {
        String shiftsFilePath = "D:\\homework\\Lancasters\\vpp\\LancastersKitchenMgmt\\software\\src\\scenes\\utils\\shifts.csv";
        Map<String, List<LocalDate>> shiftsCoveredByClockIns = getShiftsCoveredByClockIns(shiftsFilePath);

        // Display the results
        StringBuilder contentText = new StringBuilder();
        shiftsCoveredByClockIns.forEach((employee, dates) -> {
            contentText.append(employee).append(":\n");
            Map<LocalDate, Integer> dateCountMap = new HashMap<>();
            for (LocalDate date : dates) {
                dateCountMap.put(date, dateCountMap.getOrDefault(date, 0) + 1);
            }
            dateCountMap.forEach((date, count) -> {
                contentText.append("- Date: ").append(date);
                if (count > 1) {
                    contentText.append(", Shifts Covered: ").append(count);
                }
                contentText.append("\n");
            });
        });
        shiftsCoveredLabel.setText("Shifts covered based on existing clock-ins:\n" + contentText.toString());
    }

    private Map<String, List<LocalDate>> getShiftsCoveredByClockIns(String shiftsFilePath) {
        Map<String, List<LocalDate>> shiftsCoveredByClockIns = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(shiftsFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 4) {
                    String employee = parts[0];
                    LocalDate shiftDate = LocalDate.parse(parts[4]);
                    String shiftStartTime = parts[2];
                    String shiftEndTime = calculateShiftEndTime(shiftStartTime);
                    // Check if the shift is covered by clock-ins
                    if (isShiftCovered(employee, shiftDate, shiftStartTime, shiftEndTime)) {
                        shiftsCoveredByClockIns.computeIfAbsent(employee, k -> new ArrayList<>()).add(shiftDate);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return shiftsCoveredByClockIns;
    }

    private void checkMissedShifts() {
        String shiftsFilePath = "D:\\homework\\Lancasters\\vpp\\LancastersKitchenMgmt\\software\\src\\scenes\\utils\\shifts.csv";

        // Read and parse shifts data
        Map<String, Set<LocalDate>> missedShifts = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(shiftsFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 4) {
                    String employee = parts[0];
                    LocalDate shiftDate = LocalDate.parse(parts[4]);
                    String shiftStartTime = parts[2];
                    String shiftEndTime = calculateShiftEndTime(shiftStartTime);
                    // Check if the shift is covered by clock-ins
                    if (!isShiftCovered(employee, shiftDate, shiftStartTime, shiftEndTime)) {
                        missedShifts.computeIfAbsent(employee, k -> new HashSet<>()).add(shiftDate);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Display the results
        StringBuilder contentText = new StringBuilder();
        missedShifts.forEach((employee, dates) -> {
            contentText.append(employee).append(":\n");
            dates.forEach(date -> contentText.append("- ").append(date).append("\n"));
        });
        missedShiftsLabel.setText("Employees who missed their shifts:\n" + contentText.toString());
    }
    private boolean isShiftCovered(String employee, LocalDate shiftDate, String shiftStartTime, String shiftEndTime) {
        String clockInsFilePath = "D:\\homework\\Lancasters\\vpp\\LancastersKitchenMgmt\\software\\src\\scenes\\utils\\clock-ins.csv";
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        try (BufferedReader br = new BufferedReader(new FileReader(clockInsFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 4 && parts[0].equals(employee)) {
                    LocalDate clockInDate = LocalDate.parse(parts[1]);
                    LocalTime clockInTime = LocalTime.parse(parts[2], timeFormatter);
                    LocalTime clockOutTime = LocalTime.parse(parts[3], timeFormatter);
                    // Check if the clock-in date matches the shift date
                    if (clockInDate.equals(shiftDate)) {
                        LocalTime shiftStartTimeParsed = LocalTime.parse(shiftStartTime, timeFormatter);
                        LocalTime shiftEndTimeParsed = LocalTime.parse(shiftEndTime, timeFormatter);
                        // Check if the clock-in time falls within the shift time range
                        if (clockOutTime.isAfter(shiftStartTimeParsed) && clockInTime.isBefore(shiftEndTimeParsed)) {
                            return true; // Clock-in found within the shift time range
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
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
    private String calculateShiftEndTime(String shiftStartTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime startTime = LocalTime.parse(shiftStartTime, formatter);
        LocalTime endTime = startTime.plusMinutes(30); // Assuming each shift is 30 minutes
        return endTime.format(formatter);
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
