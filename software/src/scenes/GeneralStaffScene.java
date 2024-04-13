package scenes;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.format.DateTimeParseException;
import java.time.format.TextStyle;
import java.util.*;

import static scenes.StaffHoursScene.SHIFTS_FILE_PATH;


public class GeneralStaffScene extends PersonalizableScene {
    private TextField focusedField;
    private DatePicker fromDateField;
    private DatePicker toDateField;
    private TextArea reasonTextArea;
    private int INPUT_FIELD_WIDTH = 255;
    TextField clockInField;
    TextField clockOutField;
    ComboBox<String> reasonDropdown;

    private static final String POPUP_STYLE = "-fx-background-color: white; -fx-padding: 10px;";
    private static final String TIME_LABEL_STYLE = "-fx-font-size: 14px;";
    private static final String SEPARATOR_STYLE = "-fx-background-color: #f0f0f0; -fx-opacity: 0.5; -fx-pref-height: 1px;";
    private List<String> selectedTimes = new LinkedList<>();

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

        VBox rightPane = new VBox(20);
        rightPane.setAlignment(Pos.TOP_RIGHT); // Aligning to the top right
        rightPane.setPadding(new Insets(10));

        


        ComboBox<String> roleDropdown = createRoleDropdown();
        roleDropdown.setStyle("-fx-font-size: 16px; -fx-background-color: #f0f0f0; -fx-border-color: #999999;-fx-background-radius: 20");
        roleDropdown.setOnMouseEntered(e -> roleDropdown.setStyle("-fx-font-size: 16px; -fx-background-color: #e0e0e0; -fx-border-color: #999999"));
        roleDropdown.setOnMouseExited(e -> roleDropdown.setStyle("-fx-font-size: 16px; -fx-background-color: #f0f0f0; -fx-border-color: #999999"));
        BorderPane.setAlignment(roleDropdown, Pos.TOP_RIGHT);
        BorderPane.setMargin(roleDropdown, new Insets(-30, 10, 0, 0));

        rightPane.getChildren().addAll(roleDropdown, createCalendarPane());
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




        reasonDropdown = new ComboBox<>();;
        reasonDropdown.getItems().addAll("Disease", "Holiday", "Personal", "Emergency", "Other");

        reasonDropdown.setStyle("-fx-font-size: 12px; -fx-background-color: #f0f0f0; -fx-border-color: #999999;-fx-background-radius: 20");
        reasonDropdown.setMaxWidth(INPUT_FIELD_WIDTH);
        reasonDropdown.setPromptText("Reason for Leave");
        reasonDropdown.setOnMouseEntered(e -> reasonDropdown.setStyle("-fx-font-size: 12px; -fx-background-color: #e0e0e0; -fx-border-color: #999999"));
        reasonDropdown.setOnMouseExited(e -> reasonDropdown.setStyle("-fx-font-size: 12px; -fx-background-color: #f0f0f0; -fx-border-color: #999999"));
        Button submitLeaveButton = new Button("Submit Leave Request");
        submitLeaveButton.setOnAction(event -> submitLeaveRequest(fromDateField.getValue(), toDateField.getValue(), reasonDropdown.getValue()));
        submitLeaveButton.setStyle(IDLE_BUTTON_STYLE);
        submitLeaveButton.setOnMouseEntered(e -> submitLeaveButton.setStyle(HOVERED_BUTTON_STYLE));
        submitLeaveButton.setOnMouseExited(e -> submitLeaveButton.setStyle(IDLE_BUTTON_STYLE));
        submitLeaveButton.setOnMouseClicked(e -> submitLeaveButton.setStyle(CLICKED_BUTTON_STYLE));

        Button viewCurrentMenu = new Button("View Current Menu");
        viewCurrentMenu.setPrefWidth(INPUT_FIELD_WIDTH);
        viewCurrentMenu.setOnAction(event -> getMenu());
        viewCurrentMenu.setStyle(IDLE_BUTTON_STYLE);
        viewCurrentMenu.setOnMouseEntered(e -> viewCurrentMenu.setStyle(HOVERED_BUTTON_STYLE));
        viewCurrentMenu.setOnMouseExited(e -> viewCurrentMenu.setStyle(IDLE_BUTTON_STYLE));
        viewCurrentMenu.setOnMouseClicked(e -> viewCurrentMenu.setStyle(CLICKED_BUTTON_STYLE));

