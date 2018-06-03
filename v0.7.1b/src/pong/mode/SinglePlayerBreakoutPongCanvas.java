package pong.mode;

import pong.collision.PongPaddleCollisionDetector;
import pong.collision.PongWallCollisionDetector;
import pong.control.PongPreferencesEditor;
import pong.lang.Language;
import pong.paddle.Paddle;
import pong.paddle.PaddleMode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * Classe qui decrit le mode casse-briques.
 */
public class SinglePlayerBreakoutPongCanvas extends PongCanvas {

    /**
     * Le nombre de lignes de briques.
     */
    private int rows;


    /**
     * Le nombre de colonnes de briques.
     */
    private int cols;

    /**
     * Le score du joueur.
     */
    private int score = 0;

    /**
     * Le nombre de vies restantes.
     */
    private int livesLeft = 3;

    /**
     * Map qui permet de chercher un terme dans la langue voulue.
     */
    private Map<String, String> lang = Language.getLanguageMap(Language.getLoc());

    /**
     * Texte pour afficher le score.
     */
    private String scoreString = lang.get("scoreString");

    /**
     * Texte pour afficher le nombre de vies restantes.
     */
    private String livesString = lang.get("livesString");

    /**
     * La raquette de l'utilisateur.
     */
    private Paddle playerPaddle;

    /**
     * Tableau dynamique deux-dimensionnel qui va contenir les briques (qui sont en fait des raquettes immobiles)
     */
    private ArrayList<ArrayList<Paddle>> bricks;

    /**
     * L'espace entre les briques.
     */
    private double interSpace;

    /**
     * La hauteur des briques.
     */
    private double brickHeight;

    /**
     * La position x ou il faudra commencer a dessiner les briques.
     */
    private double initialX;

    /**
     * La couleur des briques.
     */
    private Color brickColor;

    /**
     * Le Timer pour animer les deplacements de la raquette de l'utilisateur.
     */
    private Timer paddleTimer;

    /**
     * Rectangle englobant la partie de l'ecran ou les informations (score, vies restantes) seront affichees.
     */
    private Rectangle infoBounds;

    /**
     * La police de caracteres du score/des informations.
     */
    private String scoreFont = Font.SANS_SERIF;

    /**
     * La taille des informations.
     */
    private int scoreFontSize = 15;

    /**
     * Decrit si le jeu est termine.
     */
    private boolean gameOver = false;

    /**
     * Permet de construire un SinglePlayerBreakoutPongCanvas a partir d'un nombre de lignes et de colonnes.
     */
    public SinglePlayerBreakoutPongCanvas(int rows, int cols) {

        super();  // on construit la superclasse
        this.rows = rows;
        this.cols = cols;

        interSpace = 3;
        // Pour calculer la hauteur des briques, il faut prendre en compte les espaces entre les briques.
        brickHeight = (getHeight() - ((rows + 1) * interSpace)) / rows;
        // Idem pour calculer la position x ou il faut commencer a dessiner les briques. Les briques ont la meme largeur
        // qu'une raquette standard.
        initialX = getWidth() - cols * interSpace - cols * INITIAL_PADDLE_WIDTH;
        // Lorsque la fenetre change de taille, la balle ne doit rentrer a l'interieur des briques. Il faut donc dire a
        // la superclasse que l'ecart entre les briques et la balle n'est pas le meme.
        setBallOffset(getWidth() - initialX);

        // On utilise deux boucles for imbriques pour iterer a travers le tableau des briques, initialiser chaque brique
        // et la dessiner a l'ecran.
        double currentY = interSpace;  // la position y actuelle ou il faut dessiner la brique.
        int initialValue = 1;  // la valeur initiale de la brique.

        // On itere a travers les lignes de briques pour initialiser l'ArrayList a qui represente la ligne et le mettre
        // dans le tableau des briques. On incremente aussi la position y ou il faut dessiner les briques a la fin.
        for (int row = 0; row < rows; row++) {
            double currentX = initialX;  // la position x actuelle ou il faut commencer a dessiner les briques.
            int value = initialValue;
            ArrayList<Paddle> currentRow = new ArrayList<Paddle>();
            bricks.add(row, currentRow);

            // On itere a travers chaque ligne pour initialiser chaque brique de la ligne et on calcule sa position x en
            // incrementant la valeur currentX a chaque fois. On calcule aussi la valeur de chaque brique qui incremente
            // de 2 points par colonne.
            for (int col = 0; col < cols; col++) {
                Paddle brick = new Paddle(this, currentX, currentY, INITIAL_PADDLE_WIDTH, brickHeight, Paddle.RIGHT, PaddleMode.NONE);
                brick.setValue(value);
                currentRow.add(col, brick);
                currentX += INITIAL_PADDLE_WIDTH + interSpace;
                value += 2;
            }
            currentY += brickHeight + interSpace;
        }

        // On enleve tous les detecteurs de collisions qui ont etes initialises dans la superclasse car les briques etaient
        // null.
        removeAllPaddleCollisionDetectors();
        // On les reajoute.
        addCollisionDetectors();
    }

