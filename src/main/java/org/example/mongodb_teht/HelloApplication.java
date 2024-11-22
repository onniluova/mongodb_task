package org.example.mongodb_teht;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    private HelloController controller;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        controller = fxmlLoader.getController();

        stage.setTitle("MongoDB CRUD App");
        stage.setScene(scene);
        stage.show();

        // Handle application shutdown
        stage.setOnCloseRequest(event -> {
            if (controller != null) {
                controller.close();
            }
            Platform.exit();
        });
    }

    @Override
    public void stop() {
        if (controller != null) {
            controller.close();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}