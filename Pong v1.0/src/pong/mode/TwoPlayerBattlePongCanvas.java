// File: TwoPlayerBattlePongCanvas.java
// Author: Leo
// Date Created: 4/12/15 3:23 PM

package pong.mode;

import pong.animation.MissileMover;
import pong.collision.PongMissileCollisionDetector;
import pong.missile.PongMissile;
import pong.paddle.BattlePaddle;
import pong.paddle.Paddle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Vector;

/**
 * Mode a deux joueurs. Les regles sont les memes qu'au mode homme vs. homme, mais les joueurs peuvent tirer
 * des missiles pour essayer de detruire l'autre joueur et gagner un point sans resistance.
 */
public abstract class TwoPlayerBattlePongCanvas extends TwoPlayerPongCanvas {

    /**
     * Vector qui va contenir les detecteurs de collisions entre les missiles et les raquettes.
     */
    private Vector<PongMissileCollisionDetector> pongMissileCollisionDetectors = new Vector<PongMissileCollisionDetector>();

    /**
     * La couleur des missiles.
     */
    private Color missileColor = Color.RED;

    /**
     * Le Timer pour deplacer la raquette de gauche avec les touches.
     */
    private Timer paddle1Timer;

    /**
     * Le Timer pour deplacer la raquette de droite avec les touches.
     */
    private Timer paddle2Timer;

    /**
     * Le Timer pour deplacer le missile de la raquette de gauche.
     */
    private Timer missile1Timer;

    /**
     * Le Timer pour deplacer le missile de la raquette de droite.
     */
    private Timer missile2Timer;

    /**
     * La raquette de gauche (avec la capacite de lancer des missiles).
     */
    private BattlePaddle battlePaddle1;

    /**
     * La raquette de droite (avec la capacite de lancer des missiles).
     */
    private BattlePaddle battlePaddle2;

    /**
     * Le delai des Timers d'animation des missiles.
     */
    private final static int TIMER_DELAY = 20;

    private int player1MissileKey;

    private int player2MissileKey;

    public TwoPlayerBattlePongCanvas() {

        addMissileCollisionDetectors();
    }

    protected abstract void initBattlePaddles();

    @Override
    void initPaddles() {

        player1MissileKey = KeyEvent.VK_D;
        player2MissileKey = KeyEvent.VK_LEFT;
        // On initialise les raquettes des joueurs.
        initBattlePaddles();

        // On initialise les Timers d'animation des missiles, mais on ne les demarre pas encore.
        missile1Timer = new Timer(TIMER_DELAY, new MissileMover(this, battlePaddle1));
        missile2Timer = new Timer(TIMER_DELAY, new MissileMover(this, battlePaddle2));

        // On dit a la superclasse quelles sont les raquettes.
        setPaddle1(battlePaddle1);
        setPaddle2(battlePaddle2);
    }

    /**
     * Permet de chercher le Timer d'animation lie a un missile donne.
     */
    public Timer getTimerForMissile(PongMissile missile) {

        if (missile.equals(battlePaddle1.getMissile())) {
            return missile1Timer;
        } else if (missile.equals(battlePaddle2.getMissile())) {
            return missile2Timer;
        } else {
            return null;
        }
    }

