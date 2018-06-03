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

    private int rows = 5, cols = 3;
    private Paddle playerPaddle;
    private Paddle[][] bricks;
    private int interSpace = 3;
    private int brickHeight = (INITIAL_PANEL_HEIGHT - ((cols + 1) * interSpace)) / cols;
    private int initialX = INITIAL_PANEL_WIDTH - cols * interSpace;

    @Override
    public int getNumberOfPaddles() {

        return 1 + bricks.length;
    }

    @Override
    public Paddle[] getPaddles() {

        Paddle[] paddles = new Paddle[rows*cols + 1];

        int currentPosition = 0;

        for (int row = 0; row < bricks.length; row++) {

            System.arraycopy(bricks[row], 0, paddles, currentPosition, bricks[row].length);
            currentPosition += bricks[row].length;
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
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(getForegroundColor());
        g2d.fill(playerPaddle);
        g2d.setColor(Color.RED);

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

        return null;
    }

    @Override
    public boolean isPlayer1Turn() {

        return false;
    }

    @Override
    public void setPlayer1Turn(boolean player) {

    }

    @Override
    void addCollisionDetectors() {

        pongWallCollisionDetector = new PongWallCollisionDetector(this);

        for (final Paddle paddle : getPaddles()) {

            pongPaddleCollisionDetectors.add(new PongPaddleCollisionDetector(this, paddle) {
                @Override
                public void performCustomAction() {
                    if (!paddle.getPaddleMode().equals(PaddleMode.HUMAN)) {
                        paddle.destroy();
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