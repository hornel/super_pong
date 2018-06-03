// File: PongPaddleCollisionDetector.java
// Author: Leo
// Date Created: 4/4/15 8:14 PM

package pong.collision;

import pong.ball.Ball;
import pong.mode.PongCanvas;
import pong.paddle.Paddle;
import pong.sound.PongSoundPlayer;
import pong.sound.Sounds;

/**
 * Classe qui detecte les collisions entre la balle et une raquette.
 */
public class PongPaddleCollisionDetector implements CollisionDetector {

    /**
     * La balle dont les collisions doivent etre verifiees.
     */
    private Ball ball;

    /**
     * La raquette avec laquelle la balle peut collisionner.
     */
    private Paddle paddle;

    /**
     * Le PongCanvas ou les collisions ont lieu.
     */
    private PongCanvas pongCanvas;

    /**
     * Le son qui doit etre joue lorsque la balle frappe la raquette.
     */
    private PongSoundPlayer paddleHitSound = Sounds.getPaddleHitSound();

    public PongPaddleCollisionDetector(PongCanvas pongCanvas, Paddle paddle) {

        this.pongCanvas = pongCanvas;
        this.ball = pongCanvas.getBall();
        this.paddle = paddle;
    }

    public Paddle getPaddle() {

        return paddle;
    }

    /**
     * Decrit si la balle a touche la limite exterieure (limite vers le centre du jeu) de la raquette. Il faut donc que:
     * - La limite gauche de la balle soit superieure ou egale a la limite exterieur de la raquette si la raquette se
     *   trouve a droite
     * - La limite droite de la balle soit inferieure ou egale a la limite exterieur de la raquette si la raquette se
     *   trouve a gauche
     */
    public boolean ballHitPaddleOuterEdge() {

        if (paddle.getSide() == Paddle.LEFT) {
            return ball.getLeftLimit() <= paddle.getOuterLimit();
        } else {
            return ball.getRightLimit() >= paddle.getOuterLimit();
        }
    }

    /**
     * Decrit si la balle n'a pas depasse la limite superieure de la raquette. Il faut que:
     * - Le centre vertical de la balle soit superieur ou egal a la limite superieure de la raquette
     */
    public boolean ballDidNotSurpassPaddleUpperEdge() {

        return ball.getCenterY() >= paddle.getUpperLimit();
    }

    /**
     * Decrit si la balle n'a pas depasse la limite superieure de la raquette. Il faut que:
     * - Le centre vertical de la balle est inferieur ou egal a la limite inferieure de la raquette
     */
    public boolean ballDidNotSurpassPaddleLowerEdge() {

        return ball.getCenterY() <= paddle.getLowerLimit();
    }

    /**
     * Decrit si la balle n'est pas derriere la raquette. Il faut que:
     * - La limite droite de la balle soit superieure ou egale a la limite arriere de la raquette si la raquette se
     *   trouve a gauche
     * - La limite gauche de la balle soit inferieure ou egale a la limite arriere de la raquette si la raquette se
     *   trouve a droite
     */
    public boolean ballNotBehindPaddle() {

        if (paddle.getSide() == Paddle.LEFT) {
            return ball.getRightLimit() >= paddle.getBehindLimit();
        } else {
            return ball.getLeftLimit() <= paddle.getBehindLimit();
        }
    }

