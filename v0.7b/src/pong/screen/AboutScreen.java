package pong.screen;

import pong.internet.BugReporter;
import pong.lang.Language;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

/**
 * Classe qui permet d'afficher une fenetre "A Propos de Pong".
 */
public class AboutScreen extends JDialog {

    /**
     * Map qui permet de chercher un terme dans la langue voulue.
     */
    private Map<String, String> lang = Language.getLanguageMap(Language.getLoc());

    /**
     * La version du jeu.
     */
    public static final String VERSION_NUMBER = "0.7b";

    public AboutScreen(Frame owner) {

        super(owner, false);
        initUI();
        addComponents();
        pack();
        setLocationRelativeTo(owner);
    }

    /**
     * Initialise la fenetre.
     */
    public void initUI() {

        setTitle(lang.get("about"));
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());
        setResizable(false);
    }

    /**
     * Ajoute les composantes a la fenetre.
     */
    public void addComponents() {

        // On veut une fenetre comme celle-ci:
        //  ___________________________
        // |   _____________________   |
        // |  |                     |  |
        // |  |                     |  |
        // |  |                     |  |
        // |  |      [icone]        |  |
        // |  |                     |  |
        // |  |                     |  |
        // |  |                     |  |
        // |  |_____________________|  |
        // |                           |
        // |           PONG            |
        // |      Version x.x.x        |
        // | (c) 2015 Leo Horne. Tous  |
        // |     droits reserves.      |
        // |   ____________________    |
        // |  | Signaler un bug... |   |
        // |   ____________________    |
        // |  | Guide utilisateur  |   |
        // |___________________________|

        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(10, 5, 0, 5);

        // l'icone
        JLabel icon = new JLabel(new ImageIcon(getClass().getResource("PongIcon.png")), JLabel.CENTER);
        icon.setAlignmentX(CENTER_ALIGNMENT);
        add(icon, c);

        c.gridy++;

        // PONG
        JLabel pong = new JLabel("Pong");
        pong.setAlignmentX(CENTER_ALIGNMENT);
        pong.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        add(pong, c);

        // Version: x.x.x
        c.gridy++;
        c.insets = new Insets(0, 5, 0, 5);
        JLabel version = new JLabel(lang.get("version") + " " + VERSION_NUMBER);
        version.setAlignmentX(CENTER_ALIGNMENT);
        add(version, c);

        // (c) 2015 Leo Horne. Tous droits reserves.
        c.gridy++;
        JLabel copyright = new JLabel(lang.get("copyright"));
        copyright.setAlignmentX(CENTER_ALIGNMENT);
        add(copyright, c);

        // Signaler un bug...
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

        // Guide utilisateur...
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