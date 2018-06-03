// File: PongCanvas.java
// Author: Leo Horne
// Date Created: Saturday, March 7, 2015
// Version: 0.3a

/* Improvements in this version:
    * New getters and setters for access to private variables
    * New functions (reset()) to accommodate PongControls
*/

package pong.mode;

import pong.collision.PongPaddleCollisionDetector;
import pong.control.PongPreferencesEditor;
import pong.lang.Language;
import pong.paddle.Paddle;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Map;

/**
 * Classe abstraite definissant un PongCanvas avec deux joueurs.
 */
public abstract class TwoPlayerPongCanvas extends PongCanvas {

    /**
     * La raquette du joueur de gauche.
     */
    protected Paddle paddle1;

    /**
     * La raquette du joueur de gauche.
     */
    protected Paddle paddle2;

    /**
     * Le score du joueur de gauche.
     */
    private Integer player1Score = 0;

    /**
     * Le score du joueur de droite.
     */
    private Integer player2Score = 0;

    /**
     * Le score maximal.
     */
    private int maxScore = 10;

    /**
     * La taille de la police du score.
     */
    private int scoreFontSize = 50;

    /**
     * La police de caracteres du score.
     */
    private String scoreFont = "Andale Mono";

    /**
     * Map qui permet de chercher un terme dans la langue voulue.
     */
    private Map<String, String> lang = Language.getLanguageMap(Language.getLoc());

    /**
     * Le texte qui sera affiche si un joueur gagne.
     */
    private String winnerString = lang.get("winnerString");

    /**
     * Decrit si le score est active.
     */
    private boolean scoreEnabled = true;

