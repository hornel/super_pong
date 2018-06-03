// File: Ball.java
// Author: Leo Horne
// Date Created: Friday, March 6, 2015
// Version: 0.1

package pong;

import java.awt.geom.Ellipse2D;

class Ball extends Ellipse2D.Double {

    private int ballX, ballY, ballDiameter;
    int xTrajectory, yTrajectory;
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

        if (pongCanvas.isPlayer1Turn) {
            return ballX;
        } else {
            return ballX + ballDiameter;
        }
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
        ballY += yTrajectory;
        super.setFrame(ballX, ballY, ballDiameter, ballDiameter);
    }

    private boolean ballOutOfBounds() {
        return ballX > pongCanvas.panelWidth + ballDiameter || ballX < -ballDiameter;
    }

    private boolean ballHitTopOrBottom() {
        return ballY >= pongCanvas.panelHeight - ballDiameter || ballY <= 0;
    }

    private boolean ballHitPaddle1OuterEdge() {
        return getOuterLimit() <= paddle1.getOuterLimit();
    }

    private boolean ballNotBehindPaddle1() {
        return getOuterLimit() >= paddle1.getInnerLimit(xTrajectory);
    }

    private boolean ballDidNotSurpassPaddle1UpperEdge() {
        return getUpperLimit() >= paddle1.getUpperLimit();
    }

    private boolean ballDidNotSurpassPaddle1LowerEdge() {
        return getLowerLimit() <= paddle1.getLowerLimit();
    }

    private boolean ballHitPaddle2OuterEdge() {
        return getOuterLimit() >= paddle2.getOuterLimit();
    }

    private boolean ballNotBehindPaddle2() {
        return getOuterLimit() <= paddle2.getInnerLimit(xTrajectory);
    }

    private boolean ballDidNotSurpassPaddle2UpperEdge() {
        return getUpperLimit() >= paddle2.getUpperLimit();
    }

    private boolean ballDidNotSurpassPaddle2LowerEdge() {
        return getLowerLimit() <= paddle2.getLowerLimit();
    }

    void checkCollisions() {

        if (ballOutOfBounds()) {
            serve();

        } else if (ballHitTopOrBottom()) {
            yTrajectory = -yTrajectory;

        } else if ((ballHitPaddle1OuterEdge() && ballNotBehindPaddle1()) && ballDidNotSurpassPaddle1UpperEdge() && ballDidNotSurpassPaddle1LowerEdge()) {
            checkPaddleCollisions();
            xTrajectory = -xTrajectory;
            pongCanvas.isPlayer1Turn = false;

        } else if ((ballHitPaddle2OuterEdge() && ballNotBehindPaddle2()) && ballDidNotSurpassPaddle2UpperEdge() && ballDidNotSurpassPaddle2LowerEdge()) {
            checkPaddleCollisions();
            xTrajectory = -xTrajectory;
            pongCanvas.isPlayer1Turn = true;
        }
    }

    private void checkPaddleCollisions() {

        int paddleCenter;
        int hitDistance;
        int ballCenter;

        if (pongCanvas.isPlayer1Turn) {
            paddleCenter = paddle1.getYCenter();

        } else {
            paddleCenter = paddle2.getYCenter();
        }

        ballCenter = ballY + ballDiameter / 2;
        hitDistance = ballCenter - paddleCenter;

        yTrajectory = (int)(Math.abs(hitDistance) / yBendFactor) * (yTrajectory / Math.abs(yTrajectory));
        if (yTrajectory == 0) {
            yTrajectory += 1;
        }

        if (yTrajectory < 0 && hitDistance > 0) {
            yTrajectory = -yTrajectory;

        } else if (yTrajectory > 0 && hitDistance < 0) {
            yTrajectory = -yTrajectory;
        }
    }

    private void serve() {

        yTrajectory = 0;

        if (ballX < -300) {

            pongCanvas.player2Score++;
            ballX = 50;
            ballY = 50;
            xTrajectory = currentXTrajectory;
            yTrajectory = currentYTrajectory;
            pongCanvas.isPlayer1Turn = false;

        } else if (ballX > pongCanvas.panelWidth + 300) {

            pongCanvas.player1Score++;
            ballX = pongCanvas.panelWidth - 50;
            ballY = 50;
            xTrajectory = -currentXTrajectory;
            yTrajectory = currentYTrajectory;
            pongCanvas.isPlayer1Turn = true;
        }
    }
}