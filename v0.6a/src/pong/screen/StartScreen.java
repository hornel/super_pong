// File: StartScreen.java
// Author: Leo
// Date Created: 5/1/15 5:23 PM

package pong.screen;

import pong.mode.PongCanvas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class StartScreen extends JFrame {

    private JLabel pongLabel;
    private JLabel startLabel;

    public StartScreen() {

        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.BLACK);
        setFocusable(true);
        initComponents();
        addComponents();
        addListeners();
        setSize(PongCanvas.INITIAL_PANEL_WIDTH, PongCanvas.INITIAL_PANEL_HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void initComponents() {

        pongLabel = new JLabel("PONG");
        pongLabel.setFont(new Font("Bauhaus93", Font.PLAIN, 200));
        pongLabel.setForeground(Color.WHITE);
        pongLabel.setHorizontalAlignment(SwingConstants.CENTER);
        pongLabel.setVerticalTextPosition(JLabel.CENTER);
        pongLabel.setBorder(BorderFactory.createEmptyBorder(100, 50, 30, 50));

        startLabel = new JLabel("Press any key to start");
        startLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        startLabel.setForeground(Color.WHITE);
        startLabel.setHorizontalAlignment(SwingConstants.CENTER);
        startLabel.setVerticalTextPosition(JLabel.CENTER);
        startLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 130, 0));
    }

    public void addComponents() {

        add(pongLabel, BorderLayout.CENTER);
        add(startLabel, BorderLayout.SOUTH);
    }

    private void addListeners() {

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {

                Pong pong = new Pong();
                pong.setVisible(true);
                dispose();
            }
        });
    }

    public static void main(String[] args) {

        StartScreen startScreen = new StartScreen();
        startScreen.setVisible(true);
    }
}
