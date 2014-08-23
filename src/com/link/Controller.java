package com.link;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.regex.*;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable{

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nameField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {

                if (newValue.length() < 3 && newValue.length() > 0){errorLabel.setText("Username too short!");}
                else if (newValue.length() > 16){errorLabel.setText("Username too long!");}
                else if(newValue.matches("^[A-Za-z0-9_-]*$") == false){errorLabel.setText("Username not valid");}
                else {errorLabel.setText("");}
            }
        });
    }

    @FXML
    private void joinAction() {}

    @FXML
    private void createAction() {

    }

    @FXML
    private TextField nameField;

    @FXML
    private Label errorLabel;

}
