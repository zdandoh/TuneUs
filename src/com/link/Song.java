package com.link;

import java.io.File;
import java.io.IOException;

public class Song {
    public String raw;
    public String id;
    public String path;
    public String file_name = "unknown";
    public int play_timestamp;
    public boolean song_ready = false;
    public boolean is_youtube = false;
    public String youtube_id = "none";

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
        if(id.startsWith("yt;")){
            this.is_youtube = true;
            youtube_id = id.split(";")[1];
            this.path = "cache/" + this.youtube_id + ".mp3";
        }
        this.getSong();
    }

    public void isSongReady(){
        Thread checker_thread = new Thread(){
            public void run(){
                boolean exists = false;
                String file_path = System.getProperty("user.dir") + "\\cache\\" + youtube_id + ".mp3";
                System.out.println(file_path);
                File fi = new File(file_path);
                while(!exists){
                    if(fi.exists() && !fi.isDirectory()){
                        song_ready = true;
                        exists = true;
                        Main.sleep(100);
                    }
                }
            }
        };
        checker_thread.setDaemon(true);
        checker_thread.start();
    }

    public void getSong(){
        String url = "";
        if (this.id.startsWith("yt;")){
            String dl_command = "cmd.exe /c " + System.getProperty("user.dir") + "\\cache\\youtube-dl.exe -o \"cache\\%(id)s.%(ext)s\" --extract-audio --audio-format mp3 " + youtube_id;
            try{
                System.out.println("YOUTUBE URL DOWNLOAD");
                Runtime.getRuntime().exec(dl_command);
            }
            catch(IOException e){
                System.out.println(e);
            }
            isSongReady(); // update song availability
        }
        else{
            url = Main.player.getURLFromID(this.id);
            final String download_url = url;
            Thread downloader = new Thread(){
                public void run(){
            song_ready = Main.session.getFile(download_url, path);
        }
    };
            downloader.setDaemon(true);
            downloader.start();
        }
    }

    public void printSong(){
        System.out.println(this.raw);
    }
}
