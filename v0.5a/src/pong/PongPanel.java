package pong;


import java.awt.*;

public interface PongPanel {

    int INITIAL_PANEL_WIDTH = 950;
    int INITIAL_PANEL_HEIGHT = 600;
    int INITIAL_PADDLE_WIDTH = 20;
    int INITIAL_PADDLE_HEIGHT = 100;
    int INITIAL_PADDLE1_X = 10;
    int INITIAL_PADDLE2_X = INITIAL_PANEL_WIDTH - INITIAL_PADDLE_WIDTH - 10;
    int INITIAL_PADDLE1_Y = 0;
    int INITIAL_PADDLE2_Y = INITIAL_PADDLE1_Y;

    int INITIAL_BALL_X = 50;
    int INITIAL_BALL_Y = 50;
    int INITIAL_BALL_DIAMETER = 20;
    int INITIAL_X_TRAJECTORY = 9;
    int INITIAL_Y_TRAJECTORY = 9;

    int getPanelHeight();

    int getPanelWidth();

    Ball getBall();

    int getNumberOfPaddles();

    Color getForegroundColor();

    Color getBackgroundColor();

    void setForegroundColor(Color color);

    Thread getAnimationThread();

    void setBackground(Color color);

    void repaint();

    void reset();

    void setShouldAlwaysRequestFocus(boolean focus);

    void deinit();
}
