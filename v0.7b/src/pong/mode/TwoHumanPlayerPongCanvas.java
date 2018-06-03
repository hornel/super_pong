// File: TwoHumanPlayerPongCanvas.java
// Author: Leo
// Date Created: 4/10/15 5:15 PM
// Version: 0.1a

package pong.mode;

import pong.paddle.Paddle;
import pong.paddle.PaddleMode;

import javax.swing.*;
import java.awt.event.KeyEvent;

public class TwoHumanPlayerPongCanvas extends TwoPlayerPongCanvas {

    private Timer paddle1Timer;
    private Timer paddle2Timer;

    @Override
    void initPaddles() {

        setPaddle1(new Paddle(this, INITIAL_PADDLE1_X, INITIAL_PADDLE1_Y, INITIAL_PADDLE_WIDTH, INITIAL_PADDLE_HEIGHT, Paddle.LEFT, PaddleMode.HUMAN));
        setPaddle2(new Paddle(this, getWidth() - INITIAL_PADDLE_WIDTH - 10, INITIAL_PADDLE2_Y, INITIAL_PADDLE_WIDTH, INITIAL_PADDLE_HEIGHT, Paddle.RIGHT, PaddleMode.HUMAN));

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
    void addListeners() {

    }

    @Override
    public void end() {

        super.end();
        paddle1Timer.stop();
        paddle2Timer.stop();
    }
}