// File: PongModeChooser.java
// Author: Leo
// Date Created: 4/9/15 5:51 PM

package pong.control;

import pong.lang.Language;
import pong.mode.PongMode;
import pong.screen.Pong;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

/**
 * Classe qui permet a l'utilisateur de selectionner un mode de jeu lorsqu'il veut commencer un nouveau jeu.
 */
public class PongModeChooser extends JDialog implements ActionListener {

    /**
     * Le JPanel ou se trouveront les boutons radio pour selectionner le mode.
     */
    private JPanel radioButtonPanel = new JPanel(new GridLayout(0, 1));

    /**
     * Le ButtonGroup auquel les boutons radio seront ajoutes.
     */
    private ButtonGroup radioButtonGroup;

    /**
     * Le JButton pour annuler le choix du mode.
     */
    private JButton cancelButton;

    /**
     * Le JButton pour accepter le choix du mode.
     */
    private JButton okButton;

    /**
     * Le Pong dont le mode sera change.
     */
    private Pong pong;

    /**
     * Map qui permet de chercher un terme dans la langue voulue.
     */
    private Map<String, String> lang = Language.getLanguageMap(Language.getLoc());

    /**
     * Tableau pour contenir les boutons radio.
     */
    private JRadioButton[] radioButtons;

    /**
     * Cette variable represente le mode de jeu choisi par l'utilisateur. Sa valeur initiale depend du mode dans lequel
     * l'utilisateur se trouve.
     */
    private PongMode pongMode;

    /**
     * Tableau representant les parametres eventuellement necessaires pour instantier un PongCanvas.
     */
    private Object[] params = new Object[0];

