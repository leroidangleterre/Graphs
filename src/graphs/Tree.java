/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphs;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

/**
 *
 * @author arthurmanoha
 */
public class Tree{

    // Type of display: regular up-down tree (0) of angled-tree (1) or stairs-like (2)
    int typeOfDisplay = 0;
    double angle = 0;

    // The value of the root node
    private int value;

    // All the leaves or branches that come from the current node
    private ArrayList<Tree> branches;

    // Max amount of branches for any given node
    private static int MAX_NODES = 2;

    // Coordinates on the 2d-plane when painted on a panel:
    private double x, y;

    // Spacing between two consecutive columns (in pixels)
    int columnSpacing = 10;
    // Spacing between two consecutive lines (in pixels)
    int lineSpacing = 500;

    int maxInternalValue = 0;

    public Tree(int val){
        this.value = val;
        this.branches = new ArrayList<>();
        this.x = 0;
        this.y = 0;
        this.maxInternalValue = val;
    }

    /**
     * Returns the string representing the tree, with the root printed first,
     * and all branches shifted to the right.
     */
    public String toString(){
        return this.toString(0);
    }

    public String toString(int shift){
        String result = "";
        if(shift > 0){
            result += "\n";
        }
        for(int i = 0; i < shift; i++){
            result += ".";
        }
        result += this.value;
        for(Tree branch : this.branches){
            result += branch.toString(shift + 1);
        }
        return result;
    }

    /**
     * Add a given value at a certain depth in the tree.
     *
     */
    public void addValue(int value, int depth){
        if(depth == 1){
            this.branches.add(new Tree(value));
        } else if(depth > 1){
            if(this.branches.isEmpty()){
                Tree newBranch = new Tree(0);
                this.branches.add(newBranch);
                newBranch.addValue(value, depth - 1);
            }
        }
        this.maxInternalValue = Math.max(value, maxInternalValue);
    }

    /**
     * Return the length of the longest branch from root to leaf.
     *
     * @return the depth of the tree
     */
    public int getDepth(){
        if(isLeaf()){
            return 1;
        } else{
            int maxBranchLength = 0;
            for(Tree branch : branches){
                maxBranchLength = Math.max(maxBranchLength, branch.getDepth());
            }
            return 1 + maxBranchLength;
        }
    }

    /**
     * Find and return the shortest branch of that tree. If no branch exists but
     * there is only the root, return the whole tree.
     */
    public Tree getShortestBranch(){
        if(this.isLeaf()){
            return this;
        } else{
            Tree shortestBranch = branches.get(0);
            int minLength = shortestBranch.getDepth();
            for(Tree branch : this.branches){
                if(branch.getDepth() < minLength){
                    // Found a new shortest branch.
                    shortestBranch = branch;
                    minLength = branch.getDepth();
                }
            }
            return shortestBranch;
        }
    }

    /**
     * Add a value at the right position so that the tree remains equilibrated.
     *
     * @param value The new value added to the graph
     */
    public void addValueEq(int value){
        if(this.getDepth() == 1 || this.branches.size() < MAX_NODES){
            // No branches yet, or not too many branches yet: add the value as a new branch.
            this.branches.add(new Tree(value));
        } else{
            // Find the shortest branch, add the value to that branch.
            Tree shortestBranch = this.getShortestBranch();
            shortestBranch.addValueEq(value);
        }
        this.maxInternalValue = Math.max(value, maxInternalValue);
    }

