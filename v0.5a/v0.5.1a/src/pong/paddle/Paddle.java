// File: Paddle.java
// Author: Leo Horne
// Date Created: Friday, March 6, 2015
// Version: 0.1.1a

/* New in this version:
    * Uses ?: statements instead of if-else
 */

package pong.paddle;

import pong.missile.PongMissile;
import pong.mode.TwoPlayerBattlePongCanvas;
import pong.animation.MissileAnimation;
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
    private PongMissile missile;
    private boolean hasFiredMissile = false;
    private boolean destroyed = false;
    private MissileAnimation missileAnimation;

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

    public Paddle(PongCanvas pongCanvas, int paddleX, int paddleY, int paddleWidth, int paddleHeight, int side, PaddleMode paddleMode) {

        super(paddleX, paddleY, paddleWidth, paddleHeight);
        this.pongCanvas = pongCanvas;
        this.paddleX = paddleX;
        this.paddleY = paddleY;
        this.paddleWidth = paddleWidth;
        this.paddleHeight = paddleHeight;
        this.side = side;
        this.paddleMode = paddleMode;
        this.missile = new PongMissile(this, 20, 10, 10, pongCanvas.getPanelWidth());
    }

    public Paddle(TwoPlayerBattlePongCanvas pongCanvas, int paddleX, int paddleY, int paddleWidth, int paddleHeight, int side, PaddleMode paddleMode) {

        super(paddleX, paddleY, paddleWidth, paddleHeight);
        this.pongCanvas = pongCanvas;
        this.paddleX = paddleX;
        this.paddleY = paddleY;
        this.paddleWidth = paddleWidth;
        this.paddleHeight = paddleHeight;
        this.side = side;
        this.paddleMode = paddleMode;
        this.missile = new PongMissile(this, 20, 10, 10, pongCanvas.getPanelWidth());
        this.missileAnimation = new MissileAnimation(pongCanvas, this);
    }

    public Timer getPlayerModeTimer(int upKey, int downKey) {

        if (paddleMode == PaddleMode.HUMAN) {
            PaddleMover paddleMover = new PaddleMover(this, upKey, downKey, pongCanvas);
            pongCanvas.addKeyListener(paddleMover);
            return new Timer(TIMER_DELAY, paddleMover);

        } else if (paddleMode == PaddleMode.AI) {
            PaddleAI paddleAI = new PaddleAI(this, pongCanvas.getBall());
            return new Timer(TIMER_DELAY, paddleAI);

        } else {
            return null;
        }
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
        super.setRect(paddleX, paddleY, paddleWidth, paddleHeight);
    }

    public int getSide() {

        return (int) side;
    }

    public void prepareNewMissile()  {

        if (!hasFiredMissile()) {
            resetMissile();
            missile.setMissileY(getYCenter());
            hasFiredMissile = true;
        }
    }

    public PongMissile getMissile() {

        return missile;
    }

    public boolean hasFiredMissile() {

        return hasFiredMissile;
    }

    public void resetMissile() {

        missile.reset();
        hasFiredMissile = false;
    }

    public void moveMissile() {

        if (hasFiredMissile) {

            missile.move();
        }
    }

    public void destroy() {

        destroyed = true;
        paddleX = 0;
        paddleY = 0;
        paddleHeight = 0;
        paddleWidth = 0;
        super.setFrame(0, 0, 0, 0);
    }

    public void undestroy() {

        if (destroyed) {
            if (getSide() == LEFT) {

                paddleX = PongCanvas.INITIAL_PADDLE1_X;
                paddleY = PongCanvas.INITIAL_PADDLE1_Y;
                paddleWidth = PongCanvas.INITIAL_PADDLE_WIDTH;
                paddleHeight = PongCanvas.INITIAL_PADDLE_HEIGHT;
                super.setFrame(paddleX, paddleY, paddleWidth, paddleHeight);

            } else {

                paddleX = PongCanvas.INITIAL_PADDLE2_X;
                paddleY = PongCanvas.INITIAL_PADDLE2_Y;
                paddleWidth = PongCanvas.INITIAL_PADDLE_WIDTH;
                paddleHeight = PongCanvas.INITIAL_PADDLE_HEIGHT;
                super.setFrame(paddleX, paddleY, paddleWidth, paddleHeight);
            }
        }
        destroyed = false;
    }

    public void setMissileAnimation(MissileAnimation missileAnimation) {

        this.missileAnimation = missileAnimation;
    }

    public MissileAnimation getMissileAnimation() {

        return missileAnimation;
    }
}