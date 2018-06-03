// File: FontLoader.java
// Author: Leo
// Date Created: 5/1/15 3:23 PM

package pong.screen;

import java.awt.*;
import java.io.IOException;

/**
 * Classe qui permet de chercher une police d'ecriture depuis le dossier ou se trouve cette classe.
 */
public final class FontLoader {

    /**
     * Permet de chercher une police d'ecriture TrueType.
     * @param fontFile le nom du fichier ou se trouve la police.
     * @param style le style (Font.PLAIN, Font.BOLD, etc.)
     * @param size la taille de la police
     */
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