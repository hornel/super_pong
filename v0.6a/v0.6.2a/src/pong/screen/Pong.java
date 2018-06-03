// File: Pong.java
// Author: Leo Horne
// Date Created: Saturday, March 7, 2015

package pong.screen;

import pong.control.PongControls;
import pong.control.PongModeChooser;
import pong.control.SingleWindowManager;
import pong.mode.PongCanvas;
import pong.mode.PongMode;
import pong.mode.TwoHumanPlayerPongCanvas;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Class for creating the root window of the game.
 */

public class Pong extends JFrame {

    private PongCanvas pongCanvas;
    private PongControls pongControls;
    private SingleWindowManager<JDialog> singleWindowManager = new SingleWindowManager<JDialog>();

    public Pong() {

        tryToEnableMacIntegration();
        initGUI();
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {

                JFrame pong = new Pong();
                pong.setVisible(true);
            }
        });
    }

    /**
     * Initializes the Pong GUI. Called in the Pong constructor.
     */

    private void initGUI() {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        pongCanvas = new TwoHumanPlayerPongCanvas();
        pongControls = new PongControls(this);
        setMinimumSize(new Dimension((int) pongControls.getMinimumSize().getWidth() + 25, 375));
        setLayout(new BorderLayout());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Pong");
        setupGUI();
        addListeners();
        pack();
        setLocationRelativeTo(null);
    }

    /**
     * Adds Swing Components to the Pong window.
     */

    private void setupGUI() {

        add(pongCanvas, BorderLayout.CENTER);
        add(pongControls, BorderLayout.SOUTH);
    }

    /**
     * Adds listeners to the window components.
     */

    private void addListeners() {

        pongCanvas.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                pongControls.getPauseButton().setEnabled(false);
                pongControls.getPreferencesButton().setEnabled(false);
            }
        });
    }

    public void showPongModeChooser() {

        if (!singleWindowManager.isOccupied()) {
            singleWindowManager.setComponent(new PongModeChooser(this));
        }
        if (singleWindowManager.isOccupied()) {
            singleWindowManager.getComponent().setVisible(true);
        }
    }

    public void changePongMode(PongMode pongMode, Object... params) {

        pongCanvas.end();
        remove(pongCanvas);
        Constructor<?> pongCanvasConstructor = pongMode.getCorrespondingPongCanvasConstructor();
        try {
            pongCanvas = (PongCanvas) pongCanvasConstructor.newInstance(params);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        addListeners();
        pongControls.refresh();
        add(pongCanvas);
        pongCanvas.repaint();
        pongControls.resume();
    }

    public PongCanvas getPongCanvas() {

        return pongCanvas;
    }

    private void tryToEnableMacIntegration() {

        String fullScreenClassName = "com.apple.eawt.FullScreenUtilities";
        String fullScreenMethodName = "setWindowCanFullScreen";

        String topMenuString = "apple.laf.useScreenMenuBar";
        String aboutNameString = "com.apple.mrj.application.apple.menu.about.name";
        String textAntiAliasing = "apple.awt.textantialiasing";
        String useQuartz = "apple.awt.graphics.UseQuartz";

        if (System.getProperty("os.name").contains("Mac OS X")) {

            try {
                Class<?> fullScreenClass = Class.forName(fullScreenClassName);
                Method enableFullScreen = fullScreenClass.getMethod(fullScreenMethodName, Window.class, boolean.class);
                enableFullScreen.invoke(null, this, true);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            System.setProperty(topMenuString, "true");
            System.setProperty(aboutNameString, "Pong");
            System.setProperty(textAntiAliasing, "true");
            System.setProperty(useQuartz, "true");
        }
    }
}