// File: BallAnimation.java
// Author: Leo Horne
// Date Created: 3/15/15 6:20 PM

package pong.animation;

import pong.collision.PongPaddleCollisionDetector;
import pong.mode.PongCanvas;
import pong.paddle.PaddleMode;

/**
 * Implementation of Runnable to animate the ball in a Thread.
 */

public class BallAnimation implements Runnable {

    private static final int FRAMES_PER_SECOND = 500;
    private static final int TIMER_DELAY = 1000 / FRAMES_PER_SECOND;
    private PongCanvas pongCanvas;

    public BallAnimation(PongCanvas pongCanvas) {

        this.pongCanvas = pongCanvas;
    }

    @Override
    public void run() {

        long beforeTime, sleepTime, timeDifference;

        beforeTime = System.currentTimeMillis();

        while (pongCanvas.getPlayerScore(1) < pongCanvas.getMaxScore() && pongCanvas.getPlayerScore(2) < pongCanvas.getMaxScore()) {

            for (PongPaddleCollisionDetector pongPaddleCollisionDetector : pongCanvas.getCollisionDetectors()) {
                pongPaddleCollisionDetector.checkCollisions();
                if (pongPaddleCollisionDetector.getPaddle().getPaddleMode() == PaddleMode.AI || pongPaddleCollisionDetector.getPaddle().getPaddleMode() == PaddleMode.HUMAN) {
                    pongCanvas.repaint((int)pongPaddleCollisionDetector.getPaddle().getX(), 0, PongCanvas.INITIAL_PADDLE_WIDTH, pongCanvas.getPanelHeight());
                }
            }

            pongCanvas.getPongWallCollisionDetector().checkCollisions();
            pongCanvas.getBall().move();
            pongCanvas.repaint((int)(pongCanvas.getBall().getBallX() - pongCanvas.getBall().getXTrajectory() - 50),
                    (int)(pongCanvas.getBall().getBallY() - pongCanvas.getBall().getYTrajectory() - 50),
                    (int)(pongCanvas.getBall().getBallDiameter() + 2 * pongCanvas.getBall().getXTrajectory() + 100),
                    (int)(pongCanvas.getBall().getBallDiameter() + 2 * pongCanvas.getBall().getYTrajectory() + 100)
                    /*pongCanvas.getBall().getBounds()*/);

            timeDifference = System.currentTimeMillis() - beforeTime;
            sleepTime = TIMER_DELAY - timeDifference;

            if (sleepTime < 0) {
                sleepTime = 2;
            }

            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            beforeTime = System.currentTimeMillis();
        }
    }
}