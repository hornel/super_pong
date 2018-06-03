// File: Pong.java
// Author: Leo Horne
// Date Created: Saturday, March 7, 2015

package pong.screen;

import pong.control.PongControls;
import pong.control.PongModeChooser;
import pong.control.SingleWindowManager;
import pong.mode.PongCanvas;
import pong.mode.PongMode;
import pong.mode.TwoHumanPlayerPongCanvas;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Classe representant la fenetre de base du jeu.
 */
public class Pong extends JFrame {

    /**
     * Le PongCanvas dans la fenetre.
     */
    private PongCanvas pongCanvas;

    /**
     * Les PongControls dans la fenetre.
     */
    private PongControls pongControls;

    /**
     * Permet de n'avoir qu'une fenetre de changement de mode ouverte a la fois.
     */
    private SingleWindowManager<JDialog> singleWindowManager = new SingleWindowManager<JDialog>();

    public Pong() {

        tryToEnableMacIntegration();
        initGUI();
        addComponents();
        addListeners();
        pack();
        setLocationRelativeTo(null);
    }

    /**
     * Intialise la fenetre.
     */
    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {

                JFrame pong = new Pong();
                pong.setVisible(true);
            }
        });
    }

    /**
     * Initialise l'interface graphique.
     */
    private void initGUI() {

        // On veut utiliser le LAF du systeme.
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        // par defaut, le mode de jeu est le mode homme vs. homme.
        pongCanvas = new TwoHumanPlayerPongCanvas();
        pongControls = new PongControls(this);
        // on fixe la taille minimale de la fenetre de telle maniere a pouvoir toujours afficher tous les boutons.
        setMinimumSize(new Dimension((int) pongControls.getMinimumSize().getWidth() + 25, 375));
        setLayout(new BorderLayout());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Pong");
    }

    /**
     * Ajoute les composantes a la fenetre.
     */
    private void addComponents() {

        add(pongCanvas, BorderLayout.CENTER);
        add(pongControls, BorderLayout.SOUTH);
    }

    /**
     * Ajoute les ecouteurs aux composantes qui en ont besoin.
     */
    private void addListeners() {

        // On veut que lorsque le jeu se termine, les boutons pause et preferences sont desactives.
        pongCanvas.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                pongControls.getPauseButton().setEnabled(false);
                pongControls.getPreferencesButton().setEnabled(false);
            }
        });
    }

    /**
     * Permet d'afficher la fenetre de changement de mode.
     */
    public void showPongModeChooser() {

        if (!singleWindowManager.isOccupied()) {
            singleWindowManager.setComponent(new PongModeChooser(this));
        }
        if (singleWindowManager.isOccupied()) {
            singleWindowManager.getComponent().setVisible(true);
        }
    }

    /**
     * Permet de changer le mode du jeu a partir d'un PongMode donne et des parametres donnes.
     */
    public void changePongMode(PongMode pongMode, Object... params) {

        // on termine le jeu
        pongCanvas.end();
        // on enleve le PongCanvas actuellement affiche.
        remove(pongCanvas);
        // pour etre sur qu'il est enleve
        getContentPane().setVisible(false);
        getContentPane().setVisible(true);
        getContentPane().repaint();
        // On cherche le constructeur du PongCanvas correspondant au PongMode.
        Constructor<?> pongCanvasConstructor = pongMode.getCorrespondingPongCanvasConstructor();
        try {
            // on instantie le nouveau pongCanvas avec les parametres donnes.
            pongCanvas = (PongCanvas) pongCanvasConstructor.newInstance(params);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        // il faut reajouter les ecouteurs parce que le PongCanvas n'est plus le meme.
        addListeners();
        // il faut dire aux PongControls que le PongCanvas a change.
        pongControls.refresh();
        // on ajoute le nouveau PongCanvas a la fenetre.
        add(pongCanvas);
        // on redessine et revalide la region ou l'ancien pongCanvas etait.
        pongCanvas.repaint();
        getContentPane().setVisible(false);
        getContentPane().setVisible(true);
        getContentPane().repaint();
        // on demarre le jeu.
        pongControls.resume();
    }

    /**
     * Cherche le PongCanvas qui se trouve dans la fenetre.
     */
    public PongCanvas getPongCanvas() {

        return pongCanvas;
    }

    /**
     * Permet d'activer des fonctions d'integration avec Mac OS X:
     * - le mode plein ecran du systeme d'exploitation
     * - la barre de menu en haut de l'ecran
     * - des fonctions qui permettent un affichage plus agreable des composantes.
     */
    private void tryToEnableMacIntegration() {

        if (System.getProperty("os.name").toLowerCase().contains("mac os x")) {

            // On dit a Mac OS X que ce programme peut utiliser le mode plein ecran fourni par le systeme.
            try {
                Class<?> fullScreenClass = Class.forName("com.apple.eawt.FullScreenUtilities");
                Method enableFullScreen = fullScreenClass.getMethod("setWindowCanFullScreen", Window.class, boolean.class);
                enableFullScreen.invoke(null, this, true);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            // On active la barre de menu en haut de l'ecran.
            System.setProperty("apple.laf.useScreenMenuBar", "true");
            // On active l'anticrenelage du texte
            System.setProperty("apple.awt.textantialiasing", "true");
            // On utilise Quartz au lieu de Java2D pour dessiner a l'ecran.
            System.setProperty("apple.awt.graphics.UseQuartz", "true");
        }
    }
}