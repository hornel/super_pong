// File: FontLoader.java
// Author: Leo
// Date Created: 5/1/15 3:23 PM

package pong.screen;

import java.awt.*;
import java.io.IOException;

public final class FontLoader {

    public static Font loadTrueTypeFont(String fontFile, int style, int size) {

        Font font = null;

        try {
            font = Font.createFont(Font.TRUETYPE_FONT, FontLoader.class.getResourceAsStream(fontFile)).deriveFont(style, size);
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return font;
    }
}