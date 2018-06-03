// File: Ball.java
// Author: Leo Horne
// Date Created: Friday, March 6, 2015
// Version: 0.2a

/* Changes from last version:
    * Improved collision (now takes into consideration if upper part of ball hits lower part of paddle and vice versa)
    * Adds optional gravityEnabled
    * Updated to use getters and setters from PongCanvas
 */

package pong.ball;

import pong.mode.PongCanvas;
import pong.paddle.Paddle;
import pong.sound.PongSoundPlayer;

import java.awt.geom.Ellipse2D;

/**
 * Class which defines the ball and the way
 * it moves.
 */

public class Ball extends Ellipse2D.Double {

    private double gravity, terminalVelocity;
    private double gravityCoefficient = 150, terminalMultiplier = 2;
    private double maxYTrajectoryMultiplier = 1.6;  // describes how much the y trajectory should be multiplied in respect to the x trajectory
    double xTrajectory;
    double yTrajectory;
    double maxYTrajectory;  // describes maximum y trajectory
    private double ballX, ballY, ballDiameter;
    private boolean gravityEnabled = false;
    private double currentTrajectory;   // necessary for serving
    private PongCanvas pongCanvas;
    private PongSoundPlayer scoreSound = new PongSoundPlayer("point.wav");          // sound to be played when player receives a point

    /**
     * Class constructor specifying parent pongCanvas, initial x-coordinate,
     * initial y-coordinate, initial diameter of ball, initial x trajectory,
     * and initial y trajectory.
     *
     * @param pongCanvas   the parent pongCanvas.
     * @param ballX        the initial x-coordinate of the ball.
     * @param ballY        the initial y-coordinate of the ball.
     * @param ballDiameter the initial diameter of the ball.
     * @param xTrajectory  the initial x trajectory of the ball.
     * @param yTrajectory  the initial y trajectory of the ball.
     */
    public Ball(PongCanvas pongCanvas, int ballX, int ballY, int ballDiameter, double xTrajectory, double yTrajectory) {

        super(ballX, ballY, ballDiameter, ballDiameter);
        this.pongCanvas = pongCanvas;
        this.ballX = ballX;
        this.ballY = ballY;
        this.ballDiameter = ballDiameter;
        gravity = xTrajectory / gravityCoefficient;
        terminalVelocity = xTrajectory * terminalMultiplier;
        currentTrajectory = Math.abs(xTrajectory);
        setTrajectory(xTrajectory, yTrajectory);
        this.maxYTrajectory = xTrajectory * maxYTrajectoryMultiplier;
    }

    public double getRightLimit() {

        return ballX + ballDiameter;
    }

    public double getLeftLimit() {

        return ballX;
    }

    public double getUpperLimit() {

        return ballY;
    }

    public double getLowerLimit() {

        return ballY + ballDiameter;
    }

    /**
     * Sets the ball x and y trajectory based on values entered as arguments.
     *
     * @param xTrajectory the x trajectory that the ball should have.
     * @param yTrajectory the y trajectory that the ball should have.
     */

    public void setTrajectory(double xTrajectory, double yTrajectory) {

        this.xTrajectory = xTrajectory;
        this.currentTrajectory = xTrajectory;
        this.yTrajectory = yTrajectory;
        this.maxYTrajectory = xTrajectory * maxYTrajectoryMultiplier;
    }

    public void setSpeed(double xTrajectory, boolean shouldRemainAfterServe) {

        double oldXTrajectory = this.xTrajectory;
        double yBendFactorRatio = yTrajectory / Math.abs(this.xTrajectory);
        this.xTrajectory = xTrajectory;
        this.yTrajectory = this.xTrajectory * yBendFactorRatio;

        if (shouldRemainAfterServe) {
            this.currentTrajectory = Math.abs(this.xTrajectory);
            this.maxYTrajectory = this.xTrajectory * maxYTrajectoryMultiplier;
            this.gravity = Math.abs(xTrajectory) / gravityCoefficient;
            this.terminalVelocity = Math.abs(xTrajectory) * terminalMultiplier;
        }

        if (oldXTrajectory < 0) {
            this.xTrajectory *= -1;
        }
    }

    public double getSpeed() {

        return Math.abs(xTrajectory);
    }

    /**
     * Sets the speed of the ball based on a single value. Adapts the
     * y trajectory automatically.
     *
     * @param xTrajectory the speed that the ball should be set to.
     */

    public void setSpeed(double xTrajectory) {

        double oldXTrajectory = this.xTrajectory;
        double yBendFactorRatio = yTrajectory / Math.abs(this.xTrajectory);
        this.xTrajectory = Math.abs(xTrajectory);
        this.yTrajectory = this.xTrajectory * yBendFactorRatio;
        this.currentTrajectory = Math.abs(this.xTrajectory);
        this.maxYTrajectory = this.xTrajectory * maxYTrajectoryMultiplier;
        this.gravity = Math.abs(xTrajectory) / gravityCoefficient;
        this.terminalVelocity = Math.abs(xTrajectory) * terminalMultiplier;

        if (oldXTrajectory < 0) {
            this.xTrajectory *= -1;
        }
    }

    /**
     * Moves the ball by the current x and y trajectory.
     */

