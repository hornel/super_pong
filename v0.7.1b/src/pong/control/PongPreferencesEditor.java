// File: PongPreferencesEditor.java
// Author: Leo Horne
// Date Created: Saturday, March 7, 2015
// Version: 0.1

package pong.control;

import pong.ball.Ball;
import pong.lang.Language;
import pong.mode.*;
import pong.paddle.Paddle;
import pong.paddle.PaddleMode;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.Map;

/**
 * Classe qui permet de modifier les preferences du jeu.
 */
public class PongPreferencesEditor {

    /**
     * La vitesse minimale de la balle.
     */
    public static final double MINIMUM_BALL_SPEED = 0.1;

    /**
     * La vitesse maximale de la balle.
     */
    public static final double MAXIMUM_BALL_SPEED = 2.5;

    /**
     * La taille minimale de la balle.
     */
    public static final double MINIMUM_BALL_SIZE = 5;

    /**
     * La taille maximale de la balle.
     */
    public static final double MAXIMUM_BALL_SIZE = 150;

    /**
     * La hauteur minimale de la raquette.
     */
    public static final double MINIMUM_PADDLE_HEIGHT = 10;

    /**
     * L'espace entre les bouts de la raquette et les limites superieures et inferieures du jeu.
     */
    public static final double PADDING = 4;

    /**
     * La hauteur maximale de la raquette.
     */
    public static double MAXIMUM_PADDLE_HEIGHT;

    /**
     * Le mupliplicateur minimale de la vitesse terminale. Permet d'obtenir la vitesse terminale en multipliant
     * la trajectoire x de la balle par cette variable.
     */
    public static final double MINIMUM_TERMINAL_MULTIPLIER = 2;

    /**
     * Le mupliplicateur maximale de la vitesse terminale. Permet d'obtenir la vitesse terminale en multipliant
     * la trajectoire x de la balle par cette variable.
     */
    public static final double MAXIMUM_TERMINAL_MULTIPLIER = 3;

    /**
     * Le diviseur minimale de la gravite. Permet d'obtenir la gravite en divisant
     * la trajectoire x de la balle par cette variable.
     */
    public static final double MINIMUM_GRAVITY_COEFFICIENT = 50;

    /**
     * Le diviseur maximale de la gravite. Permet d'obtenir la gravite en divisant
     * la trajectoire x de la balle par cette variable.
     */
    public static final double MAXIMUM_GRAVITY_COEFFICIENT = 250;

    /**
     * Le score maximale que l'utilisateur peut choisir.
     */
    public static final int MAXIMUM_SCORE = 100;

    /**
     * Le score minimale que l'utilisateur peut choisir.
     */
    public static int MINIMUM_SCORE = 1;

    /**
     * La difficulte minimale de l'IA.
     */
    public static final int MINIMUM_AI_DIFFICULY = 10;

    /**
     * La difficulte maximale de l'IA.
     */
    public static final int MAXIMUM_AI_DIFFICULTY = 70;

    /**
     * La difficulte intiale de l'IA.
     */
    public static final int INITIAL_AI_DIFFICULTY = 40;

    /**
     * Le PongCanvas dont les preferences vont etre modifiees.
     */
    private PongCanvas pongCanvas;

    /**
     * Map qui permet de chercher un terme dans la langue voulue.
     */
    private Map<String, String> lang = Language.getLanguageMap(Language.getLoc());

    public PongPreferencesEditor(PongCanvas pongCanvas) {

        this.pongCanvas = pongCanvas;

        // il faut calculer la hauteur maximale de la raquette car elle depend de la hauteur du jeu.
        MAXIMUM_PADDLE_HEIGHT = pongCanvas.getHeight() - PADDING * 2;
    }

    /**
     * Cherche la vitesse actuelle de la balle.
     */
    public double getCurrentBallSpeed() {

        return pongCanvas.getBall().getSpeed();
    }

