package s25.cs151.application;

import java.io.IOException;
import java.lang.reflect.Method;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneController {

    private Stage stage;

    public SceneController(Stage stage) {
        this.stage = stage;
    }

    public void switchScene(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            Object controller = loader.getController();
            System.out.println("Loaded controller: " + controller);

            // Use reflection to check if the controller has a "setSceneController" method and invoke it
            if (controller != null) {
                Method method = controller.getClass().getMethod("setSceneController", SceneController.class);
                method.invoke(controller, this);
            }

            // Create new scene with constant dimensions
            Scene newScene = new Scene(root, main.WINDOW_WIDTH, main.WINDOW_HEIGHT);
            stage.setScene(newScene);
            stage.show();
        } catch (IOException | NoSuchMethodException | IllegalAccessException | java.lang.reflect.InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
