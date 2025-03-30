package s25.cs151.application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class DefineCoursesController {
    private SceneController sceneController;

    @FXML private Button cancelButton;
    @FXML private Button continueButton;
    @FXML private TextField courseCodeTextField;
    @FXML private TextField courseNameTextField;
    @FXML private TextField sectionNumberTextField;
    @FXML private Label errorLabel;
    private CourseDaoInt dao;

    public static final String FILE_NAME = "Courses.csv";

    public void setSceneController(SceneController sceneController) {
        this.sceneController = sceneController;
    }

    @FXML
    public void initialize() {
        System.out.println("Cancel Button: " + cancelButton); // Debugging line
        System.out.println("Continue Button: " + continueButton); // Debugging line
        this.dao = new CourseDao();

        if (cancelButton != null) {
            cancelButton.setOnAction(event -> {
                System.out.println("cancel clicked");
                sceneController.switchScene("Home.fxml");
            });
        }

        if (continueButton != null) {
            continueButton.setOnAction(event -> {
                String courseCode = courseCodeTextField.getText().trim();
                String courseName = courseNameTextField.getText().trim();
                String sectionNumber = sectionNumberTextField.getText().trim();
                if (validateFields()) {
                    try {
                        dao.storeCourse(courseCode, courseName, sectionNumber);
                        // If successful, go back to home page
                        sceneController.switchScene("Home.fxml");
                    } catch (IllegalArgumentException e) {
                        errorLabel.setText("Course " + courseCode + " section " + sectionNumber + " already exists.");
                        errorLabel.setVisible(true);
                    }
                }
            });
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
