package pong.mode;

import pong.control.tabs.*;
import pong.lang.Language;

import java.lang.reflect.Constructor;
import java.util.Map;

/**
 * Enumeration decrivant les differents modes de jeu.
 */
public enum PongMode {

    /**
     * Le mode d'entrainement.
     */
    SINGLE_PLAYER_TRAINING {
        @Override
        public Constructor<?> getCorrespondingPongCanvasConstructor() {

            try {
                return SinglePlayerTrainingPongCanvas.class.getConstructor();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public String[] getCustomTabClassNames() {

            return new String[] {ColorSchemePanel.class.getName(), BallPanel.class.getName(), PaddlePanel.class.getName(), PhysicsPanel.class.getName(), WallPanel.class.getName()};
        }

        @Override
        public String getUserFriendlyName() {

            return lang.get("sgTraining");
        }
    },

    /**
     * Le mode casse-brique.
     */
    SINGLE_PLAYER_BREAKOUT {
        @Override
        public Constructor<?> getCorrespondingPongCanvasConstructor() {

            try {
                return SinglePlayerBreakoutPongCanvas.class.getConstructor(int.class, int.class);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public String[] getCustomTabClassNames() {

            return new String[] {ColorSchemePanel.class.getName(), BallPanel.class.getName(), PaddlePanel.class.getName(), PhysicsPanel.class.getName(), BricksPanel.class.getName()};
        }

        @Override
        public String getUserFriendlyName() {

            return lang.get("sgArkanoid");
        }
    },

    /**
     * Le mode homme vs. homme.
     */
    DUAL_PLAYER_HUMAN_VS_HUMAN {
        @Override
        public Constructor<?> getCorrespondingPongCanvasConstructor() {

            try {
                return TwoHumanPlayerPongCanvas.class.getConstructor();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public String[] getCustomTabClassNames() {
            return new String[] {ColorSchemePanel.class.getName(), BallPanel.class.getName(), PaddlePanel.class.getName(), ScorePanel.class.getName(), PhysicsPanel.class.getName()};
        }

        @Override
        public String getUserFriendlyName() {

            return lang.get("dualHumanHuman");
        }
    },

    /**
     * Le mode homme vs. ordinateur.
     */
    DUAL_PLAYER_HUMAN_VS_AI {
        @Override
        public Constructor<?> getCorrespondingPongCanvasConstructor() {

            try {
                return TwoAIAndHumanPlayerPongCanvas.class.getConstructor();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public String[] getCustomTabClassNames() {
            return new String[] {ColorSchemePanel.class.getName(), BallPanel.class.getName(), PaddlePanel.class.getName(), ScorePanel.class.getName(), PhysicsPanel.class.getName(), AIPanel.class.getName()};
        }

        @Override
        public String getUserFriendlyName() {

            return lang.get("dualHumanComputer");
        }
    },

    /**
     * Le mode homme vs. homme - bataille (avec missiles)
     */
    DUAL_PLAYER_HUMAN_VS_HUMAN_BATTLE {
        @Override
        public Constructor<?> getCorrespondingPongCanvasConstructor() {

            try {
                return TwoHumanPlayerBattlePongCanvas.class.getConstructor();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public String[] getCustomTabClassNames() {
            return new String[] {ColorSchemePanel.class.getName(), BallPanel.class.getName(), PaddlePanel.class.getName(), ScorePanel.class.getName(), PhysicsPanel.class.getName(), MissilePanel.class.getName()};
        }

        @Override
        public String getUserFriendlyName() {

            return lang.get("dualHumanBattle");
        }
    },

    /**
     * Le mode homme vs. ordinateur - bataille (avec missiles)
     */
    DUAL_PLAYER_HUMAN_VS_AI_BATTLE {
        @Override
        public Constructor<?> getCorrespondingPongCanvasConstructor() {
            try {
                return TwoPlayerAIBattlePongCanvas.class.getConstructor();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public String[] getCustomTabClassNames() {
            return new String[] {ColorSchemePanel.class.getName(), BallPanel.class.getName(), PaddlePanel.class.getName(), ScorePanel.class.getName(), PhysicsPanel.class.getName(), AIPanel.class.getName(), MissilePanel.class.getName()};
        }

        @Override
        public String getUserFriendlyName() {

            return lang.get("dualAIBattle");
        }
    };

    private static Map<String, String> lang = Language.getLanguageMap(Language.getLoc());
    /**
     * Permet de chercher le constructeur du PongCanvas lie au PongMode.
     */
    public abstract Constructor<?> getCorrespondingPongCanvasConstructor();

    /**
     * Permet de chercher les noms des classes des onglets de preferences qui sont applicables au PongCanvas decrit.
     */
    public abstract String[] getCustomTabClassNames();

    /**
     * Permet de chercher le nom du mode visible par l'utilisateur.
     */
    public abstract String getUserFriendlyName();
}