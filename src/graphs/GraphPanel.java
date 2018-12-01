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

    public GraphPanel(Tree t){
        super();
        this.tree = t;
        this.x0 = 0;
        this.y0 = 0;
        this.zoom = 1;
    }

    @Override
    protected void paintComponent(Graphics g){
        g.setColor(Color.white);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setFont(new Font("Monospaced", PLAIN, 40));
        g.setColor(Color.black);
        tree.computeCoordinates(g);
        tree.paint(g);
    }
}
