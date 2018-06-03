// File: PongControls.java
// Author: Leo Horne
// Date Created: Saturday, March 7, 2015

package pong.control;

import pong.lang.Language;
import pong.mode.PongCanvas;
import pong.screen.AboutScreen;
import pong.screen.Pong;
import pong.sound.Sounds;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

/**
 * Classe represantant les controles en-dessous du jeu.
 */
public class PongControls extends JPanel {

    /**
     * Le Pong qu'une instance de cette classe va controler.
     */
    private Pong pong;

    /**
     * Le PongCanvas du Pong qu'une instance de cette classe va controler.
     */
    private PongCanvas pongCanvas;

    /**
     * Bouton qui permet de mettre en pause/reprendre le jeu.
     */
    private JButton pauseButton;

    /**
     * Bouton qui permet de quitter le jeu.
     */
    private JButton quitButton;

    /**
     * Bouton qui permet d'ouvrir les preferences du jeu.
     */
    private JButton preferencesButton;

    /**
     * Bouton qui permet de changer le mode de jeu.
     */
    private JButton changeModeButton;

    /**
     * Bouton qui permet d'afficher de l'aide.
     */
    private JButton helpButton;

    /**
     * Decrit si le jeu est en pause.
     */
    private boolean paused = false;

    /**
     * Map qui permet de chercher un terme dans la langue voulue.
     */
    private Map<String, String> lang = Language.getLanguageMap(Language.getLoc());

    /**
     * Permet de n'avoir qu'une fenetre de preferences ouverte a la fois.
     */
    private SingleWindowManager<PongPreferencesWindow> prefsSingleWindowManager = new SingleWindowManager<PongPreferencesWindow>();

    /**
     * Permet de n'avoir qu'une fenetre d'aide ouverte a la fois.
     */
    private SingleWindowManager<AboutScreen> aboutScreenSingleWindowManager = new SingleWindowManager<AboutScreen>();

    public PongControls(Pong pong) {

        this.pong = pong;
        this.pongCanvas = pong.getPongCanvas();
        initGUI();
    }

    /**
     * Initialise l'interface graphique.
     */
    private void initGUI() {

        setFocusable(false);
        setLayout(new FlowLayout());
        addButtons();
    }

