package s25.cs151.application;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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

    @FXML private TableView<OfficeHours> officeHoursTable;
    @FXML private TableColumn<OfficeHours, String> semesterColumn;
    @FXML private TableColumn<OfficeHours, String> yearColumn;
    @FXML private TableColumn<OfficeHours, String> daysColumn;
    @FXML private Button backButton;
    @FXML private Button editButton;
    @FXML private Button deleteButton;

    private ObservableList<OfficeHours> officeHoursList = FXCollections.observableArrayList();

    public void setSceneController(SceneController sceneController) {
        this.sceneController = sceneController;
    }

    @FXML
    public void initialize() {
        // Set up table columns
        semesterColumn.setCellValueFactory(new PropertyValueFactory<>("semester"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        daysColumn.setCellValueFactory(new PropertyValueFactory<>("days"));

        // Load office hours data
        loadOfficeHours();

        // Set up button handlers
        backButton.setOnAction(event -> handleBackButton());
        editButton.setOnAction(event -> handleEditButton());
        deleteButton.setOnAction(event -> handleDeleteButton());
    }

    private void loadOfficeHours() {
        try (BufferedReader br = new BufferedReader(new FileReader(CreateOfficeHoursController.FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length >= 3) {
                    String year = values[0];
                    String semester = values[1];
                    String days = formatDays(values[2]);
                    officeHoursList.add(new OfficeHours(semester, year, days));
                }
            }
            officeHoursTable.setItems(officeHoursList);
        } catch (IOException e) {
            System.err.println("Error reading office hours file: " + e.getMessage());
            e.printStackTrace();
        }
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
    private void handleBackButton() {
        sceneController.switchScene("Home.fxml");
    }

    @FXML
    private void handleEditButton() {
        OfficeHours selected = officeHoursTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            showEditDialog(selected);
        } else {
            showAlert("Please select an office hours entry to edit.");
        }
    }

    private void showEditDialog(OfficeHours officeHours) {
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
        OfficeHours selected = officeHoursTable.getSelectionModel().getSelectedItem();
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
                    saveAllOfficeHours();
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
        saveAllOfficeHours();
        // Reload the table
        officeHoursList.clear();
        loadOfficeHours();
    }

    private void saveAllOfficeHours() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(CreateOfficeHoursController.FILE_NAME, false))) {
            for (OfficeHours oh : officeHoursList) {
                String days = convertDaysToBinary(oh.getDays());
                pw.println(oh.getYear() + "," + oh.getSemester() + "," + days);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String convertDaysToBinary(String daysString) {
        String[] days = daysString.split(", ");
        StringBuilder binary = new StringBuilder("00000");
        for (String day : days) {
            switch (day) {
                case "Monday": binary.setCharAt(0, '1'); break;
                case "Tuesday": binary.setCharAt(1, '1'); break;
                case "Wednesday": binary.setCharAt(2, '1'); break;
                case "Thursday": binary.setCharAt(3, '1'); break;
                case "Friday": binary.setCharAt(4, '1'); break;
            }
        }
        return binary.toString();
    }

    // Data model class for office hours
    public static class OfficeHours {
        private String semester;
        private String year;
        private String days;

        public OfficeHours(String semester, String year, String days) {
            this.semester = semester;
            this.year = year;
            this.days = days;
        }

        public String getSemester() { return semester; }
        public String getYear() { return year; }
        public String getDays() { return days; }

        public void update(String semester, String year, String days) {
            this.semester = semester;
            this.year = year;
            this.days = days;
        }
    }
} 