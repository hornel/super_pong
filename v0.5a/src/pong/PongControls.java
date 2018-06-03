// File: PongControls.java
// Author: Leo Horne
// Date Created: Saturday, March 7, 2015
// Version: 0.1a

package pong;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Provides common game function the the pong game.
 */

class PongControls extends JPanel {

    private Pong pong;
    private PongPanel pongCanvas;
    private JButton pauseButton, quitButton, preferencesButton, newGameButton;
    private boolean paused = false;
    private JCheckBox gravityCheckBox;
    private boolean gravityEnabled = false;

    /**
     * Constructor specifying the parent Pong window and parent pongCanvas.
     * @param pong          the parent Pong
     * @param pongCanvas    the parent PongCanvas
     */
    public PongControls(Pong pong, PongPanel pongCanvas) {
        this.pong = pong;
        this.pongCanvas = pongCanvas;
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

    /**
     * Describes action to be performed when "Quit" button is clicked.
     */

    private void handleQuitEvent() {

        pongCanvas.deinit();
        pong.dispose();
        System.exit(0);
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
}