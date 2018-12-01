/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphs;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/**
 *
 * @author arthurmanoha
 */
public class PanelMouseWheelListener implements MouseWheelListener{

    private GraphPanel panel;

    public PanelMouseWheelListener(GraphPanel p){
        this.panel = p;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e){
        panel.mouseWheelMoved(e.getWheelRotation(), e.getX(), e.getY());
        panel.repaint();
    }

}
