// File: PongAI.java
// Author: Leo
// Date Created: 4/5/15 9:59 PM
// Version: 0.1a

package pong.paddle;

import pong.ball.Ball;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PaddleAI implements ActionListener {

    private final static int UP = -1;
    private final static int DOWN = 1;
    private final static int NO_DIRECTION = 0;

    private int paddleSpeed = 15;

    private Ball ball;
    private Paddle paddle;

    public PaddleAI(Paddle paddle, Ball ball) {

        this.ball = ball;
        this.paddle = paddle;
    }

    private int getDirection() {

        if (paddle.getUpperLimit() > ball.getLowerLimit()) {
            return UP;
        } else if (paddle.getLowerLimit() < ball.getUpperLimit()) {
            return DOWN;
        } else {
            return NO_DIRECTION;
        }
    }

    private void movePaddle() {

        int direction = getDirection();
        paddle.setPaddleY(paddle.getY() + (paddleSpeed * direction));
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {

        movePaddle();
    }

    public int getPaddleSpeed() {

        return paddleSpeed;
    }

    public void setPaddleSpeed(int paddleSpeed) {

        this.paddleSpeed = paddleSpeed;
    }
}