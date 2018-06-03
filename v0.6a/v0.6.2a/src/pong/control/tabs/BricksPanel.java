package pong.control.tabs;

import pong.control.PongPreferencesEditor;
import pong.lang.Language;
import pong.mode.PongCanvas;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

/**
 * Onglet de preferences pour changer la couleur des briques dans le mode "casse-brique"
 */
public class BricksPanel extends PongTabPanel {

    /**
     * Le PongPreferencesEditor qui va modifier la couleur des briques.
     */
    private PongPreferencesEditor ppe;

    /**
     * Map qui permet de chercher un terme dans la langue voulue
     */
    private Map<String, String> lang = Language.getLanguageMap(Language.getLoc());

    /**
     * JButton pour changer la couleur des briques
     */
    private JButton bricksColorButton;

    public BricksPanel(PongCanvas pongCanvas) {

        this.ppe = new PongPreferencesEditor(pongCanvas);
        initButtons();
        addListeners();
        addButtons();
    }

    /**
     * Initialise le bouton.
     */
    private void initButtons() {

        bricksColorButton = new JButton(lang.get("bricksColorChange"));
        bricksColorButton.setFocusable(false);
        bricksColorButton.setAlignmentX(CENTER_ALIGNMENT);
    }

    /**
     * Ajoute un ecouteur au bouton.
     */
    private void addListeners() {

        bricksColorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                ppe.setBrickColor();
            }
        });
    }

    /**
     * Ajoute le bouton au JPanel.
     */
    private void addButtons() {

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(bricksColorButton);
    }

    @Override
    public String getTabName() {

        return lang.get("bricks");
    }
}
