package pong.mode;

import pong.control.tabs.*;

import java.lang.reflect.Constructor;

public enum PongMode {

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
    },

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
    },

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
    },

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
    },

    DUAL_PLAYER_HUMAN_VS_HUMAN_BATTLE {
        @Override
        public Constructor<?> getCorrespondingPongCanvasConstructor() {

            try {
                return TwoPlayerBattlePongCanvas.class.getConstructor();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public String[] getCustomTabClassNames() {
            return new String[] {ColorSchemePanel.class.getName(), BallPanel.class.getName(), PaddlePanel.class.getName(), ScorePanel.class.getName(), PhysicsPanel.class.getName(),  MissilePanel.class.getName()};
        }
    };

    public abstract Constructor<?> getCorrespondingPongCanvasConstructor();

    public abstract String[] getCustomTabClassNames();
}