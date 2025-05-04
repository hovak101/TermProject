package s25.cs151.application.CONTROLLER;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class HomeController {

    private SceneController sceneController;

    @FXML private Button createOfficeHoursButton;
    @FXML private Button manageCurrentHoursButton;
    @FXML private Button setTimeSlotsButton;
    @FXML private Button viewTimeSlotsButton;
    
    /**
     * Button for viewing appointment/office hours schedules.
     * Navigates to the ViewAppointments view which displays all appointments
     * in a table sorted by date and time.
     */
    @FXML private Button viewAppointmentsButton;
    
    /**
     * Button for defining new appointment/office hours schedules.
     * Navigates to the DefineAppointments view which allows faculty to
     * enter appointment details including student name, date, time slot, etc.
     */
    @FXML private Button defineAppointmentsButton;
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
                sceneController.switchScene("VIEW/CreateOfficeHours.fxml");
            });
        }

        if (manageCurrentHoursButton != null) {
            manageCurrentHoursButton.setOnAction(event -> {
                System.out.println("Manage Current Office Hours Clicked");
                sceneController.switchScene("VIEW/ManageOfficeHours.fxml");
            });
        }

        if (setTimeSlotsButton != null) {
            setTimeSlotsButton.setOnAction(event -> {
                System.out.println("Define Semester's Time Slots Clicked");
                sceneController.switchScene("VIEW/SetTimeSlots.fxml");
            });
        }

        if (viewTimeSlotsButton != null) {
            viewTimeSlotsButton.setOnAction(event -> {
                System.out.println("View Semester's Time Slots Clicked");
                sceneController.switchScene("VIEW/ViewTimeSlots.fxml");
            });
        }

        if (viewAppointmentsButton != null) {
            viewAppointmentsButton.setOnAction(event -> {
                System.out.println("View My Appointments Clicked");
                sceneController.switchScene("VIEW/ViewAppointments.fxml");
            });
        }

        if (defineAppointmentsButton != null) {
            defineAppointmentsButton.setOnAction(event -> {
                System.out.println("Define My Appointments Clicked");
                sceneController.switchScene("VIEW/DefineAppointments.fxml");
            });
        }

        if (defineCoursesButton != null) {
            defineCoursesButton.setOnAction(actionEvent -> {
                System.out.println("Define Courses Clicked");
                sceneController.switchScene("VIEW/DefineCourses.fxml");
            });
        }

        if (viewCoursesButton != null) {
            viewCoursesButton.setOnAction(event -> {
                System.out.println("View Courses Clicked");
                sceneController.switchScene("VIEW/ViewCourses.fxml");
            });
        }
    }
}