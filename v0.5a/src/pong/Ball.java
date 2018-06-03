// File: Ball.java
// Author: Leo Horne
// Date Created: Friday, March 6, 2015
// Version: 0.2a

/* Changes from last version:
    * Improved collision (now takes into consideration if upper part of ball hits lower part of paddle and vice versa)
    * Adds optional gravityEnabled
    * Updated to use getters and setters from PongCanvas
 */

package pong;

import java.awt.geom.Ellipse2D;

/**
 * Class which defines the ball and the way
 * it moves. Also takes care of collision
 * detection.
 */

class Ball extends Ellipse2D.Double {

    private double ballX, ballY, ballDiameter;
    double xTrajectory;
    double yTrajectory;
    private boolean gravityEnabled = false;
    private static final double GRAVITY = 0.3, TERMINAL_VELOCITY = 30;
    private double currentTrajectory;   // necessary for serving
    double yBendFactor;     // describes how much the y trajectory should increase/decrease when hit by the paddle
    double maxYBendFactor;  // describes maximum y trajectory
    private final double maxYBendFactorMultiplier = 1.6;  // describes how much the y trajectory should be multiplied in respect to the x trajectory
    private Paddle paddle1, paddle2;  // needs to be aware of paddles for collision detection
    private TwoHumanPlayerPongCanvas pongCanvas;
    private PongSoundPlayer paddleHitSound = new PongSoundPlayer("paddleHit.wav");  // sound to be played when paddle is hit
    private PongSoundPlayer wallHitSound = new PongSoundPlayer("wallHit.wav");      // sound to be played when top or bottom is hit
    private PongSoundPlayer scoreSound = new PongSoundPlayer("point.wav");          // sound to be played when player receives a point


    /**
     * Class constructor specifying parent pongCanvas, initial x-coordinate,
     * initial y-coordinate, initial diameter of ball, initial x trajectory,
     * and initial y trajectory.
     * @param pongCanvas    the parent pongCanvas.
     * @param ballX         the initial x-coordinate of the ball.
     * @param ballY         the initial y-coordinate of the ball.
     * @param ballDiameter  the initial diameter of the ball.
     * @param xTrajectory   the initial x trajectory of the ball.
     * @param yTrajectory   the initial y trajectory of the ball.
     */
    public Ball(TwoHumanPlayerPongCanvas pongCanvas, int ballX, int ballY, int ballDiameter, int xTrajectory, int yTrajectory) {

        super(ballX, ballY, ballDiameter, ballDiameter);
        this.pongCanvas = pongCanvas;
        this.ballX = ballX;
        this.ballY = ballY;
        this.ballDiameter = ballDiameter;
        currentTrajectory = Math.abs(xTrajectory);
        setTrajectory(xTrajectory, yTrajectory);
        this.maxYBendFactor = xTrajectory * maxYBendFactorMultiplier;
        getPaddles();
    }

    private void getPaddles() {

        paddle1 = pongCanvas.getPaddle1();
        paddle2 = pongCanvas.getPaddle2();
    }

    public double getOuterLimit() {

        return pongCanvas.isPlayer1Turn ? ballX : ballX + ballDiameter;
    }

    public double getUpperLimit() {
        return ballY;
    }

    public double getLowerLimit() {
        return ballY + ballDiameter;
    }

    /**
     * Sets the ball x and y trajectory based on values entered as arguments.
     * @param xTrajectory the x trajectory that the ball should have.
     * @param yTrajectory the y trajectory that the ball should have.
     */

    public void setTrajectory(int xTrajectory, int yTrajectory) {

        this.xTrajectory = xTrajectory;
        this.currentTrajectory = xTrajectory;
        this.yTrajectory = yTrajectory;
        this.maxYBendFactor = xTrajectory * maxYBendFactorMultiplier;
    }

    /**
     * Sets the speed of the ball based on a single value. Adapts the
     * y trajectory automatically.
     * @param xTrajectory the speed that the ball should be set to.
     */

    public void setSpeed(double xTrajectory) {

        double yBendFactorRatio = yTrajectory / this.xTrajectory;
        this.xTrajectory = xTrajectory;
        this.yTrajectory = this.xTrajectory * yBendFactorRatio;
        this.currentTrajectory = Math.abs(this.xTrajectory);
        this.maxYBendFactor = this.xTrajectory * maxYBendFactorMultiplier;

        if (pongCanvas.isPlayer1Turn) {
            this.xTrajectory *= -1;
        }
    }

    public double getSpeed() { return Math.abs(xTrajectory); }

    /**
     * Moves the ball by the current x and y trajectory.
     */

    public void move() {

        ballX += xTrajectory;
        if (gravityEnabled && yTrajectory <= TERMINAL_VELOCITY) {
            yTrajectory += GRAVITY;
        }
        ballY += yTrajectory;
        super.setFrame(ballX, ballY, ballDiameter, ballDiameter);
    }

    void setGravity(boolean b) {

        gravityEnabled = b;
    }

    private boolean ballOutOfBounds() {
        return ballX > pongCanvas.getPanelWidth() + ballDiameter || ballX < -ballDiameter;
    }

