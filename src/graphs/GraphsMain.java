/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphs;

import java.awt.Dimension;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JPanel;

/**
 *
 * @author arthurmanoha
 */
public class GraphsMain{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){

        Tree t = new Tree(0);
        System.out.println("" + t);
        System.out.println("depth: " + t.getDepth());

        int nbElements = 40;

        for(int i = 1; i < nbElements; i++){
            t.addValueEq(i);
//            System.out.println("" + t);
//            System.out.println("depth: " + t.getDepth());
        }
        System.out.println("" + t);
        System.out.println("depth: " + t.getDepth());

        JFrame window = new JFrame();
        window.setPreferredSize(new Dimension(640, 480));

        JPanel panel = new GraphPanel(t);

        window.setDefaultCloseOperation(EXIT_ON_CLOSE);
        window.add(panel);
        window.setVisible(true);
        window.pack();
        panel.setSize(640, 480);
        panel.setPreferredSize(new Dimension(640, 480));
    }

}
