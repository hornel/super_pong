// File: PongCanvas.java
// Author: Leo Horne
// Date Created: Saturday, March 7, 2015
// Version: 0.3a

/* Improvements in this version:
    * New getters and setters for access to private variables
    * New functions (reset()) to accommodate PongControls
*/

package pong;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

/**
 * Class which creates and manages the Pong canvas
 * seen on the screen. Sets up interactions between
 * user and screen elements (paddles).
 */

class TwoHumanPlayerPongCanvas extends JPanel implements PongPanel {

    private int panelWidth = INITIAL_PANEL_WIDTH;
    private int panelHeight = INITIAL_PANEL_HEIGHT;

    private Paddle paddle1;
    private Paddle paddle2;

    private Ball ball;

    private PaddleMover paddleMover;

    private Integer player1Score = 0, player2Score = 0;
    private int maxScore = 10;
    private int scoreFontSize = 50;
    private String scoreFont = "Andale Mono";
    private String winnerString = "The %s side wins!";  // String to be displayed when someone wins the game

    private Thread animationThread;

    private Color backgroundColor = Color.BLACK;
    private Color foregroundColor = Color.WHITE;

    boolean isPlayer1Turn = false;
    boolean shouldAlwaysRequestFocus = true;  // If application is unfocused, the user cannot move the paddles with the keyboard

    public TwoHumanPlayerPongCanvas() {

        initGUI();
        initCanvas();
        initPaddleMover();
    }

    /**
     * Initializes the Panel which houses the game.
     */

    private void initGUI() {

        setPreferredSize(new Dimension(this.panelWidth, this.panelHeight));
        setSize(this.panelWidth, this.panelHeight);
        setBackground(this.backgroundColor);
        setDoubleBuffered(true);
        setFocusable(true);
        addListeners();
    }

    /**
     * Initializes the ball and paddles.
     */

    private void initCanvas() {

        paddle1 = new Paddle(INITIAL_PADDLE1_X, INITIAL_PADDLE1_Y, INITIAL_PADDLE_WIDTH, INITIAL_PADDLE_HEIGHT, Paddle.LEFT);
        paddle2 = new Paddle(INITIAL_PADDLE2_X, INITIAL_PADDLE2_Y, INITIAL_PADDLE_WIDTH, INITIAL_PADDLE_HEIGHT, Paddle.RIGHT);

        ball = new Ball(this, INITIAL_BALL_X, INITIAL_BALL_Y, INITIAL_BALL_DIAMETER, INITIAL_X_TRAJECTORY, INITIAL_Y_TRAJECTORY);
    }

    /**
     * Initializes the ability to move the paddles with the keyboard.
     */

    private void initPaddleMover() {

        paddleMover = new PaddleMover(this);
        addKeyListener(paddleMover);
        Timer paddleTimer = new Timer(30, paddleMover);
        paddleTimer.start();
    }

    /**
     * Draws all items to the canvas.
     * @param g the graphics object to draw with.
     */

    private void drawCanvas(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(foregroundColor);

        g2d.fill(paddle1);
        g2d.fill(paddle2);
        g2d.fill(ball);

        g2d.setFont(new Font(scoreFont, Font.PLAIN, scoreFontSize));
        g2d.drawString(player1Score.toString(), panelWidth/4 - scoreFontSize/2, scoreFontSize);
        g2d.drawString(player2Score.toString(), panelWidth*3/4 - scoreFontSize/2, scoreFontSize);

        BasicStroke dotted = new BasicStroke(5, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 0, new float[] {40}, 0);  // BasicStroke(float width, int cap, int join, float miterlimit, float[] dash, float dash_phase)
        g2d.setStroke(dotted);

        g2d.drawLine(panelWidth/2, 0, panelWidth/2, panelHeight);


        if (player1Score == maxScore) {
            winnerString = String.format(winnerString, "left");
            int stringWidth = (int) g2d.getFontMetrics().getStringBounds(winnerString, g2d).getWidth();     // get winnerString width with specified font
            int stringHeight = (int) g2d.getFontMetrics().getStringBounds(winnerString, g2d).getHeight();   // get winnerString height with the specified font
            int xstart = panelWidth / 2 - stringWidth / 2;                                                  // center the string horizontally
            int ystart = panelHeight / 2 - stringHeight / 2;                                                // center the string vertically
            g2d.drawString(winnerString, xstart, ystart);

        } else if (player2Score == maxScore) {
            winnerString = String.format(winnerString, "right");
            int stringWidth = (int) g2d.getFontMetrics().getStringBounds(winnerString, g2d).getWidth();
            int stringHeight = (int) g2d.getFontMetrics().getStringBounds(winnerString, g2d).getHeight();
            int xstart = panelWidth / 2 - stringWidth / 2;
            int ystart = panelHeight / 2 - stringHeight / 2;
            g2d.drawString(winnerString, xstart, ystart);
        }

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

        addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent focusEvent) {
                if (shouldAlwaysRequestFocus) {
                    requestFocus();
                }
            }
        });
    }

    /**
     * Resets the game so that the player can start a new one.
     */
    @Override
    public void reset() {

        isPlayer1Turn = false;
        ball = new Ball(this, INITIAL_BALL_X, INITIAL_BALL_Y, INITIAL_BALL_DIAMETER, INITIAL_X_TRAJECTORY, INITIAL_Y_TRAJECTORY);
        if (player1Score == maxScore || player2Score == maxScore) {  // somehow, omitting this causes the ball to go faster and faster (linked to thread not stopped yet?)
            animationThread = new Thread(new BallAnimation(this));
            animationThread.start();
        }
        player1Score = 0;
        player2Score = 0;
    }

//    void animationCycle() {
//
//        ball.checkCollisions();
//        ball.move();
//    }

    @Override
    public void addNotify() {

        super.addNotify();
        animationThread = new Thread(new BallAnimation(this));
        animationThread.start();
    }

    @Override
    public void paintComponent(Graphics g) {

        Toolkit.getDefaultToolkit().sync();
        super.paintComponent(g);
        drawCanvas(g);
    }

    void increasePlayerScore(int playerNumber) {

        if (playerNumber == 1) {
            player1Score++;
        } else {
            player2Score++;
        }
    }

    @Override
    public int getPanelWidth() { return panelWidth; }

    @Override
    public int getPanelHeight() { return panelHeight; }

    int getPlayerScore(int player) { return player == 1 ? player1Score : player2Score; }

    int getMaxScore() { return maxScore; }

    public Paddle getPaddle1() { return paddle1; }

    public Paddle getPaddle2() { return paddle2; }

    @Override
    public Ball getBall() { return ball; }

    @Override
    public Thread getAnimationThread() { return animationThread; }

    @Override
    public Color getForegroundColor() { return foregroundColor; }

    @Override
    public Color getBackgroundColor() { return backgroundColor; }

    @Override
    public void setForegroundColor(Color color) { foregroundColor = color; }

    @Override
    public int getNumberOfPaddles() { return 2; }

    @Override
    public void setShouldAlwaysRequestFocus(boolean focus) { shouldAlwaysRequestFocus = focus; }

    /**
     * Cleans up before quitting the game.
     */

    @Override
    public void deinit() {

        ball.deinit();
    }
}