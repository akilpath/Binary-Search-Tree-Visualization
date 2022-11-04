import java.util.ArrayList;
import java.util.Arrays;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * Controller class for the main visualization. 
 * 
 * @author Akil Pathiranage
 */
public class PrimaryController {

    @FXML
    VBox screen = new VBox();
    Pane visualizationBox;
    Node root;

    Text sortedArrayText;
    Label evaluationLabel;

    Slider animationDurationSlider;
    Slider arrayLengthSlider;
    Label arrayLength;

    ButtonBar buttonBar;

    Timeline animation;

    int[] arr;

    ArrayList<Integer> sortedArray = new ArrayList<Integer>();
    ArrayList<Node> sortedNodes;

    boolean findingNode = false;

    Node lastNode;
    Node currentNode;
    Node nodeToInsert;
    Node newNode;

    int stepCount = 0;
    int sortedStepCount = 0;

    /**
     * FXML initialize method for the main scene
     */
    @FXML
    public void initialize() {
        arr = App.generateArray();
        Label arrayTitle = new Label();
        arrayTitle.setText("Randomly Generated Array " + Arrays.toString(arr));
        arrayTitle.setFont(new Font("Microsoft YaHei Light", 30));
        arrayTitle.setTextFill(Color.WHITE);
        arrayTitle.setId("randomArrayTitle");

        evaluationLabel = new Label();
        evaluationLabel.setFont(new Font("Microsoft YaHei Light", 30));
        evaluationLabel.setTextFill(Color.WHITE);

        visualizationBox = new Pane();
        visualizationBox.setId("visualizationBox");
        visualizationBox.setMinHeight(500);
        visualizationBox.setMinWidth(1280);
        visualizationBox.setMaxHeight(500);
        visualizationBox.setMaxWidth(1280);
        visualizationBox.setStyle("-fx-background-color: transparent;");

        // Elements for the animation speed and array length
        HBox controlContainer = new HBox();
        controlContainer.setAlignment(Pos.CENTER);
        controlContainer.setSpacing(30);

        Label animationSpeed = new Label();
        animationSpeed.setText("Animation Speed:");
        animationSpeed.setFont(new Font("Microsoft YaHei Light", 20));
        animationSpeed.setTextFill(Color.WHITE);

        animationDurationSlider = new Slider();
        animationDurationSlider.setMaxWidth(100);
        animationDurationSlider.setMax(1400);
        animationDurationSlider.setMin(100);
        animationDurationSlider.setValue(1400- App.animationDuration);
        animationDurationSlider.setOnMouseReleased(this::updateAnimationSpeed);

        arrayLength = new Label();
        arrayLength.setText("Array Length: " + App.setLength);
        arrayLength.setFont(new Font("Microsoft YaHei Light", 20));
        arrayLength.setTextFill(Color.WHITE);

        arrayLengthSlider = new Slider();
        arrayLengthSlider.setMaxWidth(200);
        arrayLengthSlider.setMax(20);
        arrayLengthSlider.setMin(3);
        arrayLengthSlider.setValue(App.setLength);
        arrayLengthSlider.setOnMouseReleased(this::updateArrayLength);

        controlContainer.getChildren().addAll(animationSpeed, animationDurationSlider, arrayLength, arrayLengthSlider);
        //--------------------------------------------------


        Button step = new Button("Step");
        step.setFont(new Font("Microsoft YaHei Light", 15));
        step.setTextFill(Color.WHITE);
        step.setStyle("-fx-background-color: transparent;");
        step.setOnAction(this::step);
        step.setId("stepButton");

        Button reset = new Button("Reset");
        reset.setFont(new Font("Microsoft YaHei Light", 15));
        reset.setTextFill(Color.WHITE);
        reset.setStyle("-fx-background-color: transparent;");
        reset.setOnAction(this::resetTree);
        reset.setId("resetButton");

        Button autoSort = new Button("Auto sort");
        autoSort.setFont(new Font("Microsoft YaHei Light", 15));
        autoSort.setTextFill(Color.WHITE);
        autoSort.setStyle("-fx-background-color: transparent;");
        autoSort.setOnAction(this::fullSort);
        autoSort.setId("autoSortButton");

        Button generateNewArray = new Button("Generate New Set");
        generateNewArray.setFont(new Font("Microsoft YaHei Light", 15));
        generateNewArray.setTextFill(Color.WHITE);
        generateNewArray.setStyle("-fx-background-color: transparent;");
        generateNewArray.setOnAction(this::generateNewSet);
        generateNewArray.setId("generateArrayButton");

        Button explanationButton = new Button("How does it work?");
        explanationButton.setFont(new Font("Microsoft YaHei Light", 15));
        explanationButton.setTextFill(Color.WHITE);
        explanationButton.setStyle("-fx-background-color: transparent;");
        explanationButton.setOnAction(this::switchToSecondary);

        buttonBar = new ButtonBar();
        buttonBar.getButtons().addAll(step, autoSort, reset, generateNewArray, explanationButton);

        sortedArrayText = new Text();

        screen.getChildren().addAll(arrayTitle, evaluationLabel, visualizationBox, controlContainer, sortedArrayText, buttonBar);

        root = new Node(arr[0], null, 0);
        visualizationBox.getChildren().add(root.getVisual());
        root.getVisual().setLayoutX(visualizationBox.getMinWidth() / 2 - root.radius);
        root.getVisual().setLayoutY(40);
    }

