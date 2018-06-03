package pong.mode;

import pong.control.tabs.BricksPanel;
import pong.control.tabs.WallPanel;
import pong.control.tabs.MissilePanel;

public enum PongMode {

    SINGLE_PLAYER_TRAINING {
        @Override
        public PongCanvas getCorrespondingPongCanvas() {

            return new SinglePlayerTrainingPongCanvas();
        }

        @Override
        public String[] getCustomTabClassNames() {

            return new String[] {WallPanel.class.getName()};
        }
    },

    SINGLE_PLAYER_ARKANOID {
        @Override
        public PongCanvas getCorrespondingPongCanvas() {

            return new SinglePlayerArkanoidPongCanvas();
        }

        @Override
        public String[] getCustomTabClassNames() {

            return new String[] {BricksPanel.class.getName()};
        }
    },

    DUAL_PLAYER_HUMAN_VS_HUMAN {
        @Override
        public PongCanvas getCorrespondingPongCanvas() {

            return new TwoHumanPlayerPongCanvas();
        }

        @Override
        public String[] getCustomTabClassNames() {
            return new String[0];
        }
    },

    DUAL_PLAYER_HUMAN_VS_AI {
        @Override
        public PongCanvas getCorrespondingPongCanvas() {

            return new TwoAIAndHumanPlayerPongCanvas();
        }

        @Override
        public String[] getCustomTabClassNames() {
            return new String[0];
        }
    },

    DUAL_PLAYER_HUMAN_VS_HUMAN_BATTLE {
        @Override
        public PongCanvas getCorrespondingPongCanvas() {

            return new TwoPlayerBattlePongCanvas();
        }

        @Override
        public String[] getCustomTabClassNames() {
            return new String[] {MissilePanel.class.getName()};
        }
    };

    public abstract PongCanvas getCorrespondingPongCanvas();

    public abstract String[] getCustomTabClassNames();
}