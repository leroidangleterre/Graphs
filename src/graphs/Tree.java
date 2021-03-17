/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphs;

import java.awt.Color;
import java.awt.Font;
import static java.awt.Font.PLAIN;
import java.awt.Graphics;
import java.util.ArrayList;

/**
 *
 * @author arthurmanoha
 */
public class Tree {

    // Type of display:
    // Numbered tiles as a regular up-down tree:
    // - with equilibrated branches (0)
    // - with even numbers vertically aligned (3)
    // Point angled-tree (1) or stairs-like (2)
    protected int typeOfDisplay = 0;
    // The value of the root node
    protected int value;

    // Dimensions of the box
    private double width, height;

    // All the leaves or branches that come from the current node
    protected ArrayList<Tree> branches;

    // Max amount of branches for any given node (0 for no limit)
    public static int MAX_NODES = 2;

    // Coordinates on the 2d-plane when painted on a panel:
    protected double x, y;

    // Spacing between two consecutive columns (in pixels)
    protected static int columnSpacing = 5;
    // Spacing between two consecutive lines (in pixels)
    protected static int lineSpacing = 10;

    int maxInternalValue = 0;

    private Tree longestBranch;

    private boolean mustDisplayDigits;

    public Tree(int val) {
        this.value = val;
        this.branches = new ArrayList<>();
        this.x = 0;
        this.y = 0;
        this.maxInternalValue = val;
        this.longestBranch = null;
        this.mustDisplayDigits = true;
        this.width = 1;
        this.height = 1;
    }

    /**
     * Returns the string representing the tree, with the root printed first,
     * and all branches shifted to the right.
     */
    @Override
    public String toString() {
        return this.toString(0);
    }

    public String toString(int shift) {
        String result = "";
        if (shift > 0) {
            result += "\n";
        }
        for (int i = 0; i < shift; i++) {
            result += ".";
        }
        result += this.value;
        for (Tree branch : this.branches) {
            result += branch.toString(shift + 1);
        }
        return result;
    }

    /**
     * Return the length of the longest branch from root to leaf.
     *
     * @return the depth of the tree
     */
    public int getDepth() {
        if (isLeaf()) {
            return 1;
        } else {
            int maxBranchLength = 0;
            for (Tree branch : branches) {
                maxBranchLength = Math.max(maxBranchLength, branch.getDepth());
                this.longestBranch = branch;
            }
            return 1 + maxBranchLength;
        }
    }

