package scenes;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.format.DateTimeParseException;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;



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
        roleDropdown.setOnMouseEntered(e -> roleDropdown.setStyle("-fx-font-size: 16px; -fx-background-color: #e0e0e0; -fx-border-color: #999999"));
        roleDropdown.setOnMouseExited(e -> roleDropdown.setStyle("-fx-font-size: 16px; -fx-background-color: #f0f0f0; -fx-border-color: #999999"));
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

        leftPane.getChildren().addAll(viewCurrentMenu, clockInField, clockOutField, clockButton, fromDateField, toDateField, reasonDropdown, submitLeaveButton);

        Button logoutButton = createLogoutButton();
        BorderPane.setAlignment(logoutButton, Pos.BOTTOM_LEFT); // Aligning to the bottom left
        BorderPane.setMargin(logoutButton, new Insets(10, 10, 10, 10));

        GridPane calendarGrid= createCalendarPane();

        layout.setTop(greetingLabel);
        layout.setRight(roleDropdown);
        layout.setLeft(leftPane);
        layout.setBottom(logoutButton);
        layout.setRight(calendarGrid);

        return new Scene(layout, SCREEN_RES_WIDTH, SCREEN_RES_HEIGHT);
    }

    private void getMenu() {
    }

    private void clockInOut() {
        String clockInTime = clockInField.getText();
        String clockOutTime = clockOutField.getText();
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
            ComboBox<String> finalRoleDropdown = roleDropdown;
            roleDropdown.setOnMouseEntered(e -> finalRoleDropdown.setStyle("-fx-font-size: 16px; -fx-background-color: #e0e0e0; -fx-border-color: #999999"));
            ComboBox<String> finalRoleDropdown1 = roleDropdown;
            roleDropdown.setOnMouseExited(e -> finalRoleDropdown1.setStyle("-fx-font-size: 16px; -fx-background-color: #f0f0f0; -fx-border-color: #999999"));
            BorderPane.setAlignment(roleDropdown, Pos.TOP_RIGHT);
            BorderPane.setMargin(roleDropdown, new Insets(-30, 10, 0, 0));
        }
        else {
            roleDropdown = createRoleDropdownDirector();
            roleDropdown.setStyle("-fx-font-size: 16px; -fx-background-color: #f0f0f0; -fx-border-color: #999999;-fx-background-radius: 20");
            ComboBox<String> finalRoleDropdown2 = roleDropdown;
            roleDropdown.setOnMouseEntered(e -> finalRoleDropdown2.setStyle("-fx-font-size: 16px; -fx-background-color: #e0e0e0; -fx-border-color: #999999"));
            ComboBox<String> finalRoleDropdown3 = roleDropdown;
            roleDropdown.setOnMouseExited(e -> finalRoleDropdown3.setStyle("-fx-font-size: 16px; -fx-background-color: #f0f0f0; -fx-border-color: #999999"));
            BorderPane.setAlignment(roleDropdown, Pos.TOP_RIGHT);
            BorderPane.setMargin(roleDropdown, new Insets(-30, 10, 0, 0));
        }
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

        leftPane.getChildren().addAll(viewCurrentMenu, clockInField, clockOutField, clockButton, fromDateField, toDateField, reasonDropdown, submitLeaveButton);

        BorderPane.setAlignment(logoutButton, Pos.BOTTOM_LEFT); // Aligning to the bottom left
        BorderPane.setMargin(logoutButton, new Insets(10, 10, 10, 10));

        GridPane calendarGrid= createCalendarPane();

        layout.setTop(greetingLabel);
        layout.setRight(roleDropdown);
        layout.setLeft(leftPane);
        layout.setRight(calendarGrid);
        layout.setBottom(logoutButton);

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
        for (String time : selectedTimes) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("D:\\homework\\Lancasters\\vpp\\LancastersKitchenMgmt\\software\\src\\scenes\\utils\\shifts.csv", true))) {
                LocalDate currentDate = LocalDate.now();
                writer.write(employeeName + ";" + currentDate + ";" + time + ";" + selectedDay+ ";" +selectedDate);
                writer.newLine();
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
            LocalTime parsedStartTime = LocalTime.parse(startTime, formatter);
            LocalTime parsedEndTime = LocalTime.parse(endTime, formatter);

            if ((parsedStartTime.getHour() >= 0 && parsedStartTime.getHour() <= 23 &&
                    parsedStartTime.getMinute() >= 0 && parsedStartTime.getMinute() <= 59) &&
                    (parsedEndTime.getHour() >= 0 && parsedEndTime.getHour() <= 23 &&
                            parsedEndTime.getMinute() >= 0 && parsedEndTime.getMinute() <= 59)) {

                if (parsedEndTime.isAfter(parsedStartTime)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private void writeClockTime(String clockInTime, String clockOutTime) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("D:\\homework\\Lancasters\\vpp\\LancastersKitchenMgmt\\software\\src\\scenes\\utils\\clock-ins.csv", true))) {
            // TODO: Record this in the database
            LocalDate currentDate = LocalDate.now();
            writer.write(employeeName + ";" + currentDate + ";" + clockInTime + ";" + clockOutTime);
            writer.newLine();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("You successfully added clock-in and clock-out times");
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
