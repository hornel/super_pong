// File: PongPreferencesEditor.java
// Author: Leo Horne
// Date Created: Saturday, March 7, 2015
// Version: 0.1

package pong;

import java.awt.*;

class PongPreferencesEditor {

    private PongCanvas pongCanvas;
    Color foregroundColor = Color.WHITE;
    Color backgroundColor = Color.BLACK;

    public PongPreferencesEditor(PongCanvas pongCanvas) {

        this.pongCanvas = pongCanvas;
    }

    public void setBackgroundColor(Color color) {

        pongCanvas.backgroundColor = color;
    }

    public void setForegroundColor(Color color) {

        pongCanvas.foregroundColor = color;
    }
}