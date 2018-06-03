package pong.mode;

public enum PongMode {

    SINGLE_PLAYER_TRAINING {
        @Override
        public PongCanvas getCorrespondingPongCanvas() {

            return new SinglePlayerTrainingPongCanvas();
        }
    },

    SINGLE_PLAYER_ARKANOID {
        @Override
        public PongCanvas getCorrespondingPongCanvas() {

            return new SinglePlayerArkanoidPongCanvas();
        }
    },

    DUAL_PLAYER_HUMAN_VS_HUMAN {
        @Override
        public PongCanvas getCorrespondingPongCanvas() {

            return new TwoHumanPlayerPongCanvas();
        }
    },

    DUAL_PLAYER_HUMAN_VS_AI {
        @Override
        public PongCanvas getCorrespondingPongCanvas() {

            return new TwoAIAndHumanPlayerPongCanvas();
        }
    },

    DUAL_PLAYER_HUMAN_VS_HUMAN_BATTLE {
        @Override
        public PongCanvas getCorrespondingPongCanvas() {

            return new TwoPlayerBattlePongCanvas();
        }
    };


    public abstract PongCanvas getCorrespondingPongCanvas();
}