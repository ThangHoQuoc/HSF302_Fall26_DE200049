package edu.fpt.swt301.demologin;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 980, 600);
        stage.setTitle("Demo Login");
        stage.setScene(scene);
        stage.setMinWidth(800);
        stage.setMinHeight(560);
        stage.centerOnScreen();
        stage.show();
    }
}
