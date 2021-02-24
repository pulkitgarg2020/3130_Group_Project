package com.example.a3130_group_6;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class loginPage extends AppCompatActivity {
    public static String[] validEmployer = new String[2];

    protected EditText userNameEt;
    protected EditText passwordEt;
    protected Button regBt;
    protected Button loginBt;
    protected TextView loginStatus;
    private Employee employee;
    private Employer employer;
    DatabaseReference employerRef = null;
    DatabaseReference employeeRef = null;
    private List<String> employee_userName_list = new ArrayList<>();//List to store userName getting from db for Employee object
    private List<String> employee_password_list = new ArrayList<>();//List to store password getting from db for Employee object
    private List<String> employer_userName_list = new ArrayList<>();//List to store password getting from db for Employee object
    private List<String> employer_password_list = new ArrayList<>();//List to store password getting from db for Employee object

    @Override
    protected void onCreate(Bundle savedInstanceState){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button regBt = (Button) findViewById(R.id.registerBt);
        loginBt = findViewById(R.id.loginBt_employee);
    }

    protected boolean isUserNameEmpty(String userNameInput){
        return userNameInput.equals("");
    }

    protected boolean isInfoMatch(String userNameInput, String passwordInput, String userName, String password){
        return userNameInput.equals(userName) && passwordInput.equals(password);
    }

    protected boolean isPasswordEmpty(String passwordInput){
        return passwordInput.equals("");
    }

    protected String getUserName() {
        EditText userName = findViewById(R.id.userNameEt);
        return userName.getText().toString().trim();
    }

    protected String getPassword(){
        EditText password = findViewById(R.id.passwordEt);
        return password.getText().toString().trim();
    }
    protected boolean isPasswordCorrect_employer(){
        for(int i = 0; i<employer_userName_list.size(); i++){
            if(employer_userName_list.get(i).equals(getUserName()) && employer_password_list.get(i).equals(getPassword())){//When can not find matched userInfo from database.
                return true;
            }
        }
        return false;
    }

    protected boolean isPasswordCorrect_employee(){
        for(int i = 0; i<employee_userName_list.size(); i++){
            if(employee_userName_list.get(i).equals(getUserName()) && employee_password_list.get(i).equals(getPassword())){//When can not find matched userInfo from database.
                return true;
            }
        }
        return false;
    }

    protected void switchToReg(){
        Intent intent = new Intent(loginPage.this, registrationHome.class);
        startActivity(intent);
    }


    public boolean isEmployee(){
        return true;
    }

    public void onClick(View v) {
        String statusMsg = "";
        loginStatus = (TextView) findViewById(R.id.loginStatus);
        employerRef= FirebaseDatabase.getInstance().getReferenceFromUrl("https://group-6-a830d-default-rtdb.firebaseio.com/Employer");
            if (R.id.loginBt_employer==v.getId()){
                if (!isUserNameEmpty(getUserName()) && !isPasswordEmpty(getPassword())){//When password or userName is not empty.
                    dbReadEmployer(employerRef);//Get data from database
                    if( isPasswordCorrect_employer() ){//When password or userName is not empty and user's info matched
                        Intent intent = new Intent(loginPage.this, EmployerHomepage.class);//Switch to new intent.
                        startActivity(intent);
                        validEmployer[0] = getUserName();
                    }
                    else {
                        statusMsg = getResources().getString(R.string.INCORRECT_LOGIN_INFO);//Give a reminder message when user's info not matched.
                    }
                }
                else if (isUserNameEmpty(getUserName())){
                    statusMsg = getResources().getString(R.string.EMPTY_USER_NAME);//Show empty message userName is empty
                }
                else if (isPasswordEmpty(getPassword())){//Show empty message password is empty
                    statusMsg = getResources().getString(R.string.EMPTY_PASSWORD);

                }
                loginStatus.setText(statusMsg);
            }

            else if(R.id.loginBt_employee == v.getId()){
                if (isUserNameEmpty(getUserName())){
                    statusMsg = getResources().getString(R.string.EMPTY_USER_NAME);//Show empty message userName is empty
                }
                else if (isPasswordEmpty(getPassword())){//Show empty message password is empty
                    statusMsg = getResources().getString(R.string.EMPTY_PASSWORD);

                }
                else if(!isUserNameEmpty(getUserName()) && !isPasswordEmpty(getPassword())){//When password or userName is not empty.
                    employeeRef= FirebaseDatabase.getInstance().getReferenceFromUrl("https://group-6-a830d-default-rtdb.firebaseio.com/Employee");
                    dbReadEmployee(employeeRef);//Get data from database
                    if( isPasswordCorrect_employee() ){//When password or userName is not empty and user's info matched
                        Intent intent = new Intent(loginPage.this, registrationHome.class);//Switch to new intent.
                        startActivity(intent);
                    }
                    else {
                        statusMsg = getResources().getString(R.string.INCORRECT_LOGIN_INFO);//Give a reminder message when user's info not matched.
                    }
                }
                loginStatus.setText(statusMsg);
            }
            else if (R.id.registerBt == v.getId()){
                switchToReg();
            }

        }


    //Read data from dataBase and make employers' userName and password into ArrayList.
    public void dbReadEmployer(DatabaseReference db){

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Iterator<DataSnapshot> employeeItr = dataSnapshot.getChildren().iterator();
                Iterator<DataSnapshot> employerItr = dataSnapshot.getChildren().iterator();
                //Read data from data base.

                while (employerItr.hasNext()) {
                    Employer employer = employerItr.next().getValue(Employer.class);
                    String employerUserName = employer.getUserName();
                    String employerPassword = employer.getPassword();
                    employer_userName_list.add(employerUserName);
                    employer_password_list.add(employerPassword);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });


    }

    public void dbReadEmployee(DatabaseReference db){

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Iterator<DataSnapshot> employeeItr = dataSnapshot.getChildren().iterator();
                //Read data from data base.

                while (employeeItr.hasNext()) {
                    Employee employee = employeeItr.next().getValue(Employee.class);
                    String employeeUserName = employee.getUserName();
                    String employeePassword = employee.getPassword();
                    employee_userName_list.add(employeeUserName);
                    employee_password_list.add(employeePassword);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });


    }



}