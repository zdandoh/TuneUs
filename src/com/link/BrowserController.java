package com.link;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.text.TableView;
import java.io.File;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Chris on 9/3/2014.
 */
public class BrowserController implements Initializable {

    @FXML private Button joinButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    private void joinAction() {
        Parent root;
        Stage stage = (Stage) joinButton.getScene().getWindow();

        try {
            //assign session id
            stage.close();
            root = FXMLLoader.load(getClass().getResource("/com/link/resources/gui/browser.fxml"));
            stage = new Stage();
            stage.setTitle("TuneUs");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}