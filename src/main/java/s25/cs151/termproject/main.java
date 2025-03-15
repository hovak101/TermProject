package s25.cs151.termproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class main extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        SceneController sceneController = new SceneController(primaryStage);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Home.fxml"));
        Parent root = loader.load();

        // Inject SceneController into HomeController
        HomeController homeController = loader.getController();
        homeController.setSceneController(sceneController);

        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Faculty Hours");
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}