// File: PongCanvas.java
// Author: Leo Horne
// Date Created: Saturday, March 7, 2015
// Version: 0.3a

/* Improvements in this version:
    * New getters and setters for access to private variables
    * New functions (reset()) to accommodate PongControls
*/

package pong.mode;

import pong.collision.PongPaddleCollisionDetector;
import pong.paddle.Paddle;

import java.awt.*;

/**
 * Class which creates and manages the Pong canvas
 * seen on the screen. Sets up interactions between
 * user and screen elements (paddles).
 */

public abstract class TwoPlayerPongCanvas extends PongCanvas {

    Paddle paddle1;
    Paddle paddle2;
    boolean isPlayer1Turn = false;
    private Integer player1Score = 0, player2Score = 0;
    private int maxScore = 10;
    private int scoreFontSize = 50;
    private String scoreFont = "Andale Mono";
    private String winnerString = "The %s side wins!";  // String to be displayed when someone wins the game

    /**
     * Cleans up before quitting the game.
     */

    public void deinit() {

        ball.deinit();
    }

    @Override
    public int getNumberOfPaddles() {

        return getPaddles().length;
    }

    @Override
    public Paddle[] getPaddles() {

        return new Paddle[]{paddle1, paddle2};
    }

    /**
     * Draws all items to the canvas.
     *
     * @param g the graphics object to draw with.
     */

    @Override
    void drawCanvas(Graphics g) {

        super.drawCanvas(g);

        getGraphics2D().setColor(foregroundColor);
        getGraphics2D().setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        getGraphics2D().fill(paddle1);
        getGraphics2D().fill(paddle2);

        getGraphics2D().setFont(new Font(scoreFont, Font.PLAIN, scoreFontSize));
        getGraphics2D().drawString(player1Score.toString(), getPanelWidth() / 4 - scoreFontSize / 2, scoreFontSize);
        getGraphics2D().drawString(player2Score.toString(), getPanelWidth() * 3 / 4 - scoreFontSize / 2, scoreFontSize);

        if (player1Score == maxScore) {
            winnerString = String.format(winnerString, "left");
            int stringWidth = (int) getGraphics2D().getFontMetrics().getStringBounds(winnerString, getGraphics2D()).getWidth();     // get winnerString width with specified font
            int stringHeight = (int) getGraphics2D().getFontMetrics().getStringBounds(winnerString, getGraphics2D()).getHeight();   // get winnerString height with the specified font
            int xstart = getPanelWidth() / 2 - stringWidth / 2;                                                  // center the string horizontally
            int ystart = getPanelHeight() / 2 - stringHeight / 2;                                                // center the string vertically
            getGraphics2D().drawString(winnerString, xstart, ystart);

        } else if (player2Score == maxScore) {
            winnerString = String.format(winnerString, "right");
            int stringWidth = (int) getGraphics2D().getFontMetrics().getStringBounds(winnerString, getGraphics2D()).getWidth();
            int stringHeight = (int) getGraphics2D().getFontMetrics().getStringBounds(winnerString, getGraphics2D()).getHeight();
            int xstart = getPanelWidth() / 2 - stringWidth / 2;
            int ystart = getPanelHeight() / 2 - stringHeight / 2;
            getGraphics2D().drawString(winnerString, xstart, ystart);
        }
    }

    /**
     * Resets the game so that the player can start a new one.
     */

    @Override
    public void reset() {

        super.reset();
        isPlayer1Turn = false;
        player1Score = 0;
        player2Score = 0;
    }

    @Override
    public PongMode getMode() {

        return PongMode.DUAL_PLAYER_HUMAN_VS_HUMAN;
    }

    @Override
    public boolean isPlayer1Turn() {

        return isPlayer1Turn;
    }

    @Override
    public void setPlayer1Turn(boolean player) {

        isPlayer1Turn = player;
    }

    @Override
    void addCollisionDetectors() {

        for (Paddle paddle : getPaddles()) {
            pongPaddleCollisionDetectors.add(new PongPaddleCollisionDetector(this, paddle));
        }
    }

    @Override
    public void increasePlayerScore(int playerNumber) {

        if (playerNumber == 1) {
            player1Score++;
        } else {
            player2Score++;
        }
    }

    @Override
    public int getPlayerScore(int player) {

        return player == 1 ? player1Score : player2Score;
    }

    @Override
    public int getMaxScore() {

        return maxScore;
    }
}