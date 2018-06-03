// File: TrainingPongCanvas.java
// Author: Leo
// Date Created: 4/9/15 4:35 PM
// Version: 0.1a

package pong.mode;

import pong.collision.PongPaddleCollisionDetector;
import pong.collision.PongWallCollisionDetector;
import pong.control.PongPreferencesEditor;
import pong.lang.Language;
import pong.paddle.Paddle;
import pong.paddle.PaddleMode;
import pong.screen.Pong;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Mode de jeu ou le joueur pourra s'entrainer contre un mur. Le mur est reglable en position et hauteur et le joueur
 * recoit des points qui dependent de la taille/la vitesse de la balle et la hauteur du mur.
 */
public class SinglePlayerTrainingPongCanvas extends PongCanvas {

    /**
     * La raquette du joueur.
     */
    private Paddle paddle;

    /**
     * Le mur qui est compose d'une raquette immobile.
     */
    private Paddle wall;

    /**
     * La couleur du mur.
     */
    private Color wallColor;

    /**
     * Le nombre de coups reussis.
     */
    private int hits = 0;

    /**
     * Le nombre de coups rates.
     */
    private int misses = 0;

    /**
     * Le nombre maximal de coups rates.
     */
    private final static int MAX_MISSES = 5;

    /**
     * Le score final de l'utilisateur.
     */
    private int finalScore = 0;

    /**
     * Le score actuel de l'utilisateur.
     */
    private int score = 0;

    /**
     * Le rapport entre la coordonnee x du mur et la hauteur du jeu.
     */
    private double wallXRatio = 0.125;

    /**
     * Le rapport entre la hauteur du mur et la hauteur du jeu.
     */
    private double wallHeightRatio = 0.75;

    /**
     * La police de caracteres qui sera utilisee pour afficher les informations (coups reussis/rates, score, etc.)
     */
    private String scoreFont = Font.SANS_SERIF;

    /**
     * Map qui permet de chercher un terme dans la langue voulue.
     */
    private Map<String, String> lang = Language.getLanguageMap(Language.getLoc());

    /**
     * Le texte qui indiquera le nombre de coups reussis.
     */
    private String hitString = lang.get("hitString");

    /**
     * Le texte qui indiquera le nombre de coups rates.
     */
    private String missString = lang.get("missString");

    /**
     * Le texte qui indiquera le score actuel.
     */
    private String scoreString = lang.get("scoreString");

    /**
     * Le texte qui indiquera le nombre de vies restantes.
     */
    private String livesString = lang.get("livesString");

    /**
     * Tableau contenant toutes les informations (coups reussis, coups rates, vies restantes, score, temps restant) et
     * leurs textes correspondants.
     */
    private String[] allInfo;

    /**
     * La taille de la police de caracteres des informations.
     */
    private int scoreFontSize = 15;

    /**
     * Le Timer pour compter le temps restant.
     */
    private Timer scoreTimer;

    /**
     * Le Timer pour animer le deplacement de la raquette.
     */
    private Timer paddleTimer;

    /**
     * Le temps restant en secondes.
     */
    private int timeRemaining = 10 * 60;

    /**
     * Le texte qui indiquera le temps restant.
     */
    private String timeRemainingStub = lang.get("timeRemaining");

    /**
     * Le texte qui indiquera le temps restant + le temps restant.
     */
    private String timeRemainingString = timeRemainingStub + " 10:00";

    /**
     * Decrit si le temps s'est ecoule.
     */
    private boolean timeHasRunOut = false;

    /**
     * Decrit si le jeu est termine.
     */
    private boolean gameOver = false;

    /**
     * Rectangle englobant toutes les informations affichees a l'ecran.
     */
    private Rectangle infoBounds;

    public SinglePlayerTrainingPongCanvas() {

        super();
        startScoreTimer();
    }

    @Override
    public void end() {

        super.end();
        scoreTimer.stop();
        paddleTimer.stop();
    }

