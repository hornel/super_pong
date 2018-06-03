// File: LRadioButtonGroup.java
// Author: Leo
// Date Created: 4/26/15 10:03 PM

package pong.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class LRadioButtonGroup {

    private Vector<LRadioButton> radioButtons = new Vector<LRadioButton>();
    private ActionListener selectedListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

            LRadioButton source = (LRadioButton) e.getSource();
            checkSelected(source);
        }
    };

    public void add(LRadioButton r) {

        radioButtons.add(r);
        r.addActionListener(selectedListener);
    }

    public void remove(LRadioButton r) {

        radioButtons.remove(r);
        r.removeActionListener(selectedListener);
    }

    private void checkSelected(LRadioButton r) {

        r.setSelected(true);
        for (LRadioButton rb : radioButtons) {

            if (!rb.equals(r)) {
                rb.setSelected(false);
            }
        }
    }
}
