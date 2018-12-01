/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphs;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 *
 * @author arthurmanoha
 */
public class PanelMouseListener implements MouseListener{

    private boolean mouseWheelClicked;

    private GraphPanel panel;

    public PanelMouseListener(GraphPanel p){
        this.panel = p;
        mouseWheelClicked = false;
    }

    @Override
    public void mouseClicked(MouseEvent e){
//        System.out.println("clicked " + e.getButton());
    }

    @Override
    public void mousePressed(MouseEvent e){
//        System.out.println("pressed " + e.getButton());
        if(e.getButton() == MouseEvent.BUTTON2){
            panel.setMouseButtonPressed(true);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e){
//        System.out.println("released " + e.getButton());
        if(e.getButton() == MouseEvent.BUTTON2){
            panel.setMouseButtonPressed(false);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e){
    }

    @Override
    public void mouseExited(MouseEvent e){
    }

}
