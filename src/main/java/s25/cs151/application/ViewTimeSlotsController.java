package s25.cs151.application;

import java.io.IOException;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ViewTimeSlotsController {
    private SceneController sceneController;
    private SemesterTimeSlotsDaoInt timeSlotsDao;
    private ObservableList<SemesterTimeSlotsBean> timeSlotsList;

    @FXML private TableView<SemesterTimeSlotsBean> timeSlotsTable;
    @FXML private TableColumn<SemesterTimeSlotsBean, String> fromHourColumn;
    @FXML private TableColumn<SemesterTimeSlotsBean, String> toHourColumn;
    @FXML private Button backButton;
    @FXML private Button editButton;
    @FXML private Button deleteButton;

    public void setSceneController(SceneController sceneController) {
        this.sceneController = sceneController;
    }

    @FXML
    public void initialize() {
        // Set up table columns
        fromHourColumn.setCellValueFactory(new PropertyValueFactory<>("fromHour"));
        toHourColumn.setCellValueFactory(new PropertyValueFactory<>("toHour"));

        // Initialize DAO
        timeSlotsDao = new SemesterTimeSlotsDao();

        // Load time slots data
        loadTimeSlots();

        // Set up button handlers
        backButton.setOnAction(event -> handleBackButton());
        editButton.setOnAction(event -> handleEditButton());
        deleteButton.setOnAction(event -> handleDeleteButton());
    }

    private void loadTimeSlots() {
        List<SemesterTimeSlotsBean> timeSlots = timeSlotsDao.getTimeSlots();
        timeSlotsList = FXCollections.observableList(timeSlots);
        timeSlotsTable.setItems(timeSlotsList);
    }

    @FXML
    private void handleBackButton() {
        sceneController.switchScene("VIEW/Home.fxml");
    }

    @FXML
    private void handleEditButton() {
        SemesterTimeSlotsBean selected = timeSlotsTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            showEditDialog(selected);
        } else {
            showAlert("Action Required", "Please select a time slot to edit.");
        }
    }

    @FXML
    private void handleDeleteButton() {
        SemesterTimeSlotsBean selected = timeSlotsTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            // Create and style the confirmation dialog
            Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmDialog.setTitle("Confirm Deletion");
            confirmDialog.setHeaderText("Delete Time Slot");
            confirmDialog.setContentText("Are you sure you want to delete the following time slot?\n• " + 
                selected.getFromHour() + " to " + selected.getToHour());

            // Style the dialog
            DialogPane dialogPane = confirmDialog.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("style/styles.css").toExternalForm());
            dialogPane.getStyleClass().add("custom-alert");
            
            // Style the buttons
            Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
            okButton.getStyleClass().add("secondary-button");
            
            Button cancelButton = (Button) dialogPane.lookupButton(ButtonType.CANCEL);
            cancelButton.getStyleClass().add("main-button");

            confirmDialog.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    // Remove from the observable list
                    timeSlotsList.remove(selected);
                    // Save changes
                    saveTimeSlots();
                    // Show success message
                    showAlert("Success", "Time slot deleted successfully.");
                }
            });
        } else {
            showAlert("Action Required", "Please select a time slot to delete.");
        }
    }

    private void showEditDialog(SemesterTimeSlotsBean timeSlot) {
        // TODO: Implement edit dialog
        showAlert("Not Implemented", "Edit functionality will be implemented in a future update.");
    }

    private void showAlert(String headerText, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(headerText);
        alert.setContentText(message);

        // Style the alert
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("style/styles.css").toExternalForm());
        dialogPane.getStyleClass().add("custom-alert");

        // Style the OK button
        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        okButton.getStyleClass().add("main-button");

        alert.showAndWait();
    }

    private void saveTimeSlots() {
        try {
            // Clear the file and write all time slots
            java.io.PrintWriter writer = new java.io.PrintWriter(SemesterTimeSlotsDao.FILE_NAME);
            for (SemesterTimeSlotsBean timeSlot : timeSlotsList) {
                writer.println(timeSlot.getFromHour() + "-" + timeSlot.getToHour());
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Error saving time slots: " + e.getMessage());
        }
    }
} 