import pong.lang.Language;

import java.util.Map;

public class TestLoc {

    public void getUrl() {

        System.out.println(Language.getLanguageMap(Language.getLoc()).get("startScreenText"));
    }

    public static void main(String[] args) {

        TestLoc t = new TestLoc();
        t.getUrl();
    }
}
