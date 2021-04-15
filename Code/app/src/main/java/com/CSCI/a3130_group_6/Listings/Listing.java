package com.CSCI.a3130_group_6.Listings;

import com.CSCI.a3130_group_6.HelperClases.UserLocation;
import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Class for adding listing to the database
 *
 * @author  Pulkit, Han, Emily
 */
public class Listing {
    private String taskTitle;
    private String taskDescription;
    private String urgency;
    private String date;
    private String pay;
    private String status;
    private UserLocation location;
    private static int count = 0;
//    private String key;

    public Listing() {}


   public Listing(String taskTitle, String taskDescription, String urgency, String date, String pay, String status, String key) {
        this.taskTitle = taskTitle;
        this.taskDescription = taskDescription;
        this.urgency = urgency;
        this.date = date;
        this.pay = pay;
        this.status = status;
        //this.key = key;
        count ++;
    }


    public Listing(String taskTitle, String taskDescription, String urgency, String date, String pay,String status, UserLocation location) {
        this.taskTitle = taskTitle;
        this.taskDescription = taskDescription;
        this.urgency = urgency;
        this.date = date;
        this.pay = pay;
        this.status = status;
        this.location = location;
    }


    public UserLocation getLocation() {
        return location;
    }

    public void setLocation(UserLocation location) {
        this.location = location;
    }


    public String getTaskTitle() {
        return taskTitle;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus(){ return this.status; }

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

    public static int getCount() {
        return count;
    }

    public static void setCount(int count) {
        Listing.count = count;
    }


    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("taskTitle", taskTitle);
        result.put("taskDescription",taskDescription);
        result.put("urgency" ,urgency);
        result.put("date",date);
        result.put("pay",pay);
        result.put("status",status);
        //result.put("key",key);
        return result;
    }
}
