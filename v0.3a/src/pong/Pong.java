// File: Pong.java
// Author: Leo Horne
// Date Created: Saturday, March 7, 2015
// Version: 0.3

/* TODO:
 * Ability to resize window properly
 * Improve pause feature (get rid of deprecated methods)
 * PongPreferencesEditor
 * Fix new game bug (can't create new game after winning)
 * Add sound effects => tbi in PongSoundPlayer
 * Add AI => new PongAI class
 * Add variable backgrounds/user-definable backgrounds => tbi in PongPreferencesEditor
 * speed/paddle size/difficulty controls => tbi in PongPreferencesEditor
 * */


/* Changes from last version:
    * uses BorderLayout instead of FlowLayout
    * adds PongControls
 */

package pong;

import javax.swing.*;
import java.awt.*;

public class Pong extends JFrame {

    PongCanvas pongCanvas = new PongCanvas();
    PongControls pongControls = new PongControls(this, pongCanvas);

    public Pong() {
        initGUI();
    }

    private void initGUI() {

        setResizable(false);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Pong");
        setupGUI();
        addListeners();
        pack();
        setLocationRelativeTo(null);
    }

    private void setupGUI() {

        add(pongCanvas, BorderLayout.NORTH);
        add(pongControls, BorderLayout.SOUTH);
    }

    private void addListeners() {


    }

    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                Pong pong = new Pong();
                pong.setVisible(true);
            }
        });
    }
}