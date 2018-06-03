// File: PongSoundPlayer.java
// Author: Leo Horne
// Date Created: 3/15/15 8:39 AM

package pong.sound;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Classe qui permet de lire un son court dans la memoire et de le jouer.
 */
public class PongSoundPlayer {

    /**
     * Le nom du son. Le son se trouve dans le meme dossier que cette classe.
     */
    private String location;

    /**
     * Le clip qui va lire le son dans la memoire et le jouer/arreter.
     */
    private Clip clip;

    /**
     * L'heure avant que le clip soit joue.
     */
    private long beforeTime = System.currentTimeMillis();

    public PongSoundPlayer(String location) {

        this.location = location;
        initSound();
    }

    /**
     * Lit le son dans la memoire.
     */
    private void initSound() {

        try {
            URL stream = this.getClass().getResource(location);
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

    /**
     * Permet de jouer le son.
     */
    public void playSound() {

        if (clip.isRunning()) {
            clip.stop();
        }
        clip.setFramePosition(0);  // rewind to the beginning
        clip.start();
    }

    /**
     * Permet de jouer un son toute les interval secondes.
     */
    public void playSoundAfter(long interval) {

        // on utilise la variable beforeTime qui stocke l'heure avant que le son soit joue pour regarder si l'ecart
        // entre l'heure actuelle et beforeTime est >= interval. Si oui, interval secondes ont passe et on peut jouer
        // le son.
        if (System.currentTimeMillis() - beforeTime >= interval) {
            playSound();
            beforeTime = System.currentTimeMillis();
        }
    }

    /**
     * Deinitialise le son en l'enlevant de la memoire.
     */
    public void deinit() {

        if (clip.isRunning()) {
            clip.stop();
        }
        clip.close();
    }
}