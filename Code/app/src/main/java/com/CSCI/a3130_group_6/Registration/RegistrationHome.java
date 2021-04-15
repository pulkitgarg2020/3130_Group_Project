package com.CSCI.a3130_group_6.Registration;
//
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.CSCI.a3130_group_6.R;

public class RegistrationHome extends AppCompatActivity implements View.OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_home);

        Button employee =  findViewById(R.id.employeeBtn);
        Button employer =  findViewById(R.id.employer);//getting info from view
        Button back =  findViewById(R.id.back);

        employee.setOnClickListener(this);
        employer.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    //Switching between pages
    public void switchToEmployer(){
        Intent employer = new Intent(this, RegistrationForEmployers.class);
        startActivity(employer);
    }

    public void switchToEmployee(){
        Intent employee = new Intent(this, RegistrationForEmployees.class);
        startActivity(employee);
    }
    public void switchToHome(){
        Intent back = new Intent(this, LoginPage.class);
        startActivity(back);
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case (R.id.employeeBtn):
                switchToEmployee();
                break;
            case (R.id.employer):
                switchToEmployer();
                break;
            case (R.id.back):
                switchToHome();
                break;
        }
    }
}