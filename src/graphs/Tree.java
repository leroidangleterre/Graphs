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

    // The value of the root node
    private int value;

    // All the leaves or branches that come from the current node
    private ArrayList<Tree> branches;

    // Max amount of branches for any given node
    private static int MAX_NODES = 2;

    // Coordinates on the 2d-plane when painted on a panel:
    private int x, y;

    // Spacing between two consectuive columns (in pixels)
    int columnSpacing = 10;
    // Spacing between two consecutive lines (in pixels)
    int lineSpacing = 25;

    public Tree(int val){
        this.value = val;
        this.branches = new ArrayList<>();
        this.x = 0;
        this.y = 0;
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
    }

    /**
     * Paint the tree on the graphics
     *
     */
    public void paint(Graphics g){

        // The height of the current node.
        int nodeHeight = g.getFontMetrics().getHeight();
        g.setColor(Color.black);
        String str = this.value + "";
        g.drawString(str, this.x, this.y);

        // Draw a rectangle around the number.
        char[] chars = ("" + this.value).toCharArray();
        int currentNodeWidth = g.getFontMetrics().charsWidth(chars, 0, chars.length); // The width of the current node.
        g.drawRect(x, y - nodeHeight + lineSpacing, currentNodeWidth, nodeHeight - lineSpacing);

        for(Tree branch : this.branches){
            // Paint the branch
            branch.paint(g);
            // Paint the link between the branch and the current node
            g.setColor(Color.black);
            int xStart = this.x + currentNodeWidth / 2;
            int yStart = this.y;

            // The width of the root of the branch.
            char[] branchChars = ("" + branch.value).toCharArray();
            int branchRootNodeWidth = g.getFontMetrics().charsWidth(branchChars, 0, branchChars.length);
            int branchRootNodeHeight = g.getFontMetrics().getHeight();
            int xEnd = branch.x + branchRootNodeWidth / 2;
            int yEnd = branch.y - branchRootNodeHeight + lineSpacing;
            g.drawLine(xStart, yStart, xEnd, yEnd);
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
            int branchesWidth = 0;
            for(Tree branch : this.branches){
                branchesWidth += branch.getWidthInPixels(g) + columnSpacing;
            }
            branchesWidth -= columnSpacing;
            return branchesWidth;
        }
    }

    /**
     * Move a tree horizontally.
     *
     */
    public void shiftHorizontally(int dx){
        this.x += dx;
        for(Tree branch : this.branches){
            branch.shiftHorizontally(dx);
        }
    }

    /**
     * Move a tree vertically.
     *
     */
    public void shiftVertically(int dy){
        this.y += dy;
        for(Tree branch : this.branches){
            branch.shiftVertically(dy);
        }
    }

    /**
     * Compute the coordinates on the 2d-plane of all nodes (the root and all
     * the branches) Each branch will be located beneath the root; the root will
     * be located horizontally at the mean x-position of all the branches.
     */
    public void computeCoordinates(Graphics g){
        this.y = 30;

        // The height of the current node.
        int currentNodeHeight = g.getFontMetrics().getHeight();

        int totalBranchesWidth = 0;
        for(Tree branch : this.branches){
            branch.computeCoordinates(g);
            branch.shiftHorizontally(totalBranchesWidth);
            totalBranchesWidth += branch.getWidthInPixels(g) + columnSpacing;
            branch.shiftVertically(currentNodeHeight + columnSpacing);
        }
        totalBranchesWidth -= columnSpacing;

        // The node must be horizontally centered.
        char[] chars = ("" + this.value).toCharArray();
        int currentNodeWidth = g.getFontMetrics().charsWidth(chars, 0, chars.length);
        if(isLeaf()){
            this.x = 0;
        } else{
            this.x = (totalBranchesWidth - currentNodeWidth) / 2;
        }
    }

    public int getRootValue(){
        return this.value;
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
}
