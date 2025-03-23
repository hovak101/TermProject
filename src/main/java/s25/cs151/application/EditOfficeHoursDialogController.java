package s25.cs151.application;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

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

    private void saveChanges() {
        String semester = semesterComboBox.getValue();
        String year = yearTextField.getText();

        List<String> selectedDays = new ArrayList<>();
        if (mondayCheckBox.isSelected()) selectedDays.add("Monday");
        if (tuesdayCheckBox.isSelected()) selectedDays.add("Tuesday");
        if (wednesdayCheckBox.isSelected()) selectedDays.add("Wednesday");
        if (thursdayCheckBox.isSelected()) selectedDays.add("Thursday");
        if (fridayCheckBox.isSelected()) selectedDays.add("Friday");

        String days = String.join(", ", selectedDays);
        
        // Update the office hours object
        officeHours.update(semester, year, days);
        
        // Notify parent controller to refresh the table
        parentController.refreshTable();
        
        // Close the dialog
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }
} 