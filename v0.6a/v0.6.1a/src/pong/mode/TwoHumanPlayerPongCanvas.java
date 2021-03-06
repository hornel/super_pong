// File: TwoHumanPlayerPongCanvas.java
// Author: Leo
// Date Created: 4/10/15 5:15 PM
// Version: 0.1a

package pong.mode;

import pong.paddle.PaddleMode;
import pong.paddle.Paddle;

import java.awt.event.*;

public class TwoHumanPlayerPongCanvas extends TwoPlayerPongCanvas {

    @Override
    void initPaddles() {

        paddle1 = new Paddle(this, INITIAL_PADDLE1_X, INITIAL_PADDLE1_Y, INITIAL_PADDLE_WIDTH, INITIAL_PADDLE_HEIGHT, Paddle.LEFT, PaddleMode.HUMAN);
        paddle2 = new Paddle(this, INITIAL_PADDLE2_X, INITIAL_PADDLE2_Y, INITIAL_PADDLE_WIDTH, INITIAL_PADDLE_HEIGHT, Paddle.RIGHT, PaddleMode.HUMAN);

        paddle1.getPlayerModeTimer(KeyEvent.VK_W, KeyEvent.VK_S).start();
        paddle2.getPlayerModeTimer(KeyEvent.VK_UP, KeyEvent.VK_DOWN).start();
    }

    @Override
    public PongMode getMode() {

        return PongMode.DUAL_PLAYER_HUMAN_VS_HUMAN;
    }

    @Override
    void addListeners() {

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent mouseEvent) {

                if (isPlayer1Turn) {
                    paddle1.setPaddleY(mouseEvent.getY());
                } else {
                    paddle2.setPaddleY(mouseEvent.getY());
                }
            }
        });
    }
}