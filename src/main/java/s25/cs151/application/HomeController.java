package s25.cs151.application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class HomeController {

    private SceneController sceneController;

    @FXML private Button createOfficeHoursButton;
    @FXML private Button manageCurrentHoursButton;
    @FXML private Button viewAppointmentsButton;
    @FXML private Button defineCoursesButton;
    @FXML private Button viewCoursesButton;

    public void setSceneController(SceneController sceneController) {
        this.sceneController = sceneController;
    }

    @FXML
    public void initialize() {
        // Ensure the button is wired properly
        if (createOfficeHoursButton != null) {
            createOfficeHoursButton.setOnAction(event -> {
                sceneController.switchScene("CreateOfficeHours.fxml");
            });
        }

        if (manageCurrentHoursButton != null) {
            manageCurrentHoursButton.setOnAction(event -> {
                System.out.println("Manage Current Office Hours Clicked");
                sceneController.switchScene("ManageOfficeHours.fxml");
            });
        }

        if (viewAppointmentsButton != null) {
            viewAppointmentsButton.setOnAction(event -> {
                System.out.println("View My Appointments Clicked");
            });
        }

        if (defineCoursesButton != null) {
            defineCoursesButton.setOnAction(actionEvent -> {
                System.out.println("Define Courses Clicked");
                sceneController.switchScene("DefineCourses.fxml");
            });
        }

        if (viewCoursesButton != null) {
            viewCoursesButton.setOnAction(event -> {
                System.out.println("View Courses Clicked");
                sceneController.switchScene("ViewCourses.fxml");
            });
        }
    }
}