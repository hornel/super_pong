// File: PaddleMover.java
// Author: Leo Horne
// Date Created: 3/16/15 5:38 PM
// Version: 0.1a

package pong;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

class PaddleMover implements ActionListener, KeyListener {

    private TwoHumanPlayerPongCanvas pongCanvas;
    int paddleSpeed = 15;
    private boolean upIsPressed = false, downIsPressed = false, wIsPressed = false, sIsPressed = false;

    public PaddleMover(TwoHumanPlayerPongCanvas pongCanvas) {
        this.pongCanvas = pongCanvas;
    }

    private void getPaddles() {


    }

    private void movePaddle() {

        if (pongCanvas.getPaddle1().getUpperLimit() - 10 > 0 && wIsPressed) {
            pongCanvas.getPaddle1().setPaddleY((int) pongCanvas.getPaddle1().getY() - paddleSpeed);
        }
        if (pongCanvas.getPaddle1().getLowerLimit() + 10 < pongCanvas.getPanelHeight() && sIsPressed) {
            pongCanvas.getPaddle1().setPaddleY((int) pongCanvas.getPaddle1().getY() + paddleSpeed);
        }
        if (pongCanvas.getPaddle2().getUpperLimit() - 10 > 0 && upIsPressed) {
            pongCanvas.getPaddle2().setPaddleY((int) pongCanvas.getPaddle2().getY() - paddleSpeed);
        }
        if (pongCanvas.getPaddle2().getLowerLimit() + 10 < pongCanvas.getPanelHeight() && downIsPressed) {
            pongCanvas.getPaddle2().setPaddleY((int) pongCanvas.getPaddle2().getY() + paddleSpeed);
        }
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {

        if (keyEvent.getKeyCode() == KeyEvent.VK_UP) {
            upIsPressed = true;
        }
        if (keyEvent.getKeyCode() == KeyEvent.VK_DOWN) {
            downIsPressed = true;
        }
        if (keyEvent.getKeyCode() == KeyEvent.VK_W) {
            wIsPressed = true;
        }
        if (keyEvent.getKeyCode() == KeyEvent.VK_S) {
            sIsPressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

        if (keyEvent.getKeyCode() == KeyEvent.VK_UP) {
            upIsPressed = false;
        }
        if (keyEvent.getKeyCode() == KeyEvent.VK_DOWN) {
            downIsPressed = false;
        }
        if (keyEvent.getKeyCode() == KeyEvent.VK_W) {
            wIsPressed = false;
        }
        if (keyEvent.getKeyCode() == KeyEvent.VK_S) {
            sIsPressed = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {}

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        movePaddle();
    }
}
