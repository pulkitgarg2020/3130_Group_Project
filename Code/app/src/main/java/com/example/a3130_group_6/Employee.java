package com.example.a3130_group_6;

public class Employee {
    private String userName;
    private String password;
    private String phone;
    private String emailAddress;
    private String name;
    public Employee(){

    }
    public Employee(String userName, String password, String phone, String emailAddress, String name){
        this.userName = userName;
        this.password = password;
        this.phone = phone;
        this.emailAddress = emailAddress;
        this.name = name;
    }

    public String getUserName(){
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail(){return this.emailAddress;}

    public String getPhone(){return this.phone;}
}

