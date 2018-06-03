package pong.paddle;

import pong.missile.PongMissile;
import pong.mode.TwoPlayerBattlePongCanvas;
import pong.sound.PongSoundPlayer;

import java.awt.*;

public class BattlePaddle extends Paddle {

    private boolean hasFiredMissile = false;
    private PongSoundPlayer missileFiredSound;
    private boolean destroyed = false;
    private PongMissile missile;
    private double oldPaddleHeight;
    private double oldPaddleOffset;
    private TwoPlayerBattlePongCanvas battlePongCanvas;

    public BattlePaddle(TwoPlayerBattlePongCanvas battlePongCanvas, double paddleX, double paddleY, double paddleWidth, double paddleHeight, int side, PaddleMode paddleMode) {

        super(battlePongCanvas, paddleX, paddleY, paddleWidth, paddleHeight, side, paddleMode);
        this.battlePongCanvas = battlePongCanvas;
        this.missile = new PongMissile(this, 20, 10);
        this.missileFiredSound = new PongSoundPlayer("missileFired.wav");
    }

    public void tryToPrepareNewMissile() {

        if (!hasFiredMissile && !destroyed) {
            missile.reset();
            missile.setMissileY(getYCenter());
            hasFiredMissile = true;
            missileFiredSound.playSound();
        }
    }

    public PongMissile getMissile() {

        return missile;
    }

    public boolean hasFiredMissile() {

        return hasFiredMissile;
    }

    public void setHasFiredMissile(boolean hasFiredMissile) {

        this.hasFiredMissile = hasFiredMissile;
    }

    public void moveMissile(double speed) {

        if (hasFiredMissile) {

            missile.move(speed);
        }
    }

    public void destroy() {

        destroyed = true;
        Rectangle oldBounds = getBounds();
        setMoveable(false);
        oldPaddleHeight = getPaddleHeight();
        oldPaddleOffset = getSide() == LEFT ? getX() : battlePongCanvas.getWidth() - getX();
        System.out.println(oldPaddleOffset);
        setPaddleHeight(-1);
        setPaddleX(getSide() == LEFT ? battlePongCanvas.getWidth() + 1000 : -1000);
        battlePongCanvas.repaint(oldBounds);
    }

    public void undestroy() {

        if (destroyed) {

            destroyed = false;
            Rectangle oldBounds = getBounds();
            setPaddleHeight(oldPaddleHeight);
            setPaddleX(getSide() == LEFT ? oldPaddleOffset : battlePongCanvas.getWidth() - oldPaddleOffset);
            setMoveable(true);
            battlePongCanvas.repaint(oldBounds);
            battlePongCanvas.repaint(getBounds());
        }
    }

    @Override
    public void deinit() {

        super.deinit();
        missileFiredSound.deinit();
    }
}