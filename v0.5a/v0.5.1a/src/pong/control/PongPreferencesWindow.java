// File: PongPreferencesEditor.java
// Author: Leo Horne
// Date Created: 3/8/14
// Version: 0.1a

package pong.control;

import pong.mode.PongCanvas;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Class to present the preferences window to the user.
 */

public class PongPreferencesWindow extends JFrame {

    private PongPreferencesEditor ppe;
    private JButton changeForegroundColor, changeBackgroundColor;
    private JSlider ballSpeedSlider;

    /**
     * Class constructor
     *
     * @param pongCanvas parent pongCanvas
     */
    public PongPreferencesWindow(PongCanvas pongCanvas) {

        this.ppe = new PongPreferencesEditor(pongCanvas);
        initGUI();
    }

    private void initGUI() {

        setResizable(false);
        setAlwaysOnTop(true);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("Pong Preferences");
        initButtons();
        initSliders();
        addComponents();
        pack();
        setLocationRelativeTo(null);
    }

    private void initButtons() {

        changeForegroundColor = new JButton("Change Foreground Color...");
        changeBackgroundColor = new JButton("Change Background Color...");
        InputMap im = (InputMap) UIManager.get("Button.focusInputMap");  // these three lines disable the annoying "space = click" behavior of JButtons
        im.put(KeyStroke.getKeyStroke("pressed SPACE"), "none");
        im.put(KeyStroke.getKeyStroke("released SPACE"), "none");
    }

    private void initSliders() {

        ballSpeedSlider = new JSlider(JSlider.HORIZONTAL, PongPreferencesEditor.MINIMUM_BALL_SPEED, PongPreferencesEditor.MAXIMUM_BALL_SPEED, ppe.getCurrentBallSpeed());
        ballSpeedSlider.setMajorTickSpacing(5);
        ballSpeedSlider.setMinorTickSpacing(1);
        ballSpeedSlider.setSnapToTicks(true);
        ballSpeedSlider.setPaintLabels(true);
        ballSpeedSlider.setPaintTicks(true);
    }

    private void addComponents() {

        add(changeForegroundColor, BorderLayout.NORTH);
        add(changeBackgroundColor, BorderLayout.CENTER);
        add(ballSpeedSlider, BorderLayout.SOUTH);
        addListeners();
    }

    private void addListeners() {

        changeForegroundColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                ppe.setForegroundColor();
            }
        });

        changeBackgroundColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                ppe.setBackgroundColor();
            }
        });

        ballSpeedSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {

                ppe.setBallSpeedFromJSlider(ballSpeedSlider);
            }
        });
    }
}