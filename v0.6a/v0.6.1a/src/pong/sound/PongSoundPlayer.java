// File: PongSoundPlayer.java
// Author: Leo Horne
// Date Created: 3/15/15 8:39 AM

package pong.sound;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class PongSoundPlayer {

    private String location;
    private Clip clip;
    private long beforeTime = System.currentTimeMillis();

    public PongSoundPlayer(String location) {

        this.location = location;
        initSound();
    }

    private void initSound() {

        try {
            URL stream = this.getClass().getResource(location);  // Allows opening file from same directory as class file
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(stream);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            audioInputStream.close();
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

    public void playSound() {

        if (clip.isRunning())
            clip.stop();
        clip.setFramePosition(0);  // rewind to the beginning
        clip.start();
    }

    public void playSoundAfter(long interval) {

        if (System.currentTimeMillis() - beforeTime >= interval) {
            playSound();
            beforeTime = System.currentTimeMillis();
        }
    }

    public void deinit() {

        clip.stop();
        clip.close();
    }
}