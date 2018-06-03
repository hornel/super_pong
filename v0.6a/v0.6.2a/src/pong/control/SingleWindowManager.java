package pong.control;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Classe qui permet de n'avoir qu'une fenetre ouverte a la fois.
 * Le parametre C decrit le type de fenetre que ce SingleWindowManager va gerer.
 */
public class SingleWindowManager<C extends Window> {

    /**
     * La fenetre que ce SingleWindowManager va gerer.
     */
    private C c = null;

    public SingleWindowManager() {}

    /**
     * Permet de determiner quelle fenetre ce SingleWindowManager va gerer.
     */
    public void setComponent(C c1) {

        if (c == null) {
            c = c1;
            c.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    c = null;  // on nullifie c pour signaler que le SingleWindowManager est maintenant libre.
                }

                @Override
                public void windowClosed(WindowEvent e) {
                    c = null;  // idem.
                }
            });
        }
    }

    /**
     * Permet de fermer la fenetre attachee a ce SingleWindowManager.
     */
    public void disposeWindow() {

        if (c != null) {
            c.dispose();
        }
    }

    /**
     * Cherche la fenetre liee a ce SingleWindowManager.
     */
    public C getComponent() {

        return c;
    }

    /**
     * Decrit si ce SingleWindowManager est occupe (i.e., il y une fenetre deja ouverte)
     */
    public boolean isOccupied() {

        return c != null;
    }
}