    /**
     * On action method for the generate new set of numbers button.
     * This method generates a new array and resets program to match it.
     * 
     * @param event
     */
    private void generateNewSet(ActionEvent event) {
        arr = App.generateArray();
        Label arrayTitle = (Label) screen.lookup("#randomArrayTitle");
        arrayTitle.setText("Randomly Generated Array " + Arrays.toString(arr));

        resetTree(event);

    }

    /**
     * ActionEvent for the Auto sort button, allows user to see the visual without
     * having to click the step button each time.
     * 
     * @param event ActionEvent object.
     */
    private void fullSort(ActionEvent event) {
        //uses a keyframe animation to perform step automatically. 
        Button autoSortButton = (Button) buttonBar.lookup("#autoSortButton");
        Button stepButton = (Button) buttonBar.lookup("#stepButton");
        Button generateArrayButton = (Button) buttonBar.lookup("#generateArrayButton");
        stepButton.setDisable(true);
        autoSortButton.setDisable(true);
        generateArrayButton.setDisable(true);
        animationDurationSlider.setDisable(true);

        animation = new Timeline(new KeyFrame(Duration.millis(App.animationDuration), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                step(e);
            }
        }));
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play();
    }

    /**
     * ActionEvent for the step button, allows the user to step through the process
     * of building and traversing the binary search tree.
     * 
     * @param event ActionEvent object
     */
    private void step(ActionEvent event) {
        if (stepCount < arr.length - 1 || findingNode) {
            // if the algorithm is in the process of constructing the tree
            if (findingNode) {
                // if the algorithm is in the process of moving a node down the tree
                if (newNode != null) {
                    // if the algorithm has placed a temporary node in the tree.

                    // removes the temporary node and adds the new proper node in its place.
                    visualizationBox.getChildren().remove(nodeToInsert.getVisual());
                    visualizationBox.getChildren().add(newNode.getVisual());

                    Line connector = new Line(newNode.getParent().getVisual().getLayoutX() + newNode.radius,
                            newNode.getParent().getVisual().getLayoutY() + newNode.radius,
                            newNode.getParent().getVisual().getLayoutX() + newNode.radius,
                            newNode.getParent().getVisual().getLayoutY() + newNode.radius);
                    visualizationBox.getChildren().add(connector);
                    connector.toBack();

                    // animates the line connecting the new node into the tree
                    // found out how to animate lines from this stack overflow post
                    // https://stackoverflow.com/questions/50109132/javafx-shortening-a-line-on-1-side
                    Timeline connectorAnimation = new Timeline(new KeyFrame(Duration.millis(App.animationDuration / 2),
                            new KeyValue(connector.endXProperty(), newNode.getVisual().getLayoutX() + newNode.radius),
                            new KeyValue(connector.endYProperty(), newNode.getVisual().getLayoutY() + newNode.radius)));

                    connectorAnimation.setCycleCount(1);
                    connectorAnimation.play();
                    evaluationLabel.setText("Inserted");

                    // resets variables
                    findingNode = false;
                    newNode = null;
                } else if (currentNode.equals(lastNode)) {
                    // if this node is equal to the last node
                    // this indicates that there are no more nodes to traverse down, it should be
                    // added here

                    newNode = currentNode.insert(nodeToInsert, currentNode.getLevel() + 1);
                    double parentPosX = newNode.getParent().getVisual().getLayoutX();
                    double parentPosY = newNode.getParent().getVisual().getLayoutY();
                    double newPosX;
                    double newPosY;

                    // nodes coordinates are set relative to their parent's node and the change
                    // is determined by the exponential function f(x) = 375*(0.75)^x
                    if (newNode.equals(newNode.getParent().getLeft())) {
                        newPosX = parentPosX - 375 * Math.pow(0.75, newNode.getLevel());
                        // visualizationBox.getChildren().add(newNode.getVisual());
                        newNode.getVisual().setLayoutX(newPosX);
                    } else {
                        newPosX = parentPosX + 375 * Math.pow(0.75, newNode.getLevel());
                        // visualizationBox.getChildren().add(newNode.getVisual());
                        newNode.getVisual().setLayoutX(newPosX);
                    }
                    newPosY = parentPosY + 50;
                    newNode.getVisual().setLayoutY(newPosY);

                    // animates its motion to its new spot
                    // removes the highlight from the circle
                    nodeToInsert.playAnimation(newPosX - nodeToInsert.getVisual().getLayoutX(),
                            newPosY - nodeToInsert.getVisual().getLayoutY());
                    nodeToInsert.unHighlight();
                } else {
                    // this updates the current node variable and last node variable. If the node
                    // cannot further traverse
                    // down lastNode and currentNode will refer to the same object
                    lastNode = currentNode;
                    currentNode = currentNode.nonRecursiveInsertion(nodeToInsert);
                    nodeToInsert.playAnimation(
                            currentNode.getVisual().getLayoutX() - nodeToInsert.getVisual().getLayoutX(),
                            currentNode.getVisual().getLayoutY() - currentNode.radius * 2
                                    - nodeToInsert.getVisual().getLayoutY());
                    //shows the evaluation the algorithm would be doing at this stage.
                    evaluationLabel.setText(nodeToInsert.key + " < " + currentNode.key);
                }

            } else {
                //if it is beginning a cycle, it creates a new node to insert,
                //has the node fade in and resets vraiables. 
                nodeToInsert = new Node(arr[++stepCount]);
                nodeToInsert.getVisual().setId("insertionNode");
                nodeToInsert.getVisual().setOpacity(0);
                visualizationBox.getChildren().add(nodeToInsert.getVisual());
                nodeToInsert.getVisual().setLayoutX(visualizationBox.getMinWidth() / 2 - nodeToInsert.radius);
                FadeTransition fade = new FadeTransition(Duration.millis(App.animationDuration / 2),
                        nodeToInsert.getVisual());
                fade.setToValue(1);
                fade.play();

                evaluationLabel.setText("Beginning evaluations");
                lastNode = null;
                currentNode = root;
                findingNode = true;
            }
            // creates the sorted list of nodes
            if (stepCount == arr.length - 1) {
                //gets the sorted list of all nodes in the tree.
                sortedNodes = root.inOrderTraversal(new ArrayList<Node>());
            }

        } else if (sortedStepCount < sortedNodes.size()) {
            //if the algorithm is now performing in order traversal
            //this highlights all nodes in order, which visually shows 
            //how the binary tree is being traversed. 
            evaluationLabel.setText("Performing in order traversal");

            this.sortedArrayText.setFont(new Font("Microsoft YaHei Light", 20));
            this.sortedArrayText.setFill(Color.WHITE);
            this.sortedArrayText.setStyle("-fx-background-color: transparent");
            sortedNodes.get(sortedStepCount).highlight();
            if (sortedStepCount >= 1) {
                sortedNodes.get(sortedStepCount - 1).unHighlight();
            }
            sortedArray.add(sortedNodes.get(sortedStepCount).key);
            this.sortedArrayText.setText(Arrays.toString(sortedArray.toArray()));
            sortedStepCount++;
        } else {
            //enables buttons and stops autosort animation. 
            Button autoSortButton = (Button) buttonBar.lookup("#autoSortButton");
            Button stepButton = (Button) buttonBar.lookup("#stepButton");
            Button generateArrayButton = (Button) buttonBar.lookup("#generateArrayButton");
            stepButton.setDisable(false);
            autoSortButton.setDisable(false);
            generateArrayButton.setDisable(false);
            evaluationLabel.setText("Finished sort");
            animation.stop();
        }
    }

    /**
     * This method is for updating the animation speed. It is called by the
     * animation slider.
     * 
     * @param e MouseEvent object
     */
    public void updateAnimationSpeed(MouseEvent e) {
        App.animationDuration = 1500- (int) animationDurationSlider.getValue();
        System.out.println(App.animationDuration);
    }

    /**
     * This method is for updating the array length. It is called by the array
     * length slider.
     * 
     * @param e MouseEvent
     */
    public void updateArrayLength(MouseEvent e) {
        App.setLength = (int) arrayLengthSlider.getValue();
        arrayLength.setText("Array Length: " + App.setLength);
        generateNewSet(null);
    }

    /**
     * Method for resetting the tree and variables for sorting.
     * 
     * @param e ActionEvent object
     */
    public void resetTree(ActionEvent e) {
        //enables all buttons
        Button autoSortButton = (Button) buttonBar.lookup("#autoSortButton");
        Button stepButton = (Button) buttonBar.lookup("#stepButton");
        Button generateArrayButton = (Button) buttonBar.lookup("#generateArrayButton");
        stepButton.setDisable(false);
        autoSortButton.setDisable(false);
        generateArrayButton.setDisable(false);
        animationDurationSlider.setDisable(false);

        //stops animation if there is one
        try {
            animation.stop();
        } catch (Exception exception) {
        }

        //clears the tree visuals from the scene graph
        visualizationBox.getChildren().clear();

        //resets the root node
        root = new Node(arr[0], null, 0);
        visualizationBox.getChildren().add(root.getVisual());
        root.getVisual().setLayoutX(visualizationBox.getMinWidth() / 2 - root.radius);
        root.getVisual().setLayoutY(40);

        //resets variables
        evaluationLabel.setText("");
        stepCount = 0;
        sortedStepCount = 0;
        findingNode = false;
        newNode = null;
        sortedArray.clear();
        sortedArrayText.setText("");
    }

    /**
     * Method for switching to the how does it work screen.
     * 
     * @param event ActionEvent object
     */
    public void switchToSecondary(ActionEvent event) {
        try {
            App.setRoot("secondary");
        } catch (Exception e) {
        }
    }

}
