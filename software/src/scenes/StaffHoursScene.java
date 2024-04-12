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
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static scenes.ClockInScene.extractNamesFromFile;


public class StaffHoursScene  extends ManagerScene{
    MockData mockData = new MockData();
    GridPane calendar;
    Set<LocalDate> displayedDates = new HashSet<>();
    static int magicCounter = 0;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    String prevStyle;
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

        layout.setTop(greetingLabel); // Set greetingLabel to the top
        layout.setCenter(calendar); // Set calendar to the center
        layout.setBottom(backButton);
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

                int numWaitersNeeded = totalOccupiedTables / 2;
                int numChefsNeeded = (int) Math.ceil(totalOccupiedTables / 5.0);
                int numManagersNeeded = (int) Math.ceil((numWaitersNeeded + numChefsNeeded)/8.0);
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
        //TODO: check weather it was already there
        //TODO: check weather you over run items for employee
        //TODO: check shifts that current employee has
        for (String time : selectedTimes) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("D:\\homework\\Lancasters\\vpp\\LancastersKitchenMgmt\\software\\src\\scenes\\utils\\shifts.csv", true))) {
                if (magicCounter == 0) {
                    LocalDate currentDate = LocalDate.now();
                    writer.write("");
                    writer.write(employee+ ";" + currentDate + ";" + time + ";" + selectedDay+ ";" +selectedDate);
                    writer.newLine();
                    magicCounter+=1;
                } else {
                    LocalDate currentDate = LocalDate.now();
                    writer.write(employee+ ";" + currentDate + ";" + time + ";" + selectedDay+ ";" +selectedDate);
                    writer.newLine();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            updateCalendar();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
