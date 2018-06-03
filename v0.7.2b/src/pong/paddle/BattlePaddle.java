package pong.paddle;

import pong.missile.PongMissile;
import pong.mode.TwoPlayerBattlePongCanvas;
import pong.sound.PongSoundPlayer;
import pong.sound.Sounds;

import javax.swing.*;
import java.awt.*;

/**
 * Paddle qui peut lancer des missiles et etre detruite temporairement.
 */
public class BattlePaddle extends Paddle {

    /**
     * Decrit si la raquette a lance un missile.
     */
    private boolean hasFiredMissile = false;

    /**
     * Decrit si la raquette peut lancer un missile.
     */
    private boolean canFireMissile = true;

    /**
     * Son qui doit etre joue lorsque la raquette lance un missile.
     */
    private PongSoundPlayer missileFiredSound;

    /**
     * Decrit si la raquette a ete detruite (temporairement).
     */
    private boolean destroyed = false;

    /**
     * Le missile de la raquette.
     */
    private PongMissile missile;

    /**
     * Valeur devrivant la distance entre le bord du jeule plus pres de la raquette et la limite exterieure de
     * la raquette.
     */
    private double oldPaddleOffset;

    /**
     * Le TwoPlayerBattlePongCanvas ou la raquette lancera des missiles.
     */
    private TwoPlayerBattlePongCanvas battlePongCanvas;

    public BattlePaddle(TwoPlayerBattlePongCanvas battlePongCanvas, double paddleX, double paddleY, double paddleWidth, double paddleHeight, int side, PaddleMode paddleMode) {

        super(battlePongCanvas, paddleX, paddleY, paddleWidth, paddleHeight, side, paddleMode);
        this.battlePongCanvas = battlePongCanvas;
        this.missile = new PongMissile(this, 20, 10);
        this.missileFiredSound = Sounds.getMissileFiredSound();
    }

    /**
     * La raquette essaye de preparer un nouveau missile. Si elle a deja lance un missile qui se trouve actuellement
     * sur l'ecran, cette methode ne fera rien.
     */
    public void tryToPrepareNewMissile() {

        if (!hasFiredMissile && !destroyed && canFireMissile) {
            missile.reset();
            missile.setMissileY(getCenterY());
            hasFiredMissile = true;
            missileFiredSound.playSound();
        }
    }

    /**
     * Cherche le missile de la raquette.
     */
    public PongMissile getMissile() {

        return missile;
    }

    /**
     * Decrit si la raquette a lance un missile qui se trouve toujours dans le jeu.
     */
    public boolean hasFiredMissile() {

        return hasFiredMissile;
    }

    /**
     * Change la valeur de hasFiredMissile.
     */
    public void setHasFiredMissile(boolean hasFiredMissile) {

        this.hasFiredMissile = hasFiredMissile;
    }

    /**
     * Deplace le missile selon une vitesse donnee si un missile a ete lance.
     */
    public void moveMissile(double speed) {

        if (hasFiredMissile) {
            missile.move(speed);
        }
    }

    /**
     * Detruit temporairement la raquette.
     */
    public void destroy() {

        canFireMissile = false;
        destroyed = true;
        // il faudra redessiner ou la raquette etait.
        Rectangle oldBounds = getBounds();
        // la raquette ne doit plus etre deplacable.
        setMoveable(false);
        // on enregistre la position de la raquette.
        oldPaddleOffset = getSide() == LEFT ? getX() : battlePongCanvas.getWidth() - getX();
        // on deplace la raquette a un endroit en-dehors de l'ecran
        setPaddleX(-1000);
        battlePongCanvas.repaint(oldBounds);
    }

    /**
     * "Revitalise" la raquette apres qu'elle a ete detruite.
     */
    public void undestroy() {

        // si la raquette et detruite, on fait l'inverse de la methode destroy(), c-a-d on remet la raquette a
        // sa hauteur et position initiale.
        if (destroyed) {

            destroyed = false;
            Rectangle oldBounds = getBounds();
            setPaddleX(getSide() == LEFT ? oldPaddleOffset : battlePongCanvas.getWidth() - oldPaddleOffset);
            setMoveable(true);
            canFireMissile = true;
            battlePongCanvas.repaint(oldBounds);
            battlePongCanvas.repaint(getBounds());
        }
    }

    public Timer getPlayerModeTimer(int upKey, int downKey, BattlePaddle enemyPaddle) {

        if (getPaddleMode() == PaddleMode.HUMAN) {
            // permet a l'utilisateur de deplacer la raquette en utilisant les touches
            // upKey et downKey (qui sont definies par une constante dans java.awt.KeyEvent)
            PaddleMover paddleMover = new PaddleMover(this, upKey, downKey, getPongCanvas());
            paddleMover.addKeyBindings();
            return new Timer(PaddleMover.TIMER_DELAY, paddleMover);

        } else if (getPaddleMode() == PaddleMode.BATTLE_AI) {
            // permet a l'IA de deplacer sa raquette.
            BattlePaddleAI paddleAI = new BattlePaddleAI(getPongCanvas(), this, enemyPaddle, getPongCanvas().getBall());
            return new Timer(PaddleMover.TIMER_DELAY, paddleAI);

        } else {
            // si la raquette n'est controlee ni par un humain ni par l'IA
            return null;
        }
    }

    /**
     * Decrit si la raquette est temporairement detruite.
     */
    public boolean isDestroyed() {

        return destroyed;
    }
}