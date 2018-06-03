// File: SinglePlayerArkanoidPongCanvas.java
// Author: Leo
// Date Created: 4/12/15 3:21 PM

package pong.mode;

import pong.collision.PongPaddleCollisionDetector;
import pong.collision.PongWallCollisionDetector;
import pong.control.PongPreferencesEditor;
import pong.lang.Language;
import pong.paddle.Paddle;
import pong.paddle.PaddleMode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class SinglePlayerBreakoutPongCanvas extends PongCanvas {

    private int rows, cols;
    private int score = 0;
    private int livesLeft = 3;
    private Map<String, String> lang = Language.getLanguageMap(Language.getLoc());
    private String scoreString = lang.get("scoreString");
    private String livesString = lang.get("livesString");
    private Paddle playerPaddle;
    private ArrayList<ArrayList<Paddle>> bricks;
    private double interSpace;
    private double brickHeight;
    private double initialX;
    private Color brickColor;
    private Timer paddleTimer;
    private Rectangle infoBounds;
    private String scoreFont = Font.SANS_SERIF;
    private int scoreFontSize = 15;
    private boolean gameOver = false;

    public SinglePlayerBreakoutPongCanvas() {

        super();
    }

    public SinglePlayerBreakoutPongCanvas(int rows, int cols) {

        super();
        this.rows = rows;
        this.cols = cols;

        interSpace = 3;
        brickHeight = (getHeight() - ((rows + 1) * interSpace)) / rows;
        initialX = getWidth() - cols * interSpace - cols * INITIAL_PADDLE_WIDTH;
        setBallOffset(getWidth() - initialX);

        double currentY = interSpace;
        int initialValue = 1;

        for (int row = 0; row < rows; row++) {
            double currentX = initialX;
            int value = initialValue;
            ArrayList<Paddle> currentRow = new ArrayList<Paddle>();
            bricks.add(row, currentRow);

            for (int col = 0; col < cols; col++) {
                Paddle brick = new Paddle(this, currentX, currentY, INITIAL_PADDLE_WIDTH, brickHeight, Paddle.RIGHT, PaddleMode.NONE);
                brick.setValue(value);
                currentRow.add(col, brick);
                currentX += INITIAL_PADDLE_WIDTH + interSpace;
                value += 2;
            }
            currentY += brickHeight + interSpace;
        }

        removeAllPaddleCollisionDetectors();
        addCollisionDetectors();
    }

    @Override
    public void end() {

        super.end();
        paddleTimer.stop();
    }

    @Override
    public Paddle[] getPaddles() {

        int numberOfPaddles = 0;
        for (ArrayList<Paddle> brickRow : bricks) {
            for (Iterator<Paddle> iterator = brickRow.iterator(); iterator.hasNext(); iterator.next()) {
                numberOfPaddles++;
            }
        }
        Paddle[] paddles = new Paddle[numberOfPaddles + 1];

        int currentPosition = 0;

        for (ArrayList<Paddle> brickRow : bricks) {

            Paddle[] brickRowArray = new Paddle[brickRow.size()];
            brickRowArray = brickRow.toArray(brickRowArray);
            System.arraycopy(brickRowArray, 0, paddles, currentPosition, brickRowArray.length);
            currentPosition += brickRowArray.length;
        }

        paddles[paddles.length - 1] = playerPaddle;

        return paddles;
    }

    @Override
    void initPaddles() {

        playerPaddle = new Paddle(this, INITIAL_PADDLE1_X, INITIAL_PADDLE1_Y, INITIAL_PADDLE_WIDTH, INITIAL_PADDLE_HEIGHT, Paddle.LEFT, PaddleMode.HUMAN);
        bricks = new ArrayList<ArrayList<Paddle>>();
        brickColor = Color.RED;

        paddleTimer = playerPaddle.getPlayerModeTimer(KeyEvent.VK_UP, KeyEvent.VK_DOWN);
        paddleTimer.start();
    }

    @Override
    void drawCanvas(Graphics g) {

        super.drawCanvas(g);
        String[] allInfo = new String[] {String.format(scoreString, score), String.format(livesString, livesLeft)};
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setFont(new Font(scoreFont, Font.PLAIN, scoreFontSize));
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2d.setColor(getForegroundColor());
        g2d.fill(playerPaddle);

        float stringHeight = (float) g2d.getFontMetrics().getStringBounds("\"y", g2d).getHeight();
        float widestStringWidth = (float) g2d.getFontMetrics().getStringBounds(livesString, g2d).getWidth();

        for (int i = 0; i < allInfo.length; i++) {

            g2d.drawString(allInfo[i], getWidth() / 4 - widestStringWidth / 2, stringHeight * i + 20);
        }

        infoBounds = new Rectangle2D.Float(getWidth() / 4 - widestStringWidth / 2, 0, widestStringWidth, stringHeight * allInfo.length + 20).getBounds();

        g2d.setColor(brickColor);

        for (ArrayList<Paddle> brickRow : bricks) {
            for (Paddle brick : brickRow) {
                g2d.fill(brick);
            }
        }

        if (gameOver) {
            g2d.setColor(getForegroundColor());
            g2d.setFont(new Font(scoreFont, Font.PLAIN, 50));
            String gameOverString = lang.get("gameOver").toUpperCase();
            int newStringWidth = (int) g2d.getFontMetrics().getStringBounds(gameOverString, g2d).getWidth();
            int newStringHeight = (int) g2d.getFontMetrics().getStringBounds(gameOverString, g2d).getHeight();
            int xstart = getWidth() / 2 - newStringWidth / 2;
            int ystart = getHeight() / 2 - newStringHeight / 2;
            g2d.drawString(gameOverString, xstart, ystart);
        }

        g2d.dispose();
    }

    @Override
    void addListeners() {

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {

                if (!isPaused()) {
                    Rectangle oldBounds = playerPaddle.getBounds();
                    if (e.getY() <= getHeight() - PongPreferencesEditor.PADDING - playerPaddle.getHeight() / 2 && e.getY() > PongPreferencesEditor.PADDING + playerPaddle.getHeight() / 2) {
                        playerPaddle.setPaddleY(e.getY() - playerPaddle.getHeight() / 2);
                    }
                    if (e.getY() >= getHeight() - PongPreferencesEditor.PADDING - playerPaddle.getHeight() / 2) {
                        playerPaddle.setPaddleY(getHeight() - PongPreferencesEditor.PADDING - playerPaddle.getHeight());
                    }
                    if (e.getY() <= PongPreferencesEditor.PADDING) {
                        playerPaddle.setPaddleY(PongPreferencesEditor.PADDING);
                    }
                    repaint(oldBounds);
                    repaint(playerPaddle.getBounds());
                }
            }
        });

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {

                brickHeight = (getHeight() - ((rows + 1) * interSpace)) / rows;
                initialX = getWidth() - interSpace - INITIAL_PADDLE_WIDTH;

                double currentY = interSpace;

                for (ArrayList<Paddle> brickRow : bricks) {
                    double currentX = initialX;

                    for (int i = brickRow.size() - 1; i >= 0; i--) {

                        brickRow.get(i).setPaddleHeight(brickHeight);
                        brickRow.get(i).setPaddleX(currentX);
                        brickRow.get(i).setPaddleY(currentY);
                        currentX -= INITIAL_PADDLE_WIDTH + interSpace;

                    }

                    currentY += brickHeight + interSpace;
                }
                if (playerPaddle.getLowerLimit() >= e.getComponent().getHeight() - PongPreferencesEditor.PADDING) {
                    playerPaddle.setPaddleY(e.getComponent().getHeight() - PongPreferencesEditor.PADDING - playerPaddle.getHeight());
                }
                repaint();
            }
        });
    }

    @Override
    public PongMode getMode() {

        return PongMode.SINGLE_PLAYER_BREAKOUT;
    }

    @Override
    public boolean isPlayer1Turn() {

        return false;
    }

    @Override
    public void setPlayer1Turn(boolean player) {

    }

    public void setBrickColor(Color brickColor) {

        this.brickColor = brickColor;
    }

    @Override
    void addCollisionDetectors() {

        pongWallCollisionDetector = new PongWallCollisionDetector(this, true);

        for (final Paddle paddle : getPaddles()) {

            addPaddleCollisionDetector(new PongPaddleCollisionDetector(this, paddle) {
                @Override
                public void performCustomAction() {
                    if (!paddle.getPaddleMode().equals(PaddleMode.HUMAN)) {
                        for (ArrayList<Paddle> brickRow : bricks) {
                            brickRow.remove(paddle);  // iterating over bricks, not brickRow, so no need to call Iterator#remove()
                        }
                        paddle.setToBeDestroyed(true);
                        repaint(paddle.getBounds());
                        score += (int) Math.round(paddle.getValue() * (1 / playerPaddle.getHeight()) * ball.getSpeed() / (ball.getBallDiameter() / 20) * 100 * 100);
                        repaint(infoBounds);
                    }
                }
            });
        }
    }

    @Override
    public void increasePlayerScore(int player) {

        livesLeft--;
        repaint(infoBounds);
    }

    @Override
    public boolean isGameOver() {

        if (livesLeft <= 0 || getCollisionDetectors().size() == 1) {
            gameOver = true;
            fireStateChanged();
            repaint();
            return true;
        } else {
            return false;
        }
    }

    public Color getBrickColor() {

        return brickColor;
    }
}