package pong.paddle;

import pong.ball.Ball;
import pong.control.PongPreferencesEditor;
import pong.missile.PongMissile;
import pong.mode.PongCanvas;
import pong.mode.TwoPlayerBattlePongCanvas;

import java.awt.*;

/**
 * Classe qui decrit l'IA dans le mode bataille avec IA. C'est une extension de PaddleAI.
 */
public class BattlePaddleAI extends PaddleAI {

    /**
     * La balle que l'IA doit frapper.
     */
    private Ball ball;

    /**
     * La raquette que l'IA doit controler.
     */
    private BattlePaddle paddle;

    // Les trois variables suivantes sont pour determiner quand est-ce que l'IA sera incapable d'eviter un missile.
    // randomFireTimeIndex est un nombre aleatoire dans l'intervalle [0; MAX_FIRE_TIME_INDEX]. Lorsque randomFireTimeIndex
    // est egal a FIRE_TIME_INDEX, l'IA ne pourra pas eviter le missile.
    private final static long FIRE_TIME_INDEX = 1;

    private static long MAX_FIRE_TIME_INDEX = PongPreferencesEditor.INITIAL_AI_DIFFICULTY / 10;

    private long randomFireTimeIndex = 2;

    /**
     * Decrit si l'IA doit eviter le missile ou si elle peut l'ignorer pour poursuivre la balle.
     */
    private boolean shouldAvoidMissile = false;

    /**
     * La vitesse a laquelle l'IA peut eviter un missile.
     */
    private double missileAvoidingSpeed = 15;

    /**
     * Si un missile est lance, l'IA ne pourra pas depasser cette limite superieure.
     */
    private double upperLimit;

    /**
     * Si un missile est lance, l'IA ne pourra pas depasser cette limite inferieure.
     */
    private double lowerLimit;

    /**
     * La raquette que l'IA essaye de detruire.
     */
    private BattlePaddle enemyPaddle;

    /**
     * Le TwoPlayerBattlePongCanvas ou l'IA se trouve.
     */
    private TwoPlayerBattlePongCanvas pongCanvas;

    public BattlePaddleAI(PongCanvas pongCanvas, BattlePaddle paddle, BattlePaddle enemyPaddle, Ball ball) {

        super(pongCanvas, paddle, ball);
        this.ball = ball;
        this.paddle = paddle;
        this.enemyPaddle = enemyPaddle;
        upperLimit = 0;
        lowerLimit = pongCanvas.getHeight();
        if (pongCanvas instanceof TwoPlayerBattlePongCanvas) {
            this.pongCanvas = (TwoPlayerBattlePongCanvas) pongCanvas;
        }
    }

    @Override
    protected void movePaddle() {

        // On calcule les limites superieure et inferieure pour savoir ou l'IA pourra deplacer la raquette.
        calculateLimits();
        // La raquette doit toujours etre dans les limites, sinon elle ne peut plus etre deplacee.
        if (paddle.getUpperLimit() <= upperLimit) {
            paddle.setPaddleY(upperLimit + 1);
        }
        if (paddle.getLowerLimit() >= lowerLimit) {
            paddle.setPaddleY(lowerLimit - paddle.getHeight() - 1);
        }
        // Si la raquette se trouve dans les limites
        if (paddle.getUpperLimit() >= upperLimit && paddle.getLowerLimit() <= lowerLimit) {
            // L'IA va utiliser l'algorithme suivant:
            // 1. Si necessaire, elle va eviter les missiles.
            // 2. Si ce n'est pas necessaire, elle se deplace vers la balle a la vitesse de
            //    La racine carree de (la position x de la balle par rapport a la moitie du jeu ou se trouve la balle = un pourcentage)
            //    fois la difficulte de l'IA fois la vitesse de la balle
            //    si la balle se trouve dans sa moitie du terrain et elle ne l'a pas encore frappee.
            // 3. Elle va essayer de detruire la raquette de l'adversaire si cela n'est pas le cas.
            if (ball.getX() >= pongCanvas.getWidth() / 2 && (paddle.getSide() == Paddle.LEFT ? ball.getXTrajectory() < 0 : ball.getXTrajectory() > 0) && !pongCanvas.isPaused()) {
                setPaddleSpeed((Math.sqrt((ball.getX() - pongCanvas.getWidth() / 2) / pongCanvas.getWidth() / 2) * getAiDifficulty() * ball.getSpeed()));
                Rectangle oldBounds = paddle.getBounds();
                avoidMissile();
                if (!shouldAvoidMissile) {
                    paddle.setPaddleY(paddle.getY() + (getPaddleSpeed() * getDirectionTowardsBall()));
                }
                pongCanvas.repaint(oldBounds);
                pongCanvas.repaint(paddle.getBounds());
            } else if (!pongCanvas.isPaused()) {
                setPaddleSpeed(getAiDifficulty() / 3);
                Rectangle oldBounds = paddle.getBounds();
                avoidMissile();
                if (!shouldAvoidMissile) {
                    // Elle va essayer de lancer un missile entre la raquette et la balle pour essayer d'intercepter
                    // le joueur lorsqu'il est en train de chercher la balle
                    paddle.setPaddleY(paddle.getY() + (getPaddleSpeed() * getDirectionTowardsMiddleOfBallAndPaddle()));
                    if (shouldFireMissile()) pongCanvas.fireMissile(paddle);
                }
                pongCanvas.repaint(oldBounds);
                pongCanvas.repaint(paddle.getBounds());
            }
        }
    }

