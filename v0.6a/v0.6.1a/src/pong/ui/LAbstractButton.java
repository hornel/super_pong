package pong.ui;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Area;

public abstract class LAbstractButton extends JPanel implements MouseListener {

    EventListenerList listenerList = new EventListenerList();
    private String actionCommand = null;
    private int ID = 0;
    private Color inactiveColor = Color.LIGHT_GRAY, activeColor = LUIManager.getBackgroundColor(), mouseOverColor = LUIManager.getBackgroundColor().darker(), clickedColor = LUIManager.getBackgroundColor().darker().darker();
    private Color foregroundColor = LUIManager.getForegroundColor();
    private Color currentColor;
    private boolean active = true;
    private Area backgroundArea = new Area();

    public LAbstractButton() {

        setFocusable(false);
        setCurrentColor(activeColor);
        addMouseListener(this);
        setDoubleBuffered(true);
        setOpaque(false);
        LUIManager.add(this);
    }

    public abstract void refreshColors();

    public Color getForegroundColor() {

        return foregroundColor;
    }

    public void setForegroundColor(Color color) {

        foregroundColor = color;
        refreshColors();
        repaint();
    }

    public Color getCurrentColor() {

        return currentColor;
    }

    public void setCurrentColor(Color color) {

        currentColor = color;
    }

    public abstract void buildBackgroundArea();

    public Area getBackgroundArea() {

        return backgroundArea;
    }

    public void setBackgroundShape(Shape... shapes) {

        backgroundArea.reset();

        for (Shape shape : shapes) {

            backgroundArea.add(new Area(shape));
        }
    }

    public void setBackgroundShape(Area area) {

        backgroundArea.reset();
        backgroundArea.add(area);
    }

    private double calculateBrightness(Color color) {

        return Math.sqrt(Math.pow(0.241 * color.getRed(), 2) + Math.pow(0.691 * color.getGreen(), 2) + Math.pow(0.068 * color.getBlue(), 2));
    }

    public void setActiveColor(Color color) {

        if (calculateBrightness(color) > 50) {
            activeColor = color;
            mouseOverColor = color.darker();
            clickedColor = color.darker().darker();
            setCurrentColor(color);
            repaint();
        } else {
            activeColor = color;
            mouseOverColor = color.brighter();
            clickedColor = color.brighter().brighter();
            setCurrentColor(color);
            repaint();
        }
    }

    public void setInactiveColor(Color color) {

        inactiveColor = color;
    }

    public void setMouseOverColor(Color color) {

        mouseOverColor = color;
        clickedColor = color.darker();
    }

    public void setClickedColor(Color color) {

        clickedColor = color;
    }

    public void setActionCommand(String actionCommand) {

        this.actionCommand = actionCommand;
    }

    public void setID(int ID) {

        this.ID = ID;
    }

    public void setActive(boolean active) {

        this.active = active;
        setCurrentColor(active ? activeColor : inactiveColor);
        repaint();
    }

    public void addActionListener(ActionListener actionListener) {

        listenerList.add(ActionListener.class, actionListener);
    }

    public void removeActionListener(ActionListener actionListener) {

        listenerList.remove(ActionListener.class, actionListener);
    }

    protected void fireActionEvent() {

        ActionListener[] listeners = listenerList.getListeners(ActionListener.class);
        if (listeners != null && listeners.length > 0) {
            ActionEvent evt = new ActionEvent(this, ID, actionCommand);
            for (ActionListener listener : listeners) {
                listener.actionPerformed(evt);
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        if (active) {
            fireActionEvent();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

        if (active) {
            setCurrentColor(clickedColor);
            repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

        if (active) {
            setCurrentColor(mouseOverColor);
            repaint();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

        if (active) {
            setCurrentColor(mouseOverColor);
            repaint();
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {

        if (active) {
            setCurrentColor(activeColor);
            repaint();
        }
    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setPaint(currentColor);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        buildBackgroundArea();
        g2d.fill(getBackgroundArea());
    }
}