    /**
     * Paint the tree on the Graphics. When the user scrolls to the right (in
     * order to see the right-hand side of the graph), then the value x0 becomes
     * negative.
     *
     * @param g
     */
    public void paint(Graphics g, int x0, int y0, double zoom){
        if(typeOfDisplay == 0){
            // The height of the current node.
            int nodeHeight = g.getFontMetrics().getHeight();
            g.setColor(Color.black);
            String str = this.value + "";

//        // Display the digits only if there is enough room.
//        if(zoom >= 1){
//            g.drawString(str, (int) (x0 + (this.x * zoom)), (int) (y0 + (this.y * zoom)));
//        }
            // Draw a red dot at the origin of the node.
//            g.setColor(Color.red);
//            g.fillRect((int) (x0 + (this.x * zoom)) - 2,
//                    (int) (y0 + (this.y * zoom)) - 2,
//                    5, 5);
            // Draw a rectangle around the number.
            char[] chars = ("" + this.value).toCharArray();
            int currentNodeWidth = g.getFontMetrics().charsWidth(chars, 0, chars.length); // The width of the current node.
            g.setColor(Color.black);
            g.drawRect((int) (x0 + (this.x - currentNodeWidth / 2) * zoom),
                    (int) (y0 + (this.y - nodeHeight / 2) * zoom),
                    (int) (currentNodeWidth * zoom),
                    (int) ((nodeHeight) * zoom));

            for(Tree branch : this.branches){
                // Paint the branch
                branch.paint(g, x0, y0, zoom);
                // Paint the link between the branch and the current node
                g.setColor(Color.black);
                int xStart = (int) (x0 + this.x * zoom);
                int yStart = (int) (y0 + this.y * zoom);

                // The width of the root of the branch.
                char[] branchChars = ("" + branch.value).toCharArray();
                int branchRootNodeWidth = g.getFontMetrics().charsWidth(branchChars, 0, branchChars.length);
                int branchRootNodeHeight = g.getFontMetrics().getHeight();
                int xEnd = (int) (x0 + (branch.x) * zoom);
                int yEnd = (int) (y0 + (branch.y) * zoom);
                g.drawLine(xStart, yStart, xEnd, yEnd);
            }
        } else if(typeOfDisplay == 1 || typeOfDisplay == 2){

            // Draw a circle centered on the point
            int radius = (int) (0.5 * zoom);
            int xCenter = (int) (x0 + (this.x) * zoom);
            int yCenter = (int) (y0 + (this.y) * zoom);

            g.setColor(Color.red);
            g.fillOval(xCenter - radius, yCenter - radius, 2 * radius, 2 * radius);
//            System.out.println("painting a circle: " + (xCenter - radius) + ", " + (yCenter - radius) + ", " + 2 * radius + ", " + 2 * radius);

            g.setColor(Color.black);
            g.drawOval(xCenter - radius, yCenter - radius, 2 * radius, 2 * radius);

            // Draw all the branches and link them to the current circle.
            for(Tree branch : this.branches){
                // Paint the branch
                branch.paint(g, x0, y0, zoom);
                // Paint the link between the branch and the current node
                g.setColor(Color.black);
                int xStart = (int) (x0 + this.x * zoom);
                int yStart = (int) (y0 + this.y * zoom);
                int xEnd = (int) (x0 + branch.x * zoom);
                int yEnd = (int) (y0 + branch.y * zoom);

                g.drawLine(xStart, yStart, xEnd, yEnd);
            }
        }
    }

    /**
     * Return the height in pixels of the tree.
     *
     */
    public int getHeightInPixels(Graphics g){

        // The height of the current node.
        int currentNodeHeight = g.getFontMetrics().getHeight();

        if(isLeaf()){
            // If the tree has no leaf.
            return currentNodeHeight;
        } else{
            // The height of this tree is the height of the tallest branch,
            // plus the height of the node, plus the lineSpacing between the root and the branches.
            int maxBranchHeight = 0;
            for(Tree branch : this.branches){
                maxBranchHeight = Math.max(maxBranchHeight, branch.getHeightInPixels(g));
            }
            return currentNodeHeight + maxBranchHeight + lineSpacing;
        }
    }

    /**
     * Return true if the tree is a leaf, i.e. no other branch start from this
     * node.
     *
     * @return true if the tree is a leaf.
     */
    public boolean isLeaf(){
        return this.branches == null || this.branches.isEmpty();
    }

    /**
     * Return the width of the tree when displayed on the given Graphics.
     *
     * @param g
     * @return
     */
    public int getWidthInPixels(Graphics g){

        char[] chars = ("" + this.value).toCharArray();
        int currentNodeWidth = g.getFontMetrics().charsWidth(chars, 0, chars.length);

        if(isLeaf()){
            // If the tree has no leaf.
            return currentNodeWidth;
        } else{
            int totalWidth = 0;
            for(Tree branch : this.branches){
                totalWidth += branch.getWidthInPixels(g) + columnSpacing;
            }
            totalWidth -= columnSpacing;
            return Math.max(totalWidth, currentNodeWidth);

        }
    }

    /**
     * Move a tree horizontally.
     *
     */
    public void moveToRightOrLeft(double dx){
        this.x += dx;
        for(Tree branch : this.branches){
            branch.moveToRightOrLeft(dx);
        }
    }

    /**
     * Move a tree vertically.
     *
     */
    public void moveUpOrDown(double dy){
        this.y += dy;
        for(Tree branch : this.branches){
            branch.moveUpOrDown(dy);
        }
    }

