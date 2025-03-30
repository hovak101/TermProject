package s25.cs151.application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditCourseDialogController {
    @FXML private TextField courseCodeTextField;
    @FXML private TextField courseNameTextField;
    @FXML private TextField sectionNumberTextField;
    @FXML private Button cancelButton;
    @FXML private Button saveButton;
    @FXML private Label errorLabel;

    private CourseBean course;
    private ViewCoursesController parentController;

    public void setCourse(CourseBean course) {
        this.course = course;
        // Populate the fields with current values
        courseCodeTextField.setText(course.getCourseCode());
        courseNameTextField.setText(course.getCourseName());
        sectionNumberTextField.setText(course.getSectionNumber());
    }

    public void setParentController(ViewCoursesController parentController) {
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
        if (validateFields()) {
            String courseCode = courseCodeTextField.getText().trim();
            String courseName = courseNameTextField.getText().trim();
            String sectionNumber = sectionNumberTextField.getText().trim();

            course.setCourseCode(courseCode);
            course.setCourseName(courseName);
            course.setSectionNumber(sectionNumber);
            
            parentController.refreshTable();

            // Close the dialog
            Stage stage = (Stage) saveButton.getScene().getWindow();
            stage.close();
        }
    }

    private boolean validateFields() {
        StringBuilder errorMessage = new StringBuilder();
        boolean hasError = false;

        // Validate course code
        String courseCode = courseCodeTextField.getText().trim();
        if (courseCode.isEmpty()) {
            errorMessage.append("Please enter a course code\n");
            hasError = true;
        }

        // Validate course name
        String courseName = courseNameTextField.getText().trim();
        if (courseName.isEmpty()) {
            errorMessage.append("Please enter a course name\n");
            hasError = true;
        }

        // Validate section number
        String sectionNumber = sectionNumberTextField.getText().trim();
        if (sectionNumber.isEmpty()) {
            errorMessage.append("Please enter a section number\n");
            hasError = true;
        } else if (!sectionNumber.matches("\\d+")) {
            errorMessage.append("Section number must contain only numbers\n");
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