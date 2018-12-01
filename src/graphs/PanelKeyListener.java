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

    public PanelKeyListener(Tree t){
        this.tree = t;
    }

    @Override
    public void keyTyped(KeyEvent e){
        switch(e.getKeyChar()){
        case '+':
            System.out.println("plus");
//            tree.addValues();
            break;
        case '-':
            System.out.println("minus");
//            tree.removeValues()
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
