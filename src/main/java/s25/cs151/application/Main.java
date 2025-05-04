package s25.cs151.application;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import s25.cs151.application.CONTROLLER.HomeController;
import s25.cs151.application.CONTROLLER.SceneController;

public class Main extends Application {
    public static final double WINDOW_WIDTH = 800;
    public static final double WINDOW_HEIGHT = 600;

    @Override
    public void start(Stage primaryStage) throws IOException {
        SceneController sceneController = new SceneController(primaryStage);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("VIEW/Home.fxml"));
        Parent root = loader.load();

        // Inject SceneController into HomeController
        HomeController homeController = loader.getController();
        homeController.setSceneController(sceneController);

        primaryStage.setScene(new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT));
        primaryStage.setTitle("Faculty Hours");
        primaryStage.setResizable(false);  // Prevent window resizing
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}