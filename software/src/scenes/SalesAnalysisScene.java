package scenes;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SalesAnalysisScene  extends ManagerScene{
    private Map<String, Integer> menuItemsSold;
    private double totalRevenue;
    private Map<Integer, Integer> tableSales;
    private Map<String, Double> waiterSales;
    public Scene createScene() {
        menuItemsSold = new HashMap<>();
        totalRevenue = 0.0;
        tableSales = new HashMap<>();
        waiterSales = new HashMap<>();
        MockData mockData = new MockData();

        mockData.addMenuData();
        mockData.createMenu();
        mockData.generateBills();
        // Process bill data
        for (Map.Entry<Integer, List<List<String>>> entry : mockData.bill.entrySet()) {
            List<List<String>> billInfo = entry.getValue();
            for (String item : billInfo.get(0)) {
                // Count menu item sales
                menuItemsSold.put(item, menuItemsSold.getOrDefault(item, 0) + 1);
            }
            // Calculate total revenue
            totalRevenue += Double.parseDouble(billInfo.get(1).get(2));
            // Count table sales
            int tableNumber = Integer.parseInt(billInfo.get(1).get(5));
            tableSales.put(tableNumber, tableSales.getOrDefault(tableNumber, 0) + 1);
            // Count waiter sales
            String waiterName = billInfo.get(1).get(1);
            waiterSales.put(waiterName, waiterSales.getOrDefault(waiterName, 0.0) +
                    Double.parseDouble(billInfo.get(1).get(4)));
        }

        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(20));

        // Display statistics
        VBox statisticsBox = new VBox(10);
        statisticsBox.setPadding(new Insets(10));
        statisticsBox.setAlignment(Pos.CENTER);

        Label popularItemsLabel = new Label("Popular Menu Items:");
        StringBuilder popularItemsText = new StringBuilder();
        for (Map.Entry<String, Integer> entry : menuItemsSold.entrySet()) {
            popularItemsText.append(entry.getKey()).append(": ").append(entry.getValue()).append(" sold\n");
        }
        Label popularItems = new Label(popularItemsText.toString());

        Label revenueLabel = new Label("Total Revenue: $" + totalRevenue);

        Label popularTablesLabel = new Label("Most Popular Tables:");
        StringBuilder popularTablesText = new StringBuilder();
        tableSales.entrySet().stream()
                .sorted((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue())) // Sort by sales
                .limit(3) // Display top 3 tables
                .forEach(entry -> popularTablesText.append("Table ").append(entry.getKey())
                        .append(": ").append(entry.getValue()).append(" sales\n"));
        Label popularTables = new Label(popularTablesText.toString());

        Label valuableWaiterLabel = new Label("Most Valuable Waiter:");
        String valuableWaiter = waiterSales.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("No data available");
        Label valuableWaiter1 = new Label(valuableWaiter);

        statisticsBox.getChildren().addAll(popularItemsLabel, popularItems, revenueLabel,
                popularTablesLabel, popularTables, valuableWaiterLabel, valuableWaiter1);

        // Animation
        Timeline statisticsAnimation = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(statisticsBox.opacityProperty(), 0)),
                new KeyFrame(Duration.seconds(1), new KeyValue(statisticsBox.opacityProperty(), 1))
        );
        statisticsAnimation.play();

        Button backButton = createBackButtonManager();
        layout.setCenter(statisticsBox);
        layout.setBottom(backButton);
        return new Scene(layout, SCREEN_RES_WIDTH, SCREEN_RES_HEIGHT);
    }
}
