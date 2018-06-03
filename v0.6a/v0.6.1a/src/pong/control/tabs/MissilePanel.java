package pong.control.tabs;

import pong.control.PongPreferencesEditor;
import pong.lang.Language;
import pong.mode.PongCanvas;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class MissilePanel extends PongTabPanel {

    private PongPreferencesEditor ppe;
    private Map<String, String> lang = Language.getLanguageMap(Language.getLoc());
    private JButton missileColorButton;

    public MissilePanel(PongCanvas pongCanvas) {

        this.ppe = new PongPreferencesEditor(pongCanvas);
        initButtons();
        addListeners();
        addButtons();
    }

    private void initButtons() {

        missileColorButton = new JButton(lang.get("missileColorChange"));
        missileColorButton.setFocusable(false);
    }

    private void addListeners() {

        missileColorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                ppe.setMissileColor();
            }
        });
    }

    private void addButtons() {

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(missileColorButton);
    }

    @Override
    public String getTabName() {

        return lang.get("missile");
    }
}
