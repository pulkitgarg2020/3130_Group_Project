package com.CSCI.a3130_group_6.EmployeePackage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.CSCI.a3130_group_6.EmployeePackage.EmployeeAcceptedListings;
import com.CSCI.a3130_group_6.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EmployeeRatingEmployer extends AppCompatActivity implements View.OnClickListener {
    String empUserName;
    Button submitRating;
    EditText ratingBox;
    double prevRating;
    DatabaseReference employerRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_rating_employer);

        submitRating = findViewById(R.id.submitEmployerRating);
        submitRating.setOnClickListener(this);
        ratingBox = findViewById(R.id.inputEmployerRating);

        // retrieving details of the employer that has been passed from the previous intent
        Intent intent = getIntent();
        empUserName = intent.getStringExtra("userName");
        employerRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://group-6-a830d-default-rtdb.firebaseio.com/Employer/" + empUserName);
    }

    /**
     * Function: Method to check if rating ranges from 1 to 5
     * Parameters: int rating
     * Returns: boolean
     *
     */
    public boolean checkRatingRange(int rating) {
        if(rating>5){
            return false;
        }
        return true;
    }


    /**
     * Function: Method to create a Toast
     * Parameters:
     * Returns: void
     *
     */
    private void createToast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submitEmployerRating:
                getPrevRating(employerRef);
                acceptedListingsPageSwitch();
            default:
        }
    }

    /**
     * Function: Method to calculate the average rating and update it under a
     *           "Rating" object in database
     * Parameters: double prevRating
     * Returns: void
     */
    private void updateRatingInDatabase(double prevRating) {
        int currRating = Integer.parseInt(ratingBox.getText().toString());
        double rating = 0;
        if (prevRating != 0) {
            rating = (prevRating + currRating)/2.0;
        } else {
            rating = currRating;
        }
        if (checkRatingRange(currRating)){
            employerRef.child("Rating").setValue(rating);
            createToast("Rating successfully stored in the database.");
        } else {
            createToast("Rating not in range. Please enter a valid value.");
        }
    }

    /**
     * Function: This is a method to switch to Employer homepage
     * Parameters: none
     * Returns: void
     *
     */
    public void acceptedListingsPageSwitch() {
        Intent switchIntent = new Intent(this, EmployeeAcceptedListings.class);
        startActivity(switchIntent);
    }


    /**
     * Function: Method to retrieve the previous rating of an employer
     * Parameters: DatabaseReference
     * Returns: void
     */
    private void getPrevRating(DatabaseReference db) {
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    prevRating = snapshot.child("Rating").getValue(Double.class);
                } catch (NullPointerException e) {
                    prevRating = 0;
                }
                updateRatingInDatabase(prevRating);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}