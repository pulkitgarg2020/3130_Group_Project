package com.CSCI.a3130_group_6.EmployeePackage;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.CSCI.a3130_group_6.HelperClases.EmployeeNavBarRouting;
import com.CSCI.a3130_group_6.HelperClases.SortHelper;
import com.CSCI.a3130_group_6.HelperClases.UserLocation;
import com.CSCI.a3130_group_6.Listings.Listing;
import com.CSCI.a3130_group_6.Listings.ListingDetails;
import com.CSCI.a3130_group_6.Listings.ListingHistory;
import com.CSCI.a3130_group_6.Listings.ObjectCreatorUserLocationImplementation;
import com.CSCI.a3130_group_6.Listings.ObjectCreatorUserLocationSingleton;
import com.CSCI.a3130_group_6.R;
import com.CSCI.a3130_group_6.Registration.LoginPage;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static com.CSCI.a3130_group_6.Registration.LoginPage.validEmployee;

public class EmployeeHomepage extends AppCompatActivity implements View.OnClickListener {
    DatabaseReference employerRef;
    FirebaseDatabase db;
    DataSnapshot listingData;
    ListView taskList;
    Iterator<DataSnapshot> listingItr;
    ArrayList<Listing> listings;
    ArrayList<String> keys;
    ArrayList<String> employers;
    EditText search;
    ArrayAdapter<String> adapter;
    String[] listingsString;
    String [] details;
    List<String> employerName;
    Button logout;

    Button employeeProfileButton, sortButton, acceptedListingButton;
    private EmployeeProfile employeeProfile;
    ArrayList<Integer> sortPositions = new ArrayList<>();

    private UserLocation user;
    ObjectCreatorUserLocationSingleton userLocationObjectCreator;

    private HashMap<String, Listing> keyToListing = new HashMap<>();
    private HashMap<Listing, String> listingToEmployer = new HashMap<>();
    List<Listing> locationListing = new ArrayList<>();
    DatabaseReference employeeRef;

    SortHelper sort;
    ObjectCreatorSortHelperSingleton sortHelperObjectCreator;

    TextView walletView;
    String wallet;
    EmployeeNavBarRouting route;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_homepage);

        userLocationObjectCreator = new ObjectCreatorUserLocationImplementation();
        sortHelperObjectCreator = new ObjectCreatorSortHelperImplementation();

        taskList = (findViewById(R.id.TaskList));
        listings = new ArrayList<>();
        keys = new ArrayList<>();
        employers = new ArrayList<>();
        employerName = new ArrayList<>();
        db = FirebaseDatabase.getInstance();
        search = findViewById(R.id.newSearchBar);
        TextView searchStatus = findViewById(R.id.searchStatus);
        db = FirebaseDatabase.getInstance();
        employeeRef = db.getReferenceFromUrl("https://group-6-a830d-default-rtdb.firebaseio.com/Employee");
        employerRef = db.getReferenceFromUrl("https://group-6-a830d-default-rtdb.firebaseio.com/Employer");
        employeeProfile = new EmployeeProfile();


        sortButton = findViewById(R.id.sortButton);
        sortButton.setOnClickListener(this);
        walletView = findViewById(R.id.walletView);

        acceptedListingButton = findViewById(R.id.acceptListingsButton);
        acceptedListingButton.setOnClickListener(this);

        user= userLocationObjectCreator.getUserLocation();
        sort = sortHelperObjectCreator.getSortHelper();
        logout = findViewById(R.id.logout);
        logout.setOnClickListener(this);

        dbReadEmployees(employerRef, listings);
        this.showDropDownMenu();


        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                EmployeeHomepage.this.adapter.getFilter().filter(s);

                adapter.getFilter().filter(s, new Filter.FilterListener() {
                    @Override
                    public void onFilterComplete(int count) {
                        if (count == 0){
                            searchStatus.setText("No tasks available based on search");
                        } else {
                            searchStatus.setText("");
                        }
                    }
                });
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        employerRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                System.out.println(snapshot);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) { }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

