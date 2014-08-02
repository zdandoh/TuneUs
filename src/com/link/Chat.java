package com.link;

import java.util.ArrayList;
import java.util.List;

public class Chat {
    private List<String> chat_history = new ArrayList<String>();

    public void refreshChat(){
        //download chat from server
    }

    public String[] getChat(){
        refreshChat();
        String[] all_chat = new String[chat_history.size()];
        for(int message_no = 0; message_no < all_chat.length; message_no++){
            all_chat[message_no] = chat_history.get(message_no);
        }
        return all_chat;
    }

    public void sendMessage(){
        //Send message to server
    }
}
