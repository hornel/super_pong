package pong.internet;

import pong.lang.Language;
import pong.screen.AboutScreen;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

/**
 * Classe qui permet a l'utilisateur de signaler un bug.
 */
public class BugReporter extends JFrame {

    /**
     * JLabel indiquant que l'utilisateur doit ecrire le titre du bug dans le JTextField en-dessous.
     */
    private JLabel titleLabel;

    /**
     * JLabel indiquant que l'utilisateur doit ce qui s'est passe dans le JTextArea en-dessous.
     */
    private JLabel whatHappenedLabel;

    /**
     * JLabel indiquant que l'utilisateur doit comment reproduire le bug dans le JTextArea en-dessous.
     */
    private JLabel reproduceLabel;

    /**
     * JTextField ou l'utilisateur doit tapper le nom du bug.
     */
    private JTextField title;

    /**
     * JTextArea ou l'utilisateur doit tapper ce qui s'est passe.
     */
    private JTextArea whatHappened;

    /**
     * JTextArea ou l'utilisateur doit tapper comment reproduire le bug.
     */
    private JTextArea reproduceText;

    /**
     * JScrollPane pour afficher whatHappened.
     */
    private JScrollPane whatHappenedPane;

    /**
     * JScrollPane pour afficher reproduceText.
     */
    private JScrollPane reproduceTextPane;

    /**
     * JButton pour soumettre le bug.
     */
    private JButton submit;

    /**
     * JButton pour annuler.
     */
    private JButton cancel;

    /**
     * JButton pour choisir une capture d'ecran.
     */
    private JButton screenShotButton;

    /**
     * JPanel pour grouper ensemble les boutons OK et Annuler.
     */
    private JPanel buttonPanel;

    /**
     * JFileChooser pour choisir une capture d'ecran.
     */
    private JFileChooser screenShotChooser;

    /**
     * String pour stocker le chemin d'acces de la capture d'ecran.
     */
    private String attachment = null;

    /**
     * JLabel pour afficher le nom de la capture d'ecran pour que l'utilisateur sache qu'il a
     * ajoute une capture d'ecran.
     */
    private JLabel attachmentLabel;

    /**
     * Map qui permet de chercher un terme dans la langue voulue.
     */
    private Map<String, String> lang = Language.getLanguageMap(Language.getLoc());

    public BugReporter() {

        initGUI();
        initComponents();
        addListeners();
        addComponents();
        pack();
        // il faut manuellement mettre la taille de attachmentLabel car sinon, il depasse
        attachmentLabel.setPreferredSize(new Dimension(getWidth() - screenShotButton.getWidth() - submit.getWidth() - cancel.getWidth() - 30, 20));
        setLocationRelativeTo(null);
    }

