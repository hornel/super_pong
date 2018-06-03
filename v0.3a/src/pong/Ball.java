// File: Ball.java
// Author: Leo Horne
// Date Created: Friday, March 6, 2015
// Version: 0.2

/* Changes from last version:
    * Improved collision (now takes into consideration if upper part of ball hits lower part of paddle and vice versa)
    * Adds optional gravity
    * Updated to use getters and setters from PongCanvas
 */

package pong;

import java.awt.geom.Ellipse2D;

class Ball extends Ellipse2D.Double {

    private int ballX, ballY, ballDiameter;
    int xTrajectory;
    double yTrajectory;
    private boolean gravity = false;
    private static final double GRAVITY = 0.3, TERMINAL_VELOCITY = 30;
    private int currentXTrajectory, currentYTrajectory;
    double yBendFactor;
    private Paddle paddle1, paddle2;
    private PongCanvas pongCanvas;

    public Ball(PongCanvas pongCanvas, int ballX, int ballY, int ballDiameter, int xTrajectory, int yTrajectory, double yBendFactor) {

        super(ballX, ballY, ballDiameter, ballDiameter);
        this.pongCanvas = pongCanvas;
        this.ballX = ballX;
        this.ballY = ballY;
        this.ballDiameter = ballDiameter;
        currentXTrajectory = Math.abs(xTrajectory);
        currentYTrajectory = Math.abs(yTrajectory);
        setTrajectory(xTrajectory, yTrajectory);
        this.yBendFactor = yBendFactor;
        getPaddles();
    }

    private void getPaddles() {

        paddle1 = pongCanvas.paddle1;
        paddle2 = pongCanvas.paddle2;
    }

    public int getOuterLimit() {

        return pongCanvas.isPlayer1Turn ? ballX : ballX + ballDiameter;
    }

    public int getUpperLimit() {
        return ballY;
    }

    public int getLowerLimit() {
        return ballY + ballDiameter;
    }

    public void setTrajectory(int xTrajectory, int yTrajectory) {

        this.xTrajectory = xTrajectory;
        this.yTrajectory = yTrajectory;
    }

    public void move() {

        ballX += xTrajectory;
        if (gravity && yTrajectory <= TERMINAL_VELOCITY) {
            yTrajectory += GRAVITY;
        }
        ballY += yTrajectory;
        super.setFrame(ballX, ballY, ballDiameter, ballDiameter);
    }

    void setGravity(boolean b) {

        gravity = b;
    }

    private boolean ballOutOfBounds() {
        return ballX > pongCanvas.getPanelWidth() + ballDiameter || ballX < -ballDiameter;
    }

    private boolean ballHitTopOrBottom() {
        return ballY >= pongCanvas.getPanelHeight() - ballDiameter || ballY <= 0;
    }

    private boolean ballHitPaddle1OuterEdge() {
        return getOuterLimit() <= paddle1.getOuterLimit();
    }

    private boolean ballNotBehindPaddle1() {
        return getOuterLimit() >= paddle1.getInnerLimit(xTrajectory);
    }

    private boolean ballDidNotSurpassPaddle1UpperEdge() {
        return getLowerLimit() >= paddle1.getUpperLimit();
    }

    private boolean ballDidNotSurpassPaddle1LowerEdge() {
        return getUpperLimit() <= paddle1.getLowerLimit();
    }

    private boolean ballHitPaddle2OuterEdge() {
        return getOuterLimit() >= paddle2.getOuterLimit();
    }

    private boolean ballNotBehindPaddle2() {
        return getOuterLimit() <= paddle2.getInnerLimit(xTrajectory);
    }

    private boolean ballDidNotSurpassPaddle2UpperEdge() {
        return getLowerLimit() >= paddle2.getUpperLimit();
    }

    private boolean ballDidNotSurpassPaddle2LowerEdge() {
        return getUpperLimit() <= paddle2.getLowerLimit();
    }

    void checkCollisions() {

        if (ballOutOfBounds()) {
            serve();

        } else if (ballHitTopOrBottom()) {
            yTrajectory *= -1;

        } else if ((ballHitPaddle1OuterEdge() && ballNotBehindPaddle1()) && ballDidNotSurpassPaddle1UpperEdge() && ballDidNotSurpassPaddle1LowerEdge()) {
            checkPaddleCollisions();
            xTrajectory *= -1;
            pongCanvas.isPlayer1Turn = false;

        } else if ((ballHitPaddle2OuterEdge() && ballNotBehindPaddle2()) && ballDidNotSurpassPaddle2UpperEdge() && ballDidNotSurpassPaddle2LowerEdge()) {
            checkPaddleCollisions();
            xTrajectory *= -1;
            pongCanvas.isPlayer1Turn = true;
        }
    }

    private void checkPaddleCollisions() {

        int paddleCenter;
        int hitDistance;
        int ballCenter;

        paddleCenter = pongCanvas.isPlayer1Turn ? paddle1.getYCenter() : paddle2.getYCenter();

        ballCenter = ballY + ballDiameter / 2;
        hitDistance = ballCenter - paddleCenter;

        yTrajectory = (int)(Math.abs(hitDistance) / yBendFactor) * -1;
        if (yTrajectory == 0) {
            yTrajectory += 1;
        }

        if (yTrajectory < 0 && hitDistance > 0) {
            yTrajectory *= -1;

        } else if (yTrajectory > 0 && hitDistance < 0) {
            yTrajectory *= -1;
        }
    }

    void serve() {

        yTrajectory = 0;

        if (ballX < -300) {

            pongCanvas.increasePlayerScore(2);
            ballX = 50;
            ballY = 50;
            xTrajectory = currentXTrajectory;
            yTrajectory = currentYTrajectory;
            pongCanvas.isPlayer1Turn = false;

        } else if (ballX > pongCanvas.getPanelWidth() + 300) {

            pongCanvas.increasePlayerScore(1);
            ballX = pongCanvas.getPanelWidth() - 50;
            ballY = 50;
            xTrajectory = -currentXTrajectory;
            yTrajectory = currentYTrajectory;
            pongCanvas.isPlayer1Turn = true;
        }
    }
}