// File: Missile.java
// Author: Leo
// Date Created: 4/14/15 4:48 PM

package pong.missile;

import pong.paddle.BattlePaddle;
import pong.paddle.Paddle;

import java.awt.geom.Ellipse2D;

public class PongMissile extends Ellipse2D.Double {

    private BattlePaddle parentPaddle;
    private double width, height;
    private double missileX;
    private double missileY;
    private double speed = 0;

    public PongMissile(BattlePaddle parentPaddle, double width, double height) {

        super(parentPaddle.getOuterLimit(), parentPaddle.getYCenter(), width, height);
        this.missileX = parentPaddle.getSide() == Paddle.LEFT ? parentPaddle.getOuterLimit() : parentPaddle.getOuterLimit() + width;
        this.missileY = parentPaddle.getYCenter();
        this.parentPaddle = parentPaddle;
        this.width = width;
        this.height = height;
    }

    public double getFront() {

        return parentPaddle.getSide() == Paddle.LEFT ? getX() + width : getX();
    }

    public double getBack() {

        return parentPaddle.getSide() == Paddle.LEFT ? getX() : getX() + width;
    }

    public double getTop() {

        return getY();
    }

    public double getBottom() {

        return getY() + height;
    }

    public void move(double speed) {

        this.speed = speed;
        if (parentPaddle.getSide() == Paddle.RIGHT) {
            speed *= -1;
        }
        missileX += speed;
        super.setFrame(missileX, missileY, width, height);
    }

    public double getSpeed() {

        return speed;
    }

    public void setMissileY(double missileY) {

        this.missileY = missileY;
        super.setFrame(missileX, missileY, width, height);
    }

    public void reset() {

        missileX = parentPaddle.getOuterLimit();
        super.setFrame(missileX, parentPaddle.getYCenter(), width, height);
    }

    public BattlePaddle getParentPaddle() {

        return parentPaddle;
    }
}