    @Override
    protected void addListeners() {

        // Pour lancer les missiles
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {

                // Si le joueur de gauche appuie sur une touche predefinie, il lance un missile
                if (e.getKeyCode() == player1MissileKey && !isPaused()) {
                    // La raquette essaye de preparer un nouveau missile (voir documentation)
                    battlePaddle1.tryToPrepareNewMissile();
                    // On anime le missile
                    if (!missile1Timer.isRunning()) {
                        missile1Timer.start();
                    }
                }

                // Si le joueur de droite appuie sur une touche predefinie, il lance un missile
                if (e.getKeyCode() == player2MissileKey && !isPaused()) {
                    battlePaddle2.tryToPrepareNewMissile();
                    if (!missile2Timer.isRunning()) {
                        missile2Timer.start();
                    }
                }
            }
        });
    }

    /**
     * Permet de tirer un missile de maniere programmatique.
     * @param source la raquette a partir de laquelle il faut tirer un missile.
     */
    public void fireMissile(BattlePaddle source) {

        // La ligne suivante est pour eviter des erreurs si cette methode est
        // appelee quand les detecteurs de collision entre un missile et une raquette
        // ne sont pas encore initialises.
        if (pongMissileCollisionDetectors != null) {
            // On essaie de preparer un nouveau missile puis on demarre son Timer d'animation.
            source.tryToPrepareNewMissile();
            if (!getTimerForMissile(source.getMissile()).isRunning()) {
                getTimerForMissile(source.getMissile()).start();
            }
        }
    }

    @Override
    public void end() {

        super.end();
        // On arrete les Timers
        paddle1Timer.stop();
        paddle2Timer.stop();
        missile1Timer.stop();
        missile2Timer.stop();
    }

    @Override
    public void pause() {

        super.pause();
        // Il faut que les missiles s'arretent aussi quand le jeu est en pause.
        missile1Timer.stop();
        missile2Timer.stop();
    }

    @Override
    public void resume() {

        super.resume();
        // Il faut demarrer les Timers d'animation des missiles seulement si le joueur a lance un missile!
        if (battlePaddle1.hasFiredMissile()) {
            missile1Timer.start();
        }
        if (battlePaddle2.hasFiredMissile()) {
            missile2Timer.start();
        }
    }

    /**
     * Ajoute les detecteurs de collisions entre les missiles et les raquettes.
     */
    private void addMissileCollisionDetectors() {

        pongMissileCollisionDetectors.add(new PongMissileCollisionDetector(this, battlePaddle1.getMissile(), battlePaddle2));
        pongMissileCollisionDetectors.add(new PongMissileCollisionDetector(this, battlePaddle2.getMissile(), battlePaddle1));
    }

    /**
     * Cherche les detecteurs de collisions entre les missiles et les raquettes.
     */
    public Vector<PongMissileCollisionDetector> getPongMissileCollisionDetectors() {

        return pongMissileCollisionDetectors;
    }

    /**
     * Change la couleur du missile.
     */
    public void setMissileColor(Color missileColor) {

        this.missileColor = missileColor;
    }

    /**
     * Cherche les BattlePaddles du jeu.
     */
    public BattlePaddle[] getBattlePaddles() {

        return new BattlePaddle[] {battlePaddle1, battlePaddle2};
    }

    @Override
    protected void drawCustomItems(Graphics2D g2d) {

        // On regarde si les raquettes ont lances un missile et on dessine le missile a l'ecran si c'est le cas.
        for (BattlePaddle paddle : getBattlePaddles()) {
            if (paddle.hasFiredMissile()) {
                g2d.setPaint(missileColor);
                g2d.fill(paddle.getMissile());
            }
        }
    }

    @Override
    public void performOutOfBoundsAction(int side) {

        super.performOutOfBoundsAction(side);

        // Lorsqu'un joueur gagne un point, il faut remettre l'autre raquette dans le jeu.
        if (side == Paddle.RIGHT) {
            battlePaddle2.undestroy();
        } else {
            battlePaddle1.undestroy();
        }
    }

    /**
     * Cherche la couleur du missile.
     */
    public Color getMissileColor() {

        return missileColor;
    }

    /**
     * Permet de change la raquette de gauche.
     */
    public void setBattlePaddle1(BattlePaddle battlePaddle1) {

        this.battlePaddle1 = battlePaddle1;
    }

    /**
     * Permet de changer la raquette de droite.
     */
    public void setBattlePaddle2(BattlePaddle battlePaddle2) {

        this.battlePaddle2 = battlePaddle2;
    }

    /**
     * Permet de chercher la raquette de gauche.
     */
    public BattlePaddle getBattlePaddle1() {

        return battlePaddle1;
    }

    /**
     * Permet de chercher la raquette de droite.
     */
    public BattlePaddle getBattlePaddle2() {

        return battlePaddle2;
    }

    /**
     * Permet de chercher le Timer de deplacement lie a la raquette de droite.
     */
    public Timer getPaddle2Timer() {

        return paddle2Timer;
    }

    /**
     * Change le Timer de deplacement lie a la raquette de droite.
     */
    public void setPaddle2Timer(Timer paddle2Timer) {

        this.paddle2Timer = paddle2Timer;
    }

    /**
     * Change le Timer de deplacement lie a la raquette de gauche.
     */
    public Timer getPaddle1Timer() {

        return paddle1Timer;
    }

    /**
     * Permet de chercher le Timer de deplacement lie a la raquette de gauche.
     */
    public void setPaddle1Timer(Timer paddle1Timer) {

        this.paddle1Timer = paddle1Timer;
    }

    /**
     * Permet de changer sur laquelle touche que le joueur de gauche doit presser pour lancer un missile.
     */
    public void setPlayer1MissileKey(int player1MissileKey) {

        this.player1MissileKey = player1MissileKey;
    }

    /**
     * Permet de changer sur laquelle touche que le joueur de droite doit presser pour lancer un missile.
     */
    public void setPlayer2MissileKey(int player2MissileKey) {

        this.player2MissileKey = player2MissileKey;
    }
}