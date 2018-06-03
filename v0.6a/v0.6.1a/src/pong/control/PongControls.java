// File: PongControls.java
// Author: Leo Horne
// Date Created: Saturday, March 7, 2015

package pong.control;

import pong.lang.Language;
import pong.screen.Pong;
import pong.mode.PongCanvas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

/**
 * Provides common game function the the pong game.
 */

public class PongControls extends JPanel {

    private Pong pong;
    private PongCanvas pongCanvas;
    private JButton pauseButton, quitButton, preferencesButton, changeModeButton;
    private boolean paused = false;
    private JCheckBox gravityCheckBox;
    private boolean gravityEnabled = false;
    private Map<String, String> lang = Language.getLanguageMap(Language.getLoc());

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
        setLayout(new FlowLayout());
        addButtons();
    }

    private void initButtons() {

        pauseButton = new JButton(lang.get("pauseText"));
        quitButton = new JButton(lang.get("quitText"));
        preferencesButton = new JButton(lang.get("preferencesText"));
        changeModeButton = new JButton(lang.get("newGameText"));

        JButton[] buttons = new JButton[]{pauseButton, quitButton, preferencesButton, changeModeButton};

//        for (LButton b : buttons) {
//            b.setTextPadding(3, 10, 3, 10);
//        }
        for (JButton b : buttons) {
            b.setFocusable(false);
        }

        gravityCheckBox = new JCheckBox(lang.get("gravityText"));
        gravityCheckBox.setFocusable(false);

        InputMap im = (InputMap) UIManager.get("Button.focusInputMap");  // these three lines disable the annoying "space = click" behavior of JButtons
        im.put(KeyStroke.getKeyStroke("pressed SPACE"), "none");
        im.put(KeyStroke.getKeyStroke("released SPACE"), "none");
    }

    private void addButtons() {

        initButtons();
        addListeners();
        add(quitButton);
        add(changeModeButton);
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

    private void handlePauseEvent() {

        if (!paused) {
            pongCanvas.getAnimationThread().suspend();
            paused = true;
            pauseButton.setText(lang.get("playText"));
        } else {
            pongCanvas.getAnimationThread().resume();
            paused = false;
            pauseButton.setText(lang.get("pauseText"));
        }
    }

    public void pause() {

        pongCanvas.getAnimationThread().suspend();
        paused = true;
        pauseButton.setText(lang.get("playText"));
    }

    public void resume() {

        pongCanvas.getAnimationThread().resume();
        paused = false;
        pauseButton.setText(lang.get("pauseText"));
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

        pongCanvas.getInputMap().put(KeyStroke.getKeyStroke(' '), "Game Paused");
        pongCanvas.getActionMap().put("Game Paused", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handlePauseEvent();
            }
        });
    }

    public void refresh() {

        pongCanvas = pong.getPongCanvas();
        pongCanvas.getInputMap().put(KeyStroke.getKeyStroke(' '), "Game Paused");
        pongCanvas.getActionMap().put("Game Paused", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handlePauseEvent();
            }
        });
    }
}