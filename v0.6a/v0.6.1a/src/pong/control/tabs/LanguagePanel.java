package pong.control.tabs;

import pong.control.PongPreferencesEditor;
import pong.lang.Language;
//import pong.lang.Language;
//import pong.mode.PongCanvas;
//import pong.ui.LButton;
//
//import javax.swing.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.io.PrintWriter;
//import java.util.HashMap;
//import java.util.Map;
//
//public class LanguagePanel extends JPanel {
//
//    private PongPreferencesEditor ppe;
//    private Map lang = Language.getLanguageMap(Language.getLoc());
//
//    public LanguagePanel(PongCanvas pongCanvas) {
//
//        this.ppe = new PongPreferencesEditor(pongCanvas);
//        initButtons();
//        addListeners();
//        addButtons();
//    }
//
//    private void init() {
//
//        JPopupMenu languages = new JPopupMenu("Language");
//        JMenu englishUS = new JMenu("English (US)");
//        englishUS.setActionCommand("en-US");
//        JMenu french = new JMenu("Français");
//        french.setActionCommand("fr-CH");
//        JMenu german = new JMenu("Deutsch");
//        german.setActionCommand("de-DE");
//
//        languages.add(englishUS);
//        languages.add(french);
//        languages.add(german);
//    }
//
//    private void addListeners() {
//
//        changeForegroundColor.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//
//                ppe.setForegroundColor();
//            }
//        });
//
//        changeBackgroundColor.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                ppe.setBackgroundColor();
//            }
//        });
//    }
//
//    private void addButtons() {
//
//        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
//        add(changeForegroundColor);
//        add(changeBackgroundColor);
//    }
//}

public class LanguagePanel /*implements ActionListener*/ {

//    @Override
//    public void actionPerformed(ActionEvent e) {
//
//        String locFilePath = this.getClass().getResource("../../loc.conf").toString();
//        System.out.println(locFilePath);
//    }

    public static void main(String[] args) {

        String locFilePath = LanguagePanel.class.getResource("../../loc.conf").toString();
        System.out.println(locFilePath);
    }
}
