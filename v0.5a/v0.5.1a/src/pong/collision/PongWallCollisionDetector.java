// File: PongCollisionDetector.java
// Author: Leo
// Date Created: 4/4/15 8:14 PM
// Version: 0.1a

package pong.collision;

import pong.mode.PongCanvas;
import pong.ball.Ball;
import pong.sound.PongSoundPlayer;

public class PongWallCollisionDetector {

    private Ball ball;
    private PongCanvas pongCanvas;

    private PongSoundPlayer wallHitSound = new PongSoundPlayer("wallHit.wav");      // sound to be played when top or bottom is hit

    public PongWallCollisionDetector(PongCanvas pongCanvas) {

        this.pongCanvas = pongCanvas;
        this.ball = pongCanvas.getBall();
    }

    public boolean ballHitTopOrBottom() {

        return ball.getBallY() >= pongCanvas.getPanelHeight() - ball.getBallDiameter() || ball.getBallY() <= 0;
    }

    public boolean ballOutOfBounds() {

        return ball.getBallX() > pongCanvas.getPanelWidth() + ball.getBallDiameter() || ball.getBallX() < -ball.getBallDiameter();
    }

    public void checkCollisions() {

        if (ballOutOfBounds()) {
            ball.serve();

        } else if (ballHitTopOrBottom()) {
            ball.reverseYTrajectory();
            wallHitSound.playSound();
        }
    }
}