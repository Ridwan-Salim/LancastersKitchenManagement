package scenes;

import core.SceneManager;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;
import javafx.geometry.Pos;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

public class CreateSendOrder extends Manager {

    private ObservableList<String> orderItems = FXCollections.observableArrayList();
    private ObservableList<String> lowItems = FXCollections.observableArrayList();
    private ObservableList<String> regularItems = FXCollections.observableArrayList();

    private Map<String, Integer> orderMap = new HashMap<>();
    private Map<String, Integer> lowItemMap = new HashMap<>();
    private Map<String, Integer> regularItemMap = new HashMap<>();
    private MockData mockData;

    public Scene createScene() {
        mockData = new MockData();
        mockData.addLowStockItems();
        mockData.addIngredients();
        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(20));

        // Labels
        Label greetingLabel = new Label("Let's create/send some orders, " + employeeName + ".");
        greetingLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        BorderPane.setAlignment(greetingLabel, Pos.TOP_LEFT);
        BorderPane.setMargin(greetingLabel, new Insets(10, 0, 0, 10));
        Timeline labelAnimation = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(greetingLabel.opacityProperty(), 0)),
                new KeyFrame(Duration.seconds(1), new KeyValue(greetingLabel.opacityProperty(), 1))
        );
        labelAnimation.play();




        // Lists
        ListView<String> orderListView = new ListView<>(orderItems);
        ContextMenu deleteMenu = new ContextMenu();

// Delete single item
        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(event -> {
            String selectedItem = orderListView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                String[] parts = selectedItem.split(" x");
                if (parts.length == 2) {
                    String itemName = parts[0].trim();
                    int quantity = Integer.parseInt(parts[1].trim());
                    if (quantity > 1) {
                        orderMap.put(itemName, orderMap.get(itemName) - 1);
                    } else {
                        orderMap.remove(itemName);
                    }
                    updateOrderItems();
                }
            }
        });

// Delete all items
        MenuItem deleteAllItem = new MenuItem("Delete All");
        deleteAllItem.setOnAction(event -> {
            String selectedItem = orderListView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                String[] parts = selectedItem.split(" x");
                if (parts.length == 2) {
                    String itemName = parts[0].trim();
                    orderMap.remove(itemName);
                    updateOrderItems();
                }
            }
        });

        deleteMenu.getItems().addAll(deleteItem, deleteAllItem);

        orderListView.setContextMenu(deleteMenu);
        ListView<String> lowItemsListView = new ListView<>(lowItems);


        ListView<String> regularItemsListView = new ListView<>(regularItems);
        ContextMenu deleteMenu1 = new ContextMenu();

        // Delete single item
        MenuItem deleteItem1 = new MenuItem("Delete");
        deleteItem1.setOnAction(event -> {
            String selectedItem = regularItemsListView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                String[] parts = selectedItem.split(" x");
                if (parts.length == 2) {
                    String itemName = parts[0].trim();
                    int quantity = Integer.parseInt(parts[1].trim());
                    if (quantity > 1) {
                        regularItemMap.put(itemName, regularItemMap.get(itemName) - 1);
                    } else {
                        regularItemMap.remove(itemName);
                    }
                    updateRegularItems();
                }
            }
        });

// Delete all items
        MenuItem deleteAllItem1 = new MenuItem("Delete All");
        deleteAllItem1.setOnAction(event -> {
            String selectedItem = regularItemsListView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                String[] parts = selectedItem.split(" x");
                if (parts.length == 2) {
                    String itemName = parts[0].trim();
                    regularItemMap.remove(itemName);
                    updateRegularItems();
                }
            }
        });

        deleteMenu1.getItems().addAll(deleteItem1, deleteAllItem1);
        regularItemsListView.setContextMenu(deleteMenu1);

        // Scroll panes for lists
        ScrollPane orderScrollPane = new ScrollPane(orderListView);
        ScrollPane lowItemsScrollPane = new ScrollPane(lowItemsListView);
        ScrollPane regularItemsScrollPane = new ScrollPane(regularItemsListView);

        orderScrollPane.setPrefWidth(225);
        orderScrollPane.fitToHeightProperty();
        orderScrollPane.setPrefHeight(350); // Set preferred height for orderScrollPane

        lowItemsScrollPane.setPrefWidth(225);
        lowItemsScrollPane.setPrefHeight(350); // Set preferred height for lowItemsScrollPane

        regularItemsScrollPane.setPrefWidth(225);
        regularItemsScrollPane.setPrefHeight(350); // Set preferred height for regularItemsScrollPane



