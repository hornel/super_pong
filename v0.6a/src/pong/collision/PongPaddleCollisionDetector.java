// File: PongCollisionDetector.java
// Author: Leo
// Date Created: 4/4/15 8:14 PM
// Version: 0.1a

package pong.collision;

import pong.ball.Ball;
import pong.mode.PongCanvas;
import pong.paddle.Paddle;
import pong.sound.PongSoundPlayer;

public class PongPaddleCollisionDetector implements CollisionDetector {

    private Ball ball;
    private Paddle paddle;
    private PongCanvas pongCanvas;

    private PongSoundPlayer paddleHitSound = new PongSoundPlayer("paddleHit.wav");  // sound to be played when paddle is hit

    public PongPaddleCollisionDetector(PongCanvas pongCanvas, Paddle paddle) {

        this.pongCanvas = pongCanvas;
        this.ball = pongCanvas.getBall();
        this.paddle = paddle;
    }

    public boolean ballHitPaddleOuterEdge() {

        if (paddle.getSide() == Paddle.LEFT) {
            return ball.getLeftLimit() <= paddle.getOuterLimit();
        } else {
            return ball.getRightLimit() >= paddle.getOuterLimit();
        }
    }

    public boolean ballNotBehindPaddleOuterEdge() {

        if (paddle.getSide() == Paddle.LEFT) {
            return ball.getLeftLimit() >= paddle.getInnerLimit(ball.getXTrajectory());
        } else {
            return ball.getRightLimit() <= paddle.getInnerLimit(ball.getXTrajectory());
        }
    }  // necessary to make sure the ball doesn't get stuck behind the paddle

    public boolean ballDidNotSurpassPaddleUpperEdge() {

        return ball.getLowerLimit() >= paddle.getUpperLimit();
    }

    public boolean ballDidNotSurpassPaddleLowerEdge() {

        return ball.getUpperLimit() <= paddle.getLowerLimit();
    }

    public boolean ballHitPaddleUpperEdge() {

        if (paddle.getSide() == Paddle.LEFT) {
            return ball.getLeftLimit() <= paddle.getOuterLimit() && ball.getLowerLimit() >= paddle.getUpperLimit() && ball.getLowerLimit() < paddle.getLowerLimit() && ballNotBehindPaddle();
        } else {
            return ball.getRightLimit() >= paddle.getOuterLimit() && ball.getLowerLimit() >= paddle.getUpperLimit() && ball.getLowerLimit() < paddle.getLowerLimit() && ballNotBehindPaddle();
        }
    }

    public boolean ballHitPaddleLowerEdge() {

        if (paddle.getSide() == Paddle.LEFT) {
            return ball.getLeftLimit() <= paddle.getOuterLimit() && ball.getUpperLimit() <= paddle.getLowerLimit() && ball.getUpperLimit() > paddle.getUpperLimit() && ballNotBehindPaddle();
        } else {
            return ball.getRightLimit() >= paddle.getOuterLimit() && ball.getUpperLimit() <= paddle.getLowerLimit() && ball.getUpperLimit() > paddle.getUpperLimit() && ballNotBehindPaddle();
        }
    }

    public boolean ballNotBehindPaddle() {

        if (paddle.getSide() == Paddle.LEFT) {
            return ball.getRightLimit() >= paddle.getBehindLimit();
        } else {
            return ball.getLeftLimit() <= paddle.getBehindLimit();
        }
    }

    @Override
    public void checkCollisions() {

        if (((ballHitPaddleOuterEdge() && ballNotBehindPaddleOuterEdge()) && ballDidNotSurpassPaddleUpperEdge() && ballDidNotSurpassPaddleLowerEdge()) || (ballHitPaddleUpperEdge() || ballHitPaddleLowerEdge())) {
            checkPaddleCollisions();
            paddleHitSound.playSound();
            ball.reverseXTrajectory();
            if (pongCanvas.isPlayer1Turn()) {
                pongCanvas.setPlayer1Turn(false);
            } else {
                pongCanvas.setPlayer1Turn(true);
            }
            performCustomAction();
        }
    }

    /**
     * Changes the y trajectory based on the location where the ball
     * hits the paddle.
     * <p></p>
     * The method uses the following algorithm:
     * 1. Calculates the distance between the location where the ball hit the
     * paddle and the center of the paddle.
     * 2. Translates this distance into a percentage. A ball hit on the edge of
     * the paddle will have a percentage close to 100% and a ball that hits
     * near the center of the paddle will have a percentage close to 0%.
     * 3. Multiplies this percentage by the maximum y bend factor.
     * 4. Assigns this value to the y trajectory.
     */
    private void checkPaddleCollisions() {

        double paddleCenter;
        double hitDistance;
        double hitPercentile;
        double ballCenter;

        paddleCenter = paddle.getYCenter();
        ballCenter = ball.getBallY() + ball.getBallDiameter() / 2;
        hitDistance = ballCenter - paddleCenter;

        hitPercentile = hitDistance / ((paddle.getHeight() + ball.getBallDiameter()) / 2);

        ball.setYTrajectory(hitPercentile * ball.getMaxYTrajectory());

//        System.out.println("\n------------------- BALL HIT PADDLE -------------------");
//        System.out.println("Distance from center of paddle: " + hitDistance + " px");
//        System.out.println("Hit percentile: " + hitPercentile * 100 + "%");
//        System.out.println("Maximum Y Trajectory: " + ball.getMaxYTrajectory());
//        System.out.println("X Trajectory: " + ball.getXTrajectory());
//        System.out.println("Y Trajectory: " + ball.getYTrajectory());
//        System.out.println("-------------------------------------------------------");
    }

    public void performCustomAction() {}
}