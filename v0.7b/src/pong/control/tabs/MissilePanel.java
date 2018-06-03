package pong.control.tabs;

import pong.control.PongPreferencesEditor;
import pong.lang.Language;
import pong.mode.PongCanvas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

/**
 * Onglet de preferences pour changer la couleur des missiles dans le mode "bataille"
 */
public class MissilePanel extends PongTabPanel {

    /**
     * Le PongPreferencesEditor qui va modifier la couleur des missiles.
     */
    private PongPreferencesEditor ppe;

    /**
     * Map qui permet de chercher un terme dans la langue voulue
     */
    private Map<String, String> lang = Language.getLanguageMap(Language.getLoc());

    /**
     * JButton pour changer la couleur des missiles
     */
    private JButton missileColorButton;

    public MissilePanel(PongCanvas pongCanvas) {

        this.ppe = new PongPreferencesEditor(pongCanvas);
        initButtons();
        addListeners();
        addButtons();
    }

    /**
     * Initialise le bouton.
     */
    private void initButtons() {

        missileColorButton = new JButton(lang.get("missileColorChange"));
        missileColorButton.setFocusable(false);
        missileColorButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    /**
     * Ajoute un ecouteur au bouton.
     */
    private void addListeners() {

        missileColorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                ppe.setMissileColor();
            }
        });
    }

    /**
     * Ajoute le bouton au JPanel.
     */
    private void addButtons() {

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(missileColorButton);
    }

    @Override
    public String getTabName() {

        return lang.get("missile");
    }
}
