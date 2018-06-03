// File: PongCanvas.java
// Author: Leo
// Date Created: 4/4/15 6:22 PM

package pong.mode;

import pong.animation.BallAnimation;
import pong.ball.Ball;
import pong.collision.PongWallCollisionDetector;
import pong.paddle.Paddle;
import pong.collision.PongPaddleCollisionDetector;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public abstract class PongCanvas extends JPanel {

    public static final int INITIAL_PANEL_WIDTH = 950;
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
    public static final int INITIAL_X_TRAJECTORY = 9;
    public static final int INITIAL_Y_TRAJECTORY = 9;
    private int panelWidth = INITIAL_PANEL_WIDTH;
    private int panelHeight = INITIAL_PANEL_HEIGHT;
    private Graphics2D g2d;
    Ball ball;
    Thread animationThread;

    Color backgroundColor = Color.BLACK;
    Color foregroundColor = Color.WHITE;

    ArrayList<PongPaddleCollisionDetector> pongPaddleCollisionDetectors = new ArrayList<PongPaddleCollisionDetector>();
    PongWallCollisionDetector pongWallCollisionDetector;

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

    public abstract int getNumberOfPaddles();

    public abstract Paddle[] getPaddles();

    public Ball getBall() {

        return ball;
    }

    void initBall() {

        ball = new Ball(this, INITIAL_BALL_X, INITIAL_BALL_Y, INITIAL_BALL_DIAMETER, INITIAL_X_TRAJECTORY, INITIAL_Y_TRAJECTORY);
    }

    abstract void initPaddles();

    void drawCanvas(Graphics g) {

        g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setColor(foregroundColor);

        g2d.fill(ball);

        BasicStroke dotted = new BasicStroke(5, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 0, new float[]{40}, 0);  // BasicStroke(float width, int cap, int join, float miterlimit, float[] dash, float dash_phase)
        g2d.setStroke(dotted);
        g2d.drawLine(panelWidth / 2, 0, panelWidth / 2, panelHeight);
    }

    private void initGUI() {

        setPreferredSize(new Dimension(INITIAL_PANEL_WIDTH, INITIAL_PANEL_HEIGHT));
        setSize(INITIAL_PANEL_WIDTH, INITIAL_PANEL_HEIGHT);
        setBackground(backgroundColor);
        setDoubleBuffered(true);
        setFocusable(true);
        addListeners();
    }

    public void reset() {

        ball.setBallX(INITIAL_BALL_X);
        ball.setBallY(INITIAL_BALL_Y);
        ball.setTrajectory(INITIAL_X_TRAJECTORY, INITIAL_Y_TRAJECTORY);
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

    public ArrayList<PongPaddleCollisionDetector> getCollisionDetectors() {

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

    public Graphics2D getGraphics2D() {

        return g2d;
    }
}