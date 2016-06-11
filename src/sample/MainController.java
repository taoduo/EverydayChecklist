package sample;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MainController {
    private static final String TASKS_PATH = ".tasks";
    private static final int EDIT_WINDOW_WIDTH = 300;
    private static final int EDIT_WINDOW_HEIGHT = 100;
    public static Tasks tasks = null;
    public static String currentSelectedText;
    @FXML
    public Label titleLabel;
    @FXML
    public ListView taskListView;
    @FXML
    public Button checkButton;
    @FXML
    public Button resetButton;
    @FXML
    public Button editButton;
    @FXML
    public AnchorPane mainScene;


    private static HashMap<String, Task> parseTasks(String input) {
        HashMap<String, Task> map = new HashMap<>();
        String pattern = "([^#]*)#([tf])";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(input);
        while (m.find()) {
            String taskName = m.group(1);
            boolean taskChecked;
            if (m.group(2).equals("t")) {
                taskChecked = true;
            } else if (m.group(2).equals("f")) {
                taskChecked = false;
            } else {
                continue;
            }
            map.put(taskName, new Task(taskName, taskChecked));
        }
        return map;
    }

    @FXML
    public void initialize() throws IOException {
        File f = new File(TASKS_PATH);
        f.createNewFile();
        FileReader fr = new FileReader(f);
        BufferedReader br = new BufferedReader(fr);
        String input = "", line;
        while ((line = br.readLine()) != null) {
            input += line.trim();
        }
        tasks = new Tasks(parseTasks(input.trim()));
        this.update();
        this.taskListView.getSelectionModel().selectedItemProperty().addListener(event -> {
            if (this.taskListView.getSelectionModel().getSelectedIndices().size() != 0) {
                this.editButton.setDisable(false);
                currentSelectedText = (String) this.taskListView.getSelectionModel()
                        .getSelectedItems().get(0);
            } else {
                this.editButton.setDisable(true);
                currentSelectedText = null;
            }
        });
        this.taskListView.setOnKeyTyped(event -> {
            String c = event.getCharacter();
            switch (c) {
                case " ":
                    this.checkButton.fire();
                    break;
                case "r":
                    this.resetButton.fire();
            }
        });
        DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        Date date = new Date();
        titleLabel.setText(titleLabel.getText() + " as of " + dateFormat.format(date));
    }

    @FXML
    public void onCheckButtonClick() {
        ObservableList<String> selectedList = taskListView.getSelectionModel().getSelectedItems();
        for (String string : selectedList) {
            tasks.check(string);
        }
        this.update();
    }

    @FXML
    public void onResetButtonClick() {
        tasks.reset();
        this.update();
    }

    @FXML
    public void onEditButtonClick() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("EditView.fxml"));
        Parent root = loader.load();
        Stage editStage = new Stage();
        editStage.setTitle("Edit Task");
        editStage.setScene(new Scene(
                root, EDIT_WINDOW_WIDTH, EDIT_WINDOW_HEIGHT));
        editStage.sizeToScene();
        editStage.setResizable(false);
        editStage.initOwner(this.taskListView.getScene().getWindow());
        editStage.show();
        ((EditController) loader.getController()).setEditText((String) this.taskListView.
                getSelectionModel().getSelectedItem());
        EditController.mainController = this;
        editStage.getIcons().add(new Image("file:favicon.jpg"));

    }

    @FXML
    public void onAddButtonClick() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddView.fxml"));
        Parent root = loader.load();
        Stage addStage = new Stage();
        addStage.setTitle("Add Task");
        addStage.setScene(new Scene(
                root, EDIT_WINDOW_WIDTH, EDIT_WINDOW_HEIGHT));
        addStage.sizeToScene();
        addStage.setResizable(false);
        addStage.initOwner(this.taskListView.getScene().getWindow());
        addStage.show();
        AddController.mainController = this;
        addStage.getIcons().add(new Image("file:favicon.jpg"));
    }

    @FXML
    public void onDeleteButtonClick() {
        ObservableList<String> selectedList = taskListView.getSelectionModel().getSelectedItems();
        for (String string : selectedList) {
            tasks.delete(string);
        }
        this.update();
    }

    public void update() {
        ObservableList<String> taskNameList = FXCollections.observableArrayList();
        for (Task task : tasks.getTaskDict().values()) {
            if (!task.checked) {
                taskNameList.add(task.description);
            }
        }
        tasks.getTaskDict().values().stream()
                .filter(task -> !task.checked)
                .map(task -> {
                    taskNameList.add(task.description);
                    return task;
                });
        this.taskListView.setItems(taskNameList);
    }

    public static void saveStatus() throws IOException {
        FileWriter fw = new FileWriter(TASKS_PATH, false);
        for (String taskName : tasks.getTaskDict().keySet()) {
            fw.write(taskName);
            if (tasks.getTaskDict().get(taskName).checked) {
                fw.write("#t");
            } else {
                fw.write("#f");
            }
        }
        fw.close();
    }
}
