package scenes;

import core.SceneManager;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import javafx.geometry.Pos;
import javafx.util.Pair;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;


public class SalesAnalysis extends Manager {
    BorderPane layout;
    MockData mockData;
    private DatePicker startDatePicker = new DatePicker();
    private DatePicker endDatePicker = new DatePicker();
    HBox dateRangeSelector;
    // Method to calculate sales for each menu item

    private Map<String, Integer> calculateMenuItemSales(Map<Integer, List<List<String>>> billData, LocalDate startDate, LocalDate endDate) {
        Map<String, Integer> menuItemSales = new HashMap<>();
        for (List<List<String>> bill : billData.values()) {
            LocalDate transactionDate = LocalDate.parse(bill.get(1).get(6)); // Date of the transaction
            if (transactionDate.isAfter(startDate) && transactionDate.isBefore(endDate)) {
                for (String menuItem : bill.get(0)) { // Extracting menu items from the bill
                    menuItemSales.put(menuItem, menuItemSales.getOrDefault(menuItem, 0) + 1);
                }
            }
        }
        return menuItemSales;
    }

    // Method to calculate average amount spent
    private void updateScene() {
        Pair<LocalDate, LocalDate> selectedDateRange = getSelectedDateRange();
        LocalDate startDate = selectedDateRange.getKey();
        LocalDate endDate = selectedDateRange.getValue();

        // Recalculate data based on the new date range
        Map<Integer, List<List<String>>> billData = mockData.bill; // Assuming mockData is accessible here

        Map<String, Integer> menuItemSales = calculateMenuItemSales(billData, startDate, endDate);
        double averageAmountSpent = calculateAverageAmountSpent(billData, startDate, endDate);
        String mostValuableWaiter = calculateMostValuableWaiter(billData, startDate, endDate);
        Map<String, Integer> tableSales = calculateTableSales(billData, startDate, endDate);
        List<String> lessPopularItems = calculateLessPopularItems(menuItemSales, startDate, endDate);
        List<String> peakHours = calculatePeakHours(billData, startDate, endDate);

        double totalRevenue = calculateTotalRevenue(billData, startDate, endDate); // Calculate total revenue

        updatePopularDishes(menuItemSales); // Update popular dishes
        updateLessPopularDishes(lessPopularItems); // Update less popular dishes
        updatePopularTables(tableSales); // Update popular tables
        updatePeakHours(peakHours); // Update peak hours
        updateRightBox(averageAmountSpent, mostValuableWaiter, totalRevenue); // Update right box

    }

