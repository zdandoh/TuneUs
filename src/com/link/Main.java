package com.link;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        primaryStage.setTitle("TuneUs");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        Session session = new Session();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
