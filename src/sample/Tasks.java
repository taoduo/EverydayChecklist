package sample;

import java.util.HashMap;

public class Tasks {
    private HashMap<String, Task> taskDict;

    public Tasks() {
        this.taskDict = new HashMap<>();
    }

    public Tasks(HashMap<String, Task> tasks) {
        this.taskDict = tasks;
    }

    public HashMap<String, Task> getTaskDict() {
        return this.taskDict;
    }

    public void check(String taskName) {
        if (taskName == null) {
            return;
        }
        this.taskDict.get(taskName).checked = true;
    }

    public void reset() {
        for (Task t : this.taskDict.values()) {
            t.checked = false;
        }
    }

    public void delete(String task) {
        if (task == null) {
            return;
        }
        this.taskDict.remove(task);
    }
}
