package com.link;

/**
 * Created by Dan on 10/25/2014.
 */
public class Song {
    public String raw;
    public String id;
    public String path;
    public int play_timestamp;
    public boolean song_ready = false;

    public Song(){
        this.id = "none";
        this.play_timestamp = -1;
    }

    public Song(String queue_string){
        this.raw = queue_string;
        String queue_data[] = queue_string.split(":");
        this.id = queue_data[1];
        this.path = "cache/" + this.id + ".mp3";
        this.play_timestamp = Integer.parseInt(queue_data[3]);
        this.getSong();
    }

    public void getSong(){
        final String url = Main.player.getURLFromID(this.id);
        Thread downloader = new Thread(){
            public void run(){
                song_ready = Main.session.getFile(url, path);
            }
        };
        downloader.setDaemon(true);
        downloader.start();
    }
}
