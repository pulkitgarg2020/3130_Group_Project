package com.CSCI.a3130_group_6.HelperClases;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.CSCI.a3130_group_6.EmployerPackage.EmployerHomepage;
import com.CSCI.a3130_group_6.EmployerPackage.EmployerProfile;
import com.CSCI.a3130_group_6.Listings.ListingHistory;
import com.CSCI.a3130_group_6.Registration.LoginPage;


public class EmployerNavBarRouting {

    Context context;

    public EmployerNavBarRouting(Context context){
        this.context=context;
    }
    public void profileSwitch( Context context) {
        Intent switchIntent = new Intent(context, EmployerProfile.class);
        switchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(switchIntent);
    }
    public void homepageSwitch( Context context) {
        Intent switchIntent = new Intent(context, EmployerHomepage.class);
        switchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(switchIntent);
    }

    public void switchListingHistory( Context context) {
        Intent switchIntent = new Intent(context, ListingHistory.class);
        switchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(switchIntent);
    }
    public void LogoutSwitch(Context context) {
        Toast.makeText(context, "Logging out", Toast.LENGTH_SHORT).show();
        LoginPage.validEmployer = null;
        ListingHistory.employerRef=null;
        Intent switchIntent = new Intent(context, LoginPage.class);
        switchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(switchIntent);
    }/*
    public void chatSwitch( Context context) {
        Intent switchIntent = new Intent(context, EmployerChatList.class);
        switchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(switchIntent);
    }*/
}
