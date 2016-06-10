package sample;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class MainController {
    private static final String TASKS_PATH = ".tasks";
    private Tasks tasks = null;
    private ObservableList<String> taskNameList = FXCollections.observableArrayList();

    @FXML
    public ListView taskListView;
    @FXML
    public Button checkButton;
    @FXML
    public Button resetButton;
    @FXML
    public Button uncheckButton;

    @FXML
    public static List<Task> parseTasks(String input) {
        List<Task> list = new ArrayList<>();
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
            list.add(new Task(taskName, taskChecked));
        }
        return list;
    }

    @FXML
    public void initialize() {
        try {
            FileReader fr = new FileReader(TASKS_PATH);
            BufferedReader br = new BufferedReader(fr);
            String input = "", line;
            while((line = br.readLine()) != null) {
                input += line.trim();
            }
            this.tasks = new Tasks(parseTasks(input.trim()));
            for (Task task : this.tasks.getTaskList()) {
                this.taskNameList.add(task.description);
                System.out.println(task.description);
            }
            this.taskListView.setItems(taskNameList);
        } catch (FileNotFoundException e) {
            System.out.println("Tasks file not found at " + TASKS_PATH);
            System.exit(0);
        } catch (IOException e) {
            System.out.println("Error reading the file " + TASKS_PATH);
        }
    }

    @FXML
    public void onCheckButtonClick() {
        System.out.println("check");
    }

    @FXML
    public void onResetButtonClick() {
        System.out.println("reset");
    }

    @FXML
    public void onUncheckButtonClick() {
        System.out.println("uncheck");
    }
}
