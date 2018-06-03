// File: PongMissileCollisionDetector.java
// Author: Leo
// Date Created: 4/14/15 5:09 PM

package pong.collision;

import pong.missile.PongMissile;
import pong.paddle.Paddle;

public class PongMissileCollisionDetector {

    private PongMissile missile;
    private Paddle enemyPaddle;

    //private PongSoundPlayer paddleHitSound = new PongSoundPlayer("paddleHit.wav");  // sound to be played when paddle is hit

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

    public void checkCollisions() {

        if (missileHitPaddleOuterEdge() && missileWithinUpperAndLowerLimits()) {
            missile.getParentPaddle().resetMissile();
            System.out.println("                       HIT!!!!!!!!!!!!!!!!!!!");
            enemyPaddle.destroy();
            missile.getParentPaddle().getMissileAnimation().setPaused(true);
        }

        else if (missile.missed()){
            System.err.println("                       MISSED");
            missile.getParentPaddle().resetMissile();
            missile.getParentPaddle().getMissileAnimation().setPaused(true);
        }
    }
}