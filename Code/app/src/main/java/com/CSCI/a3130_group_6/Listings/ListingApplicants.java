package com.CSCI.a3130_group_6.Listings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.CSCI.a3130_group_6.EmployeePackage.EmployeeView;
import com.CSCI.a3130_group_6.EmployerPackage.EmployerHomepage;
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

public class ListingApplicants extends AppCompatActivity {

    TextView applicantStatus;
    ListView applicantList;
    String key, employer, title, date, pay, status, description, urgency, listingLocation, employeeName;
    DatabaseReference listingRef;
    FirebaseDatabase db;
    Iterator<DataSnapshot> applicantItr;
    ArrayList<String> applicantUserId;
    ArrayList<String> acceptedUsers;
    EmployerNavBarRouting route;
    TabLayout tab;
    // ToDo: Get the names of users who applied to the listing in this arraylist to show in the Listing of applicants - Bryson
    ArrayList<String> applicantNames;
    ArrayList<String> applicantMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_applicants);
        // retrieve extras
        Intent intent = getIntent();
        key = intent.getStringExtra("key");
        employer = intent.getStringExtra("employer");
        title = intent.getStringExtra("title");
        date = intent.getStringExtra("date");
        pay = intent.getStringExtra("pay");
        status = intent.getStringExtra("status");
        description = intent.getStringExtra("description");
        urgency = intent.getStringExtra("urgency");

        applicantUserId = new ArrayList<>();
        acceptedUsers = new ArrayList<>();
        applicantMessages = new ArrayList<>();
        applicantNames = new ArrayList<>();

        applicantStatus = findViewById(R.id.noApplicantsMessage);
        applicantList = findViewById(R.id.applicantList);

        // db stuff
        db = FirebaseDatabase.getInstance();
        listingLocation = "https://group-6-a830d-default-rtdb.firebaseio.com/Employer/" + employer + "/Listing/" + key + "/Applicants/";
        listingRef = db.getReferenceFromUrl(listingLocation);
        // access specific listing to retrieve applicants - if they exist
        checkForApplicants(listingRef);
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

    public void checkForApplicants(DatabaseReference db){
        // connect to db, check listing at extras
        //AT 1
        // if no exist - display error label
        final DataSnapshot[] lHold = new DataSnapshot[1];
        final DataSnapshot[] applicant = new DataSnapshot[1];
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> dataItr = dataSnapshot.getChildren().iterator();
                //Read data from data base.
                if(dataItr.hasNext()){
                    while(dataItr.hasNext()){
                        lHold[0] = dataItr.next();
                        // if employers listing has applicants
                        if(lHold[0].getKey().equals("Applied")){
                            applicantItr = lHold[0].getChildren().iterator();
                            // multiple applicants
                            while(applicantItr.hasNext()){
                                applicant[0] = applicantItr.next();
                                applicantUserId.add(applicant[0].getKey());
                                applicantNames.add(applicant[0].getKey());
                                // get messages  of applied applicants
                                applicantMessages.add(applicant[0].getValue().toString());
                            }
                            updateApplicants();
                        }
                        else if(lHold[0].getKey().equals("Accepted")){
                            applicantItr = lHold[0].getChildren().iterator();
                            // multiple applicants
                            while(applicantItr.hasNext()){
                                applicant[0] = applicantItr.next();
                                acceptedUsers.add(applicant[0].getKey());
                            }
                            updateApplicants();
                        }
                        // no applicants
                        else if (!lHold[0].getKey().equals("Paid")){
                            applicantStatus.setVisibility(View.VISIBLE);
                        }
                    }
                }else{
                    applicantStatus.setVisibility(View.VISIBLE);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    /**
     * Function: This method updates converts ArrayList of applicants into a String array to be understood by the List View
     * Parameters: none
     * Returns: void
     *
     */
    public void updateApplicants(){
        // display applicants in scrolling list view
        //AT 3
        if(applicantUserId.size()>0){
            String[] applicantsString = new String[applicantUserId.size()];
            for(int i=0; i<applicantsString.length; i++){
//                applicantsString[i] = applicantNames.get(i) + "\tStatus:" + applicantMessages.get(i);
                applicantsString[i] = "Applicant " + applicantUserId.get(i);
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, applicantsString);
            applicantList.setAdapter(adapter);
            applicantList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // on click route to employee details page
                    // send applicant name as extra -> use it to filter db
                    // TODO AT-5
                    employeeName = applicantUserId.get(position);
                    sendToEmployeeDetails(view);
                }
            });
        }
    }

    public void sendToEmployeeDetails(View view){
        boolean payOption = false;
        Intent switchIntent;
        for(int i=0; i<acceptedUsers.size(); i++){
            if(employeeName.equals(acceptedUsers.get(i))){
                payOption = true;
                break;
            }
        }
        if(payOption){
            switchIntent = new Intent(this, EmployeeView.class);
            switchIntent.putExtra("amount", pay);
            switchIntent.putExtra("key", key);
            switchIntent.putExtra("employerName", employer);
            switchIntent.putExtra("employeeName", employeeName);
            startActivity(switchIntent);
        }else{
            switchIntent = new Intent(this, ShowApplication.class);
            switchIntent.putExtra("name", employeeName);
        }

        startActivity(switchIntent);
    }
    public void homepageSwitch(View view) {
        Intent switchIntent = new Intent(this, EmployerHomepage.class);
        startActivity(switchIntent);
    }
}
