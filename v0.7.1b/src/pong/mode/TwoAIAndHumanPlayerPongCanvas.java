// File: TwoAIAndHumanPlayerPongCanvas.java
// Author: Leo
// Date Created: 4/10/15 5:18 PM
// Version: 0.1a

package pong.mode;

import pong.control.PongPreferencesEditor;
import pong.paddle.Paddle;
import pong.paddle.PaddleAI;
import pong.paddle.PaddleMode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

/**
 * Classe decrivant un jeu entre un joueur humain et un joueur controle par l'IA.
 */
public class TwoAIAndHumanPlayerPongCanvas extends TwoPlayerPongCanvas {

    /**
     * Le Timer pour deplacer la raquette de l'utilisateur avec les touches.
     */
    private Timer paddle1Timer;

    /**
     * Le Timer pour deplacer la raquette de l'IA avec les touches.
     */
    private Timer paddle2Timer;

    @Override
    void initPaddles() {

        // On dit a la superclasse quelles sont les raquettes.
        setPaddle1(new Paddle(this, INITIAL_PADDLE1_X, INITIAL_PADDLE1_Y, INITIAL_PADDLE_WIDTH, INITIAL_PADDLE_HEIGHT, Paddle.LEFT, PaddleMode.HUMAN));
        setPaddle2(new Paddle(this, INITIAL_PADDLE2_X, INITIAL_PADDLE2_Y, INITIAL_PADDLE_WIDTH, INITIAL_PADDLE_HEIGHT, Paddle.RIGHT, PaddleMode.AI));

        // On cherche les Timers d'animation de deplacement des raquettes (ce qui ajoute aussi les Key Bindings necessaires
        // a ce PongCanvas) et on les demarre.
        paddle1Timer = paddle1.getPlayerModeTimer(KeyEvent.VK_UP, KeyEvent.VK_DOWN);
        paddle2Timer = paddle2.getPlayerModeTimer(KeyEvent.VK_W, KeyEvent.VK_S);
        paddle1Timer.start();
        paddle2Timer.start();
    }

    @Override
    protected void addListeners() {

        // Comme il n'y a qu'une personne qui sera en train de jouer ce jeu, le joueur pourra deplacer la raquette
        // avec la souris. Il faut toutefois que la raquette ne puisse pas depasser les limites du jeu.
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {

                //  On ne peut deplacer les raquettes que si le jeu n'est pas en pause.
                if (!isPaused()) {
                    // Il faudra repeindre la ou la raquette etait avant le deplacement.
                    Rectangle oldBounds = paddle1.getBounds();
                    // On veut que le milieu de la raquette suive la souris, mais les raquettes ne doivent pas sortir du
                    // jeu. Si la coordonnee y de la souris par rapport a la fenetre entre la demi-hauteur de la raquette
                    // + PADDING et la hauteur du jeu - la demi-hauteur de la raquette - PADDING, on peut deplacer
                    // la raquette librement.
                    if (e.getY() <= getHeight() - PongPreferencesEditor.PADDING - paddle1.getHeight() / 2 && e.getY() > PongPreferencesEditor.PADDING + paddle1.getHeight() / 2) {
                        paddle1.setPaddleY(e.getY() - paddle1.getHeight() / 2);
                    }
                    // Si la coordonnee y de la souris par rapport a la fenetre est dans la hauteur du jeu - la
                    // demi-hauteur de la raquette - PADDING, la position de la raquette vaut la hauteur du jeu -
                    // la hauteur de la raquette - PADDING.
                    if (e.getY() >= getHeight() - PongPreferencesEditor.PADDING - paddle1.getHeight() / 2) {
                        paddle1.setPaddleY(getHeight() - PongPreferencesEditor.PADDING - paddle1.getHeight());
                    }
                    // Si la coordonnee y de la souris par rapport a la fenetre est dans la demi-hauteur de la raquette
                    // + PADDING, la position de la raquette vaut PADDING.
                    if (e.getY() <= PongPreferencesEditor.PADDING) {
                        paddle1.setPaddleY(PongPreferencesEditor.PADDING);
                    }
                    // On redessine ou la raquette etait avant le deplacement et ou elle se trouve apres le deplacement.
                    repaint(oldBounds);
                    repaint(paddle1.getBounds());
                }
            }
        });
    }

    @Override
    public PongMode getMode() {

        return PongMode.DUAL_PLAYER_HUMAN_VS_AI;
    }

    @Override
    public void end() {

        super.end();
        // Lorsqu'on arrete le jeu, il faut aussi arreter les Timers d'animation de deplacement des raquettes.
        paddle1Timer.stop();
        paddle2Timer.stop();
    }

    /**
     * Cherche l'IA de la raquette controlee par l'ordinateur.
     */
    public PaddleAI getAI() {

        // L'IA est une implementation de ActionListener. Il suffit donc de chercher l'ActionListener du Timer
        // d'animation de l'IA pour avoir l'IA, puisque l'animation utilise l'algorithme de l'IA.
        return (PaddleAI) paddle2Timer.getActionListeners()[0];
    }
}