package sample;

import java.util.HashMap;

/**
 * Data model: store the tasks in a HashMap Author: Duo Tao June 12, 2016
 */
public class Tasks {
    private HashMap<String, Task> taskDict;
    private int unfinishedCount;

    public Tasks() {
        this.taskDict = new HashMap<>();
        this.unfinishedCount = 0;
    }

    public Tasks(HashMap<String, Task> tasks) {
        this.taskDict = tasks;
        for (Task task : tasks.values()) {
            if (!task.checked) {
                this.unfinishedCount++;
            }
        }
    }

    public HashMap<String, Task> getTaskDict() {
        return this.taskDict;
    }

    public void check(String taskName) {
        if (taskName == null) {
            return;
        }
        this.taskDict.get(taskName).checked = true;
        this.unfinishedCount--;
    }

    public void reset() {
        for (Task t : this.taskDict.values()) {
            t.checked = false;
        }
        this.unfinishedCount = this.taskDict.size();
    }

    public void add(Task task) {
        if (task == null) {
            return;
        }
        this.taskDict.put(task.description, task);
        if (!task.checked) {
            this.unfinishedCount++;
        }
    }

    public void delete(String task) {
        if (task == null) {
            return;
        }
        Task removed = this.taskDict.remove(task);
        if (!removed.checked) {
            this.unfinishedCount--;
        }
    }

    public boolean isComplete() {
        return this.unfinishedCount == 0;
    }
}
