package pong.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class LButton extends LAbstractButton {

    private JLabel text;
    private int padding = 3;

    private LButton() {

        setLayout(new BorderLayout());
    }

    public LButton(String text) {

        this();
        this.text = new JLabel(text);
        Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
        this.text.setFont(font);
        this.text.setForeground(Color.WHITE);
        this.text.setVerticalAlignment(JLabel.CENTER);
        this.text.setHorizontalAlignment(JLabel.CENTER);
        this.setTextPadding(5, 5, 5, 5);
        add(this.text, BorderLayout.CENTER);
    }

    public LButton(String text, Color activeColor) {

        this(text);
        this.setActiveColor(activeColor);
    }

    @Override
    public void buildBackgroundArea() {

        setBackgroundShape(new Rectangle2D.Double(padding, padding, getWidth() - padding * 2, getHeight() - padding * 2));
    }

    public void setButtonPadding(int padding) {

        this.padding = padding;
    }

    public void setTextFont(Font font) {

        this.text.setFont(font);
    }

    public void setTextColor(Color color) {

        this.text.setForeground(color);
    }

    public void setTextPadding(int top, int left, int bottom, int right) {

        this.text.setBorder(BorderFactory.createEmptyBorder(top + padding, left + padding, bottom + padding, right + padding));
    }

    public void setText(String text) {

        this.text.setText(text);
        this.text.repaint();
    }
}