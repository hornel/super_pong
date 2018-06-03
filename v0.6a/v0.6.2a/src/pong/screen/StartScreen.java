// File: StartScreen.java
// Author: Leo
// Date Created: 5/1/15 5:23 PM

package pong.screen;

import pong.lang.Language;
import pong.mode.PongCanvas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

public class StartScreen extends JFrame {

    private JLabel pongLabel;
    private JLabel startLabel;
    private Map<String, String> lang = Language.getLanguageMap(Language.getLoc());

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
        Font pongFont = FontLoader.loadTrueTypeFont("pongfont.ttf", Font.PLAIN, 200);
        pongLabel.setFont(pongFont);
        pongLabel.setForeground(Color.WHITE);
        pongLabel.setHorizontalAlignment(SwingConstants.CENTER);
        pongLabel.setVerticalTextPosition(JLabel.CENTER);
        pongLabel.setBorder(BorderFactory.createEmptyBorder(100, 50, 30, 50));

        startLabel = new JLabel(lang.get("startScreenText"));
        startLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        startLabel.setForeground(Color.WHITE);
        startLabel.setHorizontalAlignment(SwingConstants.CENTER);
        startLabel.setVerticalTextPosition(JLabel.CENTER);
        startLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 130, 0));
    }

    private void showPong() {

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                Pong pong = new Pong();
                pong.setVisible(true);
                dispose();
            }
        });
    }

    public void addComponents() {

        add(pongLabel, BorderLayout.CENTER);
        add(startLabel, BorderLayout.SOUTH);
    }

    private void addListeners() {

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {

                showPong();
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                showPong();
            }
        });
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                StartScreen startScreen = new StartScreen();
                startScreen.setVisible(true);
            }
        });
    }
}