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
 * Onglet de preferences pour changer les couleurs du jeu.
 */
public class ColorSchemePanel extends PongTabPanel {

    /**
     * Le PongPreferencesEditor qui va modifier les couleurs.
     */
    private PongPreferencesEditor ppe;

    /**
     * Map qui permet de chercher un terme dans la langue voulue.
     */
    private Map<String, String> lang = Language.getLanguageMap(Language.getLoc());

    /**
     * Bouton pour changer la couleur du premier plan du jeu.
     */
    private JButton changeForegroundColor;

    /**
     * Bouton pour changer la couleur du fond du jeu.
     */
    private JButton changeBackgroundColor;

    public ColorSchemePanel(PongCanvas pongCanvas) {

        this.ppe = new PongPreferencesEditor(pongCanvas);
        initButtons();
        addListeners();
        addButtons();
    }

    /**
     * Initialise les boutons.
     */
    private void initButtons() {

        changeForegroundColor = new JButton(lang.get("fgColorChange"));
        changeBackgroundColor = new JButton(lang.get("bgColorChange"));
        changeForegroundColor.setFocusable(false);
        changeForegroundColor.setAlignmentX(Component.CENTER_ALIGNMENT);
        changeBackgroundColor.setFocusable(false);
        changeBackgroundColor.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    /**
     * Ajoute les ecouteurs aux boutons.
     */
    private void addListeners() {

        changeForegroundColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                ppe.setForegroundColor();
            }
        });

        changeBackgroundColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ppe.setBackgroundColor();
            }
        });
    }

    /**
     * Ajoute les boutons.
     */
    private void addButtons() {

        // On veut un onglet comme celle-ci:
        // |--------------------------------------------------|
        // |        ____________________________________      |
        // |       | Changer la couleur du premier plan |     |
        // |            _____________________________         |
        // |           |  Changer la couleur du fond |        |
        // |__________________________________________________|

        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        add(changeForegroundColor);
        add(changeBackgroundColor);
    }

    @Override
    public String getTabName() {

        return lang.get("colorScheme");
    }
}
