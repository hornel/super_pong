// File: Paddle.java
// Author: Leo Horne
// Date Created: Friday, March 6, 2015

package pong.paddle;

import pong.mode.PongCanvas;

import javax.swing.*;
import java.awt.geom.Rectangle2D;

/**
 * Class which describes a paddle.
 */

public class Paddle extends Rectangle2D.Double {

    public static final int LEFT = 1, RIGHT = 2;
    private final static int TIMER_DELAY = 30;
    private PongCanvas pongCanvas;
    private double paddleX, paddleY, paddleWidth, paddleHeight, side;
    private PaddleMode paddleMode;
    private boolean toBeDestroyed = false;
    private boolean moveable = true;
    private int value = 0;

    /**
     * Constructor specifying initial paddle x, initial paddle y, initial paddle
     * width, initial paddle height, and side of pong canvas.
     *
     * @param paddleX      the initial paddle x.
     * @param paddleY      the initial paddle y.
     * @param paddleWidth  the initial paddle width.
     * @param paddleHeight the initial paddle height.
     * @param side         the initial side of the pong canvas.
     */

    public Paddle(PongCanvas pongCanvas, double paddleX, double paddleY, double paddleWidth, double paddleHeight, int side, PaddleMode paddleMode) {

        super(paddleX, paddleY, paddleWidth, paddleHeight);
        this.pongCanvas = pongCanvas;
        this.paddleX = paddleX;
        this.paddleY = paddleY;
        this.paddleWidth = paddleWidth;
        this.paddleHeight = paddleHeight;
        this.side = side;
        this.paddleMode = paddleMode;
    }

    public Timer getPlayerModeTimer(int upKey, int downKey) {

        if (paddleMode == PaddleMode.HUMAN) {
            PaddleMover paddleMover = new PaddleMover(this, upKey, downKey, pongCanvas);
            pongCanvas.addKeyListener(paddleMover);
            return new Timer(TIMER_DELAY, paddleMover);

        } else if (paddleMode == PaddleMode.AI) {
            PaddleAI paddleAI = new PaddleAI(pongCanvas, this, pongCanvas.getBall());
            return new Timer(TIMER_DELAY, paddleAI);

        } else {
            return null;
        }
    }

    public PaddleMode getPaddleMode() {

        return paddleMode;
    }

    public double getOuterLimit() {

        return side == LEFT ? paddleX + paddleWidth : paddleX;
    }

    public double getBehindLimit() {

        return side == LEFT ? paddleX : paddleX + paddleWidth;
    }

    public double getUpperLimit() {

        return paddleY;
    }

    public double getLowerLimit() {

        return paddleY + paddleHeight;
    }

    /**
     * Gets the center of the paddle's y coordinates.
     *
     * @return the y center of the paddle.
     */

    public double getYCenter() {  // necessary for changing y trajectory of ball based on hit location

        return side == LEFT ? paddleY + paddleHeight / 2 : paddleY + paddleHeight / 2;
    }

    /**
     * Sets paddle location based on specified y coordinate.
     *
     * @param y the y coordinate the paddle location should be set to.
     */

    public void setPaddleY(double y) {

        paddleY = y;
        super.setFrame(paddleX, paddleY, paddleWidth, paddleHeight);
    }

    public void setPaddleX(double x) {

        paddleX = x;
        super.setFrame(paddleX, paddleY, paddleWidth, paddleHeight);
    }

    public int getSide() {

        return (int) side;
    }

    public void setPaddleHeight(double paddleHeight) {

        this.paddleHeight = paddleHeight;
        super.setFrame(paddleX, paddleY, paddleWidth, this.paddleHeight);
    }

    public double getPaddleHeight() {

        return paddleHeight;
    }

    public boolean isToBeDestroyed() {

        return toBeDestroyed;
    }

    public void setToBeDestroyed(boolean toBeDestroyed) {

        this.toBeDestroyed = toBeDestroyed;
    }

    public void setValue(int value) {

        this.value = value;
    }

    public int getValue() {

        return value;
    }

    public double getPaddleX() {

        return paddleX;
    }

    public void deinit() {

    }

    public boolean isMovable() {

        return moveable;
    }

    public void setMoveable(boolean moveable) {

        this.moveable = moveable;
    }
}