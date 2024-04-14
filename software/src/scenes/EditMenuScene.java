package scenes;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Duration;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import java.util.*;


public class EditMenuScene  extends ManagerScene{
    private MockData mockData = new MockData();
    private Map<Integer, Label> dishPriceLabel = new HashMap<>();
    private Map<Integer, Label> originalDishPriceLabel = new HashMap<>();
    private Map<Integer, String> dishDescriptions = new HashMap<>();
    private Map<Integer, String> dishAllergens = new HashMap<>();
    private boolean isDataInitialized = false;
    private double defaultMarkupPercentage = 5; // Default markup percentage
    private final double oldDefaultMarkupPercentage = 5; // Default markup percentage
    private double additionalMarkupPercentage = 0;
    private static int counter = 0;
    public void initializeDataIfNeeded() {
        if (!isDataInitialized) {
            mockData.addMenuData();
            mockData.createMenu();
            mockData.addWines();
            isDataInitialized = true;
        }
    }

    public Scene createScene() {
        initializeDataIfNeeded();

        // Setting up layouts
        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(20));

        HBox menuEditorContainer = new HBox();
        HBox menuRightContainer = new HBox();
        HBox mainPane = new HBox();
        VBox markupContainer = new VBox();

        ScrollPane scrollPane = new ScrollPane();
        VBox menuItemsContainer = new VBox();
        menuItemsContainer.setSpacing(25);

