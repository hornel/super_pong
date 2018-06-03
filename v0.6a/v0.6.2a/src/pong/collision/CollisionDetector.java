// File: CollisionDetector.java
// Author: Leo
// Date Created: 5/9/15 12:48 PM

package pong.collision;

/**
 * Interface qui definit un detecteur de collisions.
 */
public interface CollisionDetector {

    /**
     * Methode que sert a verifier si une collision a eu lieu et a changer les parametres necessaires apres une collision.
     */
    void checkCollisions();
}
