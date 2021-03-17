/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphs;

import java.awt.Color;
import java.awt.Font;
import static java.awt.Font.PLAIN;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 *
 * @author arthurmanoha
 */
public class GraphPanel extends JPanel {

    private Tree tree;

    private int x0, y0;
    private double zoom;
    private double standardZoomFactor;
    private double zoomForCoordinates; // Used to compute the nodes coordinates.

    private boolean mouseButtonPressed;
    private int currentXMouse, currentYMouse;
    private int previousXMouse, previousYMouse;

//    private int fontSize;
    private boolean mustComputeCoordinates;

    public GraphPanel(Tree t) {
        super();
        this.tree = t;
        this.x0 = 870;
        this.y0 = 110;
        zoomForCoordinates = 10.0;
        this.standardZoomFactor = 1.05;
        setZoomLevel(zoomForCoordinates);
        this.mouseButtonPressed = false;
        this.addMouseListener(new PanelMouseListener(this));
        this.addMouseMotionListener(new PanelMouseMotionListener(this));
        this.addMouseWheelListener(new PanelMouseWheelListener(this));
        this.addKeyListener(new PanelKeyListener(t, this));
        this.previousXMouse = 0;
        this.previousYMouse = 0;
        this.mustComputeCoordinates = true;
        repaint();
    }

    /**
     * Paint the tree on the Graphics. When the user scrolls to the right (in
     * order to see the right-hand side of the graph), then the value x0 becomes
     * negative.
     *
     * @param g
     */
    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(0, 0, getWidth(), getHeight()); // Clear screen.
        g.setColor(Color.black);

        // Compute the coordinates the first time.
        if (mustComputeCoordinates) {
            tree.computeCoordinates(g, zoomForCoordinates);
            mustComputeCoordinates = false;
        }
        tree.paint(g, x0, y0, zoom);
    }

    public void setMouseButtonPressed(boolean param) {
        this.mouseButtonPressed = param;
    }

    /**
     * Action in response to a movement of the mouse to the specified pixel.
     *
     * @param xMouse the current abscissa of the mouse.
     * @param yMouse the current ordinate of the mouse.
     */
    public void mouseMoved(int xMouse, int yMouse) {
        this.previousXMouse = this.currentXMouse;
        this.previousYMouse = this.currentYMouse;
        this.currentXMouse = xMouse;
        this.currentYMouse = yMouse;
    }

    /**
     * Action in response to a movement of the mouse to the specified pixel,
     * with a button pressed.
     *
     * @param xMouse the current abscissa of the mouse.
     * @param yMouse the current ordinate of the mouse.
     */
    public void mouseDragged(int xMouse, int yMouse) {
        this.mouseMoved(xMouse, yMouse);
        int dx = currentXMouse - previousXMouse;
        int dy = currentYMouse - previousYMouse;
        if (mouseButtonPressed) {
            this.x0 += dx;
            this.y0 += dy;
        }
    }

    /**
     * Action in response to a rotation of the mouse wheel.
     *
     * @param nbClicks the number of mousewheel rotation units detected
     * @param xCenter the x-coordinate of the mouse at the time of click
     * @param yCenter the y-coordinate of the mouse at the time of click
     */
    public void mouseWheelMoved(int nbClicks, int xCenter, int yCenter) {

        double currentZoomFactor;
        if (nbClicks > 0) {
            currentZoomFactor = 1 / standardZoomFactor;
        } else {
            currentZoomFactor = standardZoomFactor;
        }

        setZoomLevel(this.zoom * currentZoomFactor);

        x0 = (int) (currentZoomFactor * (x0 - xCenter) + xCenter);
        y0 = (int) (currentZoomFactor * (y0 - yCenter) + yCenter);
    }

    /**
     * Set the apparent position of the origin.
     *
     * @param newX0
     * @param newY0
     */
    public void setScroll(int newX0, int newY0) {
        x0 = newX0;
        y0 = newY0;
    }

    /**
     * Set the zoom level
     *
     * @param newZoomLevel
     */
    public void setZoomLevel(double newZoomLevel) {
        zoom = newZoomLevel;
    }
}
