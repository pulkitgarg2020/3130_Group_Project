package com.CSCI.a3130_group_6.Listings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.CSCI.a3130_group_6.EmployerPackage.Employer;
import com.CSCI.a3130_group_6.HelperClases.EmployerNavBarRouting;
import com.CSCI.a3130_group_6.HelperClases.ShowApplication;
import com.CSCI.a3130_group_6.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.CSCI.a3130_group_6.Registration.LoginPage.validEmployer;

public class ListingHistory extends AppCompatActivity {
    public static DatabaseReference employerRef = null;
    FirebaseDatabase database;
    String fireRef;
    public static String listingKey;
    TextView NoListing = null;
    List<Listing> listings;
    List<String> keys;
    List<String> employers;
    DataSnapshot employer;
    ListView listView=null;
    String[] listingsString;
    DataSnapshot listingData;
    Iterator<DataSnapshot> listingItr;
    String [] details;
    List<String> employerName;
    Button toggle;
    Boolean toggleFlag;//if true go to applicant false Edit listings
    TabLayout tab;
    EmployerNavBarRouting route;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_history);
        NoListing = findViewById(R.id.noListingMessage);

        listView = findViewById(R.id.employeeList);
        listings = new ArrayList<>();
        keys = new ArrayList<>();
        employers = new ArrayList<>();
        toggleFlag=true;
        employerName = new ArrayList<>();

        database = FirebaseDatabase.getInstance();
        fireRef =  "https://group-6-a830d-default-rtdb.firebaseio.com/Employer";
        employerRef= database.getReferenceFromUrl(fireRef);
        dbReadEmployer(employerRef);
        toggle=findViewById(R.id.toggleBtn);
        toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toggleFlag){
                    NoListing.setText("Switched to edit");
                    toggleFlag=false;
                }
                else{
                    NoListing.setText("Switched to applicant");
                    toggleFlag=true;
                }
            }
        });
//        tab =findViewById(R.id.tabs);
//        TabLayout.Tab activeTab = tab.getTabAt(1);
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
//                       // route.chatSwitch(getApplicationContext());
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

    /**
     * Function: This method updates converts ArrayList of Listings into String array to be understood by the List View
     * Parameters: none
     * Returns: void
     *
     */
    public void updateListing(){
        if(listings.size()>0){
            listingsString = new String[listings.size()];
            for(int i=0; i<listingsString.length; i++){
                // listingsString[i] = "Task: " + listings.get(i).getTaskTitle() + "\tStatus: " + listings.get(i).getStatus();

                listingsString[i] = listings.get(i).getTaskTitle() + "\tStatus:" + listings.get(i).getStatus();
                //editListing.setOnClickListener(this);
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listingsString);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    if(toggleFlag){
                        sendToEdit(position);
                    }
                    else{
                        Listing temp = listings.get(position);
                        details = new String[8];
                        details[0] = temp.getTaskTitle();
                        details[1] = temp.getTaskDescription();
                        details[2] = temp.getUrgency();
                        details[3] = temp.getDate();
                        details[4] = temp.getPay();
                        details[5] = temp.getStatus();
                        details[6] = keys.get(position);
                        details[7] = employerName.get(position);
                        editListing(view);
                    }
                    retrieveListingKey(position, view);
                }
            });
        }else{
            NoListing.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Function: This method fills a list view with data, but Ty's code will implement an edit/listing details page
     * Parameters: Integer - position
     * Returns: void
     *
     */
    public void sendToEdit(int position) {
        Intent switchIntent = new Intent(this, ListingApplicants.class);
        Listing temp = listings.get(position);
        // 6 == num properties of a listing
        // putExtra() listing properties to listing detail page -- currently sends to my listing applicants page
        switchIntent.putExtra("key", keys.get(position));
        switchIntent.putExtra("employer", employers.get(position));
        switchIntent.putExtra("title", temp.getTaskTitle());
        switchIntent.putExtra("date", temp.getDate());
        switchIntent.putExtra("pay", temp.getPay());
        switchIntent.putExtra("status", temp.getStatus());
        switchIntent.putExtra("description", temp.getTaskDescription());
        switchIntent.putExtra("urgency", temp.getUrgency());
        startActivity(switchIntent);
    }

    /**
     * Function: This method reads the database and retrieves all of the employers listings
     * Parameters: DatabaseReference - db
     * Returns: void
     *
     */
    public void dbReadEmployer(DatabaseReference db){
        final DataSnapshot[] listing = new DataSnapshot[1];
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> dataItr = dataSnapshot.getChildren().iterator();
                //Read data from data base.
                while (dataItr.hasNext()) {
                    employer = dataItr.next();
                    if(employer.hasChild("Listing")){
                        // if employer location has Listings
                        if(employer.getValue(Employer.class).getUserName().equals(validEmployer[0])) {
                            fireRef = fireRef + "/" + employer.getValue(Employer.class).getUserName() + "/Listing";
                            NoListing.setVisibility(View.INVISIBLE);
                            employerRef= database.getReferenceFromUrl(fireRef);
                            listingData = employer.child("Listing");
                            listingItr = listingData.getChildren().iterator();
                            while (listingItr.hasNext()) {

                                listing[0] = listingItr.next();
                                employers.add(employer.getKey());
                                keys.add(listing[0].getKey());
                                listing[0].getValue(Listing.class);
                                listings.add(listing[0].getValue(Listing.class));

                                // ToDo: @Ty Your code breaks. Please check for this.
                                //DataSnapshot next = listingItr.next();
                                String listingKey = listing[0].getKey();
                                Listing value = listing[0].getValue(Listing.class);
                                //value.setKey(listingKey);

                                // tODO: @tY Your code breaks. Please check for this.
                                //listings.add(value);

                                employerName.add(validEmployer[0]);
                            }
                        }
                    }
                }
                updateListing();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void retrieveListingKey(int position, View view){
        listingKey = keys.get(position);
        System.out.println("keys: " + listingKey);
    }

    public void editListing(View view){
        Bundle bundle = new Bundle();
        bundle.putStringArray("details",details);
        Intent intent = new Intent(this, EditEmployerListing.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

}
