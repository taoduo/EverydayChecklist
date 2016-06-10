package sample;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by duotao on 6/10/16.
 */
public class Tasks {
    private List<Task> taskList;

    public Tasks() {
        this.taskList = new ArrayList<>();
    }

    public Tasks(List<Task> tasks) {
        this.taskList = tasks;
    }

    public List<Task> getTaskList() {
        return this.taskList;
    }

    public void check(int index) {
        this.taskList.get(index).checked = true;
    }

    public void uncheck(int index) {
        this.taskList.get(index).checked = false;
    }

    public void reset() {
        for (Task t : this.taskList) {
            t.checked = false;
        }
    }


}
