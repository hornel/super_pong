// File: Ball.java
// Author: Leo Horne
// Date Created: Friday, March 6, 2015
// Version: 0.2a

/* Changes from last version:
    * Improved collision (now takes into consideration if upper part of ball hits lower part of paddle and vice versa)
    * Adds optional gravityEnabled
    * Updated to use getters and setters from PongCanvas
 */

package pong.ball;

import pong.mode.PongCanvas;
import pong.paddle.Paddle;
import pong.sound.PongSoundPlayer;
import pong.sound.Sounds;

import java.awt.geom.Ellipse2D;

/**
 * Classe qui decrit la balle du jeu. Herite de la classe Ellipse2D.Double, ce qui permet de dessiner une balle a l'ecran.
 */

public class Ball extends Ellipse2D.Double {

    /**
     * Doubles qui stockent la gravite et la vitesse terminale de la balle si ces derniers sont actives.
     */
    private double gravity, terminalVelocity;

    /**
     * Double qui decrit la valeur par laquelle il faut diviser la trajectoire x de la balle pour
     * obtenir la gravite.
     */
    private double gravityCoefficient = 150;

    /**
     * Double qui decrit la valeur par laquelle if faut multiplier la trajectoire x pour obtenir
     * la vitesse terminale.
     */
    private double terminalMultiplier = 2.5;

    /**
     * Double qui decrit combien de fois la trajectoire y peut etre plus grand que la trajectoire x.
     */
    private double maxYTrajectoryMultiplier = 1.6;

    /**
     * Double qui decrit la trajectoire x de la balle.
     */
    private double xTrajectory;

    /**
     * Double qui decrit la trajectoire y de la balle.
     */
    private double yTrajectory;

    /**
     * Double qui decrit la trajectoire y maximale de la balle.
     */
    private double maxYTrajectory;

    /**
     * Double qui decrivent, respectivement, la position x de la balle, la position y de la balle et le diametre
     * de la balle.
     */
    private double ballX, ballY, ballDiameter;

    /**
     * Boolean qui decrit si la gravite est activee ou non.
     */
    private boolean gravityEnabled = false;

    /**
     * Boolean qui decrit si la vitesse terminale est activee.
     */
    private boolean terminalVelocityEnabled = false;

    /**
     * Valeur qui est necessaire pour stocker la trajectoire de la balle lors du service, car la trajectoire change
     * avant le service.
     */
    private double currentTrajectory;

    /**
     * PongCanvas sur lequel la balle doit se trouver.
     */
    private PongCanvas pongCanvas;

    /**
     * Son qui est joue lorsqu'un joueur obtient un point ou perd la balle.
     */
    private PongSoundPlayer scoreSound = Sounds.getPointSound();

    /**
     * Constructeur initialisant toutes les variables necessaires.
     */
    public Ball(PongCanvas pongCanvas, int ballX, int ballY, int ballDiameter, double xTrajectory, double yTrajectory) {

        super(ballX, ballY, ballDiameter, ballDiameter);  // on initialise la superclasse avec les coordonnees x, y, et diametre donnes
        this.pongCanvas = pongCanvas;
        this.ballX = ballX;
        this.ballY = ballY;
        this.ballDiameter = ballDiameter;
        gravity = xTrajectory / gravityCoefficient;  // la trajectoire x de la balle peut varier selon la vitesse de la
                                                     // balle, donc il faut la mettre en rapport avec la trajectoire x
        terminalVelocity = xTrajectory * terminalMultiplier;  // la meme chose pour la vitesse terminale
        currentTrajectory = Math.abs(xTrajectory);  // il faut stocker la trajectoire x pour qu'elle ne soit pas perdue au service
        setTrajectory(xTrajectory, yTrajectory);
        this.maxYTrajectory = xTrajectory * maxYTrajectoryMultiplier;  // la trajectoire y maximale depend de la trajectoire
                                                                       // x, donc il faut qu'elle soit en rapport avec la trajectoire x
    }

    /**
     * Retourne la limite droite de la balle.
     */

    public double getRightLimit() {

        return ballX + ballDiameter;
    }

    /**
     * Retourne la limite gauche de la balle.
     */
    public double getLeftLimit() {

        return ballX;
    }

    /**
     * Retourne la limite superieur de la balle.
     */
    public double getUpperLimit() {

        return ballY;
    }

    /**
     * Retourne la limite inferieur de la balle.
     */
    public double getLowerLimit() {

        return ballY + ballDiameter;
    }

