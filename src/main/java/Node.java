
import java.util.ArrayList;

import javafx.animation.FillTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * Node class
 * 
 * @author Akil Pathiranage
 */
public class Node {
    private Node left;
    private Node right;
    private Node parent;
    double radius = 25;

    private Circle circle;
    private Text text;
    private StackPane pane;

    private int level;
    int key;

    /**
     * Constructor for a node, this is specifically used for an animation node,
     * nodes created with this constructor should never be added to the tree, but instead used as
     * a place holder to check with the tree. 
     * @param key the value for the node
     */
    public Node(int key) {
        this.key = key;
        this.circle = new Circle(radius);
        this.circle.setFill(Color.ORANGE);
        this.circle.setStroke(Color.TRANSPARENT);;

        this.text = new Text();
        this.text.setText(Integer.toString(this.key));
        this.text.setFont(new Font("Microsoft YaHei Light", 10));
        this.text.setFill(Color.WHITE);

        this.pane = new StackPane();
        this.pane.setAlignment(Pos.CENTER);

        this.pane.getChildren().add(this.circle);
        this.pane.getChildren().add(this.text);
    }

    /**
     * This constructor is for creating nodes meant to be added to the tree. 
     * @param key the value for the node
     * @param parent the parent object of the node
     * @param level the level in the tree the node is at
     */
    public Node(int key, Node parent, int level) {
        left = null;
        right = null;
        if (parent == null){
            this.parent = null;
        } else {
            this.parent = parent;
        }
        this.level = level;
        this.key = key;
        this.circle = new Circle(radius);
        this.circle.setFill(Color.GREEN);
        this.circle.setStroke(Color.TRANSPARENT);;

        this.text = new Text();
        this.text.setText(Integer.toString(this.key));
        this.text.setFont(new Font("Microsoft YaHei Light", 10));
        this.text.setFill(Color.WHITE);
        //this.text.setBackground(Background.EMPTY);

        this.pane = new StackPane();
        this.pane.setAlignment(Pos.CENTER);

        this.pane.getChildren().add(this.circle);
        this.pane.getChildren().add(this.text);
    }

    /**
     * Method for inserting a node into the tree. 
     * @param nodeToAdd the node to be inserted
     * @param level the level at which it is being inserted in
     * @return a node object, represnting a reference to the node just added
     */
    public Node insert(Node nodeToAdd, int level) {
        //done recursively

        if (nodeToAdd.key < this.key) {
            if (this.left == null) {
                this.left = new Node(nodeToAdd.key, this, level);
                return this.left;
            } else {
                return this.left.insert(nodeToAdd, level+1);
            }
        } else {
            if (this.right == null) {
                this.right = new Node(nodeToAdd.key, this, level);
                return this.right;
            } else {
                return this.right.insert(nodeToAdd, level+1);
            }
        }
    }

    /**
     * This method if for checking whether a node would be inserted here in the tree.
     * This is used to make the visualization of the node moving down the tree, it seperates
     * the recursive part of the insertion so that each step can be seperated. If the node should be added below this node,
     * it will return itself, otherwise it will return the next node to check. 
     * @param nodeToAdd the node to add
     * @return a node object, representing the next node to check under or the node to add under. 
     */
    public Node nonRecursiveInsertion(Node nodeToAdd){
        if(nodeToAdd.key < this.key){
            if(this.left == null){
                return this;
            } else {
                return this.left;
            }
        } else {
            if(this.right == null){
                return this;
            } else {
                return this.right;
            }

        }
    }

    /**
     * Method for playing the animation for this node.
     * 
     * @param toX translation value in the x axis
     * @param toY translation value in the y axis
     */
    public void playAnimation(double toX, double toY){
        TranslateTransition tt = new TranslateTransition(Duration.millis(App.animationDuration), this.pane);
        tt.setToX(toX);
        tt.setToY(toY);
        tt.play();
    }

    /**
     * Method for traversing the tree in order, done recursively. 
     * @param sortedList the arraylist for the method to sort into
     * @return returns an arraylist, containing the elements previously sorted. 
     */
    public ArrayList<Node> inOrderTraversal(ArrayList<Node> sortedList){
        if(this.left != null){
            this.left.inOrderTraversal(sortedList);
        }
        sortedList.add(this);
        if(this.right!= null){
            this.right.inOrderTraversal(sortedList);
        }
        return sortedList;
    }

    /**
     * Method for highlighting the circle.
     */
    public void highlight(){
        FillTransition highlightTransition= new FillTransition(Duration.millis(App.animationDuration/2), this.circle, Color.GREEN, Color.ORANGE);
        highlightTransition.play();
        //this.circle.setFill(Color.ORANGE);
    }

    /**
     * Method for undoing the highlight on a circle.
     */
    public void unHighlight(){
        FillTransition unHighlightTransition = new FillTransition(Duration.millis(App.animationDuration/2), this.circle, Color.ORANGE, Color.GREEN);
        unHighlightTransition.play();
        //this.circle.setFill(Color.GREEN);
    }

    /**
     * Method for getting the current level where a node is in the tree
     * the level is defined by the level of its parent + 1. The root level is set to zero, 
     * any nodes added directly under the root becomes 1.
     * @return an integer
     */
    public int getLevel(){
        return this.level;
    }
    /**
     * Method for getting the instance of the left child
     * @return a Node object, returns null if there isn't a right child
     */
    public Node getLeft(){
        return this.left;
    }

    /**
     * Method for getting the instance of the right child
     * @return a Node object, returns null if there isn't a right child
     */
    public Node getRight(){
        return this.right;
    }

    /**
     * Method for seeing if a node has a right child
     * @return true if the node has a right child, false if not
     */
    public boolean hasRight(){
        return (this.right != null);
    }

    /**
     * Method for seeing if a node has a left child
     * @return true if the node has a left child, false if not
     */
    public boolean hasLeft(){
        return (this.left != null);
    }

    /**
     * Method for getting the instance of the parent of a node
     * 
     * @return A Node object, returns null if there isn't a parent
     */
    public Node getParent(){
        return this.parent;
    }

    /**
     * Method for getting the VBox associated with this node, this contains the visual for this node.
     * @return
     */
    public StackPane getVisual(){
        return this.pane;
    }
}