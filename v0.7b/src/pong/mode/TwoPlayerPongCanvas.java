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
import pong.control.PongPreferencesEditor;
import pong.lang.Language;
import pong.paddle.Paddle;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Map;

public abstract class TwoPlayerPongCanvas extends PongCanvas {

    Paddle paddle1;
    Paddle paddle2;
    boolean isPlayer1Turn = false;
    private Integer player1Score = 0, player2Score = 0;
    private int maxScore = 10;
    private int scoreFontSize = 50;
    private String scoreFont = "Andale Mono";
    private Map<String, String> lang = Language.getLanguageMap(Language.getLoc());
    private String winnerString = lang.get("winnerString");  // String to be displayed when someone wins the game
    private boolean scoreEnabled = true;

    public TwoPlayerPongCanvas() {
        super();
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {

                paddle2.setPaddleX(e.getComponent().getWidth() - paddle2.getWidth() - 10);
                for (Paddle paddle : getPaddles()) {
                    if (paddle.getHeight() > getHeight() - 2 * PongPreferencesEditor.PADDING) {
                        paddle.setPaddleHeight(getHeight() - 2 * PongPreferencesEditor.PADDING);
                    }
                    if (paddle.getLowerLimit() >= e.getComponent().getHeight() - PongPreferencesEditor.PADDING) {
                        paddle.setPaddleY(e.getComponent().getHeight() - PongPreferencesEditor.PADDING - paddle.getHeight());
                    }
                }
                repaint();
            }
        });
    }

    /**
     * Cleans up before quitting the game.
     */

    @Override
    public void end() {

        super.end();
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
        Graphics2D g2d = (Graphics2D) g;

        g2d.setPaint(foregroundColor);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2d.fill(paddle1);
        g2d.fill(paddle2);

        g2d.setFont(new Font(scoreFont, Font.PLAIN, scoreFontSize));
        if (scoreEnabled) {
            g2d.drawString(player1Score.toString(), getWidth() / 4 - scoreFontSize / 2, scoreFontSize);
            g2d.drawString(player2Score.toString(), getWidth() * 3 / 4 - scoreFontSize / 2, scoreFontSize);
        }

        if (scoreEnabled && player1Score == maxScore) {
            g2d.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 30));
            int stringWidth = (int) g2d.getFontMetrics().getStringBounds(winnerString, g2d).getWidth();     // get winnerString width with specified font
            int stringHeight = (int) g2d.getFontMetrics().getStringBounds(winnerString, g2d).getHeight();   // get winnerString height with the specified font
            int xstart = 3 * getWidth() / 4 - stringWidth / 2;                                                  // center the string horizontally
            int ystart = getHeight() / 2 - stringHeight / 2;                                                // center the string vertically
            g2d.drawString(winnerString, xstart, ystart);

        } else if (scoreEnabled && player2Score == maxScore) {
            g2d.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 30));
            int stringWidth = (int) g2d.getFontMetrics().getStringBounds(winnerString, g2d).getWidth();
            int stringHeight = (int) g2d.getFontMetrics().getStringBounds(winnerString, g2d).getHeight();
            int xstart = getWidth() / 4 - stringWidth / 2;
            int ystart = getHeight() / 2 - stringHeight / 2;
            g2d.drawString(winnerString, xstart, ystart);
        }

        drawCustomItems(g2d);
        g2d.dispose();
    }

    void drawCustomItems(Graphics2D g2d) {}

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
            addPaddleCollisionDetector(new PongPaddleCollisionDetector(this, paddle));
        }
    }

    @Override
    public void increasePlayerScore(int playerNumber) {

        if (playerNumber == 1) {
            player1Score++;
            repaint();
        } else {
            player2Score++;
            repaint();
        }
    }

    @Override
    public boolean isGameOver() {

        if (scoreEnabled && (player1Score >= maxScore || player2Score >= maxScore)) {
            fireStateChanged();
            return true;
        } else {
            return false;
        }
    }

    public void setPaddle1(Paddle paddle1) {

        this.paddle1 = paddle1;
    }

    public void setPaddle2(Paddle paddle2) {

        this.paddle2 = paddle2;
    }

    public void setScoreEnabled(boolean scoreEnabled) {

        this.scoreEnabled = scoreEnabled;
        if (player1Score >= maxScore || player2Score >= maxScore) {
            maxScore = player1Score >= maxScore ? player1Score + 1 : player2Score + 1;
            if (maxScore > PongPreferencesEditor.MAXIMUM_SCORE) {
                maxScore = PongPreferencesEditor.MAXIMUM_SCORE;
            }
        }
    }

    public boolean isScoreEnabled() {

        return scoreEnabled;
    }

    public void setMaxScore(int maxScore) {

        this.maxScore = maxScore;
    }

    public int getMaxScore() {

        return maxScore;
    }

    public int getPlayerScore(int playerNumber) {

        return playerNumber == 1 ? player1Score : player2Score;
    }
}