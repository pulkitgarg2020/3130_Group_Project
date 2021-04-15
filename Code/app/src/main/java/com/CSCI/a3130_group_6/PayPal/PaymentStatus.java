package com.CSCI.a3130_group_6.PayPal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.CSCI.a3130_group_6.EmployeePackage.EmployeeHomepage;
import com.CSCI.a3130_group_6.HelperClases.PaymentModel;
import com.CSCI.a3130_group_6.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class PaymentStatus extends AppCompatActivity implements View.OnClickListener {

    TextView txtId, txtAmount, txtStatus;
    EditText inputRating;
    String employeeName, listingKey, employerName, amount, wallet;
    PaymentModel responseData;
    DatabaseReference employeeRef;
    double prevRating = 0;
    Button homeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_status);
        homeButton = findViewById(R.id.button);
        homeButton.setOnClickListener(this);


        txtId = findViewById(R.id.txtId);
        txtAmount = findViewById(R.id.txtAmount);
        txtStatus = findViewById(R.id.txtStatus);
        inputRating = findViewById(R.id.inputRating);

        Intent intent = getIntent();
        employeeName = intent.getStringExtra("employeeName");
        amount = intent.getStringExtra("Amount");
        listingKey = intent.getStringExtra("key");
        employerName = intent.getStringExtra("employerName");
        wallet = intent.getStringExtra("wallet");

        GsonBuilder builder = new GsonBuilder();
        Gson mGson = builder.create();
        responseData = mGson.fromJson(intent.getStringExtra("PaymentDetails"), PaymentModel.class);

        Button submitRating = findViewById(R.id.submitRating);
        submitRating.setOnClickListener(this);

       /* try {
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("PaymentDetails"));
            showDetails(jsonObject.getJSONObject("response"),intent.getStringExtra("PaymentAmount"));
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        showDetails(amount);
        updateWallet();
    }

    private void showDetails(String paymentAmount) {
        txtId.setText("Transaction ID -- "+responseData.getResponse().getId());
        txtStatus.setText("Status -- "+responseData.getResponse().getState());
        txtAmount.setText("Amount -- $" + paymentAmount);

    }

    private void updateWallet(){
        DatabaseReference listingRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://group-6-a830d-default-rtdb.firebaseio.com/Employer/");
        listingRef.child(employerName).child("Listing").child(listingKey).child("Applicants").child("Accepted").child(employeeName).setValue(null);
        listingRef.child(employerName).child("Listing").child(listingKey).child("Applicants").child("Paid").child(employeeName).child("Message").setValue("Payment from a Nigerian Prince");

        // update wallet reference under employeeName
        employeeRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://group-6-a830d-default-rtdb.firebaseio.com/Employee/"+ employeeName + "/");
        if(wallet==null){
            wallet = "0";
        }
        String postPay = wallet;
        if(responseData.getResponse().getState().equals("approved")){
            int amnt = Integer.parseInt(wallet) + Integer.parseInt(amount);
            postPay = String.valueOf(amnt);
        }
        employeeRef.child("wallet").setValue(postPay);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submitRating:
                getPrevRating(employeeRef);
                break;
            case R.id.button:
                homepageSwitch();
        }
    }

    /**
     * Function: This method switches intent to the employee homepage
     * Parameters: View view
     * Returns: void
     *
     */
    public void homepageSwitch() {
        Intent switchIntent = new Intent(this, EmployeeHomepage.class);
        startActivity(switchIntent);
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

    /**
     * Function: Method to check if rating ranges from 1 to 5
     * Parameters: int rating
     * Returns: boolean
     *
     */
    public boolean checkRatingRange(int rating) {
        if(rating>5 || rating<0){
            return false;
        }
        return true;
    }

    /**
     * Function: Method to calculate the average rating and update it under a
     *           "Rating" object in database
     * Parameters: double prevRating
     * Returns: void
     */
    private void updateRatingInDatabase(double prevRating) {
        int currRating = Integer.parseInt(inputRating.getText().toString());
        double rating = 0;
        if (prevRating != 0) {
            rating = (prevRating + currRating)/2.0;
        } else {
            rating = currRating;
        }
        if (checkRatingRange(currRating)){
            employeeRef.child("Rating").setValue(rating);
            createToast("Rating successfully stored in the database.");
        } else {
            createToast("Rating not in range. Please enter a valid value.");
        }
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
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    // update @employeeName wallet based on @amount
}
