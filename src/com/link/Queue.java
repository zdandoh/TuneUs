package com.link;

import java.util.ArrayList;
import java.util.List;

public class Queue {
    private List<String> songs = new ArrayList<String>();

    public String getNextSong()
    {
        String next_song = "none";
        if(songs.size() == 0) {
            return next_song;
        }
        else{
            next_song = (String)songs.get(0);
            songs.remove(0);
            return next_song;
        }
    }

    public boolean addSong(String name)
    {
        songs.add(name);
        return true;
    }
}