    /**
     * Compute the coordinates on the 2d-plane of all nodes (the root and all
     * the branches) Each branch will be located beneath the root; the root will
     * be located horizontally at the mean x-position of all the branches.
     */
    public void computeCoordinates(Graphics g){
        this.x = 30;
        this.y = 30;

        if(typeOfDisplay == 0){
            // The height of the current node.
            int currentNodeHeight = g.getFontMetrics().getHeight();

            int totalBranchesWidth = 0;
            for(Tree branch : this.branches){
                branch.computeCoordinates(g);
                if(totalBranchesWidth == 0){
                    // The first branch must not move.
                } else{
                    // Every next branch must be offset.
                    branch.moveToRightOrLeft(totalBranchesWidth + columnSpacing);
                }
                totalBranchesWidth += branch.getWidthInPixels(g) + columnSpacing;
                branch.moveUpOrDown(currentNodeHeight + lineSpacing);
            }
            totalBranchesWidth -= columnSpacing;

            char[] chars = ("" + this.value).toCharArray();
            int currentNodeWidth = g.getFontMetrics().charsWidth(chars, 0, chars.length);

            if(this.branches.size() == 1){
                // Only one child, must align on it.
                this.x = this.branches.get(0).x;
            } else{
                centerHorizontally(totalBranchesWidth, currentNodeWidth);
            }
        } else if(typeOfDisplay == 1){
            double dx = 1.0;
            double angle = 0.07;
            // Step 1: translate all branches
            for(Tree branch : this.branches){
                branch.computeCoordinates(g);
                branch.moveToRightOrLeft(dx);
            }
            // Step 2: rotate all branches
            for(Tree branch : this.branches){
                int val = branch.getRootValue();
                if((val / 2) * 2 == val){
                    // Turn clockwise all branches starting with an even number.
                    branch.rotate(-angle);
                } else{
                    // Turn anticlockwise all branches starting with an odd number.
                    branch.rotate(angle);
                }
            }
            this.x = 0;
            this.y = 0;
        } else if(typeOfDisplay == 2){
            // Even numbers are on the right of their parents; odd numbers are above.
            this.x = 0;
            this.y = 0;
            double dx = 1;
            for(Tree branch : this.branches){
                branch.x = 0;
                branch.y = 0;
                branch.computeCoordinates(g);

                int val = branch.getRootValue();
                if(2 * (val / 2) == val){
                    branch.moveToRightOrLeft(dx);
                } else{
                    branch.moveUpOrDown(-dx);
                }
            }
        }
    }

    /**
     * Place the node at the mean x-coordinate of all its direct children.
     *
     * @param totalBranchesWidth
     */
    private void centerHorizontally(int totalBranchesWidth, int currentNodeWidth){

        // The node must be horizontally centered.
        if(isLeaf()){
            this.x = 0;
        } else{
            this.x = (totalBranchesWidth - currentNodeWidth) / 2;
        }
    }

    public int getRootValue(){
        return this.value;
    }

    public int getMaxValue(){
        return this.maxInternalValue;
    }

    public boolean containsValue(int testValue){
        return this.findValue(testValue) != null;
    }

    public Tree findValue(int testValue){
        // Check if the entire tree starts with the requested value.
        if(this.value == testValue){
            return this;
        } else{
            // Check if one branch starts with the requested value.
            for(Tree branch : this.branches){
                if(branch.value == testValue){
                    return branch;
                }
            }
            // Nothing found at the root of the branches, so we recursively search deeper.
            for(Tree branch : this.branches){
                Tree result = branch.findValue(testValue);
                if(result != null){
                    // Found the value in one sub-branch.
                    return result;
                }
            }
        }
        // Neither the root nor the branches contain the requested value.
        return null;
    }

    /**
     * Add a new node as the root of the current tree.
     *
     */
    public void addRoot(int newRootValue){

        // Clone the original root: that will become the first internal layer.
        Tree firstLayer = new Tree(this.value);

        // Add all the branches of the original root to the clone.
        for(Tree branch : this.branches){
            firstLayer.branches.add(branch);
        }
        // Remove the branches from the original node.
        this.branches.clear();

        // Link the original node to its copy.
        this.branches.add(firstLayer);

        // Set the new root value.
        this.value = newRootValue;
    }

    /**
     * Add a tree as a new branch. Find within the current tree the node whose
     * value is the root of the newly added tree; incorporate the newly added
     * tree as another branch of the current tree. If the root value of the new
     * branch is not found in the tree, do nothing.
     */
    public void mergeBranch(Tree mergedTree){
        int newVal = mergedTree.getRootValue();
        Tree addingPoint = this.findValue(newVal);
        if(addingPoint != null){
            for(Tree addedBranch : mergedTree.branches){
                addingPoint.branches.add(addedBranch);
            }
        }
    }

    /**
     * Apply a rotation around the origin to all the nodes of the graph.
     *
     * @param angle
     */
    public void rotate(double angle){

        // Move the root of the graph.
        double oldX = this.x;
        double oldY = this.y;
        double c = Math.cos(angle);
        double s = Math.sin(angle);
        this.x = c * oldX - s * oldY;
        this.y = c * oldY + s * oldX;

        // Move all the branches.
        for(Tree branch : this.branches){
            branch.rotate(angle);
        }
    }

}
