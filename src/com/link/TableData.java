package com.link;

import javafx.beans.property.SimpleStringProperty;

/**
 * Created by Dan on 9/7/2014.
 */
public class TableData {
    private SimpleStringProperty creator = new SimpleStringProperty();
    private SimpleStringProperty users = new SimpleStringProperty();
    private SimpleStringProperty creation_time = new SimpleStringProperty();
    private SimpleStringProperty session_id = new SimpleStringProperty();

    public TableData(String table_info){
        //data structure: "id;creator;users;create_time"
        String table_array[] = table_info.split(";");
        this.session_id = new SimpleStringProperty(table_array[0]);
        this.creator = new SimpleStringProperty(table_array[1]);
        this.users = new SimpleStringProperty(table_array[2]);
        this.creation_time = new SimpleStringProperty(table_array[3]);
    }

    public String getCreator(){
        return creator.get();
    }

    public String getUsers(){
        return users.get();
    }

    public String getRecent(){
        return creation_time.get();
    }

    public String getID(){
        return session_id.get();
    }
}
