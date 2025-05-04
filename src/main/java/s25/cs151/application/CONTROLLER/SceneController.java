package s25.cs151.application.CONTROLLER;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import s25.cs151.application.Main;

public class SceneController {

    private Stage stage;

    public SceneController(Stage stage) {
        this.stage = stage;
    }

    public void switchScene(String fxmlFile) {
        try {
            // Use the Main class to load resources - this is important for consistent resource loading
            URL resource = Main.class.getResource(fxmlFile);
            
            if (resource == null) {
                System.err.println("Cannot find resource: " + fxmlFile);
                return;
            }
            
            System.out.println("Found resource at: " + resource);
            FXMLLoader loader = new FXMLLoader(resource);
            Parent root = loader.load();

            Object controller = loader.getController();
            System.out.println("Loaded controller: " + controller);

            // Use reflection to check if the controller has a "setSceneController" method and invoke it
            if (controller != null) {
                try {
                    Method method = controller.getClass().getMethod("setSceneController", SceneController.class);
                    method.invoke(controller, this);
                } catch (NoSuchMethodException e) {
                    // It's okay if the controller doesn't have this method
                    System.out.println("Controller does not have setSceneController method: " + controller.getClass().getName());
                }
            }

            // Create new scene with constant dimensions
            Scene newScene = new Scene(root, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
            stage.setScene(newScene);
            stage.show();
        } catch (IOException | IllegalAccessException | java.lang.reflect.InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
