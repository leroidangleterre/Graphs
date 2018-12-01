/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphs;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/**
 *
 * @author arthurmanoha
 */
public class PanelMouseMotionListener implements MouseMotionListener{

    private GraphPanel panel;

    public PanelMouseMotionListener(GraphPanel p){
        this.panel = p;
    }

    @Override
    public void mouseDragged(MouseEvent e){
        panel.mouseDragged(e.getX(), e.getY());
        panel.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e){
        panel.mouseMoved(e.getX(), e.getY());
        panel.repaint();
    }

}
