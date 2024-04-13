package scenes;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.geometry.Pos;
import scenes.ManagerScene;
import scenes.MockData;

import java.text.DecimalFormat;
import java.util.*;

public class SalesAnalysisScene extends ManagerScene {
    // Method to calculate sales for each menu item
    private Map<String, Integer> calculateMenuItemSales(Map<Integer, List<List<String>>> billData) {
        Map<String, Integer> menuItemSales = new HashMap<>();
        for (List<List<String>> bill : billData.values()) {
            for (String menuItem : bill.get(0)) { // Extracting menu items from the bill
                menuItemSales.put(menuItem, menuItemSales.getOrDefault(menuItem, 0) + 1);
            }
        }
        return menuItemSales;
    }

    // Method to calculate average amount spent
    private double calculateAverageAmountSpent(Map<Integer, List<List<String>>> billData) {
        double totalAmount = 0;
        for (List<List<String>> bill : billData.values()) {
            totalAmount += Double.parseDouble(bill.get(1).get(2)); // Total bill amount
        }
        return totalAmount / billData.size();
    }

    // Method to calculate sales for each table
    private Map<String, Integer> calculateTableSales(Map<Integer, List<List<String>>> billData) {
        Map<String, Integer> tableSales = new HashMap<>();
        for (List<List<String>> bill : billData.values()) {
            String tableNumber = bill.get(1).get(5); // Table number from bill info
            tableSales.put(tableNumber, tableSales.getOrDefault(tableNumber, 0) + 1);
        }
        return tableSales;
    }

    // Method to calculate the most valuable waiter
    private String calculateMostValuableWaiter(Map<Integer, List<List<String>>> billData) {
        Map<String, Double> waiterSales = new HashMap<>();
        for (List<List<String>> bill : billData.values()) {
            String waiterName = bill.get(1).get(1); // Waiter name from bill info
            double totalBillAmount = Double.parseDouble(bill.get(1).get(2)); // Total bill amount
            waiterSales.put(waiterName, waiterSales.getOrDefault(waiterName, 0.0) + totalBillAmount);
        }
        return Collections.max(waiterSales.entrySet(), Comparator.comparingDouble(Map.Entry::getValue)).getKey();
    }

    // Method to calculate less popular menu items
    private List<String> calculateLessPopularItems(Map<String, Integer> menuItemSales) {
        List<String> lessPopularItems = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : menuItemSales.entrySet()) {
            if (entry.getValue() <= 3) { // Define less popular based on a threshold (e.g., 3)
                lessPopularItems.add(entry.getKey());
            }
        }
        return lessPopularItems;
    }
    private List<String> calculatePeakHours(Map<Integer, List<List<String>>> billData) {
        Map<String, Integer> timeslotCount = new HashMap<>();
        for (List<List<String>> bill : billData.values()) {
            String timeslot = bill.get(1).get(0); // Timeslot from bill info
            timeslotCount.put(timeslot, timeslotCount.getOrDefault(timeslot, 0) + 1);
        }

        int maxCount = Collections.max(timeslotCount.values());
        List<String> peakHours = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : timeslotCount.entrySet()) {
            if (entry.getValue() == maxCount) {
                peakHours.add(entry.getKey());
            }
        }
        return peakHours;
    }

    // Create scene method
    public Scene createScene() {
        MockData mockData = new MockData();

        mockData.addMenuData();
        mockData.createMenu();
        mockData.generateBills();
        Map<Integer, List<List<String>>> billData = mockData.bill;
        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(20));

        Label greetingLabel = new Label("Time to do some analysis, " + employeeName + ".");
        greetingLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        BorderPane.setAlignment(greetingLabel, Pos.TOP_LEFT);
        BorderPane.setMargin(greetingLabel, new Insets(10, 0, 0, 10));
        Timeline labelAnimation = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(greetingLabel.opacityProperty(), 0)),
                new KeyFrame(Duration.seconds(1), new KeyValue(greetingLabel.opacityProperty(), 1))
        );
        labelAnimation.play();

        Button backButton = createBackButtonManager();

        // Generate statistics
        Map<String, Integer> menuItemSales = calculateMenuItemSales(billData);
        double averageAmountSpent = calculateAverageAmountSpent(billData);
        String mostValuableWaiter = calculateMostValuableWaiter(billData);
        Map<String, Integer> tableSales = calculateTableSales(billData);
        List<String> lessPopularItems = calculateLessPopularItems(menuItemSales);
        List<String> peakHours = calculatePeakHours(billData);

        // Left section: Popular Menu Items
        VBox popularDishesBox = new VBox(5);
        popularDishesBox.getChildren().add(new Label("Popular Menu Items:"));

