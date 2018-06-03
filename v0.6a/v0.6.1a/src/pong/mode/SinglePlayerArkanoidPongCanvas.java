// File: SinglePlayerArkanoidPongCanvas.java
// Author: Leo
// Date Created: 4/12/15 3:21 PM

package pong.mode;

import pong.collision.PongPaddleCollisionDetector;
import pong.collision.PongWallCollisionDetector;
import pong.paddle.Paddle;
import pong.paddle.PaddleMode;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class SinglePlayerArkanoidPongCanvas extends PongCanvas {

    private int rows, cols;
    private Paddle playerPaddle;
    private Paddle[][] bricks;
    private int interSpace;
    private int brickHeight;
    private int initialX;
    private Color brickColor;

    @Override
    public Paddle[] getPaddles() {

        Paddle[] paddles = new Paddle[rows * cols + 1];

        int currentPosition = 0;

        for (Paddle[] brick : bricks) {

            System.arraycopy(brick, 0, paddles, currentPosition, brick.length);
            currentPosition += brick.length;
        }

        paddles[paddles.length - 1] = playerPaddle;

        return paddles;
    }

    @Override
    void initPaddles() {

        rows = 10;
        cols = 5;
        interSpace = 5;
        brickHeight = (INITIAL_PANEL_HEIGHT - ((rows + 1) * interSpace)) / rows;
        initialX = INITIAL_PANEL_WIDTH - cols * interSpace - cols * INITIAL_PADDLE_WIDTH;

        playerPaddle = new Paddle(this, INITIAL_PADDLE1_X, INITIAL_PADDLE1_Y, INITIAL_PADDLE_WIDTH, INITIAL_PADDLE_HEIGHT, Paddle.LEFT, PaddleMode.HUMAN);
        bricks = new Paddle[rows][cols];
        brickColor = Color.RED;

        playerPaddle.getPlayerModeTimer(KeyEvent.VK_UP, KeyEvent.VK_DOWN).start();

        int currentY = interSpace;

        for (int row = 0; row < bricks.length; row++) {
            int currentX = initialX;

            for (int col = 0; col < bricks[0].length; col++) {
                bricks[row][col] = new Paddle(this, currentX, currentY, INITIAL_PADDLE_WIDTH, brickHeight, Paddle.RIGHT, PaddleMode.NONE);
                currentX += INITIAL_PADDLE_WIDTH + interSpace;
            }
            currentY += brickHeight + interSpace;

        }
    }

    @Override
    void drawCanvas(Graphics g) {

        super.drawCanvas(g);
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
        g2d.setColor(getForegroundColor());
        g2d.fill(playerPaddle);
        g2d.setColor(brickColor);

        for (int row = 0; row < bricks.length; row++) {
            for (int col = 0; col < bricks[0].length; col++) {
                g2d.fill(bricks[row][col]);
            }
        }

        g2d.dispose();
    }

    @Override
    void addListeners() {

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {

                playerPaddle.setPaddleY(e.getY());
            }
        });
    }

    @Override
    public PongMode getMode() {

        return PongMode.SINGLE_PLAYER_ARKANOID;
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

            pongPaddleCollisionDetectors.add(new PongPaddleCollisionDetector(this, paddle) {
                @Override
                public void performCustomAction() {
                    if (!paddle.getPaddleMode().equals(PaddleMode.HUMAN)) {
                        paddle.destroy();
                        repaint(paddle.getBounds());
                    }
                }
            });
        }
    }

    @Override
    public void increasePlayerScore(int player) {

    }

    @Override
    public int getPlayerScore(int player) {

        return 0;
    }

    @Override
    public int getMaxScore() {

        return 10;
    }
}