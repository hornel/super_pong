package pong.lang;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Classe qui permet de localiser le programme.
 */
public final class Language {

    /**
     * Permet de transformer un fichier de localisation donne (parametre localization) en Map utilisable par
     * un programme. Le fichier doit se trouver dans le meme dossier que cette classe. Il suffit de donner le nom
     * du fichier; il ne faut pas donner le chemin d'acces.
     * <p>
     * Le format des fichiers de localization est le suivant:
     * <p>
     * {@code identificateur: texte}
     * <p>
     * Il faut faire attention a l'espace apres le double-point.<p>
     * Les commentaires sont precedes par #.<p>
     * Les lignes blanches sont tolerees.<p>
     * Les espaces avant l'identificateur seront pris en compte dans le nom de l'identificateur.<p>
     * Les espaces apres l'identificateur ou avant le texte (a part l'espace apres le double-point)
     * seront pris en compte dans le texte.<p>
     * Les espaces apres le texte seront pris en compte dans le texte.<p>
     * Si le texte prend plusieurs lignes, il faut terminer chaque ligne a part la derniere par un \.<p>
     */
    public static Map<String, String> getLanguageMap(String localization) {

        // Map ou on va stocker les couples <identificateur, texte>.
        HashMap<String, String> values = new HashMap<String, String>();
        // InputStream pour lire le fichier de localisation.
        InputStream locStream = Language.class.getResourceAsStream(localization);
        try {
            // permet de lire le fichier ligne par ligne.
            Scanner sc = new Scanner(locStream, "UTF-8").useDelimiter("\\A");
            // tant qu'il y a encore des lignes a lire
            while (sc.hasNextLine()) {
                // ligne en train d'etre lue
                String currentLine = sc.nextLine();
                // Si la ligne est un commentaire ou une ligne blanche, il faut la sauter.
                while (currentLine.startsWith("#") || currentLine.matches(" *")) {
                    currentLine = sc.nextLine();
                }
                // Si la ligne contient un : et n'est pas un commentaire, il faut la mettre dans le Map.
                if (currentLine.contains(": ") && !currentLine.startsWith("#")) {
                    // On divise la ligne en identificateur et texte.
                    String[] splitValue = currentLine.split(": ", 2);
                    // tant que la ligne finit par un \ et il reste encore des lignes, il faut ajouter la ligne au texte.
                    while (currentLine.endsWith("\\") && sc.hasNextLine()) {
                        currentLine = sc.nextLine();
                        splitValue[1] = splitValue[1].substring(0, splitValue[1].length() - 1) + "\n" + currentLine;
                    }
                    // On ajoute identificateur et texte au Map.
                    values.put(splitValue[0], splitValue[1]);
                // Si la ligne ne contient pas de ": " et n'est ni un commentaire ni une ligne blanche, le fichier de
                // localisation n'est pas valide.
                } else {
                    throw new LanguageMapException("The language file " + localization + " contains the following unrecognized line:\n" + currentLine);
                }
            }
            // S'il manque des identificateurs par rapport au fichier de localisation de base "en-US.loc",
            // le fichier de localisation n'est pas valide.
            if (!values.keySet().equals(getAllKeyValues())) {
                throw new LanguageMapException("The language file " + localization + " is missing entries, contains\nadditional entries or contains unrecognized entries.");
            }
            locStream.close();
            sc.close();
            return values;

        // Si le fichier de localisation est invalide, il faut afficher un message pourquoi et utiliser le fichier de
        // localisation de reference "en-US.loc".
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "There was a problem reading the language file" + localization + ".", "Error", JOptionPane.ERROR_MESSAGE);
            return getLanguageMap("en-US.loc");
        } catch (LanguageMapException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return getLanguageMap("en-US.loc");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "There was an unknown problem reading the language file " + localization + " or it cannot be found.", "Error", JOptionPane.ERROR_MESSAGE);
            return getLanguageMap("en-US.loc");
        }
    }

    // Permet de chercher les identificateurs de reference pour les comparer au fichier donne.
    private static Set<String> getAllKeyValues() {

        // On cherche tout simplement les identificateurs du fichier de localisation "en-US.loc" et on les prend
        // comme reference. Cela se fait de pratiquement la meme maniere que de chercher un autre fichier de localisation,
        // mais on ne s'interesse qu'aux identificateurs.

        HashSet<String> keys = new HashSet<String>();
        InputStream locStream = Language.class.getResourceAsStream("en-US.loc");
        Scanner sc = new Scanner(locStream, "UTF-8").useDelimiter("\\A");  // reads InputStream into Scanner with delimiter \A (beginning of input)
        while (sc.hasNextLine()) {
            String currentLine = sc.nextLine();
            while (currentLine.startsWith("#") || currentLine.matches(" *")) {
                currentLine = sc.nextLine();
            }
            String[] splitValue = currentLine.split(": ", 2);
            while (currentLine.endsWith("\\") && sc.hasNextLine()) {
                currentLine = sc.nextLine();
            }
            keys.add(splitValue[0]);
        }
        try {
            locStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        sc.close();
        return keys;
    }

    // Permet de chercher le nom du fichier de localisation donne dans le fichier "loc.conf".
    // Change aussi la localisation de la JVM par rapport au nom du fichier.
    // Exemple: en-US:
    // - en: Anglais.
    // - US: Etats-Unis.
    public static String getLoc() {

        Scanner sc = new Scanner(Language.class.getResourceAsStream("loc.conf"), "UTF-8").useDelimiter("\\A");
        String loc = sc.next();
        sc.close();
        Locale.setDefault(new Locale(loc.split("-")[0], loc.split("-")[1]));
        return loc + ".loc";
    }
}

/**
 * Exception lancee lorsque le fichier de localisation n'est pas valide.
 */
class LanguageMapException extends Exception {

    public LanguageMapException(String message) {

        super(message);
    }
}