    private void updatePopularDishes(Map<String, Integer> menuItemSales) {
        Node leftNode = layout.getLeft();
        if (leftNode instanceof ScrollPane) {
            ScrollPane popularDishesScrollPane = (ScrollPane) leftNode;
            Node content = popularDishesScrollPane.getContent();
            if (content instanceof VBox) {
                VBox popularDishesContent = (VBox) content;
                popularDishesContent.getChildren().clear();
                popularDishesContent.getChildren().addAll(
                        dateRangeSelector
                );
                int itemCount = 0;
                List<Map.Entry<String, Integer>> sortedMenuItems = new ArrayList<>(menuItemSales.entrySet());
                sortedMenuItems.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
                for (Map.Entry<String, Integer> entry : sortedMenuItems) {
                    if (itemCount >= 15) {
                        break;
                    }
                    popularDishesContent.getChildren().add(new Label(entry.getKey() + ": " + entry.getValue() + " sold"));
                    itemCount++;
                }
                Label popularDishesHeading = new Label("Popular Menu Items:");
                popularDishesHeading.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
                popularDishesContent.getChildren().add(0, popularDishesHeading);
                popularDishesContent.setSpacing(10);
                popularDishesContent.setBorder(new Border(new BorderStroke(Color.LIGHTGRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
                popularDishesContent.setPadding(new Insets(10));

            }

        }
    }

    private double calculateTotalRevenue(Map<Integer, List<List<String>>> billData, LocalDate startDate, LocalDate endDate) {
        double totalRevenue = 0;
        for (List<List<String>> bill : billData.values()) {
            LocalDate transactionDate = LocalDate.parse(bill.get(1).get(6)); // Date of the transaction
            if (transactionDate.isAfter(startDate) && transactionDate.isBefore(endDate)) {
                totalRevenue += Double.parseDouble(bill.get(1).get(2)); // Total bill amount
            }
        }
        return totalRevenue;
    }

    private void updateRightBox(double averageAmountSpent, String mostValuableWaiter, double totalRevenue) {
        Node rightNode = layout.getRight();
        if (rightNode instanceof VBox) {
            VBox rightBox = (VBox) rightNode;
            rightBox.getChildren().clear();
            DecimalFormat df = new DecimalFormat("#.##");
            String formattedAverageAmountSpent = df.format(averageAmountSpent);
            String formattedTotalRevenue = df.format(totalRevenue);
            Label revenueLabel = new Label("Total Revenue: £" + formattedTotalRevenue);
            revenueLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

            Label averageAmountSpentLabel = new Label("Average Amount Spent: £" + formattedAverageAmountSpent);
            averageAmountSpentLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

            Label mostValuableWaiterLabel = new Label("Most Valuable Waiter: " + mostValuableWaiter);
            mostValuableWaiterLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
            rightBox.getChildren().addAll(
                    dateRangeSelector,
                    averageAmountSpentLabel,
                    revenueLabel,
                    mostValuableWaiterLabel
            );
        }
    }

    private void updateLessPopularDishes(List<String> lessPopularItems) {
        Node centerNode = layout.getCenter();
        if (centerNode instanceof VBox) {
            VBox centerBox = (VBox) centerNode;
            if (!centerBox.getChildren().isEmpty()) {
                Node firstChild = centerBox.getChildren().get(0);
                if (firstChild instanceof ScrollPane) {
                    ScrollPane lessPopularDishesScrollPane = (ScrollPane) firstChild;
                    Node content = lessPopularDishesScrollPane.getContent();
                    if (content instanceof VBox) {
                        VBox lessPopularDishesBox = (VBox) content;
                        lessPopularDishesBox.getChildren().clear();
                        Label lesspopularDishesHeading = new Label("Less Popular Menu Items:");
                        lesspopularDishesHeading.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
                        lessPopularDishesBox.getChildren().addAll(
                                lesspopularDishesHeading,
                                dateRangeSelector
                        );
                        lessPopularItems.forEach(item -> lessPopularDishesBox.getChildren().add(new Label(item)));
                    }
                }
            }

        }
    }

    private void updatePopularTables(Map<String, Integer> tableSales) {
        Node centerNode = layout.getCenter();
        if (centerNode instanceof VBox) {
            VBox centerBox = (VBox) centerNode;
            if (centerBox.getChildren().size() > 1) {
                Node secondChild = centerBox.getChildren().get(1);
                if (secondChild instanceof ScrollPane) {
                    ScrollPane popularTablesScrollPane = (ScrollPane) secondChild;
                    Node content = popularTablesScrollPane.getContent();
                    Label popularTables = new Label("Most Popular Tables:");
                    popularTables.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
                    if (content instanceof VBox) {
                        VBox popularTablesBox = (VBox) content;
                        popularTablesBox.getChildren().clear();
                        tableSales.entrySet().stream()
                                .sorted((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()))
                                .limit(5)
                                .forEach(entry -> popularTablesBox.getChildren().add(new Label(entry.getKey() + ": " + entry.getValue() + " times")));
                        popularTablesBox.getChildren().add(0, popularTables);
                    }
                }
            }
        }
    }

    private void updatePeakHours(List<String> peakHours) {
        Node centerNode = layout.getCenter();
        if (centerNode instanceof VBox) {
            VBox centerBox = (VBox) centerNode;
            if (centerBox.getChildren().size() > 2) {
                Node thirdChild = centerBox.getChildren().get(2);
                if (thirdChild instanceof ScrollPane) {
                    ScrollPane peakHoursScrollPane = (ScrollPane) thirdChild;
                    Node content = peakHoursScrollPane.getContent();
                    Label peakHourss = new Label("Peak Hours:");
                    peakHourss.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
                    if (content instanceof VBox) {
                        VBox peakHoursBox = (VBox) content;
                        peakHoursBox.getChildren().clear();
                        peakHours.forEach(hour -> peakHoursBox.getChildren().add(new Label(hour)));
                        peakHoursBox.getChildren().add(0, peakHourss);
                    }

                }
            }
        }
    }


    // Create scene method
    public Scene createScene() {

        dateRangeSelector = createDateRangeSelector();

        startDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> updateScene());
        endDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> updateScene());

        mockData = new MockData();

        mockData.addMenuData();
        mockData.createMenu();
        mockData.generateBills();
        Map<Integer, List<List<String>>> billData = mockData.bill;
        layout = new BorderPane();
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


        // Get selected date range

        // Left section: Popular Menu Items
        VBox popularDishesBox = new VBox(5);
        popularDishesBox.getChildren().addAll(
                dateRangeSelector
        );

        // Sort the menu items by sales count in descending order

        // Display only the top 15 popular menu items


        ScrollPane popularDishesScrollPane = new ScrollPane(popularDishesBox);
        popularDishesScrollPane.setFitToWidth(true);
        layout.setLeft(popularDishesScrollPane);

        // Center section: Less Popular Menu Items, Most Popular Tables, and Peak Hours
        VBox centerBox = new VBox(10);
        centerBox.setAlignment(Pos.CENTER);

        // Less Popular Menu Items
        VBox lessPopularDishesBox = new VBox(5);
        lessPopularDishesBox.getChildren().addAll(
                dateRangeSelector
        );
        Pair<LocalDate, LocalDate> selectedDateRange = getSelectedDateRange();
        LocalDate startDate = selectedDateRange.getKey();
        LocalDate endDate = selectedDateRange.getValue();

        // Generate statistics based on selected date range
        Map<String, Integer> menuItemSales = calculateMenuItemSales(billData, startDate, endDate);
        double averageAmountSpent = calculateAverageAmountSpent(billData, startDate, endDate);
        String mostValuableWaiter = calculateMostValuableWaiter(billData, startDate, endDate);
        Map<String, Integer> tableSales = calculateTableSales(billData, startDate, endDate);
        List<String> lessPopularItems = calculateLessPopularItems(menuItemSales, startDate, endDate);
        List<String> peakHours = calculatePeakHours(billData, startDate, endDate);

        List<Map.Entry<String, Integer>> sortedMenuItems = new ArrayList<>(menuItemSales.entrySet());
        sortedMenuItems.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        int itemCount = 0;
        for (Map.Entry<String, Integer> entry : sortedMenuItems) {
            if (itemCount >= 15) {
                break;
            }
            popularDishesBox.getChildren().add(new Label(entry.getKey() + ": " + entry.getValue() + " sold"));
            itemCount++;
        }

        lessPopularItems.forEach(item -> lessPopularDishesBox.getChildren().add(new Label(item)));
        ScrollPane lessPopularDishesScrollPane = new ScrollPane(lessPopularDishesBox);
        lessPopularDishesScrollPane.setFitToWidth(true);
        lessPopularDishesScrollPane.setPrefViewportHeight(150);
        centerBox.getChildren().add(lessPopularDishesScrollPane);

        // Most Popular Tables
        VBox popularTablesBox = new VBox(5);
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
        peakHours.forEach(hour -> peakHoursBox.getChildren().add(new Label(hour)));
        ScrollPane peakHoursScrollPane = new ScrollPane(peakHoursBox);
        peakHoursScrollPane.setFitToWidth(true);
        peakHoursScrollPane.setPrefViewportHeight(150);
        centerBox.getChildren().add(peakHoursScrollPane);

        layout.setCenter(centerBox);

        // Right section: Average bill, total revenue, and most valuable waiter
        VBox rightBox = new VBox(10);

        // Format average amount spent and total revenue to display only 2 decimals
        DecimalFormat df = new DecimalFormat("#.##");
        String formattedAverageAmountSpent = df.format(averageAmountSpent);
        double totalRevenue = calculateTotalRevenue(billData, startDate, endDate);
        String formattedTotalRevenue = df.format(totalRevenue);
        layout.setRight(rightBox);
        rightBox.setAlignment(Pos.CENTER);


        layout.setTop(greetingLabel);
        layout.setBottom(backButton);
        popularDishesBox.setStyle("-fx-background-color: #f9f9f9;");
        lessPopularDishesBox.setStyle("-fx-background-color: #f9f9f9;");
        popularTablesBox.setStyle("-fx-background-color: #f9f9f9;");
        peakHoursBox.setStyle("-fx-background-color: #f9f9f9;");
        rightBox.setStyle("-fx-background-color: #f9f9f9;");
        // Add bold and increase font size for headings

        Label popularDishesHeading = new Label("Popular Menu Items:");
        popularDishesHeading.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label lessPopularDishesHeading = new Label("Less Popular Menu Items:");
        lessPopularDishesHeading.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label popularTablesHeading = new Label("Most Popular Tables:");
        popularTablesHeading.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label peakHoursHeading = new Label("Peak Hours:");
        peakHoursHeading.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

// Add headings to the corresponding boxes
        popularDishesBox.getChildren().add(0, popularDishesHeading);
        lessPopularDishesBox.getChildren().add(0, lessPopularDishesHeading);
        popularTablesBox.getChildren().add(0, popularTablesHeading);
        peakHoursBox.getChildren().add(0, peakHoursHeading);

        Label revenueLabel = new Label("Total Revenue: £" + formattedTotalRevenue);
        revenueLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Label averageAmountSpentLabel = new Label("Average Amount Spent: $" + formattedAverageAmountSpent);
        averageAmountSpentLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Label mostValuableWaiterLabel = new Label("Most Valuable Waiter: " + mostValuableWaiter);
        mostValuableWaiterLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

// Add labels to the rightBox
        rightBox.setAlignment(Pos.BASELINE_LEFT);

// Add labels to the rightBox
        rightBox.getChildren().addAll(
                dateRangeSelector,
                averageAmountSpentLabel,
                revenueLabel,
                mostValuableWaiterLabel
        );
// Add padding to sections
        popularDishesBox.setPadding(new Insets(10));
        lessPopularDishesBox.setPadding(new Insets(10));
        popularTablesBox.setPadding(new Insets(10));
        peakHoursBox.setPadding(new Insets(10));
        rightBox.setPadding(new Insets(10));

// Apply border to sections
        popularDishesBox.setBorder(new Border(new BorderStroke(Color.LIGHTGRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        lessPopularDishesBox.setBorder(new Border(new BorderStroke(Color.LIGHTGRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        popularTablesBox.setBorder(new Border(new BorderStroke(Color.LIGHTGRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        peakHoursBox.setBorder(new Border(new BorderStroke(Color.LIGHTGRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        rightBox.setBorder(new Border(new BorderStroke(Color.LIGHTGRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

// Add spacing between elements
        popularDishesBox.setSpacing(10);
        lessPopularDishesBox.setSpacing(10);
        popularTablesBox.setSpacing(10);
        peakHoursBox.setSpacing(10);
        rightBox.setSpacing(10);

// Set text color
        popularDishesBox.setStyle("-fx-text-fill: #333;");
        lessPopularDishesBox.setStyle("-fx-text-fill: #333;");
        popularTablesBox.setStyle("-fx-text-fill: #333;");
        peakHoursBox.setStyle("-fx-text-fill: #333;");
        rightBox.setStyle("-fx-text-fill: #333;");
        Button downloadButton = createButton("Download Data", event -> {
            try {
                downloadData();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        HBox bottomBox = new HBox();
        bottomBox.setSpacing(10);
        backButton.setPrefWidth(225);
        bottomBox.getChildren().addAll(backButton, downloadButton);
        layout.setBottom(bottomBox);

// Apply a subtle shadow effect to section

        return new Scene(layout, SCREEN_RES_WIDTH, SCREEN_RES_HEIGHT);
    }
    public Scene createScene(boolean toggleManager) {

        dateRangeSelector = createDateRangeSelector();

        startDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> updateScene());
        endDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> updateScene());

        mockData = new MockData();

        mockData.addMenuData();
        mockData.createMenu();
        mockData.generateBills();
        Map<Integer, List<List<String>>> billData = mockData.bill;
        layout = new BorderPane();
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


        // Get selected date range

        // Left section: Popular Menu Items
        VBox popularDishesBox = new VBox(5);
        popularDishesBox.getChildren().addAll(
                dateRangeSelector
        );

        // Sort the menu items by sales count in descending order

        // Display only the top 15 popular menu items


        ScrollPane popularDishesScrollPane = new ScrollPane(popularDishesBox);
        popularDishesScrollPane.setFitToWidth(true);
        layout.setLeft(popularDishesScrollPane);

        // Center section: Less Popular Menu Items, Most Popular Tables, and Peak Hours
        VBox centerBox = new VBox(10);
        centerBox.setAlignment(Pos.CENTER);

        // Less Popular Menu Items
        VBox lessPopularDishesBox = new VBox(5);
        lessPopularDishesBox.getChildren().addAll(
                dateRangeSelector
        );
        Pair<LocalDate, LocalDate> selectedDateRange = getSelectedDateRange();
        LocalDate startDate = selectedDateRange.getKey();
        LocalDate endDate = selectedDateRange.getValue();

        // Generate statistics based on selected date range
        Map<String, Integer> menuItemSales = calculateMenuItemSales(billData, startDate, endDate);
        double averageAmountSpent = calculateAverageAmountSpent(billData, startDate, endDate);
        String mostValuableWaiter = calculateMostValuableWaiter(billData, startDate, endDate);
        Map<String, Integer> tableSales = calculateTableSales(billData, startDate, endDate);
        List<String> lessPopularItems = calculateLessPopularItems(menuItemSales, startDate, endDate);
        List<String> peakHours = calculatePeakHours(billData, startDate, endDate);

        List<Map.Entry<String, Integer>> sortedMenuItems = new ArrayList<>(menuItemSales.entrySet());
        sortedMenuItems.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        int itemCount = 0;
        for (Map.Entry<String, Integer> entry : sortedMenuItems) {
            if (itemCount >= 15) {
                break;
            }
            popularDishesBox.getChildren().add(new Label(entry.getKey() + ": " + entry.getValue() + " sold"));
            itemCount++;
        }

        lessPopularItems.forEach(item -> lessPopularDishesBox.getChildren().add(new Label(item)));
        ScrollPane lessPopularDishesScrollPane = new ScrollPane(lessPopularDishesBox);
        lessPopularDishesScrollPane.setFitToWidth(true);
        lessPopularDishesScrollPane.setPrefViewportHeight(150);
        centerBox.getChildren().add(lessPopularDishesScrollPane);

        // Most Popular Tables
        VBox popularTablesBox = new VBox(5);
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
        peakHours.forEach(hour -> peakHoursBox.getChildren().add(new Label(hour)));
        ScrollPane peakHoursScrollPane = new ScrollPane(peakHoursBox);
        peakHoursScrollPane.setFitToWidth(true);
        peakHoursScrollPane.setPrefViewportHeight(150);
        centerBox.getChildren().add(peakHoursScrollPane);

        layout.setCenter(centerBox);

        // Right section: Average bill, total revenue, and most valuable waiter
        VBox rightBox = new VBox(10);

        // Format average amount spent and total revenue to display only 2 decimals
        DecimalFormat df = new DecimalFormat("#.##");
        String formattedAverageAmountSpent = df.format(averageAmountSpent);
        double totalRevenue = calculateTotalRevenue(billData, startDate, endDate);
        String formattedTotalRevenue = df.format(totalRevenue);
        layout.setRight(rightBox);
        rightBox.setAlignment(Pos.CENTER);


        layout.setTop(greetingLabel);
        layout.setBottom(backButton);
        popularDishesBox.setStyle("-fx-background-color: #f9f9f9;");
        lessPopularDishesBox.setStyle("-fx-background-color: #f9f9f9;");
        popularTablesBox.setStyle("-fx-background-color: #f9f9f9;");
        peakHoursBox.setStyle("-fx-background-color: #f9f9f9;");
        rightBox.setStyle("-fx-background-color: #f9f9f9;");
        // Add bold and increase font size for headings

        Label popularDishesHeading = new Label("Popular Menu Items:");
        popularDishesHeading.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label lessPopularDishesHeading = new Label("Less Popular Menu Items:");
        lessPopularDishesHeading.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label popularTablesHeading = new Label("Most Popular Tables:");
        popularTablesHeading.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label peakHoursHeading = new Label("Peak Hours:");
        peakHoursHeading.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

// Add headings to the corresponding boxes
        popularDishesBox.getChildren().add(0, popularDishesHeading);
        lessPopularDishesBox.getChildren().add(0, lessPopularDishesHeading);
        popularTablesBox.getChildren().add(0, popularTablesHeading);
        peakHoursBox.getChildren().add(0, peakHoursHeading);

        Label revenueLabel = new Label("Total Revenue: £" + formattedTotalRevenue);
        revenueLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Label averageAmountSpentLabel = new Label("Average Amount Spent: $" + formattedAverageAmountSpent);
        averageAmountSpentLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Label mostValuableWaiterLabel = new Label("Most Valuable Waiter: " + mostValuableWaiter);
        mostValuableWaiterLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

// Add labels to the rightBox
        rightBox.setAlignment(Pos.BASELINE_LEFT);

// Add labels to the rightBox
        rightBox.getChildren().addAll(
                dateRangeSelector,
                averageAmountSpentLabel,
                revenueLabel,
                mostValuableWaiterLabel
        );
// Add padding to sections
        popularDishesBox.setPadding(new Insets(10));
        lessPopularDishesBox.setPadding(new Insets(10));
        popularTablesBox.setPadding(new Insets(10));
        peakHoursBox.setPadding(new Insets(10));
        rightBox.setPadding(new Insets(10));

// Apply border to sections
        popularDishesBox.setBorder(new Border(new BorderStroke(Color.LIGHTGRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        lessPopularDishesBox.setBorder(new Border(new BorderStroke(Color.LIGHTGRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        popularTablesBox.setBorder(new Border(new BorderStroke(Color.LIGHTGRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        peakHoursBox.setBorder(new Border(new BorderStroke(Color.LIGHTGRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        rightBox.setBorder(new Border(new BorderStroke(Color.LIGHTGRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

// Add spacing between elements
        popularDishesBox.setSpacing(10);
        lessPopularDishesBox.setSpacing(10);
        popularTablesBox.setSpacing(10);
        peakHoursBox.setSpacing(10);
        rightBox.setSpacing(10);

// Set text color
        popularDishesBox.setStyle("-fx-text-fill: #333;");
        lessPopularDishesBox.setStyle("-fx-text-fill: #333;");
        popularTablesBox.setStyle("-fx-text-fill: #333;");
        peakHoursBox.setStyle("-fx-text-fill: #333;");
        rightBox.setStyle("-fx-text-fill: #333;");
        Button downloadButton = createButton("Download Data", event -> {
            try {
                downloadData();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        HBox bottomBox = new HBox();
        bottomBox.setSpacing(10);
        backButton.setPrefWidth(225);
        bottomBox.getChildren().addAll(backButton, downloadButton);
        layout.setBottom(bottomBox);

// Apply a subtle shadow effect to section

        return new Scene(layout, SCREEN_RES_WIDTH, SCREEN_RES_HEIGHT);
    }

    private void downloadData() {
        // Create a FileChooser dialog
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Data");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

        // Show the dialog and get the selected file
        File selectedFile = fileChooser.showSaveDialog(layout.getScene().getWindow());

        if (selectedFile != null) {
            try {
                // Create a BufferedWriter to write data to the selected file
                BufferedWriter writer = new BufferedWriter(new FileWriter(selectedFile));

                // Get the selected date range
                LocalDate startDate = startDatePicker.getValue();
                LocalDate endDate = endDatePicker.getValue();

                // Write the selected date range to the file
                writer.write("Date Range: " + startDate.toString() + " to " + endDate.toString() + "\n\n");

                // Gather data from the scene
                String averageAmountSpent = ((Label) ((VBox) layout.getRight()).getChildren().get(1)).getText();
                String totalRevenue = ((Label) ((VBox) layout.getRight()).getChildren().get(2)).getText();
                String mostValuableWaiter = ((Label) ((VBox) layout.getRight()).getChildren().get(3)).getText();

                // Write the gathered data to the file
                writer.write("Average Amount Spent: " + averageAmountSpent + "\n");
                writer.write("Total Revenue: " + totalRevenue + "\n");
                writer.write("Most Valuable Waiter: " + mostValuableWaiter + "\n\n");

                // Get data from popular menu items
                writer.write("Popular Menu Items:\n");
                VBox popularDishesBox = (VBox) ((ScrollPane) layout.getLeft()).getContent();
                for (Node node : popularDishesBox.getChildren()) {
                    if (node instanceof Label) {
                        writer.write(((Label) node).getText() + "\n");
                    }
                }
                writer.write("\n");

                // Get data from less popular menu items
                writer.write("Less Popular Menu Items:\n");
                VBox lessPopularDishesBox = (VBox) ((ScrollPane) ((VBox) layout.getCenter()).getChildren().get(0)).getContent();
                for (Node node : lessPopularDishesBox.getChildren()) {
                    if (node instanceof Label && !((Label) node).getText().startsWith("Less Popular Menu Items:")) {
                        writer.write(((Label) node).getText() + "\n");
                    }
                }
                writer.write("\n");

                // Get data from popular tables
                writer.write("Popular Tables:\n");
                VBox popularTablesBox = (VBox) ((ScrollPane) ((VBox) layout.getCenter()).getChildren().get(1)).getContent();
                for (Node node : popularTablesBox.getChildren()) {
                    if (node instanceof Label && !((Label) node).getText().startsWith("Most Popular Tables:")) {
                        writer.write(((Label) node).getText() + "\n");
                    }
                }
                writer.write("\n");

                // Get data from peak hours
                writer.write("Peak Hours:\n");
                VBox peakHoursBox = (VBox) ((ScrollPane) ((VBox) layout.getCenter()).getChildren().get(2)).getContent();
                for (Node node : peakHoursBox.getChildren()) {
                    if (node instanceof Label && !((Label) node).getText().startsWith("Most Popular Tables:")) {
                        writer.write(((Label) node).getText() + "\n");
                    }
                }

                // Close the writer
                writer.close();

                // Show success message
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Download");
                alert.setHeaderText(null);
                alert.setContentText("Data downloaded successfully.");
                alert.showAndWait();
            } catch (Exception e) {
                // Show error message if failed to write data
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Failed to download data.");
                alert.showAndWait();
                e.printStackTrace();
            }
        }
    }

    private double calculateAverageAmountSpent(Map<Integer, List<List<String>>> billData, LocalDate startDate, LocalDate endDate) {
        double totalAmount = 0;
        int billCount = 0;
        for (List<List<String>> bill : billData.values()) {
            LocalDate billDate = LocalDate.parse(bill.get(1).get(6)); // Parse bill date
            if (billDate.isAfter(startDate) && billDate.isBefore(endDate)) {
                totalAmount += Double.parseDouble(bill.get(1).get(2)); // Total bill amount
                billCount++;
            }
        }
        return billCount > 0 ? totalAmount / billCount : 0;
    }

    private Map<String, Integer> calculateTableSales(Map<Integer, List<List<String>>> billData, LocalDate startDate, LocalDate endDate) {
        Map<String, Integer> tableSales = new HashMap<>();
        for (List<List<String>> bill : billData.values()) {
            LocalDate billDate = LocalDate.parse(bill.get(1).get(6)); // Parse bill date
            if (billDate.isAfter(startDate) && billDate.isBefore(endDate)) {
                String tableNumber = bill.get(1).get(5); // Table number from bill info
                tableSales.put(tableNumber, tableSales.getOrDefault(tableNumber, 0) + 1);
            }
        }
        return tableSales;
    }

    private String calculateMostValuableWaiter(Map<Integer, List<List<String>>> billData, LocalDate startDate, LocalDate endDate) {
        Map<String, Double> waiterSales = new HashMap<>();
        for (List<List<String>> bill : billData.values()) {
            LocalDate billDate = LocalDate.parse(bill.get(1).get(6)); // Parse bill date
            if (billDate.isAfter(startDate) && billDate.isBefore(endDate)) {
                String waiterName = bill.get(1).get(1); // Waiter name from bill info
                double totalBillAmount = Double.parseDouble(bill.get(1).get(2)); // Total bill amount
                waiterSales.put(waiterName, waiterSales.getOrDefault(waiterName, 0.0) + totalBillAmount);
            }
        }
        return Collections.max(waiterSales.entrySet(), Comparator.comparingDouble(Map.Entry::getValue)).getKey();
    }

    private List<String> calculateLessPopularItems(Map<String, Integer> menuItemSales, LocalDate startDate, LocalDate endDate) {
        List<String> lessPopularItems = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : menuItemSales.entrySet()) {
            if (entry.getValue() <= 50) { // Define less popular based on a threshold (e.g., 3)
                lessPopularItems.add(entry.getKey());
            }
        }
        return lessPopularItems;
    }

    private List<String> calculatePeakHours(Map<Integer, List<List<String>>> billData, LocalDate startDate, LocalDate endDate) {
        Map<String, Integer> timeslotCount = new HashMap<>();
        for (List<List<String>> bill : billData.values()) {
            LocalDate billDate = LocalDate.parse(bill.get(1).get(6)); // Parse bill date
            if (billDate.isAfter(startDate) && billDate.isBefore(endDate)) {
                String timeslot = bill.get(1).get(0); // Timeslot from bill info
                timeslotCount.put(timeslot, timeslotCount.getOrDefault(timeslot, 0) + 1);
            }
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

    private HBox createDateRangeSelector() {
        HBox dateRangeSelector = new HBox(10);

        // Set default values to select the whole year
        startDatePicker.setValue(LocalDate.of(LocalDate.now().getYear(), 1, 1));
        endDatePicker.setValue(LocalDate.of(LocalDate.now().getYear(), 12, 31));


        // Set prompt text
        startDatePicker.setPromptText("Start Date");
        endDatePicker.setPromptText("End Date");
        startDatePicker.setStyle("-fx-font-size: 12px; -fx-background-color: #f0f0f0; -fx-border-color: #999999;-fx-background-radius: 20");
        endDatePicker.setStyle("-fx-font-size: 12px; -fx-background-color: #f0f0f0; -fx-border-color: #999999;-fx-background-radius: 20");

        // Add the pickers to the layout
        dateRangeSelector.getChildren().addAll(startDatePicker, endDatePicker);
        HBox.setHgrow(startDatePicker, Priority.ALWAYS);
        HBox.setHgrow(endDatePicker, Priority.ALWAYS);

        return dateRangeSelector;
    }

    private Pair<LocalDate, LocalDate> getSelectedDateRange() {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        return new Pair<>(startDate, endDate);
    }
}
