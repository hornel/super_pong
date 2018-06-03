// File: LRadioButton2.java
// Author: Leo
// Date Created: 4/26/15 6:53 PM

package pong.ui;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class LRadioButton extends LToggleButton {

    public LRadioButton(String text) {

        super(text);
    }

    @Override
    protected void paintSelected(Graphics2D g2d) {

        g2d.fill(getEllipseFromCenter(getButtonSize() / 3));
    }

    private Ellipse2D getEllipseFromCenter(double size) {

        double x = getButtonSize() / 2 + getLeftInset();
        double y = getButtonSize() / 2 + getDrawingStartPoint();

        double newX = x - size / 2.0;
        double newY = y - size / 2.0;

        return new Ellipse2D.Double(newX, newY, size, size);
    }

    @Override
    public void buildBackgroundArea() {

        setBackgroundShape(new Ellipse2D.Double(getLeftInset(), getDrawingStartPoint(), getButtonSize(), getButtonSize()));
    }
}