// File: PongCanvas.java
// Author: Leo
// Date Created: 4/4/15 6:22 PM

package pong.mode;

import pong.animation.BallAnimation;
import pong.ball.Ball;
import pong.collision.PongWallCollisionDetector;
import pong.collision.PongPaddleCollisionDetector;
import pong.paddle.Paddle;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Vector;

public abstract class PongCanvas extends JPanel {

    public static final int INITIAL_PANEL_WIDTH = 1000;
    public static final int INITIAL_PANEL_HEIGHT = 600;
    public static final int INITIAL_PADDLE_WIDTH = 20;
    public static final int INITIAL_PADDLE_HEIGHT = 100;
    public static final int INITIAL_PADDLE1_X = 10;
    public static final int INITIAL_PADDLE2_X = INITIAL_PANEL_WIDTH - INITIAL_PADDLE_WIDTH - 10;
    public static final int INITIAL_PADDLE1_Y = INITIAL_PANEL_HEIGHT / 2;
    public static final int INITIAL_PADDLE2_Y = INITIAL_PADDLE1_Y;
    public static final int INITIAL_BALL_X = 50;
    public static final int INITIAL_BALL_Y = 50;
    public static final int INITIAL_BALL_DIAMETER = 20;
    public static final double INITIAL_X_TRAJECTORY = 1;
    public static final double INITIAL_Y_TRAJECTORY = 1;
    Ball ball;
    Thread animationThread;
    Color backgroundColor = Color.BLACK;
    Color foregroundColor = Color.WHITE;
    Vector<PongPaddleCollisionDetector> pongPaddleCollisionDetectors = new Vector<PongPaddleCollisionDetector>();
    PongWallCollisionDetector pongWallCollisionDetector;
    private int panelWidth = INITIAL_PANEL_WIDTH;
    private int panelHeight = INITIAL_PANEL_HEIGHT;

    public PongCanvas() {

        initGUI();
        initBall();
        initPaddles();
        setPongWallCollisionDetector(new PongWallCollisionDetector(this));
        addCollisionDetectors();
    }

    public void deinit() {

        ball.deinit();
    }

    public abstract Paddle[] getPaddles();

    public Ball getBall() {

        return ball;
    }

    void initBall() {

        ball = new Ball(this, INITIAL_BALL_X, INITIAL_BALL_Y, INITIAL_BALL_DIAMETER, INITIAL_X_TRAJECTORY, INITIAL_Y_TRAJECTORY);
    }

    abstract void initPaddles();

    void drawCanvas(Graphics g) {

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2d.setColor(foregroundColor);

        g2d.fill(ball);

        BasicStroke dotted = new BasicStroke(5, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 0, new float[]{40}, 0);  // BasicStroke(float width, int cap, int join, float miterlimit, float[] dash, float dash_phase)
        g2d.setStroke(dotted);
        g2d.drawLine(panelWidth / 2, 0, panelWidth / 2, panelHeight);

        g2d.dispose();

        // Subclasses must call g2d.dispose()
    }

    private void initGUI() {

        setPreferredSize(new Dimension(INITIAL_PANEL_WIDTH, INITIAL_PANEL_HEIGHT));
        setSize(INITIAL_PANEL_WIDTH, INITIAL_PANEL_HEIGHT);
        setBackground(backgroundColor);
        setDoubleBuffered(true);
        setFocusable(true);
        addListeners();
    }

    abstract void addListeners();

    @Override
    public void paintComponent(Graphics g) {

        Toolkit.getDefaultToolkit().sync();
        super.paintComponent(g);
        drawCanvas(g);
    }

    @Override
    public void addNotify() {

        super.addNotify();
        animationThread = new Thread(new BallAnimation(this));
        animationThread.start();
    }

    public int getPanelWidth() {

        return panelWidth;
    }

    public int getPanelHeight() {

        return panelHeight;
    }

    public Thread getAnimationThread() {

        return animationThread;
    }

    public abstract PongMode getMode();

    public abstract boolean isPlayer1Turn();

    public abstract void setPlayer1Turn(boolean player);

    abstract void addCollisionDetectors();

    public Vector<PongPaddleCollisionDetector> getCollisionDetectors() {

        return pongPaddleCollisionDetectors;
    }

    public PongWallCollisionDetector getPongWallCollisionDetector() {

        return pongWallCollisionDetector;
    }

    private void setPongWallCollisionDetector(PongWallCollisionDetector pongWallCollisionDetector) {

        this.pongWallCollisionDetector = pongWallCollisionDetector;
    }

    public Color getForegroundColor() {

        return foregroundColor;
    }

    public void setForegroundColor(Color color) {

        foregroundColor = color;
    }

    public Color getBackgroundColor() {

        return backgroundColor;
    }

    public abstract void increasePlayerScore(int player);

    public abstract int getPlayerScore(int player);

    public abstract int getMaxScore();
}