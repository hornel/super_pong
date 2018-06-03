package pong.control.tabs;

import javax.swing.*;

/**
 * Classe abstraite ecrivant un onglet de preferences.
 */
public abstract class PongTabPanel extends JPanel {

    /**
     * Methode abstraite qui doit retourner le nom de l'onglet.
     */
    public abstract String getTabName();
}