// Sort the menu items by sales count in descending order
        List<Map.Entry<String, Integer>> sortedMenuItems = new ArrayList<>(menuItemSales.entrySet());
        sortedMenuItems.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

// Display only the top 15 popular menu items
        int itemCount = 0;
        for (Map.Entry<String, Integer> entry : sortedMenuItems) {
            if (itemCount >= 15) {
                break;
            }
            popularDishesBox.getChildren().add(new Label(entry.getKey() + ": " + entry.getValue() + " sold"));
            itemCount++;
        }

        ScrollPane popularDishesScrollPane = new ScrollPane(popularDishesBox);
        popularDishesScrollPane.setFitToWidth(true);
        layout.setLeft(popularDishesScrollPane);
        // Center section: Less Popular Menu Items, Most Popular Tables, and Peak Hours
        VBox centerBox = new VBox(10);
        centerBox.setAlignment(Pos.CENTER);
        // Less Popular Menu Items
        VBox lessPopularDishesBox = new VBox(5);
        lessPopularDishesBox.getChildren().add(new Label("Less Popular Menu Items:"));
        lessPopularItems.forEach(item -> lessPopularDishesBox.getChildren().add(new Label(item)));
        ScrollPane lessPopularDishesScrollPane = new ScrollPane(lessPopularDishesBox);
        lessPopularDishesScrollPane.setFitToWidth(true);
        lessPopularDishesScrollPane.setPrefViewportHeight(150);
        centerBox.getChildren().add(lessPopularDishesScrollPane);

        // Most Popular Tables
        VBox popularTablesBox = new VBox(5);
        popularTablesBox.getChildren().add(new Label("Most Popular Tables:"));
        tableSales.entrySet().stream()
                .sorted((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()))
                .limit(5)
                .forEach(entry -> popularTablesBox.getChildren().add(new Label(entry.getKey() + ": " + entry.getValue() + " times")));
        ScrollPane popularTablesScrollPane = new ScrollPane(popularTablesBox);
        popularTablesScrollPane.setFitToWidth(true);
        popularTablesScrollPane.setPrefViewportHeight(150);
        centerBox.getChildren().add(popularTablesScrollPane);

        // Peak Hours
        VBox peakHoursBox = new VBox(5);
        peakHoursBox.getChildren().add(new Label("Peak Hours:"));
        peakHours.forEach(hour -> peakHoursBox.getChildren().add(new Label(hour)));
        ScrollPane peakHoursScrollPane = new ScrollPane(peakHoursBox);
        peakHoursScrollPane.setFitToWidth(true);
        peakHoursScrollPane.setPrefViewportHeight(150);
        centerBox.getChildren().add(peakHoursScrollPane);

        layout.setCenter(centerBox);

        // Right section: Average bill and most valuable waiter
        VBox rightBox = new VBox(10);
        rightBox.setAlignment(Pos.CENTER);
// Format average amount spent to display only 2 decimals
        DecimalFormat df = new DecimalFormat("#.##");
        String formattedAverageAmountSpent = df.format(averageAmountSpent);
        rightBox.getChildren().addAll(
                new Label("Average Amount Spent: $" + formattedAverageAmountSpent),
                new Label("Most Valuable Waiter: " + mostValuableWaiter)
        );
        layout.setRight(rightBox);

        layout.setTop(greetingLabel);
        layout.setBottom(backButton);

        return new Scene(layout, SCREEN_RES_WIDTH, SCREEN_RES_HEIGHT);
    }
    private HBox createDateRangeInput(String label) {
        DatePicker startDatePicker = new DatePicker();
        DatePicker endDatePicker = new DatePicker();

        Label startLabel = new Label("Start Date:");
        Label endLabel = new Label("End Date:");

        HBox hbox = new HBox(10);
        hbox.getChildren().addAll(new Label(label), startLabel, startDatePicker, endLabel, endDatePicker);

        return hbox;
    }
}
