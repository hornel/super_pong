// File: Missile.java
// Author: Leo
// Date Created: 4/14/15 4:48 PM

package pong.missile;

import pong.paddle.Paddle;

import java.awt.geom.Ellipse2D;

public class PongMissile extends Ellipse2D.Double {

    private Paddle parentPaddle;
    private double width, height;
    private double missileX;
    private double missileY;
    private double trajectory;
    private double limit;

    public PongMissile(Paddle parentPaddle, double width, double height, double trajectory, double limit) {

        super(parentPaddle.getOuterLimit(), parentPaddle.getYCenter(), width, height);
        this.missileX = parentPaddle.getSide() == Paddle.LEFT ? parentPaddle.getOuterLimit() : parentPaddle.getOuterLimit() + width;
        this.missileY = parentPaddle.getYCenter();
        this.parentPaddle = parentPaddle;
        this.width = width;
        this.height = height;
        this.trajectory = trajectory;
        if (parentPaddle.getSide() == Paddle.RIGHT) {
            this.trajectory *= -1;
        }
        this.limit = limit;
    }

    public double getFront() {

        return parentPaddle.getSide() == Paddle.LEFT ? getX() + width : getX();
    }

    public double getTop() {

        return getY();
    }

    public double getBottom() {

        return getY() + height;
    }

    public void move() {

        missileX += trajectory;
        super.setFrame(missileX, missileY, width, height);
    }

    public double getBack() {

        return parentPaddle.getSide() == Paddle.LEFT ? getX() : getX() + width;
    }

    public boolean missed() {

        return parentPaddle.getSide() == Paddle.LEFT ? getFront() > limit : getFront() < 0;
    }

    public void setMissileY(double missileY) {

        this.missileY = missileY;
        super.setFrame(missileX, missileY, width, height);
    }

    public void reset() {

        missileX = parentPaddle.getOuterLimit();
        super.setFrame(missileX, parentPaddle.getYCenter(), width, height);
    }

    public Paddle getParentPaddle() {

        return parentPaddle;
    }
}