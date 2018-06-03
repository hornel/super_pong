// File: TwoHumanPlayerPongCanvas.java
// Author: Leo
// Date Created: 4/10/15 5:15 PM
// Version: 0.1a

package pong.mode;

import pong.paddle.Paddle;
import pong.paddle.PaddleMode;

import javax.swing.*;
import java.awt.event.KeyEvent;

/**
 * Decrit un jeu de Pong standard avec deux joueurs humains.
 */
public class TwoHumanPlayerPongCanvas extends TwoPlayerPongCanvas {

    /**
     * Le Timer pour deplacer la raquette de gauche avec les touches.
     */
    private Timer paddle1Timer;

    /**
     * Le Timer pour deplacer la raquette de droite avec les touches.
     */
    private Timer paddle2Timer;

    @Override
    void initPaddles() {

        // On dit a la superclasse quelles sont les raquettes.
        setPaddle1(new Paddle(this, INITIAL_PADDLE1_X, INITIAL_PADDLE1_Y, INITIAL_PADDLE_WIDTH, INITIAL_PADDLE_HEIGHT, Paddle.LEFT, PaddleMode.HUMAN));
        setPaddle2(new Paddle(this, getWidth() - INITIAL_PADDLE_WIDTH - 10, INITIAL_PADDLE2_Y, INITIAL_PADDLE_WIDTH, INITIAL_PADDLE_HEIGHT, Paddle.RIGHT, PaddleMode.HUMAN));

        // On cherche les Timers d'animation de deplacement des raquettes (ce qui ajoute aussi les Key Bindings necessaires
        // a ce PongCanvas) et on les demarre.
        paddle1Timer = paddle1.getPlayerModeTimer(KeyEvent.VK_W, KeyEvent.VK_S);
        paddle2Timer = paddle2.getPlayerModeTimer(KeyEvent.VK_UP, KeyEvent.VK_DOWN);
        paddle1Timer.start();
        paddle2Timer.start();
    }

    @Override
    public PongMode getMode() {

        return PongMode.DUAL_PLAYER_HUMAN_VS_HUMAN;
    }

    @Override
    protected void addListeners() {

    }

    @Override
    public void end() {

        super.end();
        // Lorsqu'on arrete le jeu, il faut aussi arreter les Timers d'animation de deplacement des raquettes.
        paddle1Timer.stop();
        paddle2Timer.stop();
    }
}