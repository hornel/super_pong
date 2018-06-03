// File: PongPreferencesEditor.java
// Author: Leo Horne
// Date Created: Saturday, March 7, 2015
// Version: 0.1

package pong.control;

import pong.ball.Ball;
import pong.lang.Language;
import pong.mode.PongCanvas;
import pong.mode.SinglePlayerArkanoidPongCanvas;
import pong.mode.SinglePlayerTrainingPongCanvas;
import pong.mode.TwoPlayerBattlePongCanvas;
import pong.paddle.Paddle;
import pong.paddle.PaddleMode;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.Map;

/**
 * Class to edit pong game attributes
 */

public class PongPreferencesEditor {

    public static final double MINIMUM_BALL_SPEED = 0.1;
    public static final double MAXIMUM_BALL_SPEED = 2.5;
    public static final double MINIMUM_BALL_SIZE = 5;
    public static final double MAXIMUM_BALL_SIZE = 150;
    public static final double MINIMUM_PADDLE_HEIGHT = 10;
    public static final double PADDING = 4;
    public static final double MAXIMUM_PADDLE_HEIGHT = PongCanvas.INITIAL_PANEL_HEIGHT - PADDING * 2;
    private PongCanvas pongCanvas;
    private Map<String, String> lang = Language.getLanguageMap(Language.getLoc());

    /**
     * Class constructor specifying pongCanvas to be edited.
     *
     * @param pongCanvas the pongCanvas to be edited.
     */
    public PongPreferencesEditor(PongCanvas pongCanvas) {

        this.pongCanvas = pongCanvas;
    }

    public double getCurrentBallSpeed() {

        return pongCanvas.getBall().getSpeed();
    }

    public double getCurrentBallSize() {

        return pongCanvas.getBall().getBallDiameter();
    }

    public double getCurrentPaddleHeight() {

        Paddle[] paddles = pongCanvas.getPaddles();
        double height = paddles[0].getPaddleHeight();
        for (Paddle paddle : paddles) {
            if (paddle.getPaddleMode().equals(PaddleMode.HUMAN) || paddle.getPaddleMode().equals(PaddleMode.AI)) {
                height = paddle.getPaddleHeight();
                break;
            }
        }
        return height;
    }

    public void setBallSpeed(double speed) {

        pongCanvas.getBall().setSpeed(speed);
    }

