package scenes;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static scenes.ClockIn.extractNamesFromFile;
//TODO: test same shifts
//TODO: test if user has more than 40 hours

public class StaffHours extends Manager {
    MockData mockData = new MockData();
    GridPane calendar;
    Set<LocalDate> displayedDates = new HashSet<>();
    static int magicCounter = 0;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    String prevStyle;
    public static final String SHIFTS_FILE_PATH = "D:\\homework\\Lancasters\\vpp\\LancastersKitchenMgmt\\software\\src\\scenes\\utils\\shifts.csv";
    public Scene createScene() {
        mockData.generateTablePredictions();
        mockData.generateYearPredictions();
        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(20));

        Label greetingLabel = new Label("Let's check the staff hours, " + employeeName + ".");
        greetingLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        BorderPane.setAlignment(greetingLabel, Pos.TOP_LEFT);
        BorderPane.setMargin(greetingLabel, new Insets(10, 0, 0, 10));
        Timeline labelAnimation = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(greetingLabel.opacityProperty(), 0)),
                new KeyFrame(Duration.seconds(1), new KeyValue(greetingLabel.opacityProperty(), 1))
        );
        labelAnimation.play();

        calendar = createCalendarPane();
        Button backButton = createBackButtonManager();
        Button shiftsButton = createButton("View Shifts", actionEvent -> {openShiftsWindow();});

        layout.setTop(greetingLabel); // Set greetingLabel to the top
        layout.setCenter(calendar); // Set calendar to the center
        layout.setBottom(backButton);
        layout.setRight(shiftsButton);
        for (LocalDate date : displayedDates) {
            String dateString = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            calculateStaffNeeded(dateString);
        }
        scheduler.scheduleAtFixedRate(this::updateCalendarTask, 0, 1, TimeUnit.SECONDS);
        return new Scene(layout, SCREEN_RES_WIDTH, SCREEN_RES_HEIGHT);
    }
    public Scene createScene(boolean toggleManager) {
        mockData.generateTablePredictions();
        mockData.generateYearPredictions();
        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(20));

        Label greetingLabel = new Label("Let's check the staff hours, " + employeeName + ".");
        greetingLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        BorderPane.setAlignment(greetingLabel, Pos.TOP_LEFT);
        BorderPane.setMargin(greetingLabel, new Insets(10, 0, 0, 10));
        Timeline labelAnimation = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(greetingLabel.opacityProperty(), 0)),
                new KeyFrame(Duration.seconds(1), new KeyValue(greetingLabel.opacityProperty(), 1))
        );
        labelAnimation.play();

        calendar = createCalendarPane();
        Button backButton = createBackButtonManager();
        Button shiftsButton = createButton("View Shifts", actionEvent -> {openShiftsWindow();});

        layout.setTop(greetingLabel); // Set greetingLabel to the top
        layout.setCenter(calendar); // Set calendar to the center
        layout.setBottom(backButton);
        layout.setRight(shiftsButton);
        for (LocalDate date : displayedDates) {
            String dateString = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            calculateStaffNeeded(dateString);
        }
        scheduler.scheduleAtFixedRate(this::updateCalendarTask, 0, 1, TimeUnit.SECONDS);
        return new Scene(layout, SCREEN_RES_WIDTH, SCREEN_RES_HEIGHT);
    }
    private void updateCalendarTask() {
        try {
            updateCalendar();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void calculateStaffNeeded(String date) {
        int tableWaiterLimit = 2;
        float tableChefLimit = 5.0f;
        float waiterChefManagerLimit = 8.0f;
        Map<Integer, List<List<String>>> tablePredictions = mockData.tablePrediction.get(date);

        if (tablePredictions == null) {
            System.out.println("No table predictions available for the specified date.");
            return;
        }

        Map<String, Map<String, Integer>> dailyQuota = new HashMap<>();

        for (int hour = 17; hour < 23; hour++) {
            for (int minute = 0; minute < 60; minute += 30) {
                String timeSlot = String.format("%02d:%02d-%02d:%02d", hour, minute, hour, minute + 30);
                int totalOccupiedTables = 0;

                for (List<List<String>> tablePrediction : tablePredictions.values()) {
                    for (List<String> timeSlotInfo : tablePrediction) {
                        if (timeSlot.equals(timeSlotInfo.get(0)) && timeSlotInfo.get(1).equals("true")) {
                            totalOccupiedTables++;
                            break;
                        }
                    }
                }

                int numWaitersNeeded = totalOccupiedTables / tableWaiterLimit;
                int numChefsNeeded = (int) Math.ceil(totalOccupiedTables / tableChefLimit);
                int numManagersNeeded = (int) Math.ceil((numWaitersNeeded + numChefsNeeded)/ waiterChefManagerLimit);
                HashMap<String, Integer> quotaHashMap = new HashMap<>();
                quotaHashMap.put("waiter", 1); //TODO: change this shisBen
                quotaHashMap.put("chef", 1);
                quotaHashMap.put("manager", 1);
                dailyQuota.put(timeSlot, quotaHashMap);
            }
        }

        // Write the dailyQuota to a file
        writeQuotaToFile(date, dailyQuota);
    }

    private void writeQuotaToFile(String date, Map<String, Map<String, Integer>> dailyQuota) {
        // Write the quota data to a file, format: "date,timeSlot,quota"
        String correctDate = date.replaceAll("/", "");
        String packageName = "quotas";
        File quotaFile = new File(packageName.replace(".", "/") + "/quota_" + correctDate + ".csv");
        if (!quotaFile.exists()) {
            // Create the package directory if it doesn't exist
            quotaFile.getParentFile().mkdirs();
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(quotaFile))) {
            for (Map.Entry<String, Map<String, Integer>> entry : dailyQuota.entrySet()) {
                writer.write(date + ";" + entry.getKey() + ";" + entry.getValue().get("waiter") + ";" + entry.getValue().get("chef") + ";" + entry.getValue().get("manager") + ";\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void updateCalendar() throws IOException {
        Map<LocalDate, Map<String, List<String>>> result = QuotaChecker.checkQuotas("D:\\homework\\Lancasters\\vpp\\LancastersKitchenMgmt\\software\\src\\scenes\\utils\\shifts.csv", "quotas");

        // Iterate through the displayed dates in the calendar
        for (LocalDate displayedDate : displayedDates) {
            if (result.containsKey(displayedDate)) {
                Map<String, List<String>> timeSlotStatus = result.get(displayedDate);

                // Check if all time slots from 17:00 up to 23:00 are satisfied
                boolean allSatisfied = true;
                for (int hour = 17; hour < 23; hour++) {
                    String startTimeSlot = String.format("%02d:00", hour);
                    String endTimeSlot = String.format("%02d:30", hour);
                    String timeSlotKey = startTimeSlot + "-" + endTimeSlot;

                    List<String> slotInfo = timeSlotStatus.getOrDefault(timeSlotKey, Arrays.asList("false", ""));
                    String satisfied = slotInfo.get(0);
                    String quotaInfo = slotInfo.get(1);

                    if (!"true".equals(satisfied)) {
                        allSatisfied = false;
                        break;
                    }
                }

                // Update appearance of the calendar day based on validation result
                StackPane dayPane = findDayPane(displayedDate);
                if (dayPane != null) {
                    if (allSatisfied) {
                        dayPane.setStyle("-fx-background-color: #90EE90;");
                        prevStyle = "-fx-background-color: #90EE90;";
                    } else {
                        dayPane.setStyle("-fx-background-color: #FFA07A;");
                        prevStyle = "-fx-background-color: #FFA07A";
                    }
                }
            }
        }
    }

    // Helper method to find the StackPane corresponding to a displayed date in the calendar
    private StackPane findDayPane(LocalDate date) {
        for (Node node : calendar.getChildren()) {
            if (node instanceof StackPane && GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) >= 2) {
                StackPane dayPane = (StackPane) node;
                int dayOfMonth = Integer.parseInt(((Label) dayPane.getChildren().get(0)).getText());
                LocalDate displayedDate = LocalDate.now().withDayOfMonth(1).plusDays(dayOfMonth - 1);
                if (displayedDate.equals(date)) {
                    return dayPane;
                }
            }
        }
        return null;
    }

    private GridPane createCalendarPane() {
        Map<LocalDate, Map<String, List<String>>> result;
        try {
            result = QuotaChecker.checkQuotas("D:\\homework\\Lancasters\\vpp\\LancastersKitchenMgmt\\software\\src\\scenes\\utils\\shifts.csv", "quotas");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
                dayPane.setOnMouseEntered(event -> {
                    if (!dayPane.getStyle().contains("-fx-background-color: #f0f0f0")) {
                        prevStyle = dayPane.getStyle();
                        dayPane.setStyle("-fx-background-color: #f0f0f0;");
                    }
                });
                dayPane.setOnMouseExited(event -> dayPane.setStyle(prevStyle));
                int finalDayOfMonth = dayOfMonth;
                dayPane.setOnMouseClicked(event -> {
                    String message = "You clicked on " + dayLabel.getText();
                    LocalDate selectedDate = firstDayOfMonth.plusDays(finalDayOfMonth - 1);
                    showTimeSelectionWindow((Stage) calendarPane.getScene().getWindow(), selectedDate, result);
                });
                calendarPane.add(dayPane, col, row);
                dayOfMonth++;
                displayedDates.add(firstDayOfMonth.plusDays(finalDayOfMonth - 1));
            }
        }
        return calendarPane;
    }

    private void showTimeSelectionWindow(Stage ownerStage, LocalDate currentDate, Map<LocalDate, Map<String, List<String>>> result) {
        VBox content = new VBox(5);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(10));
        content.setStyle("-fx-background-color: #ffffff; -fx-border-color: #ccc; -fx-border-width: 1px; -fx-border-radius: 5px;");

        // Creating the ComboBox to select employee names
        ObservableList<String> employeeNames = FXCollections.observableArrayList(extractNamesFromFile("D:\\homework\\Lancasters\\vpp\\LancastersKitchenMgmt\\software\\src\\scenes\\usersInfo\\users_info.txt"));
        ComboBox<String> employeeComboBox = new ComboBox<>(employeeNames);
        employeeComboBox.setPrefWidth(INPUT_FIELD_WIDTH);
        employeeComboBox.setPromptText("Select Employee");
        employeeComboBox.setStyle("-fx-font-size: 16px; -fx-background-color: #f0f0f0; -fx-border-color: #999999;-fx-background-radius: 20");

        // Event handlers for ComboBox
        ComboBox<String> finalEmployeeComboBox = employeeComboBox;
        employeeComboBox.setOnMouseEntered(e -> finalEmployeeComboBox.setStyle("-fx-font-size: 16px; -fx-background-color: #e0e0e0; -fx-border-color: #999999"));
        ComboBox<String> finalEmployeeComboBox2 = employeeComboBox;
        employeeComboBox.setOnMouseExited(e -> finalEmployeeComboBox2.setStyle("-fx-font-size: 16px; -fx-background-color: #f0f0f0; -fx-border-color: #999999"));

        List<Label> timeLabels = new ArrayList<>();
        List<String> selectedTimes = new ArrayList<>();

        String currentDay = currentDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());
        String currDate = currentDate.toString();

        // Adding ComboBox to the content
        content.getChildren().add(employeeComboBox);
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

                timeLabel.setOnMouseClicked(e -> {
                    if (selectedTimes.contains(time)) {
                        selectedTimes.remove(time);
                        timeLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #333;");
                    } else {
                        selectedTimes.add(time);
                        timeLabel.setStyle("-fx-font-size: 18px; -fx-background-color: #90caf9;");
                    }
                });

                // Highlight time slots from 17 to 23 with unsatisfied quotas
                if (hour >= 17 && hour < 23 && result.containsKey(currentDate)) {
                    Map<String, List<String>> timeSlotStatus = result.get(currentDate);
                    String timeSlotKey = String.format("%02d:%02d-%02d:%02d", hour, minute, hour, minute + 30);
                    List<String> slotInfo = timeSlotStatus.getOrDefault(timeSlotKey, Arrays.asList("false", ""));
                    String satisfied = slotInfo.get(0);
                    if ("false".equals(satisfied)) {
                        timeLabel.setStyle("-fx-font-size: 18px; -fx-background-color: #ffcccc;"); // Red color for unsatisfied quotas
                    }
                    // Display number of different employees needed
                    String quotaInfo = slotInfo.get(1);
                    if (!"".equals(quotaInfo)) {
                        String[] quotas = quotaInfo.split(", ");
                        int numWaitersNeeded = Integer.parseInt(quotas[0]);
                        int numChefsNeeded = Integer.parseInt(quotas[1]);
                        int numManagersNeeded = Integer.parseInt(quotas[2]);
                        timeLabel.setText(time + " - " + nextTime + " (" + numManagersNeeded + " waiters, " + numChefsNeeded + " chefs, " + numWaitersNeeded + " managers)");
                    }
                }

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
            String selectedEmployee = employeeComboBox.getValue(); // Retrieve selected employee name
            if (selectedEmployee != null && !selectedTimes.isEmpty()) {
                recordTimes(selectedTimes, currentDay, currDate, selectedEmployee); // Pass selected employee name to recordTimes
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

        Scene scene = new Scene(new StackPane(scrollPane), 450, 720);
        scene.setFill(Color.TRANSPARENT);

        Stage stage = new Stage();
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.initOwner(ownerStage);
        stage.setScene(scene);
        stage.setTitle("Time Selection");
        stage.show();
    }
    private void recordTimes(List<String> selectedTimes, String selectedDay, String selectedDate, String employee) {
        // Read existing records
        List<String> existingRecords = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(SHIFTS_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                existingRecords.add(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Calculate total hours worked by the employee in the current week
        double totalHoursThisWeek = calculateTotalHoursThisWeek(employee);

        // Calculate total hours to be added with the new shift
        double hoursToAdd = selectedTimes.size() * 0.5;

        // Check if adding the new shift will exceed 40 hours
        if (totalHoursThisWeek + hoursToAdd > 40) {
            showAlert("Warning", "Employee has already worked 40 hours this week.", Alert.AlertType.WARNING);
            return;
        }

        boolean recordExists = false;
        for (String existingRecord : existingRecords) {
            String[] parts = existingRecord.split(";");
            String existingEmployee = parts[0];
            String existingDate = parts[1];
            String existingTime = parts[2];
            String existingDay = parts[3];
            if (existingEmployee.equals(employee) && existingDate.equals(LocalDate.now().toString()) && existingTime.equals(selectedTimes.get(0)) && existingDay.equals(selectedDay)) {
                recordExists = true;
                break;
            }
        }
        if (recordExists) {
            showAlert("Warning", "You tried to add shifts that were added previously.", Alert.AlertType.WARNING);
        }        // If the record doesn't exist, add it
        if (!recordExists) {
            // Add the new shift
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(SHIFTS_FILE_PATH, true))) {
                for (String time : selectedTimes) {
                    LocalDate currentDate = LocalDate.now();
                    writer.write(employee + ";" + currentDate + ";" + time + ";" + selectedDay + ";" + selectedDate);
                    writer.newLine();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            showAlert("Done", "You successfully recorded selected shifts.", Alert.AlertType.INFORMATION);
        }
        try {
            updateCalendar();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private double calculateTotalHoursThisWeek(String employee) {
        // Calculate total hours worked by the employee in the current week
        double totalHoursThisWeek = 0.0;
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.minusDays(today.getDayOfWeek().getValue() - 1); // Assuming week starts from Monday
        try (BufferedReader reader = new BufferedReader(new FileReader(SHIFTS_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                String existingEmployee = parts[0];
                LocalDate existingDate = LocalDate.parse(parts[1]);
                if (existingEmployee.equals(employee) && existingDate.isAfter(startOfWeek.minusDays(1)) && existingDate.isBefore(startOfWeek.plusDays(7))) {
                    totalHoursThisWeek += 0.5; // Assuming each shift is for 30 minutes
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return totalHoursThisWeek;
    }
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    private void openShiftsWindow() {
        Stage shiftsStage = new Stage();
        shiftsStage.setTitle("Employee Shifts");
        shiftsStage.setWidth(620);
        shiftsStage.setHeight(640);

        // Read shifts data
        Map<String, List<String>> shiftsData = readShiftsData();

        // Calculate total hours worked for each employee in a month
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
                shiftArr[1] = "Created on: " + shiftArr[1] +". ";
                shiftArr[2] = "Shift time-slot: " + shiftArr[2]+". ";
                shiftArr[3] = "Day of shift: " + shiftArr[3]+". ";
                shiftArr[4] = "Date of shift: " + shiftArr[4]+". ";
                shift = shiftArr[1] + shiftArr[2] + shiftArr[3] + shiftArr[4];
                Label shiftLabel = new Label(shift);
                shiftLabel.setFont(Font.font("Arial", 12));
                shift = tempShift;

                String finalShift = shift;
                Button deleteButton = createButton("-", e -> {
                    // Remove the shift from shiftsData and update the shifts.csv file
                    shifts.remove(finalShift);
                    updateShiftsFile(shiftsData);
                    // Update the layout
                    shiftsBox.getChildren().remove(shiftRow);
                }, 2, 4, 2);


                shiftRow.getChildren().addAll(shiftLabel, deleteButton);
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

    private Map<String, List<String>> readShiftsData() {
        Map<String, List<String>> shiftsData = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(SHIFTS_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                String employee = parts[0];
                String shiftCreationDate = parts[1];
                String startTime = parts[2];
                String dayOfWeek = parts[3];
                String shiftDate = parts[4];
                String shiftInfo = String.format("%s;%s;%s;%s;%s", employee, shiftCreationDate, startTime, dayOfWeek, shiftDate);
                shiftsData.computeIfAbsent(employee, k -> new ArrayList<>()).add(shiftInfo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return shiftsData;
    }

    private Map<String, Double> calculateTotalHoursPerEmployee(Map<String, List<String>> shiftsData) {
        Map<String, Double> totalHoursPerEmployee = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : shiftsData.entrySet()) {
            double totalHours = entry.getValue().size() * 0.5; // Assuming each shift is for 30 minutes
            totalHoursPerEmployee.put(entry.getKey(), totalHours);
        }
        return totalHoursPerEmployee;
    }

    private void updateShiftsFile(Map<String, List<String>> shiftsData) {
        try (FileWriter writer = new FileWriter(SHIFTS_FILE_PATH)) {
            for (Map.Entry<String, List<String>> entry : shiftsData.entrySet()) {
                String employee = entry.getKey();
                for (String shift : entry.getValue()) {
                    writer.write(shift + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
