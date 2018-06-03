// File: PongControls.java
// Author: Leo Horne
// Date Created: Saturday, March 7, 2015
// Version: 0.1a

package pong.control;

import pong.Pong;
import pong.ui.PongCheckBox;
import pong.mode.PongCanvas;
import pong.ui.PongButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Provides common game function the the pong game.
 */

public class PongControls extends JPanel {

    private Pong pong;
    private PongCanvas pongCanvas;
    private PongButton pauseButton, quitButton, preferencesButton, newGameButton, changeModeButton;
    private boolean paused = false;
    private PongCheckBox gravityCheckBox;
    private boolean gravityEnabled = false;

    /**
     * Constructor specifying the parent Pong window and parent pongCanvas.
     *
     * @param pong the parent Pong
     */
    public PongControls(Pong pong) {

        this.pong = pong;
        this.pongCanvas = pong.getPongCanvas();
        initGUI();
    }

    private void initGUI() {

        setFocusable(false);
        setPreferredSize(new Dimension(pongCanvas.getPanelWidth(), 40));
        setSize(pongCanvas.getPanelWidth(), 40);
        setLayout(new FlowLayout());
        addButtons();
    }

    private void initButtons() {

        pauseButton = new PongButton("Pause");
        quitButton = new PongButton("Quit");
        preferencesButton = new PongButton("Preferences...");
        newGameButton = new PongButton("New Game");
        changeModeButton = new PongButton("Change Mode...");

        gravityCheckBox = new PongCheckBox("Gravity");

        InputMap im = (InputMap) UIManager.get("Button.focusInputMap");  // these three lines disable the annoying "space = click" behavior of JButtons
        im.put(KeyStroke.getKeyStroke("pressed SPACE"), "none");
        im.put(KeyStroke.getKeyStroke("released SPACE"), "none");
    }

    private void addButtons() {

        initButtons();
        addListeners();
        add(quitButton);
        add(changeModeButton);
        add(newGameButton);
        add(pauseButton);
        add(preferencesButton);

        add(gravityCheckBox);
    }

    /**
     * Describes action to be performed when "Quit" button is clicked.
     */

    private void handleQuitEvent() {

        pongCanvas.deinit();
        pong.dispose();
        System.exit(0);
    }

    private void handleChangeModeEvent() {

        paused = false;
        handlePauseEvent();
        pong.showPongModeChooser();
    }

    /**
     * Describes action to be performed when "Pause" button is clicked.
     */

    void handlePauseEvent() {

        if (!paused) {
            pongCanvas.getAnimationThread().suspend();
            paused = true;
            pauseButton.setText("Play");
        } else {
            pongCanvas.getAnimationThread().resume();
            paused = false;
            pauseButton.setText("Pause");
        }
    }

    /**
     * Describes action to be performed when "New Game" button is clicked.
     */

    private void handleNewGameEvent() {

        paused = true;
        handlePauseEvent();
        pongCanvas.reset();
        pongCanvas.getBall().setGravity(gravityEnabled);
    }

    /**
     * Describes action to be performed when "Preferences..." button is clicked.
     */

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

        changeModeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                handleChangeModeEvent();
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

                gravityEnabled = !gravityEnabled;
                pongCanvas.getBall().setGravity(gravityEnabled);
            }
        });
    }

    public void refresh() {

        pongCanvas = pong.getPongCanvas();
    }
}