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
    @FXML private TextField courseTextField;
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
        

        if (cancelButton != null) {
            cancelButton.setOnAction(event -> {
                System.out.println("cancel clicked");
                sceneController.switchScene("Home.fxml");
            });
        }

        if (continueButton != null) {
            continueButton.setOnAction(event -> {
                System.out.println("Continue button clicked!");
                if (validateFields()) {
                    saveOfficeHours();
                }
            });
        }
    }

    private boolean validateFields() {
        StringBuilder errorMessage = new StringBuilder();
        boolean hasError = false;

        // Validate year
        String course = courseTextField.getText().trim();
        if (course.isEmpty()) {
            errorMessage.append("Please enter a year\n");
            hasError = true;
        }

        return !hasError;
    }

    private void saveOfficeHours() {
        String course = courseTextField.getText();
        writeToCSV(course);
    }

    private void writeToCSV(String course) {
        try (FileWriter fw = new FileWriter(FILE_NAME, true); // true for append mode
             PrintWriter pw = new PrintWriter(fw)) {
            pw.println(course);
            System.out.println("Successfully wrote to CSV file");
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
