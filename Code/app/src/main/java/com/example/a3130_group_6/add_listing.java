package com.example.a3130_group_6;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.Iterator;

public class add_listing extends AppCompatActivity implements View.OnClickListener {

    FirebaseDatabase database = null;
    Listing list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_listing);

        ImageButton imageButton = findViewById(R.id.imageButton);
        imageButton.setOnClickListener(this);

        Button submitButton = findViewById(R.id.submitTask);
        submitButton.setOnClickListener(this);
    }

    protected boolean isEmptyTaskTitle(String task) {
        return task.isEmpty();
    }

    protected boolean isEmptyTaskDescription(String description) {
        return description.isEmpty();
    }

    protected boolean isEmptyUrgency(String urgency) {
        return urgency.isEmpty();
    }

    protected boolean checkUrgencyRange(String urgency) {
        try {
            int num = Integer.parseInt(urgency);
            for (int i = 1; i < 6; i++){
                if (num == i) {
                    return true;
                }
            }
        } catch (Exception e) {
            System.out.println("Urgency not in range");
        }
        return false;
    }

    protected boolean isEmptyDate(String date) {
        return date.isEmpty();
    }

    protected boolean isEmptyPay(String pay) {
        return pay.isEmpty();
    }

    protected void setStatusMessage(String message) {
        TextView statusLabel = findViewById(R.id.statusLabel);
        statusLabel.setText(message);
    }

    @Override
    public void onClick(View view) {
        EditText taskTitle = findViewById(R.id.inputTaskTitle);
        EditText taskDescription = findViewById(R.id.inputTaskDescription);
        EditText urgency = findViewById(R.id.inputUrgency);
        EditText date = findViewById(R.id.enterDate);
        EditText pay = findViewById(R.id.inputPay);

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
                } else {
                    DatabaseReference listing = FirebaseDatabase.getInstance().getReferenceFromUrl("https://group-6-a830d-default-rtdb.firebaseio.com/Employer");
                    list = new Listing(taskTitle.getText().toString(), taskDescription.getText().toString(), urgency.getText().toString(), date.getText().toString(), pay.getText().toString());
                    System.out.println(loginPage.validEmployer[0]);

                    listing.child(String.valueOf(loginPage.validEmployer[0])).child("Listing").push().setValue(list);
                }
                break;
            case R.id.imageButton:
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
        }
    }

//    public void dbRead(DatabaseReference db) {
//        db.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                Iterator<DataSnapshot> employerItr = dataSnapshot.getChildren().iterator();
//                //Read data from data base.
//
//                // details of the current user
//                String employerUserName = loginPage.validEmployer[0];
//                String employerPassword = loginPage.validEmployer[1];
//
//                while (employerItr.hasNext()) {
//                    Employer employer = employerItr.next().getValue(Employer.class);
//                    if (employer.getUserName() == employerUserName &&
//                    employer.getPassword() == employerPassword) {
//                        db.child(String.valueOf(employer)).child("Listing").setValue(list);
//                    }
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//
//            }
//        });
//    }

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