    public void move() {

        ballX += xTrajectory;
        if (gravityEnabled && Math.abs(yTrajectory) <= terminalVelocity) {
            yTrajectory += gravity;
        }
        ballY += yTrajectory;
        super.setFrame(ballX, ballY, ballDiameter, ballDiameter);
    }

    public void setGravity(boolean b) {

        gravityEnabled = b;
    }

    /**
     * Serves the ball after a player gets a point.
     */

    public void serve() {

        yTrajectory = 0;  // 1. to avoid sounds  2. to make sure the ball doesn't do anything stupid
        setSpeed(PongCanvas.INITIAL_X_TRAJECTORY, false);  // makes sure the delay between ball out of bounds and serve is always the same, regardless of ball speed

        if (ballX < -300) {  // -300 to create a delay between ball out of bounds and serve

            pongCanvas.increasePlayerScore(2);
            scoreSound.playSound();
            ballX = 50 + ballDiameter;  // make sure the ball is in front of the paddle when serving!
            ballY = 50 + ballDiameter;
            xTrajectory = currentTrajectory;  // reset x and y trajectory
            yTrajectory = currentTrajectory;
            pongCanvas.setPlayer1Turn(false);

        } else if (ballX > pongCanvas.getPanelWidth() + 300) {

            pongCanvas.increasePlayerScore(1);
            scoreSound.playSound();
            ballX = pongCanvas.getPanelWidth() - 50 - ballDiameter;
            ballY = 50 + ballDiameter;
            xTrajectory = -currentTrajectory;
            yTrajectory = currentTrajectory;
            pongCanvas.setPlayer1Turn(true);
        }
    }

    public void serveFromSide(int side) {

        yTrajectory = 0;  // 1. to avoid sounds  2. to make sure the ball doesn't do anything stupid
        setSpeed(PongCanvas.INITIAL_X_TRAJECTORY, false);  // makes sure the delay between ball out of bounds and serve is always the same, regardless of ball speed

        if (side == Paddle.LEFT) {  // -300 to create a delay between ball out of bounds and serve

            scoreSound.playSound();
            ballX = 50 + ballDiameter;  // make sure the ball is in front of the paddle when serving!
            ballY = 50 + ballDiameter;
            xTrajectory = currentTrajectory;  // reset x and y trajectory
            yTrajectory = currentTrajectory;
            pongCanvas.setPlayer1Turn(false);

        } else {

            scoreSound.playSound();
            ballX = pongCanvas.getPanelWidth() - 50 - ballDiameter;
            ballY = 50 + ballDiameter;
            xTrajectory = -currentTrajectory;
            yTrajectory = currentTrajectory;
            pongCanvas.setPlayer1Turn(true);
        }
    }

    public void serveFromMiddle() {

        yTrajectory = 0;  // 1. to avoid sounds  2. to make sure the ball doesn't do anything stupid
        setSpeed(PongCanvas.INITIAL_X_TRAJECTORY, false);  // makes sure the delay between ball out of bounds and serve is always the same, regardless of ball speed

        if (ballX < -300 || ballX > pongCanvas.getPanelWidth() + 300) {  // -300 to create a delay between ball out of bounds and serve

            pongCanvas.increasePlayerScore(2);
            scoreSound.playSound();
            ballX = pongCanvas.getPanelWidth() / 2;  // make sure the ball is in front of the paddle when serving!
            ballY = pongCanvas.getPanelHeight() / 2;
            xTrajectory = -currentTrajectory;  // reset x and y trajectory
            yTrajectory = currentTrajectory;
        }
    }

    public void deinit() {

        scoreSound.deinit();
    }

    public void reverseYTrajectory() {

        yTrajectory *= -1;
    }

    public double getBallY() {

        return ballY;
    }

    public void setBallY(double ballY) {

        this.ballY = ballY;
        super.setFrame(ballX, ballY, ballDiameter, ballDiameter);
    }

    public double getBallDiameter() {

        return ballDiameter;
    }

    public double getXTrajectory() {

        return xTrajectory;
    }

    public void changeXTrajectory(int side) {

        if (side == Paddle.LEFT) {
            xTrajectory = Math.abs(xTrajectory);
        } else {
            xTrajectory = -Math.abs(xTrajectory);
        }
    }

    public double getYTrajectory() {

        return yTrajectory;
    }

    public void setYTrajectory(double yTrajectory) {

        this.yTrajectory = yTrajectory;
    }

    public double getMaxYTrajectory() {

        return maxYTrajectory;
    }

    public void setMaxYTrajectoryMultiplier(double maxYTrajectoryMultiplier) {

        this.maxYTrajectoryMultiplier = maxYTrajectoryMultiplier;
    }

    public double getMaxYTrajectoryMultiplier() {

        return maxYTrajectoryMultiplier;
    }

    public double getBallX() {

        return ballX;
    }

    public void setBallX(double ballX) {

        this.ballX = ballX;
    }

    public void setBallDiameter(double diameter) {

        this.ballDiameter = diameter;
        super.setFrame(ballX, ballY, ballDiameter, ballDiameter);
    }

    public double getGravityCoefficient() {

        return gravityCoefficient;
    }

    public void setGravityCoefficient(double gravityCoefficient) {

        this.gravityCoefficient = gravityCoefficient;
    }

    public double getTerminalMultiplier() {

        return terminalMultiplier;
    }

    public void setTerminalMultiplier(double terminalMultiplier) {

        this.terminalMultiplier = terminalMultiplier;
    }
}