    /**
     * Methode qui permet de changer les trajectoires x et y de la balle.
     */
    public void setTrajectory(double xTrajectory, double yTrajectory) {

        this.xTrajectory = xTrajectory;
        this.currentTrajectory = xTrajectory;  // il faut stocker la trajectoire x pour qu'elle ne soit pas perdue au service
        this.yTrajectory = yTrajectory;
        this.maxYTrajectory = xTrajectory * maxYTrajectoryMultiplier;  // il faut recalculer la trajectoire y maximale
                                                                       // car la trajectoire x a change.
    }


    /**
     * Methode qui permet de changer la vitesse de la balle et qui adapte la trajectoire x,
     * la trajectoire y, la trajectoire actuelle, la trajectoire y maximale, la gravite et la
     * vitesse terminale.
     */
    public void setSpeed(double xTrajectory) {

        // il faut recalculer toutes les parametres qui sont en lien avec la vitesse
        double oldXTrajectory = this.xTrajectory;
        double yBendFactorRatio = yTrajectory / Math.abs(this.xTrajectory);
        this.xTrajectory = Math.abs(xTrajectory);
        this.yTrajectory = this.xTrajectory * yBendFactorRatio;
        this.currentTrajectory = Math.abs(this.xTrajectory);
        this.maxYTrajectory = this.xTrajectory * maxYTrajectoryMultiplier;
        this.gravity = Math.abs(xTrajectory) / gravityCoefficient;
        this.terminalVelocity = Math.abs(xTrajectory) * terminalMultiplier;

        if (oldXTrajectory < 0) {
            this.xTrajectory *= -1;
        }
    }

    /**
     * Methode qui permet de changer la vitesse de la balle temporairement (la nouvelle vitesse sera
     * perdue au service) si la valeur de shouldRemainAfterServe est false.
     */
    public void setSpeed(double xTrajectory, boolean shouldRemainAfterServe) {

        double oldXTrajectory = this.xTrajectory;
        double yBendFactorRatio = yTrajectory / Math.abs(this.xTrajectory);
        this.xTrajectory = xTrajectory;
        this.yTrajectory = this.xTrajectory * yBendFactorRatio;

        if (shouldRemainAfterServe) {
            this.currentTrajectory = Math.abs(this.xTrajectory);
            this.maxYTrajectory = this.xTrajectory * maxYTrajectoryMultiplier;
            this.gravity = Math.abs(xTrajectory) / gravityCoefficient;
            this.terminalVelocity = Math.abs(xTrajectory) * terminalMultiplier;
        }

        if (oldXTrajectory < 0) {
            this.xTrajectory *= -1;
        }
    }

    public double getSpeed() {

        return Math.abs(xTrajectory);  // la valeur absolue de la trajectoire x est toujours egale a la vitesse
    }

    /**
     * Deplace la balle selon sa trajectoire x, y et gravite.
     */
    public void move() {

        ballX += xTrajectory;
        if (gravityEnabled) {  // il faut ajouter la gravite si elle est activee
            if (terminalVelocityEnabled && Math.abs(yTrajectory) <= terminalVelocity) {
                yTrajectory += gravity;  // on ajoute la gravite a la trajectoire y pour accelerer la balle
            } else {
                yTrajectory += gravity;
            }
        }
        ballY += yTrajectory;
        super.setFrame(ballX, ballY, ballDiameter, ballDiameter);
    }

    public void setGravity(boolean b) {

        gravityEnabled = b;
    }

    /**
     * Methode qui permet de faire le service.
     */
    public void serve() {

        yTrajectory = 0;  // on fait deplacer la balle horizontalement pour eviter des sons et d'autres effets indesirables

        // on veut creer un delai entre l'obtention d'un point/la perte de la balle et le service qui soit egal peu importe
        // la vitesse de la balle. Il faut donc que la balle aie la meme vitesse avant chaque service.
        setSpeed(PongCanvas.INITIAL_X_TRAJECTORY, false);

        // on regarde si la coordonne x de la balle est inferieur a -300 et non 0 pour creer un delai
        if (ballX < -300) {  // balle a sorti du jeu a droite

            pongCanvas.increasePlayerScore(2);
            scoreSound.playSound();
            ballX = 50 + ballDiameter;  // il faut que la balle soit devant la raquette lors du service!
            ballY = Math.random() * ((double) pongCanvas.getHeight() - getBallDiameter());  // pour avoir un service different
                                                                                            // a chaque fois, la coordonnee y du
                                                                                            // service est aleatoire
            xTrajectory = currentTrajectory;  // il faut remettre les trajectoires x et y aux valeurs qu'ils etaient avant
            yTrajectory = currentTrajectory;  // le service
            pongCanvas.setPlayer1Turn(false);

        // les memes commentaires pour si la balle a sorti du jeu a gauche
        } else if (ballX > pongCanvas.getWidth() + 300) {

            pongCanvas.increasePlayerScore(1);
            scoreSound.playSound();
            ballX = pongCanvas.getWidth() - 50 - ballDiameter;
            ballY = Math.random() * ((double) pongCanvas.getHeight() - getBallDiameter());
            xTrajectory = -currentTrajectory;
            yTrajectory = currentTrajectory;
            pongCanvas.setPlayer1Turn(true);
        }
    }

