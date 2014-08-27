package com.link;

import javafx.event.EventHandler;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Chris on 8/25/2014.
 */
public class MainController implements Initializable {

    @Override
                    public void initialize(URL location, ResourceBundle resources) {
                chat_bar.setOnKeyPressed(new EventHandler<KeyEvent>()
                {
                    @Override
                    public void handle(KeyEvent ke)
                    {
                        if (ke.getCode().equals(KeyCode.ENTER))

                {
                    //Do stuff with the sent chat message
                }
            }
        });
    }


    @FXML
    private TextField chat_bar;

    @FXML
    private void addSong(){
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/com/link/resources/gui/add_song.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Add Song");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {e.printStackTrace();}
    }

}
