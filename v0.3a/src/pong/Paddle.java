// File: Paddle.java
// Author: Leo Horne
// Date Created: Friday, March 6, 2015
// Version: 0.11

/* New in this version:
    * Uses ?: statements instead of if-else
 */

package pong;

import java.awt.geom.Rectangle2D;

class Paddle extends Rectangle2D.Double {

    private int paddleX, paddleY, paddleWidth, paddleHeight, side;
    public static final int LEFT = 1, RIGHT = 2;

    public Paddle(int paddleX, int paddleY, int paddleWidth, int paddleHeight, int side) {

        super(paddleX, paddleY, paddleWidth, paddleHeight);
        this.paddleX = paddleX;
        this.paddleY = paddleY;
        this.paddleWidth = paddleWidth;
        this.paddleHeight = paddleHeight;
        this.side = side;
    }

    public int getOuterLimit() {

        return side == LEFT ? paddleX + paddleWidth : paddleX;
    }

    public int getInnerLimit(int xTrajectory) {

        return side == LEFT ? getOuterLimit() - Math.abs(xTrajectory) : getOuterLimit() + Math.abs(xTrajectory);
    }

    public int getUpperLimit() {
        return paddleY;
    }

    public int getLowerLimit() {
        return paddleY + paddleHeight;
    }

    public int getYCenter() {

        return side == LEFT ? paddleY + paddleHeight / 2 : paddleY + paddleHeight / 2;
    }

    public void setPaddleY(int y) {
        paddleY = y;
        super.setRect(paddleX, paddleY, paddleWidth, paddleHeight);
    }
}