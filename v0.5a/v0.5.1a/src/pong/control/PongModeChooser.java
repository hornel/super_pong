// File: PongModeChooser.java
// Author: Leo
// Date Created: 4/9/15 5:51 PM
// Version: 0.1a

package pong.control;

import pong.mode.PongMode;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PongModeChooser extends JFrame implements ActionListener {

    EventListenerList listenerList = new EventListenerList();

    JPanel radioButtonPanel = new JPanel(new GridLayout(0, 1));

    private ButtonGroup radioButtonGroup;

    private JButton cancelButton;
    private JButton okButton;

    private JRadioButton[] radioButtons;

    private PongMode pongMode = PongMode.DUAL_PLAYER_HUMAN_VS_HUMAN;

    public PongModeChooser() {

        initGUI();
        initButtons();
        initRadioButtons();
        initRadioButtonPanel();
        addComponents();
        addListeners();
        pack();
        setLocationRelativeTo(null);
    }

    private void initGUI() {

        setTitle("Choose Mode");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);
        setAlwaysOnTop(true);
    }

    private void initButtons() {

        cancelButton = new JButton("Cancel");
        okButton = new JButton("OK");
    }

    private void initRadioButtons() {

        JRadioButton training = new JRadioButton("Single player: training");
        JRadioButton arkanoid = new JRadioButton("Single player: arkanoid");
        JRadioButton twoHumanPlayers = new JRadioButton("Two players: human vs. human");
        JRadioButton oneHumanOneAI = new JRadioButton("Two players: human vs. computer");
        JRadioButton battle = new JRadioButton("Two players: human vs. human battle");

        twoHumanPlayers.setSelected(true);

        training.setActionCommand(PongMode.SINGLE_PLAYER_TRAINING.toString());
        arkanoid.setActionCommand(PongMode.SINGLE_PLAYER_ARKANOID.toString());
        twoHumanPlayers.setActionCommand(PongMode.DUAL_PLAYER_HUMAN_VS_HUMAN.toString());
        oneHumanOneAI.setActionCommand(PongMode.DUAL_PLAYER_HUMAN_VS_AI.toString());
        battle.setActionCommand(PongMode.DUAL_PLAYER_HUMAN_VS_HUMAN_BATTLE.toString());

        radioButtons = new JRadioButton[]{training, arkanoid, twoHumanPlayers, oneHumanOneAI, battle};

        groupRadioButtons();
    }

    private void groupRadioButtons() {

        radioButtonGroup = new ButtonGroup();
        for (JRadioButton j : radioButtons) {
            radioButtonGroup.add(j);
        }
    }

    private void initRadioButtonPanel() {

        for (JRadioButton j : radioButtons) {
            radioButtonPanel.add(j);
        }
    }

    private void addComponents() {

        add(radioButtonPanel, BorderLayout.PAGE_START);
        add(cancelButton, BorderLayout.LINE_START);
        add(okButton, BorderLayout.LINE_END);
    }

    private void addListeners() {

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                fireStateChanged();
                dispose();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                dispose();
            }
        });

        for (JRadioButton j : radioButtons) {
            j.addActionListener(this);
        }
    }

    public void addChangeListener(ChangeListener listener) {

        listenerList.add(ChangeListener.class, listener);
    }

    public void removeChangeListener(ChangeListener listener) {

        listenerList.remove(ChangeListener.class, listener);
    }

    protected void fireStateChanged() {

        ChangeListener[] listeners = listenerList.getListeners(ChangeListener.class);
        if (listeners != null && listeners.length > 0) {
            ChangeEvent evt = new ChangeEvent(this);
            for (ChangeListener listener : listeners) {
                listener.stateChanged(evt);
            }
        }
    }

    public PongMode getPongMode() {

        return pongMode;
    }    @Override
    public void actionPerformed(ActionEvent actionEvent) {

        setPongMode(PongMode.valueOf(actionEvent.getActionCommand()));
    }

    public void setPongMode(PongMode pongMode) {

        this.pongMode = pongMode;
    }



//    public static void main(String[] args) {
//
//        PongModeChooser pongModeChooser = new PongModeChooser();
//        pongModeChooser.setVisible(true);
//    }
}