// TextFields
        TextField orderItemTextField = new TextField();
        TextField normalAmountTextField = new TextField();

        normalAmountTextField.setPromptText("Enter Normal Amount");

// Event handlers

        regularItemsListView.setEditable(true);
        regularItemsListView.setOnEditCommit(event -> {
            int index = event.getIndex();
            String newValue = event.getNewValue();
            if (index >= 0 && index < regularItems.size()) {
                regularItems.set(index, newValue);
                String[] parts = newValue.split(" x");
                if (parts.length == 2) {
                    String itemName = parts[0].trim();
                    int amount = Integer.parseInt(parts[1].trim());
                    regularItemMap.put(itemName, amount);
                }
            }
        });

// Layout setup

        Label totalPriceLabel = new Label("Total Price: £0.00");
        totalPriceLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");


        Button addOrderItemButton = createButton("+ Add Item", e -> {
            String newItemInput = orderItemTextField.getText().trim();
            if (!newItemInput.isEmpty()) {
                String[] parts = newItemInput.split("-");
                if (parts.length == 2) {
                    String itemName = parts[0].trim();
                    try {
                        int quantity = Integer.parseInt(parts[1].trim());
                        if (quantity > 0) {
                            orderMap.put(itemName, orderMap.getOrDefault(itemName, 0) + quantity);
                            updateOrderItems();
                            orderItemTextField.clear();
                        } else {
                            // Handle invalid quantity
                            System.out.println("Please enter a valid positive quantity.");
                        }
                    } catch (NumberFormatException ex) {
                        // Handle invalid quantity format
                        System.out.println("Please enter a valid quantity in the format 'item-quantity'.");
                    }
                } else {
                    // Handle invalid input format
                    System.out.println("Please enter the item and quantity in the format 'item-quantity'.");
                }
            }
        });
        addOrderItemButton.setPrefWidth(225);
        Button getLowItemsButton = createButton("Get Low Items", e -> {
            // Retrieve low items from mock data
            lowItemMap.clear();
            lowItemMap.putAll(mockData.lowStockItems);
            updateLowItems();
        });
        getLowItemsButton.setPrefWidth(225);
        Button getRegularItemsButton = createButton("Get Norms", e -> {
            // Retrieve regular items from mock data
            regularItemMap.clear();
            regularItemMap.putAll(mockData.usualStockQuantities);
            updateRegularItems();
        });
        getRegularItemsButton.setPrefWidth(225);
        Button applyNormalAmountButton = createButton("Apply Norm", e -> {
            if (normalAmountTextField.getText().split("-").length > 1)
                mockData.applyNormalAmount(normalAmountTextField.getText().split("-")[0], Integer.parseInt(normalAmountTextField.getText().split("-")[1]));
            else
                mockData.applyNormalAmount(Integer.parseInt(normalAmountTextField.getText()));
        });
        applyNormalAmountButton.setPrefWidth(225);
        Button addDifferenceToOrderButton = createButton("Add Difference to Order", e -> {
            // Calculate difference between regular and low amounts
            for (String item : regularItemMap.keySet()) {
                int regularAmount = regularItemMap.getOrDefault(item, 0);
                int lowAmount = lowItemMap.getOrDefault(item, 0);
                int difference = regularAmount - lowAmount;
                if (difference > 0) {
                    // Add the difference to the order
                    orderMap.put(item, orderMap.getOrDefault(item, 0) + difference);
                }
            }
            // Update the order items list
            updateOrderItems();
        });
        addDifferenceToOrderButton.setPrefWidth(275);
        Button calculatePriceButton = createButton("Calculate Price", e -> {
            // Calculate total price of items in the order
            double totalPrice = 0;
            for (String item : orderMap.keySet()) {
                int quantity = orderMap.get(item);
                double pricePerItem = mockData.getItemPrice(item); // Assuming there's a method to get the price per item
                totalPrice += quantity * pricePerItem;
            }
            totalPriceLabel.setText("Total Price: £" + String.format("%.2f", totalPrice)); // Update the total price label
        });
        calculatePriceButton.setPrefWidth(225);
        Button sendOrderButton = createButton("Send Order", e -> {
            // Create a JSON representation of the order and send it
            String jsonOrder = createJsonOrder(); // Create a method to generate JSON representation of the order
            System.out.println("Sending Order: " + jsonOrder); // Display or send the JSON order (you can change this to whatever you need)
        });
        sendOrderButton.setPrefWidth(275);
        Button downloadButton = createButton("Download", e -> {
            // Download the content of the screen as a file
            // You need to implement the logic to save the content of the screen to a file
            // This can include saving the order list, low items, regular items, etc.
            // For simplicity, let's assume you have a method called saveContentToFile(String content, String filename)
            String content = "Order Items: " + orderItems + "\nLow Items: " + lowItems + "\nRegular Items: " + regularItems;
            saveContentToFile(content);
        });
        downloadButton.setPrefWidth(275);

        totalPriceLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label orderLabel =  new Label("Order Items");
        orderLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        VBox orderVBox = new VBox(10, orderLabel, orderScrollPane, orderItemTextField);
        orderVBox.setAlignment(Pos.CENTER);
        orderVBox.setPadding(new Insets(10));
        orderVBox.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #cccccc; -fx-border-radius: 5px;");

        Label lowItemsLabel = new Label("Low Items");
        lowItemsLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        VBox lowItemsVBox = new VBox(10, lowItemsLabel, lowItemsScrollPane);
        lowItemsVBox.setAlignment(Pos.BASELINE_CENTER);
        lowItemsVBox.setPadding(new Insets(10));
        lowItemsVBox.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #cccccc; -fx-border-radius: 5px;");

        Label regularItems = new Label("Regular Items");
        regularItems.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        VBox regularItemsVBox = new VBox(10, regularItems, regularItemsScrollPane, normalAmountTextField);
        regularItemsVBox.setAlignment(Pos.CENTER);
        regularItemsVBox.setPadding(new Insets(10));
        regularItemsVBox.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #cccccc; -fx-border-radius: 5px;");
