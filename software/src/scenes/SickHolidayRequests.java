package scenes;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SickHolidayRequests extends Manager {

    public static final int QUOTA = 28; // Quota for leave days per year
    private List<LeaveRequest> leaveRequests;
    private VBox leaveRequestsContainer;

    public Scene createScene() {
        readLeaveRequestsFromFile("D:\\LancastersKitchenMgmt\\software\\src\\scenes\\utils\\leaves.csv");
        Label greetingLabel = new Label("Let`s observe sick/holiday requests, " + employeeName + ".");
        greetingLabel.setMinWidth(350);
        greetingLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        BorderPane.setAlignment(greetingLabel, Pos.TOP_LEFT);
        BorderPane.setMargin(greetingLabel, new Insets(10, 0, 0, 10));
        Timeline labelAnimation = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(greetingLabel.opacityProperty(), 0)),
                new KeyFrame(Duration.seconds(1), new KeyValue(greetingLabel.opacityProperty(), 1))
        );
        labelAnimation.play();
        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #f0f0f0;");

        // Only read leave requests from CSV file once
        if (leaveRequests == null) {
            leaveRequests = new ArrayList<>();
            readLeaveRequestsFromFile("D:\\LancastersKitchenMgmt\\software\\src\\scenes\\utils\\leaves.csv");
        }

        leaveRequestsContainer = new VBox(20); // Increased spacing
        leaveRequestsContainer.setPadding(new Insets(10));
        leaveRequestsContainer.setAlignment(Pos.TOP_LEFT);

        // Add column names
        leaveRequestsContainer.getChildren().add(createColumnNames());

        // Display leave requests on the screen
        for (LeaveRequest request : leaveRequests) {
            leaveRequestsContainer.getChildren().add(createLeaveRequestNode(request));
        }

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(leaveRequestsContainer);
        scrollPane.setFitToWidth(true);

        Button backButton = createBackButtonManager();
        HBox topLayer = new HBox();
        topLayer.setSpacing(300);
        topLayer.getChildren().addAll(greetingLabel, backButton);
        layout.setTop(topLayer);
        BorderPane.setAlignment(backButton, Pos.CENTER_RIGHT);

        layout.setCenter(scrollPane);

        return new Scene(layout, SCREEN_RES_WIDTH, SCREEN_RES_HEIGHT);
    }

    public Scene createScene(boolean toggleManager) {
        readLeaveRequestsFromFile("D:\\LancastersKitchenMgmt\\software\\src\\scenes\\utils\\leaves.csv");
        Label greetingLabel = new Label("Let`s observe sick/holiday requests, " + employeeName + ".");
        greetingLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        BorderPane.setAlignment(greetingLabel, Pos.TOP_LEFT);
        BorderPane.setMargin(greetingLabel, new Insets(10, 0, 0, 10));
        Timeline labelAnimation = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(greetingLabel.opacityProperty(), 0)),
                new KeyFrame(Duration.seconds(1), new KeyValue(greetingLabel.opacityProperty(), 1))
        );
        labelAnimation.play();
        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #f0f0f0;");

        // Only read leave requests from CSV file once
        if (leaveRequests == null) {
            leaveRequests = new ArrayList<>();
            readLeaveRequestsFromFile("D:\\LancastersKitchenMgmt\\software\\src\\scenes\\utils\\leaves.csv");
        }

        leaveRequestsContainer = new VBox(20); // Increased spacing
        leaveRequestsContainer.setPadding(new Insets(10));
        leaveRequestsContainer.setAlignment(Pos.TOP_LEFT);

        // Add column names
        leaveRequestsContainer.getChildren().add(createColumnNames());

        // Display leave requests on the screen
        for (LeaveRequest request : leaveRequests) {
            leaveRequestsContainer.getChildren().add(createLeaveRequestNode(request));
        }

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(leaveRequestsContainer);
        scrollPane.setFitToWidth(true);

        Button backButton = createBackButtonManager();
        HBox topLayer = new HBox();
        topLayer.setSpacing(300);
        topLayer.getChildren().addAll(greetingLabel, backButton);
        layout.setTop(topLayer);
        BorderPane.setAlignment(backButton, Pos.CENTER_RIGHT);

        layout.setCenter(scrollPane);

        return new Scene(layout, SCREEN_RES_WIDTH, SCREEN_RES_HEIGHT);
    }


    private void readLeaveRequestsFromFile(String filename) {
        leaveRequests = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 6 || parts.length == 5) {
                    LeaveRequest request = new LeaveRequest(parts[0], parts[1], parts[2], parts[3], parts[4]);
                    leaveRequests.add(request);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private HBox createColumnNames() {
        HBox columnNames = new HBox(20);
        columnNames.setSpacing(25);// Increased spacing
        columnNames.setStyle("-fx-background-color: #cccccc; -fx-padding: 5px;");
        columnNames.setAlignment(Pos.CENTER_LEFT);

        Label nameLabel = new Label("Employee Name");
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14)); // Bold font
        nameLabel.setTextFill(Color.BLACK);

        Label dateCreatedLabel = new Label("Date Created");
        dateCreatedLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14)); // Bold font
        dateCreatedLabel.setTextFill(Color.BLACK);

        Label startDateLabel = new Label("Start Date");
        startDateLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14)); // Bold font
        startDateLabel.setTextFill(Color.BLACK);

        Label endDateLabel = new Label("End Date");
        endDateLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14)); // Bold font
        endDateLabel.setTextFill(Color.BLACK);

        Label reasonLabel = new Label("Reason");
        reasonLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14)); // Bold font
        reasonLabel.setTextFill(Color.BLACK);

        Label actionLabel = new Label("Action");
        actionLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14)); // Bold font
        actionLabel.setTextFill(Color.BLACK);

        Label exceedsQuotaLabel = new Label("Exceeds Quota");
        exceedsQuotaLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14)); // Bold font
        exceedsQuotaLabel.setTextFill(Color.BLACK);

        columnNames.getChildren().addAll(nameLabel, dateCreatedLabel, startDateLabel, endDateLabel, reasonLabel, actionLabel, exceedsQuotaLabel);
        return columnNames;
    }

    private HBox createLeaveRequestNode(LeaveRequest request) {
        HBox requestNode = new HBox(20); // Increased spacing
        requestNode.setSpacing(35);
        requestNode.setStyle("-fx-background-color: #ffffff; -fx-padding: 10px;"); // Increased padding
        requestNode.setAlignment(Pos.CENTER_LEFT);

        Label nameLabel = new Label(request.getEmployeeName());
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12)); // Bold font
        nameLabel.setTextFill(Color.BLACK);

        Label dateCreatedLabel = new Label(request.getDateCreated());
        dateCreatedLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12)); // Bold font
        dateCreatedLabel.setTextFill(Color.BLACK);

        Label startDateLabel = new Label(request.getStartDate());
        startDateLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12)); // Bold font
        startDateLabel.setTextFill(Color.BLACK);

        Label endDateLabel = new Label(request.getEndDate());
        endDateLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12)); // Bold font
        endDateLabel.setTextFill(Color.BLACK);

        Label reasonLabel = new Label(request.getReason());
        reasonLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12)); // Bold font
        reasonLabel.setTextFill(Color.BLACK);

        Button approveButton = new Button("Approve");
        approveButton.setOnAction(event -> {
            request.setApproved(true);
            removeLeaveRequestNode(request); // Remove the node from the container
            saveLeaveRequestsToFile("D:\\LancastersKitchenMgmt\\software\\src\\scenes\\utils\\leaves.csv", leaveRequests);
            updateUI(); // Update the UI
        });
        String HOVERED_BUTTON_STYLE1 = "-fx-background-color:  #34962d; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 12px; -fx-padding: 3 50;";
        String IDLE_BUTTON_STYLE1 = "-fx-background-color:  #07cc00; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 12px; -fx-padding: 3 50;";
        String CLICKED_BUTTON_STYLE1 = "-fx-background-color:  #00671b; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 12px; -fx-padding: 3 50;";
        approveButton.setAlignment(Pos.CENTER);
        approveButton.setStyle(IDLE_BUTTON_STYLE1);
        approveButton.setOnMouseEntered(e -> approveButton.setStyle(HOVERED_BUTTON_STYLE1));
        approveButton.setOnMouseExited(e -> approveButton.setStyle(IDLE_BUTTON_STYLE1));
        approveButton.setOnMouseClicked(e -> approveButton.setStyle(CLICKED_BUTTON_STYLE1));
        Button disapproveButton = new Button("Disapprove");

        disapproveButton.setOnAction(event -> {
            request.setApproved(false);
            removeLeaveRequestNode(request); // Remove the node from the container
            saveLeaveRequestsToFile("D:\\LancastersKitchenMgmt\\software\\src\\scenes\\utils\\leaves.csv", leaveRequests);
            updateUI(); // Update the UI
        });

        String HOVERED_BUTTON_STYLE2 = "-fx-background-color:  #a40101; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 12px; -fx-padding: 3 50;";
        String IDLE_BUTTON_STYLE2 = "-fx-background-color:  #da0101; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 12px; -fx-padding: 3 50;";
        String CLICKED_BUTTON_STYLE2 = "-fx-background-color:  #5d0000; -fx-text-fill: white; -fx-background-radius: 20; -fx-font-size: 12px; -fx-padding: 3 50;";
        disapproveButton.setAlignment(Pos.CENTER);
        disapproveButton.setStyle(IDLE_BUTTON_STYLE2);
        disapproveButton.setOnMouseEntered(e -> disapproveButton.setStyle(HOVERED_BUTTON_STYLE2));
        disapproveButton.setOnMouseExited(e -> disapproveButton.setStyle(IDLE_BUTTON_STYLE2));
        disapproveButton.setOnMouseClicked(e -> disapproveButton.setStyle(CLICKED_BUTTON_STYLE2));
        disapproveButton.setMinWidth(30);
        Label exceedsQuotaLabel = new Label(request.exceedsQuota() ? "Yes" : "No");
        exceedsQuotaLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12)); // Bold font
        exceedsQuotaLabel.setTextFill(Color.BLACK);

        requestNode.getChildren().addAll(nameLabel, dateCreatedLabel, startDateLabel, endDateLabel, reasonLabel, approveButton, disapproveButton, exceedsQuotaLabel);
        return requestNode;
    }

    private void updateUI() {
        readLeaveRequestsFromFile("D:\\LancastersKitchenMgmt\\software\\src\\scenes\\utils\\leaves.csv");
        leaveRequestsContainer.getChildren().clear(); // Clear the existing UI elements
        leaveRequestsContainer.getChildren().add(createColumnNames()); // Add column names

        // Add leave request nodes to the container
        for (LeaveRequest request : leaveRequests) {
            if (!request.isApproved()) {
                leaveRequestsContainer.getChildren().add(createLeaveRequestNode(request));
            }
        }
    }

    private void removeLeaveRequestNode(LeaveRequest request) {
        Iterator<LeaveRequest> iterator = leaveRequests.iterator();
        while (iterator.hasNext()) {
            LeaveRequest leaveRequest = iterator.next();
            if (leaveRequest.equals(request)) {
                iterator.remove();
                break;
            }
        }
    }

    private void saveLeaveRequestsToFile(String filename, List<LeaveRequest> requests) {
        String approvedLeavesFile = "D:\\LancastersKitchenMgmt\\software\\src\\scenes\\utils\\approved_leaves.csv";
        try (FileWriter writer = new FileWriter(approvedLeavesFile, true)) { // Use append mode
            for (LeaveRequest request : requests) {
                writer.write(request.toCSVFormat() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (FileWriter writer = new FileWriter(filename)) {
            for (LeaveRequest request : requests) {
                if (!request.isApproved()) {
                    writer.write(request.toCSVFormat() + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

class LeaveRequest {
    private static final int DAYS_IN_YEAR = 365; // Assuming non-leap year
    private String employeeName;
    private String dateCreated;
    private String startDate;
    private String endDate;
    private String reason;
    private boolean approved;

    public LeaveRequest(String employeeName, String dateCreated, String startDate, String endDate, String reason) {
        this.employeeName = employeeName;
        this.dateCreated = dateCreated;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reason = reason;
        this.approved = false; // Default to not approved
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getReason() {
        return reason;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public boolean exceedsQuota() {
        int totalLeaveDays = calculateLeaveDays(startDate, endDate);
        return totalLeaveDays > SickHolidayRequests.QUOTA;
    }

    public String toCSVFormat() {
        return employeeName + ";" + dateCreated + ";" + startDate + ";" + endDate + ";" + reason + ";" + approved;
    }

    private int calculateLeaveDays(String startDate, String endDate) {
        // Parse start and end dates to calculate the difference in days
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate start = LocalDate.parse(startDate, formatter);
        LocalDate end = LocalDate.parse(endDate, formatter);

        // Calculate the difference in days
        return (int) ChronoUnit.DAYS.between(start, end) + 1;
    }
}
