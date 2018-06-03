// File: TrainingPongCanvas.java
// Author: Leo
// Date Created: 4/9/15 4:35 PM
// Version: 0.1a

package pong.mode;

import pong.collision.PongPaddleCollisionDetector;
import pong.paddle.Paddle;
import pong.paddle.PaddleMode;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class SinglePlayerTrainingPongCanvas extends PongCanvas {

    Paddle paddle, wall;

    @Override
    public int getNumberOfPaddles() {

        return 1;
    }

    @Override
    public Paddle[] getPaddles() {

        return new Paddle[]{paddle, wall};
    }

    @Override
    void initPaddles() {

        paddle = new Paddle(this, INITIAL_PADDLE1_X, INITIAL_PADDLE1_Y, INITIAL_PADDLE_WIDTH, INITIAL_PADDLE_HEIGHT, Paddle.LEFT, PaddleMode.HUMAN);
        wall = new Paddle(this, getPanelWidth() - 7, 2, 5, getPanelHeight() - 4, Paddle.RIGHT, PaddleMode.NONE);

        paddle.getPlayerModeTimer(KeyEvent.VK_UP, KeyEvent.VK_DOWN).start();
    }

    @Override
    void drawCanvas(Graphics g) {

        super.drawCanvas(g);
        getGraphics2D().setColor(getForegroundColor());
        getGraphics2D().setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        getGraphics2D().fill(paddle);
        getGraphics2D().fill(wall);
    }

    @Override
    void addListeners() {

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {

                paddle.setPaddleY(e.getY());
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

        for (Paddle paddle : getPaddles()) {
            pongPaddleCollisionDetectors.add(new PongPaddleCollisionDetector(this, paddle));
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

        return Integer.MAX_VALUE;  // no score, so approximate positive infinity with MAX_VALUE
    }
}