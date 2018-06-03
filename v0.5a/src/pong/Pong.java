// File: Pong.java
// Author: Leo Horne
// Date Created: Saturday, March 7, 2015
// Version: 0.3a
// Global Version: 0.4a

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

/* Global changes:
    * New sound effects
 */

package pong;

import javax.swing.*;
import java.awt.*;

/**
 * Class for creating the root window of the game.
 */

public class Pong extends JFrame {

    TwoHumanPlayerPongCanvas pongCanvas = new TwoHumanPlayerPongCanvas();
    PongControls pongControls = new PongControls(this, pongCanvas);

    public Pong() {
        initGUI();
    }

    /**
     * Initializes the Pong GUI. Called in the Pong constructor.
     */

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

    /**
     * Adds Swing Components to the Pong window.
     */

    private void setupGUI() {

        add(pongCanvas, BorderLayout.NORTH);
        add(pongControls, BorderLayout.SOUTH);
    }

    /**
     * Adds listeners to the window components.
     */

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