// File: SinglePlayerArkanoidPongCanvas.java
// Author: Leo
// Date Created: 4/12/15 3:21 PM

package pong.mode;

import pong.paddle.Paddle;

public class SinglePlayerArkanoidPongCanvas extends PongCanvas {

    @Override
    public int getNumberOfPaddles() {

        return 1;
    }

    @Override
    public Paddle[] getPaddles() {

        return new Paddle[0];
    }

    @Override
    void initPaddles() {

    }

    @Override
    void addListeners() {

    }

    @Override
    public PongMode getMode() {

        return null;
    }

    @Override
    public boolean isPlayer1Turn() {

        return false;
    }

    @Override
    public void setPlayer1Turn(boolean player) {

    }

    @Override
    void addCollisionDetectors() {

    }

    @Override
    public void increasePlayerScore(int player) {

    }

    @Override
    public int getPlayerScore(int player) {

        return 0;
    }

    @Override
    public int getMaxScore() {

        return 10;
    }
}