package com.link;

import javafx.event.EventHandler;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
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
    String user = String.format("<%s> ", LoginController.nickname);
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        id_box.setText(Main.session.session_id);
        // Setup chat bar event handler
        chat_bar.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    if (!chat_bar.getText().trim().isEmpty()) {
                        chat_history.appendText(user + chat_bar.getText() + "\n");
                        String message = chat_bar.getText();
                        chat_bar.clear();
                        //Do stuff with the sent chat message
                        String data = String.format("?id=%s&message=%s", Main.session.session_id, message);
                        Main.session.readPage(Main.session.getUrl("SEND_MESSAGE") + data);
                    }
                }
            }
        });
    }

    @FXML
    private TextArea id_box;

    @FXML
    private TextField chat_bar;

    @FXML
    private TextArea chat_history;

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
