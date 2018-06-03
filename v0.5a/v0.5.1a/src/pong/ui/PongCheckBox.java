// File: PongCheckBox.java
// Author: Leo
// Date Created: 4/11/15 3:06 PM

package pong.ui;

import javax.swing.*;

public class PongCheckBox extends JCheckBox {

    public PongCheckBox(String text) {

        setFocusable(false);
        setText(text);
    }
}
