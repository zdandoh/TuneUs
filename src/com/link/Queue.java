package com.link;

import java.util.ArrayList;
import java.util.List;

public class Queue {
    private List<String> songs = new ArrayList<String>();

    public Queue(){
        Thread pollThread = new Thread(){
            public void run(){
                while (true){
                    pollQueue();
                    try {
                        Thread.sleep(1000);
                    }
                    catch(InterruptedException e){
                        System.out.println("Polling thread interrupted");
                    }
                }
            }
        };
        pollThread.setDaemon(true);
        pollThread.start();
    }

    public String getNextSong()
    {
        String next_song = "";
        if(songs.size() == 0) {
            return next_song;
        }
        else{
            next_song = (String)songs.get(0);
            if(Integer.parseInt(next_song.split(":")[3]) <= System.currentTimeMillis() / 1000L){
                songs.remove(0);
                return next_song;
            }
            else{
                next_song = "";
            }
        }
        return next_song;
    }

    public boolean addSong(String name)
    {
        songs.add(name);
        return true;
    }

    public void pollQueue(){
        String data = String.format("?id=%s&last_poll=%s", Main.session.session_id, (int)(System.currentTimeMillis() / 1000L));
        String check_queue = Main.session.readPage(Main.session.getUrl("CHECK_QUEUE") + data);
        if (check_queue.contains("Server Error")){
            //probably timed out, no new audio
        }
        else{
            String[] new_songs = check_queue.split("\n");
            for(String song: new_songs){
                //failed page load conditional should be removed once server actually works right
                if (song.length() > 0 && !song.equals("failed page load")){
                    songs.add(song);
                }
            }
        }
    }
}
