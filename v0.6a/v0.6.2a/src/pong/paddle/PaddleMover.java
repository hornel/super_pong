// File: PaddleMover.java
// Author: Leo Horne
// Date Created: 3/16/15 5:38 PM
// Version: 0.1a

package pong.paddle;

import pong.mode.PongCanvas;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class PaddleMover implements ActionListener, KeyListener {

    int paddleSpeed = 15;
    private boolean upIsPressed = false, downIsPressed = false;
    private Paddle paddle;
    private PongCanvas pongCanvas;
    private int upKey, downKey;

    public PaddleMover(Paddle paddle, int upKey, int downKey, PongCanvas pongCanvas) {

        this.paddle = paddle;
        this.upKey = upKey;
        this.downKey = downKey;
        this.pongCanvas = pongCanvas;
    }

    private void movePaddle() {

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

    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {

        if (keyEvent.getKeyCode() == upKey) {
            upIsPressed = true;
        }
        if (keyEvent.getKeyCode() == downKey) {
            downIsPressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

        if (keyEvent.getKeyCode() == upKey) {
            upIsPressed = false;
        }
        if (keyEvent.getKeyCode() == downKey) {
            downIsPressed = false;
        }
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {

        movePaddle();
    }
}