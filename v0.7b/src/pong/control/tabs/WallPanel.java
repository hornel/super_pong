package pong.control.tabs;

import pong.control.PongPreferencesEditor;
import pong.lang.Language;
import pong.mode.PongCanvas;
import pong.mode.SinglePlayerTrainingPongCanvas;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

/**
 * Onglet de preferences pour changer la hauteur, la couleur et la position du mur dans un SinglePlayerTrainingPongCanvas
 */
public class WallPanel extends PongTabPanel {

    /**
     * Le PongPreferencesEditor qui va modifier la physique du jeu.
     */
    private PongPreferencesEditor ppe;

    /**
     * Le SinglePlayerTrainingPongCanvas dont le mur va etre modifie.
     */
    private SinglePlayerTrainingPongCanvas pongCanvas;

    /**
     * Map qui permet de chercher un terme dans la langue voulue.
     */
    private Map<String, String> lang = Language.getLanguageMap(Language.getLoc());

    /**
     * JButton pour changer la couleur du mur.
     */
    private JButton wallColorButton;

    /**
     * JSlider pour changer la hauteur du mur.
     */
    private JSlider wallHeightSlider;

    /**
     * JSpinner pour changer la position du mur.
     */
    private JSpinner positionSpinner;

    /**
     * Le SpinnerModel que le JSpinner pour changer la position du mur utilisera.
     */
    private SpinnerModel positionModel;

    public WallPanel(PongCanvas pongCanvas) {

        this.ppe = new PongPreferencesEditor(pongCanvas);
        // il faut que le parametre pongCanvas soit un PongCanvas et non un SinglePlayerTrainingPongCanvas car
        // PongPreferencesWindow va donner un PongCanvas.
        if (pongCanvas instanceof SinglePlayerTrainingPongCanvas) {
            this.pongCanvas = (SinglePlayerTrainingPongCanvas) pongCanvas;
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
        // 1. wallHeightSlider doit chercher ses valeurs de PongPreferencesEditor et du PongCanvas.
        // 2. positionSpinner doit deplacer le mur selon le sens des fleches dans le JSpinner. Les valeurs (coordonnees
        //    du mur ne doivent pas etre affichees.
        // 3. positionSpinner utilise la coordonnee y du mur pour changer la position du mur.

        wallColorButton = new JButton(lang.get("changeWallColor"));
        wallColorButton.setFocusable(false);

        wallHeightSlider = new JSlider(JSlider.HORIZONTAL, (int) PongPreferencesEditor.MINIMUM_PADDLE_HEIGHT, pongCanvas.getHeight() - 2 * (int) PongPreferencesEditor.PADDING, (int) pongCanvas.getWallHeight());
        wallHeightSlider.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), lang.get("changeWallHeight"), TitledBorder.CENTER, TitledBorder.TOP));
        wallHeightSlider.setFocusable(false);

        // le parametre "step" du SpinnerNumberModel doit etre negatif car le mur doit suivre le sens des fleches quand
        // il est deplace, alors que normalement, la fleche contre le haut va augmenter les coordonnees du mur, ce qui
        // va le faire descendre, ce qui est contraire a nos voeux. Il faut donc renverser le sens des fleches en utilisant
        // un step negatif.

        positionModel = new SpinnerNumberModel(pongCanvas.getWall().getY(), PongPreferencesEditor.PADDING, (pongCanvas.getHeight() - PongPreferencesEditor.PADDING - pongCanvas.getWall().getPaddleHeight()), -5);
        positionSpinner = new JSpinner(positionModel);
        positionSpinner.setEditor(new JLabel("Position"));
        positionSpinner.getEditor().setFocusable(false);
        positionSpinner.setFocusable(false);
    }

    /**
     * Ajoute les ecouteurs aux composantes de l'interface graphique.
     */
    private void addListeners() {

        wallColorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                ppe.setWallColor();
            }
        });

        // lorsque la hauteur du mur change, sa position maximale change aussi car le mur ne peut pas depasser les limites
        // du jeu. Il faut donc adapter le SpinnerModel du positionSpinner.
        wallHeightSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {

                ppe.setWallHeight(wallHeightSlider.getValue());
                positionModel = new SpinnerNumberModel(pongCanvas.getWall().getY(), PongPreferencesEditor.PADDING, (pongCanvas.getHeight() - PongPreferencesEditor.PADDING - pongCanvas.getWall().getPaddleHeight()), -5);
                positionSpinner.setModel(positionModel);
                positionSpinner.setValue(pongCanvas.getWall().getY());
            }
        });

        positionSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {

                ppe.setWallPosition(((Double) positionSpinner.getValue()));
                positionModel = new SpinnerNumberModel(pongCanvas.getWall().getY(), PongPreferencesEditor.PADDING, (pongCanvas.getHeight() - PongPreferencesEditor.PADDING - pongCanvas.getWall().getPaddleHeight()), -5);
                positionSpinner.setModel(positionModel);
                positionSpinner.setValue(pongCanvas.getWall().getY());
            }
        });
    }

    /**
     * Ajoute les composantes a l'interface graphique.
     */
    private void addComponents() {

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        add(wallColorButton);
        add(wallHeightSlider);
        add(positionSpinner);
    }

    @Override
    public String getTabName() {

        return lang.get("wall");
    }
}
