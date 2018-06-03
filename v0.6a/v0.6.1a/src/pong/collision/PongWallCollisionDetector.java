// File: PongCollisionDetector.java
// Author: Leo
// Date Created: 4/4/15 8:14 PM
// Version: 0.1a

package pong.collision;

import pong.ball.Ball;
import pong.mode.PongCanvas;
import pong.sound.PongSoundPlayer;

public class PongWallCollisionDetector implements CollisionDetector {

    private Ball ball;
    private PongCanvas pongCanvas;
    private boolean shouldServeFromMiddle = false;
    private boolean shouldServeFromSide = false;
    private int sideToServeFrom = 1;

    private PongSoundPlayer wallHitSound = new PongSoundPlayer("wallHit.wav");      // sound to be played when top or bottom is hit

    public PongWallCollisionDetector(PongCanvas pongCanvas) {

        this.pongCanvas = pongCanvas;
        this.ball = pongCanvas.getBall();
    }

    public PongWallCollisionDetector(PongCanvas pongCanvas, boolean shouldServeFromMiddle) {

        this(pongCanvas);
        this.shouldServeFromMiddle = shouldServeFromMiddle;
    }

    public PongWallCollisionDetector(PongCanvas pongCanvas, int sideToServeFrom) {

        this(pongCanvas);
        this.sideToServeFrom = sideToServeFrom;
        this.shouldServeFromSide = true;
    }

    public boolean ballHitTopOrBottom() {

//        if (ball.getBallY() >= pongCanvas.getPanelHeight() - ball.getBallDiameter() || ball.getBallY() <= 0) {
//            System.out.println("Y: " + (ball.getY() + ball.getBallDiameter()));
//            System.out.println("YTraj: " + ball.getYTrajectory() + "\n");
//        }
        return ball.getBallY() >= pongCanvas.getPanelHeight() - ball.getBallDiameter() || ball.getBallY() <= 0;
    }

    public boolean ballOutOfBounds() {

        return ball.getBallX() > pongCanvas.getPanelWidth() + ball.getBallDiameter() || ball.getBallX() < -ball.getBallDiameter();
    }

    @Override
    public void checkCollisions() {

        if (!shouldServeFromMiddle && !shouldServeFromSide && ballOutOfBounds()) {
            ball.serve();

        } else if (shouldServeFromMiddle && ballOutOfBounds()) {
            ball.serveFromMiddle();

        } else if (shouldServeFromSide && ballOutOfBounds()) {
            ball.serveFromSide(sideToServeFrom);

        } else if (ballHitTopOrBottom()) {
            ball.reverseYTrajectory();
            wallHitSound.playSound();
        }
    }
}