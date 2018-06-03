// File: PongPaddleCollisionDetector.java
// Author: Leo
// Date Created: 4/4/15 8:14 PM

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

    public Paddle getPaddle() {

        return paddle;
    }

    public boolean ballHitPaddleOuterEdge() {

        if (paddle.getSide() == Paddle.LEFT) {
            return ball.getLeftLimit() <= paddle.getOuterLimit();
        } else {
            return ball.getRightLimit() >= paddle.getOuterLimit();
        }
    }

    public boolean ballDidNotSurpassPaddleUpperEdge() {

        return ball.getCenterY() >= paddle.getUpperLimit();
    }

    public boolean ballDidNotSurpassPaddleLowerEdge() {

        return ball.getCenterY() <= paddle.getLowerLimit();
    }

    public boolean ballNotBehindPaddle() {

        if (paddle.getSide() == Paddle.LEFT) {
            return ball.getRightLimit() >= paddle.getBehindLimit();
        } else {
            return ball.getLeftLimit() <= paddle.getBehindLimit();
        }
    }

    public boolean ballHitPaddleUpperEdge() {

        if (paddle.getSide() == Paddle.LEFT) {
            return ball.getCenterX() <= paddle.getOuterLimit() && ball.getLowerLimit() >= paddle.getUpperLimit() && ball.getLowerLimit() < (paddle.getUpperLimit() + paddle.getHeight() / 2) && ballNotBehindPaddle();
        } else {
            return ball.getCenterX() >= paddle.getOuterLimit() && ball.getLowerLimit() >= paddle.getUpperLimit() && ball.getLowerLimit() < (paddle.getUpperLimit() + paddle.getHeight() / 2) && ballNotBehindPaddle();
        }
    }

    public boolean ballHitPaddleLowerEdge() {

        if (paddle.getSide() == Paddle.LEFT) {
            return ball.getCenterX() <= paddle.getOuterLimit() && ball.getUpperLimit() <= paddle.getLowerLimit() && ball.getUpperLimit() > (paddle.getLowerLimit() - paddle.getHeight() / 2) && ballNotBehindPaddle();
        } else {
            return ball.getCenterX() >= paddle.getOuterLimit() && ball.getUpperLimit() <= paddle.getLowerLimit() && ball.getUpperLimit() > (paddle.getLowerLimit() - paddle.getHeight() / 2) && ballNotBehindPaddle();
        }
    }

    public boolean ballEdgeHitPaddleCorner() {

        // checks if the norm of the vector going from the center of the ball to the corner of the paddle is equal to or
        // less than the radius of the ball if the center of the ball is lower or higher than the edge of the paddle
        return ball.getCenterY() > paddle.getLowerLimit() &&
                Math.sqrt(
                        Math.pow(ball.getCenterX() - paddle.getOuterLimit(), 2)
                        + Math.pow(ball.getCenterY() - paddle.getLowerLimit(), 2)
                )
                        <= ball.getBallDiameter() / 2
                ||
                ball.getCenterY() < paddle.getUpperLimit() &&
                        Math.sqrt(
                                Math.pow(ball.getCenterX() - paddle.getOuterLimit(), 2)
                                + Math.pow(ball.getCenterY() - paddle.getUpperLimit(), 2)
                        )
                                <= ball.getBallDiameter() / 2;
    }

    public boolean ballCloseToPaddle() {

        return Math.abs(ball.getCenterX() - paddle.getOuterLimit()) <= ball.getBallDiameter() / 2 + ball.getXTrajectory() + paddle.getWidth();
        // makes sure the distance between the center of the ball and the front of the paddle is smaller than the radius
        // of the ball + the x trajectory of the ball + the width of the paddle to provide a point to start collision
        // detection calculations so as not to waste system resources
    }

    @Override
    public void checkCollisions() {

        if (ballCloseToPaddle() &&  // to prevent needless calculations
                (((ballHitPaddleOuterEdge() && ballNotBehindPaddle()) && ballDidNotSurpassPaddleUpperEdge() && ballDidNotSurpassPaddleLowerEdge()) ||
                ballEdgeHitPaddleCorner() ||
                        (ballHitPaddleUpperEdge() || ballHitPaddleLowerEdge()))) {

            checkPaddleCollisions();
            paddleHitSound.playSoundAfter(500);  // only play the sound every 500 ms (0.5 s) to prevent slowing down if ball inside paddle
            ball.changeXTrajectory(paddle.getSide());
            if (paddle.getSide() == Paddle.LEFT) {
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

    public void performCustomAction() {/* method to be overridden while instantiating to perform a custom action */}
}