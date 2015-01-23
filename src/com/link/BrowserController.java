package com.link;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class BrowserController implements Initializable {

    @FXML private Button joinButton;

    @FXML
    private TableView<TableData> dataTable;

    //table columns

    @FXML
    TableColumn<TableData, String> creator_column;

    @FXML
    TableColumn<TableData, String> users_column;

    @FXML
    TableColumn<TableData, String> recent_column;

    @FXML
    private ComboBox search_selector;

    @FXML
    private TextField search_bar;

    private ObservableList<TableData> data = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        creator_column.setCellValueFactory(
                new PropertyValueFactory<TableData, String>("creator"));
        users_column.setCellValueFactory(
                new PropertyValueFactory<TableData, String>("users"));
        recent_column.setCellValueFactory(
                new PropertyValueFactory<TableData, String>("recent"));

        dataTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() > 1) {
                    joinAction();
                }
            }
        });

        dataTable.getColumns().addListener(new ListChangeListener() {
            @Override
            public void onChanged(Change change) {
                change.next();
                if(change.wasReplaced()) {
                    dataTable.getColumns().clear();
                    dataTable.getColumns().addAll(creator_column, users_column, recent_column);
                }
            }
        });
    }

    @FXML
    private void searchTable(){
        data = FXCollections.observableArrayList();
        String search_result[] = searchQuery((String)search_selector.getValue(), search_bar.getText());
        for(String result: search_result){
            TableData table_row = new TableData(result);
            data.add(table_row);
        }
        dataTable.setItems(data);
    }

    private String[] searchQuery(String data_field, String field_content){
        String search_results = Main.session.readPage(Main.session.getUrl("SEARCH_BY") + String.format("?%s=%s", data_field.toLowerCase(), field_content));
        return search_results.split("\n");
    }

    @FXML
    private void joinAction() {
        try {
            TableView.TableViewSelectionModel selectionModel = dataTable.getSelectionModel();
            TableData selected = (TableData) selectionModel.getSelectedItem();
            //do stuff with session_id
            Main.session.session_id = selected.getID();

            openMain();
        }
        catch (NullPointerException e) {
            ErrorDialog dialog = new ErrorDialog("Error", "Choose a Session to join.", 220, 100);
            dialog.show();
        }
    }

private void openMain(){

    Parent root;
    Stage stage = (Stage) joinButton.getScene().getWindow();
    try {
        //assign session id
        stage.close();
        root = FXMLLoader.load(getClass().getResource("/com/link/resources/gui/main.fxml"));
        stage = new Stage();
        stage.setTitle("TuneUs");
        stage.setScene(new Scene(root));
        stage.show();

    }
    catch (IOException e) {
        e.printStackTrace();
        }
    }
}