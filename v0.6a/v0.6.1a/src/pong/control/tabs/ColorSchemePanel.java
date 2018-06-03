package pong.control.tabs;

import pong.control.PongPreferencesEditor;
import pong.lang.Language;
import pong.mode.PongCanvas;
import pong.ui.LButton;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class ColorSchemePanel extends PongTabPanel {

    private PongPreferencesEditor ppe;
    private Map<String, String> lang = Language.getLanguageMap(Language.getLoc());
    private JButton changeForegroundColor;
    private JButton changeBackgroundColor;

    public ColorSchemePanel(PongCanvas pongCanvas) {

        this.ppe = new PongPreferencesEditor(pongCanvas);
        initButtons();
        addListeners();
        addButtons();
    }

    private void initButtons() {

        changeForegroundColor = new JButton(lang.get("fgColorChange"));
        changeBackgroundColor = new JButton(lang.get("bgColorChange"));
        changeForegroundColor.setFocusable(false);
        changeBackgroundColor.setFocusable(false);
    }

    private void addListeners() {

        changeForegroundColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                ppe.setForegroundColor();
            }
        });

        changeBackgroundColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ppe.setBackgroundColor();
            }
        });
    }

    private void addButtons() {

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(changeForegroundColor);
        add(changeBackgroundColor);
    }

    @Override
    public String getTabName() {

        return lang.get("colorScheme");
    }
}
