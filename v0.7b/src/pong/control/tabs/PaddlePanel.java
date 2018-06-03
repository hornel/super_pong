package pong.control.tabs;

import pong.control.PongPreferencesEditor;
import pong.lang.Language;
import pong.mode.PongCanvas;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.Map;

public class PaddlePanel extends PongTabPanel {

    private PongPreferencesEditor ppe;
    private Map<String, String> lang = Language.getLanguageMap(Language.getLoc());
    private JSlider paddleHeightSlider;
//    private LSlider paddleHeightSlider;

    public PaddlePanel(PongCanvas pongCanvas) {

        this.ppe = new PongPreferencesEditor(pongCanvas);
        init();
        addListeners();
        addComponents();
    }

    private void init() {

        paddleHeightSlider = new JSlider(JSlider.HORIZONTAL, (int)PongPreferencesEditor.MINIMUM_PADDLE_HEIGHT, (int)PongPreferencesEditor.MAXIMUM_PADDLE_HEIGHT, (int)ppe.getCurrentPaddleHeight());
        paddleHeightSlider.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), lang.get("changePaddleHeight"), TitledBorder.CENTER, TitledBorder.TOP));
        paddleHeightSlider.setFocusable(false);
//        paddleHeightSlider = new LSlider(PongPreferencesEditor.MINIMUM_PADDLE_HEIGHT, PongPreferencesEditor.MAXIMUM_PADDLE_HEIGHT, ppe.getCurrentPaddleHeight(), 1);
//        paddleHeightSlider.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), lang.get("changePaddleHeight").toString(), TitledBorder.CENTER, TitledBorder.TOP));

    }

    private void addListeners() {

        paddleHeightSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {

                ppe.setPaddleHeight(paddleHeightSlider.getValue());
                ppe.repaintPongCanvas();
            }
        });
//        paddleHeightSlider.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                ppe.setPaddleHeight(paddleHeightSlider.getValue());
//                ppe.repaintPongCanvas();
//            }
//        });
    }

    private void addComponents() {

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(paddleHeightSlider);
    }

    @Override
    public String getTabName() {

        return lang.get("paddle");
    }
}
