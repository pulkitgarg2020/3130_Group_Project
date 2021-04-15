package com.CSCI.a3130_group_6.Registration;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.CSCI.a3130_group_6.EmployerPackage.Employer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Class to validate in realtime if entered username already
 * exists in the database
 *
 * @author  Pulkit Garg
 */
public class CheckExistingUserName {
    ArrayList<String> employeeList = new ArrayList<>();
    ArrayList<String> employerList = new ArrayList<>();
    DatabaseReference employer = null;
    DatabaseReference employee = null;

    /**
     * Function: Constructor for the class
     * Parameters:
     * Returns:
     */
    public CheckExistingUserName() {
        initializeDatabase();
        saveUserNamesInLists();
    }

    /**
     * Function: method to initialize the reference for employer and employee
     * Parameters:
     * Returns: void
     *
     */
    public void initializeDatabase() {
        employer = FirebaseDatabase.getInstance().getReferenceFromUrl("https://group-6-a830d-default-rtdb.firebaseio.com/Employer");
        employee = FirebaseDatabase.getInstance().getReferenceFromUrl("https://group-6-a830d-default-rtdb.firebaseio.com/Employee");
    }

    /**
     * Function: method to save the usernames of employer and employee in lists
     * Parameters:
     * Returns: void
     *
     */
    public void saveUserNamesInLists() {
        employeeList = getUserNameList(employer, employeeList);
        employerList = getUserNameList(employee, employerList);
    }

    /**
     * Function: method to retrieve the list of usernames from the database
     * Parameters: DatabaseReference, ArrayList
     * Returns: Arraylist of usernames
     *
     */
    private ArrayList<String> getUserNameList(DatabaseReference db, ArrayList<String> list){
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> employerItr = dataSnapshot.getChildren().iterator();
                //Read data from data base.

                while (employerItr.hasNext()) {
                    Employer employerFromDB = employerItr.next().getValue(Employer.class);
                    String employerUserName = employerFromDB.getUserName();
                    list.add(employerUserName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return list;
    }

    /**
     * Function: Method to check the length of the username and then show an error if
     *           the length of the username is less than 3 characters
     * Parameters: TextView, String
     * Returns: boolean
     *
     */
    private boolean checkUserNameLength(TextView error, String inputUserName) {
        if (inputUserName.length() < 3){
            error.setText("Username too short");
            return false;
        }
        return true;
    }

    /**
     * Function: method to check if the username exists in any of the lists
     *           from the database and print an error in realtime
     * Parameters: EditText, TextView
     * Returns:
     *
     */
    public void validateUsername(EditText username, TextView error) {
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (checkUserNameLength(error, String.valueOf(s))) {
                    checkUserNameInDatabase(employeeList, employerList, String.valueOf(s), error);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    /**
     * Function: method to check if the username exists in the database
     *           and set the error text in the statusLabel
     * Parameters: ArrayList, ArrayList, String, TextView
     * Returns:
     *
     */
    private void checkUserNameInDatabase(ArrayList<String> employeeList, ArrayList<String> employerList,
                                         String userName, TextView error){
        if(employeeList.contains(userName) || employerList.contains(userName)){
            error.setText("Username already taken. Please enter a different username.");
        }
        else error.setText("Username valid");
    }

    /**
     * Function: method to check if the user changed the username or not
     * Parameters: TextView
     * Returns: boolean
     *
     */
    public boolean checkUserNameError(TextView error) {
        String text = String.valueOf(error.getText());
        String error1 = "Username already taken. Please enter a different username.";
        String error2 = "Username too short";
        return (text.equals(error1) || text.equals(error2));
    }

}
