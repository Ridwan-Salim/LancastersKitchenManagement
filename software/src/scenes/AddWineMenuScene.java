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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AddWineMenuScene extends DirectorScene {

    private Map<Integer, String> wines = new HashMap<>();

    public Scene createScene() {
        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(20));

        VBox menuEditorContainer = new VBox();
        HBox mainPane = new HBox();
        ScrollPane scrollPane = new ScrollPane();
        VBox menuItemsContainer = new VBox();
        HBox saveContainer = new HBox();
        menuItemsContainer.setSpacing(25);

        Label greetingLabel = new Label("Let`s add some wine to the menu, " + employeeName + ".");
        greetingLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        BorderPane.setAlignment(greetingLabel, Pos.TOP_LEFT);
        BorderPane.setMargin(greetingLabel, new Insets(10, 0, 0, 10));
        Timeline labelAnimation = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(greetingLabel.opacityProperty(), 0)),
                new KeyFrame(Duration.seconds(1), new KeyValue(greetingLabel.opacityProperty(), 1))
        );
        labelAnimation.play();

        // Most of the UI generation for the ScrollPane happens in here
        for (Map.Entry<Integer, String[]> entry : MockData.menu.entrySet()) {
            VBox dishItemsContainer = new VBox();
            GridPane dishContainer = new GridPane();
            VBox wineLabelContainer = new VBox();
            Integer id = entry.getKey();
            Label dishName = new Label(entry.getValue()[0]);
            dishName.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
            GridPane.setMargin(dishName, new Insets(10, 0, 0, 20)); // TRBL
            dishContainer.getChildren().add(dishName);
            dishContainer.add(wineLabelContainer, 2, 0);

            ColumnConstraints col1 = new ColumnConstraints();
            col1.setPrefWidth(250);
            ColumnConstraints col2 = new ColumnConstraints();
            col2.setPrefWidth(250);
            dishContainer.getColumnConstraints().addAll(col1, col2);

            String[] wineList = {""};
            ComboBox<String> wineSelector = new ComboBox<>();
            wineSelector.setPromptText("Add Wine");
            wineSelector.setStyle("-fx-font-size: 16px; -fx-background-color: #f0f0f0; -fx-border-color: #999999;-fx-background-radius: 20");
            wineSelector.setOnMouseEntered(e -> wineSelector.setStyle("-fx-font-size: 16px; -fx-background-color: #e0e0e0; -fx-border-color: #999999"));
            wineSelector.setOnMouseEntered(e -> wineSelector.setStyle("-fx-font-size: 16px; -fx-background-color: #e0e0e0; -fx-border-color: #999999"));
            for (String wine : MockData.wines.keySet()) {
                wineSelector.getItems().add(wine);
            }

            Button addWine = new Button("Add Wine");
            dishContainer.add(addWine, 1, 0);
            GridPane.setMargin(addWine, new Insets(10, 0, 0, 0));
            addWine.setStyle("-fx-background-color:  #0077CC; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 16px; -fx-padding: 3 5;");
            addWine.setPrefWidth(150);
            addWine.setOnMouseEntered(e -> addWine.setStyle("-fx-background-color: #025692; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 16px; -fx-padding: 3 5;"));
            addWine.setOnMouseExited(e -> addWine.setStyle("-fx-background-color:  #0077CC; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 16px; -fx-padding: 3 5;"));
            addWine.setOnMouseClicked(e -> {
                // Turn the addwine into a combobox
                dishContainer.getChildren().remove(addWine);
                dishContainer.add(wineSelector, 1, 0);
            });

            wineSelector.setOnAction(event -> {
                Set<String> selectedWines = new HashSet<>();
                String selectedWine = wineSelector.getValue();
                if (selectedWines.contains(selectedWine)) {
                    return;
                }
                selectedWines.add(selectedWine);
                Label wineLabel = new Label(selectedWine);
                wineLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
                wineLabelContainer.getChildren().add(wineLabel);
                VBox.setMargin(wineLabel, new Insets(5, 0, 0, 0));

                // Bring back the add wine button
                dishContainer.getChildren().remove(wineSelector);
                dishContainer.getChildren().add(addWine);

                if (wineList[0].isEmpty()) {
                    wineList[0] += selectedWine;
                } else {
                    wineList[0] += "," + selectedWine;
                }
                wines.put(id, wineList[0]);
                //System.out.println(MockData.menu.get(id)[0] + ": " + MockData.menu.get(id)[4]);
            });

            // HBox that holds info about Dish name labels and Wine Selector Buttons
            dishContainer.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #999999;");
            dishContainer.setMinSize(750, 50);
            dishItemsContainer.getChildren().add(dishContainer);

            menuItemsContainer.getChildren().add(dishItemsContainer); // Group the elements together in the end
        }

        Button saveAll = new Button("Save Completed Menu");
        HBox.setMargin(saveAll, new Insets(100, 0, 0, 50));
        saveAll.setOnAction(saveEvent -> {
            if(wines.size() < 1){
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
                alert.setContentText("Conmpleted menu has been saved to the database");
                alert.showAndWait();

                for(Map.Entry<Integer, String> entry: wines.entrySet()){
                    MockData.menu.get(entry.getKey())[4] = entry.getValue();
                }
            }
            //MockData.printMenu();
        });

        saveAll.setStyle("-fx-background-color:  #0077CC; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 16px; -fx-padding: 3 5;");
        saveAll.setPrefWidth(250);
        saveAll.setOnMouseEntered(e -> saveAll.setStyle("-fx-background-color: #025692; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 16px; -fx-padding: 3 5;"));
        saveAll.setOnMouseExited(e -> saveAll.setStyle("-fx-background-color:  #0077CC; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 16px; -fx-padding: 3 5;"));
        saveAll.setOnMouseClicked(e -> saveAll.setStyle("-fx-background-color: #002540; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 16px; -fx-padding: 3 50;"));


        // Group all the elements together in a hierarchical way
        menuItemsContainer.setMinWidth(750);
        menuItemsContainer.setMaxWidth(750);

        VBox.setMargin(scrollPane, new Insets(100, 0, 0, -510));
        scrollPane.setContent(menuItemsContainer);
        scrollPane.setMinSize(750, 380);
        scrollPane.setMaxSize(750, 380);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background-color:transparent;");

        saveContainer.getChildren().add(saveAll);
        menuEditorContainer.getChildren().addAll(scrollPane);
        mainPane.getChildren().addAll(menuEditorContainer, saveContainer);

        Button backButton = createBackButtonDirector();
        layout.setLeft(greetingLabel);
        layout.setBottom(backButton);
        layout.setCenter(mainPane);
        return new Scene(layout, SCREEN_RES_WIDTH, SCREEN_RES_HEIGHT);
    }
}
