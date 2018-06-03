package pong.control.tabs;

import pong.control.PongPreferencesEditor;
import pong.lang.Language;
import pong.mode.PongCanvas;
import pong.ui.LSlider;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class BallPanel extends PongTabPanel {

    private PongPreferencesEditor ppe;
    private Map<String, String> lang = Language.getLanguageMap(Language.getLoc());
    private JSlider ballSpeedSlider;
    private JSlider ballSizeSlider;

    public BallPanel(PongCanvas pongCanvas) {

        this.ppe = new PongPreferencesEditor(pongCanvas);
        init();
        addListeners();
        addComponents();
    }

    protected void init() {

        ballSpeedSlider = new JSlider(JSlider.HORIZONTAL, (int)(PongPreferencesEditor.MINIMUM_BALL_SPEED*10), (int)(PongPreferencesEditor.MAXIMUM_BALL_SPEED*10), (int)(ppe.getCurrentBallSpeed()*10));
        ballSpeedSlider.setMajorTickSpacing(5);
        ballSpeedSlider.setMinorTickSpacing(1);
        ballSpeedSlider.setSnapToTicks(true);
        ballSpeedSlider.setPaintLabels(true);
        ballSpeedSlider.setPaintTicks(true);
        ballSpeedSlider.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), lang.get("changeBallSpeed"), TitledBorder.CENTER, TitledBorder.TOP));
        ballSpeedSlider.setFocusable(false);

//        ballSpeedSlider = new LSlider((PongPreferencesEditor.MINIMUM_BALL_SPEED*10), (PongPreferencesEditor.MAXIMUM_BALL_SPEED*10), (ppe.getCurrentBallSpeed()*10), 1);
//        ballSpeedSlider.setHorizontalPadding(20);
//        ballSpeedSlider.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), lang.get("changeBallSpeed").toString(), TitledBorder.CENTER, TitledBorder.TOP));
//        ballSpeedSlider.setShowValues(true);
        //ballSpeedSlider.setBorder(BorderFactory.createLineBorder(Color.RED));

        ballSizeSlider = new JSlider(JSlider.HORIZONTAL, (int)PongPreferencesEditor.MINIMUM_BALL_SIZE, (int)PongPreferencesEditor.MAXIMUM_BALL_SIZE, (int)ppe.getCurrentBallSize());
        ballSizeSlider.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), lang.get("changeBallSize"), TitledBorder.CENTER, TitledBorder.TOP));
        ballSizeSlider.setFocusable(false);
//        ballSizeSlider = new LSlider(PongPreferencesEditor.MINIMUM_BALL_SIZE, PongPreferencesEditor.MAXIMUM_BALL_SIZE, ppe.getCurrentBallSize(), 1);
//        ballSizeSlider.setHorizontalPadding(20);
//        ballSizeSlider.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), lang.get("changeBallSize").toString(), TitledBorder.CENTER, TitledBorder.TOP));

    }

    protected void addListeners() {

        ballSpeedSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {

                ppe.setBallSpeed(ballSpeedSlider.getValue() / 10.0d);
            }
        });

//        ballSpeedSlider.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//
//                ppe.setBallSpeed(ballSpeedSlider.getValue() / 10.0d);
//            }
//        });

        ballSizeSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {

                ppe.setBallSize(ballSizeSlider.getValue());
                ppe.repaintPongCanvas();
            }
        });

//        ballSizeSlider.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//
//                ppe.setBallSize(ballSizeSlider.getValue());
//                ppe.repaintPongCanvas();
//            }
//        });
    }

    protected void addComponents() {

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(ballSpeedSlider);
        add(ballSizeSlider);
    }

    @Override
    public String getTabName() {

        return lang.get("ball");
    }
}