        Button viewShifts = createButton("View my shifts", e -> openShiftsWindow(employeeName));
        viewShifts.setPrefWidth(INPUT_FIELD_WIDTH);
        leftPane.getChildren().addAll(viewShifts, viewCurrentMenu, clockInField, clockOutField, clockButton, fromDateField, toDateField, reasonDropdown, submitLeaveButton);

        Button logoutButton = createLogoutButton();
        BorderPane.setAlignment(logoutButton, Pos.BOTTOM_LEFT); // Aligning to the bottom left
        BorderPane.setMargin(logoutButton, new Insets(10, 10, 10, 10));

        HBox topContainer = new HBox();
        topContainer.getChildren().addAll(greetingLabel, roleDropdown);
        topContainer.setSpacing(475);

// Set alignment to put the role dropdown on the right
        HBox.setHgrow(roleDropdown, Priority.ALWAYS);
        HBox.setMargin(roleDropdown, new Insets(0, -100, 0, 0));

        layout.setTop(topContainer);
        layout.setLeft(leftPane);
        layout.setBottom(logoutButton);
        layout.setRight(createCalendarPane());
        return new Scene(layout, SCREEN_RES_WIDTH, SCREEN_RES_HEIGHT);
    }

    private void getMenu() {
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
    private void openShiftsWindow(String employeeName) {
        Stage shiftsStage = new Stage();
        shiftsStage.setTitle("Employee Shifts");
        shiftsStage.setWidth(620);
        shiftsStage.setHeight(640);

        // Read shifts data for the specific employee
        Map<String, List<String>> shiftsData = readShiftsData(employeeName);

        // Calculate total hours worked for the employee in a month
        Map<String, Double> totalHoursPerEmployee = calculateTotalHoursPerEmployee(shiftsData);

        // Display employee shifts and total hours
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.setStyle("-fx-background-color: #f0f0f0;");

        for (Map.Entry<String, List<String>> entry : shiftsData.entrySet()) {
            String employee = entry.getKey();
            List<String> shifts = entry.getValue();

            VBox employeeBox = new VBox(5);
            employeeBox.setStyle("-fx-border-color: #ccc; -fx-border-width: 1px; -fx-padding: 5px;");

            Label nameLabel = new Label(employee);
            nameLabel.setStyle("-fx-font-weight: bold;");
            nameLabel.setFont(Font.font("Arial", 14));

            VBox shiftsBox = new VBox(3);
            shiftsBox.setStyle("-fx-padding: 5px;");
            for (String shift : shifts) {
                String tempShift = shift;
                HBox shiftRow = new HBox(5);
                shiftRow.setAlignment(Pos.CENTER_LEFT);
                String[] shiftArr = shift.split(";");
                shiftArr[1] = "Created on: " + shiftArr[1] + ". ";
                shiftArr[2] = "Shift time-slot: " + shiftArr[2] + ". ";
                shiftArr[3] = "Day of shift: " + shiftArr[3] + ". ";
                shiftArr[4] = "Date of shift: " + shiftArr[4] + ". ";
                shift = shiftArr[1] + shiftArr[2] + shiftArr[3] + shiftArr[4];
                Label shiftLabel = new Label(shift);
                shiftLabel.setFont(Font.font("Arial", 12));
                shift = tempShift;

                shiftRow.getChildren().add(shiftLabel);
                shiftsBox.getChildren().add(shiftRow);
            }

            Label totalHoursLabel = new Label("Total Hours: " + totalHoursPerEmployee.getOrDefault(employee, 0.0));
            totalHoursLabel.setFont(Font.font("Arial", 12));

            employeeBox.getChildren().addAll(nameLabel, shiftsBox, totalHoursLabel);
            layout.getChildren().add(employeeBox);
        }

        ScrollPane scrollPane = new ScrollPane(layout);
        scrollPane.setFitToWidth(true);

        Button closeButton = createButton("Close", e -> shiftsStage.close());
        VBox mainLayout = new VBox(10);
        mainLayout.getChildren().addAll(scrollPane, closeButton);
        mainLayout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(mainLayout, 320, 640);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        shiftsStage.setScene(scene);
        shiftsStage.show();
    }

    private Map<String, Double> calculateTotalHoursPerEmployee(Map<String, List<String>> shiftsData) {
        Map<String, Double> totalHoursPerEmployee = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : shiftsData.entrySet()) {
            double totalHours = entry.getValue().size() * 0.5; // Assuming each shift is for 30 minutes
            totalHoursPerEmployee.put(entry.getKey(), totalHours);
        }
        return totalHoursPerEmployee;
    }

    private Map<String, List<String>> readShiftsData(String employeeName) {
        Map<String, List<String>> shiftsData = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(SHIFTS_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                String employee = parts[0];
                if (employee.equals(employeeName)) {
                    String shiftCreationDate = parts[1];
                    String startTime = parts[2];
                    String dayOfWeek = parts[3];
                    String shiftDate = parts[4];
                    String shiftInfo = String.format("%s;%s;%s;%s;%s", employee, shiftCreationDate, startTime, dayOfWeek, shiftDate);
                    shiftsData.computeIfAbsent(employee, k -> new ArrayList<>()).add(shiftInfo);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return shiftsData;
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


        ComboBox<String> roleDropdown;
        VBox rightPane = new VBox(20);
        rightPane.setAlignment(Pos.TOP_RIGHT); // Aligning to the top right
        rightPane.setPadding(new Insets(10));

        if (toggleManager) {
            roleDropdown = createRoleDropdownManager();
        } else {
            roleDropdown = createRoleDropdownDirector();
        }

        roleDropdown.setStyle("-fx-font-size: 16px; -fx-background-color: #f0f0f0; -fx-border-color: #999999;-fx-background-radius: 20");
        ComboBox<String> finalRoleDropdown = roleDropdown;
        roleDropdown.setOnMouseEntered(e -> finalRoleDropdown.setStyle("-fx-font-size: 16px; -fx-background-color: #e0e0e0; -fx-border-color: #999999"));
        ComboBox<String> finalRoleDropdown1 = roleDropdown;
        roleDropdown.setOnMouseExited(e -> finalRoleDropdown1.setStyle("-fx-font-size: 16px; -fx-background-color: #f0f0f0; -fx-border-color: #999999"));

        rightPane.getChildren().addAll(roleDropdown, createCalendarPane());
        VBox leftPane = new VBox(20);
        leftPane.setAlignment(Pos.BOTTOM_LEFT); // Aligning to the bottom left
        leftPane.setPadding(new Insets(10));

        TextField clockInField = new TextField();
        clockInField.setStyle("-fx-font-size: 12px; -fx-background-color: #f0f0f0; -fx-border-color: #999999;-fx-background-radius: 20");
        clockInField.setMaxWidth(INPUT_FIELD_WIDTH);
        clockInField.setPromptText("Clock In (HH:mm)");

        TextField clockOutField = new TextField();
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

        ComboBox<String> reasonDropdown = new ComboBox<>();
        reasonDropdown.getItems().addAll("Disease", "Holiday", "Personal", "Emergency", "Other");
        reasonDropdown.setStyle("-fx-font-size: 12px; -fx-background-color: #f0f0f0; -fx-border-color: #999999;-fx-background-radius: 20");
        ComboBox<String> finalRoleDropdown4 = reasonDropdown;
        reasonDropdown.setOnMouseEntered(e -> finalRoleDropdown4.setStyle("-fx-font-size: 12px; -fx-background-color: #e0e0e0; -fx-border-color: #999999"));
        ComboBox<String> finalRoleDropdown5 = reasonDropdown;
        reasonDropdown.setOnMouseExited(e -> finalRoleDropdown5.setStyle("-fx-font-size: 12px; -fx-background-color: #f0f0f0; -fx-border-color: #999999"));
        reasonDropdown.setMaxWidth(INPUT_FIELD_WIDTH);
        reasonDropdown.setPromptText("Reason for Leave");

        Button submitLeaveButton = new Button("Submit Leave Request");
        submitLeaveButton.setOnAction(event -> submitLeaveRequest(fromDateField.getValue(), toDateField.getValue(), reasonDropdown.getValue()));
        submitLeaveButton.setStyle(IDLE_BUTTON_STYLE);
        submitLeaveButton.setOnMouseEntered(e -> submitLeaveButton.setStyle(HOVERED_BUTTON_STYLE));
        submitLeaveButton.setOnMouseExited(e -> submitLeaveButton.setStyle(IDLE_BUTTON_STYLE));
        submitLeaveButton.setOnMouseClicked(e -> submitLeaveButton.setStyle(CLICKED_BUTTON_STYLE));

        Button viewCurrentMenu = new Button("View Current Menu");
        viewCurrentMenu.setPrefWidth(INPUT_FIELD_WIDTH);
        viewCurrentMenu.setOnAction(event -> getMenu());
        viewCurrentMenu.setStyle(IDLE_BUTTON_STYLE);
        viewCurrentMenu.setOnMouseEntered(e -> viewCurrentMenu.setStyle(HOVERED_BUTTON_STYLE));
        viewCurrentMenu.setOnMouseExited(e -> viewCurrentMenu.setStyle(IDLE_BUTTON_STYLE));
        viewCurrentMenu.setOnMouseClicked(e -> viewCurrentMenu.setStyle(CLICKED_BUTTON_STYLE));

        Button viewShifts = createButton("View my shifts", e -> openShiftsWindow(employeeName));
        viewShifts.setPrefWidth(INPUT_FIELD_WIDTH);

        leftPane.getChildren().addAll(viewShifts, viewCurrentMenu, clockInField, clockOutField, clockButton, fromDateField, toDateField, reasonDropdown, submitLeaveButton);

        Button logoutButton = createLogoutButton();
        BorderPane.setAlignment(logoutButton, Pos.BOTTOM_LEFT); // Aligning to the bottom left
        BorderPane.setMargin(logoutButton, new Insets(10, 10, 10, 10));

        HBox topContainer = new HBox();
        topContainer.getChildren().addAll(greetingLabel, roleDropdown);
        topContainer.setSpacing(540);

// Set alignment to put the role dropdown on the right
        HBox.setHgrow(roleDropdown, Priority.ALWAYS);
        HBox.setMargin(roleDropdown, new Insets(0, -100, 0, 0));

        layout.setTop(topContainer);
        layout.setLeft(leftPane);
        layout.setBottom(logoutButton);
        layout.setRight(createCalendarPane());
        return new Scene(layout, SCREEN_RES_WIDTH, SCREEN_RES_HEIGHT);
    }

    private GridPane createCalendarPane() {
        GridPane calendarPane = new GridPane();
        calendarPane.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 20px;");
        int numRows = 7; // Increased numRows to accommodate weekday labels

        String currentMonth = LocalDate.now().getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault());

        Label monthLabel = new Label(currentMonth);
        monthLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        calendarPane.add(monthLabel, 0, 0, 7, 1);

        // Add weekday labels
        String[] weekdayNames = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (int col = 0; col < 7; col++) {
            Label weekdayLabel = new Label(weekdayNames[col]);
            weekdayLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 0 10px 0 0;");
            calendarPane.add(weekdayLabel, col, 1);
        }

        LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
        int dayOfWeekOfFirstDay = firstDayOfMonth.getDayOfWeek().getValue();

        int dayOfMonth = 1;
        for (int row = 2; row < numRows + 2; row++) {
            for (int col = 0; col < 7; col++) {
                if ((row == 2 && col < dayOfWeekOfFirstDay) || dayOfMonth > firstDayOfMonth.lengthOfMonth()) {
                    continue;
                }
                StackPane dayPane = new StackPane();
                dayPane.setAlignment(Pos.CENTER);
                dayPane.setPrefSize(100, 100);
                Label dayLabel = new Label(String.valueOf(dayOfMonth));
                dayLabel.setStyle("-fx-font-size: 16px;");
                dayPane.getChildren().add(dayLabel);
                dayPane.setOnMouseEntered(event -> dayPane.setStyle("-fx-background-color: #90caf9; -fx-background-radius: 50%;"));
                dayPane.setOnMouseExited(event -> dayPane.setStyle(null));
                int finalDayOfMonth = dayOfMonth;
                dayPane.setOnMouseClicked(event -> {
                    String message = "You clicked on " + dayLabel.getText();
                    LocalDate selectedDate = firstDayOfMonth.plusDays(finalDayOfMonth - 1);
                    showTimeSelectionWindow((Stage) calendarPane.getScene().getWindow(), selectedDate);
                });
                calendarPane.add(dayPane, col, row);
                dayOfMonth++;
            }
        }
        return calendarPane;
    }

    private void showTimeSelectionWindow(Stage ownerStage, LocalDate currentDate) {
        VBox content = new VBox(5);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(10));
        content.setStyle("-fx-background-color: #ffffff; -fx-border-color: #ccc; -fx-border-width: 1px; -fx-border-radius: 5px;");

        List<Label> timeLabels = new ArrayList<>();
        List<String> selectedTimes = new ArrayList<>();

        String currentDay = currentDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());
        String currDate = currentDate.toString();

        for (int hour = 0; hour < 24; hour++) {
            for (int minute = 0; minute <= 30; minute += 30) {
                String time = String.format("%02d:%02d", hour, minute);
                String nextTime = "";
                if (hour == 23 && minute == 30) {
                    nextTime = "00:00";
                } else if (minute == 30) {
                    nextTime = String.format("%02d:00", hour + 1);
                } else if (minute == 0) {
                    nextTime = String.format("%02d:30", hour);
                }
                Label timeLabel = new Label(time + " - " + nextTime);
                timeLabel.getStyleClass().add("time-label");
                timeLabels.add(timeLabel);

                timeLabel.setOnMouseEntered(e -> {
                    if (!selectedTimes.contains(time)) {
                        timeLabel.setStyle("-fx-font-size: 18px; -fx-background-color: #f0f0f0;");
                    }
                });
                timeLabel.setOnMouseExited(e -> {
                    if (!selectedTimes.contains(time)) {
                        timeLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #333;");
                    }
                });

                timeLabel.setOnMouseClicked(e -> {
                    if (selectedTimes.contains(time)) {
                        selectedTimes.remove(time);
                        timeLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #333;");
                    } else {
                        selectedTimes.add(time);
                        timeLabel.setStyle("-fx-font-size: 18px; -fx-background-color: #90caf9;");
                    }
                });

                content.getChildren().addAll(timeLabel);

                if (hour < 23 || minute < 30) {
                    Label separator = new Label();
                    separator.setStyle("-fx-background-color: #ccc; -fx-min-height: 1px;");
                    content.getChildren().add(separator);
                }
            }
        }

        Button doneButton = new Button("Done");
        doneButton.setAlignment(Pos.CENTER);
        doneButton.setOnAction(e -> {
            if (!selectedTimes.isEmpty()) {
                recordTimes(selectedTimes, currentDay, currDate);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Done");
                alert.setHeaderText("You successfully recorded selected shifts.");
                alert.showAndWait();
            }
            ((Stage) content.getScene().getWindow()).close();
        });
        doneButton.setStyle("-fx-font-size: 14px; -fx-padding: 8px 16px; -fx-background-radius: 5px; -fx-text-fill: white; -fx-background-color: #2196f3;");
        doneButton.setStyle(IDLE_BUTTON_STYLE);
        doneButton.setOnMouseEntered(e -> doneButton.setStyle(HOVERED_BUTTON_STYLE));
        doneButton.setOnMouseExited(e -> doneButton.setStyle(IDLE_BUTTON_STYLE));
        doneButton.setOnMouseClicked(e -> doneButton.setStyle(CLICKED_BUTTON_STYLE));
        content.getChildren().add(doneButton);

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);

        Scene scene = new Scene(new StackPane(scrollPane), 350, 720);
        scene.setFill(Color.TRANSPARENT);

        Stage stage = new Stage();
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.initOwner(ownerStage);
        stage.setScene(scene);
        stage.setTitle("Time Selection");
        stage.show();
    }



    private void recordTimes(List<String> selectedTimes, String selectedDay, String selectedDate) {
        List<String> existingRecords = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("D:\\homework\\Lancasters\\vpp\\LancastersKitchenMgmt\\software\\src\\scenes\\utils\\shifts.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                existingRecords.add(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Check if the record already exists
        boolean recordExists = false;
        for (String existingRecord : existingRecords) {
            String[] parts = existingRecord.split(";");
            String existingEmployee = parts[0];
            String existingDate = parts[1];
            String existingTime = parts[2];
            String existingDay = parts[3];
            if (existingEmployee.equals(employeeName) && existingDate.equals(LocalDate.now().toString()) && existingDay.equals(selectedDay) && selectedTimes.contains(existingTime)) {
                recordExists = true;
                break;
            }
        }

        // If the record doesn't exist, add it
        if (!recordExists) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("D:\\homework\\Lancasters\\vpp\\LancastersKitchenMgmt\\software\\src\\scenes\\utils\\shifts.csv", true))) {
                for (String time : selectedTimes) {
                    LocalDate currentDate = LocalDate.now();
                    writer.write(employeeName + ";" + currentDate + ";" + time + ";" + selectedDay + ";" + selectedDate);
                    writer.newLine();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void submitLeaveRequest(LocalDate fromDate, LocalDate toDate, String reason) {
        try {
            if (toDate.isBefore(fromDate)) {
                throw new Exception();
            }

            LocalDate currentDate = LocalDate.now();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("D:\\homework\\Lancasters\\vpp\\LancastersKitchenMgmt\\software\\src\\scenes\\utils\\leaves.csv", true))) {
                writer.write(employeeName + ";" + currentDate + ";" + fromDate + ";" + toDate + ";" + reason);
                writer.newLine();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("Successfully submitted sick/leave form!");
                alert.showAndWait();
            } catch (IOException e) {
                System.err.println("Error writing to file: " + e.getMessage());
            }

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Incorrect time format!");
            alert.setContentText("Ensure that \"To date\" is after the \"From date\".");
            alert.showAndWait();
        }
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
                        parsedEndTime.getMinute() >= 0 && parsedEndTime.getMinute() <= 59);
            }

            // If end time is null, only validate start time
            if (endTime.isEmpty()) {
                LocalTime parsedStartTime = LocalTime.parse(startTime, formatter);
                return (parsedStartTime.getHour() >= 0 && parsedStartTime.getHour() <= 23 &&
                        parsedStartTime.getMinute() >= 0 && parsedStartTime.getMinute() <= 59);
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

    private void writeClockTime(String clockInTime, String clockOutTime) {

        String csvFilePath = "D:\\homework\\Lancasters\\vpp\\LancastersKitchenMgmt\\software\\src\\scenes\\utils\\clock-ins.csv";
        try {
            List<String> lines = Files.readAllLines(Paths.get(csvFilePath));
            LocalDate currentDate = LocalDate.now();
            String recordToSearch = employeeName + ";" + currentDate + ";" + clockInTime + ";";
            boolean recordFound = false;
            String clockInTimeFromRecord = "";
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                String[] parts = line.split(";");
                if (parts.length >= 3 && parts[0].equals(employeeName) && parts[1].equals(currentDate.toString())) {
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
                String newRecord = employeeName + ";" + currentDate + ";" + clockInTime + ";" + clockOutTime;
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
