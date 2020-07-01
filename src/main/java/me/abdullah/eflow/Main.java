package me.abdullah.eflow;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.stage.Stage;

public class Main extends Application {

    /**
     * Creates the application
     * @param primaryStage The app's stage
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        // Loads the FXML file
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("display.fxml"));

        // Creates the scene and shows it
        primaryStage.setTitle("eFlows");
        primaryStage.setScene(new Scene(root, 1200, 1000, false, SceneAntialiasing.BALANCED));
        primaryStage.show();

        root.requestFocus();
    }

    public static void main(String[] args) {
        launch(args);
    }
}