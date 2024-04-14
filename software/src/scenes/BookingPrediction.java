package scenes;

import core.SceneManager;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class BookingPrediction extends Manager {
    private Scene scene;
    private MockData mockData = new MockData();

    public Scene createScene() {
        mockData.generateYearPredictions();
        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(20));

        Label greetingLabel = new Label("Want to predict some future, " + employeeName + "?");
        greetingLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        BorderPane.setAlignment(greetingLabel, Pos.TOP_LEFT);
        BorderPane.setMargin(greetingLabel, new Insets(10, 0, 0, 10));



        ComboBox<String> monthComboBox = new ComboBox<>();
        monthComboBox.getItems().addAll("January", "February", "March", "April", "May", "June", "July", "August",
                "September", "October", "November", "December");
        monthComboBox.setValue("January");

        monthComboBox.setStyle("-fx-font-size: 14px; -fx-background-color: #f0f0f0; -fx-border-color: #999999;-fx-background-radius: 20");
        monthComboBox.setOnMouseEntered(e -> monthComboBox.setStyle("-fx-font-size: 14px; -fx-background-color: #e0e0e0; -fx-border-color: #999999"));
        monthComboBox.setOnMouseExited(e -> monthComboBox.setStyle("-fx-font-size: 14px; -fx-background-color: #f0f0f0; -fx-border-color: #999999"));
        monthComboBox.setPrefWidth(225);
        Button sortByMoneyButton = createButton("Sort by Money", event -> sortAndDisplayData("money", monthComboBox.getValue()));
        sortByMoneyButton.setPrefWidth(225);
        Button sortByTablesButton= createButton("Sort by Tables", event -> sortAndDisplayData("tables", monthComboBox.getValue()));
        sortByTablesButton.setPrefWidth(225);
        VBox bookingDataContainer = new VBox(10);
        bookingDataContainer.setAlignment(Pos.CENTER_LEFT);
        bookingDataContainer.setPadding(new Insets(10));

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(bookingDataContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefViewportHeight(400); // Adjust as needed
        scrollPane.setStyle("-fx-background-color: #f7f7f7; -fx-background-insets: 0; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");

        Button backButton = createBackButtonManager();
        backButton.setPrefWidth(200);
        Button downloadButton = createButton("Download",event -> {
            // Retrieve booking data for the selected month
            String selectedMonth = monthComboBox.getValue();
            Map<String, List<List<String>>> bookings = mockData.getYearPredictionsForMonth(selectedMonth);
            // Call a method to download the content of the output text field
            downloadData(bookings.toString());
        } );
        downloadButton.setPrefWidth(200);
        HBox bottomBox = new HBox(downloadButton);
        bottomBox.getChildren().add(backButton);
        bottomBox.setAlignment(Pos.CENTER_RIGHT);
        bottomBox.setPadding(new Insets(10));
        bottomBox.setSpacing(150);

        Button showAllDataButton = createButton("All Booking Data", event -> showAllBookingData(bookingDataContainer));
        showAllDataButton.setPrefWidth(225);

// Retrieve booking data and update the UI
        updateBookingData(bookingDataContainer, "January");
        GridPane controlPanel = new GridPane();
        controlPanel.setPadding(new Insets(10));
        controlPanel.setAlignment(Pos.CENTER_LEFT);
        controlPanel.setHgap(10);
        controlPanel.setVgap(10);

// Add buttons to the grid
        controlPanel.add(monthComboBox, 0, 0);
        controlPanel.add(sortByMoneyButton, 0, 1);
        controlPanel.add(sortByTablesButton, 1, 0);
        controlPanel.add(showAllDataButton, 1, 1);
// Adding components to the layout
        layout.setTop(greetingLabel);
        layout.setCenter(scrollPane);
        layout.setLeft(controlPanel);
        layout.setBottom(bottomBox);

        scene = new Scene(layout, SCREEN_RES_WIDTH, SCREEN_RES_HEIGHT);
        return scene;
    }

    private void showAllBookingData(VBox bookingDataContainer) {
        // Clear the current booking data container
        bookingDataContainer.getChildren().clear();

        // Retrieve all booking data
        Map<String, List<List<String>>> allBookings = mockData.bookings;

        // Sort the booking data by date
        TreeMap<String, List<List<String>>> sortedBookings = new TreeMap<>(Comparator.naturalOrder());
        sortedBookings.putAll(allBookings);

        // Display all booking data in sorted order
        for (Map.Entry<String, List<List<String>>> entry : sortedBookings.entrySet()) {
            String date = entry.getKey();
            List<List<String>> predictions = entry.getValue();

            addDateLabel(bookingDataContainer, date);

            for (List<String> prediction : predictions) {
                StringBuilder predictionString = new StringBuilder();
                predictionString.append("Tables Occupied: ").append(prediction.get(0)).append(", ")
                        .append("Time Slot: ").append(prediction.get(1)).append(", ")
                        .append("Predicted Bill: ").append(prediction.get(2));
                addPredictionLabel(bookingDataContainer, predictionString.toString());
            }
        }
    }
    public static void addDateLabel(VBox container, String date) {
        Label dateLabel = new Label("Date: " + date);
        dateLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
        VBox.setMargin(dateLabel, new Insets(5, 0, 5, 0));
        container.getChildren().add(dateLabel);
    }

    public static void addPredictionLabel(VBox container, String predictionData) {
        Label predictionLabel = new Label(predictionData);
        predictionLabel.setStyle("-fx-font-size: 14px;");
        VBox.setMargin(predictionLabel, new Insets(0, 0, 5, 20));
        container.getChildren().add(predictionLabel);
    }
    private void updateBookingData(VBox bookingDataContainer, String selectedMonth) {
        // Retrieve booking data for the selected month
        Map<String, List<List<String>>> bookings = getBookingsForMonth(selectedMonth);

        // Display the retrieved booking data
        for (Map.Entry<String, List<List<String>>> entry : bookings.entrySet()) {
            String date = entry.getKey();
            List<List<String>> predictions = entry.getValue();

            Label dateLabel = new Label("Date: " + date);
            dateLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
            VBox.setMargin(dateLabel, new Insets(5, 0, 5, 0));
            bookingDataContainer.getChildren().add(dateLabel);

            for (List<String> prediction : predictions) {
                StringBuilder predictionString = new StringBuilder();
                predictionString.append("Tables Occupied: ").append(prediction.get(0)).append(", ")
                        .append("Time Slot: ").append(prediction.get(1)).append(", ")
                        .append("Predicted Bill: ").append(prediction.get(2));
                Label predictionLabel = new Label(predictionString.toString());
                predictionLabel.setStyle("-fx-font-size: 14px;");
                VBox.setMargin(predictionLabel, new Insets(0, 0, 5, 20));
                bookingDataContainer.getChildren().add(predictionLabel);
            }
        }
    }
    private Map<String, List<List<String>>> getBookingsForMonth(String selectedMonth) {
        Map<String, List<List<String>>> allBookings = mockData.bookings;
        Map<String, List<List<String>>> bookingsForMonth = new TreeMap<>(Comparator.naturalOrder());

        for (Map.Entry<String, List<List<String>>> entry : allBookings.entrySet()) {
            String date = entry.getKey();
            String month = getMonthFromDate(date);
            if (month.equalsIgnoreCase(selectedMonth)) {
                bookingsForMonth.put(date, entry.getValue());
            }
        }
        return bookingsForMonth;
    }

    private String getMonthFromDate(String date) {
        // Assuming date format is "YYYY-MM-DD", extract the month part
        String[] parts = date.split("/");
        if (parts.length >= 2) {
            int month = Integer.parseInt(parts[1]);
            return switch (month) {
                case 1 -> "January";
                case 2 -> "February";
                case 3 -> "March";
                case 4 -> "April";
                case 5 -> "May";
                case 6 -> "June";
                case 7 -> "July";
                case 8 -> "August";
                case 9 -> "September";
                case 10 -> "October";
                case 11 -> "November";
                case 12 -> "December";
                default -> "";
            };
        }
        return "";
    }

    private void sortAndDisplayData(String sortPreference, String selectedMonth) {
        // Retrieve the root layout
        BorderPane layout = (BorderPane) scene.getRoot();

        // Retrieve the booking data container from the center of the layout
        ScrollPane scrollPane = (ScrollPane) layout.getCenter();
        VBox bookingDataContainer = (VBox) scrollPane.getContent();

        // Clear the current booking data container
        bookingDataContainer.getChildren().clear();

        // Retrieve booking data for the selected month
        Map<String, List<List<String>>> bookings = mockData.getYearPredictionsForMonth(selectedMonth);

        // Sort the booking data based on the selected preference
        Comparator<String> keyComparator = null;

        // Define comparator based on sort preference
        if (sortPreference.equals("money")) {
            keyComparator = (date1, date2) -> {
                double sum1 = bookings.get(date1).stream()
                        .mapToDouble(prediction -> Double.parseDouble(prediction.get(2)))
                        .sum();
                double sum2 = bookings.get(date2).stream()
                        .mapToDouble(prediction -> Double.parseDouble(prediction.get(2)))
                        .sum();
                return Double.compare(sum2, sum1);
            };
        } else if (sortPreference.equals("tables")) {
            keyComparator = (date1, date2) -> {
                int sum1 = bookings.get(date1).stream()
                        .mapToInt(prediction -> Integer.parseInt(prediction.get(0)))
                        .sum();
                int sum2 = bookings.get(date2).stream()
                        .mapToInt(prediction -> Integer.parseInt(prediction.get(0)))
                        .sum();
                return Integer.compare(sum2, sum1);
            };
        }

        // Create a TreeMap with the defined keyComparator
        TreeMap<String, List<List<String>>> sortedBookings = new TreeMap<>(keyComparator);

        // Put all entries from 'bookings' into 'sortedBookings'
        sortedBookings.putAll(bookings);

        // Update the booking data container with the sorted data
        for (Map.Entry<String, List<List<String>>> entry : sortedBookings.entrySet()) {
            String date = entry.getKey();
            List<List<String>> predictions = entry.getValue();

            VBox dateBox = new VBox();
            dateBox.setStyle("-fx-padding: 10px;");
            addDateLabel(dateBox, date);
            bookingDataContainer.getChildren().add(dateBox);

            for (List<String> prediction : predictions) {
                StringBuilder predictionString = new StringBuilder();
                predictionString.append("Tables Occupied: ").append(prediction.get(0)).append(", ")
                        .append("Time Slot: ").append(prediction.get(1)).append(", ")
                        .append("Predicted Bill: ").append(prediction.get(2));
                addPredictionLabel(dateBox, predictionString.toString());
            }
        }
    }
    private void downloadData(String content) {
        // Create a FileChooser
        FileChooser fileChooser = new FileChooser();

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show save file dialog
        File file = fileChooser.showSaveDialog(scene.getWindow());

        if (file != null) {
            saveTextToFile(content, file);
        }
    }

    private void saveTextToFile(String content, File file) {
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(content);
        } catch (IOException e) {
            // Handle exception
            e.printStackTrace();
        }
    }

}