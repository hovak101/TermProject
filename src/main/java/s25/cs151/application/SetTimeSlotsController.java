package s25.cs151.application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

public class SetTimeSlotsController {

    private SceneController sceneController;

    @FXML
    private Button cancelButton;
    @FXML private Button continueButton;
    @FXML private ComboBox<String> fromHourComboBox;
    @FXML private ComboBox<String> toHourComboBox;
    @FXML private Label errorLabel;

    private SemesterTimeSlotsDao dao;

    public void setSceneController(SceneController sceneController) {
        this.sceneController = sceneController;
    }

    @FXML
    public void initialize() {
        System.out.println("Cancel Button: " + cancelButton); // Debugging line
        System.out.println("Continue Button: " + continueButton); // Debugging line
        this.dao = new SemesterTimeSlotsDao();

        if (cancelButton != null) {
            cancelButton.setOnAction(event -> {
                System.out.println("cancel clicked");
                sceneController.switchScene("VIEW/Home.fxml");
            });
        }

        if (continueButton != null) {
            continueButton.setOnAction(event -> {
                String fromHour = fromHourComboBox.getValue();
                String toHour = toHourComboBox.getValue();
                if (validateFields()) {
                    try {
                        dao.storeTimeSlot(fromHour, toHour);
                        // If successful, go back to home page
                        sceneController.switchScene("VIEW/Home.fxml");
                    } catch (IllegalArgumentException e) {
                        errorLabel.setText("Unable to save time slots");
                        errorLabel.setVisible(true);
                    }
                }
            });
        }
    }

    private boolean validateFields() {
        StringBuilder errorMessage = new StringBuilder();
        boolean hasError = false;

        // Validate From Hour
        if (fromHourComboBox.getValue() == null) {
            errorMessage.append("Please select a from hour\n");
            hasError = true;
        }

        // Validate To Hour
        if (toHourComboBox.getValue() == null) {
            errorMessage.append("Please select a to hour\n");
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
