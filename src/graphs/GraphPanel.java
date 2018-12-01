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
public class GraphPanel extends JPanel{

    private Tree tree;

    private int x0, y0;
    private double zoom;
    private double standardZoomFactor;

    private boolean mouseButtonPressed;
    private int currentXMouse, currentYMouse;
    private int previousXMouse, previousYMouse;

    public GraphPanel(Tree t){
        super();
        this.tree = t;
        this.x0 = 37;
        this.y0 = 277;
        this.zoom = 0.0056745875199823975;
        this.standardZoomFactor = 1.05;
        this.mouseButtonPressed = false;
        this.addMouseListener(new PanelMouseListener(this));
        this.addMouseMotionListener(new PanelMouseMotionListener(this));
        this.addMouseWheelListener(new PanelMouseWheelListener(this));
        this.addKeyListener(new PanelKeyListener(t));
        this.previousXMouse = 0;
        this.previousYMouse = 0;
    }

    /**
     * Paint the tree on the Graphics. When the user scrolls to the right (in
     * order to see the right-hand side of the graph), then the value x0 becomes
     * negative.
     *
     * @param g
     */
    @Override
    protected void paintComponent(Graphics g){
        g.setColor(Color.white);
        g.fillRect(0, 0, getWidth(), getHeight()); // Clear screen.
        g.setFont(new Font("Monospaced", PLAIN, 40));
        g.setColor(Color.black);
        tree.computeCoordinates(g);
//        System.out.println("calling Tree.paint(" + x0 + ", " + y0 + ")");
        tree.paint(g, x0, y0, zoom);
    }

    public void setMouseButtonPressed(boolean param){
        this.mouseButtonPressed = param;
    }

    /**
     * Action in response to a movement of the mouse to the specified pixel.
     *
     * @param dx the current abscissa of the mouse.
     * @param dy the current ordinate of the mouse.
     */
    public void mouseMoved(int xMouse, int yMouse){
        this.previousXMouse = this.currentXMouse;
        this.previousYMouse = this.currentYMouse;
        this.currentXMouse = xMouse;
        this.currentYMouse = yMouse;
    }

    /**
     * Action in response to a movement of the mouse to the specified pixel,
     * with a button pressed.
     *
     * @param dx the current abscissa of the mouse.
     * @param dy the current ordinate of the mouse.
     */
    public void mouseDragged(int xMouse, int yMouse){
        this.mouseMoved(xMouse, yMouse);
        int dx = currentXMouse - previousXMouse;
        int dy = currentYMouse - previousYMouse;
        if(mouseButtonPressed){
            this.x0 += dx;
            System.out.println("changing x0 to " + this.x0);
            this.y0 += dy;
            System.out.println("changing y0 to " + this.y0);
        }
    }

    /**
     * Action in response to a rotation of the mouse wheel.
     *
     */
    public void mouseWheelMoved(int nbClicks, int xCenter, int yCenter){

        double currentZoomFactor;
        System.out.println("nbClicks:" + nbClicks);
        if(nbClicks > 0){
            currentZoomFactor = 1 / standardZoomFactor;
        } else{
            currentZoomFactor = standardZoomFactor;
        }

        this.zoom = this.zoom * currentZoomFactor;

        x0 = (int) (currentZoomFactor * (x0 - xCenter) + xCenter);
        y0 = (int) (currentZoomFactor * (y0 - yCenter) + yCenter);

        System.out.println("x0: " + x0 + ", y0: " + y0 + ", zoom: " + zoom);
    }
}
