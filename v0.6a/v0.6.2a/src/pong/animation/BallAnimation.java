// File: BallAnimation.java
// Author: Leo Horne
// Date Created: 3/15/15 6:20 PM

package pong.animation;

import pong.collision.PongPaddleCollisionDetector;
import pong.mode.PongCanvas;
import pong.paddle.Paddle;
import pong.paddle.PaddleMode;

import java.util.Iterator;

/**
 * Extension de {@code Thread} qui sert à animer la balle et vérifier s'il y a eu une collision.
 */

public class BallAnimation extends Thread {

    private static final int FRAMES_PER_SECOND = 500;

    /**
     * Le temps entre les images successives.
     */
    private static final int TIMER_DELAY = 1000 / FRAMES_PER_SECOND;

    /**
     * {@code boolean} qui décrit si ce {@code Thread} est en pause.
     */
    private volatile boolean paused = false;

    /**
     * {@code boolean} qui décrit si ce {@code Thread} est mort.
     */
    private volatile boolean killed = false;

    /**
     * Variable qui donne accès au {@code PongCanvas} du jeu pour pouvoir déplacer la balle et redessiner le {@code PongCanvas}.
     */
    private PongCanvas pongCanvas;

    /**
     * Constructeur permettant de passer un {@code PongCanvas} comme paramètre.
     * @param pongCanvas le {@code PongCanvas} à faire passer.
     */
    public BallAnimation(PongCanvas pongCanvas) {

        this.pongCanvas = pongCanvas;
    }

    /**
     * Méthode qui est executé lorsqu'on appelle Thread#start().
     */
    @Override
    public void run() {

        long beforeTime;  // Temps avant l'execution de la boucle
        long sleepTime;  // Temps pendant lequel il faut arreter le Thread
        long timeDifference;  // Variable qui stockera le temps qu'il faut pour executer tout ce qui doit etre execute pour
                              // verifier si une collision a eu lieu et pour deplacer la balle

        beforeTime = System.currentTimeMillis();

        // Tant que le jeu n'est pas fini et le Thread n'est pas mort, il faut continuer à exécuter la boucle
        while (!pongCanvas.isGameOver() && !killed) {

            if (!paused) {  // Si le jeu n'est pas en pause, il faut vérifier si une collision a eu lieu et déplacer la balle


                // On itere a travers les detecteurs de collision du PongCanvas pour vérifier si une collision a eu lieu avec une raquette
                for (Iterator<PongPaddleCollisionDetector> iterator = pongCanvas.getCollisionDetectors().iterator(); iterator.hasNext(); ) {
                    PongPaddleCollisionDetector pongPaddleCollisionDetector = iterator.next();
                    pongPaddleCollisionDetector.checkCollisions();
                    Paddle currentPaddle = pongPaddleCollisionDetector.getPaddle();
                    if (currentPaddle.isToBeDestroyed()) {
                        iterator.remove();  // il faut appeler iterator.remove() au lieu d'enlever directement le detecteur de collisions
                                            // concerne car sinon on aura un ConcurrentModificationException
                                            // http://stackoverflow.com/questions/8189466/java-util-concurrentmodificationexception
                                            // http://stackoverflow.com/questions/15993356/how-iterators-remove-method-actually-remove-an-object
                                            // http://stackoverflow.com/questions/223918/iterating-through-a-list-avoiding-concurrentmodificationexception-when-removing?rq=1
                    }
                }

                pongCanvas.getPongWallCollisionDetector().checkCollisions();  // regarder si la balle a touche un mur
                pongCanvas.getBall().move();

                // On redessine autour de la balle. On ne redessine pas tout car cela ralentit enormement le jeu
                // et il faut redessiner un petit rectangle autour de la balle pour eviter que la balle apparait plusieurs
                // fois a l'ecran
                pongCanvas.repaint((int) (pongCanvas.getBall().getBallX() - 50),
                        (int) (pongCanvas.getBall().getBallY() - 50),
                        (int) (pongCanvas.getBall().getBallDiameter() + 100),
                        (int) (pongCanvas.getBall().getBallDiameter() + 100));

                timeDifference = System.currentTimeMillis() - beforeTime;  // on mesure le temps qu'on a pris pour verifier les collisions et deplacer la balle
                sleepTime = TIMER_DELAY - timeDifference;  // on soustrait ce temps au TIMER_DELAY pour avoir une animation lisse avec des intervalles de temps constantes

                if (sleepTime < 0) {  // il ne faut pas que le timeDifference soit negatif car on ne peut pas arreter le Thread pendant un
                                      // temps negatif, donc il faut verifier si timeDifference est positif
                    sleepTime = 2;
                }

                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                beforeTime = System.currentTimeMillis();  // il faut reinitialiser cette variable pour mesurer le temps entre executions successives

            } else {  // Si le jeu est en pause, on laisse s'arreter plus longtemps le Thread pour economiser les ressources
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        pongCanvas.end();  // apres que le boucle est terminee, on termine le PongCanvas
    }

    /**
     * Methode pour mettre l'animation en pause.
     * @param paused la variable decrivant si le jeu doit etre mis en pause ({@code true}) ou non ({@code false})
     */

    public void setPaused(boolean paused) {

        this.paused = paused;
    }

    /**
     * Methode pour terminer l'animation.
     */
    public void kill() {

        killed = true;
    }
}