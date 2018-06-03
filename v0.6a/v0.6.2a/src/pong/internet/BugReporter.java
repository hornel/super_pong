package pong.internet;

import pong.lang.Language;
import pong.screen.AboutScreen;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class BugReporter extends JFrame {

    private JLabel titleLabel;
    private JLabel whatHappenedLabel;
    private JLabel reproduceLabel;
    private JLabel nameLabel;
    private JTextField title;
    private JTextArea whatHappened;
    private JTextArea reproduceText;
    private JTextField name;
    private JScrollPane titlePane;
    private JScrollPane whatHappenedPane;
    private JScrollPane reproduceTextPane;
    private JButton submit;
    private JButton cancel;
    private JButton screenShotButton;
    private JPanel buttonPanel;
    private JFileChooser screenShotChooser;
    private String attachment = null;
    private JLabel attachmentLabel;
    private Map<String, String> lang = Language.getLanguageMap(Language.getLoc());

    public BugReporter() {

        initGUI();
        initComponents();
        addListeners();
        addComponents();
        pack();
        attachmentLabel.setPreferredSize(new Dimension(getWidth() - screenShotButton.getWidth() - submit.getWidth() - cancel.getWidth() - 30, 20));
        setLocationRelativeTo(null);
    }

    private void initComponents() {

        buttonPanel = new JPanel();

        titleLabel = new JLabel(lang.get("title"));
        whatHappenedLabel = new JLabel(lang.get("whatHappenedString"));
        reproduceLabel = new JLabel(lang.get("reproduceString"));
        nameLabel = new JLabel(lang.get("name"));
        attachmentLabel = new JLabel();
        attachmentLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));

        title = new JTextField(39);
        name = new JTextField(32);

        whatHappened = new JTextArea(10, 40);
        whatHappened.setLineWrap(true);
        whatHappened.setWrapStyleWord(true);

        reproduceText = new JTextArea(10, 40);
        reproduceText.setLineWrap(true);
        reproduceText.setWrapStyleWord(true);

        titlePane = new JScrollPane(title);
        whatHappenedPane = new JScrollPane(whatHappened);
        whatHappened.setFocusable(true);
        reproduceTextPane = new JScrollPane(reproduceText);

        submit = new JButton(lang.get("submit"));
        submit.setFocusable(false);
        cancel = new JButton(lang.get("cancel"));
        cancel.setFocusable(false);
        buttonPanel.add(cancel);
        buttonPanel.add(submit);

        screenShotButton = new JButton(lang.get("addScreenshot"));
        screenShotButton.setFocusable(false);

        screenShotChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "png", "gif", "jpeg", "bmp", "tiff");
        screenShotChooser.setFileFilter(filter);

        getRootPane().setDefaultButton(submit);
    }

    private void initGUI() {

        setTitle(lang.get("reportBug").substring(0, lang.get("reportBug").length() - 3));
        setLayout(new GridBagLayout());
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
    }

    private void addListeners() {

        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getContentPane().removeAll();
                getContentPane().revalidate();
                getContentPane().repaint();
                JProgressBar j = new JProgressBar();
                j.setIndeterminate(true);
                GridBagConstraints c = new GridBagConstraints();
                c.gridx = 0;
                c.gridy = 0;
                add(j, c);
                c.gridy++;
                add(new JLabel(lang.get("sendingBugReport")), c);
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (attachment == null) {
                            EmailSender.sendEmailViaGmail("pongbugreporter@gmail.com", "java.verify", "pongbugreporter@gmail.com",
                                    "Bug Report: " + title.getText(),
                                    "-------------- What Happened --------------\n" +
                                            whatHappened.getText() +
                                            "\n\n-------------- How to Reproduce ---------------\n" +
                                            reproduceText.getText() + "\n\n" +
                                    "Sender: " + name.getText() + "\n" +
                                    "OS: " + System.getProperty("os.name") + "\n" +
                                    "Version: " + AboutScreen.VERSION_NUMBER + "\n\n");
                        } else {
                            EmailSender.sendEmailViaGmail("pongbugreporter@gmail.com", "java.verify", "pongbugreporter@gmail.com",
                                    "Bug Report: " + title.getText(),
                                    "-------------- What Happened --------------\n" +
                                            whatHappened.getText() +
                                            "\n\n-------------- How to Reproduce ---------------\n" +
                                            reproduceText.getText() + "\n\n" +
                                            "Sender: " + name.getText() + "\n" +
                                            "OS: " + System.getProperty("os.name") + "\n\n" +
                                            "Version: " + AboutScreen.VERSION_NUMBER + "\n\n",
                                    attachment);
                        }
                        getContentPane().removeAll();
                        getContentPane().revalidate();
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

                        add(new JLabel("Bug report submitted."), c);

                        c.anchor = GridBagConstraints.SOUTH;
                        c.gridy++;
                        add(okButton, c);

                        revalidate();
                        repaint();
                    }
                });
                t.start();
            }
        });


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

    private void addComponents() {

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
        c.gridy++;
        c.gridwidth = 1;
        add(nameLabel, c);
        c.gridx++;
        c.gridwidth = 3;
        add(name, c);
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

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                BugReporter b = new BugReporter();
                b.setVisible(true);
            }
        });
    }
}