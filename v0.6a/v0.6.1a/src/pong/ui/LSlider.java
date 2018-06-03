package pong.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;

public class LSlider extends LAbstractButton {

    private double minValue;
    private double maxValue;
    private double initialValue = 0;
    private double step;
    private double height = 15;
    private Rectangle2D.Double slider;
    private double minPosition, maxPosition;
    private boolean moveable = false;
    private double position = 20;
    private double difference;
    private double coefficient;
    private boolean beingInitialized = true;
    private double hpadding = 20;
    private double vpadding = 20;
    private JPanel spaceHolder = new JPanel();
    private boolean showValues = false;

    private Color foregroundColor = LUIManager.getForegroundColor();

    public LSlider(double minValue, double maxValue, double initialValue, double step) throws ValueRangeException {

        setOpaque(false);
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.step = step;
        this.initialValue = initialValue;
        addListeners();
        setLayout(new BorderLayout());
        add(spaceHolder, BorderLayout.CENTER);
        spaceHolder.setOpaque(false);
        spaceHolder.setBorder(BorderFactory.createEmptyBorder((int)(vpadding + height / 2), 0, 0, (int)(vpadding + height / 2)));
    }

    public void setHorizontalPadding(double padding) {

        this.hpadding = padding;
        repaint();
    }

    public void setVerticalPadding(double padding) {

        this.vpadding = padding;
        spaceHolder.setBorder(BorderFactory.createEmptyBorder((int)(vpadding + height / 2), 0, 0, (int)(vpadding + height / 2)));
        repaint();
    }

    public void setShowValues(boolean showValues) {

        this.showValues = showValues;
    }

    @Override
    public void refreshColors() {

        foregroundColor = LUIManager.getForegroundColor();
    }

    @Override
    public void buildBackgroundArea() {

        setBackgroundShape(new Rectangle2D.Double(hpadding, getHeight() / 2 - height / 2, getWidth() - hpadding * 2, height));
    }

    private void initSlider(Graphics2D g2d) {

        g2d.setPaint(foregroundColor);
        slider = new Rectangle2D.Double(position + hpadding, getHeight() / 2 - height / 2, height, height);
        g2d.fill(slider);
        g2d.setPaint(LUIManager.getBackgroundColor());
        g2d.draw(new Rectangle2D.Double(hpadding, getHeight() / 2 - height / 2, getWidth() - 2 * hpadding - 1, height));
        if (showValues) {
            g2d.setPaint(Color.BLACK);
            g2d.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
            g2d.drawString("" + getValue(), (float) (position + hpadding), (float) (getHeight() / 2 + height + 7));
        }
        minPosition = 0;
        maxPosition = getWidth() - height - 2 * hpadding;
        coefficient = (maxValue - minValue) / maxPosition;
        if (beingInitialized) {
            setValue(initialValue);
            beingInitialized = false;
        }
//        System.out.println(minPosition);
//        System.out.println(minValue);
//        System.out.println(positionDifference);
    }

    public double getValue() {

        double unroundedValue = position * coefficient + minValue;
        double roundedValue = step * (Math.round(unroundedValue / step));
        return roundedValue;
    }

    public void setValue(double value) throws ValueRangeException {

        if (value < minValue || value > maxValue || value % step != 0) {
            throw new ValueRangeException("trying to set slider to illegal value");
        }

        position = (value - minValue) / coefficient;
        repaint();
    }

    private void moveSlider(double x, double difference) {

        position = x - difference;
        repaint();
    }

    private void addListeners() {

        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {

                if (position < minPosition || getValue() < minValue) {
                    moveable = false;
                    position = minPosition;
                    repaint();
                    fireActionEvent();
                } else if (position > maxPosition || getValue() > maxValue) {
                    moveable = false;
                    position = maxPosition;
                    System.out.println(maxPosition);
                    System.out.println(position);
                    repaint();
                    fireActionEvent();
                }
                if (moveable) {
                    moveSlider(e.getX(), difference);
//                    System.out.println(slider.getX());
//                    System.out.println(maxPosition);
                    fireActionEvent();
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (slider.contains(e.getX(), e.getY())) {
                    moveable = true;
                    difference = e.getX() - position;
                    System.out.println(getValue());
                    fireActionEvent();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                moveable = false;
                if (slider.getX() < minPosition || getValue() < minValue) {
                    position = minPosition;
                    repaint();
                    fireActionEvent();
                }
                if (slider.getX() > maxPosition || getValue() > maxValue) {
                    position = maxPosition;
                    repaint();
                    fireActionEvent();
                }
            }
        });
    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        initSlider(g2d);
        g2d.dispose();
    }
}