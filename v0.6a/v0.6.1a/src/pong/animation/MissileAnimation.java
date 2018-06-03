// File: MissileAnimation.java
// Author: Leo
// Date Created: 4/16/15 8:25 PM

package pong.animation;

import pong.collision.PongMissileCollisionDetector;
import pong.mode.TwoPlayerBattlePongCanvas;
import pong.paddle.Paddle;

public class MissileAnimation extends Thread {

    private static final int FRAMES_PER_SECOND = 40;
    private static final int TIMER_DELAY = 1000 / FRAMES_PER_SECOND;
    private TwoPlayerBattlePongCanvas pongCanvas;
    private Paddle paddle;

    private volatile boolean paused = false;

    private boolean threadStarted = false;

    public MissileAnimation(TwoPlayerBattlePongCanvas pongCanvas, Paddle paddle) {

        this.pongCanvas = pongCanvas;
        this.paddle = paddle;
    }

    public void setPaused(boolean b) {

        paused = b;
    }

    public boolean threadIsStarted() {

        return threadStarted;
    }

    @Override
    public void start() {

        super.start();
        threadStarted = true;
    }

    @Override
    public void run() {

        long beforeTime, sleepTime, timeDifference;
        sleepTime = TIMER_DELAY;

        beforeTime = System.currentTimeMillis();

        while (true) {

            if (!paused) {

                for (PongMissileCollisionDetector pongMissileCollisionDetector : pongCanvas.getPongMissileCollisionDetectors()) {
                    pongMissileCollisionDetector.checkCollisions();
                }

                paddle.moveMissile();
                pongCanvas.repaint();

                timeDifference = System.currentTimeMillis() - beforeTime;
                sleepTime = TIMER_DELAY - timeDifference;

                if (sleepTime < 0) {
                    sleepTime = 2;
                }

                beforeTime = System.currentTimeMillis();
            }

            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}