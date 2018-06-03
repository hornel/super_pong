// File: TwoPlayerBattlePongCanvas.java
// Author: Leo
// Date Created: 4/12/15 3:23 PM

package pong.mode;

import pong.collision.PongMissileCollisionDetector;
import pong.paddle.Paddle;
import pong.paddle.PaddleMode;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;

public class TwoPlayerBattlePongCanvas extends TwoPlayerPongCanvas {

    private ArrayList<PongMissileCollisionDetector> pongMissileCollisionDetectors = new ArrayList<PongMissileCollisionDetector>();

    public TwoPlayerBattlePongCanvas() {

        addMissileCollisionDetectors();
    }

    @Override
    void initPaddles() {

        paddle1 = new Paddle(this, INITIAL_PADDLE1_X, INITIAL_PADDLE1_Y, INITIAL_PADDLE_WIDTH, INITIAL_PADDLE_HEIGHT, Paddle.LEFT, PaddleMode.HUMAN);
        paddle2 = new Paddle(this, INITIAL_PADDLE2_X, INITIAL_PADDLE2_Y, INITIAL_PADDLE_WIDTH, INITIAL_PADDLE_HEIGHT, Paddle.RIGHT, PaddleMode.HUMAN);

        paddle1.getPlayerModeTimer(KeyEvent.VK_W, KeyEvent.VK_S).start();
        paddle2.getPlayerModeTimer(KeyEvent.VK_UP, KeyEvent.VK_DOWN).start();
    }

    @Override
    void addListeners() {

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

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {

                if (e.getKeyCode() == KeyEvent.VK_D) {
                    paddle1.prepareNewMissile();
                    if (!paddle1.getMissileAnimation().threadIsStarted()) {
                        paddle1.getMissileAnimation().start();
                    }
                    paddle1.getMissileAnimation().setPaused(false);
                }

                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    paddle2.prepareNewMissile();
                    if (!paddle2.getMissileAnimation().threadIsStarted()) {
                        paddle2.getMissileAnimation().start();
                    }
                    paddle2.getMissileAnimation().setPaused(false);
                }
            }
        });
    }

    private void addMissileCollisionDetectors() {

        for (int i = 0; i < getPaddles().length; i++){
            pongMissileCollisionDetectors.add(new PongMissileCollisionDetector(getPaddles()[i].getMissile(), getPaddles()[(i+1) % getPaddles().length]));
        }
    }

    public ArrayList<PongMissileCollisionDetector> getPongMissileCollisionDetectors() {

        return pongMissileCollisionDetectors;
    }

    @Override
    public void increasePlayerScore(int playerNumber) {

        super.increasePlayerScore(playerNumber);

        if (playerNumber == 1) {
            paddle2.undestroy();
        } else {
            paddle1.undestroy();
        }
    }

    @Override
    void drawCanvas(Graphics g) {

        super.drawCanvas(g);
        Graphics2D g2d = (Graphics2D) g;

        for (Paddle paddle : getPaddles()) {
            if (paddle.hasFiredMissile()) {
                g2d.setColor(Color.RED);
                g2d.fill(paddle.getMissile());
            }
        }
    }
}