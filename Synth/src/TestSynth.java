import javax.swing.*;
import javax.swing.plaf.synth.SynthLookAndFeel;
import java.awt.*;
import java.text.ParseException;

public class TestSynth extends JFrame {

    public void init() {

        SynthLookAndFeel slaf = new SynthLookAndFeel();
        try {
            slaf.load(getClass().getResourceAsStream("synth.xml"), TestSynth.class);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            UIManager.setLookAndFeel(slaf);
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        JButton b1 = new JButton("This is a test");
        JSlider js1 = new JSlider(0, 10);
        js1.setMinorTickSpacing(1);
        js1.setMajorTickSpacing(5);
        js1.setPaintTicks(true);
        js1.setPaintLabels(true);
        js1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Slider"));
        System.out.println(js1.getName());
        JRadioButton jb1 = new JRadioButton("TEST");
        JCheckBox jc1 = new JCheckBox("TEST AGAIN");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());
        add(b1);
        add(js1);
        add(jc1);
        add(jb1);
    }

    public static void main(String[] args) {

        TestSynth s = new TestSynth();
        s.init();
        s.setVisible(true);
    }
}
