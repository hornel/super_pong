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
 * Onglet de preferences pour modifier la vitesse et la taille de la balle.
 */
public class BallPanel extends PongTabPanel {

    /**
     * Le PongPreferencesEditor qui va modifier la vitesse/la taille de la balle.
     */
    private PongPreferencesEditor ppe;

    /**
     * Map qui permet de chercher un terme dans la langue voulue
     */
    private Map<String, String> lang = Language.getLanguageMap(Language.getLoc());

    /**
     * JSlider pour changer la vitesse de la balle
     */
    private JSlider ballSpeedSlider;

    /**
     * JSlider pour changer la taille de la balle
     */
    private JSlider ballSizeSlider;

    public BallPanel(PongCanvas pongCanvas) {

        this.ppe = new PongPreferencesEditor(pongCanvas);
        init();
        addListeners();
        addComponents();
    }

    /**
     * Initialise les composantes de l'interface graphique.
     */
    private void init() {

        // le JSlider qui change la vitesse doit chercher ses valeurs de PongPreferencesEditor et doit avoir des labels
        // qui indiquent la vitesse de la balle.

        ballSpeedSlider = new JSlider(JSlider.HORIZONTAL, (int)(PongPreferencesEditor.MINIMUM_BALL_SPEED*10), (int)(PongPreferencesEditor.MAXIMUM_BALL_SPEED*10), (int)(ppe.getCurrentBallSpeed()*10));
        ballSpeedSlider.setMajorTickSpacing(5);
        ballSpeedSlider.setMinorTickSpacing(1);
        ballSpeedSlider.setSnapToTicks(true);
        ballSpeedSlider.setPaintLabels(true);
        ballSpeedSlider.setPaintTicks(true);
        ballSpeedSlider.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), lang.get("changeBallSpeed"), TitledBorder.CENTER, TitledBorder.TOP));
        ballSpeedSlider.setFocusable(false);

        // le JSlider qui change la taille doit chercher ses valeurs de PongPreferencesEditor
        ballSizeSlider = new JSlider(JSlider.HORIZONTAL, (int)PongPreferencesEditor.MINIMUM_BALL_SIZE, (int)PongPreferencesEditor.MAXIMUM_BALL_SIZE, (int)ppe.getCurrentBallSize());
        ballSizeSlider.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), lang.get("changeBallSize"), TitledBorder.CENTER, TitledBorder.TOP));
        ballSizeSlider.setFocusable(false);

    }

    /**
     * Ajoute les ecouteurs aux composantes de l'interface graphique.
     */
    private void addListeners() {

        ballSpeedSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {

                ppe.setBallSpeed(ballSpeedSlider.getValue() / 10.0d);
            }
        });

        ballSizeSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {

                ppe.setBallSize(ballSizeSlider.getValue());
                ppe.repaintPongCanvas();
            }
        });
    }

    /**
     * Ajoute les composantes a l'interface graphique.
     */
    private void addComponents() {

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(ballSpeedSlider);
        add(ballSizeSlider);
    }

    @Override
    public String getTabName() {

        return lang.get("ball");
    }
}