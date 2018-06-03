// File: PongSoundPlayer.java
// Author: Leo Horne
// Date Created: 3/15/15 8:39 AM
// Version: 0.1a

package pong;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

class PongSoundPlayer {

    String location;
    Clip clip;
    AudioInputStream audioInputStream;

    public PongSoundPlayer(String location) {

        this.location = location;
        initSound();
    }

    private void initSound() {

        try {
            URL path = this.getClass().getResource(location);  // Allows opening file from same directory as class file
            audioInputStream = AudioSystem.getAudioInputStream(path);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    void playSound() {

        if (clip.isRunning())
            clip.stop();
        clip.setFramePosition(0);  // rewind to the beginning
        clip.start();
    }

    void deinit() {

        clip.stop();
        clip.close();
        try {
            audioInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}