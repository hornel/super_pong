package pong.control.tabs;

import pong.control.PongPreferencesEditor;
import pong.lang.Language;
import pong.mode.PongCanvas;
import pong.mode.SinglePlayerTrainingPongCanvas;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Map;

public class WallPanel extends PongTabPanel {

    private PongPreferencesEditor ppe;
    private SinglePlayerTrainingPongCanvas pongCanvas;
    private Map<String, String> lang = Language.getLanguageMap(Language.getLoc());
    private JButton wallColorButton;
    private JSlider wallHeightSlider;
    private JSpinner positionSpinner;
    private SpinnerModel positionModel;

    public WallPanel(PongCanvas pongCanvas) {

        this.ppe = new PongPreferencesEditor(pongCanvas);
        if (pongCanvas instanceof SinglePlayerTrainingPongCanvas) {
            this.pongCanvas = (SinglePlayerTrainingPongCanvas) pongCanvas;
        }
        initButtons();
        addListeners();
        addButtons();
    }

    private void initButtons() {

        wallColorButton = new JButton(lang.get("changeWallColor"));
        wallColorButton.setFocusable(false);

        wallHeightSlider = new JSlider(JSlider.HORIZONTAL, (int) PongPreferencesEditor.MINIMUM_PADDLE_HEIGHT, (int) PongPreferencesEditor.MAXIMUM_PADDLE_HEIGHT, (int) pongCanvas.getWallHeight());
        wallHeightSlider.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), lang.get("changeWallHeight"), TitledBorder.CENTER, TitledBorder.TOP));
        wallHeightSlider.setFocusable(false);

        positionModel = new SpinnerNumberModel(pongCanvas.getWall().getY(), PongPreferencesEditor.PADDING, (pongCanvas.getPanelHeight() - PongPreferencesEditor.PADDING - pongCanvas.getWall().getPaddleHeight()), -5);
        positionSpinner = new JSpinner(positionModel);
        positionSpinner.setEditor(new JLabel("Position"));
        positionSpinner.getEditor().setFocusable(false);
        positionSpinner.setFocusable(false);
    }

    private void addListeners() {

        wallColorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                ppe.setWallColor();
            }
        });

        wallHeightSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {

//                try {
                ppe.setWallHeight(wallHeightSlider.getValue());
                positionModel = new SpinnerNumberModel(pongCanvas.getWall().getY(), PongPreferencesEditor.PADDING, (pongCanvas.getPanelHeight() - PongPreferencesEditor.PADDING - pongCanvas.getWall().getPaddleHeight()), -5);
                positionSpinner.setModel(positionModel);
                positionSpinner.setValue(pongCanvas.getWall().getY());
//                    System.out.println("Min: " + PongPreferencesEditor.PADDING
//                            + "Value: " + pongCanvas.getWall().getY()
//                            + "Max: " + (pongCanvas.getPanelHeight() - PongPreferencesEditor.PADDING - pongCanvas.getWall().getPaddleHeight()));
//                } catch (Exception ex){
//                    ex.printStackTrace();
//                    System.out.println("Min: " + PongPreferencesEditor.PADDING
//                            + "Value: " + pongCanvas.getWall().getY()
//                            + "Max: " + (pongCanvas.getPanelHeight() - PongPreferencesEditor.PADDING - pongCanvas.getWall().getPaddleHeight()));
//                }
            }
        });

        positionSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {

//                try {
                    ppe.setWallPosition(((Double) positionSpinner.getValue()));
                    positionModel = new SpinnerNumberModel(pongCanvas.getWall().getY(), PongPreferencesEditor.PADDING, (pongCanvas.getPanelHeight() - PongPreferencesEditor.PADDING - pongCanvas.getWall().getPaddleHeight()), -5);
                    positionSpinner.setModel(positionModel);
                    positionSpinner.setValue(pongCanvas.getWall().getY());
//                    System.out.println("Min: " + PongPreferencesEditor.PADDING
//                            + "Value: " + pongCanvas.getWall().getY()
//                            + "Max: " + (pongCanvas.getPanelHeight() - PongPreferencesEditor.PADDING - pongCanvas.getWall().getPaddleHeight()));
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                    System.out.println("Min: " + PongPreferencesEditor.PADDING
//                            + "Value: " + pongCanvas.getWall().getY()
//                            + "Max: " + (pongCanvas.getPanelHeight() - PongPreferencesEditor.PADDING - pongCanvas.getWall().getPaddleHeight()));
//                }
            }
        });
    }

    private void addButtons() {

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        add(wallColorButton);
        add(wallHeightSlider);
        add(positionSpinner);
    }

    @Override
    public String getTabName() {

        return lang.get("wall");
    }
}
