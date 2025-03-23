package s25.cs151.application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class HomeController {

    private SceneController sceneController;

    @FXML private Button createOfficeHoursButton;
    @FXML private Button manageCurrentHoursButton;
    @FXML private Button viewAppointmentsButton;

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
    }
}