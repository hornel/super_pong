package pong.control.tabs;

import pong.control.PongPreferencesEditor;
import pong.lang.Language;
import pong.mode.PongCanvas;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class BricksPanel extends PongTabPanel {

    private PongPreferencesEditor ppe;
    private Map<String, String> lang = Language.getLanguageMap(Language.getLoc());
    private JButton bricksColorButton;

    public BricksPanel(PongCanvas pongCanvas) {

        this.ppe = new PongPreferencesEditor(pongCanvas);
        initButtons();
        addListeners();
        addButtons();
    }

    private void initButtons() {

        bricksColorButton = new JButton(lang.get("bricksColorChange"));
        bricksColorButton.setFocusable(false);
    }

    private void addListeners() {

        bricksColorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                ppe.setBrickColor();
            }
        });
    }

    private void addButtons() {

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(bricksColorButton);
    }

    @Override
    public String getTabName() {

        return lang.get("bricks");
    }
}
