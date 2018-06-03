package pong.control.tabs;

import pong.control.PongPreferencesEditor;

import javax.swing.*;

public abstract class PongTabPanel extends JPanel {

    protected PongPreferencesEditor ppe;

    public abstract String getTabName();
}