    /**
     * Permet de chercher la direction dans laquelle il faut se deplacer pour aller entre la balle et la raquette.
     */
    private int getDirectionTowardsMiddleOfBallAndPaddle() {

        double paddleY = enemyPaddle.getCenterY();  // le centre de la raquette
        double ballY = ball.getCenterY();  // le centre de la balle

        double middle = (paddleY + ballY) / 2;  // le milieu de la balle et la raquette

        // si middle < la limite superieure de la raquette, il faut se deplacer vers le haut
        if (middle < paddle.getUpperLimit()) {
            return UP;
        // si middle > la limite inferieure de la raquette, il faut se deplacer vers le bas
        } else if (middle > paddle.getLowerLimit()) {
            return DOWN;
        // sinon, il ne faut pas se deplacer
        } else {
            return NO_DIRECTION;
        }
    }

    /**
     * Calcule les limites dans lesquelles l'IA peut deplacer la raquette sans etre tue par un missile.
     */
    private void calculateLimits() {

        // le missile de l'adversaire
        PongMissile missile = enemyPaddle.getMissile();
        // si l'adversaire a lance un missile
        if (enemyPaddle.hasFiredMissile()) {
            // si la distance entre le missile et la raquette est < 150 et le missile est au-dessus de la raquette, il
            // faut changer la limite superieure
            if (missile.getBottom() < paddle.getUpperLimit() && Math.abs(missile.getFront() - paddle.getOuterLimit()) <= 150) {
                upperLimit = missile.getBottom() + missileAvoidingSpeed;
            // si la distance entre le missile et la raquette est < 150 et le missile est au-deessous de la raquette, il
            // faut changer la limite inferieure
            } else if (missile.getTop() > paddle.getLowerLimit() && Math.abs(missile.getFront() - paddle.getOuterLimit()) <= 150) {
                lowerLimit = missile.getTop() - missileAvoidingSpeed;
            // sinon, on remet les limites a leurs valeurs par defaut
            } else {
                upperLimit = 0;
                lowerLimit = pongCanvas.getHeight();
            }
            // Il faut eviter le missile si le missile se trouve juste devant la raquette (distance max. 150)
            shouldAvoidMissile = missile.getTop() <= paddle.getLowerLimit() && missile.getBottom() >= paddle.getUpperLimit() && Math.abs(missile.getFront() - paddle.getOuterLimit()) <= 150;
        // si l'adversaire n'a pas lance de missile, il faut remettre les limites a leurs valeurs par defaut, il ne faut
        // pas eviter le missile et il faut recalculer quand l'IA ne pourra pas eviter le missile
        } else {
            upperLimit = 0;
            lowerLimit = pongCanvas.getHeight();
            shouldAvoidMissile = false;
            randomFireTimeIndex = Math.round(Math.random() * MAX_FIRE_TIME_INDEX);
        }
    }

    // Permet d'eviter un missile si necessaire
    private void avoidMissile() {

        // si on doit eviter le missile
        if (shouldAvoidMissile) {
            // si on peut eviter le missile
            if (randomFireTimeIndex != FIRE_TIME_INDEX) {
                // le missile de l'adversaire
                PongMissile missile = enemyPaddle.getMissile();
                // la distance entre le missile et la limite superieure de la raquette
                double missileDistanceFromTop = Math.abs(missile.getBottom() - paddle.getUpperLimit());
                // la distance entre le missile et la limite inferieure de la raquette
                double missileDistanceFromBottom = Math.abs(missile.getTop() - paddle.getLowerLimit());

                // on cherche a savoir s'il faut eviter le missile en allant vers le bas ou vers le haut. On prend
                // le chemin ou on fait la moins de distance possible. Mais il faut aussi faire attention, car la raquette
                // ne doit pas depasser les limites du jeu.
                if (missileDistanceFromTop <= missileDistanceFromBottom && missileDistanceFromTop <= Math.abs(pongCanvas.getHeight() - paddle.getLowerLimit())) {
                    paddle.setPaddleY(paddle.getY() + (missileAvoidingSpeed * DOWN));
                } else if (missileDistanceFromTop > missileDistanceFromBottom && missileDistanceFromBottom <= paddle.getUpperLimit()) {
                    paddle.setPaddleY(paddle.getY() + (missileAvoidingSpeed * UP));
                } else if (missileDistanceFromTop > missileDistanceFromBottom) {
                    paddle.setPaddleY(paddle.getY() + (missileAvoidingSpeed * DOWN));
                } else {
                    paddle.setPaddleY(paddle.getY() + (missileAvoidingSpeed * UP));
                }
            }
        }
    }

    /**
     * Decrit s'il faut lancer un missile
     */
    private boolean shouldFireMissile() {

        // On lance un missile si la distance entre la raquette de l'adversaire et celle qui est controlee par l'IA est
        // < 120 et la raquette de l'adversaire n'est pas detruit.
        return (Math.abs(enemyPaddle.getY() - paddle.getY()) < (getAiDifficulty() * 3) && !enemyPaddle.isDestroyed());
    }

    @Override
    public void setAiDifficulty(int aiDifficulty) {

        super.setAiDifficulty(aiDifficulty);
        // il faut changer les deux variables suivantes pour adapter la difficulte de l'IA.
        MAX_FIRE_TIME_INDEX = aiDifficulty / 10;
        randomFireTimeIndex = Math.round(Math.random() * MAX_FIRE_TIME_INDEX);
        System.out.println(getAiDifficulty());
    }
}