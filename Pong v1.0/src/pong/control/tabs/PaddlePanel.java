package pong.control.tabs;

import pong.control.PongPreferencesEditor;
import pong.lang.Language;
import pong.mode.PongCanvas;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.Map;

/**
 * Onglet de preferences pour changer les attributs de la raquette.
 */
public class PaddlePanel extends PongTabPanel {

    /**
     * Le PongPreferencesEditor qu'on va utiliser pour modifier les preferences des raquettes.
     */
    private PongPreferencesEditor ppe;
    /**
     * Map qui permet de chercher un terme dans la langue voulue.
     */
    private Map<String, String> lang = Language.getLanguageMap(Language.getLoc());
    /**
     * Le Slider pour changer la taille de la raquette.
     */
    private JSlider paddleHeightSlider;

    public PaddlePanel(PongCanvas pongCanvas) {

        this.ppe = new PongPreferencesEditor(pongCanvas);
        init();
        addListeners();
        addComponents();
    }

    /**
     * Intialise les composants graphiques.
     */
    private void init() {

        paddleHeightSlider = new JSlider(JSlider.HORIZONTAL, (int) PongPreferencesEditor.MINIMUM_PADDLE_HEIGHT, (int) PongPreferencesEditor.MAXIMUM_PADDLE_HEIGHT, (int) ppe.getCurrentPaddleHeight());
        paddleHeightSlider.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), lang.get("changePaddleHeight"), TitledBorder.CENTER, TitledBorder.TOP));
        paddleHeightSlider.setFocusable(false);

    }

    /**
     * Ajoute les ecouteurs.
     */
    private void addListeners() {

        // Pour changer la taille de la raquette via PongPreferencesEditor
        paddleHeightSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {

                ppe.setPaddleHeight(paddleHeightSlider.getValue());
                ppe.repaintPongCanvas();
            }
        });
    }

    /**
     * Ajoute les composants graphiques.
     */
    private void addComponents() {

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(paddleHeightSlider);
    }

    @Override
    public String getTabName() {

        return lang.get("paddle");
    }
}
