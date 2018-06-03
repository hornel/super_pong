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

public class TwoAIAndHumanPlayerPongCanvas extends TwoPlayerPongCanvas {

    private Timer paddle1Timer;
    private Timer paddle2Timer;

    @Override
    void initPaddles() {

        setPaddle1(new Paddle(this, INITIAL_PADDLE1_X, INITIAL_PADDLE1_Y, INITIAL_PADDLE_WIDTH, INITIAL_PADDLE_HEIGHT, Paddle.LEFT, PaddleMode.HUMAN));
        setPaddle2(paddle2 = new Paddle(this, INITIAL_PADDLE2_X, INITIAL_PADDLE2_Y, INITIAL_PADDLE_WIDTH, INITIAL_PADDLE_HEIGHT, Paddle.RIGHT, PaddleMode.AI));

        paddle1Timer = paddle1.getPlayerModeTimer(KeyEvent.VK_UP, KeyEvent.VK_DOWN);
        paddle2Timer = paddle2.getPlayerModeTimer(KeyEvent.VK_W, KeyEvent.VK_S);
        paddle1Timer.start();
        paddle2Timer.start();
    }

    @Override
    void addListeners() {

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {

                if (!isPaused()) {
                    Rectangle oldBounds = paddle1.getBounds();
                    if (e.getY() <= getHeight() - PongPreferencesEditor.PADDING - paddle1.getHeight() / 2 && e.getY() > PongPreferencesEditor.PADDING + paddle1.getHeight() / 2) {
                        paddle1.setPaddleY(e.getY() - paddle1.getHeight() / 2);
                    }
                    if (e.getY() >= getHeight() - PongPreferencesEditor.PADDING - paddle1.getHeight() / 2) {
                        paddle1.setPaddleY(getHeight() - PongPreferencesEditor.PADDING - paddle1.getHeight());
                    }
                    if (e.getY() <= PongPreferencesEditor.PADDING) {
                        paddle1.setPaddleY(PongPreferencesEditor.PADDING);
                    }
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
        paddle1Timer.stop();
        paddle2Timer.stop();
    }

    public PaddleAI getAI() {

        return (PaddleAI) paddle2Timer.getActionListeners()[0];
    }
}