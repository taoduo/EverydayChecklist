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
        this.taskDict.get(taskName).checked = true;
    }

    public void uncheck(String taskName) {
        this.taskDict.get(taskName).checked = false;
    }

    public void reset() {
        for (Task t : this.taskDict.values()) {
            t.checked = false;
        }
    }

    public void delete(String task) {
        this.taskDict.remove(task);
    }
}
