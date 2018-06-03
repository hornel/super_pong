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

public class PaddleAI implements ActionListener {

    private final static int UP = -1;
    private final static int DOWN = 1;
    private final static int NO_DIRECTION = 0;
    private int aiDifficulty = PongPreferencesEditor.INITIAL_AI_DIFFICULTY;

    private double paddleSpeed;

    private Ball ball;
    private Paddle paddle;
    private PongCanvas pongCanvas;

    public PaddleAI(PongCanvas pongCanvas, Paddle paddle, Ball ball) {

        this.ball = ball;
        this.paddle = paddle;
        this.pongCanvas = pongCanvas;
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

    private int getDirectionTowardsMiddle() {

        if (paddle.getCenterY() > pongCanvas.getHeight() / 2 + 15) {
            return UP;
        } else if (paddle.getCenterY() < pongCanvas.getHeight() / 2 - 15) {
            return DOWN;
        } else {
            return NO_DIRECTION;
        }
    }

    private void movePaddle() {

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

    @Override
    public void actionPerformed(ActionEvent actionEvent) {

        movePaddle();
    }

    public double getPaddleSpeed() {

        return paddleSpeed;
    }

    public void setPaddleSpeed(double paddleSpeed) {

        this.paddleSpeed = paddleSpeed;
    }

    public int getAiDifficulty() {

        return aiDifficulty;
    }

    public void setAiDifficulty(int aiDifficulty) {

        this.aiDifficulty = aiDifficulty;
    }
}