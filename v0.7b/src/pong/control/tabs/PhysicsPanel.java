package pong.control.tabs;

import pong.control.PongPreferencesEditor;
import pong.lang.Language;
import pong.mode.PongCanvas;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

/**
 * Onglet de preferences pour changer la physique du jeu
 */
public class PhysicsPanel extends PongTabPanel {

    /**
     * Map qui permet de chercher un terme dans la langue voulue
     */
    private Map<String, String> lang = Language.getLanguageMap(Language.getLoc());

    /**
     * Le PongPreferencesEditor qui va modifier la physique du jeu.
     */
    private PongPreferencesEditor ppe;

    /**
     * Le PongCanvas dont la physique doit etre modifiee
     */
    private PongCanvas pongCanvas;

    /**
     * JCheckBox pour activer/desactiver la gravite
     */
    private JCheckBox gravityCheckBox;

    /**
     * JCheckBox pour activer/desactiver la vitesse terminale
     */
    private JCheckBox terminalCheckBox;

    /**
     * JSlider pour changer la vitesse terminale
     */
    private JSlider terminalVelocitySlider;

    /**
     * JSlider pour changer la gravite
     */
    private JSlider gravitySlider;

    public PhysicsPanel(PongCanvas pongCanvas) {

        this.ppe = new PongPreferencesEditor(pongCanvas);
        this.pongCanvas = pongCanvas;
        initComponents();
        addListeners();
        addComponents();
    }

    /**
     * Initialise les composantes de l'interface graphique.
     */
    private void initComponents() {

        // On veut le suivant:
        // 1. Lorsque la gravite est desactivee, le JSlider qui permet de changer la gravite, le JCheckBox pour activer/
        //    desactiver la vitesse terminale est desactive et le JSlider qui permet de changer la vitesse terminale est
        //    desactive
        // 2. Lorsque la gravite est desactivee, la vitesse terminale est desactivee aussi
        // 3. Lorsque la gravite est activee, le JSlider pour changer la gravite est active et le JCheckBox pour activer
        //    la vitesse terminale est active.
        // 4. Le JCheckBox pour changer la vitesse terminale est active seulement si la vitesse terminale est activee.
        // 5. Les JSliders pour changer la gravite et la vitesse terminale cherchent leurs valeurs du PongCanvas et de
        //    PongPreferencesEditor.
        // 6. Les JCheckBox pour activer/desactiver la gravite/vitesse terminale cherchent leurs etats du PongCanvas.

        gravityCheckBox = new JCheckBox(lang.get("gravity"));
        gravityCheckBox.setFocusable(false);
        gravityCheckBox.setSelected(pongCanvas.getBall().isGravityEnabled());

        terminalCheckBox = new JCheckBox(lang.get("terminalVelocity"));
        terminalCheckBox.setFocusable(false);
        terminalCheckBox.setSelected(pongCanvas.getBall().isTerminalVelocityEnabled());
        terminalCheckBox.setEnabled(gravityCheckBox.isSelected());

        terminalVelocitySlider = new JSlider(JSlider.HORIZONTAL, (int) PongPreferencesEditor.MINIMUM_TERMINAL_MULTIPLIER * 10,
                (int) PongPreferencesEditor.MAXIMUM_TERMINAL_MULTIPLIER * 10, (int) (pongCanvas.getBall().getTerminalMultiplier() * 10));
        terminalVelocitySlider.setFocusable(false);
        terminalVelocitySlider.setEnabled(terminalCheckBox.isSelected() && gravityCheckBox.isSelected());

        // la gravite est plus grande si la valeur du JSlider est plus petite et vice versa, donc pour avoit un JSlider
        // normal ou la gravite est plus grande si la valeur du JSlider est plus grande, il faut prendre des valeurs negatives

        gravitySlider = new JSlider(JSlider.HORIZONTAL, -(int) PongPreferencesEditor.MAXIMUM_GRAVITY_COEFFICIENT, -(int) PongPreferencesEditor.MINIMUM_GRAVITY_COEFFICIENT,
                -(int) (pongCanvas.getBall().getGravityCoefficient()));
        gravitySlider.setFocusable(false);
        gravitySlider.setEnabled(gravityCheckBox.isSelected());
    }

    /**
     * Ajoute les ecouteurs aux composantes de l'interface graphique
     */
    private void addListeners() {

        // Lorsque l'utilisateur utilise les composantes graphiques, il faut que les 6 regles ci-dessus soient respectes.

        gravityCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ppe.toggleGravityEnabled();
                terminalCheckBox.setSelected(gravityCheckBox.isSelected() ? terminalCheckBox.isSelected() : gravityCheckBox.isSelected());
                terminalCheckBox.setEnabled(gravityCheckBox.isSelected());
                gravitySlider.setEnabled(gravityCheckBox.isSelected());
                terminalVelocitySlider.setEnabled(terminalCheckBox.isSelected());
            }
        });

        terminalCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ppe.toggleTerminalVelocityEnabled();
                terminalVelocitySlider.setEnabled(terminalCheckBox.isSelected());
            }
        });

        gravitySlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                ppe.setGravityCoefficient(-gravitySlider.getValue());
            }
        });

        terminalVelocitySlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                ppe.setTerminalMultiplier(terminalVelocitySlider.getValue() / 10);
            }
        });
    }

    /**
     * Ajoute les composantes a l'interface graphique.
     */
    private void addComponents() {

        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 0;
        add(gravityCheckBox, c);
        c.gridx++;
        add(terminalCheckBox, c);
        c.gridx = 0;
        c.gridy++;
        add(gravitySlider, c);
        c.gridx++;
        add(terminalVelocitySlider, c);
    }

    @Override
    public String getTabName() {

        return lang.get("physics");
    }
}
