// File: PongMissileCollisionDetector.java
// Author: Leo
// Date Created: 4/14/15 5:09 PM

package pong.collision;

import pong.missile.PongMissile;
import pong.mode.TwoPlayerBattlePongCanvas;
import pong.paddle.BattlePaddle;
import pong.sound.PongSoundPlayer;

import java.awt.geom.Area;

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
    private PongSoundPlayer paddleHitSound = new PongSoundPlayer("missileHit.wav");  // sound to be played when paddle is hit

    public PongMissileCollisionDetector(TwoPlayerBattlePongCanvas pongCanvas, PongMissile missile, BattlePaddle enemyPaddle) {

        this.pongCanvas = pongCanvas;
        this.enemyPaddle = enemyPaddle;
        this.missile = missile;
    }

    @Override
    public void checkCollisions() {

        // il faut verifier les collisions seulement si le missile est pres de la raquette pour econimiser les ressources
        if (Math.abs(missile.getFront() - enemyPaddle.getOuterLimit()) <= missile.getSpeed()) {

            // Pour verifier s'il y a eu une collision, on cree deux Area avec le missile et la raquette et on les
            // intersecte. Si l'intersection des deux Area est nulle, il n'y a pas eu de collision, et si elle n'est pas
            // nulle, il y a eu une collision.
            Area missileArea = new Area(missile);
            missileArea.intersect(new Area(enemyPaddle));

            // S'il y eu une collision, il faut:
            // 1. Remettre les coordonnees du missile a leurs valeurs originelles
            // 2. Dire au BattlePaddle qu'il peut maintenant lancer un autre missile
            // 3. Joueur le son qui doit etre joue lorsqu'une raquette est touchee
            // 4. Arreter le Timer d'animation du missile
            // 5. Redessiner la ou le missile etait lors de la collision
            if (!missileArea.isEmpty()) {
                missile.reset();
                missile.getParentPaddle().setHasFiredMissile(false);
                paddleHitSound.playSound();
                enemyPaddle.destroy();
                pongCanvas.getTimerForMissile(missile).stop();
                pongCanvas.repaint(missile.getBounds());
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