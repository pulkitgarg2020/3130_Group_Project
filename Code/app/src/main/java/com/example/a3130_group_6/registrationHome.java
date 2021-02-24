package com.example.a3130_group_6;
//
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class registrationHome extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_registration_home);

        Button employee =  findViewById(R.id.employeeBtn);
        Button employer =  findViewById(R.id.employer);
        Button back =  findViewById(R.id.back);

        employee.setOnClickListener(this);
        employer.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    protected void switchToEmployer(){
        Intent employer = new Intent(this, registrationForEmployers.class);
        startActivity(employer);
    }

    protected void switchToEmployee(){
        Intent employee = new Intent(this, registrationForEmployees.class);
        startActivity(employee);
    }
    protected void switchToHome(){
        Intent back = new Intent(this, loginPage.class);
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