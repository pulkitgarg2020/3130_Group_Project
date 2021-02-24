package com.example.a3130_group_6;

/*
Class for adding listing to the database
 */
public class Listing {
    private String taskTitle;
    private String taskDescription;
    private String urgency;
    private String date;
    private String pay;
    private static int count = 0;

    public Listing() {
    }

    public Listing(String taskTitle, String taskDescription, String urgency, String date, String pay) {
        this.taskTitle = taskTitle;
        this.taskDescription = taskDescription;
        this.urgency = urgency;
        this.date = date;
        this.pay = pay;
        count ++;
    }

    public int  increment(){
        return count;
    }
    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getUrgency() {
        return urgency;
    }

    public void setUrgency(String urgency) {
        this.urgency = urgency;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }
}
