package scenes;

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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;

public class CreateSendOrderScene extends ManagerScene {

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
        ListView<String> lowItemsListView = new ListView<>(lowItems);
        ListView<String> regularItemsListView = new ListView<>(regularItems);

        // Scroll panes for lists
        ScrollPane orderScrollPane = new ScrollPane(orderListView);
        ScrollPane lowItemsScrollPane = new ScrollPane(lowItemsListView);
        ScrollPane regularItemsScrollPane = new ScrollPane(regularItemsListView);

        orderScrollPane.setPrefWidth(225);
        orderScrollPane.setPrefHeight(350); // Set preferred height for orderScrollPane

        lowItemsScrollPane.setPrefWidth(225);
        lowItemsScrollPane.setPrefHeight(350); // Set preferred height for lowItemsScrollPane

        regularItemsScrollPane.setPrefWidth(225);
        regularItemsScrollPane.setPrefHeight(350); // Set preferred height for regularItemsScrollPane

        Button addOrderItemButton = new Button("+ Add Item");
        Button getLowItemsButton = new Button("Get Low Items");
        Button getRegularItemsButton = new Button("Get Regular Items");
        Button applyNormalAmountButton = new Button("Apply Normal Amount");

// TextFields
        TextField orderItemTextField = new TextField();
        TextField normalAmountTextField = new TextField();

        normalAmountTextField.setPromptText("Enter Normal Amount");

// Event handlers
        addOrderItemButton.setOnAction(e -> {
            String newItemName = orderItemTextField.getText();
            if (!newItemName.isEmpty()) {
                if (orderMap.containsKey(newItemName)) {
                    orderMap.put(newItemName, orderMap.get(newItemName) + 1);
                } else {
                    orderMap.put(newItemName, 1);
                }
                updateOrderItems();
                orderItemTextField.clear();
            }
        });

        getLowItemsButton.setOnAction(e -> {
            // Retrieve low items from mock data
            lowItemMap.clear();
            lowItemMap.putAll(mockData.lowStockItems);
            updateLowItems();
        });

        getRegularItemsButton.setOnAction(e -> {
            // Retrieve regular items from mock data
            regularItemMap.clear();
            regularItemMap.putAll(mockData.usualStockQuantities);
            updateRegularItems();
        });

        normalAmountTextField.setOnAction(e -> {
            if (normalAmountTextField.getText().split("-").length > 1)
                mockData.applyNormalAmount(normalAmountTextField.getText().split("-")[0], Integer.parseInt(normalAmountTextField.getText().split("-")[1]));
            else
                mockData.applyNormalAmount(Integer.parseInt(normalAmountTextField.getText()));
        });

        applyNormalAmountButton.setOnAction(e -> {
            if (normalAmountTextField.getText().split("-").length > 1)
                mockData.applyNormalAmount(normalAmountTextField.getText().split("-")[0], Integer.parseInt(normalAmountTextField.getText().split("-")[1]));
            else
                mockData.applyNormalAmount(Integer.parseInt(normalAmountTextField.getText()));
        });

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
        VBox orderVBox = new VBox(10, new Label("Order Items"), orderScrollPane, orderItemTextField, addOrderItemButton);
        VBox lowItemsVBox = new VBox(10, new Label("Low Items"), lowItemsScrollPane, getLowItemsButton);
        VBox regularItemsVBox = new VBox(10, new Label("Regular Items"), regularItemsScrollPane, getRegularItemsButton, normalAmountTextField, applyNormalAmountButton);
        orderVBox.setPadding(new Insets(10));
        lowItemsVBox.setPadding(new Insets(10));
        regularItemsVBox.setPadding(new Insets(10));

        HBox listsHBox = new HBox(20, orderVBox, lowItemsVBox, regularItemsVBox);
        VBox leftPart = new VBox();
        leftPart.getChildren().addAll(greetingLabel, listsHBox);
        layout.setLeft(leftPart);
        layout.setRight(createBackButtonManager());

        return new Scene(layout, SCREEN_RES_WIDTH, SCREEN_RES_HEIGHT);
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
