// File: PongPreferencesEditor.java
// Author: Leo Horne
// Date Created: Saturday, March 7, 2015
// Version: 0.1

package pong.control;

import pong.mode.PongCanvas;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Class to edit pong game attributes
 */

public class PongPreferencesEditor {

    public static final int MINIMUM_BALL_SPEED = 1;
    public static final int MAXIMUM_BALL_SPEED = 30;
    private PongCanvas pongCanvas;

    /**
     * Class constructor specifying pongCanvas to be edited.
     *
     * @param pongCanvas the pongCanvas to be edited.
     */
    public PongPreferencesEditor(PongCanvas pongCanvas) {

        this.pongCanvas = pongCanvas;
    }

    public int getCurrentBallSpeed() {

        return (int) pongCanvas.getBall().getSpeed();
    }

    public void setBallSpeedFromJSlider(JSlider slider) {

        pongCanvas.getBall().setSpeed(slider.getValue());
    }

    public void setBackgroundColor() {

        JFrame colorChooserFrame = new JFrame();

        final JColorChooser colorChooser = new JColorChooser(pongCanvas.getBackgroundColor());

        colorChooser.getSelectionModel().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {

                pongCanvas.setBackground(colorChooser.getColor());
                pongCanvas.repaint();
            }
        });

        colorChooserFrame.setResizable(false);
        colorChooserFrame.setAlwaysOnTop(true);
        colorChooserFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        colorChooserFrame.setTitle("Change Background Color");
        colorChooserFrame.add(colorChooser);
        colorChooserFrame.pack();
        colorChooserFrame.setLocationRelativeTo(null);
        colorChooserFrame.setVisible(true);
    }

    public void setForegroundColor() {

        JFrame colorChooserFrame = new JFrame();

        final JColorChooser colorChooser = new JColorChooser(pongCanvas.getForegroundColor());

        colorChooser.getSelectionModel().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {

                pongCanvas.setForegroundColor(colorChooser.getColor());
                pongCanvas.repaint();
            }
        });

        colorChooserFrame.setResizable(false);
        colorChooserFrame.setAlwaysOnTop(true);
        colorChooserFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        colorChooserFrame.setTitle("Change Foreground Color");
        colorChooserFrame.add(colorChooser);
        colorChooserFrame.pack();
        colorChooserFrame.setLocationRelativeTo(null);
        colorChooserFrame.setVisible(true);
    }
}