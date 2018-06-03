// File: PongCanvas.java
// Author: Leo
// Date Created: 4/4/15 6:22 PM

package pong.mode;

import pong.animation.BallAnimation;
import pong.ball.Ball;
import pong.collision.PongPaddleCollisionDetector;
import pong.collision.PongWallCollisionDetector;
import pong.paddle.Paddle;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
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
    protected Ball ball;
    protected BallAnimation animationThread;
    protected Color backgroundColor = Color.BLACK;
    protected Color foregroundColor = Color.WHITE;
    private Vector<PongPaddleCollisionDetector> pongPaddleCollisionDetectors = new Vector<PongPaddleCollisionDetector>();
    protected PongWallCollisionDetector pongWallCollisionDetector;
    protected EventListenerList listenerList = new EventListenerList();
    private double ballOffset = 40;
    private boolean paused = false;

    public PongCanvas() {

        initGUI();
        initBall();
        initPaddles();
        setPongWallCollisionDetector(new PongWallCollisionDetector(this));
        addCollisionDetectors();
    }

    public void end() {

        paused = true;
        animationThread.kill();
    }

    public void pause() {

        animationThread.setPaused(true);
        paused = true;
    }

    public void resume() {

        animationThread.setPaused(false);
        paused = false;
    }

    public boolean isPaused() {

        return paused;
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
        g2d.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight());

        g2d.dispose();

        // Subclasses must call g2d.dispose()
    }

    private void initGUI() {

        setPreferredSize(new Dimension(INITIAL_PANEL_WIDTH, INITIAL_PANEL_HEIGHT));
        setSize(INITIAL_PANEL_WIDTH, INITIAL_PANEL_HEIGHT);
        setBackground(backgroundColor);
        setDoubleBuffered(true);
        setFocusable(true);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (ball.getY() + ball.getBallDiameter() + 2 > e.getComponent().getHeight()) {
                    ball.setBallY(e.getComponent().getHeight() - ball.getBallDiameter() - 2);
                }
                if (ball.getX() + ball.getBallDiameter() + 2 > e.getComponent().getWidth() - ballOffset) {
                    ball.setBallX(e.getComponent().getWidth() - ball.getBallDiameter() - ballOffset - 2);
                }
                repaint();
            }
        });
        addListeners();
    }

    @Override
    public void setBackground(Color background) {

        super.setBackground(background);
        backgroundColor = background;
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
        animationThread = new BallAnimation(this);
        animationThread.start();
    }

    public Thread getAnimationThread() {

        return animationThread;
    }

    public abstract PongMode getMode();

    public abstract boolean isPlayer1Turn();

    public abstract void setPlayer1Turn(boolean player);

    abstract void addCollisionDetectors();

    public void addPaddleCollisionDetector(PongPaddleCollisionDetector p) {

        pongPaddleCollisionDetectors.add(p);
    }

    public void removePaddleCollisionDetector(PongPaddleCollisionDetector p) {

        pongPaddleCollisionDetectors.remove(p);
    }

    public PongPaddleCollisionDetector getPaddleCollisionDetectorForPaddle(Paddle paddle) {

        PongPaddleCollisionDetector ppcd_linkedToPaddle = null;
        for (PongPaddleCollisionDetector p : pongPaddleCollisionDetectors) {
            if (p.getPaddle().equals(paddle)) {
                ppcd_linkedToPaddle = p;
            }
        }
        return ppcd_linkedToPaddle;
    }

    public void removeAllPaddleCollisionDetectors() {

        pongPaddleCollisionDetectors.removeAllElements();
    }

    public Vector<PongPaddleCollisionDetector> getPongPaddleCollisionDetectors() {

        return pongPaddleCollisionDetectors;
    }

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

    public void setBallOffset(double ballOffset) {

        this.ballOffset = ballOffset;
    }

    public double getBallOffset() {

        return ballOffset;
    }

    public void addChangeListener(ChangeListener c) {

        listenerList.add(ChangeListener.class, c);
    }

    public void removeChangeListener(ChangeListener c) {

        listenerList.remove(ChangeListener.class, c);
    }

    protected void fireStateChanged() {

        ChangeListener[] listeners = listenerList.getListeners(ChangeListener.class);
        if (listeners != null && listeners.length > 0) {
            ChangeEvent evt = new ChangeEvent(this);
            for (ChangeListener listener : listeners) {
                listener.stateChanged(evt);
            }
        }
    }

    public abstract void increasePlayerScore(int player);

    public abstract boolean isGameOver();
}