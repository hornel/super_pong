// File: PongMissileCollisionDetector.java
// Author: Leo
// Date Created: 4/14/15 5:09 PM

package pong.collision;

import pong.missile.PongMissile;
import pong.mode.TwoPlayerBattlePongCanvas;
import pong.paddle.BattlePaddle;
import pong.paddle.Paddle;
import pong.sound.PongSoundPlayer;
import pong.sound.Sounds;

import java.awt.*;

/**
 * Classe qui sert a detecter les collisions entre un missile et une raquette.
 */
public class PongMissileCollisionDetector implements CollisionDetector {

    /**
     * TwoPlayerBattlePongCanvas ou il faut detecter les collisions.
     */
    private TwoPlayerBattlePongCanvas pongCanvas;

    /**
     * Missile pour lequel il faut detecter les collisions.
     */
    private PongMissile missile;

    /**
     * BattlePaddle pour lequel il faut detecter les collisions.
     */
    private BattlePaddle enemyPaddle;

    /**
     * Son qui doit etre joue lorsque la raquette est touchee.
     */
    private PongSoundPlayer paddleHitSound = Sounds.getMissileHitSound();

    public PongMissileCollisionDetector(TwoPlayerBattlePongCanvas pongCanvas, PongMissile missile, BattlePaddle enemyPaddle) {

        this.pongCanvas = pongCanvas;
        this.enemyPaddle = enemyPaddle;
        this.missile = missile;
    }

    /**
     * Decrit si le missile est derriere la limite exterieure de la raquette. Il faut que:
     * - la limite devant du missile soit <= la limite exterieure de la raquette si la raquette se trouve a gauce
     *   ou la limite devant du missile soit >= la limite exterieure de la raquette si la raquette se trouve a droite.
     */
    public boolean missileBehindPaddle() {

        return enemyPaddle.getSide() == Paddle.LEFT ? missile.getFront() <= enemyPaddle.getOuterLimit() : missile.getFront() >= enemyPaddle.getOuterLimit();
    }

    /**
     * Decrit si le missile est en face de la raquette. Il faut que:
     * - La limite superieure du missile soit >= la limite superieure de la raquette
     * - La limite inferieure du missile soit <= la limite inferieure de la raquette
     */
    public boolean missileWithinPaddleLimits() {

        return missile.getTop() >= enemyPaddle.getUpperLimit() && missile.getBottom() <= enemyPaddle.getLowerLimit();
    }

    @Override
    public void checkCollisions() {

        // il faut verifier les collisions seulement si le missile est pres de la raquette pour econimiser les ressources
        if (Math.abs(missile.getFront() - enemyPaddle.getOuterLimit()) <= missile.getSpeed()) {

            // Definition d'une collision:
            // 1. Le missile a depasse la limite exterieure de la raquette
            // 2. Le missile est en face de la raquette.

            // S'il y eu une collision, il faut:
            // 1. Remettre les coordonnees du missile a leurs valeurs originelles
            // 2. Dire au BattlePaddle qu'il peut maintenant lancer un autre missile
            // 3. Joueur le son qui doit etre joue lorsqu'une raquette est touchee
            // 4. Arreter le Timer d'animation du missile
            // 5. Redessiner la ou le missile etait lors de la collision
            if (missileBehindPaddle() && missileWithinPaddleLimits()) {
                // il va falloir redessiner ou le missile etait
                Rectangle oldMissileBounds = missile.getBounds();
                missile.reset();
                missile.getParentPaddle().setHasFiredMissile(false);
                paddleHitSound.playSound();
                enemyPaddle.destroy();
                pongCanvas.getTimerForMissile(missile).stop();
                pongCanvas.repaint(oldMissileBounds);
            }

        // Si le missile n'a pas touche une raquette, il faut:
        // 1. Remettre les coordonnees du missile a leurs valeurs originelles
        // 2. Dire au BattlePaddle qu'il peut maintenant lancer un autre missile
        // 3. Arreter le Timer d'animation du missile
        } else if (missile.getBack() > pongCanvas.getWidth() || missile.getBack() < 0) {

            missile.reset();
            missile.getParentPaddle().setHasFiredMissile(false);
            pongCanvas.getTimerForMissile(missile).stop();
        }
    }
}