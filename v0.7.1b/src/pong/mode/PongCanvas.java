// File: PongCanvas.java
// Author: Leo
// Date Created: 4/4/15 6:22 PM

package pong.mode;

import pong.animation.BallAnimation;
import pong.ball.Ball;
import pong.collision.PongPaddleCollisionDetector;
import pong.collision.PongWallCollisionDetector;
import pong.paddle.Paddle;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Vector;

/**
 * Classe abstraite decrivant un PongCanvas generique (c'est-a-dire un PongCanvas sans raquettes, il s'agit d'un PongCanvas
 * avec une balle, des collisions avec les murs et une ligne du milieu).
 */
public abstract class PongCanvas extends JPanel {

    /**
     * La largeur initiale du PongCanvas.
     */
    public static final int INITIAL_PANEL_WIDTH = 1000;

    /**
     * La hauteur initiale du PongCanvas.
     */
    public static final int INITIAL_PANEL_HEIGHT = 600;

    /**
     * La largeur initiale des raquettes.
     */
    public static final int INITIAL_PADDLE_WIDTH = 20;

    /**
     * La hauteur initiale des raquettes.
     */
    public static final int INITIAL_PADDLE_HEIGHT = 100;

    /**
     * La position x initiale de la raquette gauche.
     */
    public static final int INITIAL_PADDLE1_X = 10;

    /**
     * La position x initiale de la raquette droite.
     */
    public static final int INITIAL_PADDLE2_X = INITIAL_PANEL_WIDTH - INITIAL_PADDLE_WIDTH - 10;

    /**
     * La position y initiale de la raquette gauche.
     */
    public static final int INITIAL_PADDLE1_Y = INITIAL_PANEL_HEIGHT / 2;

    /**
     * La position initiale de la raquette droite.
     */
    public static final int INITIAL_PADDLE2_Y = INITIAL_PADDLE1_Y;

    /**
     * La position x initiale de la balle.
     */
    public static final int INITIAL_BALL_X = 50;

    /**
     * La position y initiale de la balle.
     */
    public static final int INITIAL_BALL_Y = 50;

    /**
     * Le diametre initial de la balle.
     */
    public static final int INITIAL_BALL_DIAMETER = 20;

    /**
     * La trajectoire x initiale de la balle.
     */
    public static final double INITIAL_X_TRAJECTORY = 1;

    /**
     * La trajectoire y initiale de la balle.
     */
    public static final double INITIAL_Y_TRAJECTORY = 1;

    /**
     * La balle.
     */
    protected Ball ball;

    /**
     * Le Thread qui permet d'animer la balle.
     */
    protected BallAnimation animationThread;

    /**
     * La couleur du fond du jeu.
     */
    protected Color backgroundColor = Color.BLACK;

    /**
     * La couleur du premier plan du jeu.
     */
    protected Color foregroundColor = Color.WHITE;

    /**
     * Vector contenant les detecteurs de collisions entre la balle et une raquette.
     */
    private Vector<PongPaddleCollisionDetector> pongPaddleCollisionDetectors = new Vector<PongPaddleCollisionDetector>();

    /**
     * Le detecteur de collisions entre la balle et les murs.
     */
    protected PongWallCollisionDetector pongWallCollisionDetector;

    /**
     * La liste qui sera utilisee pour ecouter des evenements.
     */
    protected EventListenerList listenerList = new EventListenerList();

    /**
     * L'espace minimale entre la/les raquette(s) droite(s).
     */
    private double ballOffset = 40;

    /**
     * Decrit si le jeu est en pause.
     */
    private boolean paused = false;

    public PongCanvas() {

        initGUI();
        initBall();
        initPaddles();
        setPongWallCollisionDetector(new PongWallCollisionDetector(this));
        addCollisionDetectors();
    }

    /**
     * Permet de terminer le jeu en terminant l'animation de la balle.
     */
    public void end() {

        paused = true;  // pour qu'on ne puisse plus deplacer les raquettes/lancer des missiles
        animationThread.kill();
    }

    /**
     * Permet de mettre en pause le jeu en mettant en pause l'animation de la balle.
     */
    public void pause() {

        animationThread.setPaused(true);
        paused = true;
    }

    /**
     * Permet de reprendre le jeu en reprenant l'animation de la balle.
     */
    public void resume() {

        animationThread.setPaused(false);
        paused = false;
    }

    /**
     * Decrit si le jeu est en pause.
     */
    public boolean isPaused() {

        return paused;
    }

    /**
     * Methode qui cherche toutes les raquettes du jeu sous forme de tableau de raquettes.
     */
    public abstract Paddle[] getPaddles();

    /**
     * Cherche la balle.
     */
    public Ball getBall() {

        return ball;
    }

    /**
     * Initialise la balle.
     */
    private void initBall() {

        ball = new Ball(this, INITIAL_BALL_X, INITIAL_BALL_Y, INITIAL_BALL_DIAMETER, INITIAL_X_TRAJECTORY, INITIAL_Y_TRAJECTORY);
    }

    /**
     * Initialise les raquettes.
     */
    abstract void initPaddles();

    /**
     * Dessine les elements du jeu a l'ecran. Peut etre redefinie par des sous-classes.
     */
    protected void drawCanvas(Graphics g) {

        Graphics2D g2d = (Graphics2D) g.create();
        // On veut dessiner avec la meilleure qualite.
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2d.setColor(foregroundColor);

        g2d.fill(ball);

        // Pour dessiner la ligne du milieu
        BasicStroke dotted = new BasicStroke(5, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 0, new float[]{40}, 0);  // BasicStroke(float width, int cap, int join, float miterlimit, float[] dash, float dash_phase)
        g2d.setStroke(dotted);
        g2d.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight());

