// File: TestSlider.java
// Author: Leo
// Date Created: 5/2/15 9:54 AM

package pong.ui;

import javax.swing.*;
import java.awt.*;

public class TestSlider extends JFrame {

    public TestSlider() {

        setLayout(new BorderLayout());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        LSlider ls = new LSlider(30, 60, 39, 2);
        add(ls, BorderLayout.CENTER);
        setSize(200, 50);
        ls.setValue(100);
    }

    public static void main(String[] args) {

        TestSlider testSlider = new TestSlider();
        testSlider.setVisible(true);
    }
}
