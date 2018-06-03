// File: Paddle.java
// Author: Leo Horne
// Date Created: Friday, March 6, 2015
// Version: 0.1

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

        if (side == LEFT) {
            return paddleX + paddleWidth;
        } else {
            return paddleX;
        }
    }

    public int getInnerLimit(int xTrajectory) {

        if (side == LEFT) {
            return getOuterLimit() - Math.abs(xTrajectory);
        } else {
            return getOuterLimit() + Math.abs(xTrajectory);
        }
    }

    public int getUpperLimit() {
        return paddleY;
    }

    public int getLowerLimit() {
        return paddleY + paddleHeight;
    }

    public int getYCenter() {

        if (side == LEFT) {
            return paddleY + paddleHeight / 2;
        } else {
            return paddleY + paddleHeight / 2;
        }
    }

    public void setPaddleY(int y) {
        paddleY = y;
        super.setRect(paddleX, paddleY, paddleWidth, paddleHeight);
    }
}