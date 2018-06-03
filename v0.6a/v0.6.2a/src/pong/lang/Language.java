package pong.lang;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public final class Language {

    public static Map<String, String> getLanguageMap(String localization) {

        HashMap<String, String> values = new HashMap<String, String>();
        InputStream locStream = Language.class.getResourceAsStream(localization);
        try {
            Scanner sc = new Scanner(locStream, "UTF-8").useDelimiter("\\A");  // reads InputStream into Scanner with delimiter \A (beginning of input)
            while (sc.hasNextLine()) {
                String currentLine = sc.nextLine();
                if (currentLine.contains(": ")) {
                    String[] splitValue = currentLine.split(": ", 2);
                    while (currentLine.endsWith("\\") && !currentLine.endsWith("\\\\") && sc.hasNextLine()) {
                        currentLine = sc.nextLine();
                        splitValue[1] = splitValue[1].substring(0, splitValue[1].length() - 1) + "\n" + currentLine;
                    }
                    values.put(splitValue[0], splitValue[1]);
                } else {
                    throw new LanguageMapException("The language file " + localization + " contains the following unrecognized line:\n" + currentLine);
                }
            }
            if (!values.keySet().equals(getAllKeyValues())) {
                throw new LanguageMapException("The language file " + localization + " is missing entries, contains\nadditional entries or contains unrecognized entries.");
            }
            locStream.close();
            sc.close();
            return values;
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

    private static Set<String> getAllKeyValues() {

        HashSet<String> keys = new HashSet<String>();
        InputStream locStream = Language.class.getResourceAsStream("en-US.loc");
        Scanner sc = new Scanner(locStream, "UTF-8").useDelimiter("\\A");  // reads InputStream into Scanner with delimiter \A (beginning of input)
        while (sc.hasNextLine()) {
            String currentLine = sc.nextLine();
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

    public static String getLoc() {

        Scanner sc = new Scanner(Language.class.getResourceAsStream("loc.conf"), "UTF-8").useDelimiter("\\A");
        String loc = sc.next();
        sc.close();
        Locale.setDefault(new Locale(loc.split("-")[0], loc.split("-")[1]));
        return loc + ".loc";
    }

//    public static void writeLoc(String loc) {
//
//        PrintWriter writer = new PrintWriter("loc.conf")
//    }
}

class LanguageMapException extends Exception {

    public LanguageMapException(String message) {

        super(message);
    }
}