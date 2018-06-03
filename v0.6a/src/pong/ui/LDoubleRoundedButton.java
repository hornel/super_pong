// File: LDoubleRoundedButton.java
// Author: Leo
// Date Created: 4/30/15 10:04 PM

package pong.ui;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

public class LDoubleRoundedButton extends LButton {

    public LDoubleRoundedButton(String text) {

        super(text);
        setTextPadding(3, 10, 3, 10);
        setActiveColor(Color.GRAY.darker());
    }

    @Override
    public void buildBackgroundArea() {

        int arcSize = getHeight() / 2;

        RoundRectangle2D mainRect = new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), arcSize, arcSize);
        Rectangle2D coverBottomLeft = new Rectangle2D.Double(0, getHeight() - arcSize, arcSize, arcSize);
        Rectangle2D coverTopRight = new Rectangle2D.Double(getWidth() - arcSize, 0, arcSize, arcSize);

        setBackgroundShape(mainRect, coverBottomLeft, coverTopRight);
    }
}
