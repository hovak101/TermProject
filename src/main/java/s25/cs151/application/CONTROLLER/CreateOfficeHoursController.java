package s25.cs151.application.CONTROLLER;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import s25.cs151.application.MODEL.SemesterOfficeHourDao;
import s25.cs151.application.MODEL.SemesterOfficeHourDaoInt;

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
    @FXML private Label errorLabel;

    private SemesterOfficeHourDaoInt dao;

    public void setSceneController(SceneController sceneController) {
        this.sceneController = sceneController;
    }

    @FXML
    public void initialize() {
        System.out.println("Cancel Button: " + cancelButton); // Debugging line
        System.out.println("Continue Button: " + continueButton); // Debugging line
        this.dao = new SemesterOfficeHourDao();

        if (cancelButton != null) {
            cancelButton.setOnAction(event -> {
                System.out.println("cancel clicked");
                sceneController.switchScene("VIEW/Home.fxml");
            });
        }

        if (continueButton != null) {
            continueButton.setOnAction(event -> {
                String semester = semesterComboBox.getValue();
                int year = Integer.parseInt(yearTextField.getText());
                if (validateFields()) {
                    try {
                        dao.storeSemesterOfficeHours(semester, year, mondayCheckBox.isSelected(),
                                tuesdayCheckBox.isSelected(), wednesdayCheckBox.isSelected(), thursdayCheckBox.isSelected(),
                                fridayCheckBox.isSelected());
                        // If successful, go back to home page
                        sceneController.switchScene("VIEW/Home.fxml");
                    } catch (IllegalArgumentException e) {
                        errorLabel.setText("Office hours for " + semester + " " + year + " already exists.");
                        errorLabel.setVisible(true);
                    }
                }
            });
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
}
