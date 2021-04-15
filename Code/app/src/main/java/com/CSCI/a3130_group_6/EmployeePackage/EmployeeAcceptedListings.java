package com.CSCI.a3130_group_6.EmployeePackage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.CSCI.a3130_group_6.EmployerPackage.Employer;
import com.CSCI.a3130_group_6.HelperClases.EmployeeNavBarRouting;
import com.CSCI.a3130_group_6.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import static com.CSCI.a3130_group_6.Registration.LoginPage.validEmployee;

public class EmployeeAcceptedListings extends AppCompatActivity {

    DatabaseReference employerRef = null;
    private ArrayList<Employer> employerList = new ArrayList<>();
    Set<Employer> employerSet = new LinkedHashSet<>();
    ArrayAdapter<String> adapter;
    ListView employerRatingList;
    String currEmployer;
    EmployeeNavBarRouting route;
    TabLayout tab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_accepted_listings);
        employerRatingList = findViewById(R.id.employerList);
        employerRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://group-6-a830d-default-rtdb.firebaseio.com/Employer");
        dbReadEmployer(employerRef);
    }

    /**
     * Function: Read data from employer database reference and add the employer object to a list if
     *           the employee is working under that employer
     * Parameters: DatabaseReference db
     * Return: void
     */
    public void dbReadEmployer(DatabaseReference db){

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // iterable for employers
                Iterator<DataSnapshot> employerItr = dataSnapshot.getChildren().iterator();

                // running loop over employers
                while (employerItr.hasNext()) {
                    DataSnapshot employerSnap = employerItr.next();
                    Employer employer = employerSnap.getValue(Employer.class);

                    // Iterable for listings under each employer
                    Iterator<DataSnapshot> listsItr = employerSnap.child("Listing").getChildren().iterator();

                    // running loop over listings
                    while (listsItr.hasNext()) {

                        // Iterable for applicants who are accepted under each listing
                        Iterator<DataSnapshot> acceptedEmp = listsItr.next().child("Applicants").
                                child("Accepted").getChildren().iterator();

                        // running loop over all the accepted employees
                        while (acceptedEmp.hasNext()) {

                            // get key of the accepted employer
                            String userName = acceptedEmp.next().getKey();

                            // compare key with the current employee who is logged in
                            if (userName.equals(validEmployee[0])){
                                employerSet.add(employer);
                                break;
                            }
                        }
                    }
                }
                setValueForEmployerList(employerSet);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    /**
     * Function: Method to set the value for the Set and add all its elements to an arrayList
     * Parameters: Set<Employer> employerSet
     * Returns: void
     */
    private void setValueForEmployerList(Set<Employer> employerSet) {
        this.employerSet = employerSet;
        for (Employer e: employerSet){
            this.employerList.add(e);
        }
        setRatingList(employerList);
    }

    private void setRatingList(ArrayList<Employer> employerList) {
        ArrayList<String> employerUserNameList = new ArrayList<>();
        for(int i = 0; i < employerList.size(); i++){
            employerUserNameList.add(employerList.get(i).getUserName());
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                employerUserNameList);
        employerRatingList.setAdapter(adapter);
        clickableListView(employerRatingList, employerUserNameList);
    }

    private void clickableListView(ListView employerRatingList, ArrayList<String> employerUserNameList){
        employerRatingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currEmployer = employerUserNameList.get(position);
                giveRating();
            }
        });
    }

    private void giveRating(){
        Bundle bundle = new Bundle();
        bundle.putString("userName", currEmployer);
        Intent intent = new Intent(this, EmployeeRatingEmployer.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}