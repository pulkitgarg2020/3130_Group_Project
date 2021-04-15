package com.CSCI.a3130_group_6.Listings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.CSCI.a3130_group_6.EmployeePackage.EmployeeHomepage;
import com.CSCI.a3130_group_6.HelperClases.UserLocation;
import com.CSCI.a3130_group_6.R;
import com.CSCI.a3130_group_6.Registration.RegistrationForEmployees;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;

import static com.CSCI.a3130_group_6.Registration.LoginPage.validEmployee;

public class ListingDetails extends AppCompatActivity {
    DatabaseReference listingRef = null;
    FirebaseDatabase database;

    Button apply;
    String [] listing = null;
    EditText applicationMessage;
    UserLocation exactAddress;
    Activity activity;
    LatLng userCurrentLocation;
    TextView showAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle extras = getIntent().getExtras();
        listing = extras.getStringArray("details");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_details);

        applicationMessage = findViewById(R.id.applicationMessage);

        apply = findViewById(R.id.applyToListing);
        apply.setOnClickListener(this::onClick);

        database = FirebaseDatabase.getInstance();
        listingRef = database.getReferenceFromUrl("https://group-6-a830d-default-rtdb.firebaseio.com/Employer");

        showAddress = findViewById(R.id.showAddress);
        activity = ListingDetails.this;

        userCurrentLocation = new LatLng(Double.parseDouble(listing[9]),
                Double.parseDouble(listing[8]));
        setTextBox();
//        tab =findViewById(R.id.tabs);
//        TabLayout.Tab activeTab = tab.getTabAt(3);
//        activeTab.select();
//        route = new EmployerNavBarRouting(getApplicationContext());
//        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//
//                switch (tab.getText().toString()) {
//                    case "Listing":
//                        route.switchListingHistory(getApplicationContext());
//                        break;
//                    case "Profile":
//                        route.profileSwitch(getApplicationContext());
//                        break;
//                    case "Logout":
//                        route.LogoutSwitch(getApplicationContext());
//                        break;
//                    case "Home":
//                        route.homepageSwitch(getApplicationContext());
//                        break;
//                    case "Chat":
//                        //route.chatSwitch(getApplicationContext());
//                        break;
//                }
//            }
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) { }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {}
//        });
    }

    public void setTitleDisplay(String title){
        TextView textView = findViewById(R.id.titleInput);
        textView.setText(title);
    }
    public void setDescriptionDisplay(String description){
        TextView textView = findViewById(R.id.descriptionInput);
        textView.setText(description);
    }
    public void setUrgencyDisplay(String urgency){
        TextView textView = findViewById(R.id.urgencyInput);
        textView.setText(urgency);
    }
    public void setDateDisplay(String date){
        TextView textView = findViewById(R.id.dateInput);
        textView.setText(date);
    }
    public void setPayDisplay(String pay){
        TextView textView = findViewById(R.id.payInput);
        textView.setText(pay);
    }

    public void setStatusDisplay(String status){
        TextView textView = findViewById(R.id.statusInput);
        textView.setText(status);
    }


    public void setTextBox() {
        setTitleDisplay(listing[0]);
        setDescriptionDisplay(listing[1]);
        setUrgencyDisplay(listing[2]);
        setDateDisplay(listing[3]);
        setPayDisplay(listing[4]);
        setStatusDisplay(listing[5]);
        try {
            getAddressFromLocation(userCurrentLocation);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Function: Method to get exact address using latitude and longitude
     * Parameters: LatLng
     * Returns: void
     *
     */
    // method to get the exact address from latitude and longitude
    private void getAddressFromLocation(LatLng currentLocation) throws IOException {
        exactAddress = new UserLocation(currentLocation, activity);
        exactAddress.createAddress();
        showAddress.setText(exactAddress.getAddress());
    }

    public void homepageSwitch(View view) {
        Intent switchIntent = new Intent(this, EmployeeHomepage.class);
        startActivity(switchIntent);
    }

    /**
     * Function: This method applies an employee to a listing
     * Parameters: none
     * Returns: void
     *
     */
    public void applyToListing(){
        // save current employee under listing in database
        // save object user to database to Firebase
        listingRef.child(listing[7]).child("Listing").child(listing[6]).child("Applicants").child("Applied").child(validEmployee[0]).child("Message").setValue(applicationMessage.getText().toString());

        applicationMessage.setText(null);
    }

    public void onClick(View v) {
        switch ((v.getId())) {
            case R.id.applyToListing:
                applyToListing();
                Toast.makeText(ListingDetails.this, "You have successfully applied to this listing with your new message", Toast.LENGTH_LONG).show();
                break;

        }

    }

}