        Label greetingLabel = new Label("Let`s edit the menu, " + employeeName + ".");
        greetingLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        BorderPane.setAlignment(greetingLabel, Pos.TOP_LEFT);
        BorderPane.setMargin(greetingLabel, new Insets(10, 0, 0, 10));
        Timeline labelAnimation = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(greetingLabel.opacityProperty(), 0)),
                new KeyFrame(Duration.seconds(1), new KeyValue(greetingLabel.opacityProperty(), 1))
        );
        labelAnimation.play();

        // Most of the UI generation for the ScrollPane happens in here
        for (Map.Entry<Integer, String[]> entry : mockData.menu.entrySet()) {
            VBox dishItemsContainer = new VBox();
            GridPane dishContainer = new GridPane();
            GridPane descAllergenContainer = new GridPane();
            Integer id = entry.getKey();
            Label dishName = new Label(entry.getValue()[0]);
            dishName.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
            GridPane.setMargin(dishName, new Insets(10, 0, 0, 20)); // TRBL

            Label dishPrice = new Label(entry.getValue()[1]);
            String priceString = entry.getValue()[1].trim().replace("£", "");
            double price = (Double.parseDouble(priceString)); // Add default markup /10 as not whole ingredient used
            dishPrice.setText("£" + String.format("%.2f", price));
            dishPrice.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
            GridPane.setMargin(dishPrice, new Insets(10, 0, 0, 20));
            dishPriceLabel.put(id, dishPrice); // Labels are saved so that they can be accessed and used later
            if (counter < mockData.menu.size()) {
                Label originalCopy = new Label(dishPrice.getText());
                originalDishPriceLabel.put(id, originalCopy);
                counter++;
            }
            dishContainer.add(dishName, 0, 0);
            dishContainer.add(dishPrice, 1, 0); // Grid system to align the text more absolutely

            ColumnConstraints col1 = new ColumnConstraints();
            col1.setPrefWidth(250);
            ColumnConstraints col2 = new ColumnConstraints();
            col2.setPrefWidth(250);
            dishContainer.getColumnConstraints().addAll(col1, col2);


            ColumnConstraints col3 = new ColumnConstraints();
            col3.setPrefWidth(255);
            ColumnConstraints col4 = new ColumnConstraints();
            col4.setPrefWidth(75);
            ColumnConstraints col5 = new ColumnConstraints();
            col5.setPrefWidth(75);
            descAllergenContainer.getColumnConstraints().addAll(col3, col4, col5);

            // HBox that holds info about Dish name and Price labels

            // HBox that holds info about Dish description and Allergen info
            descAllergenContainer.setStyle("-fx-background-color: #f0f0f0;");
            GridPane.setMargin(descAllergenContainer, new Insets(0, 0, 0, 0));
            descAllergenContainer.setMinSize(500, 75);
            descAllergenContainer.setMaxSize(500, 75);
            dishItemsContainer.getChildren().add(descAllergenContainer);

            Button addDescription = new Button("Add Description");
            GridPane.setMargin(addDescription, new Insets(10, 0, 0, 0));
            addDescription.setOnAction(event -> {
                TextArea descriptionTextArea = new TextArea();
                descriptionTextArea.setPromptText("Enter description");
                descriptionTextArea.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #999999;");
                descriptionTextArea.setMaxWidth(250);
                descriptionTextArea.setWrapText(true);
                descriptionTextArea.requestFocus();
                descAllergenContainer.getChildren().remove(addDescription);
                descAllergenContainer.add(descriptionTextArea, 0, 0);

                Button saveDescription = new Button("Save");
                saveDescription.setOnAction(saveEvent -> {
                    String description = descriptionTextArea.getText();
                    if(description.length() < 1){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Invalid Input");
                        alert.setHeaderText(null);
                        alert.setContentText("Please enter a description before saving");
                        alert.showAndWait();
                    } else{
                        dishDescriptions.put(id, description);
                        String[] saveData = new String[mockData.dishInfoCapacity];
                        String saveName = dishName.getText();
                        String savePrice = dishPriceLabel.get(id).getText();
                        String saveDesc = dishDescriptions.get(id);
                        String saveAllergens = dishAllergens.get(id);
                        saveData[0] = saveName;
                        saveData[1] = savePrice;
                        saveData[2]=  saveDesc;
                        saveData[3] = saveAllergens;
                        mockData.menu.put(id, saveData);
                        System.out.println(mockData.menu.get(id)[2]);
                        descAllergenContainer.getChildren().remove(descriptionTextArea);
                        descAllergenContainer.getChildren().remove(saveDescription);

                        descriptionTextArea.setText(description);
                        addDescription.setText("Edit Description");
                        descAllergenContainer.getChildren().add(addDescription);
                    }
                });
                saveDescription.setStyle("-fx-background-color:  #0077CC; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 16px; -fx-padding: 3 5;");
                saveDescription.setPrefWidth(150);
                saveDescription.setOnMouseEntered(e -> saveDescription.setStyle("-fx-background-color: #025692; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 16px; -fx-padding: 3 5;"));
                saveDescription.setOnMouseExited(e -> saveDescription.setStyle("-fx-background-color:  #0077CC; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 16px; -fx-padding: 3 5;"));
                saveDescription.setOnMouseClicked(e -> saveDescription.setStyle("-fx-background-color: #002540; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 16px; -fx-padding: 3 50;"));
                descAllergenContainer.add(saveDescription, 0, 1);

            });
            addDescription.setStyle("-fx-background-color:  #0077CC; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 16px; -fx-padding: 3 5;");
            addDescription.setPrefWidth(150);
            addDescription.setOnMouseEntered(e -> addDescription.setStyle("-fx-background-color: #025692; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 16px; -fx-padding: 3 5;"));
            addDescription.setOnMouseExited(e -> addDescription.setStyle("-fx-background-color:  #0077CC; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 16px; -fx-padding: 3 5;"));
            addDescription.setOnMouseClicked(e -> addDescription.setStyle("-fx-background-color: #002540; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 16px; -fx-padding: 3 50;"));
            descAllergenContainer.add(addDescription, 0, 0);


            CheckBox dairy = new CheckBox("Dairy");
            CheckBox soy = new CheckBox("Soy");
            CheckBox nuts = new CheckBox("Nuts");
            CheckBox egg = new CheckBox("Egg");
            CheckBox shellfish = new CheckBox("Shellfish");
            CheckBox vegan = new CheckBox("Vegan");
            List<CheckBox> checkboxes = Arrays.asList(dairy, soy, nuts, egg, shellfish);

            for (CheckBox checkbox : checkboxes) {
                checkbox.setOnAction(event -> {
                    String currentAllergens = dishAllergens.getOrDefault(id, "");
                    // Stop adding the same allergen twice logic replace currentAllergens
                    if (checkbox.isSelected() && !currentAllergens.contains(checkbox.getText())) {
                        currentAllergens += (currentAllergens.isEmpty() ? "" : ",") + checkbox.getText();
                    } else if (!checkbox.isSelected() && currentAllergens.contains(checkbox.getText())) {
                        currentAllergens = currentAllergens.replace(checkbox.getText() + ",", "").replace(checkbox.getText(), "");
                    }
                    // Add to map as normal
                    dishAllergens.put(id, currentAllergens);
                });
            }

            descAllergenContainer.add(dairy, 1, 0);
            descAllergenContainer.add(soy, 2, 0);
            descAllergenContainer.add(nuts, 3, 0);
            descAllergenContainer.add(egg, 1, 1);
            descAllergenContainer.add(shellfish, 2, 1);
            descAllergenContainer.add(vegan, 3, 1);

            // HBox that holds info about Dish name and Price labels
            dishContainer.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #999999;");
            dishContainer.setMinSize(500, 50);
            dishContainer.setMaxSize(500, 50);
            dishItemsContainer.getChildren().add(dishContainer);


            dishItemsContainer.setMinHeight(110);
            menuItemsContainer.getChildren().add(dishItemsContainer); // Group the elements together in the end
        }

        Label menuDishName = new Label("Dish Name");
        HBox.setMargin(menuDishName, new Insets(60, 0, 0, -450));
        menuDishName.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Label menuDishPrice = new Label("Price");
        HBox.setMargin(menuDishPrice, new Insets(60, 0, 0, 100));
        menuDishPrice.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        TextField changeAdditionalMarkup = new TextField();
        VBox.setMargin(changeAdditionalMarkup, new Insets(100, 0, 0, 350));
        changeAdditionalMarkup.setStyle("-fx-font-size: 16px; -fx-background-color: #f0f0f0; -fx-border-color: #313131;-fx-background-radius: 20");
        changeAdditionalMarkup.setMaxWidth(INPUT_FIELD_WIDTH * 1.3);
        changeAdditionalMarkup.setPromptText("Add Additional Markup Percentage");
        markupContainer.getChildren().add(changeAdditionalMarkup);

        Button saveAdditionalMarkup = createButton("Add service fee %", event -> {
            String text = changeAdditionalMarkup.getText();
            // If the field is empty or the value is less than 0.1%
            if (text.isEmpty() || !text.matches("^\\d*\\.?\\d+$") || Double.parseDouble(text) < 0.1) {
                showErrorAlert("Invalid Input");
                return;
            }
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm changes");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to update the additional markup percentage?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                additionalMarkupPercentage = Double.parseDouble(text);
                // Update dish prices based on the new additional markup
                updateDishPricesAdditional();
                alert.close();
            }
        });
        saveAdditionalMarkup.setPrefWidth(INPUT_FIELD_WIDTH);
        VBox.setMargin(saveAdditionalMarkup, new Insets(10, 0, 0, 350));

        markupContainer.getChildren().add(saveAdditionalMarkup);

        // Other existing code...


        Button saveAll = new Button("Save Menu");
        VBox.setMargin(saveAll, new Insets(50, 0, 0, 350));
        saveAll.setOnAction(saveEvent -> {
            if(dishDescriptions.size() < 1 && dishAllergens.size() < 1){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Operation");
                alert.setHeaderText(null);
                alert.setContentText("Please modify the menu before saving");
                alert.showAndWait();
                return;
            } else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Menu Saved");
                alert.setHeaderText(null);
                alert.setContentText("Menu changes have been saved to the database");
                alert.showAndWait();

                for(Map.Entry<Integer, Label> entry: dishPriceLabel.entrySet()){
                    String[] saveData = new String[mockData.dishInfoCapacity];
                    String saveName = mockData.menu.get(entry.getKey())[0];
                    String savePrice = entry.getValue().getText();
                    String saveDescription = dishDescriptions.get(entry.getKey());
                    String saveAllergens = dishAllergens.get(entry.getKey());
                    saveData[0] = saveName;
                    saveData[1] = savePrice;
                    saveData[2] =  saveDescription;
                    saveData[3] = saveAllergens;
                    mockData.menu.put(entry.getKey(), saveData);
                }
            }
            mockData.printMenu();
        });
        TextField changeInitialMarkup = new TextField();
        VBox.setMargin(changeInitialMarkup, new Insets(10, 0, 0, 350));
        changeInitialMarkup.setStyle("-fx-font-size: 16px; -fx-background-color: #f0f0f0; -fx-border-color: #313131;-fx-background-radius: 20");
        changeInitialMarkup.setMaxWidth(INPUT_FIELD_WIDTH * 1.3);
        changeInitialMarkup.setPromptText("Initial Markup Percentage");
        markupContainer.getChildren().add(changeInitialMarkup);

        Button saveInitialMarkup = createButton("Save initial Markup", event -> {
            String text = changeInitialMarkup.getText();
            if (!text.isEmpty() && text.matches("^\\d*\\.?\\d+$") && Float.valueOf(text) < 20) {
                defaultMarkupPercentage = Double.parseDouble(text);
                // Update dish prices based on the new initial markup
                updateDishPrices();
            } else {
                // Show error message
                showErrorAlert("Invalid Initial Markup Percentage");
            }
        });
        saveInitialMarkup.setPrefWidth(INPUT_FIELD_WIDTH);
        VBox.setMargin(saveInitialMarkup, new Insets(10, 0, 0, 350));
        // Add styles and event handlers for the button
        markupContainer.getChildren().add(saveInitialMarkup);
        saveAll.setStyle(IDLE_BUTTON_STYLE);
        saveAll.setPrefWidth(255);
        saveAll.setOnMouseEntered(e -> saveAll.setStyle(HOVERED_BUTTON_STYLE));
        saveAll.setOnMouseExited(e -> saveAll.setStyle(IDLE_BUTTON_STYLE));
        saveAll.setOnMouseClicked(e -> saveAll.setStyle(CLICKED_BUTTON_STYLE));
        markupContainer.getChildren().add(saveAll);

        // Group all the elements together in a hierarchical way
        menuItemsContainer.setMinWidth(500);
        menuItemsContainer.setMaxWidth(500);

        HBox.setMargin(scrollPane, new Insets(100, 0, 0, -430));
        scrollPane.setContent(menuItemsContainer);
        scrollPane.setMinSize(500, 375);
        scrollPane.setMaxSize(500, 375);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background-color:transparent;");

        menuEditorContainer.getChildren().addAll(scrollPane, menuDishName, menuDishPrice);
        menuRightContainer.getChildren().add(markupContainer);
        mainPane.getChildren().addAll(menuEditorContainer, menuRightContainer);

        Button backButton = createBackButtonManager();
        layout.setLeft(greetingLabel);
        layout.setBottom(backButton);
        layout.setCenter(mainPane);
        return new Scene(layout, SCREEN_RES_WIDTH, SCREEN_RES_HEIGHT);
    }
    private void updateDishPrices() {
        for (Map.Entry<Integer, Label> label : originalDishPriceLabel.entrySet()) {
            Integer key = label.getKey();
            Label originalLabel = label.getValue();
            Label dishLabel = dishPriceLabel.get(key);
            String priceLabelText = originalLabel.getText().replaceAll("[^0-9.]", "");
            double price;

            // Calculate price with default markup
            double defaultMarkupPrice = (Double.parseDouble(priceLabelText)) * defaultMarkupPercentage / oldDefaultMarkupPercentage;


            // Calculate total price
            price = defaultMarkupPrice;

            dishLabel.setText("£" + String.format("%.2f", price));
            mockData.menu.get(label.getKey())[2] = dishLabel.getText();
        }
    }
    private void updateDishPricesAdditional() {
        for (Map.Entry<Integer, Label> label : dishPriceLabel.entrySet()) {
            String priceLabelText = label.getValue().getText().replaceAll("[^0-9.]", "");
            double price;



            // Calculate price with additional markup
            double additionalMarkupPrice = (additionalMarkupPercentage * Double.parseDouble(priceLabelText) / 100);

            // Calculate total price
            price =  Double.parseDouble(priceLabelText) + additionalMarkupPrice;

            label.getValue().setText("£" + String.format("%.2f", price));
            MockData.menu.get(label.getKey())[2] = label.getValue().getText();
        }
    }
    private void showErrorAlert(String invalidInput) {
    }
}
