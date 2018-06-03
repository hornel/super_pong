// File: PongPreferencesEditor.java
// Author: Leo Horne
// Date Created: 3/8/14

package pong.control;

import pong.control.tabs.*;
import pong.lang.Language;
import pong.mode.PongCanvas;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * Class to present the preferences window to the user.
 */

public class PongPreferencesWindow extends JFrame {

    private PongCanvas pongCanvas;
    private Map<String, String> lang = Language.getLanguageMap(Language.getLoc());
    private JTabbedPane tabs;

    /**
     * Class constructor
     *
     * @param pongCanvas parent pongCanvas
     */
    public PongPreferencesWindow(PongCanvas pongCanvas) {

        this.pongCanvas = pongCanvas;
        initGUI();
    }

    private void initGUI() {

        setResizable(false);
        setAlwaysOnTop(true);
        setLayout(new GridLayout(0, 1));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle(lang.get("prefsWindowTitle"));
        initTabs();
        add(tabs);
        pack();
        setLocationRelativeTo(pongCanvas);
    }

    private void initTabs() {

        tabs = new JTabbedPane();
        tabs.setFocusable(false);

        PongTabPanel colorSchemePanel = new ColorSchemePanel(pongCanvas);
        tabs.add(lang.get("colorScheme"), colorSchemePanel);

        PongTabPanel ballPanel = new BallPanel(pongCanvas);
        tabs.add(lang.get("ball"), ballPanel);

        PongTabPanel paddlePanel = new PaddlePanel(pongCanvas);
        tabs.add(lang.get("paddle"), paddlePanel);

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