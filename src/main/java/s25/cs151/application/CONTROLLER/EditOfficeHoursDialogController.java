package s25.cs151.application.CONTROLLER;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import s25.cs151.application.MODEL.SemesterOfficeHourBean;

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

    private SemesterOfficeHourBean officeHours;
    private ManageOfficeHoursController parentController;

    public void setOfficeHours(SemesterOfficeHourBean officeHours) {
        this.officeHours = officeHours;
        // Populate the fields with current values
        semesterComboBox.setValue(officeHours.getSemester());

        yearTextField.setText(String.valueOf(officeHours.getYear()));
        
        // Parse the days string and set checkboxes
        String days = officeHours.getDays();
        if (days != null && days.length() >= 5) {
            if (days.charAt(0) == '1') mondayCheckBox.setSelected(true);
            if (days.charAt(1) == '1') tuesdayCheckBox.setSelected(true);
            if (days.charAt(2) == '1') wednesdayCheckBox.setSelected(true);
            if (days.charAt(3) == '1') thursdayCheckBox.setSelected(true);
            if (days.charAt(4) == '1') fridayCheckBox.setSelected(true);
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
            int year = Integer.parseInt(yearTextField.getText());
            String days = getSelectedDays();

            officeHours.setSemester(semester);
            officeHours.setYear(year);
            officeHours.setDays(days);
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
        StringBuilder sb = new StringBuilder("00000");
        if (mondayCheckBox.isSelected()) sb.setCharAt(0, '1');
        if (tuesdayCheckBox.isSelected()) sb.setCharAt(1, '1');
        if (wednesdayCheckBox.isSelected()) sb.setCharAt(2, '1');
        if (thursdayCheckBox.isSelected()) sb.setCharAt(3, '1');
        if (fridayCheckBox.isSelected()) sb.setCharAt(4, '1');
        return sb.toString();
    }
} 