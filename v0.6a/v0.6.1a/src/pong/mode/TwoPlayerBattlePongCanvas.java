// File: TwoPlayerBattlePongCanvas.java
// Author: Leo
// Date Created: 4/12/15 3:23 PM

package pong.mode;

import pong.collision.PongMissileCollisionDetector;
import pong.paddle.PaddleMode;
import pong.paddle.Paddle;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Vector;

public class TwoPlayerBattlePongCanvas extends TwoPlayerPongCanvas {

    private Vector<PongMissileCollisionDetector> pongMissileCollisionDetectors = new Vector<PongMissileCollisionDetector>();
    private Color missileColor = Color.RED;

    public TwoPlayerBattlePongCanvas() {

        addMissileCollisionDetectors();
    }

    @Override
    public PongMode getMode() {

        return PongMode.DUAL_PLAYER_HUMAN_VS_HUMAN_BATTLE;
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

        for (int i = 0; i < getPaddles().length; i++) {
            pongMissileCollisionDetectors.add(new PongMissileCollisionDetector(getPaddles()[i].getMissile(), getPaddles()[(i + 1) % getPaddles().length]));
        }
    }

    public Vector<PongMissileCollisionDetector> getPongMissileCollisionDetectors() {

        return pongMissileCollisionDetectors;
    }

    public void setMissileColor(Color missileColor) {

        this.missileColor = missileColor;
    }

    @Override
    void drawCustomItems(Graphics2D g2d) {

        for (Paddle paddle : getPaddles()) {
            if (paddle.hasFiredMissile()) {
                g2d.setPaint(missileColor);
                g2d.fill(paddle.getMissile());
            }
        }
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
}