// File: TrainingPongCanvas.java
// Author: Leo
// Date Created: 4/9/15 4:35 PM
// Version: 0.1a

package pong.mode;

import pong.collision.PongPaddleCollisionDetector;
import pong.collision.PongWallCollisionDetector;
import pong.control.PongPreferencesEditor;
import pong.lang.Language;
import pong.paddle.Paddle;
import pong.paddle.PaddleMode;
import pong.screen.Pong;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class SinglePlayerTrainingPongCanvas extends PongCanvas {

    private Paddle paddle, wall;
    private Color wallColor;
    private int hits = 0;
    private int misses = 0;
    private final static int MAX_MISSES = 5;
    private int finalScore = 0;
    private int score = 0;
    private double wallXRatio = 0.125;
    private double wallHeightRatio = 0.75;
    private String scoreFont = Font.SANS_SERIF;
    private Map<String, String> lang = Language.getLanguageMap(Language.getLoc());
    private String hitString = lang.get("hitString");
    private String missString = lang.get("missString");
    private String scoreString = lang.get("scoreString");
    private String livesString = lang.get("livesString");
    private String[] allInfo;
    private int scoreFontSize = 15;
    private Timer scoreTimer;
    private Timer paddleTimer;
    private int timeRemaining = 10 * 60;  // 10 minutes in seconds
    private String timeRemainingStub = lang.get("timeRemaining");
    private String timeRemainingString = timeRemainingStub + " 10:00";
    private boolean timeHasRunOut = false;
    private boolean gameOver = false;
    private Rectangle infoBounds;

    public SinglePlayerTrainingPongCanvas() {

        super();
        startScoreTimer();
    }

    @Override
    public void end() {

        super.end();
        scoreTimer.stop();
        paddleTimer.stop();
    }

    private void displayGameOverDialog() {

        pause();
        int choice = JOptionPane.showConfirmDialog(this, lang.get("gameOverOptions"), lang.get("gameOver"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (choice == JOptionPane.YES_OPTION) {
            resume();
            repaint();
        } else {
            gameOver = true;
            repaint();
        }
    }

    private void startScoreTimer() {

        scoreTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (timeRemaining <= 0) {
                    timeHasRunOut = true;
                    finalScore = score;
                    displayGameOverDialog();
                    scoreTimer.stop();
                    return;
                }
                if (MAX_MISSES - misses <= 0) {
                    timeRemainingString = timeRemainingStub + " N/A";
                    displayGameOverDialog();
                    scoreTimer.stop();
                    return;
                }
                timeRemaining--;
                long minsLeft = TimeUnit.SECONDS.toMinutes(timeRemaining);
                long secsLeft = timeRemaining - TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(timeRemaining));
                String minsLeftFinal = String.valueOf(minsLeft).length() < 2 ? "0" + String.valueOf(minsLeft) : String.valueOf(minsLeft);
                String secsLeftFinal = String.valueOf(secsLeft).length() < 2 ? "0" + String.valueOf(secsLeft) : String.valueOf(secsLeft);
                timeRemainingString = String.format(timeRemainingStub + " %s:%s", minsLeftFinal, secsLeftFinal);
                repaint(infoBounds);
            }
        });
    }

    private void calculateScore() {

        score += (int) Math.round(((double) 1 /*/ (misses + 1.0)*/) /* * (1.0 / (Math.pow(wall.getHeight(), 4)))*/ * ball.getSpeed()  * 10e2 * Math.pow((1 - (wall.getHeight() / getHeight())), 5) / (getBall().getBallDiameter() / 20));
    }

    @Override
    public Paddle[] getPaddles() {

        return new Paddle[]{paddle, wall};
    }

    @Override
    void initPaddles() {

        paddle = new Paddle(this, INITIAL_PADDLE1_X, INITIAL_PADDLE1_Y, INITIAL_PADDLE_WIDTH, INITIAL_PADDLE_HEIGHT, Paddle.LEFT, PaddleMode.HUMAN);
        wall = new Paddle(this, getWidth() - 7, (getHeight() / 2) - ((int) (getHeight() * 0.75) / 2), 5, (int) (getHeight() * 0.75), Paddle.RIGHT, PaddleMode.NONE);

        paddleTimer = paddle.getPlayerModeTimer(KeyEvent.VK_UP, KeyEvent.VK_DOWN);
        paddleTimer.start();
    }

    @Override
    void drawCanvas(Graphics g) {

        super.drawCanvas(g);
        allInfo = new String[] {String.format(hitString, hits),
                                String.format(missString, misses),
                                String.format(scoreString, MAX_MISSES - misses > 0 && !timeHasRunOut ? score : finalScore),
                                String.format(livesString, misses <= MAX_MISSES ? MAX_MISSES - misses : 0),
                                timeRemainingString};
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(getForegroundColor());
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
        g2d.fill(paddle);

        float stringHeight = (float) g2d.getFontMetrics().getStringBounds("\"y", g2d).getHeight();
        float widestStringWidth = (float) g2d.getFontMetrics().getStringBounds(timeRemainingString, g2d).getWidth();
        for (int i = 0; i < allInfo.length; i++) {
            g2d.drawString(allInfo[i], getWidth() / 4 - widestStringWidth / 2, stringHeight * i + 30);
        }
        infoBounds = new Rectangle2D.Float(getWidth() / 4 - widestStringWidth / 2, 0, widestStringWidth, stringHeight * allInfo.length + 30).getBounds();
        g2d.setColor(wallColor);
        g2d.fill(wall);

        if (gameOver) {
            g2d.setColor(getForegroundColor());
            g2d.setFont(new Font(scoreFont, Font.PLAIN, 50));
            String gameOverString = lang.get("gameOver").toUpperCase();
            int newStringWidth = (int) g2d.getFontMetrics().getStringBounds(gameOverString, g2d).getWidth();
            int xstart = getWidth() / 2 - newStringWidth / 2;
            int ystart = getHeight() / 2;
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
                    Rectangle oldBounds = paddle.getBounds();
                    if (e.getY() <= getHeight() - PongPreferencesEditor.PADDING - paddle.getHeight() / 2 && e.getY() > PongPreferencesEditor.PADDING + paddle.getHeight() / 2) {
                        paddle.setPaddleY(e.getY() - paddle.getHeight() / 2);
                    }
                    if (e.getY() >= getHeight() - PongPreferencesEditor.PADDING - paddle.getHeight() / 2) {
                        paddle.setPaddleY(getHeight() - PongPreferencesEditor.PADDING - paddle.getHeight());
                    }
                    if (e.getY() <= PongPreferencesEditor.PADDING) {
                        paddle.setPaddleY(PongPreferencesEditor.PADDING);
                    }
                    repaint(oldBounds);
                    repaint(paddle.getBounds());
                }
            }
        });

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {

                wall.setPaddleX(e.getComponent().getWidth() - 7);
                wall.setPaddleHeight(e.getComponent().getHeight() * wallHeightRatio);
                wall.setPaddleY(e.getComponent().getHeight() * wallXRatio);
                repaint();
                for (Paddle paddle : getPaddles()) {
                    if (paddle.getHeight() > getHeight() - 2 * PongPreferencesEditor.PADDING) {
                        paddle.setPaddleHeight(getHeight() - 2 * PongPreferencesEditor.PADDING);
                    }
                }
                if (paddle.getLowerLimit() >= e.getComponent().getHeight() - PongPreferencesEditor.PADDING) {
                    paddle.setPaddleY(e.getComponent().getHeight() - PongPreferencesEditor.PADDING - paddle.getHeight());
                }
            }
        });
    }

    @Override
    public PongMode getMode() {

        return PongMode.SINGLE_PLAYER_TRAINING;
    }

    @Override
    public boolean isPlayer1Turn() {

        return true;
    }

    @Override
    public void setPlayer1Turn(boolean player) {

    }

    @Override
    void addCollisionDetectors() {

        pongWallCollisionDetector = new PongWallCollisionDetector(this, Paddle.RIGHT);

        addPaddleCollisionDetector(new PongPaddleCollisionDetector(this, paddle));
        addPaddleCollisionDetector(new PongPaddleCollisionDetector(this, wall) {
            @Override
            public void performCustomAction() {
                hits++;
                calculateScore();
                repaint(infoBounds);
            }
        });
    }

    @Override
    public void increasePlayerScore(int player) {  // used to increment misses here

        misses++;
        if (misses == MAX_MISSES) {
            finalScore = score;
        }
        repaint(infoBounds);
    }

    @Override
    public boolean isGameOver() {

        if (gameOver) {
            fireStateChanged();
            return gameOver;
        } else {
            return false;
        }
    }

    @Override
    public void pause() {

        super.pause();
        scoreTimer.stop();
    }

    @Override
    public void resume() {

        super.resume();
        if (timeRemaining > 0 && MAX_MISSES - misses > 0) {
            scoreTimer.start();
        }
    }

    public void setWallHeight(double height) {

        wall.setPaddleHeight(height);
        wallHeightRatio = height / getHeight();
    }

    public double getWallHeight() {

        return wall.getPaddleHeight();
    }

    public Paddle getWall() {

        return wall;
    }

    public void setWallColor(Color wallColor) {

        this.wallColor = wallColor;
    }

    public void setWallXRatio(double wallXRatio) {
        this.wallXRatio = wallXRatio;
    }
}