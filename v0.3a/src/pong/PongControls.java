// File: PongControls.java
// Author: Leo Horne
// Date Created: Saturday, March 7, 2015
// Version: 0.1

package pong;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class PongControls extends JPanel {

    private Pong pong;
    private PongCanvas pongCanvas;
    //private PongPreferencesEditor pongPreferencesEditor;
    private JButton pauseButton, quitButton, preferencesButton, newGameButton;
    private boolean paused = false;
    private JCheckBox gravityCheckBox;
    private boolean gravity = false;

    public PongControls(Pong pong, PongCanvas pongCanvas/*, PongPreferencesEditor pongPreferencesEditor*/) {
        this.pong = pong;
        this.pongCanvas = pongCanvas;
        //this.pongPreferencesEditor = pongPreferencesEditor;
        initGUI();
    }

    private void initGUI() {

        setPreferredSize(new Dimension(pongCanvas.getPanelWidth(), 40));
        setSize(pongCanvas.getPanelWidth(), 40);
        setLayout(new FlowLayout());
        addButtons();
    }

    private void initButtons() {

        pauseButton = new JButton("Pause");
        quitButton = new JButton("Quit");
        preferencesButton = new JButton("Preferences...");
        newGameButton = new JButton("New Game");

        gravityCheckBox = new JCheckBox("Gravity");

        InputMap im = (InputMap)UIManager.get("Button.focusInputMap");  // these three lines disable the annoying "space = click" behavior of JButtons
        im.put(KeyStroke.getKeyStroke("pressed SPACE"), "none");
        im.put(KeyStroke.getKeyStroke("released SPACE"), "none");
    }

    private void addButtons() {

        initButtons();
        addListeners();
        add(quitButton);
        add(newGameButton);
        add(pauseButton);
        add(preferencesButton);

        add(gravityCheckBox);
    }

    private void handleQuitEvent() {

        pong.dispose();
        System.exit(0);
    }

    void handlePauseEvent() {

        if (!paused) {
            pongCanvas.animationThread.suspend();
            paused = true;
            pauseButton.setText("Play");
        } else {
            pongCanvas.animationThread.resume();
            paused = false;
            pauseButton.setText("Pause");
        }
    }

    private void handleNewGameEvent() {

        paused = true;
        handlePauseEvent();
        pongCanvas.reset();
    }

    private void handlePreferencesEvent() {

        paused = false;
        handlePauseEvent();
        PongPreferencesWindow ppw = new PongPreferencesWindow(pongCanvas);
        ppw.setVisible(true);
    }

    private void addListeners() {

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                handleQuitEvent();
            }
        });

        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                handleNewGameEvent();
            }
        });

        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                handlePauseEvent();
            }
        });

        preferencesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                handlePreferencesEvent();
            }
        });

        gravityCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                gravity = !gravity;
                pongCanvas.ball.setGravity(gravity);
            }
        });
    }
}