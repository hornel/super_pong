// File: PongPreferencesEditor.java
// Author: Leo Horne
// Date Created: 3/8/14

package pong.control;

import pong.control.tabs.PongTabPanel;
import pong.lang.Language;
import pong.mode.PongCanvas;
import pong.screen.Pong;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * Classe qui presente la fenetre de preferences a l'utilisateur.
 */
public class PongPreferencesWindow extends JDialog {

    /**
     * Le Pong qui est le parent de cette fenetre.
     */
    private Pong pong;

    /**
     * Le PongCanvas dont les preferences vont etre modifies.
     */
    private PongCanvas pongCanvas;

    /**
     * Map qui permet de chercher un terme dans la langue voulue.
     */
    private Map<String, String> lang = Language.getLanguageMap(Language.getLoc());

    /**
     * JTabbedPane pour presenter les onglets.
     */
    private JTabbedPane tabs;

    public PongPreferencesWindow(Pong pong, PongCanvas pongCanvas) {

        super(pong, false);
        this.pong = pong;
        this.pongCanvas = pongCanvas;
        initGUI();
    }

    /**
     * Initialise l'interface graphique.
     */
    private void initGUI() {

        setResizable(false);
        setLayout(new GridLayout(0, 1));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle(lang.get("prefsWindowTitle"));
        initTabs();
        add(tabs);
        pack();
        setLocationRelativeTo(pongCanvas);
    }

    /**
     * Intialise les onglets.
     */
    private void initTabs() {

        tabs = new JTabbedPane();
        tabs.setFocusable(false);

        // La fenetre va "savoir" quels onglets utiliser en cherchant le PongMode correspondant au PongCanvas dont les
        // preferences sont en train d'etre modifies. A partir de ce PongMode, on peut chercher les constructeurs des
        // onglets qui correspondent au PongCanvas et instantier l'onglet en lui donnant le PongCanvas dont les
        // preferences vont etre modifies comme parametre.

        for (String tabName : pongCanvas.getMode().getCustomTabClassNames()) {

            PongTabPanel tab;
            try {
                tab = (PongTabPanel) Class.forName(tabName).getConstructor(PongCanvas.class).newInstance(pongCanvas);
                tabs.add(tab.getTabName(), tab);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}