    /**
     * Decrit si la balle a frappe la limite superieure de la raquette. Il faut que:
     * - si la raquette se trouve a gauche:
     *   - le centre horizontal de la balle soit <= la limite exterieure de la raquette
     *   - la limite inferieure de la balle soit >= la limite superieure de la raquette
     *   - La balle se trouve dans la moitie superieure de la raquette
     *   - La balle ne soit pas derriere la raquette
     * - si la raqeutte se trouve a droite:
     *   - le centre horizontal de la balle soit >= la limite exterieure de la raquette
     *   - la limite inferieure de la balle soit >= la limite superieure de la raquette
     *   - La balle se trouve dans la moitie superieure de la raquette
     *   - La balle ne soit pas derriere la raquette
     */
    public boolean ballHitPaddleUpperEdge() {

        if (paddle.getSide() == Paddle.LEFT) {
            return ball.getCenterX() <= paddle.getOuterLimit() && ball.getLowerLimit() >= paddle.getUpperLimit() && ball.getLowerLimit() < (paddle.getUpperLimit() + paddle.getHeight() / 2) && ballNotBehindPaddle();
        } else {
            return ball.getCenterX() >= paddle.getOuterLimit() && ball.getLowerLimit() >= paddle.getUpperLimit() && ball.getLowerLimit() < (paddle.getUpperLimit() + paddle.getHeight() / 2) && ballNotBehindPaddle();
        }
    }

    /**
     * Decrit si la balle a frappe la limite inferieure de la raquette. Il faut que:
     * - si la raquette se trouve a gauche:
     *   - le centre horizontal de la balle soit <= la limite exterieure de la raquette
     *   - la limite inferieure de la balle soit <= la limite superieure de la raquette
     *   - La balle se trouve dans la moitie inferieure de la raquette
     *   - La balle ne soit pas derriere la raquette
     * - si la raqeutte se trouve a droite:
     *   - le centre horizontal de la balle soit <= la limite exterieure de la raquette
     *   - la limite inferieure de la balle soit >= la limite superieure de la raquette
     *   - La balle se trouve dans la moitie inferieure de la raquette
     *   - La balle ne soit pas derriere la raquette
     */
    public boolean ballHitPaddleLowerEdge() {

        if (paddle.getSide() == Paddle.LEFT) {
            return ball.getCenterX() <= paddle.getOuterLimit() && ball.getUpperLimit() <= paddle.getLowerLimit() && ball.getUpperLimit() > (paddle.getLowerLimit() - paddle.getHeight() / 2) && ballNotBehindPaddle();
        } else {
            return ball.getCenterX() >= paddle.getOuterLimit() && ball.getUpperLimit() <= paddle.getLowerLimit() && ball.getUpperLimit() > (paddle.getLowerLimit() - paddle.getHeight() / 2) && ballNotBehindPaddle();
        }
    }

    /**
     * Decrit si la balle a frappe un coin de la raquette. Cela n'est pas la meme chose que si la balle a frappe la limite
     * superieure, inferieure ou exterieure de la raquette car la balle est un cercle. Il faut donc que:
     * - Le centre vertical de la raquette soit superieur a la limite inferieure de la raquette
     * - La norme du vecteur allant du centre de la balle au coin inferieur soit plus petite ou egale au rayon de la balle
     * ---- OU ------
     * - Le centre vertical de la raquette soit inferieur a la limite superieure de la raquette
     * - La norme du vecteur allant du centre de la balle au coin superieur soit plus petite ou egale au rayon de la balle
     */
    public boolean ballEdgeHitPaddleCorner() {

        return ball.getCenterY() > paddle.getLowerLimit() &&
                Math.sqrt(
                        Math.pow(ball.getCenterX() - paddle.getOuterLimit(), 2)
                        + Math.pow(ball.getCenterY() - paddle.getLowerLimit(), 2)
                )
                        <= ball.getBallDiameter() / 2
                ||
                ball.getCenterY() < paddle.getUpperLimit() &&
                        Math.sqrt(
                                Math.pow(ball.getCenterX() - paddle.getOuterLimit(), 2)
                                + Math.pow(ball.getCenterY() - paddle.getUpperLimit(), 2)
                        )
                                <= ball.getBallDiameter() / 2;
    }

    /**
     * Methode qui decrit si la balle est proche de la raquette. Il faut que:
     * - la distance horizontale entre le centre de la balle et la limite exterieure de la raquette soit plus petit que
     *   le rayon de la balle + la trajectoire x de la raquette + la largeur de la raquette
     * pour que la balle peut etre decrite comme etant "proche de la raquette"
     */
    public boolean ballCloseToPaddle() {

        return Math.abs(ball.getCenterX() - paddle.getOuterLimit()) <= ball.getBallDiameter() / 2 + ball.getXTrajectory() + paddle.getWidth();
    }

