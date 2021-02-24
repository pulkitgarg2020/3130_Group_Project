package com.example.a3130_group_6;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class employers extends AppCompatActivity {

    TextView name,username,password,vpassword,phone,email;
    Button home,addPayment,submit,switchToEmployee;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        name = findViewById(R.id.name);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        vpassword = findViewById(R.id.vpassword);
        phone= findViewById(R.id.phone);
        email = findViewById(R.id.email);
        home = findViewById(R.id.Employer);
        addPayment = findViewById(R.id.AddPayment);
        submit = findViewById(R.id.Submit);
        switchToEmployee = findViewById(R.id.emp);
        switchToEmployee.setOnClickListener(this::onClick);
        home.setOnClickListener(this::onClick);

    }


    protected void switchToEmployee(){
        Intent employee = new Intent(this, Employee.class);
        startActivity(employee);
    }
    protected void switchToHome(){
        Intent back = new Intent(this, loginPage.class);
        startActivity(back);
    }

    public void onClick(View v) {
        switch (v.getId()){

            case (R.id.emp):
                switchToEmployee();
                break;

            case (R.id.employer):
                switchToHome();
                break;
        }
    }
}