    /**
     * Permet de servir la balle d'un cote du jeu en utilisant les valeurs Paddle.RIGHT (= 2) et Paddle.LEFT (= 1) comme parametres
     */
    public void serveFromSide(int side) {

        yTrajectory = 0;
        setSpeed(PongCanvas.INITIAL_X_TRAJECTORY, false);

        if (side == Paddle.LEFT) {

            pongCanvas.increasePlayerScore(2);
            scoreSound.playSound();
            ballX = 50 + ballDiameter;
            ballY = 50 + ballDiameter;
            xTrajectory = currentTrajectory;
            yTrajectory = currentTrajectory;
            pongCanvas.setPlayer1Turn(false);

        } else {

            pongCanvas.increasePlayerScore(1);
            scoreSound.playSound();
            ballX = pongCanvas.getWidth() - 50 - ballDiameter;
            ballY = 50 + ballDiameter;
            xTrajectory = -currentTrajectory;
            yTrajectory = currentTrajectory;
            pongCanvas.setPlayer1Turn(true);
        }
    }

    /**
     * Permet de servir la balle du milieu.
     */
    public void serveFromMiddle() {

        yTrajectory = 0;
        setSpeed(PongCanvas.INITIAL_X_TRAJECTORY, false);

        if (ballX < -300 || ballX > pongCanvas.getWidth() + 300) {

            pongCanvas.increasePlayerScore(2);
            scoreSound.playSound();
            ballX = pongCanvas.getWidth() / 2;
            ballY = pongCanvas.getHeight() / 2;
            xTrajectory = -currentTrajectory;
            yTrajectory = currentTrajectory;
        }
    }

    public double getBallY() {

        return ballY;
    }

    public void setBallY(double ballY) {

        this.ballY = ballY;
        super.setFrame(ballX, ballY, ballDiameter, ballDiameter);
    }

    public double getBallDiameter() {

        return ballDiameter;
    }

    public double getXTrajectory() {

        return xTrajectory;
    }

    /**
     * Permet de changer la direction de la trajectoire x selon un cote:
     * Paddle.LEFT, ce qui signifiera que la trajectoire x doit etre positive ou
     * Paddle.RIGHT, ce qui signifiera que la trajectoire x doit etre negative.
     */
    public void changeXTrajectory(int side) {

        if (side == Paddle.LEFT) {
            xTrajectory = Math.abs(xTrajectory);
        } else {
            xTrajectory = -Math.abs(xTrajectory);
        }
    }

    public double getYTrajectory() {

        return yTrajectory;
    }

    public void setYTrajectory(double yTrajectory) {

        this.yTrajectory = yTrajectory;
    }

    public double getMaxYTrajectory() {

        return maxYTrajectory;
    }

    public double getBallX() {

        return ballX;
    }

    public void setBallX(double ballX) {

        this.ballX = ballX;
        super.setFrame(ballX, ballY, ballDiameter, ballDiameter);
    }

    public void setBallDiameter(double diameter) {

        this.ballDiameter = diameter;
        super.setFrame(ballX, ballY, ballDiameter, ballDiameter);
    }

    public double getGravityCoefficient() {

        return gravityCoefficient;
    }

    public void setGravityCoefficient(double gravityCoefficient) {

        this.gravityCoefficient = gravityCoefficient;
        gravity = Math.abs(xTrajectory) / gravityCoefficient;
    }

    public double getTerminalMultiplier() {

        return terminalMultiplier;
    }

    public void setTerminalMultiplier(double terminalMultiplier) {

        this.terminalMultiplier = terminalMultiplier;
        terminalVelocity = Math.abs(xTrajectory) * terminalMultiplier;
    }

    public boolean isGravityEnabled() {

        return gravityEnabled;
    }

    public boolean isTerminalVelocityEnabled() {

        return terminalVelocityEnabled;
    }

    public void setTerminalVelocityEnabled(boolean terminalVelocityEnabled) {

        this.terminalVelocityEnabled = terminalVelocityEnabled;
    }
}