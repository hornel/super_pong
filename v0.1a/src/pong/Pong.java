// File: Pong.java
// Author: Leo Horne
// Date Created: Sunday, February 15, 2015
// Version: 0.1

/* TODO:
 * split different requirements for paddle-hitting into functions
 * y_bend_factor
 * (getters) and setters
 * Add score-keeping mechanism
 * Add pause feature
 * Add color-changing feature
 * Add quit feature
 * Add new game feature
 * Add sound effects
 * Add AI
 * Add variable backgrounds/user-definable backgrounds
 * speed/paddle size/difficulty controls
 * menu bar with preferences dialog
 * */

package pong;

import javax.swing.*;
import java.awt.*;

public class Pong extends JFrame {

    PongCanvas pongCanvas = new PongCanvas();

    public Pong() {
        initGUI();
    }

    private void initGUI() {

        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Pong");
        setupGUI();
        addListeners();
        pack();
        setLocationRelativeTo(null);
    }

    private void setupGUI() {

        add(pongCanvas);
        // TODO: add UI buttons
    }

    private void addListeners() {
        // TODO: add listeners for UI buttons
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                Pong pong = new Pong();
                pong.setVisible(true);
            }
        });
    }
}