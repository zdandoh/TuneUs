package com.link;


import edu.gvsu.cis.masl.channelAPI.ChannelAPI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Chris on 9/3/2014.
 */
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
    private javafx.scene.control.TextField search_bar;

    private ObservableList<TableData> data = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        creator_column.setCellValueFactory(
                new PropertyValueFactory<TableData, String>("creator"));
        users_column.setCellValueFactory(
                new PropertyValueFactory<TableData, String>("users"));
        recent_column.setCellValueFactory(
                new PropertyValueFactory<TableData, String>("creation_time"));
    }

    @FXML
    private void searchTable(){
        data = FXCollections.observableArrayList();
        String search_result[] = searchQuery((String)search_selector.getValue(), search_bar.getText());
        for(String result: search_result){
            TableData table_row = new TableData(result);
            System.out.println(table_row.getCreator() + table_row.getRecent() + table_row.getUsers());
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
        Parent root;
        searchTable();
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