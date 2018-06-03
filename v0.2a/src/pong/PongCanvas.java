// File: PongCanvas.java
// Author: Leo Horne
// Date Created: Friday, March 6, 2015
// Version: 0.2

/* Improvements in this version:
    * Uses Thread animation instead of Timer animation (animationCycle and addNotify methods)
    * Modified addListeners method so only one MouseMotionListener is used
    * New Ball and Paddle classes
*/

package pong;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.MessageFormat;

class PongCanvas extends JPanel {

    Paddle paddle1, paddle2;
    private Ball ball;
    int panelWidth = 950, panelHeight = 600;
    private final static int INITIAL_PADDLE_WIDTH = 20, INITIAL_PADDLE_HEIGHT = 100;
    private final int INITIAL_PADDLE1_X = 10, INITIAL_PADDLE2_X = panelWidth - INITIAL_PADDLE_WIDTH - 10, INITIAL_PADDLE1_Y = panelHeight / 2 - INITIAL_PADDLE_HEIGHT / 2, INITIAL_PADDLE2_Y = INITIAL_PADDLE1_Y;
    private static final int INITIAL_X_TRAJECTORY = 5, INITIAL_Y_TRAJECTORY = 5;
    private static final double INITIAL_Y_BEND_FACTOR = 4;  // higher value = less reflection (due to division)
    private static final int INITIAL_BALL_X = 50, INITIAL_BALL_Y = 50;
    private static final int INITIAL_BALL_DIAMETER = 20;
    private int timerDelay = 25;
    boolean isPlayer1Turn = false;
    Integer player1Score = 0, player2Score = 0;
    private int maxScore = 10;
    private String winnerString = "The {0} side wins!";
    private int scoreFontSize = 50;
    private String scoreFont = "Andale Mono";
    private Thread animationThread;

    public PongCanvas() {
        initGUI();
        initCanvas();
    }

    private void initGUI() {

        setPreferredSize(new Dimension(panelWidth, panelHeight));
        setSize(panelWidth, panelHeight);
        setBackground(Color.BLACK);
        setDoubleBuffered(true);
        addListeners();
    }

    private void initCanvas() {

        paddle1 = new Paddle(INITIAL_PADDLE1_X, INITIAL_PADDLE1_Y, INITIAL_PADDLE_WIDTH, INITIAL_PADDLE_HEIGHT, Paddle.LEFT);
        paddle2 = new Paddle(INITIAL_PADDLE2_X, INITIAL_PADDLE2_Y, INITIAL_PADDLE_WIDTH, INITIAL_PADDLE_HEIGHT, Paddle.RIGHT);

        ball = new Ball(this, INITIAL_BALL_X, INITIAL_BALL_Y, INITIAL_BALL_DIAMETER, INITIAL_X_TRAJECTORY, INITIAL_Y_TRAJECTORY, INITIAL_Y_BEND_FACTOR);

    }

    private void drawCanvas(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.WHITE);

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
    }



    private void addListeners() {

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent mouseEvent) {
                if (isPlayer1Turn) {
                    paddle1.setPaddleY(mouseEvent.getY());
                } else {
                    paddle2.setPaddleY(mouseEvent.getY());
                }
            }
        });
    }

    private void animationCycle() {

        ball.checkCollisions();
        ball.move();
    }

    @Override
    public void addNotify() {

        super.addNotify();

        animationThread = new Thread(new Runnable() {
            @Override
            public void run() {

                long beforeTime, sleepTime, timeDifference;

                beforeTime = System.currentTimeMillis();

                while (true) {

                    animationCycle();
                    repaint();

                    if (player1Score == maxScore || player2Score == maxScore) {
                        break;
                    }

                    timeDifference = System.currentTimeMillis() - beforeTime;
                    sleepTime = timerDelay - timeDifference;

                    if (sleepTime < 0) {
                        sleepTime = 2;
                    }

                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    beforeTime = System.currentTimeMillis();
                }
            }
        });

        animationThread.start();
    }

    @Override
    public void paintComponent(Graphics g) {

        Toolkit.getDefaultToolkit().sync();
        super.paintComponent(g);
        drawCanvas(g);

        if (player1Score == maxScore) {
            winnerString = MessageFormat.format(winnerString, "left");
            int stringWidth = (int) g.getFontMetrics().getStringBounds(winnerString, g).getWidth();
            int stringHeight = (int) g.getFontMetrics().getStringBounds(winnerString, g).getHeight();
            int xstart = panelWidth / 2 - stringWidth / 2;
            int ystart = panelHeight / 2 - stringHeight / 2;
            g.drawString(winnerString, xstart, ystart);

        } else if (player2Score == maxScore) {
            winnerString = MessageFormat.format(winnerString, "right");
            int stringWidth = (int) g.getFontMetrics().getStringBounds(winnerString, g).getWidth();
            int stringHeight = (int) g.getFontMetrics().getStringBounds(winnerString, g).getHeight();
            int xstart = panelWidth / 2 - stringWidth / 2;
            int ystart = panelHeight / 2 - stringHeight / 2;
            g.drawString(winnerString, xstart, ystart);
        }
    }
}