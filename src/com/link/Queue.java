package com.link;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Slider;

import java.util.ArrayList;
import java.util.List;

public class Queue {
    private List<Song> songs = new ArrayList<Song>();
    public ObservableList listview_data = FXCollections.observableArrayList();
    private int poll_interval = 1000;
    private int inactive_counter = 0;

    public Queue(){
        Thread pollThread = new Thread(){
            public void run(){
                while (true){
                    pollQueue();
                    try {
                        Thread.sleep(poll_interval);
                    }
                    catch(InterruptedException e){
                        System.out.println("Polling thread interrupted");
                    }
                    if(inactive_counter > 30){
                        //no songs have been posted, going into "sleep mode"
                        poll_interval *= 2; // double interval after every idle request past this point
                        System.out.println("Sleep mode active, delay: " + poll_interval);
                    }
                }
            }
        };
        pollThread.setDaemon(true);
        pollThread.start();
    }

    public Song getNextSong()
    {
        String next_song_data = "";
        if(songs.size() == 0) {
            return new Song();
        }
        else{
            Song next_song = songs.get(0);
            if(next_song.play_timestamp <= System.currentTimeMillis() / 1000L && next_song.song_ready){
                songs.remove(0);
                return next_song;
            }
            else{
                next_song_data = "";
            }
        }
        return new Song();
    }

    public void pollQueue(){
        String data = String.format("?id=%s&last_poll=%s", Main.session.session_id, (int)(System.currentTimeMillis() / 1000L));
        String check_queue = Main.session.readPage(Main.session.getUrl("CHECK_QUEUE") + data);
        this.inactive_counter += 1;
        if (check_queue.contains("Server Error")){
            //probably timed out, no new audio
        }
        else{
            String[] new_songs = check_queue.split("\n");
            for(String song: new_songs){
                //failed page load conditional should be removed once server actually works right
                if (song.length() > 0 && !song.equals("failed page load")){
                    Song new_song = new Song(song);
                    songs.add(new_song);
                    final String song_name;
                    if(new_song.id.startsWith("yt")){
                        song_name = Main.session.getVideoTitle(new_song.id);
                    }
                    else{
                        song_name = new_song.file_name;
                    }
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            listview_data.add(song_name);
                        }
                    });
                    this.inactive_counter = 0;
                }
            }
        }
    }
}
