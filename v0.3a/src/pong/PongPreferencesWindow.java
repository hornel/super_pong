// File: PongPreferencesEditor.java
// Author: Leo Horne
// Date Created: 3/8/14
// Version: 0.1

package pong;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class PongPreferencesWindow extends JFrame {

    private PongCanvas pongCanvas;
    private JButton changeForegroundColor, changeBackgroundColor;

    public PongPreferencesWindow(PongCanvas pongCanvas) {

        this.pongCanvas = pongCanvas;
        initGUI();
    }

    private void initGUI() {

        setResizable(false);
        setAlwaysOnTop(true);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("Pong Preferences");
        initButtons();
        addButtons();
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

    private void addButtons() {

        add(changeForegroundColor, BorderLayout.NORTH);
        add(changeBackgroundColor, BorderLayout.SOUTH);
        addListeners();
    }

    private void addListeners() {

        changeForegroundColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFrame colorChooserFrame = new JFrame();

                final JColorChooser colorChooser = new JColorChooser(pongCanvas.foregroundColor);

                colorChooser.getSelectionModel().addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent changeEvent) {
                        pongCanvas.foregroundColor = colorChooser.getColor();
                        pongCanvas.repaint();
                    }
                });

                colorChooserFrame.setResizable(false);
                colorChooserFrame.setAlwaysOnTop(true);
                colorChooserFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                colorChooserFrame.setTitle("Change Foreground Color");
                colorChooserFrame.add(colorChooser);
                colorChooserFrame.pack();
                colorChooserFrame.setLocationRelativeTo(null);
                colorChooserFrame.setVisible(true);
            }
        });

        changeBackgroundColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFrame colorChooserFrame = new JFrame();

                final JColorChooser colorChooser = new JColorChooser(pongCanvas.backgroundColor);

                colorChooser.getSelectionModel().addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent changeEvent) {
                        pongCanvas.setBackground(colorChooser.getColor());
                        pongCanvas.repaint();
                    }
                });

                colorChooserFrame.setResizable(false);
                colorChooserFrame.setAlwaysOnTop(true);
                colorChooserFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                colorChooserFrame.setTitle("Change Background Color");
                colorChooserFrame.add(colorChooser);
                colorChooserFrame.pack();
                colorChooserFrame.setLocationRelativeTo(null);
                colorChooserFrame.setVisible(true);
            }
        });
    }
}