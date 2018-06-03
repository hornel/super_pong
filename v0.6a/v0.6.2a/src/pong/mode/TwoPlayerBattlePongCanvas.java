// File: TwoPlayerBattlePongCanvas.java
// Author: Leo
// Date Created: 4/12/15 3:23 PM

package pong.mode;

import pong.animation.MissileMover;
import pong.collision.PongMissileCollisionDetector;
import pong.missile.PongMissile;
import pong.paddle.BattlePaddle;
import pong.paddle.Paddle;
import pong.paddle.PaddleMode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

public class TwoPlayerBattlePongCanvas extends TwoPlayerPongCanvas {

    private Vector<PongMissileCollisionDetector> pongMissileCollisionDetectors = new Vector<PongMissileCollisionDetector>();
    private Color missileColor = Color.RED;
    private Timer paddle1Timer;
    private Timer paddle2Timer;
    private Timer missile1Timer;
    private Timer missile2Timer;
    private BattlePaddle battlePaddle1;
    private BattlePaddle battlePaddle2;
    private final static int TIMER_DELAY = 20;
    private boolean paused = false;

    public TwoPlayerBattlePongCanvas() {

        addMissileCollisionDetectors();
    }

    @Override
    public PongMode getMode() {

        return PongMode.DUAL_PLAYER_HUMAN_VS_HUMAN_BATTLE;
    }

    @Override
    void initPaddles() {

        battlePaddle1 = new BattlePaddle(this, INITIAL_PADDLE1_X, INITIAL_PADDLE1_Y, INITIAL_PADDLE_WIDTH, INITIAL_PADDLE_HEIGHT, Paddle.LEFT, PaddleMode.HUMAN);
        battlePaddle2 = new BattlePaddle(this, INITIAL_PADDLE2_X, INITIAL_PADDLE2_Y, INITIAL_PADDLE_WIDTH, INITIAL_PADDLE_HEIGHT, Paddle.RIGHT, PaddleMode.HUMAN);

        missile1Timer = new Timer(TIMER_DELAY, new MissileMover(this, battlePaddle1));
        missile2Timer = new Timer(TIMER_DELAY, new MissileMover(this, battlePaddle2));

        paddle1 = battlePaddle1;
        paddle2 = battlePaddle2;

        paddle1Timer = battlePaddle1.getPlayerModeTimer(KeyEvent.VK_W, KeyEvent.VK_S);
        paddle2Timer = battlePaddle2.getPlayerModeTimer(KeyEvent.VK_UP, KeyEvent.VK_DOWN);
        paddle1Timer.start();
        paddle2Timer.start();
    }

    public Timer getTimerForMissile(PongMissile missile) {

        if (missile.equals(battlePaddle1.getMissile())) {
            return missile1Timer;
        } else if (missile.equals(battlePaddle2.getMissile())) {
            return missile2Timer;
        } else {
            return null;
        }
    }

    @Override
    void addListeners() {

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {

                if (e.getKeyCode() == KeyEvent.VK_D && !paused) {
                    battlePaddle1.tryToPrepareNewMissile();
                    if (!missile1Timer.isRunning()) {
                        missile1Timer.start();
                    }
                }

                if (e.getKeyCode() == KeyEvent.VK_LEFT && !paused) {
                    battlePaddle2.tryToPrepareNewMissile();
                    if (!missile2Timer.isRunning()) {
                        missile2Timer.start();
                    }
                }
            }
        });
    }

    @Override
    public void end() {

        super.end();
        paddle1Timer.stop();
        paddle2Timer.stop();
        missile1Timer.stop();
        missile2Timer.stop();
    }

    @Override
    public void pause() {

        super.pause();
        missile1Timer.stop();
        missile2Timer.stop();
        paused = true;
    }

    @Override
    public void resume() {

        super.resume();
        paused = false;
        if (battlePaddle1.hasFiredMissile()) {
            missile1Timer.start();
        }
        if (battlePaddle2.hasFiredMissile()) {
            missile2Timer.start();
        }
    }

    private void addMissileCollisionDetectors() {

        pongMissileCollisionDetectors.add(new PongMissileCollisionDetector(this, battlePaddle1.getMissile(), battlePaddle2));
        pongMissileCollisionDetectors.add(new PongMissileCollisionDetector(this, battlePaddle2.getMissile(), battlePaddle1));
    }

    public Vector<PongMissileCollisionDetector> getPongMissileCollisionDetectors() {

        return pongMissileCollisionDetectors;
    }

    public void setMissileColor(Color missileColor) {

        this.missileColor = missileColor;
    }

    public BattlePaddle[] getBattlePaddles() {

        return new BattlePaddle[] {battlePaddle1, battlePaddle2};
    }

    @Override
    void drawCustomItems(Graphics2D g2d) {

        for (BattlePaddle paddle : getBattlePaddles()) {
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
            battlePaddle2.undestroy();
        } else {
            battlePaddle1.undestroy();
        }
    }

    public Color getMissileColor() {

        return missileColor;
    }
}