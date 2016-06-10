package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class MainController {
    @FXML
    public ListView listView;
    @FXML
    public Button checkButton;
    @FXML
    public Button resetButton;

    @FXML
    public void initialize() {
        System.out.println("init");
    }
    @FXML
    public void onCheckButtonClick() {
        System.out.println("check");
    }

    @FXML
    public void onResetButtonClick() {
        System.out.println("reset");
    }
}
