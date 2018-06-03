// File: LToggleButton2.java
// Author: Leo
// Date Created: 4/26/15 5:36 PM

package pong.ui;

import javax.swing.*;
import java.awt.*;

public abstract class LToggleButton extends LAbstractButton {

    private JLabel text;
    private double size = 12;
    private boolean selected = false;
    private Color selectedColor = getForegroundColor();
    private int leftInset = 5;

    private LToggleButton() {

        setLayout(new GridLayout(0, 1));
    }

    public LToggleButton(String text) {

        this();
        this.text = new JLabel(text);
        Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
        this.text.setFont(font);
        this.text.setForeground(Color.BLACK);
        this.text.setVerticalAlignment(JLabel.CENTER);
        this.text.setBorder(BorderFactory.createEmptyBorder(0, (int) size + leftInset + 4, 0, 0));

        add(this.text);
        setBorder(BorderFactory.createEmptyBorder(2, 0, 2, leftInset));
    }

    protected double getDrawingStartPoint() {

        double containerMidpoint = getHeight() / 2;
        return containerMidpoint - (size / 2);
    }

    public double getButtonSize() {

        return size;
    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        if (selected) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(selectedColor);
            paintSelected(g2d);
        }
    }

    public boolean isSelected() {

        return selected;
    }

    public void setSelected(boolean selected) {

        this.selected = selected;
        repaint();
    }

    public int getLeftInset() {

        return leftInset;
    }

    protected abstract void paintSelected(Graphics2D g2d);

    @Override
    public void refreshColors() {

        selectedColor = LUIManager.getForegroundColor();
    }
}