// File: TwoPlayerBattlePongCanvas.java
// Author: Leo
// Date Created: 4/12/15 3:23 PM

package pong.mode;

import pong.paddle.BattlePaddle;
import pong.paddle.Paddle;
import pong.paddle.PaddleMode;

import java.awt.event.KeyEvent;

/**
 * Classe decrivant le mode bataille avec deux joueurs humains
 */
public class TwoHumanPlayerBattlePongCanvas extends TwoPlayerBattlePongCanvas {

    @Override
    protected void initBattlePaddles() {

        // On initialise les raquettes
        setBattlePaddle1(new BattlePaddle(this, INITIAL_PADDLE1_X, INITIAL_PADDLE1_Y, INITIAL_PADDLE_WIDTH, INITIAL_PADDLE_HEIGHT, Paddle.LEFT, PaddleMode.HUMAN));
        setBattlePaddle2(new BattlePaddle(this, INITIAL_PADDLE2_X, INITIAL_PADDLE2_Y, INITIAL_PADDLE_WIDTH, INITIAL_PADDLE_HEIGHT, Paddle.RIGHT, PaddleMode.HUMAN));

        // On initialise les Timers pour pouvoir deplacer les raquettes avec les touches
        setPaddle1Timer(getBattlePaddle1().getPlayerModeTimer(KeyEvent.VK_W, KeyEvent.VK_S));
        setPaddle2Timer(getBattlePaddle2().getPlayerModeTimer(KeyEvent.VK_UP, KeyEvent.VK_DOWN));

        // On demarre les Timers
        getPaddle1Timer().start();
        getPaddle2Timer().start();
    }

    @Override
    public PongMode getMode() {

        return PongMode.DUAL_PLAYER_HUMAN_VS_HUMAN_BATTLE;
    }
}