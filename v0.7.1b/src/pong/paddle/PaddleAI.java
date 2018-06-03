// File: PongAI.java
// Author: Leo
// Date Created: 4/5/15 9:59 PM
// Version: 0.1a

package pong.paddle;

import pong.ball.Ball;
import pong.control.PongPreferencesEditor;
import pong.mode.PongCanvas;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Intelligence artificielle pour une raquette.
 */
public class PaddleAI implements ActionListener {

    /**
     * Constante qui dit qu'il faut avancer vers le haut.
     */
    private final static int UP = -1;

    /**
     * Constante qui dit qu'il faut avancer vers le bas.
     */
    private final static int DOWN = 1;

    /**
     * Constante qui dit qu'il ne faut pas avancer.
     */
    private final static int NO_DIRECTION = 0;

    /**
     * Valeur determinant la difficulte de l'IA.
     */
    private int aiDifficulty = PongPreferencesEditor.INITIAL_AI_DIFFICULTY;

    /**
     * Valeur determinant la vitesse de l'IA.
     */
    private double paddleSpeed;

    /**
     * La balle que l'IA doit frapper.
     */
    private Ball ball;

    /**
     * La raquette que l'IA doit controler.
     */
    private Paddle paddle;

    /**
     * Le PongCanvas ou l'IA se trouve.
     */
    private PongCanvas pongCanvas;

    public PaddleAI(PongCanvas pongCanvas, Paddle paddle, Ball ball) {

        this.ball = ball;
        this.paddle = paddle;
        this.pongCanvas = pongCanvas;
    }

    /**
     * Cherche la direction vers laquelle il faut aller pour etre au meme endroit que la balle.
     */
    private int getDirection() {

        if (paddle.getUpperLimit() > ball.getLowerLimit()) {
            return UP;
        } else if (paddle.getLowerLimit() < ball.getUpperLimit()) {
            return DOWN;
        } else {
            return NO_DIRECTION;
        }
    }

    /**
     * Cherche la direction vers laquelle il faut aller pour aller au centre.
     */
    private int getDirectionTowardsMiddle() {

        if (paddle.getCenterY() > pongCanvas.getHeight() / 2 + 15) {
            return UP;
        } else if (paddle.getCenterY() < pongCanvas.getHeight() / 2 - 15) {
            return DOWN;
        } else {
            return NO_DIRECTION;
        }
    }

    /**
     * Deplace la raquette pour frapper la balle (si possible).
     */
    private void movePaddle() {

        // L'IA suit les regles suivantes:
        // 1. Elle reste/se deplace vers le centre a la vitesse de 5 fois la vitesse de la balle si:
        //     a. Elle vient de frapper la balle
        //     b. La balle ne se trouve pas dans sa moitie du terrain.
        // 2. Elle se deplace vers la balle a la vitesse de
        //    La racine carree de (la position x de la balle par rapport a la moitie du jeu ou se trouve la balle = un pourcentage)
        //    fois la difficulte de l'IA fois la vitesse de la balle
        //    si la balle se trouve dans sa moitie du terrain et elle ne l'a pas encore frappee.

        if (ball.getX() >= pongCanvas.getWidth() / 2 && (paddle.getSide() == Paddle.LEFT ? ball.getXTrajectory() < 0 : ball.getXTrajectory() > 0) && !pongCanvas.isPaused()) {
            Rectangle oldBounds = paddle.getBounds();
            setPaddleSpeed((Math.sqrt((ball.getX() - pongCanvas.getWidth() / 2) / pongCanvas.getWidth() / 2) * aiDifficulty * ball.getSpeed()));
            paddle.setPaddleY(paddle.getY() + (paddleSpeed * getDirection()));
            pongCanvas.repaint(oldBounds);
            pongCanvas.repaint(paddle.getBounds());
        } else if (!pongCanvas.isPaused()) {
            Rectangle oldBounds = paddle.getBounds();
            setPaddleSpeed((5 * ball.getSpeed()));
            paddle.setPaddleY(paddle.getY() + (paddleSpeed * getDirectionTowardsMiddle()));
            pongCanvas.repaint(oldBounds);
            pongCanvas.repaint(paddle.getBounds());
        }
    }

    // Methode utilisee par le Timer pour deplacer la raquette periodiquement.
    @Override
    public void actionPerformed(ActionEvent actionEvent) {

        movePaddle();
    }

    /**
     * Change la vitesse actuelle de l'IA.
     */
    public void setPaddleSpeed(double paddleSpeed) {

        this.paddleSpeed = paddleSpeed;
    }

    /**
     * Cherche la difficulte de l'IA.
     */
    public int getAiDifficulty() {

        return aiDifficulty;
    }

    /**
     * Change la difficulte de l'IA.
     */
    public void setAiDifficulty(int aiDifficulty) {

        this.aiDifficulty = aiDifficulty;
    }
}