    @Override
    public void end() {

        super.end();
        // Il faut arreter le Timer d'animation des deplacements de la raquette.
        paddleTimer.stop();
    }

    @Override
    public Paddle[] getPaddles() {

        // Pour chercher tous les raquettes, il faut transformer le tableau des briques en tableau unidimensionnel et
        // ajouter la raquette du joueur.
        // Pour faire cela, il faut transformer l'ArrayList deux-dimensionnel contenant les briques en tableau simple
        // unidimensionnel. Pour faire cela, il faut calculer le nombre de briques + la raquette de l'utilisateur car
        // ce sera necessaire pour initialiser le tableau simple.
        int numberOfPaddles = 0;
        for (ArrayList<Paddle> brickRow : bricks) {
            for (Iterator<Paddle> iterator = brickRow.iterator(); iterator.hasNext(); iterator.next()) {
                numberOfPaddles++;
            }
        }
        Paddle[] paddles = new Paddle[numberOfPaddles + 1];  // On peut maintenant initialiser le tableau simple.

        // On itere a travers chaque ligne de briques et on le copie sous forme de tableau simple dans le tableau paddles.
        int currentPosition = 0;  // l'indice ou il faudra copier une ligne de briques.

        // On itere a travers chaque ligne de briques et on le copie sous forme de tableau simple dans le tableau paddles.
        for (ArrayList<Paddle> brickRow : bricks) {

            Paddle[] brickRowArray = new Paddle[brickRow.size()];
            brickRowArray = brickRow.toArray(brickRowArray);
            System.arraycopy(brickRowArray, 0, paddles, currentPosition, brickRowArray.length);
            currentPosition += brickRowArray.length;
        }

        // La derniere raquette du tableau est la raquette de l'utilisateur.
        paddles[paddles.length - 1] = playerPaddle;

        return paddles;
    }

    @Override
    void initPaddles() {

        playerPaddle = new Paddle(this, INITIAL_PADDLE1_X, INITIAL_PADDLE1_Y, INITIAL_PADDLE_WIDTH, INITIAL_PADDLE_HEIGHT, Paddle.LEFT, PaddleMode.HUMAN);
        bricks = new ArrayList<ArrayList<Paddle>>();
        brickColor = Color.RED;

        paddleTimer = playerPaddle.getPlayerModeTimer(KeyEvent.VK_UP, KeyEvent.VK_DOWN);
        paddleTimer.start();
    }

    @Override
    protected void drawCanvas(Graphics g) {

        super.drawCanvas(g);
        String[] allInfo = new String[] {String.format(scoreString, score), String.format(livesString, livesLeft)};
        Graphics2D g2d = (Graphics2D) g.create();
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
        g2d.setColor(getForegroundColor());
        g2d.fill(playerPaddle);

        // Voir meme ligne dans la classe SinglePlayerTrainingPongCanvas.
        float stringHeight = (float) g2d.getFontMetrics().getStringBounds("\"y", g2d).getHeight();
        float widestStringWidth = (float) g2d.getFontMetrics().getStringBounds(livesString, g2d).getWidth();

        for (int i = 0; i < allInfo.length; i++) {

            g2d.drawString(allInfo[i], getWidth() / 4 - widestStringWidth / 2, stringHeight * i + 20);
        }

        infoBounds = new Rectangle2D.Float(getWidth() / 4 - widestStringWidth / 2, 0, widestStringWidth, stringHeight * allInfo.length + 20).getBounds();

        // On dessine les briques.
        g2d.setColor(brickColor);

        for (ArrayList<Paddle> brickRow : bricks) {
            for (Paddle brick : brickRow) {
                g2d.fill(brick);
            }
        }

        // Si le jeu est termine, on dessine les mots "JEU TERMINE" au milieu de l'ecran.
        if (gameOver) {
            g2d.setColor(getForegroundColor());
            g2d.setFont(new Font(scoreFont, Font.PLAIN, 50));
            String gameOverString = lang.get("gameOver").toUpperCase();
            int newStringWidth = (int) g2d.getFontMetrics().getStringBounds(gameOverString, g2d).getWidth();
            int newStringHeight = (int) g2d.getFontMetrics().getStringBounds(gameOverString, g2d).getHeight();
            int xstart = getWidth() / 2 - newStringWidth / 2;
            int ystart = getHeight() / 2 - newStringHeight / 2;
            g2d.drawString(gameOverString, xstart, ystart);
        }

        g2d.dispose();
    }

