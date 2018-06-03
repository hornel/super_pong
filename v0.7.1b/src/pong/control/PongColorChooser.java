package pong.control;

import pong.lang.Language;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

/**
 * Classe qui permet de changer la couleur dans le preferences. Permet aussi d'annuler le changement.
 */
public class PongColorChooser extends JFrame {

    /**
     * Le JColorChooser qui sera utilise pour le choix des couleurs.
     */
    private JColorChooser colorChooser;

    /**
     * Le bouton pour valider le choix.
     */
    private JButton okButton;

    /**
     * Le bouton pour annuler le choix.
     */
    private JButton cancelButton;

    /**
     * La couleur que colorChooser doit avoir initialement.
     */
    private Color initialColor;

    /**
     * Map qui permet de chercher un terme dans la langue voulue.
     */
    private Map<String, String> lang = Language.getLanguageMap(Language.getLoc());

    /**
     * Constructeur qui initialise l'interface graphique et qui accepte des parametres pour le titre de la fenetre
     * et la couleur initiale du JColorChooser.
     */
    public PongColorChooser(String title, Color initialColor) {

//        On veut une fenetre semblable a celle-la:
//
//        |---------------------------------------|
//        |---------------------------------------|
//        |                                       |
//        |                                       |
//        |        selection des couleurs         |
//        |                                       |
//        |                                       |
//        |                         _______   __  |
//        |                        |Annuler| |OK| |
//        |_______________________________________|

//        La couleur de l'objet dont l'utilisateur et train de changer la couleur changera
//        si l'utilisateur clique sur une des couleurs.

        setTitle(title);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setAlwaysOnTop(true);
        setResizable(false);
        this.initialColor = initialColor;
        initComponents();
        addListeners();
        addComponents();
        pack();
        setLocationRelativeTo(null);
    }

    /**
     * Initialise les composantes de l'interface graphique.
     */
    private void initComponents() {

        colorChooser = new JColorChooser(initialColor);
        colorChooser.setFocusable(false);

        okButton = new JButton(lang.get("ok"));
        okButton.setFocusable(false);
        okButton.setAlignmentX(RIGHT_ALIGNMENT);
        getRootPane().setDefaultButton(okButton);

        cancelButton = new JButton(lang.get("cancel"));
        cancelButton.setFocusable(false);
        cancelButton.setAlignmentX(RIGHT_ALIGNMENT);
    }

    /**
     * Ajoute les ecouteurs aux composantes de l'interface graphique.
     */
    private void addListeners() {

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Pour annuler le changement, il suffit de remettre la couleur du JColorChooser a sa valeur initiale,
                // ce qui va lancer un ChangeEvent, ce qui va permettre d'executer ce qui se trouve dans le ChangeListener
                // du SelectionModel du JColorChooser.

                colorChooser.getSelectionModel().setSelectedColor(initialColor);
                dispose();
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
        add(colorChooser, c);

        // On ajoute les boutons a un JPanel interne pour pouvoir les grouper ensemble a droite.
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
        buttonPanel.add(cancelButton);
        buttonPanel.add(okButton);

        c.gridy++;
        c.anchor = GridBagConstraints.EAST;
        add(buttonPanel, c);
    }

    /**
     * Permet d'ajouter un ChangeListener aux SelectionModel du JColorChooser sans devoir d'abord chercher le
     * JColorChooser.
     */
    public void addChangeListener(ChangeListener c) {

        colorChooser.getSelectionModel().addChangeListener(c);
    }

    public JColorChooser getColorChooser() {

        return colorChooser;
    }
}