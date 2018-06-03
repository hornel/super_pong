// File: PongModeChooser.java
// Author: Leo
// Date Created: 4/9/15 5:51 PM
// Version: 0.1a

package pong.control;

import pong.mode.PongMode;
import pong.ui.LButton;
import pong.ui.LRadioButton;
import pong.ui.LRadioButtonGroup;

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

    private LRadioButtonGroup radioButtonGroup;

    private LButton cancelButton;
    private LButton okButton;

    private LRadioButton[] radioButtons;

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

        cancelButton = new LButton("Cancel");
        okButton = new LButton("OK");
    }

    private void initRadioButtons() {

        LRadioButton training = new LRadioButton("Single player: training");
        LRadioButton arkanoid = new LRadioButton("Single player: arkanoid");
        LRadioButton twoHumanPlayers = new LRadioButton("Two players: human vs. human");
        LRadioButton oneHumanOneAI = new LRadioButton("Two players: human vs. computer");
        LRadioButton battle = new LRadioButton("Two players: human vs. human battle");

        twoHumanPlayers.setSelected(true);

        training.setActionCommand(PongMode.SINGLE_PLAYER_TRAINING.toString());
        arkanoid.setActionCommand(PongMode.SINGLE_PLAYER_ARKANOID.toString());
        twoHumanPlayers.setActionCommand(PongMode.DUAL_PLAYER_HUMAN_VS_HUMAN.toString());
        oneHumanOneAI.setActionCommand(PongMode.DUAL_PLAYER_HUMAN_VS_AI.toString());
        battle.setActionCommand(PongMode.DUAL_PLAYER_HUMAN_VS_HUMAN_BATTLE.toString());

        radioButtons = new LRadioButton[]{training, arkanoid, twoHumanPlayers, oneHumanOneAI, battle};

        groupRadioButtons();
    }

    private void groupRadioButtons() {

        radioButtonGroup = new LRadioButtonGroup();
        for (LRadioButton r : radioButtons) {
            radioButtonGroup.add(r);
        }
    }

    private void initRadioButtonPanel() {

        for (LRadioButton r : radioButtons) {
            radioButtonPanel.add(r);
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

        for (LRadioButton r : radioButtons) {
            r.addActionListener(this);
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
    }

    public void setPongMode(PongMode pongMode) {

        this.pongMode = pongMode;
    }    @Override
    public void actionPerformed(ActionEvent actionEvent) {

        setPongMode(PongMode.valueOf(actionEvent.getActionCommand()));
    }


}