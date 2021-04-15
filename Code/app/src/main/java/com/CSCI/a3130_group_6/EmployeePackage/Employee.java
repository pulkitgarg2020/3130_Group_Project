package com.CSCI.a3130_group_6.EmployeePackage;

public class Employee {
    private String userName;
    private String password;
    private String phone;
    private String email;
    private String name;
    private String description;
    private String resumeUrl;
    private String clientID;
    private String wallet;

    public Employee(){

    }
    public Employee(String userName, String password, String phone, String emailAddress, String name){
        this.userName = userName;
        this.password = password;
        this.phone = phone;
        this.email = emailAddress;
        this.name = name;
    }
    public Employee(String userName, String password, String phone, String emailAddress, String name, String resumeUrl){
        this.userName = userName;
        this.password = password;
        this.phone = phone;
        this.email = emailAddress;
        this.name = name;
        this.resumeUrl = resumeUrl;
    }

    public String getUserName(){
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getWallet() {
        return wallet;
    }

    public void setResumeUrl(String resumeUrl) {
        this.resumeUrl = resumeUrl;
    }

    public String getResumeUrl(){
        return resumeUrl;
    }

    public String getClientID(){
        return this.clientID;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmailAddress(String emailAddress) {
        this.email = emailAddress;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail(){return this.email;}

    public String getPhone(){return this.phone;}

    public void setDescription(String description) { this.description = description;
    }

    public String getDescription() { return description;
    }

}

