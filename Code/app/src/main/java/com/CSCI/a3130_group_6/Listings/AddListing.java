package com.CSCI.a3130_group_6.Listings;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.CSCI.a3130_group_6.HelperClases.EmployerNavBarRouting;
import com.CSCI.a3130_group_6.R;
import com.CSCI.a3130_group_6.Registration.LoginPage;
import com.CSCI.a3130_group_6.HelperClases.UserLocation;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;


public class AddListing extends AppCompatActivity implements View.OnClickListener {
    Listing list;
    AddListingMap map;
    EditText taskTitle;
    EditText taskDescription;
    EditText urgency;
    EditText date;
    EditText pay;
    ObjectCreatorListingSingleton objectCreator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_listing);
        objectCreator = new ObjectCreatorListingImplementation();
        map = new AddListingMap();

        if (savedInstanceState != null) {
            taskTitle.setText(savedInstanceState.getString("taskTitle"));
            taskTitle.setText(savedInstanceState.getString("taskDescription"));
            taskTitle.setText(savedInstanceState.getString("urgency"));
            taskTitle.setText(savedInstanceState.getString("date"));
            taskTitle.setText(savedInstanceState.getString("pay"));
        }

        ImageButton imageButton = findViewById(R.id.imageButton);
        imageButton.setOnClickListener(this);

        Button submitButton = findViewById(R.id.submitTask);
        submitButton.setOnClickListener(this);

        Button addLocationBt = findViewById(R.id.add_locationBt);
        addLocationBt.setOnClickListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putString("taskTitle", taskTitle.getText().toString());
        outState.putString("taskDescription", taskDescription.getText().toString());
        outState.putString("urgency", urgency.getText().toString());
        outState.putString("date", date.getText().toString());
        outState.putString("pay", pay.getText().toString());
    }

    public boolean isEmptyTaskTitle(String task) {
        return task.isEmpty();
    }

    public boolean isEmptyTaskDescription(String description) {
        return description.isEmpty();
    }

    public boolean isEmptyUrgency(String urgency) {
        return urgency.isEmpty();
    }

    /**
     * Function: Method to check if urgency ranges from 1 to 5
     * Parameters: String
     * Returns: boolean
     *
     */
    public boolean checkUrgencyRange(String urgency) {
        try {
            int num = Integer.parseInt(urgency);
            for (int i = 1; i < 6; i++){
                if (num == i) {
                    return true;
                }
            }
        } catch (Exception e) {
            System.err.println("Urgency not in range");
        }
        return false;
    }

    public boolean isEmptyDate(String date) {
        return date.isEmpty();
    }

    public boolean isEmptyPay(String pay) {
        return pay.isEmpty();
    }

    public boolean isEmptyLocation(String location){
        return location.isEmpty();
    }

    public void setStatusMessage(String message) {
        TextView statusLabel = findViewById(R.id.statusLabel);
        statusLabel.setText(message);
    }

    public void employeeMapSwitch() {
        Intent EmployeeMapIntent = new Intent(this, AddListingMap.class);
        startActivity(EmployeeMapIntent);
    }

    public boolean checkIfLocationEmpty(UserLocation current){
        return current == null;
    }

    @Override
    public void onClick(View view) {

        EditText status = findViewById(R.id.statusInput);
        taskTitle = findViewById(R.id.titleInput);
        taskDescription = findViewById(R.id.descriptionInput);
        urgency = findViewById(R.id.urgencyInput);
        date = findViewById(R.id.dateInput);
        pay = findViewById(R.id.payInput);
        TextView currentLocation = findViewById(R.id.currentLocationView);
        Button addLocationBt = findViewById(R.id.add_locationBt);

        switch (view.getId()) {
            case R.id.submitTask:
                if (isEmptyTaskTitle(taskTitle.getText().toString())) {
                    setStatusMessage("Error: Empty Task Title");
                }
                else if (isEmptyTaskDescription(taskDescription.getText().toString().trim())) {
                    setStatusMessage("Error: Empty Task Description");
                }
                else if (isEmptyUrgency(urgency.getText().toString().trim())) {
                    setStatusMessage("Error: Please fill in Urgency");
                }
                else if (isEmptyDate(date.getText().toString().trim())) {
                    setStatusMessage("Error: Please fill in Date");
                }
                else if (isEmptyPay(pay.getText().toString().trim())) {
                    setStatusMessage("Error: Please fill in Pay");
                }
                else if (checkIfLocationEmpty(AddListingMap.presentLocation)){
                    setStatusMessage("Error: Please choose a location");
                } else {
                    // creating reference to the Employer object in the database
                    DatabaseReference listing = FirebaseDatabase.getInstance().getReferenceFromUrl("https://group-6-a830d-default-rtdb.firebaseio.com/Employer");

                    // creating a list object with relevant entries which have to be added to the database
                    list = objectCreator.getListingObject();
                    list.setTaskTitle(taskTitle.getText().toString());
                    list.setTaskDescription(taskDescription.getText().toString());
                    list.setUrgency(urgency.getText().toString());
                    list.setDate(date.getText().toString());
                    list.setPay(pay.getText().toString());
                    list.setStatus("OPEN");
                    list.setLocation(AddListingMap.presentLocation);

                    // pushing entries to the database
                    listing.child(String.valueOf(LoginPage.validEmployer[0])).child("Listing").push().setValue(list);
                }
                break;
            case R.id.add_locationBt:
                employeeMapSwitch();
                break;
            case R.id.imageButton:
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
                break;
            default:
        }
    }


    public static final int GET_FROM_GALLERY = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}