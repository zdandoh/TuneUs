package com.link;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

/**
 * Created by Dan on 9/17/2014.
 */
public class Player {
    private MediaPlayer media_player;
    public Player(){
        Thread play_checker = new Thread(){
            public void run(){
                playAsync();
            }
        };
        play_checker.setDaemon(true);
        play_checker.start();
    }
    public void play(String path){
        Media song = new Media(path);
        media_player = new MediaPlayer(song);
        //set progress indicator updating thread
        final int song_length = 1; //getLength(file); I broke this
        Thread progress_updater = new Thread() {
            public void run() {
                updateProgress(song_length);
            }
        };
        progress_updater.setDaemon(true);
        //progress_updater.start();
        media_player.play();
    }

    public void setVolume(double volume_level){
        volume_level /= 100;
        media_player.setVolume(volume_level);
    }

    public String getURLFromID(String id){
        String url = "";
        if (id.startsWith("yt")){
            // youtube ID
            System.out.println("YOUTUBE URLS NOT YET SUPPORTED");
        }
        else{
            // blobstore ID
            url = "http://tuneusserv.appspot.com/blob/serve/" + id;
        }
        return url;
    }

    public void playAsync(){
        //checks for songs to play from song queue
        while(true){
            try {
                Thread.sleep(1000);
            }
            catch(InterruptedException e){
                System.out.println("nen");}
            final Song next_song = Main.queue.getNextSong();
            if(next_song.play_timestamp > -1){
                URI uri = new File(next_song.path).toURI();
                play(uri.toString());
            }
        }
    }

    public int getLength(String file) {
        int duration = -1;

        try {
            AudioFile audioFile = AudioFileIO.read(new File(file));
            duration = audioFile.getAudioHeader().getTrackLength();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return duration;
    }

    public void updateProgress(double length){
        int progress = 0;
        FXMLLoader fxmlLoader = new FXMLLoader();
        try{
            Pane main_pane = fxmlLoader.load(getClass().getResource("/com/link/resources/gui/main.fxml").openStream());
        }
        catch(IOException e){
            System.out.println("Unable to load main controller class");
        }
        MainController main_control = (MainController) fxmlLoader.getController();
        while(progress < 360){
            try{
                Thread.sleep(1000);
            }
            catch(InterruptedException e){
                System.out.println("Update thread interrupted");
            }
            main_control.setProgress(progress);
        }
    }
}
