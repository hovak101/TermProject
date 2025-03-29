package s25.cs151.application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ManageOfficeHoursController {
    private SceneController sceneController;

    @FXML private TableView<SemesterOfficeHourBean> officeHoursTable;
    @FXML private TableColumn<SemesterOfficeHourBean, String> semesterColumn;
    @FXML private TableColumn<SemesterOfficeHourBean, String> yearColumn;
    @FXML private TableColumn<SemesterOfficeHourBean, String> daysColumn;
    @FXML private Button backButton;
    @FXML private Button editButton;
    @FXML private Button deleteButton;

    private SemesterOfficeHourDaoInt dao;
    private ObservableList<SemesterOfficeHourBean> officeHoursList;

    public void setSceneController(SceneController sceneController) {
        this.sceneController = sceneController;
    }

    private String formatDays(String daysString) {
        String[] days = daysString.split("");
        List<String> formattedDays = new ArrayList<>();
        if (days.length >= 5) {
            if (days[0].equals("1")) formattedDays.add("Monday");
            if (days[1].equals("1")) formattedDays.add("Tuesday");
            if (days[2].equals("1")) formattedDays.add("Wednesday");
            if (days[3].equals("1")) formattedDays.add("Thursday");
            if (days[4].equals("1")) formattedDays.add("Friday");
        }
        return String.join(", ", formattedDays);
    }

    @FXML
    public void initialize() {
        // Set up table columns
        semesterColumn.setCellValueFactory(new PropertyValueFactory<>("semester"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        daysColumn.setCellValueFactory(cellData -> {
            String raw = cellData.getValue().getDays(); // e.g., "10101"
            String formatted = formatDays(raw);         // e.g., "Monday, Wednesday, Friday"
            return new javafx.beans.property.SimpleStringProperty(formatted);
        });

        this.dao = new SemesterOfficeHourDao();
        // Load office hours data
        loadOfficeHours();

        // Set up button handlers
        backButton.setOnAction(event -> handleBackButton());
        editButton.setOnAction(event -> handleEditButton());
        deleteButton.setOnAction(event -> handleDeleteButton());
    }

    private void loadOfficeHours() {
        officeHoursList = FXCollections.observableList(new ArrayList<>(dao.getSemesterOfficeHours()));
            // Sort the list by year (descending) and semester
            officeHoursList.sort((oh1, oh2) -> {
                // First compare by year (descending)
                int yearCompare = Integer.compare(oh2.getYear(), oh1.getYear());
                if (yearCompare != 0) {
                    return yearCompare;
                }
                // If years are equal, compare by semester
                return getSemesterOrder(oh2.getSemester()) - getSemesterOrder(oh1.getSemester());
            });
            officeHoursTable.setItems(officeHoursList);
    }

    private int getSemesterOrder(String semester) {
        switch (semester.toLowerCase()) {
            case "spring": return 4;
            case "summer": return 3;
            case "fall": return 2;
            case "winter": return 1;
            default: return 0;
        }
    }

    @FXML
    private void handleBackButton() {
        sceneController.switchScene("Home.fxml");
    }

    @FXML
    private void handleEditButton() {
        SemesterOfficeHourBean selected = officeHoursTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            showEditDialog(selected);
        } else {
            showAlert("Please select an office hours entry to edit.");
        }
    }

    private void showEditDialog(SemesterOfficeHourBean officeHours) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("EditOfficeHoursDialog.fxml"));
            Parent root = loader.load();

            EditOfficeHoursDialogController dialogController = loader.getController();
            dialogController.setOfficeHours(officeHours);
            dialogController.setParentController(this);

            Scene scene = new Scene(root);
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Office Hours");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(officeHoursTable.getScene().getWindow());
            dialogStage.setScene(scene);
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteButton() {
        SemesterOfficeHourBean selected = officeHoursTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            // Show confirmation dialog
            Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmDialog.setTitle("Confirm Deletion");
            confirmDialog.setHeaderText("Delete Office Hours");
            confirmDialog.setContentText("Are you sure you want to delete the office hours for " +
                selected.getSemester() + " " + selected.getYear() + "?");

            confirmDialog.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    // Remove from the observable list
                    officeHoursList.remove(selected);
                    // Save changes to file
                    dao.storeSemesterOfficeHours(officeHoursList);
                    // Show success message
                    showAlert("Office hours deleted successfully.");
                }
            });
        } else {
            showAlert("Please select an office hours entry to delete.");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void refreshTable() {
        // Save all office hours back to the file
        dao.storeSemesterOfficeHours(officeHoursList);
        // Reload the table
        officeHoursList.clear();
        loadOfficeHours();
    }
} 