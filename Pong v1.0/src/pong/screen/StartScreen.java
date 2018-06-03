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

/**
 * Classe qui represente la fenetre montree a l'utilisateur avant le demarrage du jeu.
 */
public class StartScreen extends JFrame {

    /**
     * JLabel qui va afficher le mot "PONG".
     */
    private JLabel pongLabel;
    /**
     * JLabel qui va afficher le ce que doit faire l'utilisateur pour demarrer le jeu.
     */
    private JLabel startLabel;

    /**
     * Map qui permet de chercher un terme dans la langue voulue.
     */
    private Map<String, String> lang = Language.getLanguageMap(Language.getLoc());

    /**
     * Contructeur qui va initialiser l'interface graphique.
     */
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

    /**
     * Initialise les composantes de l'interface graphique.
     */
    public void initComponents() {

        // On veut une fenetre comme celle-ci:
        //  __________________________________________________________________________
        // |                                                                          |
        // |                                                                          |
        // |                   _______  _______  _        _______                     |
        // |                   (  ____ )(  ___  )( (    /|(  ____ \                   |
        // |                   | (    )|| (   ) ||  \  ( || (    \/                   |
        // |                   | (____)|| |   | ||   \ | || |                         |
        // |                   |  _____)| |   | || (\ \) || | ____                    |
        // |                   | (      | |   | || | \   || | \_  )                   |
        // |                   | )      | (___) || )  \  || (___) |                   |
        // |                   |/       (_______)|/    )_)(_______)                   |
        // |                                                                          |
        // |                                                                          |
        // |     Appuyez sur une touche ou cliquez sur la souris pour commencer       |
        // |                                                                          |
        // |                                                                          |
        // |__________________________________________________________________________|

        pongLabel = new JLabel("PONG");
        Font pongFont = FontLoader.loadTrueTypeFont("pongfont.ttf", Font.PLAIN, 200);
        pongLabel.setFont(pongFont);
        pongLabel.setForeground(Color.WHITE);
        pongLabel.setHorizontalAlignment(SwingConstants.CENTER);
        pongLabel.setVerticalTextPosition(JLabel.CENTER);
        pongLabel.setBorder(BorderFactory.createEmptyBorder(150, 50, 30, 50));

        startLabel = new JLabel(lang.get("startScreenText"));
        startLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        startLabel.setForeground(Color.WHITE);
        startLabel.setHorizontalAlignment(SwingConstants.CENTER);
        startLabel.setVerticalTextPosition(JLabel.CENTER);
        startLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 130, 0));
    }

    /**
     * Permet d'afficher et demarrer le jeu.
     */
    private void showPong() {

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                Pong pong = new Pong();
                pong.setVisible(true);
                dispose();  // on ferme cette fenetre.
            }
        });
    }

    /**
     * Ajoute les composantes a la fenetre.
     */
    public void addComponents() {

        add(pongLabel, BorderLayout.CENTER);
        add(startLabel, BorderLayout.SOUTH);
    }

    /**
     * Ajoute les ecouteurs a la fenetre pour que l'utilisateur puisse cliquer sur la fenetre ou appuyer sur n'importe
     * quelle touche pour demarrer le jeu.
     */
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

    /**
     * Affiche cette fenetre.
     */
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