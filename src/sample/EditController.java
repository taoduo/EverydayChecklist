package sample;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controller for the task edit window
 */
public class EditController {
    @FXML
    public TextField editText;
    @FXML
    public Button cancelButton;
    @FXML
    public Button saveButton;
    public static MainController mainController;
    private String initializeText;

    @FXML
    public void initialize() {
        this.initializeText = MainController.currentSelectedText;
    }
    @FXML
    public void onCancelButtonClick(Event e) {
        Stage stage = (Stage) ((Button) (e.getSource())).getScene().getWindow();
        stage.close();
    }

    @FXML
    public void onSaveButtonClick(Event e) {
        MainController.tasks.getTaskDict().remove(initializeText);
        MainController.tasks.getTaskDict().put(this.editText.getText(),
                new Task(this.editText.getText(), false));
        mainController.update();
        Stage stage = (Stage) ((Button) (e.getSource())).getScene().getWindow();
        stage.close();
    }

    /**
     * Get the original text from the main window
     *
     * @param initString the original text, passed from MainController
     */
    public void setEditText(String initString) {
        this.editText.setText(initString);
        this.editText.selectAll();
    }
}
