// File: BallAnimation.java
// Author: Leo Horne
// Date Created: 3/15/15 6:20 PM
// Version: 0.1a

package pong;

/**
 * Implementation of Runnable to animate the ball in a Thread.
 */

class BallAnimation implements Runnable {

    private PongCanvas pongCanvas;
    private static final int FRAMES_PER_SECOND = 40;
    private static final int TIMER_DELAY = 1000/FRAMES_PER_SECOND;

    public BallAnimation(PongCanvas pongCanvas) {
        this.pongCanvas = pongCanvas;
    }

    @Override
    public void run() {

        long beforeTime, sleepTime, timeDifference;

        beforeTime = System.currentTimeMillis();

        while (pongCanvas.getPlayerScore(1) < pongCanvas.getMaxScore() && pongCanvas.getPlayerScore(2) < pongCanvas.getMaxScore()) {

            pongCanvas.getBall().checkCollisions();
            pongCanvas.getBall().move();
            pongCanvas.repaint();

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