    /**
     * Affiche un dialogue a l'utilisateur lorsque qu'il n'a plus de vies ou le temps s'est ecoule avec un choix
     * d'arreter ou de continuer a jouer.
     */
    private void displayGameOverDialog() {

        pause();  // on arrete le jeu pendant que le dialogue est affiche.
        int choice = JOptionPane.showConfirmDialog(this, lang.get("gameOverOptions"), lang.get("gameOver"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (choice == JOptionPane.YES_OPTION) {
            resume();  // on reprend le jeu
            repaint();
        } else {
            // On signale que le jeu est termine
            gameOver = true;
            repaint();
        }
    }

    /**
     * Initialise et demarre le Timer qui s'occupera de compter le temps restant.
     */
    private void startScoreTimer() {

        scoreTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // Si le temps s'est ecoule, il faut:
                // 1. Dire que le temps s'est ecoule pour que le score final soit affiche
                // 2. Calculer le score final (qui sera egal au score actuel a l'instant ou le temps s'ecoule)
                // 3. Afficher un message si l'utilisateur veut continuer a joueur
                // 4. Arreter le Timer qui compte le temps restant
                // 5. N'executer rien d'autre
                if (timeRemaining <= 0) {
                    timeHasRunOut = true;
                    finalScore = score;
                    displayGameOverDialog();
                    scoreTimer.stop();
                    return;
                }

                // S'il ne reste plus de vies (c'est-a-dire le nombre de coup rates maximal - le nombre de coups rates
                // actuel <= 0):
                // 1. Afficher que le temps n'est plus compte
                // 2. Le score final est deja calcule (voir methode performOutOfBoundsAction(int side))
                // 3. Afficher un message si l'utilisateur veut continuer a joueur
                // 4. Arreter le Timer qui compte le temps restant
                // 5. N'executer rien d'autre
                if (MAX_MISSES - misses <= 0) {
                    timeRemainingString = timeRemainingStub + " N/A";
                    displayGameOverDialog();
                    scoreTimer.stop();
                    return;
                }
                // Chaque seconde, on enleve une seconde au temps restant et on indique le temps restant sous forme de
                // MM:SS (ex: 09:59, 07:06, etc.)
                timeRemaining--;
                long minsLeft = TimeUnit.SECONDS.toMinutes(timeRemaining);  // le nombre de minutes restantes
                long secsLeft = timeRemaining - TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(timeRemaining));  // le nombre de secondes restantes
                // On regarde s'il faut ajouter un zero devant les minutes ou les secondes (si leur longueur < 2)
                String minsLeftFinal = String.valueOf(minsLeft).length() < 2 ? "0" + String.valueOf(minsLeft) : String.valueOf(minsLeft);
                String secsLeftFinal = String.valueOf(secsLeft).length() < 2 ? "0" + String.valueOf(secsLeft) : String.valueOf(secsLeft);
                // timeRemainingString aura l'apparence suivante: "Temps restant: 07:46 s"
                timeRemainingString = String.format(timeRemainingStub + " %s:%s", minsLeftFinal, secsLeftFinal);
                repaint(infoBounds);
            }
        });
        scoreTimer.start();
    }

    /**
     * Calcule l'augmentation du score et l'ajoute au score actuel.
     */
    private void calculateScore() {

        // Le score depend de trois facteurs:
        // 1. La vitesse de la balle (le score est directement proportionnel a la vitesse de la balle)
        // 2. La hauteur du mur par rapport a la hauteur du jeu (le score est proportionnel au rapport entre la hauteur
        //    du mur et la hauteur du jeu a la puissance 5, puisqu'un mur plus petit est BEAUCOUP plus difficile a
        //    toucher qu'un mur plus grand)
        // 3. Le diametre de la balle (le score est inversement proportionnel au diametre de la balle/20)
        score += (int) Math.round(1.0 * ball.getSpeed() * 10e2 * Math.pow((1 - (wall.getHeight() / getHeight())), 5) / (getBall().getBallDiameter() / 20));
    }

    @Override
    public Paddle[] getPaddles() {

        return new Paddle[] {paddle, wall};
    }

    @Override
    void initPaddles() {

        paddle = new Paddle(this, INITIAL_PADDLE1_X, INITIAL_PADDLE1_Y, INITIAL_PADDLE_WIDTH, INITIAL_PADDLE_HEIGHT, Paddle.LEFT, PaddleMode.HUMAN);
        // Par defaut, le mur occupe 3/4 de la hauteur du jeu et est centre.
        wall = new Paddle(this, getWidth() - 7, (getHeight() / 2) - ((int) (getHeight() * 0.75) / 2), 5, (int) (getHeight() * 0.75), Paddle.RIGHT, PaddleMode.NONE);

        paddleTimer = paddle.getPlayerModeTimer(KeyEvent.VK_UP, KeyEvent.VK_DOWN);
        paddleTimer.start();
    }

    @Override
    protected void drawCanvas(Graphics g) {

        super.drawCanvas(g);
        // On regroupe ensemble toutes les informations pour pouvoir les dessiner plus facilement.
        allInfo = new String[] {String.format(hitString, hits),
                                String.format(missString, misses),
                                String.format(scoreString, MAX_MISSES - misses > 0 && !timeHasRunOut ? score : finalScore),
                                String.format(livesString, misses <= MAX_MISSES ? MAX_MISSES - misses : 0),
                                timeRemainingString};
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(getForegroundColor());
        g2d.setFont(new Font(scoreFont, Font.PLAIN, scoreFontSize));
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2d.fill(paddle);

        // On calcule la hauteur d'un String qui utilise un caractere haut et un caractere bas ("y)
        float stringHeight = (float) g2d.getFontMetrics().getStringBounds("\"y", g2d).getHeight();
        // On calcule la largeur de l'information la plus longue
        float widestStringWidth = (float) g2d.getFontMetrics().getStringBounds(timeRemainingString, g2d).getWidth();
        // On dessine les informations l'une au-dessous de l'autre, centrees dans le cote du joueur
        for (int i = 0; i < allInfo.length; i++) {
            g2d.drawString(allInfo[i], getWidth() / 4 - widestStringWidth / 2, stringHeight * i + 30);
        }
        // On recalcule le rectangle ou se trouvent les informations
        infoBounds = new Rectangle2D.Float(getWidth() / 4 - widestStringWidth / 2, 0, widestStringWidth, stringHeight * allInfo.length + 30).getBounds();
        g2d.setColor(wallColor);
        g2d.fill(wall);

        // Si le jeu est termine, on affiche "JEU TERMINE" au centre du jeu
        if (gameOver) {
            g2d.setColor(getForegroundColor());
            g2d.setFont(new Font(scoreFont, Font.PLAIN, 50));
            String gameOverString = lang.get("gameOver").toUpperCase();
            int newStringWidth = (int) g2d.getFontMetrics().getStringBounds(gameOverString, g2d).getWidth();
            // Calcul des coordonnees pour centrer le texte
            int xstart = getWidth() / 2 - newStringWidth / 2;
            int ystart = getHeight() / 2;
            g2d.drawString(gameOverString, xstart, ystart);
        }
        g2d.dispose();
    }

    @Override
    protected void addListeners() {

        // Pour une explication de ceci, voir la classe TwoAIAndHumanPongCanvas.
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {

                if (!isPaused()) {
                    Rectangle oldBounds = paddle.getBounds();
                    if (e.getY() <= getHeight() - PongPreferencesEditor.PADDING - paddle.getHeight() / 2 && e.getY() > PongPreferencesEditor.PADDING + paddle.getHeight() / 2) {
                        paddle.setPaddleY(e.getY() - paddle.getHeight() / 2);
                    }
                    if (e.getY() >= getHeight() - PongPreferencesEditor.PADDING - paddle.getHeight() / 2) {
                        paddle.setPaddleY(getHeight() - PongPreferencesEditor.PADDING - paddle.getHeight());
                    }
                    if (e.getY() <= PongPreferencesEditor.PADDING) {
                        paddle.setPaddleY(PongPreferencesEditor.PADDING);
                    }
                    repaint(oldBounds);
                    repaint(paddle.getBounds());
                }
            }
        });

        // Lorsque l'utilisateur change la taille de la fenetre
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {

                // Il faut que la coordonne y et la hauteur du mur par rapport a la hauteur du jeu restent les memes.
                // Il faut donc multiplier la hauteur du jeu par le bon rapport pour obtenir la nouvelle coordonnee y
                // ou hauteur.
                wall.setPaddleX(e.getComponent().getWidth() - 7);
                wall.setPaddleHeight(e.getComponent().getHeight() * wallHeightRatio);
                wall.setPaddleY(e.getComponent().getHeight() * wallXRatio);
                // Si la raquette est plus grande que le jeu, il faut diminuer sa taille.
                for (Paddle paddle : getPaddles()) {
                    if (paddle.getHeight() > getHeight() - 2 * PongPreferencesEditor.PADDING) {
                        paddle.setPaddleHeight(getHeight() - 2 * PongPreferencesEditor.PADDING);
                    }
                }
                // Si la raquette se trouve en-dessous de la limite inferieure du jeu, il faut la monter.
                if (paddle.getLowerLimit() >= e.getComponent().getHeight() - PongPreferencesEditor.PADDING) {
                    paddle.setPaddleY(e.getComponent().getHeight() - PongPreferencesEditor.PADDING - paddle.getHeight());
                }
                repaint();
            }
        });
    }

    @Override
    public PongMode getMode() {

        return PongMode.SINGLE_PLAYER_TRAINING;
    }

    @Override
    protected void addCollisionDetectors() {

        // On doit faire le service depuis la droite, donc on dit au detecteur de collisions entre la balle et
        // les murs (du jeu) de faire le service depuis la droite.
        pongWallCollisionDetector = new PongWallCollisionDetector(this, Paddle.RIGHT);

        // Pour la raquette
        addPaddleCollisionDetector(new PongPaddleCollisionDetector(this, paddle));

        // Lorsque le joueur touche le mur, il faut:
        // 1. Incrementer le nombre de coups reussis
        // 2. Recalculer le score
        // 3. Redessiner les informations
        addPaddleCollisionDetector(new PongPaddleCollisionDetector(this, wall) {
            @Override
            public void performCustomAction() {
                hits++;
                calculateScore();
                repaint(infoBounds);
            }
        });
    }

    @Override
    public void performOutOfBoundsAction(int side) {

        // Si la balle sort du jeu, on augmente de nombre de coups rates.
        misses++;
        // Si le nombre de coups rates est egal au nombre de coups rates maximal (i.e. il ne reste plus de vies), il
        // faut calculer le score final puisque le jeu est termine
        if (misses == MAX_MISSES) {
            finalScore = score;
        }
        repaint(infoBounds);
    }

    @Override
    public boolean isGameOver() {

        if (gameOver) {
            // Le jeu a change d'etat, donc on dit a tous les ChangeListeners attaches a ce PongCanvas d'executer leur
            // methode stateChanged.
            fireStateChanged();
            return gameOver;
        } else {
            return false;
        }
    }

    @Override
    public void pause() {

        super.pause();
        // Il faut arreter le Timer pour compter le temps restant.
        scoreTimer.stop();
    }

    @Override
    public void resume() {

        super.resume();
        // Si le jeu n'est pas termine (i.e. il reste encore des vies et le temps ne s'est pas ecoule), il faut demarrer
        // le Timer pour compter le temps restant.
        if (timeRemaining > 0 && MAX_MISSES - misses > 0 && !scoreTimer.isRunning()) {
            scoreTimer.start();
        }
    }

    /**
     * Change la hauteur du mur.
     */
    public void setWallHeight(double height) {

        wall.setPaddleHeight(height);
        // Il faut recalculer le rapport entre la hauteur du mur et la hauteur du jeu avec la nouvelle hauteur.
        wallHeightRatio = height / getHeight();
    }

    /**
     * Cherche la hauteur du mur.
     */
    public double getWallHeight() {

        return wall.getPaddleHeight();
    }

    /**
     * Cherche le mur.
     */
    public Paddle getWall() {

        return wall;
    }

    /**
     * Change la couleur du mur.
     */
    public void setWallColor(Color wallColor) {

        this.wallColor = wallColor;
    }

    /**
     * Change le rapport entre la coordonee x de la balle et la hauteur du jeu.
     */
    public void setWallXRatio(double wallXRatio) {
        this.wallXRatio = wallXRatio;
    }
}