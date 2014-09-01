package com.link;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable{
    String session_id = "";
    static String nickname = "";
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nameField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {

                if (newValue.length() < 3 && newValue.length() > 0){errorLabel.setText("Username too short!");}
                else if (newValue.length() > 16){errorLabel.setText("Username too long!");}
                else if(!newValue.matches("^[A-Za-z0-9_-]*$")){errorLabel.setText("Username not valid");}
                else {errorLabel.setText("");}
            }
        });
    }

    @FXML
    private TextField sessionField;

    @FXML
    private TextField nameField;

    @FXML
    private Label errorLabel;

    @FXML
    private Button joinButton;

    @FXML
    private void joinAction(){
        Parent root;
        Stage stage = (Stage) joinButton.getScene().getWindow();

        nickname = nameField.getText();
        if(nickname.length() > 0 && errorLabel.getText().length() == 0){
            try {
                Main.session.session_id = sessionField.getText();
                stage.close();
                root = FXMLLoader.load(getClass().getResource("/com/link/resources/gui/main.fxml"));
                stage = new Stage();
                stage.setTitle("TuneUs");
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {e.printStackTrace();}
        }
        else {
                 ErrorDialog dialog = new ErrorDialog("Error", "Invalid Nickname!", 175, 100);
                 dialog.show();
             }

    }

    @FXML
    private void createAction() {
        nickname = nameField.getText();
        if(nickname.length() > 0 && errorLabel.getText().length() == 0){
            Main.session.session_id = Main.session.readPage(Main.session.getUrl("CREATE_SESSION") + "?user=" + nickname).replace("\n", "");
            sessionField.setText(Main.session.session_id);
            joinAction();
        }
    }

}
