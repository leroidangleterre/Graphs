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

        Tree originalTree = new Tree(1);

        int nbElements = 10000;
        int maxChainLength = -1;

        int windowWidth = 1500;
        int windowHeight = 700;

        JFrame window = new JFrame();
        window.addKeyListener(new PanelKeyListener(originalTree));

        JPanel panel = new GraphPanel(originalTree);

        for(int start = 2; start <= nbElements; start++){

            // Repaint a few times during building.
            if(100 * (start / 100) == start){
                System.out.println("computing Collatz sequence from " + start);
            }

            /* Create a tree with the given starting value;
                Build a tree by adding a root with the next Collatz value;
                When the root exists in the original tree, merge the trees.
             */
            Tree newTree = new Tree(start);
            boolean loop = true;
            while(loop){

                if(originalTree.containsValue(newTree.getRootValue())){
                    // At this point the original tree is supposed to contain the root of newTree,
                    // so we add newTree to the original tree.
                    originalTree.mergeBranch(newTree);
                    loop = false;
                }

                int val = newTree.getRootValue();
                int collatzVal = getCollatz(val);
                Tree newRoot = new Tree(collatzVal);
                newTree.addRoot(newRoot.getRootValue());
            }
        }
        window.setDefaultCloseOperation(EXIT_ON_CLOSE);
        window.add(panel);
        window.setVisible(true);
        window.setPreferredSize(new Dimension(windowWidth, windowHeight));
        window.pack();
    }

    public static int getCollatz(int n){
        if((n / 2) * 2 == n){
            return n / 2;
        } else{
            return 3 * n + 1;
        }
    }
}
