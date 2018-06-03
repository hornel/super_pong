// File: LSlider.java
// Author: Leo
// Date Created: 5/2/15 9:29 AM

package pong.ui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;

@Deprecated
public class LSliderOld extends LAbstractButton {

    private double padding = 0;
    private double height = 20;
    private double sliderPadding = 2;
    private double sliderHeight = height - 2 * sliderPadding;
    private double minPosition = 0, maxPosition = 0;
    private double minX, maxX, currentX, xRange;
    private double minValue, maxValue, currentValue;
    private double step;
    private double interStep = 0;
    private double position = 0;
    private double difference = 0;
    private boolean sliderClicked = false;
    private Color sliderColor = Color.WHITE;
    private Ellipse2D slider;

    public LSliderOld(double minValue, double maxValue, double currentValue, double step) {

        addListeners();
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.currentValue = currentValue;
        this.step = step;
    }

    protected double getDrawingStartPoint() {

        double containerMidpoint = getHeight() / 2.0;
        return containerMidpoint - (height / 2) - padding;
    }

    @Override
    public void refreshColors() {

    }

    @Override
    public void buildBackgroundArea() {

        double arcSize = height;
        setBackgroundShape(new RoundRectangle2D.Double(padding, padding + getDrawingStartPoint(), getWidth() - padding * 2, height, arcSize, arcSize));
    }

    public void drawSlider(Graphics2D g2d) {

        slider = new Ellipse2D.Double(sliderPadding + padding + position, getDrawingStartPoint() + padding + sliderPadding, sliderHeight, sliderHeight);
        maxPosition = getWidth() - height - 2*padding;
        minX = slider.getWidth()/2 + padding + sliderPadding;
        maxX = getWidth() - padding - slider.getWidth()/2 - sliderPadding;
        xRange = maxX - minX;
        currentX = slider.getCenterX();
//        System.out.println("\nMin X: " + minX);
//        System.out.println("Max X: " + maxX);
//        System.out.println("Range: " + (maxX - minX));
//        System.out.println("CurrentX: " + currentX);
        interStep = xRange / maxValue;  // amount of pixels between steps
        g2d.setColor(sliderColor);
        g2d.fill(slider);
    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        drawSlider(g2d);
        System.out.println(getValue());
    }

    private void moveSlider(double x, boolean suddenMove) {

        if (!suddenMove) {
            position = x - difference;
        } else {
            position = x - (height/2 + padding);
        }
        repaint();
    }

    public double getValue() {

        System.out.println("\nCurrent X: " + currentX);
        System.out.println("Range: " + xRange);
        System.out.println("Interstep: " + interStep);
        System.out.println("Max Value: " + maxValue);
        System.out.println("Min value: " + minValue);
        System.out.println("Value range: " + (maxValue - minValue));
        System.out.println("Step: " + step);
        System.out.println(currentX / interStep);
        double value = currentX / interStep + minValue;
        double roundedValue = step * (Math.round(value / step));
        System.out.println(roundedValue);
        return roundedValue;
    }

    public void setValue(double value) {

        double x = value * interStep;
        moveSlider(x, true);
    }

    private void addListeners() {

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {

                if (currentX < minX || currentX > maxX) {
                    sliderClicked = false;
                    position = slider.getX() < padding + sliderPadding ? 0 : getWidth() - height - 2*padding;
                    repaint();
                }
                if (sliderClicked) {
                    moveSlider(e.getX(), false);
                }
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {

                if (slider.contains(e.getX(), e.getY())) {
                    difference = e.getX() - position;
                } else {
                    moveSlider(e.getX(), true);
                    if (position < 0 || position > maxPosition) {
                        position = e.getX() < minX ? 0 : getWidth() - height - 2*padding;
                        repaint();
                    }
                    difference = height / 2 + padding;
                }
                sliderClicked = true;
            }

            @Override
            public void mouseReleased(MouseEvent e) {

                sliderClicked = false;
            }
        });
    }
}