    public PongModeChooser(Pong pong) {

        super(pong, false);
        this.pong = pong;
        pongMode = pong.getPongCanvas().getMode();  // valeur initiale = mode actuel
        radioButtonPanel = new JPanel();
        radioButtonPanel.setLayout(new BoxLayout(radioButtonPanel, BoxLayout.PAGE_AXIS));
        radioButtonPanel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        initGUI();
        initButtons();
        initRadioButtons();
        initRadioButtonPanel();
        addComponents();
        addListeners();
        // il faut lancer un ActionEvent depuis le bouton radio selectionne pour initialiser les eventuelles
        // composantes graphiques.
        // Exemple: le RowColumnSelector si pongMode est PongMode.SINGLE_PLAYER_BREAKOUT.
        for (JRadioButton r : radioButtons) {
            if (r.getActionCommand().equals(pongMode.toString())) {
                actionPerformed(new ActionEvent(r, 0, r.getActionCommand()));
            }
        }
        pack();
        setLocationRelativeTo(pong);
    }
    /**
     * Initialise l'interface graphique.
     */
    private void initGUI() {

        setLayout(new BorderLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle(lang.get("newGameTitle"));
        setResizable(false);
    }

    /**
     * Initialise les boutons.
     */
    private void initButtons() {

        cancelButton = new JButton(lang.get("cancel"));
        okButton = new JButton(lang.get("ok"));
        cancelButton.setFocusable(false);
        okButton.setFocusable(false);
        getRootPane().setDefaultButton(okButton); // le bouton "OK" est le bouton par defaut
    }

    /**
     * Initialise les boutons radio.
     */
    private void initRadioButtons() {

        JRadioButton training = new JRadioButton(lang.get("sgTraining"));
        JRadioButton arkanoid = new JRadioButton(lang.get("sgArkanoid"));
        JRadioButton twoHumanPlayers = new JRadioButton(lang.get("dualHumanHuman"));
        JRadioButton oneHumanOneAI = new JRadioButton(lang.get("dualHumanComputer"));
        JRadioButton battle = new JRadioButton(lang.get("dualBattle"));

        // Pour faire correspondre les boutons radio a un type de PongCanvas, on utilise l'enum PongMode:
        // a chaque bouton radio est attache un String specifique appele ActionCommand qui est envoye lorsqu'un ActionEvent est
        // lance par le bouton. Pour chaque bouton, on fait correspondre son ActionCommand au representation String du
        // PongMode. Lorsque le bouton radio est clique, on peut recuperer l'ActionCommand et faire correspondre la
        // representation String du PongMode avec le PongMode lui-meme. De la, on peut chercher le PongCanvas correspondant
        // au PongMode et l'instantier avec les parametres necessaires (cela sera fait dans la classe Pong).

        training.setActionCommand(PongMode.SINGLE_PLAYER_TRAINING.toString());
        arkanoid.setActionCommand(PongMode.SINGLE_PLAYER_BREAKOUT.toString());
        twoHumanPlayers.setActionCommand(PongMode.DUAL_PLAYER_HUMAN_VS_HUMAN.toString());
        oneHumanOneAI.setActionCommand(PongMode.DUAL_PLAYER_HUMAN_VS_AI.toString());
        battle.setActionCommand(PongMode.DUAL_PLAYER_HUMAN_VS_HUMAN_BATTLE.toString());

        radioButtons = new JRadioButton[]{training, arkanoid, twoHumanPlayers, oneHumanOneAI, battle};

        // On selectionne initialement le bouton radio correspondant a pongMode.
        for (JRadioButton r : radioButtons) {
            if (PongMode.valueOf(r.getActionCommand()).equals(pongMode)) {
                r.setSelected(true);
            }
        }

        groupRadioButtons();
    }

    /**
     * Permet de grouper ensemble les boutons radio pour qu'un ne soit selectionne a la fois.
     */
    private void groupRadioButtons() {

        radioButtonGroup = new ButtonGroup();
        for (JRadioButton r : radioButtons) {
            r.setFocusable(false);
            radioButtonGroup.add(r);
        }
    }

    /**
     * Ajoute les boutons radio au JPanel qui doit les contenir.
     */
    private void initRadioButtonPanel() {

        for (JRadioButton r : radioButtons) {
            radioButtonPanel.add(r);
        }
    }

    /**
     * Ajoute les composantes a l'interface graphique.
     */
    private void addComponents() {

        add(radioButtonPanel, BorderLayout.PAGE_START);
        add(cancelButton, BorderLayout.LINE_START);
        add(okButton, BorderLayout.LINE_END);
    }

    /**
     * Ajoute les ecouteurs aux composantes de l'interface graphique.
     */
    private void addListeners() {

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                // on cherche et instantie le PongMode correspondant au bouton radio selectionne avec les parametres
                // necessaires.
                pong.changePongMode(getPongMode(), params);
                dispose();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                dispose();
            }
        });

        for (JRadioButton r : radioButtons) {
            r.addActionListener(this);
        }
    }

    public PongMode getPongMode() {

        return pongMode;
    }

    public void setPongMode(PongMode pongMode) {

        this.pongMode = pongMode;
    }

    public void setParams(Object... params) {

        this.params = params;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {

        // on change pongMode selon l'ActionCommand du bouton radio selectionne
        setPongMode(PongMode.valueOf(actionEvent.getActionCommand()));

        // si le bouton radio selectionne est celui pour le mode casse-brique, il faut afficher le selectionneur de
        // lignes et de colonnes.
        if (actionEvent.getActionCommand().equals(PongMode.SINGLE_PLAYER_BREAKOUT.toString())) {

            radioButtonPanel.removeAll();
            for (JRadioButton r : radioButtons) {
                radioButtonPanel.add(r);
                if (r.getActionCommand().equals(PongMode.SINGLE_PLAYER_BREAKOUT.toString())) {
                    radioButtonPanel.add(new RowColumnSelector(this));
                }
            }
            pack();

        // sinon, il ne faut pas l'afficher.
        } else {
            radioButtonPanel.removeAll();
            for (JRadioButton r : radioButtons) {
                radioButtonPanel.add(r);
            }
            // il faut reinitialiser les parametres car le selectionneur de lignes et de colonnes
            // change les parametres.
            setParams(/*rien*/);
            pack();
        }
    }
}