    @Override
    protected void addListeners() {

        // Voir la classe TwoAIAndHumanPongCanvas pour une explication de ceci.
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {

                if (!isPaused()) {
                    Rectangle oldBounds = playerPaddle.getBounds();
                    if (e.getY() <= getHeight() - PongPreferencesEditor.PADDING - playerPaddle.getHeight() / 2 && e.getY() > PongPreferencesEditor.PADDING + playerPaddle.getHeight() / 2) {
                        playerPaddle.setPaddleY(e.getY() - playerPaddle.getHeight() / 2);
                    }
                    if (e.getY() >= getHeight() - PongPreferencesEditor.PADDING - playerPaddle.getHeight() / 2) {
                        playerPaddle.setPaddleY(getHeight() - PongPreferencesEditor.PADDING - playerPaddle.getHeight());
                    }
                    if (e.getY() <= PongPreferencesEditor.PADDING) {
                        playerPaddle.setPaddleY(PongPreferencesEditor.PADDING);
                    }
                    repaint(oldBounds);
                    repaint(playerPaddle.getBounds());
                }
            }
        });

        // Si la taille de la fenetre est changee, il faut adapter la taille et la position de tous les briques.
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {

                // Pour recalculer la position et la hauteur des briques, on utilise pratiquement le meme algorithme que
                // dans le constructeur, mais on itere a travers les lignes a l'envers car sinon la position des briques
                // sera inversee et on n'a pas besoin d'instantier les briques.
                brickHeight = (getHeight() - ((rows + 1) * interSpace)) / rows;
                initialX = getWidth() - interSpace - INITIAL_PADDLE_WIDTH;

                double currentY = interSpace;

                for (ArrayList<Paddle> brickRow : bricks) {
                    double currentX = initialX;

                    for (int i = brickRow.size() - 1; i >= 0; i--) {

                        brickRow.get(i).setPaddleHeight(brickHeight);
                        brickRow.get(i).setPaddleX(currentX);
                        brickRow.get(i).setPaddleY(currentY);
                        currentX -= INITIAL_PADDLE_WIDTH + interSpace;

                    }

                    currentY += brickHeight + interSpace;
                }
                // Si la raquette de l'utilisateur se trouve en-dessous de la limite inferieure du jeu, il faut la monter.
                if (playerPaddle.getLowerLimit() >= e.getComponent().getHeight() - PongPreferencesEditor.PADDING) {
                    playerPaddle.setPaddleY(e.getComponent().getHeight() - PongPreferencesEditor.PADDING - playerPaddle.getHeight());
                }
                repaint();
            }
        });
    }

    @Override
    public PongMode getMode() {

        return PongMode.SINGLE_PLAYER_BREAKOUT;
    }

    /**
     * Change la couleur des briques.
     */
    public void setBrickColor(Color brickColor) {

        this.brickColor = brickColor;
    }

    @Override
    protected void addCollisionDetectors() {

        // Il faut dire au PongWallCollisionDetector que le service doit se faire depuis le milieu.
        pongWallCollisionDetector = new PongWallCollisionDetector(this, true);

        // On itere a travers les raquettes et on ajoute un detecteur de collisions a chacun d'entre eux (y compris les
        // briques).
        for (final Paddle paddle : getPaddles()) {

            addPaddleCollisionDetector(new PongPaddleCollisionDetector(this, paddle) {
                @Override
                public void performCustomAction() {
                    // Si la raquette est une brique
                    if (!paddle.getPaddleMode().equals(PaddleMode.HUMAN)) {
                        // On enleve la brique de la ligne ou elle se trouve.
                        for (ArrayList<Paddle> brickRow : bricks) {
                            brickRow.remove(paddle);
                        }
                        // On dit prepare la brique a etre detruite definitivement (c'est-a-dire enlevee de la liste des
                        // briques et on enleve son detecteur de collisions).
                        paddle.setToBeDestroyed(true);
                        repaint(paddle.getBounds());
                        // Le joueur a touche une brique, donc il faut recalculer le score.
                        calculateScore(paddle);
                        repaint(infoBounds);
                    }
                }
            });
        }
    }

    /**
     * Calcule de combien il faut augmenter le score et ajoute cette valeur au score.
     * @param brick la brique qui a ete detruite.
     */
    private void calculateScore(Paddle brick) {

        // Le score depend de ___ facteurs:
        // 1. La valeur de la brique (le score est directement proportionnel a la valeur de la brique)
        // 2. La taille de la raquette de l'utilisateur (inversement proportionnel)
        // 3. La vitesse de la balle (proportionnel)
        // 4. Le diametre de la balle (inversement proportionnel)
        // On muliplie par 100^2 pour que le score soit egal a la valeur de la brique * 100 si les valeurs par defaut
        // sont utilisees.
        score += (int) Math.round(brick.getValue() * (1 / playerPaddle.getHeight()) * ball.getSpeed() / (ball.getBallDiameter() / 20) * 100 * 100);
    }

    @Override
    public void performOutOfBoundsAction(int side) {

        // L'utilisateur a perdu la balle, donc le nombre de vies restantes doit diminuer
        livesLeft--;
        repaint(infoBounds);
    }

    @Override
    public boolean isGameOver() {

        // Le jeu est termine s'il ne reste plus de vies ou toutes les briques sont detruites.
        if (livesLeft <= 0 || getPongPaddleCollisionDetectors().size() == 1) {
            gameOver = true;  // On signale qu'il faut dessiner "JEU TERMINE"
            // Le jeu a change d'etat, donc on dit a tous les ChangeListeners attaches a ce PongCanvas d'executer leur
            // methode stateChanged.
            fireStateChanged();
            repaint();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Cherche la couleur des briques.
     */
    public Color getBrickColor() {

        return brickColor;
    }
}