    /**
     * Inititialise les composantes de l'interface graphique.
     */
    private void initComponents() {

        buttonPanel = new JPanel();

        titleLabel = new JLabel(lang.get("title"));
        whatHappenedLabel = new JLabel(lang.get("whatHappenedString"));
        reproduceLabel = new JLabel(lang.get("reproduceString"));
        attachmentLabel = new JLabel();
        attachmentLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));

        title = new JTextField(39);

        // On veut que les JTextArea divisent les mots par espace et non par caractere.
        whatHappened = new JTextArea(10, 40);
        whatHappened.setLineWrap(true);
        whatHappened.setWrapStyleWord(true);
        whatHappened.setFont(title.getFont());

        reproduceText = new JTextArea(10, 40);
        reproduceText.setLineWrap(true);
        reproduceText.setWrapStyleWord(true);
        reproduceText.setFont(title.getFont());

        whatHappenedPane = new JScrollPane(whatHappened);
        whatHappened.setFocusable(true);
        reproduceTextPane = new JScrollPane(reproduceText);

        submit = new JButton(lang.get("submit"));
        submit.setFocusable(false);
        cancel = new JButton(lang.get("cancel"));
        cancel.setFocusable(false);
        // on ajoute ces deux boutons a buttonPanel pour les grouper ensemble.
        buttonPanel.add(cancel);
        buttonPanel.add(submit);

        screenShotButton = new JButton(lang.get("addScreenshot"));
        screenShotButton.setFocusable(false);

        // On veut que l'utilisateur puisse choisir seulement des images.
        screenShotChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(lang.get("imageFiles"), "jpg", "png", "gif", "jpeg", "bmp", "tiff");
        screenShotChooser.setFileFilter(filter);

        getRootPane().setDefaultButton(submit);
    }

    /**
     * Initialise l'interface graphique.
     */
    private void initGUI() {

        setTitle(lang.get("reportBug").substring(0, lang.get("reportBug").length() - 3));
        setLayout(new GridBagLayout());
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
    }

    /**
     * Ajoute les ecouteurs aux composantes de l'interface graphique.
     */
    private void addListeners() {

        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        // Lorsque l'utilisateur clique sur "Soumettre", on veut:
        // 1. Enlever tout de la fenetre
        // 2. Afficher une barre de progression indeterminee pendant que le bug est envoye
        // 3. Afficher un message que le bug a ete envoye.
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getContentPane().removeAll();
                // Il faut avoir les trois lignes suivantes, sinon les nouvelles composantes ne s'affichent pas.
                getContentPane().setVisible(false);
                getContentPane().setVisible(true);
                getContentPane().repaint();
                JProgressBar j = new JProgressBar();
                j.setIndeterminate(true);
                GridBagConstraints c = new GridBagConstraints();
                c.gridx = 0;
                c.gridy = 0;
                add(j, c);
                c.gridy++;
                add(new JLabel(lang.get("sendingBugReport")), c);
                // On envoie le bug dans un nouveau Thread pour que l'interface graphique reagie encore.
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // il n'y a pas de capture d'ecran
                        if (attachment == null) {
                            // On utilise un compte Gmail appele pongbugreporter@gmail.com pour envoyer et recevoir
                            // les bugs. Un email envoye par cette classe aura la forme suivante:

//                            -------------- What Happened --------------
//                              [Description de ce qui s'est passe]
//
//                            -------------- How to Reproduce ---------------
//                              [Comment reproduire le bug]
//
//                            OS: [systeme d'exploitation]
//                            Version: [version du programme]

                            EmailSender.sendEmailViaGmail("pongbugreporter@gmail.com", "java.verify", "pongbugreporter@gmail.com",
                                    "Bug Report: " + title.getText(),
                                    "-------------- What Happened --------------\n" +
                                            whatHappened.getText() +
                                            "\n\n-------------- How to Reproduce ---------------\n" +
                                            reproduceText.getText() + "\n\n" +
                                    "OS: " + System.getProperty("os.name") + "\n" +
                                    "Version: " + AboutScreen.VERSION_NUMBER + "\n\n");

                        // il y a une capture d'ecran
                        } else {
                            EmailSender.sendEmailViaGmail("pongbugreporter@gmail.com", "java.verify", "pongbugreporter@gmail.com",
                                    "Bug Report: " + title.getText(),
                                    "-------------- What Happened --------------\n" +
                                            whatHappened.getText() +
                                            "\n\n-------------- How to Reproduce ---------------\n" +
                                            reproduceText.getText() + "\n\n" +
                                            "OS: " + System.getProperty("os.name") + "\n\n" +
                                            "Version: " + AboutScreen.VERSION_NUMBER + "\n\n",
                                    attachment);
                        }

                        // On affiche un message que le bug a ete envoye
                        getContentPane().removeAll();
                        // Il faut avoir les trois lignes suivantes, sinon les nouvelles composantes ne s'affichent pas.
                        getContentPane().setVisible(false);
                        getContentPane().setVisible(true);
                        getContentPane().repaint();
                        GridBagConstraints c = new GridBagConstraints();
                        JButton okButton = new JButton(lang.get("ok"));
                        okButton.setAlignmentX(RIGHT_ALIGNMENT);
                        okButton.setAlignmentY(BOTTOM_ALIGNMENT);
                        okButton.setFocusable(false);

                        okButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                dispose();
                            }
                        });
                        getRootPane().setDefaultButton(okButton);

                        c.gridx = 0;
                        c.gridy = 0;

                        add(new JLabel(lang.get("bugReportSubmitted")), c);

                        c.anchor = GridBagConstraints.SOUTH;
                        c.gridy++;
                        add(okButton, c);

                        getContentPane().setVisible(false);
                        getContentPane().setVisible(true);
                        getContentPane().repaint();
                    }
                });
                t.start();
            }
        });


        // On presente a l'utilisateur une fenetre pour choisir une capture d'ecran et met le chemin d'acces de
        // l'image que l'utilisateur a choisie dans la variable attachment.
        screenShotButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int i = screenShotChooser.showOpenDialog(null);
                if (i == JFileChooser.APPROVE_OPTION) {
                    attachment = screenShotChooser.getSelectedFile().getAbsolutePath();
                    attachmentLabel.setText(screenShotChooser.getSelectedFile().getName());
                }
            }
        });
    }

    /**
     * Ajoute les composantes a l'interface graphique.
     */
    private void addComponents() {

        // On veut une fenetre comme celle-ci:
        //
        // |--------------------------------------|
        // |--------------------------------------|
        // |Titre:                                |
        // | ___________________________________  |
        // |                                      |
        // |Ce qui s'est passe:                   |
        // | ___________________________________  |
        // |                                      |
        // |Comment reproduire le bug:            |
        // | ___________________________________  |
        // |  _______________       _______   __  |
        // | |Ajouter capture|     |Annuler| |OK| |
        // |______________________________________|

        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 4;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(10, 5, 5, 5);
        add(titleLabel, c);
        c.insets = new Insets(5, 5, 5, 5);
        c.gridy++;
        add(title, c);
        c.gridy++;
        c.insets = new Insets(15, 5, 5, 5);
        add(whatHappenedLabel, c);
        c.gridy++;
        c.insets = new Insets(5, 5, 5, 5);
        add(whatHappenedPane, c);
        c.gridy++;
        c.insets = new Insets(15, 5, 5, 5);
        add(reproduceLabel, c);
        c.gridy++;
        c.insets = new Insets(5, 5, 5, 5);
        add(reproduceTextPane, c);
        c.gridx = 0;
        c.insets = new Insets(10, 0, 0, 0);
        c.gridy++;
        c.gridwidth = 2;
        add(screenShotButton, c);
        c.gridx += 2;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.NONE;
        add(attachmentLabel, c);
        c.gridx += 1;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.EAST;
        add(buttonPanel, c);
    }
}