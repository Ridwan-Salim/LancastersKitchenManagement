package scenes;

import core.DBConnect;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Duration;
import javafx.geometry.Pos;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class WineCellar extends Sommelier{

    public Map<Integer, String> cellarNames = new HashMap<>();
    public Map<Integer, String> cellarPrice = new HashMap<>();
    public Map<Integer, String> cellarDescription = new HashMap<>();
    public Map<Integer, String> cellarVintage = new HashMap<>();
    public Map<Integer, String> cellarQty = new HashMap<>();
    private Map<Integer, String[]> uploadCopy = new HashMap<>();

    public Scene createScene() {
        MockData.addWines();
        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(20));

        VBox menuEditorContainer = new VBox();
        HBox mainPane = new HBox();
        GridPane newWine = new GridPane();
        ScrollPane scrollPane = new ScrollPane();
        VBox menuItemsContainer = new VBox();
        HBox saveContainer = new HBox();
        //menuItemsContainer.setSpacing(25);

        Label greetingLabel = new Label("Welcome to your wine cellar, " + employeeName + ".");
        greetingLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        BorderPane.setAlignment(greetingLabel, Pos.TOP_LEFT);
        BorderPane.setMargin(greetingLabel, new Insets(10, 0, 0, 10));
        Timeline labelAnimation = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(greetingLabel.opacityProperty(), 0)),
                new KeyFrame(Duration.seconds(1), new KeyValue(greetingLabel.opacityProperty(), 1))
        );
        labelAnimation.play();

        // Most of the UI generation for the ScrollPane happens in here
        for (Map.Entry<Integer, String[]> entry : MockData.wines.entrySet()) {
            generateWineUI(entry.getKey(), entry.getValue(), menuItemsContainer);
        }

        Button saveAll = new Button("Save Completed Menu");
        HBox.setMargin(saveAll, new Insets(0, 0, 0, 100));
        saveAll.setStyle("-fx-background-color:  #0077CC; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 16px; -fx-padding: 3 5;");
        saveAll.setPrefWidth(250);
        saveAll.setOnMouseEntered(e -> saveAll.setStyle("-fx-background-color: #025692; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 16px; -fx-padding: 3 5;"));
        saveAll.setOnMouseExited(e -> saveAll.setStyle("-fx-background-color:  #0077CC; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 16px; -fx-padding: 3 5;"));
        saveAll.setOnMouseClicked(e -> saveAll.setStyle("-fx-background-color: #002540; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 16px; -fx-padding: 3 50;"));
        saveAll.setOnAction(saveEvent -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Save Wine");
            alert.setHeaderText(null);
            alert.setContentText("Confirm save cellar changes");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                for(Map.Entry<Integer, String> entry : cellarNames.entrySet()){
                    Integer id = entry.getKey();
                    String wineName = entry.getValue();
                    String winePrice = cellarPrice.get(id);
                    String wineDescription = cellarDescription.get(id);
                    String wineVintage = cellarVintage.get(id);
                    String wineQty = cellarQty.get(id);
                    MockData.wines.put(id, new String[] {wineName, winePrice, wineDescription, wineVintage, wineQty});
                }
                uploadCopy = MockData.wines.entrySet().stream()
                        .collect(Collectors.toMap(Map.Entry::getKey, e -> Arrays.copyOf(e.getValue(), e.getValue().length)));
                try {
                    DBConnect.uploadWineMenu(uploadCopy);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        //MockData.printWines();

        TextField nameTextField = new TextField();
        nameTextField.setPromptText("Name");
        nameTextField.setStyle("-fx-font-size: 16px; -fx-background-color: #f0f0f0; -fx-border-color: #313131;-fx-background-radius: 20");
        GridPane.setMargin(nameTextField, new Insets(5, 0, 0, -430));
        nameTextField.setMaxSize(100, 50);
        nameTextField.requestFocus();

        TextField vintageTextField = new TextField();
        vintageTextField.setPromptText("Vintage");
        vintageTextField.setStyle("-fx-font-size: 16px; -fx-background-color: #f0f0f0; -fx-border-color: #313131;-fx-background-radius: 20");
        GridPane.setMargin(vintageTextField, new Insets(5, 0, 0, -250));
        vintageTextField.setMaxSize(80, 50);

        TextField priceTextField = new TextField();
        priceTextField.setPromptText("Price");
        priceTextField.setStyle("-fx-font-size: 16px; -fx-background-color: #f0f0f0; -fx-border-color: #313131;-fx-background-radius: 20");
        GridPane.setMargin(priceTextField, new Insets(5, 0, 0, -140));
        priceTextField.setMaxSize(80, 50);

        TextField qtyTextField = new TextField();
        qtyTextField.setPromptText("Qty");
        qtyTextField.setStyle("-fx-font-size: 16px; -fx-background-color: #f0f0f0; -fx-border-color: #313131;-fx-background-radius: 20");
        GridPane.setMargin(qtyTextField, new Insets(5, 0, 0, 50));
        qtyTextField.setMaxSize(120, 50);

        TextArea descriptionTextArea = new TextArea();
        descriptionTextArea.setPromptText("Enter description");
        descriptionTextArea.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #999999;");
        descriptionTextArea.setMaxSize(150, 50);
        GridPane.setMargin(descriptionTextArea, new Insets(5, 0, 0, 68));
        descriptionTextArea.requestFocus();

        Button saveWine = new Button("Save Wine");
        GridPane.setMargin(saveWine, new Insets(0, 0, 0, 90));
        saveWine.setStyle("-fx-background-color:  #0077CC; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 16px; -fx-padding: 3 5;");
        saveWine.setPrefWidth(120);
        saveWine.setOnMouseEntered(e -> saveWine.setStyle("-fx-background-color: #025692; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 16px; -fx-padding: 3 5;"));
        saveWine.setOnMouseExited(e -> saveWine.setStyle("-fx-background-color:  #0077CC; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 16px; -fx-padding: 3 5;"));
        saveWine.setOnMouseClicked(e -> {
            if(nameTextField.getText() != null && priceTextField.getText() != null && descriptionTextArea.getText() != null
            && vintageTextField.getText() != null && qtyTextField.getText() != null){
                if (!isNumeric(priceTextField.getText()) || !isNumeric(vintageTextField.getText()) || !isNumeric(qtyTextField.getText())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Invalid Input");
                    alert.setHeaderText(null);
                    alert.setContentText("Price, Vintage, and Quantity must be numeric values.");
                    alert.showAndWait();
                } else{
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Save Wine");
                    alert.setHeaderText(null);
                    alert.setContentText("Would you like to save this new wine?");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {
                        String[] details = new String[] {nameTextField.getText(), priceTextField.getText(),
                                descriptionTextArea.getText(), vintageTextField.getText(), qtyTextField.getText()};

                        MockData.wines.put(MockData.wines.size()+1, details);

                        newWine.getChildren().removeAll(nameTextField, priceTextField, descriptionTextArea, vintageTextField,
                                qtyTextField, saveWine);
                        //generateWineUI(MockData.wines.size()+1, details, menuItemsContainer);
                        MockData.printWines();
                        alert.close();
                    }
                }
            }
        });

        Button addWine = new Button("Add Wine");
        HBox.setMargin(addWine, new Insets(0, 0, 0, 80));
        addWine.setStyle("-fx-background-color:  #0077CC; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 16px; -fx-padding: 3 5;");
        addWine.setPrefWidth(150);
        addWine.setOnMouseEntered(e -> addWine.setStyle("-fx-background-color: #025692; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 16px; -fx-padding: 3 5;"));
        addWine.setOnMouseExited(e -> addWine.setStyle("-fx-background-color:  #0077CC; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 16px; -fx-padding: 3 5;"));
        addWine.setOnMouseClicked(e -> {
            newWine.add(nameTextField, 0, 0);
            newWine.add(vintageTextField, 1, 0);
            newWine.add(priceTextField, 2, 0);
            newWine.add(descriptionTextArea, 4, 0);
            newWine.add(qtyTextField, 6, 0);
            newWine.add(saveWine, 7, 0);
            saveContainer.getChildren().remove(addWine);
        });


        Button exportPdf = new Button("Export PDF");
        HBox.setMargin(exportPdf, new Insets(0, 0, 0, 80));
        exportPdf.setStyle("-fx-background-color:  #0077CC; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 16px; -fx-padding: 3 5;");
        exportPdf.setPrefWidth(150);
        exportPdf.setOnMouseEntered(e -> exportPdf.setStyle("-fx-background-color: #025692; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 16px; -fx-padding: 3 5;"));
        exportPdf.setOnMouseExited(e -> exportPdf.setStyle("-fx-background-color:  #0077CC; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 16px; -fx-padding: 3 5;"));
        exportPdf.setOnMouseClicked(e -> {

        });

        // Group all the elements together in a hierarchical way
        menuItemsContainer.setMinWidth(1050);
        menuItemsContainer.setMaxWidth(1050);

        VBox.setMargin(scrollPane, new Insets(50, 0, 0, -600));
        scrollPane.setContent(menuItemsContainer);
        scrollPane.setMinSize(1050, 375);
        scrollPane.setMaxSize(1050, 375);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background-color:transparent;");

        menuEditorContainer.getChildren().addAll(scrollPane, newWine);
        mainPane.getChildren().addAll(menuEditorContainer);

        Button backButton = createBackButtonDirector();
        saveContainer.getChildren().addAll(backButton, saveAll, addWine, exportPdf);
        layout.setLeft(greetingLabel);
        layout.setBottom(saveContainer);
        layout.setCenter(mainPane);
        return new Scene(layout, SCREEN_RES_WIDTH, SCREEN_RES_HEIGHT);
    }
    public Scene createScene(boolean toggleManager) {
        MockData.addWines();
        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(20));

        VBox menuEditorContainer = new VBox();
        HBox mainPane = new HBox();
        GridPane newWine = new GridPane();
        ScrollPane scrollPane = new ScrollPane();
        VBox menuItemsContainer = new VBox();
        HBox saveContainer = new HBox();
        //menuItemsContainer.setSpacing(25);

        Label greetingLabel = new Label("Welcome to your wine cellar, " + employeeName + ".");
        greetingLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        BorderPane.setAlignment(greetingLabel, Pos.TOP_LEFT);
        BorderPane.setMargin(greetingLabel, new Insets(10, 0, 0, 10));
        Timeline labelAnimation = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(greetingLabel.opacityProperty(), 0)),
                new KeyFrame(Duration.seconds(1), new KeyValue(greetingLabel.opacityProperty(), 1))
        );
        labelAnimation.play();

        // Most of the UI generation for the ScrollPane happens in here
        for (Map.Entry<Integer, String[]> entry : MockData.wines.entrySet()) {
            generateWineUI(entry.getKey(), entry.getValue(), menuItemsContainer);
        }

        Button saveAll = new Button("Save Completed Menu");
        HBox.setMargin(saveAll, new Insets(0, 0, 0, 100));
        saveAll.setStyle("-fx-background-color:  #0077CC; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 16px; -fx-padding: 3 5;");
        saveAll.setPrefWidth(250);
        saveAll.setOnMouseEntered(e -> saveAll.setStyle("-fx-background-color: #025692; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 16px; -fx-padding: 3 5;"));
        saveAll.setOnMouseExited(e -> saveAll.setStyle("-fx-background-color:  #0077CC; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 16px; -fx-padding: 3 5;"));
        saveAll.setOnMouseClicked(e -> saveAll.setStyle("-fx-background-color: #002540; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 16px; -fx-padding: 3 50;"));
        saveAll.setOnAction(saveEvent -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Save Wine");
            alert.setHeaderText(null);
            alert.setContentText("Confirm save cellar changes");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                for(Map.Entry<Integer, String> entry : cellarNames.entrySet()){
                    Integer id = entry.getKey();
                    String wineName = entry.getValue();
                    String winePrice = cellarPrice.get(id);
                    String wineDescription = cellarDescription.get(id);
                    String wineVintage = cellarVintage.get(id);
                    String wineQty = cellarQty.get(id);
                    MockData.wines.put(id, new String[] {wineName, winePrice, wineDescription, wineVintage, wineQty});
                }
            }
        });
        //MockData.printWines();

        TextField nameTextField = new TextField();
        nameTextField.setPromptText("Name");
        nameTextField.setStyle("-fx-font-size: 16px; -fx-background-color: #f0f0f0; -fx-border-color: #313131;-fx-background-radius: 20");
        GridPane.setMargin(nameTextField, new Insets(5, 0, 0, -430));
        nameTextField.setMaxSize(100, 50);
        nameTextField.requestFocus();

        TextField vintageTextField = new TextField();
        vintageTextField.setPromptText("Vintage");
        vintageTextField.setStyle("-fx-font-size: 16px; -fx-background-color: #f0f0f0; -fx-border-color: #313131;-fx-background-radius: 20");
        GridPane.setMargin(vintageTextField, new Insets(5, 0, 0, -250));
        vintageTextField.setMaxSize(80, 50);

        TextField priceTextField = new TextField();
        priceTextField.setPromptText("Price");
        priceTextField.setStyle("-fx-font-size: 16px; -fx-background-color: #f0f0f0; -fx-border-color: #313131;-fx-background-radius: 20");
        GridPane.setMargin(priceTextField, new Insets(5, 0, 0, -140));
        priceTextField.setMaxSize(80, 50);

        TextField qtyTextField = new TextField();
        qtyTextField.setPromptText("Qty");
        qtyTextField.setStyle("-fx-font-size: 16px; -fx-background-color: #f0f0f0; -fx-border-color: #313131;-fx-background-radius: 20");
        GridPane.setMargin(qtyTextField, new Insets(5, 0, 0, 50));
        qtyTextField.setMaxSize(120, 50);

        TextArea descriptionTextArea = new TextArea();
        descriptionTextArea.setPromptText("Enter description");
        descriptionTextArea.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #999999;");
        descriptionTextArea.setMaxSize(150, 50);
        GridPane.setMargin(descriptionTextArea, new Insets(5, 0, 0, 68));
        descriptionTextArea.requestFocus();

        Button saveWine = new Button("Save Wine");
        GridPane.setMargin(saveWine, new Insets(0, 0, 0, 90));
        saveWine.setStyle("-fx-background-color:  #0077CC; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 16px; -fx-padding: 3 5;");
        saveWine.setPrefWidth(120);
        saveWine.setOnMouseEntered(e -> saveWine.setStyle("-fx-background-color: #025692; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 16px; -fx-padding: 3 5;"));
        saveWine.setOnMouseExited(e -> saveWine.setStyle("-fx-background-color:  #0077CC; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 16px; -fx-padding: 3 5;"));
        saveWine.setOnMouseClicked(e -> {
            if(nameTextField.getText() != null && priceTextField.getText() != null && descriptionTextArea.getText() != null
                    && vintageTextField.getText() != null && qtyTextField.getText() != null){
                if (!isNumeric(priceTextField.getText()) || !isNumeric(vintageTextField.getText()) || !isNumeric(qtyTextField.getText())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Invalid Input");
                    alert.setHeaderText(null);
                    alert.setContentText("Price, Vintage, and Quantity must be numeric values.");
                    alert.showAndWait();
                } else{
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Save Wine");
                    alert.setHeaderText(null);
                    alert.setContentText("Would you like to save this new wine?");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {
                        String[] details = new String[] {nameTextField.getText(), priceTextField.getText(),
                                descriptionTextArea.getText(), vintageTextField.getText(), qtyTextField.getText()};

                        MockData.wines.put(MockData.wines.size()+1, details);

                        newWine.getChildren().removeAll(nameTextField, priceTextField, descriptionTextArea, vintageTextField,
                                qtyTextField, saveWine);
                        //generateWineUI(MockData.wines.size()+1, details, menuItemsContainer);
                        MockData.printWines();
                        alert.close();
                    }
                }
            }
        });

        Button addWine = new Button("Add Wine");
        HBox.setMargin(addWine, new Insets(0, 0, 0, 80));
        addWine.setStyle("-fx-background-color:  #0077CC; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 16px; -fx-padding: 3 5;");
        addWine.setPrefWidth(150);
        addWine.setOnMouseEntered(e -> addWine.setStyle("-fx-background-color: #025692; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 16px; -fx-padding: 3 5;"));
        addWine.setOnMouseExited(e -> addWine.setStyle("-fx-background-color:  #0077CC; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 16px; -fx-padding: 3 5;"));
        addWine.setOnMouseClicked(e -> {
            newWine.add(nameTextField, 0, 0);
            newWine.add(vintageTextField, 1, 0);
            newWine.add(priceTextField, 2, 0);
            newWine.add(descriptionTextArea, 4, 0);
            newWine.add(qtyTextField, 6, 0);
            newWine.add(saveWine, 7, 0);
            saveContainer.getChildren().remove(addWine);
        });


        Button exportPdf = new Button("Export PDF");
        HBox.setMargin(exportPdf, new Insets(0, 0, 0, 80));
        exportPdf.setStyle("-fx-background-color:  #0077CC; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 16px; -fx-padding: 3 5;");
        exportPdf.setPrefWidth(150);
        exportPdf.setOnMouseEntered(e -> exportPdf.setStyle("-fx-background-color: #025692; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 16px; -fx-padding: 3 5;"));
        exportPdf.setOnMouseExited(e -> exportPdf.setStyle("-fx-background-color:  #0077CC; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 16px; -fx-padding: 3 5;"));
        exportPdf.setOnMouseClicked(e -> {

        });

        // Group all the elements together in a hierarchical way
        menuItemsContainer.setMinWidth(1050);
        menuItemsContainer.setMaxWidth(1050);

        VBox.setMargin(scrollPane, new Insets(50, 0, 0, -600));
        scrollPane.setContent(menuItemsContainer);
        scrollPane.setMinSize(1050, 375);
        scrollPane.setMaxSize(1050, 375);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background-color:transparent;");

        menuEditorContainer.getChildren().addAll(scrollPane, newWine);
        mainPane.getChildren().addAll(menuEditorContainer);

        Button backButton = createBackButtonDirector();
        saveContainer.getChildren().addAll(backButton, saveAll, addWine, exportPdf);
        layout.setLeft(greetingLabel);
        layout.setBottom(saveContainer);
        layout.setCenter(mainPane);
        return new Scene(layout, SCREEN_RES_WIDTH, SCREEN_RES_HEIGHT);
    }

    private boolean isNumeric(String str) {
        return str.matches("\\d+");
    }
    private void generateWineUI(Integer id, String[] wineDetails, VBox menuItemsContainer) {
        VBox wineItemsContainer = new VBox();
        GridPane wineContainer = new GridPane();
        Label wineName = new Label(wineDetails[0]);
        wineName.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        GridPane.setMargin(wineName, new Insets(10, 0, 0, 20)); // TRBL
        cellarNames.put(id, wineName.getText());

        Label winePrice = new Label("£" + wineDetails[1]);
        winePrice.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        GridPane.setMargin(winePrice, new Insets(10, 0, 0, 20));

        Label wineVintage = new Label(wineDetails[3]);
        wineVintage.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        GridPane.setMargin(wineVintage, new Insets(10, 0, 0, 20));
        cellarVintage.put(id, wineVintage.getText());

        Label wineQty = new Label(wineDetails[4]);
        wineQty.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        GridPane.setMargin(wineQty, new Insets(10, 0, 0, 20));

        TextArea wineDescription = new TextArea();
        wineDescription.setPromptText("Enter description");
        wineDescription.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #999999;");
        wineDescription.setMaxSize(150, 50);
        wineDescription.setWrapText(true);
        wineDescription.requestFocus();

        Button editPrice = new Button("Edit Price");
        Button savePrice = new Button("Save Price");
        TextField priceTextField = new TextField();
        Button saveDescription = new Button("Save Description");
        Button editQty = new Button("Edit Qty");
        Button saveQty = new Button("Save Qty");
        TextField qtyTextField = new TextField();

        GridPane.setMargin(savePrice, new Insets(10, 0, 0, 0));
        savePrice.setStyle("-fx-background-color:  #0077CC; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 16px; -fx-padding: 3 5;");
        savePrice.setPrefWidth(100);
        savePrice.setOnMouseEntered(e -> savePrice.setStyle("-fx-background-color: #025692; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 16px; -fx-padding: 3 5;"));
        savePrice.setOnMouseExited(e -> savePrice.setStyle("-fx-background-color:  #0077CC; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 16px; -fx-padding: 3 5;"));
        savePrice.setOnMouseClicked(e -> {
            if (!isNumeric(priceTextField.getText())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Input");
                alert.setHeaderText(null);
                alert.setContentText("Price, Vintage, and Quantity must be numeric values.");
                alert.showAndWait();
            } else{
                cellarPrice.put(id, priceTextField.getText());
                MockData.wines.get(id)[1] = priceTextField.getText();
                winePrice.setText("£"+priceTextField.getText());
                wineContainer.getChildren().remove(savePrice);
                wineContainer.getChildren().remove(priceTextField);
                wineContainer.add(editPrice, 3, 0);
            }
        });

        wineContainer.add(editPrice, 3, 0);
        GridPane.setMargin(editPrice, new Insets(10, 0, 0, 0));
        editPrice.setStyle("-fx-background-color:  #0077CC; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 16px; -fx-padding: 3 5;");
        editPrice.setPrefWidth(100);
        editPrice.setOnMouseEntered(e -> editPrice.setStyle("-fx-background-color: #025692; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 16px; -fx-padding: 3 5;"));
        editPrice.setOnMouseExited(e -> editPrice.setStyle("-fx-background-color:  #0077CC; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 16px; -fx-padding: 3 5;"));
        editPrice.setOnMouseClicked(e -> {
            wineContainer.getChildren().remove(editPrice);
            wineContainer.add(priceTextField, 2, 0);
            wineContainer.add(savePrice, 3, 0);
        });

        priceTextField.setPromptText("Enter Price");
        priceTextField.setStyle("-fx-font-size: 16px; -fx-background-color: #f0f0f0; -fx-border-color: #313131;-fx-background-radius: 20");
        GridPane.setMargin(priceTextField, new Insets(5, 0, 0, 0));
        priceTextField.setMaxSize(100, 50);
        priceTextField.requestFocus();

        GridPane.setMargin(saveDescription, new Insets(10, 0, 0, 0));
        saveDescription.setStyle("-fx-background-color:  #0077CC; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 16px; -fx-padding: 3 5;");
        saveDescription.setPrefWidth(150);
        saveDescription.setOnMouseEntered(e -> saveDescription.setStyle("-fx-background-color: #025692; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 16px; -fx-padding: 3 5;"));
        saveDescription.setOnMouseExited(e -> saveDescription.setStyle("-fx-background-color:  #0077CC; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 16px; -fx-padding: 3 5;"));
        saveDescription.setOnMouseClicked(e -> {
            saveDescription.setText("Edit Description");
            wineDescription.clear();
            MockData.wines.get(id)[2] = wineDescription.getText();
            cellarDescription.put(id, wineDescription.getText());
        });

        GridPane.setMargin(saveQty, new Insets(10, 0, 0, 0));
        saveQty.setStyle("-fx-background-color:  #0077CC; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 16px; -fx-padding: 3 5;");
        saveQty.setPrefWidth(100);
        saveQty.setOnMouseEntered(e -> saveQty.setStyle("-fx-background-color: #025692; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 16px; -fx-padding: 3 5;"));
        saveQty.setOnMouseExited(e -> saveQty.setStyle("-fx-background-color:  #0077CC; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 16px; -fx-padding: 3 5;"));
        saveQty.setOnMouseClicked(e -> {
            if (!isNumeric(qtyTextField.getText())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Input");
                alert.setHeaderText(null);
                alert.setContentText("Price, Vintage, and Quantity must be numeric values.");
                alert.showAndWait();
            } else{
                cellarQty.put(id, qtyTextField.getText());
                MockData.wines.get(id)[4] = qtyTextField.getText();
                wineQty.setText(qtyTextField.getText());
                wineContainer.getChildren().remove(saveQty);
                wineContainer.getChildren().remove(qtyTextField);
                wineContainer.add(editQty, 7, 0);
            }
        });

        wineContainer.add(editQty, 7, 0);
        GridPane.setMargin(editQty, new Insets(10, 0, 0, 0));
        editQty.setStyle("-fx-background-color:  #0077CC; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 16px; -fx-padding: 3 5;");
        editQty.setPrefWidth(100);
        editQty.setOnMouseEntered(e -> editQty.setStyle("-fx-background-color: #025692; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 16px; -fx-padding: 3 5;"));
        editQty.setOnMouseExited(e -> editQty.setStyle("-fx-background-color:  #0077CC; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 16px; -fx-padding: 3 5;"));
        editQty.setOnMouseClicked(e -> {
            wineContainer.getChildren().remove(editQty);
            wineContainer.add(qtyTextField, 6, 0);
            wineContainer.add(saveQty, 7, 0);
        });

        qtyTextField.setPromptText("Enter Qty");
        qtyTextField.setStyle("-fx-font-size: 16px; -fx-background-color: #f0f0f0; -fx-border-color: #313131;-fx-background-radius: 20");
        GridPane.setMargin(qtyTextField, new Insets(5, 0, 0, 0));
        qtyTextField.setMaxSize(100, 50);
        qtyTextField.requestFocus();

        wineContainer.add(wineName, 0, 0);
        wineContainer.add(winePrice, 2, 0);
        wineContainer.add(wineVintage, 1, 0);
        wineContainer.add(wineDescription, 4, 0);
        wineContainer.add(saveDescription, 5, 0);
        wineContainer.add(wineQty, 6, 0);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPrefWidth(215);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPrefWidth(80);
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPrefWidth(100);
        ColumnConstraints col4 = new ColumnConstraints();
        col4.setPrefWidth(120);
        ColumnConstraints col5 = new ColumnConstraints();
        col5.setPrefWidth(170);
        ColumnConstraints col6 = new ColumnConstraints();
        col6.setPrefWidth(160);
        ColumnConstraints col7 = new ColumnConstraints();
        col7.setPrefWidth(80);
        wineContainer.getColumnConstraints().addAll(col1, col2, col3, col4, col5, col6, col7);

        // HBox that holds info about Wine names
        wineContainer.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #999999;");
        wineContainer.setMinSize(750, 50);
        wineItemsContainer.getChildren().add(wineContainer);

        menuItemsContainer.getChildren().add(wineItemsContainer); // Group the elements together in the end
    }
}

