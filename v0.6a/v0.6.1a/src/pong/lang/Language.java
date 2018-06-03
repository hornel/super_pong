package pong.lang;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public final class Language {

    public static Map<String, String> getLanguageMap(String localization) {

        HashMap<String, String> values = new HashMap<String, String>();
        InputStream locStream = Language.class.getResourceAsStream(localization);
        Scanner sc = new Scanner(locStream, "UTF-8").useDelimiter("\\A");  // reads InputStream into Scanner with delimiter \A (beginning of input)
        while (sc.hasNextLine()) {
            String[] splitValue = sc.nextLine().split(": ", 2);
            values.put(splitValue[0], splitValue[1]);
        }
        try {
            locStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        sc.close();
        return values;
    }

    public static String getLoc() {

        Scanner sc = new Scanner(Language.class.getResourceAsStream("loc.conf"), "UTF-8").useDelimiter("\\A");
        String loc = sc.next();
        sc.close();
        return loc + ".loc";
    }

//    public static void writeLoc(String loc) {
//
//        PrintWriter writer = new PrintWriter("loc.conf")
//    }
}