    public void setBackgroundColor() {

        JFrame colorChooserFrame = new JFrame();

        final JColorChooser colorChooser = new JColorChooser(pongCanvas.getBackgroundColor());

        colorChooser.getSelectionModel().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {

                pongCanvas.setBackground(colorChooser.getColor());
                pongCanvas.repaint();

//                LUIManager.setBackgroundColor(colorChooser.getColor().brighter());
            }
        });

        colorChooserFrame.setResizable(false);
        colorChooserFrame.setAlwaysOnTop(true);
        colorChooserFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        colorChooserFrame.setTitle(lang.get("bgColorChangeTitle"));
        colorChooserFrame.add(colorChooser);
        colorChooserFrame.pack();
        colorChooserFrame.setLocationRelativeTo(null);
        colorChooserFrame.setVisible(true);
    }

    public void setForegroundColor() {

        JFrame colorChooserFrame = new JFrame();

        final JColorChooser colorChooser = new JColorChooser(pongCanvas.getForegroundColor());

        colorChooser.getSelectionModel().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {

                pongCanvas.setForegroundColor(colorChooser.getColor());
                pongCanvas.repaint();

//                LUIManager.setForegroundColor(colorChooser.getColor());
            }
        });

        colorChooserFrame.setResizable(false);
        colorChooserFrame.setAlwaysOnTop(true);
        colorChooserFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        colorChooserFrame.setTitle(lang.get("fgColorChangeTitle"));
        colorChooserFrame.add(colorChooser);
        colorChooserFrame.pack();
        colorChooserFrame.setLocationRelativeTo(null);
        colorChooserFrame.setVisible(true);
    }

    public void setPaddleHeight(double height) {

        Paddle[] paddles = pongCanvas.getPaddles();
        for (Paddle paddle : paddles) {
            if (paddle.getPaddleMode().equals(PaddleMode.HUMAN) || paddle.getPaddleMode().equals(PaddleMode.AI)) {
                paddle.setPaddleHeight(height);
                if (paddle.getLowerLimit() >= pongCanvas.getPanelHeight() - PADDING) {
                    paddle.setPaddleY(pongCanvas.getPanelHeight() - height - PADDING);
                }
            }
        }
    }

    public void setWallHeight(double height) {

        if (pongCanvas instanceof SinglePlayerTrainingPongCanvas) {

            SinglePlayerTrainingPongCanvas trainingPongCanvas = (SinglePlayerTrainingPongCanvas) pongCanvas;
            trainingPongCanvas.setWallHeight(height);
            if (trainingPongCanvas.getWall().getLowerLimit() >= pongCanvas.getPanelHeight() - PADDING) {
                trainingPongCanvas.getWall().setPaddleY(pongCanvas.getPanelHeight() - height - PADDING);
            }
            pongCanvas.repaint();
        }
    }

    public void setWallColor() {

        if (pongCanvas instanceof SinglePlayerTrainingPongCanvas) {

            final SinglePlayerTrainingPongCanvas trainingPongCanvas = (SinglePlayerTrainingPongCanvas) pongCanvas;

            JFrame colorChooserFrame = new JFrame();

            final JColorChooser colorChooser = new JColorChooser(pongCanvas.getForegroundColor());

            colorChooser.getSelectionModel().addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent changeEvent) {

                    trainingPongCanvas.setWallColor(colorChooser.getColor());
                    pongCanvas.repaint();

                    //                LUIManager.setForegroundColor(colorChooser.getColor());
                }
            });

            colorChooserFrame.setResizable(false);
            colorChooserFrame.setAlwaysOnTop(true);
            colorChooserFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            colorChooserFrame.setTitle(lang.get("changeWallColorTitle"));
            colorChooserFrame.add(colorChooser);
            colorChooserFrame.pack();
            colorChooserFrame.setLocationRelativeTo(null);
            colorChooserFrame.setVisible(true);
        }
    }

    public void setWallPosition(double position) {

        if (pongCanvas instanceof SinglePlayerTrainingPongCanvas) {

            SinglePlayerTrainingPongCanvas trainingPongCanvas = (SinglePlayerTrainingPongCanvas) pongCanvas;
            trainingPongCanvas.getWall().setPaddleY(position);
            pongCanvas.repaint();
        }
    }

    public void setBrickColor() {

        if (pongCanvas instanceof SinglePlayerArkanoidPongCanvas) {

            final SinglePlayerArkanoidPongCanvas arkanoidPongCanvas = (SinglePlayerArkanoidPongCanvas) pongCanvas;

            JFrame colorChooserFrame = new JFrame();

            final JColorChooser colorChooser = new JColorChooser(pongCanvas.getForegroundColor());

            colorChooser.getSelectionModel().addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent changeEvent) {

                    arkanoidPongCanvas.setBrickColor(colorChooser.getColor());
                    pongCanvas.repaint();

                    //                LUIManager.setForegroundColor(colorChooser.getColor());
                }
            });

            colorChooserFrame.setResizable(false);
            colorChooserFrame.setAlwaysOnTop(true);
            colorChooserFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            colorChooserFrame.setTitle(lang.get("bricksColorChangeTitle"));
            colorChooserFrame.add(colorChooser);
            colorChooserFrame.pack();
            colorChooserFrame.setLocationRelativeTo(null);
            colorChooserFrame.setVisible(true);
        }
    }

    public void setMissileColor() {

        if (pongCanvas instanceof TwoPlayerBattlePongCanvas) {

            final TwoPlayerBattlePongCanvas battlePongCanvas = (TwoPlayerBattlePongCanvas) pongCanvas;

            JFrame colorChooserFrame = new JFrame();

            final JColorChooser colorChooser = new JColorChooser(pongCanvas.getForegroundColor());

            colorChooser.getSelectionModel().addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent changeEvent) {

                    battlePongCanvas.setMissileColor(colorChooser.getColor());
                    pongCanvas.repaint();

                    //                LUIManager.setForegroundColor(colorChooser.getColor());
                }
            });

            colorChooserFrame.setResizable(false);
            colorChooserFrame.setAlwaysOnTop(true);
            colorChooserFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            colorChooserFrame.setTitle(lang.get("missileColorChangeTitle"));
            colorChooserFrame.add(colorChooser);
            colorChooserFrame.pack();
            colorChooserFrame.setLocationRelativeTo(null);
            colorChooserFrame.setVisible(true);
        }
    }

    public void setBallSize(double size) {

        Ball ball = pongCanvas.getBall();
        ball.setBallDiameter(size);
        if (ball.getLowerLimit() >= pongCanvas.getPanelHeight() - ((int)MAXIMUM_BALL_SPEED + 1)) {
            ball.setBallY(pongCanvas.getPanelHeight() - size - ((int)MAXIMUM_BALL_SPEED + 1));
        }
    }

    public void repaintPongCanvas() {

        pongCanvas.repaint();
    }
}