////        tab =findViewById(R.id.tabs);
//        route = new EmployeeNavBarRouting(getApplicationContext());
////        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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

    private Intent applicationIntent(){
        // ToDo: Route this to a rating page
        Intent intentToApplication = new Intent(this, ListingHistory.class);
        return intentToApplication;
    }

    /**
     * Function: This method converts all listings into one string array that can be understood by my list view
     * Parameters: none
     * Returns: void
     *
     */
    public void setTaskList(ArrayList<Listing> list){
        ArrayList<String> listingsString = new ArrayList<>();
        ArrayList<String> keysFinal = new ArrayList<>();
        ArrayList<String> employersFinal = new ArrayList<>();
        for(int i=0; i<list.size(); i++){
            //ToDo: check if a task is open or not - do not show the closed tasks - Bryson
            if (list.get(i).getStatus().equals("OPEN")) {
                listingsString.add(list.get(i).getTaskTitle());
                keysFinal.add(keys.get(i));
                employersFinal.add(employers.get(i));
            }
        }
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listingsString);


        String[] stringArray = new String[listingsString.size()];
        stringArray = listingsString.toArray(stringArray);

        taskList.setAdapter(adapter);
        taskList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Listing temp = new Listing();
                String key = "", employerName="";
                for (int i = 0; i < listings.size(); i++) {
                    if (listingsString.get(position).equals(listings.get(i).getTaskTitle())) {
                        temp = listings.get(i);
                        key = keysFinal.get(i);
                        employerName = employersFinal.get(i);
                    }
                }
                UserLocation location = temp.getLocation();
                details = new String[10];
                details[0] = temp.getTaskTitle();
                details[1] = temp.getTaskDescription();
                details[2] = temp.getUrgency();
                details[3] = temp.getDate();
                details[4] = temp.getPay();
                details[5] = temp.getStatus();
                details[6] = key;
                details[7] = employerName;
                details[8] = location.getLongitude().toString();
                details[9] = location.getLatitude().toString();
                editListing(view);
            }
        });
    }

    /**
     * Function: This method reads the database and retrieves an ArrayList of Listings
     * Parameters: DatabaseReference - db, ArrayList<Listing> - listings
     * Returns: void
     *
     */
    public void dbReadEmployees(DatabaseReference db, ArrayList<Listing> listings){
        final DataSnapshot[] employer = new DataSnapshot[1];
        final DataSnapshot[] listing = new DataSnapshot[1];
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> employerItr = dataSnapshot.getChildren().iterator();
                //Read data from data base.
                while (employerItr.hasNext()) {
                    employer[0] = employerItr.next();
                    if(employer[0].hasChild("Listing")){
                        // if employer location has Listings, add to the list
                        listingData = employer[0].child("Listing");
                        listingItr = listingData.getChildren().iterator();
                        while (listingItr.hasNext()) {
                            listing[0] = listingItr.next();
                            // add key + employer + listing to separate lists
                            keys.add(listing[0].getKey());
                            employers.add(employer[0].getKey());
                            listings.add(listing[0].getValue(Listing.class));
                        }
                    }
                }
                dbReadEmployeeLocation(employeeRef);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    /**
     *  Method to show the drop down menu
     */
    public void showDropDownMenu() {
        Spinner sortSpinner = (Spinner) findViewById(R.id.sortSpinner);
        List<String> dropDownList = new ArrayList<String>();
        dropDownList.add("sort by urgency");
        dropDownList.add("sort by date");
        dropDownList.add("sort by location");
        @SuppressLint("ResourceType") ArrayAdapter<String> itemListAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, dropDownList);
        itemListAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        sortSpinner.setAdapter(itemListAdapter);
    }

    /**
     * Function: This method sorts the listing by date
     * Parameters:
     * Return: void
     */
    private void sortByDate(){
        locationListing = sort.sortDatesDescending(locationListing);
        setTaskList(sortAllListsByIndices());
    }

    /**
     * Function: This method sorts the listing by urgency
     * Parameters:
     * Return: void
     */
    private void sortByUrgency(){
        Collections.sort(locationListing, new Comparator<Listing>() {
            @Override
            public int compare(Listing l1, Listing l2) {
                return l2.getUrgency().compareTo(l1.getUrgency());
            }
        });
        setTaskList(sortAllListsByIndices());
    }

    public void editListing(View view) {
        Bundle bundle = new Bundle();
        bundle.putStringArray("details", details);
        Intent intent = new Intent(this, ListingDetails.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * Function: This method sorts the listing by location
     * Parameters: UserLocation
     * Return: void
     */
    private void sortByLocation(UserLocation user) {
        HashMap<Listing, Double> taskDistance = new HashMap<Listing, Double>();
        for (int i = 0; i < listings.size(); i++) {
            double latitude = listings.get(i).getLocation().getLatitude();
            double longitude = listings.get(i).getLocation().getLongitude();
            double diff = user.calculateDistanceInKilometer(latitude, longitude);

            if (diff < Double.parseDouble(user.getRadius())){
                // adding listing which is in the user radius to the hashmap
                taskDistance.put(listings.get(i), diff);
            }
        }

        // sorting the hashmap based on values
        taskDistance = sort.sortByValue(taskDistance);

        // adding all the keys to an arraylist
        locationListing.clear();
        for ( Listing listKey : taskDistance.keySet() ) {
            locationListing.add(listKey);
        }

        setTaskList(sortAllListsByIndices());
    }


    private ArrayList<Listing> sortAllListsByIndices() {
        // getting the indexes for sorted list
        sortPositions = sort.getSortedPositions(listings, locationListing);
        if(sortPositions.size()>=1) {
            // sorting keys and employers ArrayList based on indices in sorted listing
            keys = new ArrayList<>(sort.sortArrayListByPosition(keys, sortPositions));
            employers = new ArrayList<>(sort.sortArrayListByPosition(employers, sortPositions));
        }
        // updating the original listings ArrayList
        listings = new ArrayList<>(locationListing);

        return listings;
    }
    /**
     * Function: Method to read the employee's location from the database
     * Parameters: DatabaseReferences
     * Return: void
     */
    public void dbReadEmployeeLocation(DatabaseReference db1){

        db1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> employeeItr = dataSnapshot.getChildren().iterator();
                //Read data from data base.
                while (employeeItr.hasNext()) {
                    //assume there will always be at least one employer
                    Employee employee = employeeItr.next().getValue(Employee.class);
                    //need to check against correct value to retrieve the correct location
                    if (employee.getUserName().equals(validEmployee[0])){
                        if(dataSnapshot.child(validEmployee[0]).child("wallet").getValue()!=null){
                            wallet = (String) dataSnapshot.child(validEmployee[0]).child("wallet").getValue();
                            walletView.setText("Wallet: $" + wallet);
                        }else{
                            walletView.setText("Wallet: $0");
                        }
                        //user = new UserLocation();
                        user = dataSnapshot.child(validEmployee[0]).child("Location").getValue(UserLocation.class);
                        sortByLocation(user);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }

    /**
     * Function: This method switches intent to the employee homepage
     * Parameters: View view
     * Returns: void
     *
     */
    public void homepageSwitch(View view) {
        Intent switchIntent = new Intent(this, EmployeeHomepage.class);
        startActivity(switchIntent);
    }

    /**
     * Created button just for testing/viewing purposes. Can/will delete after integration of navigation bar
     */
    public void employeeProfileSwitch() {
        Intent switchIntent = new Intent(this, EmployeeProfile.class);
        startActivity(switchIntent);
    }

    /**
     * Created button just for testing/viewing purposes. Can/will delete after integration of navigation bar
     */
    public void employeeAppliedListings() {
        Intent switchIntent = new Intent(this, EmployeeAcceptedListings.class);
        startActivity(switchIntent);
    }

    /**
     * Method to get the text from the dropdown menu for sort
     * @return
     * The following method has been used from Assignment 4
     */
    protected String getSelectedItem() {
        Spinner itemList = (Spinner) findViewById(R.id.sortSpinner);
        return itemList.getSelectedItem().toString();
    }

    public void LogoutSwitch(Context context) {
        Toast.makeText(context, "Logging out", Toast.LENGTH_SHORT).show();
        ListingHistory.employerRef=null;
        Intent switchIntent = new Intent(context, LoginPage.class);
        switchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(switchIntent);
    }


    public void onClick(View v) {
        if(v.getId() == R.id.acceptListingsButton){
            employeeAppliedListings();
        }
        else if (v.getId() == R.id.logout) {
            LogoutSwitch(this);
        }
        else if (v.getId() == R.id.sortButton) {
            String selectedItem = getSelectedItem();
            if (selectedItem.equals("sort by urgency")) {
                sortByUrgency();
            } else if (selectedItem.equals("sort by date")) {
                sortByDate();
            } else if (selectedItem.equals("sort by location")) {
                sortByLocation(user);
            }
        }
    }
}
