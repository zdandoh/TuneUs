package com.link;

import java.util.ArrayList;
import java.util.List;

public class Chat {
    private List<String> chat_history = new ArrayList<String>();

    public Chat(){
        Thread chat_listener = new Thread(){
            public void run(){
                refreshChat();
            }
        };
        chat_listener.setDaemon(true);
        chat_listener.start();
    }

    public void refreshChat(){
        //download chat from server
        //TODO: actually make this work
        try{
            Thread.sleep(1000);
        }
        catch(InterruptedException e){}
    }

    public String[] getChat(){
        refreshChat();
        String[] all_chat = new String[chat_history.size()];
        for(int message_no = 0; message_no < all_chat.length; message_no++){
            all_chat[message_no] = chat_history.get(message_no);
        }
        return all_chat;
    }
}