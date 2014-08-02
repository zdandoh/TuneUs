package com.link;

import java.io.IOException;

public class Main {

    public static void main(String[] args){
        Queue song_queue = new Queue();
        Session session = new Session();
        session.readPage("http://tuneusserv.appspot.com/session.py?getnew=1");
    }
}