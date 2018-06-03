// File: PongCollisionDetector.java
// Author: Leo
// Date Created: 4/4/15 8:14 PM
// Version: 0.1a

package pong.collision;

import pong.ball.Ball;
import pong.mode.PongCanvas;
import pong.mode.SinglePlayerArkanoidPongCanvas;
import pong.sound.PongSoundPlayer;

public class PongWallCollisionDetector implements CollisionDetector {

    private Ball ball;
    private PongCanvas pongCanvas;
    private boolean shouldServeFromMiddle = false;

    private PongSoundPlayer wallHitSound = new PongSoundPlayer("wallHit.wav");      // sound to be played when top or bottom is hit

    public PongWallCollisionDetector(PongCanvas pongCanvas) {

        this.pongCanvas = pongCanvas;
        this.ball = pongCanvas.getBall();
    }

    public PongWallCollisionDetector(SinglePlayerArkanoidPongCanvas pongCanvas) {

        this.pongCanvas = pongCanvas;
        this.ball = pongCanvas.getBall();
        shouldServeFromMiddle = true;
    }

    public boolean ballHitTopOrBottom() {

        return ball.getBallY() >= pongCanvas.getPanelHeight() - ball.getBallDiameter() || ball.getBallY() <= 0;
    }

    public boolean ballOutOfBounds() {

        return ball.getBallX() > pongCanvas.getPanelWidth() + ball.getBallDiameter() || ball.getBallX() < -ball.getBallDiameter();
    }

    @Override
    public void checkCollisions() {

        if (!shouldServeFromMiddle && ballOutOfBounds()) {
            ball.serve();

        } else if (shouldServeFromMiddle && ballOutOfBounds()) {
            ball.serveFromMiddle();

        } else if (ballHitTopOrBottom()) {
            ball.reverseYTrajectory();
            wallHitSound.playSound();
        }
    }
}