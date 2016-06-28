package com.codepath.simpletodo;

/**
 * Created by seetha on 6/26/16.
 */

import com.orm.SugarRecord;

/**
 * Task uses SugarRecord to create Task object to be stored in SQLite
 */
public class Task extends SugarRecord {

    public String taskText;
    public String dueDate;
    public String priority = "Low";
    public boolean completed = false;
    public String classification = "Personal";

    /**
     * Task empty constructor
     */
    public Task() {
        super();
    }

    /**
     * Task constructor
     *
     * @param taskText
     * @param dueDate
     * @param priority
     * @param completed
     * @param classification
     */
    public Task(String taskText, String dueDate, String priority, boolean completed, String classification) {
        super();
        this.taskText = taskText;
        this.dueDate = dueDate;
        this.priority = priority;
        this.completed = completed;
        this.classification = classification;
    }

    /**
     * Prints task information for debugging purposes
     */
    public void printTask() {
        System.out.println("task printing " + taskText + " " + dueDate + " " + priority + " " + classification);

    }
}