    public TwoPlayerPongCanvas() {
        super();
        // Si la taille de la fenetre est changee, il faut adapter la position de la raquette de droite pour qu'elle soit
        // toujours dans les limites du jeu et contre la limite droite du jeu.
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {

                paddle2.setPaddleX(e.getComponent().getWidth() - paddle2.getWidth() - 10);
                for (Paddle paddle : getPaddles()) {
                    if (paddle.getHeight() > getHeight() - 2 * PongPreferencesEditor.PADDING) {
                        paddle.setPaddleHeight(getHeight() - 2 * PongPreferencesEditor.PADDING);
                    }
                    if (paddle.getLowerLimit() >= e.getComponent().getHeight() - PongPreferencesEditor.PADDING) {
                        paddle.setPaddleY(e.getComponent().getHeight() - PongPreferencesEditor.PADDING - paddle.getHeight());
                    }
                }
                repaint();
            }
        });
    }

    @Override
    public Paddle[] getPaddles() {

        return new Paddle[]{paddle1, paddle2};
    }

    @Override
    protected void drawCanvas(Graphics g) {

        super.drawCanvas(g);
        Graphics2D g2d = (Graphics2D) g;

        // On veut la meilleure qualite possible.
        g2d.setPaint(foregroundColor);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        // On dessine les raquettes
        g2d.fill(paddle1);
        g2d.fill(paddle2);

        g2d.setFont(new Font(scoreFont, Font.PLAIN, scoreFontSize));
        // On dessine le score s'il est active
        if (scoreEnabled) {
            g2d.drawString(player1Score.toString(), getWidth() / 4 - scoreFontSize / 2, scoreFontSize);
            g2d.drawString(player2Score.toString(), getWidth() * 3 / 4 - scoreFontSize / 2, scoreFontSize);
        }

        // Si le joueur de gauche a gagne (son score est egal au score maximal), on dessine le mot "GAGNANT!" sur
        // son cote du jeu
        if (scoreEnabled && player1Score == maxScore) {
            g2d.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 30));
            int stringWidth = (int) g2d.getFontMetrics().getStringBounds(winnerString, g2d).getWidth();     // la largeur de winnerString
            int stringHeight = (int) g2d.getFontMetrics().getStringBounds(winnerString, g2d).getHeight();   // la hauteur de winnerString
            int xstart = getWidth() / 4 - stringWidth / 2;                                                  // on centre winnerString horizontalement...
            int ystart = getHeight() / 2 - stringHeight / 2;                                                // ...et verticalement.
            g2d.drawString(winnerString, xstart, ystart);

        // idem pour le joueur de droite
        } else if (scoreEnabled && player2Score == maxScore) {
            g2d.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 30));
            int stringWidth = (int) g2d.getFontMetrics().getStringBounds(winnerString, g2d).getWidth();
            int stringHeight = (int) g2d.getFontMetrics().getStringBounds(winnerString, g2d).getHeight();
            int xstart = 3 * getWidth() / 4 - stringWidth / 2;
            int ystart = getHeight() / 2 - stringHeight / 2;
            g2d.drawString(winnerString, xstart, ystart);
        }

        // On dessine les autres choses
        drawCustomItems(g2d);
        g2d.dispose();
    }

    /**
     * Permet aux classes qui heritent de cette classe de dessiner des choses autres que les raquettes et le score.
     */
    protected void drawCustomItems(Graphics2D g2d) {}

    @Override
    protected void addCollisionDetectors() {

        for (Paddle paddle : getPaddles()) {
            addPaddleCollisionDetector(new PongPaddleCollisionDetector(this, paddle));
        }
    }

    @Override
    public void performOutOfBoundsAction(int side) {

        // Si un joueur perd la balle, il faut incrementer le score de l'autre joueur.
        if (side == Paddle.RIGHT) {
            player1Score++;
            repaint();
        } else {
            player2Score++;
            repaint();
        }
    }

    @Override
    public boolean isGameOver() {

        // Le jeu est termine si le score d'un des joueurs est egal au score maximal.
        if (scoreEnabled && (player1Score >= maxScore || player2Score >= maxScore)) {
            // Le jeu a change d'etat, donc on dit a tous les ChangeListeners attaches a ce PongCanvas d'executer leur
            // methode stateChanged.
            fireStateChanged();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Change/definit la raquette de gauche.
     */
    public void setPaddle1(Paddle paddle1) {

        this.paddle1 = paddle1;
    }

    /**
     * Change/definit la raquette de droite.
     */
    public void setPaddle2(Paddle paddle2) {

        this.paddle2 = paddle2;
    }

    /**
     * Change si le score est active.
     */
    public void setScoreEnabled(boolean scoreEnabled) {

        this.scoreEnabled = scoreEnabled;
        // Il faut regarder si un des joueurs a atteint le score maximal
        // car sinon, si l'utilisateur active le score, un des deux joueurs gagnera tout de suite. Il faut donc
        // adapter le score maximal au score des joueurs lorsque l'utilisateur active le score (car le joueur ne connaissait
        // pas le score quand il etait desactive et il ne savait pas s'il avait gagne).
        if (player1Score >= maxScore || player2Score >= maxScore) {
            // Si c'est le cas, le score maximal sera le score du joueur avec le plus de points + 1 pour laisser continuer
            // le jeu. L'utilisateur pourra regler cela comme il veut ensuite.
            maxScore = player1Score >= maxScore ? player1Score + 1 : player2Score + 1;
            // Si le score maximal ainsi calcule est plus grand le score maximal possible (definit dans PongPreferencesEditor),
            // cela n'est pas valide et on doit remettre le score a sa valeur maximale possible.
            if (maxScore > PongPreferencesEditor.MAXIMUM_SCORE) {
                maxScore = PongPreferencesEditor.MAXIMUM_SCORE;
            }
        }
    }

    /**
     * Decrit si le score est active.
     */
    public boolean isScoreEnabled() {

        return scoreEnabled;
    }

    /**
     * Change le score maximal.
     */
    public void setMaxScore(int maxScore) {

        this.maxScore = maxScore;
    }

    /**
     * Cherche le score maximal.
     */
    public int getMaxScore() {

        return maxScore;
    }

    /**
     * Cherche le score du joueur voulu (1 = joueur de gauche, 2 = joueur de droite).
     */
    public int getPlayerScore(int playerNumber) {

        return playerNumber == 1 ? player1Score : player2Score;
    }
}