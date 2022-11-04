
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Random;

/**
 * JavaFX App
 * 
 * @author Akil Pathiranage
 * @version 1.0
 */
public class App extends Application {

    private static Scene scene;
    public static int animationDuration = 500;
    public static int setLength = 20;

    /**
     * Method for starting the application.
     * 
     * @param stage a stage object to attach the scene to. 
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("primary"), 1600, 900);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method is for changing the scemes, switching between fxml files
     * @param fxml pathname to FXMl file without the extension.
     * @throws IOException
     */
    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    /**
     * This method loads the fxml file.
     * @param fxml pathname of the fxml file without extension
     * @return a Parent object
     * @throws IOException
     */
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    /**
     * Main method for application.
     * @param args
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * Method for getting the instance of the scene.
     * 
     * @return the scene object being used.
     */
    public static Scene getScene(){
        return scene;
    }

    /**
     * This method generates a random array of a sie of your choice.
     * It picks numbers between 0 and the size -1, inclusive.
     * @return an array of integers
     */
    public static int[] generateArray(){
        int[] newArray = new int[setLength];
        Random rand = new Random();
        for(int i = 0; i < setLength; i++){
            newArray[i] = rand.nextInt(setLength);
        }
        return newArray;
    }

}