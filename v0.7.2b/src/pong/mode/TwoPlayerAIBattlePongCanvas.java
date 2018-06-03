package pong.mode;

import pong.paddle.*;

import javax.swing.*;
import java.awt.event.KeyEvent;

/**
 * Classe decrivant le mode bataille avec IA.
 */
public class TwoPlayerAIBattlePongCanvas extends TwoPlayerBattlePongCanvas implements AIPongCanvas {

    @Override
    protected void initBattlePaddles() {

        // on initialise les raquettes
        setBattlePaddle1(new BattlePaddle(this, INITIAL_PADDLE1_X, INITIAL_PADDLE1_Y, INITIAL_PADDLE_WIDTH, INITIAL_PADDLE_HEIGHT, Paddle.LEFT, PaddleMode.HUMAN));
        setBattlePaddle2(new BattlePaddle(this, INITIAL_PADDLE2_X, INITIAL_PADDLE2_Y, INITIAL_PADDLE_WIDTH, INITIAL_PADDLE_HEIGHT, Paddle.RIGHT, PaddleMode.BATTLE_AI));

        // On vuet que le joueur de gauche puisse lancer des missiles avec la fleche droite
        setPlayer1MissileKey(KeyEvent.VK_RIGHT);

        // On initialise les Timers de deplacement des raquettes
        setPaddle1Timer(getBattlePaddle1().getPlayerModeTimer(KeyEvent.VK_UP, KeyEvent.VK_DOWN));
        setPaddle2Timer(getBattlePaddle2().getPlayerModeTimer(KeyEvent.VK_W, KeyEvent.VK_S, getBattlePaddle1()));

        // On les demarre
        getPaddle1Timer().start();
        getPaddle2Timer().start();
    }

    @Override
    public PaddleAI getAI() {

        // voir la classe TwoHumanAndAIPlayerPongCanvas
        return (PaddleAI) getPaddle2Timer().getActionListeners()[0];
    }

    @Override
    public PongMode getMode() {

        return PongMode.DUAL_PLAYER_HUMAN_VS_AI_BATTLE;
    }
}