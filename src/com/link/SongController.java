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

import java.io.IOException;
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

                FileChooser.ExtensionFilter mFilter = new FileChooser.ExtensionFilter("MP3 files (*.mp3)", "*.mp3");
                FileChooser.ExtensionFilter fFilter = new FileChooser.ExtensionFilter("FLAC files (*.flac)", "*.flac");

                fileChooser.getExtensionFilters().add(fFilter);
                fileChooser.getExtensionFilters().add(mFilter);

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
        file = new File(dirText.getText());
        Stage stage = (Stage) tubeUrl.getScene().getWindow();

        if(dirText.getText().length() > 0){
            if(!file.exists()){
                ErrorDialog dialog = new ErrorDialog("File Error", "The selected file does not exist!", 175, 100);
                dialog.show();
            }
            else{
                stage.close();
                //Do something with file
                try {
                    Main.session.uploadBlob(dirText.getText(), Main.session.session_id);
                    Main.queue.addSong(dirText.getText());
                }
                catch(IOException e) {
                    ErrorDialog dialog = new ErrorDialog("Upload Error", "Failed to upload file.", 175, 100);
                    dialog.show();
                }
            }
        }
        else if (tubeUrl.getText().length() > 0) {
            if (tubeUrl.getText().startsWith("https://www.youtube.com/")) {
                //do stuff with url
                String tube_id[] = tubeUrl.getText().split("v=");
                String data = String.format("?id=%s&video=%s", Main.session.session_id,tube_id[1]);
                Main.session.readPage(Main.session.getUrl("ADD_VIDEO") + data);
                stage.close();
            }
            else{
                ErrorDialog dialog = new ErrorDialog("URL Error", "The entered URL is not valid", 175, 100);
                dialog.show();
            }
        }
        else{ErrorDialog dialog = new ErrorDialog("Error", "Select a file from your computer, or load a song from YouTube", 225, 100);
            dialog.show();}
    }

}
