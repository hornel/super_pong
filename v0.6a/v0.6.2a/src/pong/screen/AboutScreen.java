package pong.screen;

import pong.internet.BugReporter;
import pong.lang.Language;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class AboutScreen extends JDialog {

    private Map<String, String> lang = Language.getLanguageMap(Language.getLoc());
    public static final String VERSION_NUMBER = "0.6.2a Update 3";

    public AboutScreen(Frame owner) {

        super(owner, false);
        initUI();
        addComponents();
        pack();
        setLocationRelativeTo(owner);
    }

    public void initUI() {

        setTitle(lang.get("about"));
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());
        setResizable(false);
    }

    public void addComponents() {

        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(10, 5, 0, 5);

        JLabel icon = new JLabel(new ImageIcon(getClass().getResource("PongIcon.png")), JLabel.CENTER);
        icon.setAlignmentX(CENTER_ALIGNMENT);
        add(icon, c);

        c.gridy++;

        JLabel pong = new JLabel("Pong");
        pong.setAlignmentX(CENTER_ALIGNMENT);
        pong.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        add(pong, c);

        c.gridy++;
        c.insets = new Insets(0, 5, 0, 5);
        JLabel version = new JLabel(lang.get("version") + " " + VERSION_NUMBER);
        version.setAlignmentX(CENTER_ALIGNMENT);
        add(version, c);

        c.gridy++;
        JLabel copyright = new JLabel(lang.get("copyright"));
        copyright.setAlignmentX(CENTER_ALIGNMENT);
        add(copyright, c);

        JButton reportButton = new JButton(lang.get("reportBug"));
        reportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BugReporter b = new BugReporter();
                b.setVisible(true);
            }
        });
        reportButton.setFocusable(false);
        reportButton.setAlignmentX(CENTER_ALIGNMENT);

        JButton userManualButton = new JButton(lang.get("userManual"));
        userManualButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(AboutScreen.this, "Coming soon!", "User Manual", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        userManualButton.setFocusable(false);
        userManualButton.setAlignmentX(CENTER_ALIGNMENT);

        c.gridy++;
        c.insets = new Insets(10, 0, 0, 0);
        add(reportButton, c);

        c.gridy++;
        c.insets = new Insets(0, 5, 5, 5);
        add(userManualButton, c);
    }
}