    @Override
    public void checkCollisions() {

        // on regarde premierement si la balle est proche de la raquette. On utilise un logical AND (&&), donc ce qui suit
        // le && sera execute seulement si la balle est proche de la raquette. Cela permet d'economiser les ressources
        // de l'ordinateur en evitant de faire des calcules inutiles.
        if (ballCloseToPaddle() &&
                // Pour qu'il y ait une collision, il faut que:
                // Option 1:
                //    - la balle a frappe la limite exterieure de la raquette
                //    - la balle n'est pas derriere la raqeutte
                //    - la balle n'a pas depasse la limite superieure de la raquette
                //    - la balle n'a pas depasse la limite inferieure de la raquette
                // Option 2:
                //    - la balle a frappe un coin de la raquette
                // Option 3:
                //    - la balle a frappe la limite superieure ou inferieure de la raquette
                (((ballHitPaddleOuterEdge() && ballNotBehindPaddle()) && ballDidNotSurpassPaddleUpperEdge() && ballDidNotSurpassPaddleLowerEdge()) ||
                ballEdgeHitPaddleCorner() ||
                        (ballHitPaddleUpperEdge() || ballHitPaddleLowerEdge()))) {

            // on devie la trajectoire y de la balle selon l'endroit ou elle a frappe la raquette
            checkPaddleCollisions();

            // il se peut que l'utilisateur deplace soudainement la raquette sur la balle. si on joue le son a chaque
            // fois que la balle se deplace et est a l'interieur de la raquette, la balle sera enormement ralentie. Pour
            // eviter ce probleme, on joue le son seulement toutes les 500 ms. Si la balle est a l'interieur de la
            // raquette, il ne faut pas multiplier la trajectoire x de la balle par -1, car si la balle est suffisamment
            // a l'interieur de la raquette, sa trajectoire x ne suffira pas pour sortir de la raquette. Ainsi, comme la
            // balle sera toujours a l'interieur de la raquette, la trajectoire serait de nouveau multipliee par -1 et la
            // balle commencerait a vibrer sur place. Pour eviter cela, il faut changer la trajectoire x de manieure absolue:
            // elle doit etre negative quant elle frappe a droite et positive quand elle frappe a gauche.
            paddleHitSound.playSoundAfter(500);
            ball.changeXTrajectory(paddle.getSide());


            if (paddle.getSide() == Paddle.LEFT) {
                pongCanvas.setPlayer1Turn(false);
            } else {
                pongCanvas.setPlayer1Turn(true);
            }
            performCustomAction();
        }
    }

    /**
     * Methode qui change la trajectoire y de la balle selon l'endroit ou elle frappe la raquette.
     *
     */
    private void checkPaddleCollisions() {

        double paddleCenter;  // centre vertical de la raquette
        double hitDistance;  // distance entre le centre vertical et l'endroit ou la balle a frappe la raquette
        double hitPercentile;  // cette distance en pourcent (0% = centre de la raquette; 100% = extremite de la raquette)
        double ballCenter;  // centre vertical de la balle

        paddleCenter = paddle.getCenterY();
        ballCenter = ball.getCenterY();
        hitDistance = ballCenter - paddleCenter;

        hitPercentile = hitDistance / ((paddle.getHeight() + ball.getBallDiameter()) / 2);

        // on change la trajectoire y de la balle selon le pourcent qu'on a obtenu * trajectoire y maximale
        ball.setYTrajectory(hitPercentile * ball.getMaxYTrajectory());
    }

    /**
     * Methode qui peut etre redefinie lors de l'instantiation de cette classe. Elle sera executee si une collision
     * a eu lieu.
     */
    public void performCustomAction() {}
}