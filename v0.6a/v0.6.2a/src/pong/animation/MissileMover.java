// File: MissileAnimation.java
// Author: Leo
// Date Created: 4/16/15 8:25 PM

package pong.animation;

import pong.collision.PongMissileCollisionDetector;
import pong.mode.TwoPlayerBattlePongCanvas;
import pong.paddle.BattlePaddle;
import pong.paddle.Paddle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Classe qui servira a deplacer les missiles dans un {@code TwoPlayerBattlePongCanvas}. C'est une implementation d'
 * {@code ActionListener} car elle doit etre appele par un {@code javax.swing.Timer}.
 */
public class MissileMover implements ActionListener {

    /**
     * Le {@code TwoPlayerBattlePongCanvas} dont le missile doit etre deplace.
     */
    private TwoPlayerBattlePongCanvas pongCanvas;

    /**
     * Le {@code BattlePaddle} qui est le parent du missile qui est en train d'etre deplace.
     */
    private BattlePaddle paddle;


    /**
     * Constructeur servant a passer un TwoPlayerBattlePongCanvas et un BattlePaddle (parent du missile
     * qui est en train d'etre deplace) a la classe.
     */
    public MissileMover(TwoPlayerBattlePongCanvas pongCanvas, BattlePaddle paddle) {

        this.pongCanvas = pongCanvas;
        this.paddle = paddle;
    }


    // Cette methode sera appelee par le javax.swing.Timer periodiquement. Elle permet de deplacer le missile et verifier
    // s'il y a eu une collision.
    @Override
    public void actionPerformed(ActionEvent e) {

        for (PongMissileCollisionDetector pongMissileCollisionDetector : pongCanvas.getPongMissileCollisionDetectors()) {
            pongMissileCollisionDetector.checkCollisions();
        }

        // on obtient le rectangle ou le missile se trouve avant d'etre deplace pour redessiner cette region plus tard
        Rectangle oldBounds = paddle.getMissile().getBounds();
        paddle.moveMissile(pongCanvas.getBall().getSpeed() * 25);

        // Il faut redessiner ou le missile etait et ou il s'est deplace
        pongCanvas.repaint(oldBounds);
        pongCanvas.repaint(paddle.getMissile().getBounds());
    }
}