// Layout setup
        orderVBox.getChildren().addAll(addOrderItemButton, calculatePriceButton);
        lowItemsVBox.getChildren().addAll(getLowItemsButton);
        regularItemsVBox.getChildren().addAll(getRegularItemsButton, applyNormalAmountButton);
        orderVBox.setPadding(new Insets(10));
        lowItemsVBox.setPadding(new Insets(10));
        regularItemsVBox.setPadding(new Insets(10));

        HBox listsHBox = new HBox(20, orderVBox, lowItemsVBox, regularItemsVBox);
        VBox leftPart = new VBox();
        leftPart.getChildren().addAll(greetingLabel, listsHBox);
        layout.setLeft(leftPart);
        VBox rightBox = new VBox();
        rightBox.setSpacing(30);
        Button backButton = createBackButtonManager();
        backButton.setPrefWidth(275);
        rightBox.setPadding(new Insets(150, 0, 0,0));
        rightBox.getChildren().addAll(backButton,  addDifferenceToOrderButton,sendOrderButton,downloadButton,  totalPriceLabel);
        layout.setRight(rightBox);

        return new Scene(layout, SCREEN_RES_WIDTH, SCREEN_RES_HEIGHT);
    }

    private String createJsonOrder() {
        JSONObject jsonOrder = new JSONObject();
        JSONArray itemsArray = new JSONArray();

        // Add each item from the order map to the JSON array
        for (Map.Entry<String, Integer> entry : orderMap.entrySet()) {
            JSONObject itemObject = new JSONObject();
            itemObject.put("name", entry.getKey());
            itemObject.put("quantity", entry.getValue());
            itemsArray.add(itemObject);
        }

        // Add the items array to the main JSON object
        jsonOrder.put("orderItems", itemsArray);

        // Specify the file path where you want to save the JSON file
        String filePath = "order.json";

        // Write the JSON data to the file
        try (FileWriter file = new FileWriter(filePath)) {
            file.write(jsonOrder.toJSONString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return filePath;
    }

    private void saveContentToFile(String content) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Order");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        File file = fileChooser.showSaveDialog(new Stage());
        if (file != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(content);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void updateOrderItems() {
        orderItems.clear();
        for (Map.Entry<String, Integer> entry : orderMap.entrySet()) {
            orderItems.add(entry.getKey() + " x" + entry.getValue());
        }
    }

    private void updateLowItems() {
        lowItems.clear();
        for (Map.Entry<String, Integer> entry : lowItemMap.entrySet()) {
            lowItems.add(entry.getKey() + " x" + entry.getValue());
        }
    }

    private void updateRegularItems() {
        regularItems.clear();
        for (Map.Entry<String, Integer> entry : regularItemMap.entrySet()) {
            regularItems.add(entry.getKey() + " x" + entry.getValue());
        }
    }
}
