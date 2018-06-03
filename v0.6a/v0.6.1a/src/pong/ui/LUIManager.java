package pong.ui;

import java.awt.*;
import java.util.ArrayList;

public class LUIManager {

    private static ArrayList<LAbstractButton> buttons = new ArrayList<LAbstractButton>();
    private static Color backgroundColor = Color.GRAY;
    private static Color foregroundColor = Color.WHITE;
    private static final Color DEFAULT_BACKGROUND_COLOR = backgroundColor;
    private static final Color DEFAULT_FOREGROUND_COLOR = foregroundColor;

    public static void add(LAbstractButton l) {

        buttons.add(l);
    }

    public static void setBackgroundColor(Color color) {

        backgroundColor = color;
        for (LAbstractButton l : buttons) {

            l.setActiveColor(color);
        }
    }

    public static void setForegroundColor(Color color) {

        foregroundColor = color;
        for (LAbstractButton l : buttons) {

            l.setForegroundColor(color);
        }
    }

    public static Color getBackgroundColor() {
        return backgroundColor;
    }

    public static Color getForegroundColor() {
        return foregroundColor;
    }

    public static void reset() {

        backgroundColor = DEFAULT_BACKGROUND_COLOR;
        for (LAbstractButton l : buttons) {

            l.setActiveColor(DEFAULT_BACKGROUND_COLOR);
        }

        foregroundColor = DEFAULT_FOREGROUND_COLOR;
        for (LAbstractButton l : buttons) {

            l.setForegroundColor(DEFAULT_FOREGROUND_COLOR);
        }
    }
}
