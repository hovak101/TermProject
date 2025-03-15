package s25.cs151.termproject;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class CreateOfficeHoursController {

    private SceneController sceneController;

    @FXML private Button cancelButton;
    @FXML private Button continueButton;

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
                // Future logic for handling form submission
            });
        }
    }
}
