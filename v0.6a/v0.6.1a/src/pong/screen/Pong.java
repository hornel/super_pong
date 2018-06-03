// File: Pong.java
// Author: Leo Horne
// Date Created: Saturday, March 7, 2015

package pong.screen;

import pong.control.PongControls;
import pong.mode.PongCanvas;
import pong.mode.PongMode;
import pong.mode.TwoHumanPlayerPongCanvas;
import pong.control.PongModeChooser;
//import pong.ui.LUIManager;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

/**
 * Class for creating the root window of the game.
 */

public class Pong extends JFrame {

    PongCanvas pongCanvas;
    PongControls pongControls;

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
            }
        });
    }

    /**
     * Initializes the Pong GUI. Called in the Pong constructor.
     */

    private void initGUI() {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        pongCanvas = new TwoHumanPlayerPongCanvas();
        pongControls = new PongControls(this);
        //setResizable(false);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
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

        add(pongCanvas, BorderLayout.CENTER);
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
//                LUIManager.reset();
            }
        });

        pongModeChooser.setVisible(true);
    }

    public void changePongMode(PongMode pongMode) {

        remove(pongCanvas);
        pongCanvas = pongMode.getCorrespondingPongCanvas();
        pongControls.refresh();
        add(pongCanvas);
        pongCanvas.repaint();
        pongControls.resume();
    }

    public PongCanvas getPongCanvas() {

        return pongCanvas;
    }
}