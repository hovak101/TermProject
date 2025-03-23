package s25.cs151.application;

import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditOfficeHoursDialogController {
    @FXML private ComboBox<String> semesterComboBox;
    @FXML private TextField yearTextField;
    @FXML private CheckBox mondayCheckBox;
    @FXML private CheckBox tuesdayCheckBox;
    @FXML private CheckBox wednesdayCheckBox;
    @FXML private CheckBox thursdayCheckBox;
    @FXML private CheckBox fridayCheckBox;
    @FXML private Button cancelButton;
    @FXML private Button saveButton;
    @FXML private Label errorLabel;

    private ManageOfficeHoursController.OfficeHours officeHours;
    private ManageOfficeHoursController parentController;

    public void setOfficeHours(ManageOfficeHoursController.OfficeHours officeHours) {
        this.officeHours = officeHours;
        // Populate the fields with current values
        semesterComboBox.setValue(officeHours.getSemester());
        yearTextField.setText(officeHours.getYear());
        
        // Parse the days string and set checkboxes
        String days = officeHours.getDays();
        if (days != null) {
            String[] dayArray = days.split(", ");
            for (String day : dayArray) {
                switch (day) {
                    case "Monday": mondayCheckBox.setSelected(true); break;
                    case "Tuesday": tuesdayCheckBox.setSelected(true); break;
                    case "Wednesday": wednesdayCheckBox.setSelected(true); break;
                    case "Thursday": thursdayCheckBox.setSelected(true); break;
                    case "Friday": fridayCheckBox.setSelected(true); break;
                }
            }
        }
    }

    public void setParentController(ManageOfficeHoursController parentController) {
        this.parentController = parentController;
    }

    @FXML
    public void initialize() {
        cancelButton.setOnAction(event -> {
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        });

        saveButton.setOnAction(event -> {
            saveChanges();
        });
    }

    @FXML
    private void saveChanges() {
        if (validateFields()) {
            String semester = semesterComboBox.getValue();
            String year = yearTextField.getText();
            String days = getSelectedDays();

            officeHours.update(semester, year, days);
            parentController.refreshTable();

            // Close the dialog
            Stage stage = (Stage) saveButton.getScene().getWindow();
            stage.close();
        }
    }

    private boolean validateFields() {
        StringBuilder errorMessage = new StringBuilder();
        boolean hasError = false;

        // Validate semester
        if (semesterComboBox.getValue() == null) {
            errorMessage.append("Please select a semester\n");
            hasError = true;
        }

        // Validate year
        String year = yearTextField.getText().trim();
        if (year.isEmpty()) {
            errorMessage.append("Please enter a year\n");
            hasError = true;
        } else if (!year.matches("\\d{4}")) {
            errorMessage.append("Year must be a 4-digit number\n");
            hasError = true;
        }

        // Validate days
        if (!mondayCheckBox.isSelected() && !tuesdayCheckBox.isSelected() && 
            !wednesdayCheckBox.isSelected() && !thursdayCheckBox.isSelected() && 
            !fridayCheckBox.isSelected()) {
            errorMessage.append("Please select at least one day");
            hasError = true;
        }

        if (hasError) {
            errorLabel.setText(errorMessage.toString());
            errorLabel.setVisible(true);
        } else {
            errorLabel.setVisible(false);
        }

        return !hasError;
    }

    private String getSelectedDays() {
        List<String> selectedDays = new ArrayList<>();
        if (mondayCheckBox.isSelected()) selectedDays.add("Monday");
        if (tuesdayCheckBox.isSelected()) selectedDays.add("Tuesday");
        if (wednesdayCheckBox.isSelected()) selectedDays.add("Wednesday");
        if (thursdayCheckBox.isSelected()) selectedDays.add("Thursday");
        if (fridayCheckBox.isSelected()) selectedDays.add("Friday");

        return String.join(", ", selectedDays);
    }
} 