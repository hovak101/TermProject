package s25.cs151.application;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class CreateOfficeHoursController {

    private SceneController sceneController;

    @FXML private Button cancelButton;
    @FXML private Button continueButton;
    @FXML private TextField yearTextField;
    @FXML private ComboBox<String> semesterComboBox;
    @FXML private CheckBox mondayCheckBox;
    @FXML private CheckBox tuesdayCheckBox;
    @FXML private CheckBox wednesdayCheckBox;
    @FXML private CheckBox thursdayCheckBox;
    @FXML private CheckBox fridayCheckBox;

    public static final String FILE_NAME = "OfficeHours.csv";

    public void setSceneController(SceneController sceneController) {
        this.sceneController = sceneController;
    }

    @FXML
    public void initialize() {
        System.out.println("Cancel Button: " + cancelButton); // Debugging line
        System.out.println("Continue Button: " + continueButton); // Debugging line

        if (cancelButton != null) {
            cancelButton.setOnAction(event -> {
                System.out.println("cancel clicked");
                sceneController.switchScene("Home.fxml");
            });
        }

        if (continueButton != null) {
            continueButton.setOnAction(event -> {
                System.out.println("Continue button clicked!");
                saveOfficeHours();
                // Future logic for handling form submission
            });
        }
    }

    private void saveOfficeHours() {
        String semester = semesterComboBox.getValue();
        String year = yearTextField.getText();

        List<String> selectedDays = new ArrayList<>();
        if (mondayCheckBox.isSelected()) {
            selectedDays.add("1");
        } else {
            selectedDays.add("0");
        }
        if (tuesdayCheckBox.isSelected()) {
            selectedDays.add("1");
        } else {
            selectedDays.add("0");
        }
        if (wednesdayCheckBox.isSelected()) {
            selectedDays.add("1");
        } else {
            selectedDays.add("0");
        }
        if (thursdayCheckBox.isSelected()) {
            selectedDays.add("1");
        } else {
            selectedDays.add("0");
        }
        if (fridayCheckBox.isSelected()) {
            selectedDays.add("1");
        } else {
            selectedDays.add("0");
        }

        writeToCSV(semester, year, selectedDays);
    }

    private void writeToCSV(String semester, String year, List<String> selectedDays) {
        try (FileWriter fw = new FileWriter(FILE_NAME, true); // true for append mode
             PrintWriter pw = new PrintWriter(fw)) {
            String days = String.join("", selectedDays);
            pw.println(year + "," + semester + "," + days);
            System.out.println("Successfully wrote to CSV file");
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
