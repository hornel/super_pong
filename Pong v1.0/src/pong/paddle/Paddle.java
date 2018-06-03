// File: Paddle.java
// Author: Leo Horne
// Date Created: Friday, March 6, 2015

package pong.paddle;

import pong.mode.PongCanvas;

import javax.swing.*;
import java.awt.geom.Rectangle2D;

/**
 * Classe qui decrit une raquette. Herite de java.awt.geom.Rectangle2D.Double pour pouvoir etre dessine a l'ecran.
 */
public class Paddle extends Rectangle2D.Double {

    /**
     * Constante decrivant une raquette du cote gauche de l'ecran.
     */
    public static final int LEFT = 1;

    /**
     * Constante decrivant une raquette du droit gauche de l'ecran.
     */
    public static final int RIGHT = 2;

    /**
     * Le PongCanvas ou se trouve cette raquette.
     */
    private PongCanvas pongCanvas;

    /**
     * La coordonnee x de la raquette.
     */
    private double paddleX;

    /**
     * La coordonnee y de la raquette.
     */
    private double paddleY;

    /**
     * La largeur de la raquette.
     */
    private double paddleWidth;

    /**
     * La hauteur de la raquette.
     */
    private double paddleHeight;

    /**
     * Le cote de la raquette.
     */
    private int side;

    /**
     * Le mode de cette raquette (humain, IA ou rien)
     */
    private PaddleMode paddleMode;

    /**
     * Decrit si la raquette doit etre definitivement detruite.
     */
    private boolean toBeDestroyed = false;

    /**
     * Decrit si on peut deplacer la raquette.
     */
    private boolean movable = true;

    /**
     * Decrit la valeur de cette raquette lorsqu'elle est detruite.
     */
    private int value = 0;

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

    /**
     * Permet de chercher un Timer qui a pour but de deplacer la raquette lorsque certaines touches sont
     * appuyees (si le mode de la raquette est PaddleMode.HUMAN) ou qui permet a l'IA de deplacer la raquette (si
     * le mode de la raquette est PaddleMode.AI). Ajoute les Key Bindings necessaires pour accomplir cela au PongCanvas.
     */
    public Timer getPlayerModeTimer(int upKey, int downKey) {

        if (paddleMode == PaddleMode.HUMAN) {
            // permet a l'utilisateur de deplacer la raquette en utilisant les touches
            // upKey et downKey (qui sont definies par une constante dans java.awt.KeyEvent)
            PaddleMover paddleMover = new PaddleMover(this, upKey, downKey, pongCanvas);
            paddleMover.addKeyBindings();
            return new Timer(PaddleMover.TIMER_DELAY, paddleMover);

        } else if (paddleMode == PaddleMode.AI) {
            // permet a l'IA de deplacer sa raquette.
            PaddleAI paddleAI = new PaddleAI(pongCanvas, this, pongCanvas.getBall());
            return new Timer(PaddleMover.TIMER_DELAY, paddleAI);

        } else {
            // si la raquette n'est controlee ni par un humain ni par l'IA
            return null;
        }
    }

    /**
     * Cherche le mode de la raquette.
     */
    public PaddleMode getPaddleMode() {

        return paddleMode;
    }

    // Image recapitulative des limites de la raquette
    //
    // |----------------------------------------------|
    // |                  PongCanvas                  |
    // |----------------------------------------------|
    // |                       |                      |
    // |   superieure                   O             |
    // | a  __                 |                 __   |
    // | r |  |                                 |  |  |
    // | r |  |                |                |  |  |
    // | i |  |  exterieure                     |  |  |
    // | e |  |                |                |  |  |
    // | r |--|                                 |--|  |
    // | e                     |                      |
    // |   inferieure                                 |
    // |                       |                      |
    // |______________________________________________|


    /**
     * Cherche la limite exterieure de la raquette (la limite la plus proche de l'interieur du jeu), ce qui depend
     * du cote de la raquette.
     */
    public double getOuterLimit() {

        return side == LEFT ? paddleX + paddleWidth : paddleX;
    }

    /**
     * Cherche la limite arriere de la raquette (la limite la plus eloignee de l'interieur du jeu), ce qui depend
     * du cote de la raquette.
     */
    public double getBehindLimit() {

        return side == LEFT ? paddleX : paddleX + paddleWidth;
    }

    /**
     * Cherche la limite superieure de la raquette.
     */
    public double getUpperLimit() {

        return paddleY;
    }

    /**
     * Cherche la limite inferieure de la raquette.
     */
    public double getLowerLimit() {

        return paddleY + paddleHeight;
    }

    /**
     * Change la coordonne y de la raquette a la coordonne y donnee.
     */
    public void setPaddleY(double y) {

        paddleY = y;
        super.setFrame(paddleX, paddleY, paddleWidth, paddleHeight);
    }

    /**
     * Change la coordonne y de la raquette a la coordonne x donnee.
     */
    public void setPaddleX(double x) {

        paddleX = x;
        super.setFrame(paddleX, paddleY, paddleWidth, paddleHeight);
    }

    /**
     * Cherche le cote de la raquette.
     */
    public int getSide() {

        return side;
    }

    /**
     * Change la hauteur de la raquette a la hauteur donnee.
     */
    public void setPaddleHeight(double paddleHeight) {

        this.paddleHeight = paddleHeight;
        super.setFrame(paddleX, paddleY, paddleWidth, this.paddleHeight);
    }

    /**
     * Cherche la hauteur de la raquette.
     */
    public double getPaddleHeight() {

        return paddleHeight;
    }

    /**
     * Decrit si la raquette doit etre detruite definitivement.
     */
    public boolean isToBeDestroyed() {

        return toBeDestroyed;
    }

    /**
     * Change si la raquette doit etre detruite.
     */
    public void setToBeDestroyed(boolean toBeDestroyed) {

        this.toBeDestroyed = toBeDestroyed;
    }

    /**
     * Change la valeur de cette raquette lorsqu'elle est detruite.
     */
    public void setValue(int value) {

        this.value = value;
    }

    /**
     * Cherche la valeur de cette raquette lorsqu'elle est detruite.
     */
    public int getValue() {

        return value;
    }

    /**
     * Decrit si on peut deplacer la raquette.
     */
    public boolean isMovable() {

        return movable;
    }

    /**
     * Change si on peut deplacer la raquette.
     */
    public void setMovable(boolean movable) {

        this.movable = movable;
    }

    public PongCanvas getPongCanvas() {

        return pongCanvas;
    }
}