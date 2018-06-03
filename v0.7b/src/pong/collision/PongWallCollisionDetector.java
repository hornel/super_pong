// File: PongCollisionDetector.java
// Author: Leo
// Date Created: 4/4/15 8:14 PM
// Version: 0.1a

package pong.collision;

import pong.ball.Ball;
import pong.mode.PongCanvas;
import pong.sound.PongSoundPlayer;
import pong.sound.Sounds;

/**
 * Classe qui gere les collisions entre la balle et les murs (en haut et en bas)
 */
public class PongWallCollisionDetector implements CollisionDetector {

    /**
     * La balle dont les collisions doivent etre verifiees.
     */
    private Ball ball;

    /**
     * Le PongCanvas ou les collisions ont lieu.
     */
    private PongCanvas pongCanvas;

    /**
     * Boolean qui definit si le service doit se faire depuis le milieu
     */
    private boolean shouldServeFromMiddle = false;

    /**
     * Boolean qui definit si le service doit se faire depuis un cote
     */
    private boolean shouldServeFromSide = false;

    /**
     * Le cote depuis lequel le service doit se faire
     */
    private int sideToServeFrom = 1;

    /**
     * Son qui doit etre joue lorsqu'un mur est frappe par la balle
     */
    private PongSoundPlayer wallHitSound = Sounds.getWallHitSound();

    public PongWallCollisionDetector(PongCanvas pongCanvas) {

        this.pongCanvas = pongCanvas;
        this.ball = pongCanvas.getBall();
    }

    public PongWallCollisionDetector(PongCanvas pongCanvas, boolean shouldServeFromMiddle) {

        this(pongCanvas);
        this.shouldServeFromMiddle = shouldServeFromMiddle;
    }

    public PongWallCollisionDetector(PongCanvas pongCanvas, int sideToServeFrom) {

        this(pongCanvas);
        this.sideToServeFrom = sideToServeFrom;
        this.shouldServeFromSide = true;
    }

    /**
     * Methode qui decrit si la balle a frappe le mur inferieur. Il faut que:
     * - la coordonnee y de la balle soit >= la hauteur du jeu - le diametre de la balle
     */
    public boolean ballHitBottom() {

        return ball.getBallY() >= pongCanvas.getHeight() - ball.getBallDiameter();
    }

    /**
     * Methode qui decrit si la balle a frappe le mur superieur. Il faut que:
     * - la coordonnee y de la balle soit <= 0
     */
    public boolean ballhitTop() {

        return ball.getBallY() <= 0;
    }

    /**
     * Methode qui decrit si la balle est sortie du jeu. Il faut que:
     * - la coordonnee x de la balle soit > la largeur du jeu + le diametre de la balle
     * -------- OU ---------
     * - la coordonnee x de la balle soit < le diametre negatif de la balle
     */
    public boolean ballOutOfBounds() {

        return ball.getBallX() > pongCanvas.getWidth() + ball.getBallDiameter() || ball.getBallX() < -ball.getBallDiameter();
    }

    @Override
    public void checkCollisions() {

        // si la balle est sortie du jeu, il faut faire le service
        if (!shouldServeFromMiddle && !shouldServeFromSide && ballOutOfBounds()) {
            ball.serve();

        } else if (shouldServeFromMiddle && ballOutOfBounds()) {
            ball.serveFromMiddle();

        } else if (shouldServeFromSide && ballOutOfBounds()) {
            ball.serveFromSide(sideToServeFrom);

        } else if (ballHitBottom()) {
            ball.setYTrajectory(-(Math.abs(ball.getYTrajectory())));
            wallHitSound.playSound();

        } else if (ballhitTop()) {
            ball.setYTrajectory(Math.abs(ball.getYTrajectory()));
            wallHitSound.playSound();
        }
    }
}