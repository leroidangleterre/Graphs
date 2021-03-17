package graphs;

import java.awt.Dimension;
import java.util.Date;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JPanel;

/**
 *
 * @author arthurmanoha
 */
public class GraphsMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        Tree originalTree = new Tree(0);

        int nbElements = 20;

        int windowWidth = 1900;
        int windowHeight = 700;

        JFrame window = new JFrame();

        JPanel panel = new GraphPanel(originalTree);
        window.addKeyListener(new PanelKeyListener(originalTree, (GraphPanel) panel));

        long startMilliseconds = new Date().getTime();

        for (int step = 1; step <= 100; step++) {
            originalTree.addValueEq(step);
        }

        int test = 83;
        int happy = -1;
        int i = 0;
        boolean loop2 = true;

        long endMilliseconds = new Date().getTime();

        System.out.println("Computation time for " + nbElements + " values: " + (double) (endMilliseconds - startMilliseconds) / 1000 + "s.");
        window.setTitle("Param: " + nbElements + ", size of tree: " + originalTree.getNbNodes());
        window.setDefaultCloseOperation(EXIT_ON_CLOSE);
        window.add(panel);
        window.setVisible(true);
        window.setPreferredSize(new Dimension(windowWidth, windowHeight));
        window.pack();

        window.invalidate();
    }

    public static int getCollatz(int n) {
        if ((n / 2) * 2 == n) {
            return n / 2;
        } else {
            return 3 * n + 1;
        }
    }

    /**
     * Happification: square all the digits of a number and add them.
     *
     * @param n the number to happify
     * @return the sum of the squares of the digits of the number
     */
    public static int getHappification(int n) {

        int happy = 0;
        int trimmedValue = n; // Extract the unit as long as there are units to extract

        while (trimmedValue > 0) {
            int unit = trimmedValue - (trimmedValue / 10) * 10; // The last digit of n; 0<=unit<=9
            happy += unit * unit;
            trimmedValue = trimmedValue / 10;
        }
        return happy;
    }
}