    private boolean ballHitTopOrBottom() { return ballY >= pongCanvas.getPanelHeight() - ballDiameter || ballY <= 0; }

    private boolean ballHitPaddle1OuterEdge() { return getOuterLimit() <= paddle1.getOuterLimit(); }

    private boolean ballNotBehindPaddle1() { return getOuterLimit() >= paddle1.getInnerLimit(xTrajectory); }  // necessary to make sure the ball doesn't get stuck behind the paddle

    private boolean ballDidNotSurpassPaddle1UpperEdge() { return getLowerLimit() >= paddle1.getUpperLimit(); }

    private boolean ballDidNotSurpassPaddle1LowerEdge() { return getUpperLimit() <= paddle1.getLowerLimit(); }

    private boolean ballHitPaddle2OuterEdge() { return getOuterLimit() >= paddle2.getOuterLimit(); }

    private boolean ballNotBehindPaddle2() { return getOuterLimit() <= paddle2.getInnerLimit(xTrajectory); }

    private boolean ballDidNotSurpassPaddle2UpperEdge() { return getLowerLimit() >= paddle2.getUpperLimit(); }

    private boolean ballDidNotSurpassPaddle2LowerEdge() { return getUpperLimit() <= paddle2.getLowerLimit(); }

    void checkCollisions() {

        if (ballOutOfBounds()) {
            serve();

        } else if (ballHitTopOrBottom()) {
            yTrajectory *= -1;
            wallHitSound.playSound();

        } else if ((ballHitPaddle1OuterEdge() && ballNotBehindPaddle1()) && ballDidNotSurpassPaddle1UpperEdge() && ballDidNotSurpassPaddle1LowerEdge()) {
            checkPaddleCollisions();
            paddleHitSound.playSound();
            xTrajectory *= -1;
            pongCanvas.isPlayer1Turn = false;

        } else if ((ballHitPaddle2OuterEdge() && ballNotBehindPaddle2()) && ballDidNotSurpassPaddle2UpperEdge() && ballDidNotSurpassPaddle2LowerEdge()) {
            checkPaddleCollisions();
            paddleHitSound.playSound();
            xTrajectory *= -1;
            pongCanvas.isPlayer1Turn = true;
        }
    }

    /**
     * Changes the y trajectory based on the location where the ball
     * hits the paddle.
     * <p></p>
     * The method uses the following algorithm:
     * 1. Calculates the distance between the location where the ball hit the
     *    paddle and the center of the paddle.
     * 2. Translates this distance into a percentage. A ball hit on the edge of
     *    the paddle will have a percentage close to 100% and a ball that hits
     *    near the center of the paddle will have a percentage close to 0%.
     * 3. Multiplies this percentage by the maximum y bend factor.
     * 4. Assigns this value to the y trajectory.
     */
    private void checkPaddleCollisions() {

        double paddleCenter;
        double hitDistance;
        double hitPercentile;
        double ballCenter;

        paddleCenter = pongCanvas.isPlayer1Turn ? paddle1.getYCenter() : paddle2.getYCenter();
        ballCenter = ballY + ballDiameter / 2;
        hitDistance = ballCenter - paddleCenter;

        hitPercentile = hitDistance / ((paddle1.getHeight() + ballDiameter) / 2);

        yBendFactor = hitPercentile * maxYBendFactor;

        yTrajectory = yBendFactor;

        System.out.println("\n------------------- BALL HIT PADDLE -------------------");
        System.out.println("Distance from center of paddle: " + hitDistance + " px");
        System.out.println("Hit percentile: " + hitPercentile*100 + "%");
        System.out.println("Maximum Y Bend Factor: " + maxYBendFactor);
        System.out.println("Y Bend Factor: " + yBendFactor);
        System.out.println("X Trajectory: " + xTrajectory);
        System.out.println("Y Trajectory: " + yTrajectory);
        System.out.println("-------------------------------------------------------");
    }

    /**
     * Serves the ball after a player gets a point.
     */

    void serve() {

        yTrajectory = 0;  // 1. to avoid sounds  2. to make sure the ball doesn't do anything stupid
        xTrajectory = pongCanvas.isPlayer1Turn ? -9 : 9;  // makes sure the delay between ball out of bounds and serve is always the same, regardless of ball speed

        if (ballX < -300) {  // -300 to create a delay between ball out of bounds and serve

            pongCanvas.increasePlayerScore(2);
            scoreSound.playSound();
            ballX = 50;  // make sure the ball is in front of the paddle when serving!
            ballY = 50;
            xTrajectory = currentTrajectory;  // reset x and y trajectory
            yTrajectory = currentTrajectory;
            pongCanvas.isPlayer1Turn = false;

        } else if (ballX > pongCanvas.getPanelWidth() + 300) {

            pongCanvas.increasePlayerScore(1);
            scoreSound.playSound();
            ballX = pongCanvas.getPanelWidth() - 50;
            ballY = 50;
            xTrajectory = -currentTrajectory;
            yTrajectory = currentTrajectory;
            pongCanvas.isPlayer1Turn = true;
        }
    }

    void deinit() {

        paddleHitSound.deinit();
        wallHitSound.deinit();
        scoreSound.deinit();
    }
}