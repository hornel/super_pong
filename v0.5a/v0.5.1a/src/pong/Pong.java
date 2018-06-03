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

import pong.control.PongControls;
import pong.mode.PongCanvas;
import pong.mode.PongMode;
import pong.control.PongModeChooser;
import pong.mode.TwoHumanPlayerPongCanvas;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

/**
 * Class for creating the root window of the game.
 */

public class Pong extends JFrame {

    PongCanvas pongCanvas = new TwoHumanPlayerPongCanvas();
    PongControls pongControls = new PongControls(this);

    public Pong() {

        initGUI();
    }

    public static void main(String[] args) {

        for (PongMode pongMode : PongMode.values()) {

            try {
                Class.forName(pongMode.getCorrespondingPongCanvas().getClass().getName());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {

                JFrame pong = new Pong();
                pong.setVisible(true);
                //Pong pong2 = (Pong) pong;
                //pong2.showPongModeChooser();
            }
        });
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

    public void showPongModeChooser() {

        final PongModeChooser pongModeChooser = new PongModeChooser();

        pongModeChooser.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {

                changePongMode(pongModeChooser.getPongMode());
            }
        });

        pongModeChooser.setVisible(true);
    }

    public void changePongMode(PongMode pongMode) {

        remove(pongCanvas);
        pongCanvas = pongMode.getCorrespondingPongCanvas();
        pongControls.refresh();
        add(pongCanvas);
    }

    public PongCanvas getPongCanvas() {

        return pongCanvas;
    }
}