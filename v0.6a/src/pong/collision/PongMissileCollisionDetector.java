// File: PongMissileCollisionDetector.java
// Author: Leo
// Date Created: 4/14/15 5:09 PM

package pong.collision;

import pong.missile.PongMissile;
import pong.paddle.Paddle;
import pong.sound.PongSoundPlayer;

public class PongMissileCollisionDetector implements CollisionDetector {

    private PongMissile missile;
    private Paddle enemyPaddle;

    private PongSoundPlayer paddleHitSound = new PongSoundPlayer("missileHit.wav");  // sound to be played when paddle is hit

    public PongMissileCollisionDetector(PongMissile missile, Paddle enemyPaddle) {

        this.enemyPaddle = enemyPaddle;
        this.missile = missile;
    }

    public boolean missileHitPaddleOuterEdge() {

        if (enemyPaddle.getSide() == Paddle.LEFT) {
            return missile.getFront() <= enemyPaddle.getOuterLimit();
        } else {
            return missile.getFront() >= enemyPaddle.getOuterLimit();
        }
    }

    public boolean missileWithinUpperAndLowerLimits() {

        return missile.getBottom() >= enemyPaddle.getUpperLimit() && missile.getTop() <= enemyPaddle.getLowerLimit();
    }

    @Override
    public void checkCollisions() {

        if (missileHitPaddleOuterEdge() && missileWithinUpperAndLowerLimits()) {  // missile hit
            missile.getParentPaddle().resetMissile();
            paddleHitSound.playSound();
            enemyPaddle.destroy();
            missile.getParentPaddle().getMissileAnimation().setPaused(true);

        } else if (missile.missed()) {  // missile missed
            missile.getParentPaddle().resetMissile();
            missile.getParentPaddle().getMissileAnimation().setPaused(true);
        }
    }
}