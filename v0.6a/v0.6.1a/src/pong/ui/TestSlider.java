// File: TestSlider.java
// Author: Leo
// Date Created: 5/2/15 9:54 AM

package pong.ui;

import javax.swing.*;

public class TestSlider extends JFrame {

    public TestSlider() {

        setLayout(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        LSlider ls = new LSlider(10, 30, 22, 2);
        LSliderOld ls1 = new LSliderOld(10, 30, 20, 2);
        ls1.setBounds(50, 100, 100, 20);
        ls.setBounds(20, 20, 500, 50);
        add(ls);
        add(ls1);
        setSize(1000, 800);
    }

    public static void main(String[] args) {

        TestSlider testSlider = new TestSlider();
        testSlider.setVisible(true);
    }
}
