// File: PongCanvas.java
// Author: Leo Horne
// Date Created: Thursday, March 4, 2015
// Version: 0.1

package pong;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

class PongCanvas extends JPanel {

    private Rectangle2D paddle1, paddle2;
    private Ellipse2D ball;
    private int panelWidth = 950, panelHeight = 600;
    private int paddleWidth = 20, paddleHeight = 100;
    private int paddle1x = 10, paddle2x = panelWidth - paddleWidth - 10, paddle1y = panelHeight / 2 - paddleHeight / 2, paddle2y = paddle1y;
    private int xTrajectory = 5, yTrajectory = 5;
    private double yBendFactor = 4;  // higher value = less reflection (division)
    private int currentXTrajectory = Math.abs(xTrajectory), currentYTrajectory = Math.abs(yTrajectory);
    private int ballx = 50, bally = 50;
    private int ballDiameter = 20;
    private int timerDelay = 15;  // TODO: replace by Thread animation
    private boolean isPlayer1Turn = false;
    private Integer player1Score = 0, player2Score = 0;
    private int scoreFontSize = 50;
    private String scoreFont = "Andale Mono";
    private Timer timer;

    public PongCanvas() {
        initGUI();
    }

    private void initGUI() {

        setPreferredSize(new Dimension(panelWidth, panelHeight));
        setBackground(Color.BLACK);
        setDoubleBuffered(true);
        addListeners();

        timer = new Timer(timerDelay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {  // TODO: replace by Thread
                checkCollisions();
                ballx += xTrajectory;
                bally += yTrajectory;
                repaint();
            }
        });
        timer.start();
    }

    private void setupCanvas(Graphics g) {

        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setColor(Color.WHITE);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        paddle1 = new Rectangle2D.Double(paddle1x, paddle1y, paddleWidth, paddleHeight);
        paddle2 = new Rectangle2D.Double(paddle2x, paddle2y, paddleWidth, paddleHeight);

        ball = new Ellipse2D.Double(ballx, bally, ballDiameter, ballDiameter);

        g2d.fill(paddle1);
        g2d.fill(paddle2);
        g2d.fill(ball);

        g2d.setFont(new Font(scoreFont, Font.PLAIN, scoreFontSize));
        g2d.drawString(player1Score.toString(), panelWidth/4 - scoreFontSize/2, scoreFontSize);
        g2d.drawString(player2Score.toString(), panelWidth*3/4 - scoreFontSize/2, scoreFontSize);

        BasicStroke dotted = new BasicStroke(5, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 0, new float[] {40}, 0);  // BasicStroke(float width, int cap, int join, float miterlimit, float[] dash, float dash_phase)
        g2d.setStroke(dotted);

        g2d.drawLine(panelWidth/2, 0, panelWidth/2, panelHeight);

        Toolkit.getDefaultToolkit().sync();
        g2d.dispose();
    }

    private boolean ballOutOfBounds() {
        return ballx > panelWidth + ballDiameter || ballx < -ballDiameter;
    }

    private boolean ballHitTopOrBottom() {
        return bally >= panelHeight - ballDiameter || bally <= 0;
    }

    private boolean ballHitPaddle1OuterEdge() {
        return ballx - 0.5 <= paddle1x + paddleWidth;
    }

    private boolean ballNotBehindPaddle1() {
        return ballx - 0.5 >= paddle1x + paddleWidth - Math.abs(xTrajectory);
    }

    private boolean ballDidNotSurpassPaddle1UpperEdge() {
        return bally >= paddle1y;
    }

    private boolean ballDidNotSurpassPaddle1LowerEdge() {
        return bally <= paddle1y + paddleHeight;
    }

    private boolean ballHitPaddle2OuterEdge() {
        return ballx + 0.5 >= paddle2x - paddleWidth;
    }

    private boolean ballNotBehindPaddle2() {
        return ballx + 0.5 <= paddle2x - paddleWidth + Math.abs(xTrajectory);
    }

    private boolean ballDidNotSurpassPaddle2UpperEdge() {
        return bally >= paddle2y;
    }

    private boolean ballDidNotSurpassPaddle2LowerEdge() {
        return bally <= (paddle2y + paddleHeight);
    }

    private void checkCollisions() {

        if (ballOutOfBounds()) {
            serve();
        } else if (ballHitTopOrBottom()) {
            yTrajectory = -yTrajectory;
        } else if ((ballHitPaddle1OuterEdge() && ballNotBehindPaddle1()) && ballDidNotSurpassPaddle1UpperEdge() && ballDidNotSurpassPaddle1LowerEdge()) {
            checkPaddleCollisions();
            xTrajectory = -xTrajectory;
            isPlayer1Turn = false;
        } else if ((ballHitPaddle2OuterEdge() && ballNotBehindPaddle2()) && ballDidNotSurpassPaddle2UpperEdge() && ballDidNotSurpassPaddle2LowerEdge()) {
            checkPaddleCollisions();
            xTrajectory = -xTrajectory;
            isPlayer1Turn = true;
        }
    }

    private void checkPaddleCollisions() {

        int paddleCenter;
        int hitDistance;
        int ballCenter;

        if (isPlayer1Turn) {
            paddleCenter = paddle1y + paddleHeight / 2;
        } else {
            paddleCenter = paddle2y + paddleHeight / 2;
        }

        ballCenter = bally + ballDiameter / 2;
        hitDistance = ballCenter - paddleCenter;

        yTrajectory = (int)(Math.abs(hitDistance) / yBendFactor) * (yTrajectory / Math.abs(yTrajectory));
        if (yTrajectory == 0) {
            yTrajectory += 1;
        }

        if (yTrajectory < 0 && hitDistance > 0) {
            yTrajectory = -yTrajectory;
        } else if (yTrajectory > 0 && hitDistance < 0) {
            yTrajectory = -yTrajectory;
        }
    }

    private void addListeners() {

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent mouseEvent) {
                if (isPlayer1Turn) {
                    paddle1y = mouseEvent.getY();
                }
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent mouseEvent) {
                if (!isPlayer1Turn) {
                    paddle2y = mouseEvent.getY();
                }
            }
        });
    }

    private void serve() {

        yTrajectory = 0;
        if (ballx < -300) {
            player2Score++;
            ballx = 50;
            bally = 50;
            xTrajectory = currentXTrajectory;
            yTrajectory = currentYTrajectory;
            isPlayer1Turn = false;
        } else if (ballx > panelWidth + 300) {
            player1Score++;
            ballx = panelWidth - 50;
            bally = 50;
            xTrajectory = -currentXTrajectory;
            yTrajectory = currentYTrajectory;
            isPlayer1Turn = true;
        }
    }

    @Override
    public void paintComponent(Graphics g) {

        Toolkit.getDefaultToolkit().sync();
        super.paintComponent(g);
        setupCanvas(g);
    }
}