    /**
     * Cherche la taille actuelle de la balle.
     */
    public double getCurrentBallSize() {

        return pongCanvas.getBall().getBallDiameter();
    }

    /**
     * Cherche la hauteur actuelle des raquettes.
     */
    public double getCurrentPaddleHeight() {

        Paddle[] paddles = pongCanvas.getPaddles();
        // valeur qui sera retournee s'il n'existe pas de Paddles dont le PaddleMode est PaddleMode.HUMAN ou PaddleMode.AI.
        double height = paddles[0].getPaddleHeight();

        // il faut chercher la hauteur des raquettes qui sont manipulees par un utilisateur ou l'ordinateur.
        for (Paddle paddle : paddles) {
            if (paddle.getPaddleMode().equals(PaddleMode.HUMAN) || paddle.getPaddleMode().equals(PaddleMode.AI)) {
                height = paddle.getPaddleHeight();
                break;
            }
        }
        return height;
    }

    /**
     * Modifie la vitesse de la balle.
     */
    public void setBallSpeed(double speed) {

        pongCanvas.getBall().setSpeed(speed);
    }

    /**
     * Modifie la couleur du fond du jeu.
     */
    public void setBackgroundColor() {

        // on utilise la classe PongColorChooser pour presenter une fenetre pour choisir les couleurs et modifier
        // la couleur du fond.

        final PongColorChooser backgroundChooser = new PongColorChooser(lang.get("bgColorChangeTitle"), pongCanvas.getBackgroundColor());
        backgroundChooser.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                pongCanvas.setBackground(backgroundChooser.getColorChooser().getColor());
                pongCanvas.repaint();
            }
        });
        backgroundChooser.setVisible(true);
    }

    /**
     * Modifie la couleur du premier plan (balle, raquettes, score, ligne du milieu, etc.) du jeu.
     */
    public void setForegroundColor() {

        // on utilise la classe PongColorChooser pour presenter une fenetre pour choisir les couleurs et modifier
        // la couleur du premier plan.

        final PongColorChooser foregroundChooser = new PongColorChooser(lang.get("fgColorChangeTitle"), pongCanvas.getForegroundColor());
        foregroundChooser.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                pongCanvas.setForegroundColor(foregroundChooser.getColorChooser().getColor());
                pongCanvas.repaint();
            }
        });
        foregroundChooser.setVisible(true);
    }

    /**
     * Modifie la hauteur des raquettes.
     */
    public void setPaddleHeight(double height) {

        Paddle[] paddles = pongCanvas.getPaddles();
        for (Paddle paddle : paddles) {
            // on veut seulement changer la hauter de la raquette si la raquette est manipulee par une personne ou l'ordinateur.
            if (paddle.getPaddleMode().equals(PaddleMode.HUMAN) || paddle.getPaddleMode().equals(PaddleMode.AI)) {
                paddle.setPaddleHeight(height);
                // si la raquette depasse la limite inferieure du jeu, il faut la deplacer pour qu'elle soit dans les limites.
                if (paddle.getLowerLimit() >= pongCanvas.getHeight() - PADDING) {
                    paddle.setPaddleY(pongCanvas.getHeight() - height - PADDING);  // On utilise PADDING pour creer un
                                                                                   // espace entre la raquette et la limite du jeu
                }
            }
        }
    }

    /**
     * Modifie la hauteur du mur dans un SinglePlayerTrainingPongCanvas.
     */
    public void setWallHeight(double height) {

        if (pongCanvas instanceof SinglePlayerTrainingPongCanvas) {

            SinglePlayerTrainingPongCanvas trainingPongCanvas = (SinglePlayerTrainingPongCanvas) pongCanvas;
            trainingPongCanvas.setWallHeight(height);
            if (trainingPongCanvas.getWall().getLowerLimit() >= pongCanvas.getHeight() - PADDING) {
                trainingPongCanvas.getWall().setPaddleY(pongCanvas.getHeight() - height - PADDING);
            }
            pongCanvas.repaint();
        }
    }

    /**
     * Modifie la couleur du mur dans un SinglePlayerTrainingPongCanvas.
     */
    public void setWallColor() {

        // On peut changer la couleur du mur seulement si le PongCanvas est un SinglePlayerTrainingPongCanvas.
        if (pongCanvas instanceof SinglePlayerTrainingPongCanvas) {

            final SinglePlayerTrainingPongCanvas trainingPongCanvas = (SinglePlayerTrainingPongCanvas) pongCanvas;
            // on utilise la classe PongColorChooser pour presenter une fenetre pour choisir les couleurs et modifier
            // la couleur du mur.
            final PongColorChooser wallColorChooser = new PongColorChooser(lang.get("bgColorChangeTitle"), trainingPongCanvas.getBackgroundColor());
            wallColorChooser.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    trainingPongCanvas.setWallColor(wallColorChooser.getColorChooser().getColor());
                    pongCanvas.repaint();
                }
            });
            wallColorChooser.setVisible(true);
        }
    }

    /**
     * Modifie la position du mur dans un SinglePlayerTrainingPongCanvas.
     */
    public void setWallPosition(double position) {

        // On peut changer la couleur du mur seulement si le PongCanvas est un SinglePlayerTrainingPongCanvas.
        if (pongCanvas instanceof SinglePlayerTrainingPongCanvas) {

            SinglePlayerTrainingPongCanvas trainingPongCanvas = (SinglePlayerTrainingPongCanvas) pongCanvas;
            trainingPongCanvas.getWall().setPaddleY(position);
            // si la taille du jeu change, il faut maintenir la proportion du mur
            trainingPongCanvas.setWallXRatio(position / trainingPongCanvas.getHeight());
            pongCanvas.repaint();
        }
    }

    /**
     * Modifie la couleur des briques dans un SinglePlayerBreakoutPongCanvas.
     */
    public void setBrickColor() {

        // On peut changer la couleur des briques seulement si le PongCanvas est un SinglePlayerBreakoutPongCanvas.
        if (pongCanvas instanceof SinglePlayerBreakoutPongCanvas) {

            final SinglePlayerBreakoutPongCanvas arkanoidPongCanvas = (SinglePlayerBreakoutPongCanvas) pongCanvas;

            // on utilise la classe PongColorChooser pour presenter une fenetre pour choisir les couleurs et modifier
            // la couleur des briques.
            final PongColorChooser brickColorChooser = new PongColorChooser(lang.get("bgColorChangeTitle"), arkanoidPongCanvas.getBrickColor());
            brickColorChooser.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    arkanoidPongCanvas.setBrickColor(brickColorChooser.getColorChooser().getColor());
                    pongCanvas.repaint();
                }
            });
            brickColorChooser.setVisible(true);
        }
    }

    /**
     * Modifie la couleur des missiles dans un TwoPlayerBattlePongCanvas.
     */
    public void setMissileColor() {

        // On peut changer la couleur des missiles seulement si le PongCanvas est un TwoPlayerBattlePongCanvas.
        if (pongCanvas instanceof TwoPlayerBattlePongCanvas) {

            final TwoPlayerBattlePongCanvas battlePongCanvas = (TwoPlayerBattlePongCanvas) pongCanvas;

            // on utilise la classe PongColorChooser pour presenter une fenetre pour choisir les couleurs et modifier
            // la couleur des missiles.
            final PongColorChooser missileColorChooser = new PongColorChooser(lang.get("bgColorChangeTitle"), battlePongCanvas.getMissileColor());
            missileColorChooser.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    battlePongCanvas.setMissileColor(missileColorChooser.getColorChooser().getColor());
                    pongCanvas.repaint();
                }
            });
            missileColorChooser.setVisible(true);
        }
    }

    /**
     * Modifie la taille de la balle.
     * @param size
     */
    public void setBallSize(double size) {

        Ball ball = pongCanvas.getBall();
        ball.setBallDiameter(size);
        // Si la balle depasse la limite inferieure du jeu, il faut la deplacer pour qu'elle soit dans les limites.
        if (ball.getLowerLimit() >= pongCanvas.getHeight() - ((int) MAXIMUM_BALL_SPEED + 1)) {
            ball.setBallY(pongCanvas.getHeight() - size - ((int) MAXIMUM_BALL_SPEED + 1));
        }
        // Si la balle commence a rentrer a l'interieur de la/des raquette(s), il faut la deplacer pour qu'elle ne soit
        // plus dedans
        if (ball.getRightLimit() >= pongCanvas.getWidth() - pongCanvas.getBallOffset() - PADDING) {
            ball.setBallX(pongCanvas.getWidth() - pongCanvas.getBallOffset() - ball.getBallDiameter() - PADDING);
        }
    }

    /**
     * Redessine le PongCanvas.
     */
    public void repaintPongCanvas() {

        pongCanvas.repaint();
    }

    /**
     * Active/desactive la gravite.
     */
    public void toggleGravityEnabled() {

        pongCanvas.getBall().setGravity(!pongCanvas.getBall().isGravityEnabled());
    }

    /**
     * Active/desactive la vitesse terminale.
     */
    public void toggleTerminalVelocityEnabled() {

        pongCanvas.getBall().setTerminalVelocityEnabled(!pongCanvas.getBall().isTerminalVelocityEnabled());
    }

    /**
     * Change le multiplicateur de la vitesse terminale.
     */
    public void setTerminalMultiplier(double terminalMultiplier) {

        pongCanvas.getBall().setTerminalMultiplier(terminalMultiplier);
    }

    /**
     * Change le diviseur de la gravite.
     */
    public void setGravityCoefficient(double gravityCoefficient) {

        pongCanvas.getBall().setGravityCoefficient(gravityCoefficient);
    }

    /**
     * Active/desactive le score dans un TwoPlayerPongCanvas.
     */
    public void toggleScoreEnabled() {

        // On peut changer si le score est active seulement si le PongCanvas est un TwoPlayerPongCanvas.
        if (pongCanvas instanceof TwoPlayerPongCanvas) {

            TwoPlayerPongCanvas twoPlayerPongCanvas = (TwoPlayerPongCanvas) pongCanvas;
            twoPlayerPongCanvas.setScoreEnabled(!twoPlayerPongCanvas.isScoreEnabled());
            twoPlayerPongCanvas.repaint();
            MINIMUM_SCORE = twoPlayerPongCanvas.getPlayerScore(1) >= twoPlayerPongCanvas.getPlayerScore(2) ?
                            twoPlayerPongCanvas.getPlayerScore(1) + 1 :
                            twoPlayerPongCanvas.getPlayerScore(2) + 1;
        }
    }

    /**
     * Change le score maximal.
     */
    public void setMaxScore(int maxScore) {

        // On peut changer le score maximal seulement si le PongCanvas est un TwoPlayerPongCanvas.
        if (pongCanvas instanceof TwoPlayerPongCanvas) {

            TwoPlayerPongCanvas twoPlayerPongCanvas = (TwoPlayerPongCanvas) pongCanvas;
            twoPlayerPongCanvas.setMaxScore(maxScore);
        }
    }

    /**
     * Change la difficulte de l'IA.
     */
    public void setAiDifficulty(int aiDifficulty) {

        // On peut changer la difficulte de l'IA seulement si le PongCanvas est un TwoAIAndHumanPlayerPongCanvas.
        if (pongCanvas instanceof TwoAIAndHumanPlayerPongCanvas) {

            TwoAIAndHumanPlayerPongCanvas aiPongCanvas = (TwoAIAndHumanPlayerPongCanvas) pongCanvas;
            aiPongCanvas.getAI().setAiDifficulty(aiDifficulty);
        }
    }
}