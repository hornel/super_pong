// File: Missile.java
// Author: Leo
// Date Created: 4/14/15 4:48 PM

package pong.missile;

import pong.paddle.BattlePaddle;
import pong.paddle.Paddle;

import java.awt.geom.Ellipse2D;

/**
 * Classe qui decrit le missile dans un TwoPlayerBattlePongCanvas.
 */
public class PongMissile extends Ellipse2D.Double {

    /**
     * La raquette qui lance le missile.
     */
    private BattlePaddle parentPaddle;

    /**
     * La hauteur du missile.
     */
    private double width;

    /**
     * La largeur du missile.
     */
    private double height;

    /**
     * La coordonnee x du missile.
     */
    private double missileX;

    /**
     * La coordonnee y du missile.
     */
    private double missileY;

    /**
     * La vitesse du missile.
     */
    private double speed = 0;

    public PongMissile(BattlePaddle parentPaddle, double width, double height) {

        super(parentPaddle.getOuterLimit(), parentPaddle.getCenterY(), width, height);
        this.missileX = parentPaddle.getSide() == Paddle.LEFT ? parentPaddle.getOuterLimit() : parentPaddle.getOuterLimit() - width;
        this.missileY = parentPaddle.getCenterY();
        this.parentPaddle = parentPaddle;
        this.width = width;
        this.height = height;
    }

    /**
     * Cherche l'avant du missile (depend du cote de la raquette qui a lance ce missile).
     */
    public double getFront() {

        return parentPaddle.getSide() == Paddle.LEFT ? getX() + width : getX();
    }

    /**
     * Cherche l'arriere du missile (depend du cote de la raquette qui a lance ce missile).
     */
    public double getBack() {

        return parentPaddle.getSide() == Paddle.LEFT ? getX() : getX() + width;
    }

    /**
     * Cherche la limite superieure du missile.
     */
    public double getTop() {

        return getY();
    }

    /**
     * Cherche la limite inferieure du missile.
     */
    public double getBottom() {

        return getY() + getHeight();
    }

    /**
     * Deplace le missile a une vitesse donnee
     * @param speed la vitesse.
     */
    public void move(double speed) {

        this.speed = speed;
        if (parentPaddle.getSide() == Paddle.RIGHT) {
            speed *= -1;  // Si le missile est lance depuis la droite, il faut la deplacer vers la gauche
        }
        missileX += speed;
        super.setFrame(missileX, missileY, width, height);
    }

    /**
     * Cherche la vitesse du missile.
     */
    public double getSpeed() {

        return speed;
    }

    /**
     * Change la coordonnee y du missile.
     */
    public void setMissileY(double missileY) {

        this.missileY = missileY;
        super.setFrame(missileX, missileY, width, height);
    }

    /**
     * Remet le missile a son endroit initial.
     */
    public void reset() {

        this.missileX = parentPaddle.getSide() == Paddle.LEFT ? parentPaddle.getOuterLimit() : parentPaddle.getOuterLimit() - width;
        super.setFrame(missileX, parentPaddle.getCenterY(), width, height);
    }

    /**
     * Cherche la raquette (BattlePaddle) qui a lance le missile.
     */
    public BattlePaddle getParentPaddle() {

        return parentPaddle;
    }
}