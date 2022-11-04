import java.io.IOException;
import javafx.fxml.FXML;

/**
 * Controller class for the how does it work scene.
 * 
 * @author Mr.Brooks
 */
public class SecondaryController {

    /**
     * Method for switching the the mains screen.
     * @throws IOException
     */
    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }
}