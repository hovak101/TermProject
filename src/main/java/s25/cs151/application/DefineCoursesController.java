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
                System.out.println("Continue button clicked!");
                String courseCode = courseCodeTextField.getText();
                String courseName = courseNameTextField.getText();
                String sectionNumber = sectionNumberTextField.getText();
                if (validateFields()) {
                    try {
                        dao.storeCourse(courseCode, courseName, Integer.parseInt(sectionNumber));
                    } catch (IllegalArgumentException e) {
                        errorLabel.setText(e.toString());
                        errorLabel.setVisible(true);
                    }
                }
            });
        }
    }

    private boolean validateFields() {
        StringBuilder errorMessage = new StringBuilder();
        boolean hasError = false;

        // Validate year
        String courseCode = courseCodeTextField.getText().trim();
        if (courseCode.isEmpty()) {
            errorMessage.append("Please enter a year\n");
            hasError = true;
        }

        // Validate year
        String courseName = courseNameTextField.getText().trim();
        if (courseName.isEmpty()) {
            errorMessage.append("Please enter a year\n");
            hasError = true;
        }

        // Validate year
        String sectionNumber = sectionNumberTextField.getText().trim();
        if (sectionNumber.isEmpty()) {
            errorMessage.append("Please enter a year\n");
            hasError = true;
        }

        return !hasError;
    }
}
