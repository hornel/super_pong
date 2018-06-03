// File: PongPreferencesEditor.java
// Author: Leo Horne
// Date Created: 3/8/14
// Version: 0.1a

package pong;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Class to present the preferences window to the user.
 */

class PongPreferencesWindow extends JFrame {

    private PongPanel pongCanvas;
    private PongPreferencesEditor ppe;
    private JButton changeForegroundColor, changeBackgroundColor;
    private JSlider ballSpeedSlider;

    /**
     * Class constructor
     * @param pongCanvas    parent pongCanvas
     */
    public PongPreferencesWindow(PongPanel pongCanvas) {

        this.pongCanvas = pongCanvas;
        this.ppe = new PongPreferencesEditor(pongCanvas);
        initGUI();
    }

    private void initGUI() {

        pongCanvas.setShouldAlwaysRequestFocus(false);
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

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                pongCanvas.setShouldAlwaysRequestFocus(true);
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