    /**
     * Initialise les boutons.
     */
    private void initButtons() {

        pauseButton = new JButton(lang.get("pauseText"));
        quitButton = new JButton(lang.get("quitText"));
        preferencesButton = new JButton(lang.get("preferencesText"));
        changeModeButton = new JButton(lang.get("newGameText"));

        helpButton = new JButton();
        helpButton.setFocusable(false);

        // si l'utilisateur est sur un Mac, on peut utiliser le bouton d'aide specifique a Mac OS X.
        if (System.getProperty("os.name").toLowerCase().startsWith("mac os x")) {
            helpButton.putClientProperty("JButton.buttonType", "help");
        } else {

            // on veut avoir un bouton tres petit avec un point d'interrogation.
            helpButton.setText("?");
            helpButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 10));
            helpButton.setMargin(new Insets(-10, -10, -10, -10));  // sinon, le texte du bouton ne s'affiche pas.
            helpButton.setPreferredSize(new Dimension(20, 20));  // on reduit le bouton a une taille tres petite.
        }

        JButton[] buttons = new JButton[] {pauseButton, quitButton, preferencesButton, changeModeButton};

        for (JButton b : buttons) {
            b.setFocusable(false);  // les boutons ne doivent pas etre focusable parce que sinon, les KeyListeners du
                                    // jeu ne marcheront plus. Cela ameliore aussi l'aspect visuel des boutons.
        }
    }

    /**
     * Ajoute les boutons.
     */
    private void addButtons() {

        initButtons();
        addListeners();
        add(quitButton);
        add(changeModeButton);
        add(pauseButton);
        add(preferencesButton);
        add(helpButton);
    }

    /**
     * Decrit ce qui doit se passer si l'utilisateur clique sur "Quitter".
     */
    private void handleQuitEvent() {

        pongCanvas.end();  // on finit l'animation
        pong.dispose();  // on ferme la fenetre
        Sounds.deinitSounds();  // on deinitialise les sons
        System.exit(0);
    }

    /**
     * Decrit ce qui doit se passer si l'utilisateur clique sur "Nouveau Jeu...".
     */
    private void handleChangeModeEvent() {

        pause();  // on met en pause le jeu quand l'utilisateur ouvre une autre fenetre.
        pong.showPongModeChooser();
    }

    /**
     * Decrit ce qui doit se passer si l'utilisateur clique sur "Pause".
     */
    private void handlePauseEvent() {

        if (!paused) {
            pongCanvas.pause();
            paused = true;
            pauseButton.setText(lang.get("playText"));
        } else {
            pongCanvas.resume();
            paused = false;
            pauseButton.setText(lang.get("pauseText"));
        }
    }

    /**
     * Permet de mettre le PongCanvas en pause.
     */
    public void pause() {

        pongCanvas.pause();
        paused = true;
        pauseButton.setText(lang.get("playText"));
    }

    /**
     * Permet de reprendre le jeu.
     */
    public void resume() {

        pongCanvas.resume();
        paused = false;
        pauseButton.setText(lang.get("pauseText"));
    }

    /**
     * Decrit ce qui doit se passer si l'utilisateur clique sur "Preferences...".
     */
    private void handlePreferencesEvent() {

        pause();  // on met en pause le jeu quand l'utilisateur ouvre une autre fenetre.

        // seulement une fenetre de preferences peut etre ouverte a la fois.
        if (!prefsSingleWindowManager.isOccupied()) {
            prefsSingleWindowManager.setComponent(new PongPreferencesWindow(pong, pongCanvas));
        }
        if (prefsSingleWindowManager.isOccupied()){
            prefsSingleWindowManager.getComponent().setVisible(true);
        }
    }

    /**
     * Decrit ce qui doit se passer si l'utilisateur clique le bouton d'aide.
     */
    private void handleHelpEvent() {

        pause();

        // seuelement une fenetre d'aide peut etre ouverte a la fois.
        if (!aboutScreenSingleWindowManager.isOccupied()) {
            aboutScreenSingleWindowManager.setComponent(new AboutScreen(pong));
        }
        if (aboutScreenSingleWindowManager.isOccupied()) {
            aboutScreenSingleWindowManager.getComponent().setVisible(true);
        }
    }

    /**
     * Ajoute les ecouteurs aux boutons.
     */
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

        helpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                handleHelpEvent();
            }
        });

        // Ces deux lignes permettent a l'utilisateur d'appuyer sur espace pour mettre en pause/reprendre le jeu.
        pongCanvas.getInputMap().put(KeyStroke.getKeyStroke(' '), "Game Paused");
        pongCanvas.getActionMap().put("Game Paused", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handlePauseEvent();
            }
        });
    }

    /**
     * Methode qui doit etre appelee quand le PongCanvas change pour que les boutons se remettent a leurs valeurs
     * initiales et pour que cette classe soit avertie que le PongCanvas n'est plus le meme.
     */
    public void refresh() {

        pongCanvas = pong.getPongCanvas();
        pauseButton.setEnabled(true);
        preferencesButton.setEnabled(true);
        pongCanvas.getInputMap().put(KeyStroke.getKeyStroke(' '), "Game Paused");
        // il faut fermer la fenetre de preferences car il faudra peut-etre l'adapter au nouveau mode de jeu.
        prefsSingleWindowManager.disposeWindow();
        pongCanvas.getActionMap().put("Game Paused", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handlePauseEvent();
            }
        });
    }

    public JButton getPauseButton() {

        return pauseButton;
    }

    public JButton getPreferencesButton() {

        return preferencesButton;
    }
}