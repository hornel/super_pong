// File: FontLoader.java
// Author: Leo
// Date Created: 5/1/15 3:23 PM

package pong.ui.font;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public final class FontLoader {

    public static Font loadTrueTypeFont(String fontFile) {

        Font font = null;

        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File(fontFile));
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return font;
    }
}