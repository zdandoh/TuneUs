package com.link;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;

/**
 * Created by Dan on 9/17/2014.
 */
public class Player {
    private MediaPlayer media_player;
    public void play(String file){
        File raw_file = new File(file);
        URI file_uri = raw_file.toURI();
        Media song = new Media(file_uri.toString());
        media_player = new MediaPlayer(song);
        //set progress indicator updating thread
        final int song_length = getLength(file);
        Thread progress_updater = new Thread(){
            public void run(){
                updateProgress(song_length);
            }
        };
        progress_updater.setDaemon(true);
        //progress_updater.start();
        media_player.play();
        try{
            Thread.sleep(song_length * 1000 + 10000);
        }
        catch(InterruptedException e){
            System.out.println("Playing thread interrupted");
        }
    }

    public void setVolume(double volume_level){
        volume_level /= 100;
        media_player.setVolume(volume_level);
    }

    public void playAsync(final String file){
        Thread currently_playing = new Thread(){
            public void run(){
                play(file);
            }
        };
        currently_playing.setDaemon(true);
        currently_playing.start();
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
