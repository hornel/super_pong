// File: LCheckBox2.java
// Author: Leo
// Date Created: 4/26/15 5:44 PM

package pong.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

public class LCheckBox extends LToggleButton {

    public LCheckBox(String text) {

        super(text);
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                setSelected(!isSelected());
            }
        });
    }

    @Override
    protected void paintSelected(Graphics2D g2d) {

        g2d.draw(new Line2D.Double(getButtonSize() / 4 + getLeftInset(), getButtonSize() / 2 + getDrawingStartPoint(), getButtonSize() / 3 + getLeftInset(), getButtonSize() * (3.0 / 4) + getDrawingStartPoint()));
        g2d.draw(new Line2D.Double(getButtonSize() / 3 + getLeftInset(), getButtonSize() * (3.0 / 4) + getDrawingStartPoint(), getButtonSize() * (3.0 / 4) + getLeftInset(), getButtonSize() / 4 + getDrawingStartPoint()));
        repaint();
    }

    @Override
    public void buildBackgroundArea() {

        setBackgroundShape(new Rectangle2D.Double(getLeftInset(), getDrawingStartPoint(), getButtonSize(), getButtonSize()));
    }
}