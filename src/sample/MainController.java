package sample;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Controller for the Main window Author: Duo Tao June 12, 2016
 */
public class MainController {
    private static final String TASKS_PATH = ".tasks";
    private static final int EDIT_WINDOW_WIDTH = 300;
    private static final int EDIT_WINDOW_HEIGHT = 100;
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("MMM dd, yyyy");
    private static int daysKept = 0;
    private static Calendar previousCompleteDate = new GregorianCalendar();
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

    /**
     * Show a congratulations message upon completing all tasks of today
     */
    private static void showCompleteAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(daysKept + " days of tasks complete!");
        alert.setContentText("You have persisted to your plan for " + daysKept + " days.\nKeep it up! You can do it!");
        alert.showAndWait();
    }

    /**
     * Handle behaviors on all task for today completed
     */
    private static void tasksComplete() {
        Calendar now = new GregorianCalendar();
        // if previous date is not today
        if (!(now.get(Calendar.YEAR) == previousCompleteDate.get(Calendar.YEAR) &&
                now.get(Calendar.MONTH) == previousCompleteDate.get(Calendar.MONTH) &&
                now.get(Calendar.DATE) == previousCompleteDate.get(Calendar.DATE))) {
            now.add(Calendar.DATE, -1);
            // if previous date is yesterday
            if (now.get(Calendar.YEAR) == previousCompleteDate.get(Calendar.YEAR) &&
                    now.get(Calendar.MONTH) == previousCompleteDate.get(Calendar.MONTH) &&
                    now.get(Calendar.DATE) == (previousCompleteDate.get(Calendar.DATE))) {
                daysKept++;
            } else {
                daysKept = 1;
            }
            previousCompleteDate = new GregorianCalendar();
            showCompleteAlert();
        }
    }

    /**
     * parse the .task file and get three configs from it: previous completed day, how many days are
     * kept and the task status for today
     *
     * @param input the input text
     * @return the constructed HashMap of the tasks
     */
    private static HashMap<String, Task> parseTasks(String input) {
        String[] split = input.split("\\n");
        // get the previous day
        String previousDateString = split[0];
        try {
            previousCompleteDate.setTime(DATE_FORMAT.parse(previousDateString));
        } catch (ParseException e) {
            previousCompleteDate = new GregorianCalendar();
            e.printStackTrace();
        }
        // get the days kept
        String daysKeptString = split[1];
        Matcher dm = Pattern.compile("DAYS_KEPT=([0-9]+)").matcher(daysKeptString);
        if (dm.find()) {
            daysKept = Integer.parseInt(dm.group(1));
        }
        // get the tasks
        String taskString = split[2];
        HashMap<String, Task> map = new HashMap<>();
        String pattern = "([^#]*)#([tf])";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(taskString);
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
            input += line.trim() + "\n";
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
        Date date = new Date();
        titleLabel.setText(titleLabel.getText() + " as of " + DATE_FORMAT.format(date));
    }

    @FXML
    public void onCheckButtonClick() {
        ObservableList<String> selectedList = taskListView.getSelectionModel().getSelectedItems();
        for (String string : selectedList) {
            tasks.check(string);
        }
        this.update();
        if (tasks.isComplete()) {
            tasksComplete();
        }
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

    /**
     * Update the views according to the task list
     */
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

    /**
     * Save the status to the .task file on quit
     * @throws IOException when the writing has some problem
     */
    public static void saveStatus() throws IOException {
        FileWriter fw = new FileWriter(TASKS_PATH, false);
        fw.write(DATE_FORMAT.format(previousCompleteDate.getTime()) + "\n");
        fw.write("DAYS_KEPT=" + daysKept + "\n");
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
