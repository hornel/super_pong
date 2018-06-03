// File: PaddleMover.java
// Author: Leo Horne
// Date Created: 3/16/15 5:38 PM
// Version: 0.1a

package pong.paddle;

import pong.mode.PongCanvas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Classe qui permet a l'utilisateur de deplacer la raquette un utilisant des touches donnees.
 */
public class PaddleMover implements ActionListener {

    /**
     * Le delai entre les deplacements de la raquette.
     */
    public final static int TIMER_DELAY = 30;

    /**
     * La vitesse de la raquette.
     */
    private int paddleSpeed = 15;

    /**
     * Decrit si la touche pour avancer vers le haut est appuye.
     */
    private boolean upIsPressed = false;

    /**
     * Decrit si la touche pour avancer vers le bas est appuye.
     */
    private boolean downIsPressed = false;

    /**
     * La raquette qui doit etre deplacee.
     */
    private Paddle paddle;

    /**
     * Le PongCanvas ou la raquette doit etre deplacee.
     */
    private PongCanvas pongCanvas;

    /**
     * Numero decrivant la touche pour avancer vers le haut. La valeur de cette touche correspond a la valeur pour le
     * meme touche definie dans java.awt.KeyEvent.
     */
    private int upKey;

    /**
     * Numero decrivant la touche pour avancer vers le haut. La valeur de cette touche correspond a la valeur pour le
     * meme touche definie dans java.awt.KeyEvent.
     */
    private int downKey;

    public PaddleMover(Paddle paddle, int upKey, int downKey, PongCanvas pongCanvas) {

        this.paddle = paddle;
        this.upKey = upKey;
        this.downKey = downKey;
        this.pongCanvas = pongCanvas;
    }

    /**
     * Deplace la raquette.
     */
    private void movePaddle() {

        // Pour qu'on puisse deplacer la raquette, il faut que les conditions suivantes soient remplies:
        // 1. Il doit etre possible de deplacer la raquette
        // 2. La raquette ne doit pas etre en train de sortir du jeu.
        // 3. La touche pour deplacer la raquette doit etre apuyee.
        // 4. Le jeu ne peut pas etre en pause.

        if (paddle.isMovable() && paddle.getUpperLimit() - 10 > 0 && upIsPressed && !pongCanvas.isPaused()) {
            Rectangle oldBounds = paddle.getBounds();
            paddle.setPaddleY(paddle.getY() - paddleSpeed);
            pongCanvas.repaint(oldBounds);
            pongCanvas.repaint(paddle.getBounds());
        }
        if (paddle.isMovable() && paddle.getLowerLimit() + 10 < pongCanvas.getHeight() && downIsPressed && !pongCanvas.isPaused()) {
            Rectangle oldBounds = paddle.getBounds();
            paddle.setPaddleY(paddle.getY() + paddleSpeed);
            pongCanvas.repaint(oldBounds);
            pongCanvas.repaint(paddle.getBounds());
        }
    }

    /**
     * Ajoute les Key Bindings au PongCanvas pour deplacer la raquette.
     */
    public void addKeyBindings() {

        InputMap im = pongCanvas.getInputMap();
        ActionMap am = pongCanvas.getActionMap();

        // L'utilisateur a appuye sur la touche pour avancer vers le haut.
        im.put(KeyStroke.getKeyStroke(upKey, 0, false), "up PRESSED" + paddle.getSide());
        // L'utilisateur a appuye sur la touche pour avancer vers le bas.
        im.put(KeyStroke.getKeyStroke(downKey, 0, false), "down PRESSED" + paddle.getSide());
        // L'utilisateur a lache la touche pour avancer vers le haut.
        im.put(KeyStroke.getKeyStroke(upKey, 0, true), "up RELEASED" + paddle.getSide());
        // L'utilisateur a lache la touche pour avancer vers le bas.
        im.put(KeyStroke.getKeyStroke(downKey, 0, true), "down RELEASED" + paddle.getSide());

        am.put("up PRESSED" + paddle.getSide(), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                upIsPressed = true;
            }
        });

        am.put("down PRESSED" + paddle.getSide(), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                downIsPressed = true;
            }
        });

        am.put("up RELEASED" + paddle.getSide(), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                upIsPressed = false;
            }
        });

        am.put("down RELEASED" + paddle.getSide(), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                downIsPressed = false;
            }
        });
    }

    // Methode appelee par le Timer qui va avancer la raquette.
    @Override
    public void actionPerformed(ActionEvent actionEvent) {

        movePaddle();
    }
}