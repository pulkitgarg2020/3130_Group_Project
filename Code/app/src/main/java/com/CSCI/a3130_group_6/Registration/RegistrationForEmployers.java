package com.CSCI.a3130_group_6.Registration;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.PatternsCompat;

import com.CSCI.a3130_group_6.EmployerPackage.Employer;
import com.CSCI.a3130_group_6.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationForEmployers extends AppCompatActivity implements View.OnClickListener{
    EditText name,username,password,vpassword,phone,email, business;
    Button homeBt,submitBt, employeeBt;

    TextView error;
    TextView statusLabel;
    DatabaseReference employerRef = null;
    Employer employers;
    CheckExistingUserName user;

    ObjectCreatorEmployerSingleton objectCreator;

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
        homeBt = findViewById(R.id.Employer);
        submitBt = findViewById(R.id.Submit1);
        employeeBt = findViewById(R.id.emp);
        business = findViewById(R.id.business);
        statusLabel = findViewById(R.id.statusLabel);

        employeeBt.setOnClickListener(this);
        homeBt.setOnClickListener(this);
        submitBt.setOnClickListener(this);
        username.setOnClickListener(this);
        error = findViewById(R.id.error);

        user = new CheckExistingUserName();
        user.validateUsername(username, error);

        objectCreator = new ObjectCreatorEmployerImplementation();
        employers = objectCreator.getEmployer();

    }

    /*
    Switch to login page
     */
    public void switchToHome(){
        Intent back = new Intent(this, LoginPage.class);
        startActivity(back);
    }

    /*
    Changing pages to see employer registration
     */
    public void switchToEmployee(){
        Intent employee = new Intent(this, RegistrationForEmployees.class);
        startActivity(employee);
    }

    public boolean isUserNameEmpty(){
        return getInputUserName().equals("");
    }

    public boolean isNameEmpty(){
        return getName().equals("");
    }

    public boolean isPasswordEmpty(){
        return getInputPassword().equals("");
    }

    public boolean isVerifyPasswordEmpty(){
        return vpassword.getText().toString().trim().equals("");
    }

    public boolean isPhoneEmpty(){ return getPhoneNumber().equals(""); }

    public boolean isValidEmail(String email){
        return PatternsCompat.EMAIL_ADDRESS.matcher(email).matches();
    }

    /*
    Checking registration information
     */
    public boolean validRegistrationInformation() {
        return !isUserNameEmpty() && !isPasswordEmpty() && !isNameEmpty() && !isPhoneEmpty()
                && isValidEmail(getInputEmailAddress());
    }

    /*
    Saving employee information to the database
     */
    public void saveEmployerToDataBase(Object Employers) {
        //save object user to database to Firebase
        employerRef = FirebaseDatabase.getInstance().getReference();
        employerRef.child("Employer").child(String.valueOf(employers.getUserName())).setValue(Employers);
    }

    public String getInputUserName(){
        return username.getText().toString().trim();
    }

    public String getInputPassword(){
        return password.getText().toString().trim();
    }

    public String getInputEmailAddress() {
        return email.getText().toString().trim();
    }

    public String getPhoneNumber() {
        return phone.getText().toString().trim();
    }

    public String getInputVpassword(){
        return vpassword.getText().toString().trim();
    }

    public boolean isPasswordMatched(String password, String vPassword){
        return (password.equals(vPassword));
    }

    public String getBusinessName() {
        return business.getText().toString().trim();
    }

    public String getName() {
        return name.getText().toString().trim();
    }

    // method to create a Toast
    private void createToast(String message){
        Toast toast = Toast.makeText(this, message,Toast.LENGTH_LONG);
        toast.show();
    }

    public void onClick(View v) {
        switch (v.getId()){
            case (R.id.emp):
                switchToEmployee();
                break;
            case (R.id.Employer):
                switchToHome();
                break;
            case (R.id.Submit1):
                if(!validRegistrationInformation()) {
                    createToast("Empty or invalid registration information");
                }
                else if(user.checkUserNameError(error)){
                    createToast("Please change the username");
                }
                else if(!isPasswordMatched(getInputPassword(), getInputVpassword())){//when the password and verification password are not matched
                    statusLabel.setText("password is not matched");
                }
                else {
                    employers.setUserName(getInputUserName());
                    employers.setPassword(getInputPassword());
                    employers.setEmailAddress(getInputEmailAddress());
                    employers.setPhone(getPhoneNumber());
                    employers.setName(getName());
                    employers.setBusinessName(getBusinessName());
                    saveEmployerToDataBase(employers);
                    switchToHome();//Once all registration info correct, switch to loginPage
                    break;
                }
        }
    }
}