    /**
     * Find and return the shortest branch of that tree. If no branch exists but
     * there is only the root, return the whole tree.
     */
    public Tree getShortestBranch() {
        if (this.isLeaf()) {
            return this;
        } else {
            Tree shortestBranch = branches.get(0);
            int minLength = shortestBranch.getDepth();
            for (Tree branch : this.branches) {
                if (branch.getDepth() < minLength) {
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
    public void addValueEq(int value) {
        if (this.isLeaf()) {
            branches.add(new Tree(value));
        } else if (branches.size() < 2) {
            // Add as a new branch
            branches.add(new Tree(value));
        } else {
            // Add to  the smallest branch
            Tree smallestBranch = getSmallestBranch();
            smallestBranch.addValueEq(value);
        }
    }

    /**
     * Get the branch that has the smallest number of nodes.
     *
     * @return the smallest branch by number of nodes.
     */
    private Tree getSmallestBranch() {
        int smallestNodeNumber = Integer.MAX_VALUE;
        Tree smallestBranch = null;
        for (Tree branch : branches) {
            if (branch.getNbNodes() < smallestNodeNumber) {
                // New local min
                smallestBranch = branch;
                smallestNodeNumber = branch.getNbNodes();
            }
        }
        return smallestBranch;
    }

    /**
     * Paint the tree on the Graphics. When the user scrolls to the right (in
     * order to see the right-hand side of the graph), then the value x0 becomes
     * negative.
     *
     * The visible rectangle is centered around the (x, y) coordinates of the
     * node.
     *
     * @param g
     */
    public void paint(Graphics g, int x0, int y0, double zoom) {

        g.setColor(Color.black);
        String str = getStringValue();

        // Draw a rectangle around the number.
        int gray = 200;
        g.setColor(new Color(gray, gray, gray));
        g.fillRect((int) (x0 + (this.x - width / 2) * zoom),
                (int) (y0 + (this.y - height / 2) * zoom),
                (int) (width * zoom),
                (int) (height * zoom));
        g.setColor(Color.black);
        g.drawRect((int) (x0 + (this.x - width / 2) * zoom),
                (int) (y0 + (this.y - height / 2) * zoom),
                (int) (width * zoom),
                (int) (height * zoom));

        // Display text value.
        // Choose the largest font size that allows the text to remain inside the box.
        int fontSize = 1;
        do {
            fontSize++;
            g.setFont(new Font("Monospaced", PLAIN, fontSize));
        } while (g.getFontMetrics().stringWidth(str) < width * zoom);
        fontSize--;

        g.setColor(Color.black);
        g.drawString(str,
                (int) (x0 + (this.x - width / 2) * zoom),
                (int) (y0 + this.y * zoom + (height / 2) * zoom));

        for (Tree branch : this.branches) {
            // Paint the branch
            branch.paint(g, x0, y0, zoom);

            // Paint the link between the branch and the current node
            g.setColor(Color.black);
            int xStart = (int) (x0 + this.x * zoom);
            int yStart = (int) (y0 + (this.y + this.height / 2) * zoom);
            // The width of the root of the branch.
            int xEnd = (int) (x0 + (branch.x) * zoom);
            int yEnd = (int) (y0 + (branch.y - branch.height / 2) * zoom);
            g.drawLine(xStart, yStart, xEnd, yEnd);
        }
    }

    /**
     * Return the height in pixels of the tree.
     *
     * @param g
     */
    public int getHeightInPixels(Graphics g) {

        // The height of the current node.
        int currentNodeHeight = g.getFontMetrics().getHeight();

        if (isLeaf()) {
            // If the tree has no leaf.
            return currentNodeHeight;
        } else {
            // The height of this tree is the height of the tallest branch,
            // plus the height of the node, plus the lineSpacing between the root and the branches.
            int maxBranchHeight = 0;
            for (Tree branch : this.branches) {
                maxBranchHeight = Math.max(maxBranchHeight, branch.getHeightInPixels(g));
            }
            return currentNodeHeight + maxBranchHeight + lineSpacing;
        }
    }

    /**
     * Get the x-coordinate of the node.
     *
     * @return the x-coordinate of the node.
     */
    public double getApparentX() {
        return x;
    }

    /**
     * Get the y-coordinate of the node.
     *
     * @return the y-coordinate of the node.
     */
    public double getApparentY() {
        return y;
    }

    /**
     * Return true if the tree is a leaf, i.e. no other branch start from this
     * node.
     *
     * @return true if the tree is a leaf.
     */
    public boolean isLeaf() {
        return this.branches == null || this.branches.isEmpty();
    }

    /**
     * Return the width of the tree when displayed on the given Graphics.
     *
     * @param g
     * @return the apparent width in pixels
     */
    public int getWidthInPixels(Graphics g) {

        int currentNodeWidth = getNodeWidth(g);

        if (isLeaf()) {
            // If the tree has no leaf.
            return currentNodeWidth;
        } else {
            int totalBranchesWidth = 0;
            for (Tree branch : this.branches) {
                // Amount of extra-space added for each extra branch
                if (totalBranchesWidth > 0) {
                    totalBranchesWidth += columnSpacing;
                }

                // Amount of space used by the branch itself
                totalBranchesWidth += branch.getWidthInPixels(g);
            }
            return Math.max(totalBranchesWidth, currentNodeWidth);

        }
    }

    /**
     * Move a tree horizontally.
     *
     * @param dx horizontal displacement in pixels
     */
    public void moveRight(double dx) {
        this.x += dx;
        for (Tree branch : this.branches) {
            branch.moveRight(dx);
        }
    }

    /**
     * Move a tree vertically.
     *
     * @param dy vertical displacement in pixels
     */
    public void moveDown(double dy) {
        this.y += dy;
        for (Tree branch : this.branches) {
            branch.moveDown(dy);
        }
    }

    /**
     * Compute the coordinates on the 2d-plane of all nodes (the root and all
     * the branches). Each branch will be located beneath the root; the root
     * will be located horizontally at the average x-position of all the
     * branches.
     *
     * @param g
     * @param zoom
     */
    public void computeCoordinates(Graphics g, double zoom) {
        this.x = 0;
        this.y = 0;

        this.width = getNodeWidth(g);
        this.height = getNodeHeight(g);

        int widthOfBranchesToTheLeft = 0;
        int amountOfColSpacing = 0;
        for (Tree branch : branches) {
            branch.computeCoordinates(g, zoom);

            branch.moveRight(widthOfBranchesToTheLeft + branch.getWidthInPixels(g) / 2 + amountOfColSpacing);
            widthOfBranchesToTheLeft += branch.getWidthInPixels(g) + amountOfColSpacing;
            amountOfColSpacing += columnSpacing;

            branch.moveDown(this.getNodeHeight(g) + lineSpacing);
        }

        // Move all branches to the left to center them horizontally.
        for (Tree branch : branches) {
            branch.moveRight(-widthOfBranchesToTheLeft / 2);
        }
    }

    public int getRootValue() {
        return this.value;
    }

    /**
     * Get a String representation of this node (i.e. the root of the tree).
     *
     * @return a String that represents the node.
     */
    public String getStringValue() {
        return this.value + "";
    }

    public int getMaxValue() {
        return this.maxInternalValue;
    }

    /**
     * Return true if at least one node in this tree has the given value
     *
     * @param testValue
     * @return true if at least one node is equal to the given value.
     */
    public boolean containsValue(int testValue) {
        return this.findValue(testValue) != null;
    }

    /**
     * Find a sub-tree of this tree that starts with the requested value
     *
     * @param testValue the value to look for
     * @return if it exists, the sub-tree that starts with testValue; otherwise,
     * null;
     */
    public Tree findValue(int testValue) {
        // Check if the entire tree starts with the requested value.
        if (this.value == testValue) {
            return this;
        } else {
            // Check if one branch starts with the requested value.
            for (Tree branch : this.branches) {
                if (branch.value == testValue) {
                    return branch;
                }
            }
            // Nothing found at the root of the branches, so we recursively search deeper.
            for (Tree branch : this.branches) {
                Tree result = branch.findValue(testValue);
                if (result != null) {
                    // Found the value in one sub-branch.
                    return result;
                }
            }
        }
        // Neither the root nor the branches contain the requested value.
        return null;
    }

    /**
     * Return true if the given node is part of the tree
     *
     * @param node
     * @return true if the given node is part of the tree
     */
    public boolean containsNode(Tree node) {
        return this.findNode(node) != null;
    }

    /**
     * Find a sub-tree of this tree that starts with the requested value
     *
     * @param node the node to look for
     * @return if it exists, the sub-tree that starts with the given node (not
     * just a node that has the same value, but the very same object);
     * otherwise, null;
     */
    public Tree findNode(Tree node) {
        // Check if the entire tree starts with the requested node.
        if (this == node) {
            return this;
        } else {
            // Check if one branch starts with the requested node.
            for (Tree branch : this.branches) {
                if (branch == node) {
                    return branch;
                }
            }
            // Nothing found at the root of the branches, so we recursively search deeper.
            for (Tree branch : this.branches) {
                Tree result = branch.findNode(node);
                if (result != null) {
                    // Found the value in one sub-branch.
                    return result;
                }
            }
        }
        // Neither the root nor the branches contain the requested node.
        return null;
    }

    /**
     * Add a new node as the root of the current tree. The former root becomes
     * the only child of the new root.
     *
     * @param newRootValue the value that will be the new root.
     */
    public void addRoot(int newRootValue) {

        // Clone the original root: that will become the first internal layer.
        Tree firstLayer = new Tree(this.value);

        // Add all the branches of the original root to the clone.
        for (Tree branch : this.branches) {
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
     * Replace the current root with the new one.
     * Keep the existing branches.
     *
     * @param newRootValue
     */
    public void setRootValue(int newRootValue) {
        this.value = newRootValue;
    }

    /**
     * Remove all children.
     *
     */
    public void clear() {
        this.branches.clear();
    }

    /**
     * Add a tree as a new branch.Find within the current tree the node whose
     * value is the root of the newly added tree; incorporate the newly added
     * tree as another branch of the current tree. If the root value of the new
     * branch is not found in the tree, do nothing.
     *
     * @param mergedTree the newly added tree
     */
    public void mergeBranch(Tree mergedTree) {
        int newVal = mergedTree.getRootValue();
        Tree addingPoint = this.findValue(newVal);
        if (addingPoint != null) {
            for (Tree addedBranch : mergedTree.branches) {
                addingPoint.branches.add(addedBranch);
            }
        }
    }

    /**
     * Apply a rotation around the origin to all the nodes of the graph.
     *
     * @param angle
     */
    public void rotate(double angle) {

        // Move the root of the graph.
        double oldX = this.x;
        double oldY = this.y;
        double c = Math.cos(angle);
        double s = Math.sin(angle);
        this.x = c * oldX - s * oldY;
        this.y = c * oldY + s * oldX;

        // Move all the branches.
        for (Tree branch : this.branches) {
            branch.rotate(angle);
        }
    }

    /**
     * Create and return a copy of the longest branch in this tree.
     *
     * @return a copy of the longest branch in this tree.
     */
    public Tree buildLongestBranch() {

        // Create a copy of the longest sub-branch, then add the current node copy as its root.
        Tree longestSubBranch = null;
        if (longestBranch != null) {
            longestSubBranch = longestBranch.buildLongestBranch();
            longestSubBranch.addRoot(this.value);
        } else {
            longestSubBranch = new Tree(this.value);
        }

        return longestSubBranch;
    }

    public void printLongestBranch() {
        System.out.println("" + this.buildLongestBranch());
    }

    public void increaseColumnSpacing(boolean bigger) {
        if (bigger) {
            columnSpacing *= 2;
        } else {
            columnSpacing /= 2;
            if (columnSpacing < 1) {
                columnSpacing = 1;
            }
        }
    }

    public void increaseLineSpacing(boolean bigger) {
        if (bigger) {
            lineSpacing *= 2;
        } else {
            lineSpacing /= 2;
            if (lineSpacing < 1) {
                lineSpacing = 1;
            }
        }
    }

    public void toggleDisplayDigits() {
        this.mustDisplayDigits = !this.mustDisplayDigits;
        for (Tree branch : this.branches) {
            branch.toggleDisplayDigits();
        }
    }

    /**
     * Return the first branch of this tree that starts with an even value.
     *
     * @return
     */
    public Tree getEvenBranch() {
        for (Tree branch : this.branches) {
            int val = branch.value;
            if (2 * (val / 2) == val) {
                // Found the even branch.
                return branch;
            }
        }
        return null;
    }

    /**
     * Return the number of nodes in that tree.
     *
     */
    public int getNbNodes() {
        int res = 1; // The root of this tree
        for (Tree branch : this.branches) {
            res += branch.getNbNodes();
        }
        return res;
    }

    /**
     * Check whether a tree is in fact a closed-loop graph, i.e. not actually a
     * valid tree
     *
     * @return true if at least one closed loop can be found.
     */
    public boolean isCyclic() {
        // We look for the root node in each of the branches.
        for (Tree branch : this.branches) {
            if (branch.containsNode(this)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get the apparent height of the node.
     *
     * @param g
     * @return
     */
    protected int getNodeHeight(Graphics g) {
        int heightInPixels = g.getFontMetrics().getHeight() / 2;
        return heightInPixels;
    }

    /**
     * Return the width of the root node
     *
     * @param g
     * @return
     */
    public int getNodeWidth(Graphics g) {
        int widthInPixels = g.getFontMetrics().stringWidth(getStringValue());
        return widthInPixels;
    }
}
