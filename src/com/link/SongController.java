package com.link;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Chris on 8/26/2014.
 */
public class SongController implements Initializable {
    File file;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        browseButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();

                //Set extension filter

                FileChooser.ExtensionFilter mFilter = new FileChooser.ExtensionFilter("MP3 files (*.mp3)", "*.mp3");
                FileChooser.ExtensionFilter fFilter = new FileChooser.ExtensionFilter("FLAC files (*.flac)", "*.flac");

                fileChooser.getExtensionFilters().add(fFilter);
                fileChooser.getExtensionFilters().add(mFilter);

                //Show open file dialog
                file = fileChooser.showOpenDialog(null);

                dirText.setText(file.getPath());
            }
        });
    }
    @FXML
    private Button browseButton;

    @FXML
    private TextField dirText;

    @FXML
    private TextField tubeUrl;

    @FXML
    private void addSong(){
        if(!(dirText.getText() == "")){
            System.out.println("Import");
        }
        else if (!(tubeUrl.getText() == "")){
            //Do stuff to fetch song from YouTube
            System.out.println("YouTube");
        }

        Stage stage = (Stage) tubeUrl.getScene().getWindow();
        stage.close();

    }


}
