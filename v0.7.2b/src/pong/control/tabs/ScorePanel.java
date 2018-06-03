package pong.control.tabs;

import pong.control.PongPreferencesEditor;
import pong.lang.Language;
import pong.mode.PongCanvas;
import pong.mode.TwoPlayerPongCanvas;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

/**
 * Onglet de preferences pour changer si le score est active et le score maximal dans un TwoPlayerPongCanvas
 */
public class ScorePanel extends PongTabPanel {

    /**
     * Map qui permet de chercher un terme dans la langue voulue
     */
    private Map<String, String> lang = Language.getLanguageMap(Language.getLoc());

    /**
     * Le PongPreferencesEditor qui va modifier la physique du jeu.
     */
    private PongPreferencesEditor ppe;

    /**
     * Le TwoPlayerPongCanvas dont le score doit etre modifie
     */
    private TwoPlayerPongCanvas pongCanvas;

    /**
     * JCheckBox pour changer si le score est active/desactive
     */
    private JCheckBox scoreToggle;

    /**
     * JSpinner pour changer le score maximal du jeu
     */
    private JSpinner maxScoreSpinner;

    public ScorePanel(PongCanvas pongCanvas) {

        ppe = new PongPreferencesEditor(pongCanvas);
        if (pongCanvas instanceof TwoPlayerPongCanvas) {
            this.pongCanvas = (TwoPlayerPongCanvas) pongCanvas;
        }
        initComponents();
        addListeners();
        addComponents();
    }

    /**
     * Initialise les composantes de l'interface graphique.
     */
    private void initComponents() {

        // On veut le suivant:
        // 1. Le JCheckBox pour changer si le score est active cherche son etat du TwoPlayerPongCanvas
        // 2. Le JSpinner pour changer le score maximal cherche sa valeur du TwoPlayerPongCanvas
        // 3. Le JSpinner pour changer le score maximal est active seulement si le JCheckBox pour changer si le score est active
        //    est active.
        // 4. Le JSpinner pour changer le score maximal ne permet d'entrer que des nombres.

        scoreToggle = new JCheckBox(lang.get("score"));
        scoreToggle.setFocusable(false);
        scoreToggle.setSelected(pongCanvas.isScoreEnabled());
        scoreToggle.setAlignmentX(CENTER_ALIGNMENT);

        maxScoreSpinner = new JSpinner(new SpinnerNumberModel(pongCanvas.getMaxScore(), PongPreferencesEditor.MINIMUM_SCORE,
                                       PongPreferencesEditor.MAXIMUM_SCORE, 1));
        maxScoreSpinner.setFocusable(false);
        maxScoreSpinner.setEnabled(scoreToggle.isSelected());
        maxScoreSpinner.setValue(pongCanvas.getMaxScore());
        JFormattedTextField maxScoreText = ((JSpinner.NumberEditor) maxScoreSpinner.getEditor()).getTextField();
        NumberFormatter colTxtFormatter =  ((NumberFormatter) maxScoreText.getFormatter());
        colTxtFormatter.setCommitsOnValidEdit(true);
        colTxtFormatter.setAllowsInvalid(false);
        colTxtFormatter.setOverwriteMode(true);
    }

    /**
     * Ajoute les ecouteurs aux composantes de l'interface graphique
     */
    private void addListeners() {

        // Lorsque l'utilisateur utilise les composantes graphiques, il faut que les 4 regles ci-dessus soient respectes.

        scoreToggle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ppe.toggleScoreEnabled();
                maxScoreSpinner.setEnabled(scoreToggle.isSelected());

                // Comme le score continue a monter meme si le score est desactive, il faut que la valeur du
                // JSpinner pour changer le score maximal soit toujours plus grande que le score actuel et plus petit
                // que le score maximal definit dans PongPreferencesEditor

                if ((Integer) maxScoreSpinner.getValue() < PongPreferencesEditor.MINIMUM_SCORE) {
                    maxScoreSpinner.setValue(PongPreferencesEditor.MINIMUM_SCORE);
                } else if ((Integer) maxScoreSpinner.getValue() > PongPreferencesEditor.MAXIMUM_SCORE) {
                    maxScoreSpinner.setValue(PongPreferencesEditor.MAXIMUM_SCORE);
                }
                maxScoreSpinner.setModel(new SpinnerNumberModel(pongCanvas.getMaxScore(), PongPreferencesEditor.MINIMUM_SCORE,
                        PongPreferencesEditor.MAXIMUM_SCORE, 1));
            }
        });

        maxScoreSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                ppe.setMaxScore((Integer) maxScoreSpinner.getValue());
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
        c.insets = new Insets(5, 5, 5, 5);
        c.gridwidth = 2;
        add(scoreToggle, c);
        c.gridy++;
        c.gridwidth = 1;
        add(new JLabel(lang.get("maxScore")), c);
        c.gridx++;
        add(maxScoreSpinner, c);
    }

    @Override
    public String getTabName() {

        return lang.get("score");
    }
}