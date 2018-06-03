package pong.control.tabs;

import pong.control.PongPreferencesEditor;
import pong.lang.Language;
import pong.mode.AIPongCanvas;
import pong.mode.PongCanvas;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.Hashtable;
import java.util.Map;

/**
 * Onglet de preferences pour changer la difficulte de l'IA.
 */
public class AIPanel extends PongTabPanel {

    /**
     * Map qui permet de chercher un terme dans la langue voulue
     */
    private Map<String, String> lang = Language.getLanguageMap(Language.getLoc());

    /**
     * JSlider pour changer la difficulte de l'IA
     */
    private JSlider aiSlider;

    /**
     * TwoAIAndHumanPlayerPongCanvas dont la difficulte de l'IA doit etre changee.
     */
    private AIPongCanvas pongCanvas;

    /**
     * Le PongPreferencesEditor qui va modifier la difficulte de l'IA
     */
    private PongPreferencesEditor ppe;

    public AIPanel(PongCanvas pongCanvas) {

        if (pongCanvas instanceof AIPongCanvas) {
            this.pongCanvas = (AIPongCanvas) pongCanvas;
        }
        ppe = new PongPreferencesEditor(pongCanvas);
        initComponents();
        addListeners();
        addComponents();
    }

    /**
     * Initialise les composantes de l'interface graphique.
     */
    private void initComponents() {

        // On veut un JSlider qui cherche ses valeurs max, min et valeur actuelle de PongPreferencesEditor et qui a
        // des labels "facile", "moyen", "difficile" pour que l'utilisateur sache la difficulte qu'il est en train de mettre

        aiSlider = new JSlider(JSlider.HORIZONTAL, PongPreferencesEditor.MINIMUM_AI_DIFFICULY, PongPreferencesEditor.MAXIMUM_AI_DIFFICULTY,
                                pongCanvas.getAI().getAiDifficulty());
        aiSlider.setFocusable(false);
        aiSlider.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), lang.get("changeAIDifficulty"), TitledBorder.CENTER, TitledBorder.TOP));
        aiSlider.setMajorTickSpacing(10);
        aiSlider.setPaintTicks(true);
        Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
        labelTable.put(PongPreferencesEditor.MINIMUM_AI_DIFFICULY, new JLabel(lang.get("easy")));
        labelTable.put(PongPreferencesEditor.INITIAL_AI_DIFFICULTY, new JLabel(lang.get("medium")));
        labelTable.put(PongPreferencesEditor.MAXIMUM_AI_DIFFICULTY, new JLabel(lang.get("hard")));
        aiSlider.setLabelTable(labelTable);
        aiSlider.setPaintLabels(true);
    }

    /**
     * Ajoute les ecouteurs aux composantes de l'interface graphique
     */
    private void addListeners() {

        aiSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                ppe.setAiDifficulty(aiSlider.getValue());
            }
        });
    }

    /**
     * Ajoute les composantes a l'interface graphique.
     */
    private void addComponents() {

        add(aiSlider);
    }

    @Override
    public String getTabName() {
        return lang.get("AI");
    }
}
