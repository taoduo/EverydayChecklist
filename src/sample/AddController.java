package sample;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controller for the add task window
 */
public class AddController {
    @FXML
    public TextField newTaskDesc;
    @FXML
    public Button cancelButton;
    @FXML
    public Button addButton;
    public static MainController mainController;

    @FXML
    public void onCancelButtonClick(Event e) {
        Stage stage = (Stage) ((Button) (e.getSource())).getScene().getWindow();
        stage.close();
    }

    @FXML
    public void onAddButtonClick(Event e) {
        MainController.tasks.getTaskDict().put(this.newTaskDesc.getText(),
                new Task(this.newTaskDesc.getText(), false));
        mainController.update();
        Stage stage = (Stage) ((Button) (e.getSource())).getScene().getWindow();
        stage.close();
    }
}
