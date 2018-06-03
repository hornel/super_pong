package pong.control;

import pong.lang.Language;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.NumberFormatter;
import java.text.DecimalFormat;
import java.util.Map;

/**
 * Classe qui permet a l'utilisateur de selectionner un numbre de colonnes et de lignes.
 */
public class RowColumnSelector extends JPanel {

    /**
     * Le PongModeChooser ou ce selecteur de lignes/colonnes sera place.
     */
    private PongModeChooser pongModeChooser;

    /**
     * JSpinner pour selectionner le nombre de lignes.
     */
    private JSpinner rowSpinner;

    /**
     * JSpinner pour selectionner le nombre de colonnes.
     */
    private JSpinner colSpinner;

    /**
     * Map qui permet de chercher un terme dans la langue voulue.
     */
    private Map<String, String> lang = Language.getLanguageMap(Language.getLoc());

    /**
     * Label pour indiquer quel JSpinner est le JSpinner pour changer le nombre de lignes.
     */
    private JLabel rowsLabel = new JLabel(lang.get("rows") + " ");
    /**
     * Label pour indiquer quel JSpinner est le JSpinner pour changer le nombre de colonnes.
     */
    private JLabel colsLabel = new JLabel(lang.get("columns") + " ");

    public RowColumnSelector(PongModeChooser pongModeChooser) {

        this.pongModeChooser = pongModeChooser;
        initGUI();
    }

    /**
     * Initialise l'interface graphique.
     */
    private void initGUI() {

        // On veut une interface comme celle-ci:
        //          __                __
        // Lignes: |10|^v  Colonnes: | 5|^v

        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        setAlignmentX(JPanel.LEFT_ALIGNMENT);
        rowsLabel.setBorder(BorderFactory.createEmptyBorder(0, 27, 0, 0));
        colsLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        initSpinners();
        addListeners();
        colSpinner.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        add(rowsLabel);
        add(rowSpinner);
        add(colsLabel);
        add(colSpinner);
    }

    /**
     * Initialise les JSpinners.
     */
    private void initSpinners() {

        // Nombre de lignes initial: 10; nombre minimal: 1; nombre maximal: 15; increments de: 1
        SpinnerModel rowModel = new SpinnerNumberModel(10, 1, 15, 1);
        // Nombre de colonnes initial: 5; nombre minimal: 1; nombre maximal: 10; increments de: 1
        SpinnerModel colModel = new SpinnerNumberModel(5, 1, 10, 1);

        // On veut que l'utilisateur ne puisse entrer que des nombres dans les JSpinners. On veut egalement
        // que le PongModeChooser soit averti si l'utilisateur choisit d'entrer les nombres manuellement (but du
        // setCommitsOnValidEdit(true).
        rowSpinner = new JSpinner(rowModel);
        rowSpinner.setEditor(new JSpinner.NumberEditor(rowSpinner));
        JFormattedTextField rowTxt = ((JSpinner.NumberEditor) rowSpinner.getEditor()).getTextField();
        NumberFormatter rowTxtFormatter =  ((NumberFormatter) rowTxt.getFormatter());
        rowTxtFormatter.setCommitsOnValidEdit(true);
        rowTxtFormatter.setAllowsInvalid(false);
        rowTxtFormatter.setOverwriteMode(true);

        colSpinner = new JSpinner(colModel);
        colSpinner.setEditor(new JSpinner.NumberEditor(colSpinner));
        JFormattedTextField colTxt = ((JSpinner.NumberEditor) colSpinner.getEditor()).getTextField();
        NumberFormatter colTxtFormatter =  ((NumberFormatter) colTxt.getFormatter());
        colTxtFormatter.setCommitsOnValidEdit(true);
        colTxtFormatter.setAllowsInvalid(false);
        colTxtFormatter.setOverwriteMode(true);

        // On initialise les parametres du PongModeChooser avec les valeurs initiales des JSpinners.
        pongModeChooser.setParams(getRows(), getColumns());
    }

    /**
     * Ajoute les ecouteurs aux JSpinners.
     */
    private void addListeners() {

        // On change les parametres du PongModeChooser aux valeurs des JSpinners pour change le nombre de
        // lignes/colonnes pour qu'elles soient utilisables pour instantier une classe qui en a besoin.
        // Exemple: SinglePlayerBreakoutPongCanvas
        rowSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                pongModeChooser.setParams(getRows(), getColumns());
            }
        });

        colSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                pongModeChooser.setParams(getRows(), getColumns());
            }
        });
    }

    /**
     * Permet de chercher le nombres de lignes que l'utilisateur a choisi.
     */
    private int getRows() {

        return (Integer) rowSpinner.getValue();
    }

    /**
     * Permet de chercher le nombres de colonnes que l'utilisateur a choisi.
     */
    private int getColumns() {

        return (Integer) colSpinner.getValue();
    }
}