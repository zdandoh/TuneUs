package com.link;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.StageStyle;


/**
 * Created by Chris on 8/27/2014.
 **/


public class ErrorDialog extends Stage {

    public ErrorDialog(String title, String message, int width, int height) {
        super();
		
        BorderPane pane = new BorderPane();

        Image img = new Image("/com/link/resources/gui/images/errorIcon.png");
        ImageView icon = new ImageView(img);

        Text label = new Text(message);

        Button ok = new Button("Okay");
        ok.setPrefWidth(75);
        ok.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent ae) {
                close();
            }
        });

        pane.setLeft(icon);
        pane.setCenter(label);
        pane.setBottom(ok);

        label.setWrappingWidth(width-70);
        label.setTextAlignment(TextAlignment.CENTER);

        BorderPane.setAlignment(ok, Pos.TOP_CENTER);
        BorderPane.setAlignment(label, Pos.CENTER_LEFT);
        BorderPane.setAlignment(icon, Pos.CENTER);
        BorderPane.setMargin(icon, new Insets(10, 10, 10, 10));
        BorderPane.setMargin(label, new Insets(10, 10, 10, 10));
        BorderPane.setMargin(ok, new Insets(10, 10, 10, 10));
		
        Scene scene = new Scene(pane, width, height);
		
        this.setScene(scene);
        this.setTitle(title);
        this.setResizable(false);

        this.initModality(Modality.APPLICATION_MODAL);
    }
}


