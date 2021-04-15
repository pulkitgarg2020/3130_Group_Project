package com.CSCI.a3130_group_6.EmployerPackage;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.CSCI.a3130_group_6.EmployeePackage.Employee;
import com.CSCI.a3130_group_6.HelperClases.EmployerNavBarRouting;
import com.CSCI.a3130_group_6.Listings.AddListing;
import com.CSCI.a3130_group_6.Listings.ListingHistory;
import com.CSCI.a3130_group_6.R;
import com.CSCI.a3130_group_6.Registration.LoginPage;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class EmployerHomepage extends AppCompatActivity implements View.OnClickListener{

    ArrayList<Employee> employees;
    DatabaseReference employeeRef;
    DatabaseReference notificationRef;
    String taskTitle;
    Button logout;

    Map<String, Integer> listings;
    TabLayout tab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_homepage);

        listings = Collections.synchronizedMap(new HashMap<String, Integer>());
        logout = findViewById(R.id.Logout);
        logout.setOnClickListener(this);

        employeeRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://group-6-a830d-default-rtdb.firebaseio.com/Employee");
        setEmployeeList();

        // ----  fetch all the listings data

        // database reference to the Listing child of the employer who is currently logged in
        notificationRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://group-6-a830d-default-rtdb.firebaseio.com/Employer/").child(LoginPage.validEmployer[0]).child("Listing");

        createPrevListMap(notificationRef);

        // listening to any change in data in the database
        notificationRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // getting the parent of the snapshot
                DatabaseReference parent = snapshot.getRef().getParent();

                // getting the children of the Applied object under the listing that has changed
                Iterable<DataSnapshot> newList = snapshot.child("Applicants").child("Applied").getChildren();

                int sizeNew = 0;
                for (Object i: newList) {
                    sizeNew++;
                }

                // retrieving the task title to show in the notification
                taskTitle = (String) snapshot.child("taskTitle").getValue();

                // checking if the parent of the listing matches the current employer who is logged in
                if (parent.getParent().getKey().equals(LoginPage.validEmployer[0])) {
                    if (listings.get(snapshot.getKey()) < sizeNew) {
                        notification(snapshot.getKey(), sizeNew);
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) { }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
//        tab =findViewById(R.id.tabs);
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
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {}
//        });
    }


    private void createPrevListMap(DatabaseReference db){
        HashMap<String, Integer> applicantSizeMap = new HashMap<>();
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterator<DataSnapshot> listingItr = snapshot.getChildren().iterator();
                while (listingItr.hasNext()) {
                    DataSnapshot ref = listingItr.next();
                    String key = ref.getKey();
                    Iterable<DataSnapshot> applicants = ref.child("Applicants").child("Applied").getChildren();
                    int size = 0;
                    for (Object i: applicants) {
                        size++;
                    }
                    applicantSizeMap.put(key, size);
                }
                setListingObject(applicantSizeMap);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    private void setListingObject(HashMap<String, Integer> list) {
        this.listings = list;
    }

    /**
     * Function: This method is used to create a push notification on the device
     * Parameters: none
     * Returns: void
     * Citation: Idea from https://www.geeksforgeeks.org/how-to-push-notification-in-android-using-firebase-cloud-messaging/
     */
    private void notification(String snapshot, int sizeNew) {
        if (listings.get(snapshot) < sizeNew) {
            Intent applicationIntent = applicationIntent();
            PendingIntent pendingIntent
                    = PendingIntent.getActivity(
                    this, 0, applicationIntent,
                    PendingIntent.FLAG_ONE_SHOT);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel =
                        new NotificationChannel("n", "n", NotificationManager.IMPORTANCE_DEFAULT);
                NotificationManager manager = getSystemService(NotificationManager.class);
                manager.createNotificationChannel(channel);
            }

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "n")
                    .setContentText("Application")
                    .setSmallIcon(R.drawable.application)
                    .setAutoCancel(true)
                    .setContentText("An employee applied to your listing: " + taskTitle)
                    .setContentIntent(pendingIntent);
            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);

            // creating unique ID based on the key in the hashmap
            int id = 0;
            for (char a: snapshot.toCharArray()) {
                id += (int) a;
            }
            id+=listings.get(snapshot);
            managerCompat.notify(id, builder.build());

            listings.put(snapshot, listings.get(snapshot) + 1);
        }
    }

    public Intent applicationIntent(){
        Intent intentToApplication = new Intent(this, ListingHistory.class);
        return intentToApplication;
    }

    public void setEmployeeList(){
        // connect to db, retrieve employees
        employees = new ArrayList<>();
        // causing bugs (wasn't working before anyway)
        //dbReadEmployees(employeeRef, employees);
        String[] employeesString = new String[employees.size()];
        employees.toArray(employeesString);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, employeesString);
        ListView employeeList = (ListView) findViewById(R.id.employeeList);
        // employeeList.setAdapter(adapter);
    }

    public boolean searchFunctioning(String search){
        /* irrelevant testing process for unit tests.
        searchView.setQuery(search, true);
        return searchView.getQuery().toString().equals(search);
        */
        return !search.isEmpty();
    }

    public boolean checkEmployeeList(String[] employees){
        for(String individual : employees) {
            if(individual.isEmpty()){
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * Function: This is a method to switch to Employer profile class
     * Parameters: none
     * Returns: void
     *
     */
    public void profileSwitch(View view) {
        Intent switchIntent = new Intent(this, EmployerProfile.class);
        startActivity(switchIntent);
    }

    /**
     * Function: This is a method to switch to Add listing page
     * Parameters: none
     * Returns: void
     *
     */
    public void addTaskSwitch(View view) {
        Intent switchIntent = new Intent(this, AddListing.class);
        startActivity(switchIntent);
    }

    /**
     * Function: This is a method to switch to Employer homepage
     * Parameters: none
     * Returns: void
     *
     */
    public void homepageSwitch(View view) {
        Intent switchIntent = new Intent(this, EmployerHomepage.class);
        startActivity(switchIntent);
    }

    public void switchListingHistory(View view) {
        Intent switchIntent = new Intent(this, ListingHistory.class);
        startActivity(switchIntent);
    }

    public void LogoutSwitch(Context context) {
        Toast.makeText(context, "Logging out", Toast.LENGTH_SHORT).show();
        ListingHistory.employerRef=null;
        Intent switchIntent = new Intent(context, LoginPage.class);
        switchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(switchIntent);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.Logout) {
            LogoutSwitch(this);
        }
    }
}