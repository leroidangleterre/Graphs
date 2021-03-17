/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author arthurmanoha
 */
public class PanelKeyListener implements KeyListener{

    private Tree tree;
    private GraphPanel panel;

    public PanelKeyListener(Tree t, GraphPanel p){
        this.tree = t;
        this.panel = p;
    }

    @Override
    public void keyTyped(KeyEvent e){
        switch(e.getKeyChar()){
        case '+':
            System.out.println("colplus");
            this.tree.increaseColumnSpacing(true);
            panel.repaint();
            break;
        case '-':
            System.out.println("colminus");
            this.tree.increaseColumnSpacing(false);
            panel.repaint();
            break;
        case '0':
            System.out.println("lineplus");
            this.tree.increaseLineSpacing(true);
            panel.repaint();
            break;
        case '1':
            System.out.println("lineminus");
            this.tree.increaseLineSpacing(false);
            panel.repaint();
            break;
        case 'd':
            this.tree.toggleDisplayDigits();
            panel.repaint();
            break;
        default:
            break;
        }
    }

    @Override
    public void keyPressed(KeyEvent e){
    }

    @Override
    public void keyReleased(KeyEvent e){
    }

}