        g2d.dispose();
    }

    /**
     * Initialise l'interface graphique du jeu. Permet un degre minimal de soutien pour le changement de taille du jeu.
     */
    private void initGUI() {

        setPreferredSize(new Dimension(INITIAL_PANEL_WIDTH, INITIAL_PANEL_HEIGHT));
        setSize(INITIAL_PANEL_WIDTH, INITIAL_PANEL_HEIGHT);
        setBackground(backgroundColor);
        setDoubleBuffered(true);
        setFocusable(true);
        // La balle doit se deplacer si la distance entre elle et le bord du jeu et plus petite ou egale a ballOffset
        // (c'est-a-dire la distance minimale entre la balle et la/les raquette(s) droite(s)).
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (ball.getY() + ball.getBallDiameter() + 2 > e.getComponent().getHeight()) {
                    ball.setBallY(e.getComponent().getHeight() - ball.getBallDiameter() - 2);
                }
                if (ball.getX() + ball.getBallDiameter() + 2 > e.getComponent().getWidth() - ballOffset) {
                    ball.setBallX(e.getComponent().getWidth() - ball.getBallDiameter() - ballOffset - 2);
                }
                repaint();
            }
        });
        addListeners();
    }

    /**
     * Permet de changer la couleur du fond du jeu. Heritee de la superclasse JPanel, mais redefinie pour changer la
     * variable backgroundColor.
     */
    @Override
    public void setBackground(Color background) {

        super.setBackground(background);
        backgroundColor = background;
    }

    /**
     * Ajoute les ecouteurs necessaires au jeu.
     */
    protected abstract void addListeners();

    // Dessine tous les elements du jeu a l'ecran en utilisant la methode drawCanvas(Graphics g).
    @Override
    public void paintComponent(Graphics g) {

        Toolkit.getDefaultToolkit().sync();
        super.paintComponent(g);
        drawCanvas(g);
    }

    // On redefinit cette methode pour demarrer l'animation.
    @Override
    public void addNotify() {

        super.addNotify();
        animationThread = new BallAnimation(this);
        animationThread.start();
    }

    /**
     * Cherche le PongMode correspondant au mode de jeu que represente ce PongCanvas.
     */
    public abstract PongMode getMode();

    /**
     * Ajoute les detecteurs de collisions entre la balle et les raquettes ou entre les missiles et les raquettes.
     */
    protected abstract void addCollisionDetectors();

    /**
     * Ajoute un detecteur de collision entre la balle et une raquette.
     */
    public void addPaddleCollisionDetector(PongPaddleCollisionDetector p) {

        pongPaddleCollisionDetectors.add(p);
    }

    /**
     * Envleve tous les detecteurs de collisions entre la balle et les raquettes.
     */
    public void removeAllPaddleCollisionDetectors() {

        pongPaddleCollisionDetectors.removeAllElements();
    }

    /**
     * Cherche tous les detecteurs de collisions entre la balle et les raquettes.
     */
    public Vector<PongPaddleCollisionDetector> getPongPaddleCollisionDetectors() {

        return pongPaddleCollisionDetectors;
    }

    /**
     * Cherche le detecteur de collision entre la balle et les murs.
     */
    public PongWallCollisionDetector getPongWallCollisionDetector() {

        return pongWallCollisionDetector;
    }

    /**
     * Permet de changer le detecteur de collision entre la balle et les murs.
     */
    private void setPongWallCollisionDetector(PongWallCollisionDetector pongWallCollisionDetector) {

        this.pongWallCollisionDetector = pongWallCollisionDetector;
    }

    /**
     * Cherche la couleur du premier plan.
     */
    public Color getForegroundColor() {

        return foregroundColor;
    }

    /**
     * Change la couleur du premier plan.
     */
    public void setForegroundColor(Color color) {

        foregroundColor = color;
    }

    /**
     * Cherche la couleur du fond du jeu.
     */
    public Color getBackgroundColor() {

        return backgroundColor;
    }

    /**
     * Change la distance minimale entre la balle et la/les raquette(s) droite(s).
     */
    public void setBallOffset(double ballOffset) {

        this.ballOffset = ballOffset;
    }

    /**
     * Cherche la distance minimale entre la balle et la/les raquette(s) droite(s).
     */
    public double getBallOffset() {

        return ballOffset;
    }

    /**
     * Ajoute un ChangeListener a ce PongCanvas.
     */
    public void addChangeListener(ChangeListener c) {

        listenerList.add(ChangeListener.class, c);
    }

    /**
     * Enleve un ChangeListener de ce PongCanvas.
     */
    public void removeChangeListener(ChangeListener c) {

        listenerList.remove(ChangeListener.class, c);
    }

    /**
     * Lance un ChangeEvent qui sera ecoute par les ChangeListeners attaches a ce PongCanvas (c'est-a-dire dans sa
     * liste d'ecouteurs) et qui executera la methode stateChanged(ChangeEvent c) de tous les ChangeListeners attaches.
     */
    protected void fireStateChanged() {

        ChangeListener[] listeners = listenerList.getListeners(ChangeListener.class);
        if (listeners != null && listeners.length > 0) {
            ChangeEvent evt = new ChangeEvent(this);
            for (ChangeListener listener : listeners) {
                listener.stateChanged(evt);
            }
        }
    }

    /**
     * Methode qui est appelee lorsque la balle sort du jeu et est servie. Prend un parametre (int) qui decrit le cote ou la balle
     * est sortie: gauche = Paddle.LEFT = 1 et droite = Paddle.RIGHT = 2.
     */
    public abstract void performOutOfBoundsAction(int side);

    /**
     * Decrit si le jeu est termine.
     */
    public abstract boolean isGameOver();
}