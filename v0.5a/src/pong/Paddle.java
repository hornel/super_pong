// File: Paddle.java
// Author: Leo Horne
// Date Created: Friday, March 6, 2015
// Version: 0.1.1a

/* New in this version:
    * Uses ?: statements instead of if-else
 */

package pong;

import java.awt.geom.Rectangle2D;

/**
 * Class which describes a paddle.
 */

class Paddle extends Rectangle2D.Double {

    private double paddleX, paddleY, paddleWidth, paddleHeight, side;
    public static final int LEFT = 1, RIGHT = 2;

    /**
     * Constructor specifying initial paddle x, initial paddle y, initial paddle
     * width, initial paddle height, and side of pong canvas.
     * @param paddleX       the initial paddle x.
     * @param paddleY       the initial paddle y.
     * @param paddleWidth   the initial paddle width.
     * @param paddleHeight  the initial paddle height.
     * @param side          the initial side of the pong canvas.
     */

    public Paddle(int paddleX, int paddleY, int paddleWidth, int paddleHeight, int side) {

        super(paddleX, paddleY, paddleWidth, paddleHeight);
        this.paddleX = paddleX;
        this.paddleY = paddleY;
        this.paddleWidth = paddleWidth;
        this.paddleHeight = paddleHeight;
        this.side = side;
    }

    public double getOuterLimit() {

        return side == LEFT ? paddleX + paddleWidth : paddleX;
    }

    public double getInnerLimit(double xTrajectory) {

        return side == LEFT ? getOuterLimit() - Math.abs(xTrajectory) : getOuterLimit() + Math.abs(xTrajectory);
    }

    public double getUpperLimit() {
        return paddleY;
    }

    public double getLowerLimit() {
        return paddleY + paddleHeight;
    }

    /**
     * Gets the center of the paddle's y coordinates.
     * @return the y center of the paddle.
     */

    public double getYCenter() {  // necessary for changing y trajectory of ball based on hit location

        return side == LEFT ? paddleY + paddleHeight / 2 : paddleY + paddleHeight / 2;
    }

    /**
     * Sets paddle location based on specified y coordinate.
     * @param y the y coordinate the paddle location should be set to.
     */

    public void setPaddleY(int y) {
        paddleY = y;
        super.setRect(paddleX, paddleY, paddleWidth, paddleHeight);
    }
}