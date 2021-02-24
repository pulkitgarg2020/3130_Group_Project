package com.example.a3130_group_6;

public class Employer {

    private String userName;
    private String password;
    private String phone;
    private String emailAddress;
    private String name;
    private String buisnessName;
    public Employer(){

    }
    public Employer(String userName, String password, String phone, String emailAddress, String name, String buisnessName){
        this.userName = userName;
        this.password = password;
        this.phone = phone;
        this.emailAddress = emailAddress;
        this.name = name;
        this.buisnessName = buisnessName;
    }

    public String getUserName(){
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public String getBuisnessName() {
        return buisnessName;
    }

    public String getName() {
        return name;
    }

    public String getEmailAddress() {
        return emailAddress;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setBuisnessName(String buisnessName) {
        this.buisnessName = buisnessName;
    }
}
