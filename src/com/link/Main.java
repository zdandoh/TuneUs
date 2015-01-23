package com.link;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.stage.Stage;

public class Main extends Application {
    public static final double VERSION_NUMBER = 1.0;
    public static final boolean DEV = true;
    public static Session session = new Session();
    public static Player player = new Player();
    public static Queue queue = new Queue();
    public static Parent root;
    @Override
    public void start(Stage primaryStage) throws Exception{
        System.out.printf("TuneUs v%s\n", String.valueOf(VERSION_NUMBER));
        root = FXMLLoader.load(getClass().getResource("/com/link/resources/gui/login.fxml"));
        primaryStage.setTitle("TuneUs");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        Thread update_thread = new Thread(){
            public void run(){
                AutoUpdate.update();
            }
        };
        update_thread.start();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void sleep(long ms){
        try {
            Thread.sleep(ms);
        }
        catch(InterruptedException e){
            System.out.println("Sleep interrupt");
        }
    }
}