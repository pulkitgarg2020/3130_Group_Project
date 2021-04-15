package com.CSCI.a3130_group_6.EmployeePackage;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.CSCI.a3130_group_6.HelperClases.EmployeeNavBarRouting;
import com.CSCI.a3130_group_6.Listings.ObjectCreatorListingSingleton;
import com.CSCI.a3130_group_6.R;
import com.CSCI.a3130_group_6.HelperClases.UserLocation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.CSCI.a3130_group_6.Registration.LoginPage.validEmployee;

public class EmployeeProfile extends AppCompatActivity {

    DatabaseReference employeeRef = null;
    FirebaseStorage storage;
    FirebaseDatabase database;
    UserLocation user;
    ObjectCreatorEmployeeSingleton objectCreator;
    String userName = validEmployee[0];
    Employee employee;
    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;


    String description;
    String password;
    String username;
    String phone;
    String email;
    String name;
    String radius;
    String resume;
    String clientID;
    EditText nameView;
    EditText emailView;
    EditText phoneView;
    EditText passView;
    EditText radiusView;
    EditText clientIDView;
    TextView usernameView;
    TextView statusView;
    TextView selectedPDF;
    TextView showRating;


    Button submitButton;
    Button refreshButton;
    Button imageButton;
    Button uploadResume;
    Button selectResume;
    Double rating;
    ImageView imageView;
    Uri image_uri;
    Uri pdf;
    ProgressDialog progress;
    TabLayout tab;
    EmployeeNavBarRouting route;
    // use upload profile button to
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_profile);

        objectCreator = new ObjectCreatorEmployeeImplementation();

        storage = FirebaseStorage.getInstance();
        database=FirebaseDatabase.getInstance();

        // get data from database
        employeeRef= FirebaseDatabase.getInstance().getReferenceFromUrl("https://group-6-a830d-default-rtdb.firebaseio.com/Employee");
        dbReadEmployee(employeeRef);
        // set all views
        setViews();

        employee = objectCreator.getEmployee();

        imageView = findViewById(R.id.profilePicture);
        imageButton = findViewById(R.id.profileImageButton);

        uploadResume = findViewById(R.id.uploadResume);
        selectResume = findViewById(R.id.seeResume);

        // set button to update to database on click
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Update fields
                // define new employee object and set fields
                //Employee employee = new Employee();

                setStatusMessage(true, "");

                System.out.println(clientIDView.getText().toString().trim());
                if (isNameEmpty(nameView.getText().toString())) {
                    setStatusMessage(false, "Error: Please fill in name");
                }
                else if (isEmailEmpty(emailView.getText().toString().trim())) {
                    setStatusMessage(false, "Error: Please fill in email");
                }
                else if (isPhoneNumEmpty(phoneView.getText().toString().trim())) {
                    setStatusMessage(false, "Error: Please fill in phone number");
                }
                else if (isPasswordEmpty(passView.getText().toString().trim())) {
                    setStatusMessage(false,"Error: Please fill in password");
                }
                else if (isRadiusEmpty(radiusView.getText().toString().trim())) {
                    setStatusMessage(false,"Error: Please enter a radius between 1-25");
                }
                else if (!isRadiusInRange(radiusView.getText().toString().trim())) {
                    setStatusMessage(false,"Error: Please enter a radius between 1-25");
                }
                else if (isClientIDEmpty(clientIDView.getText().toString().trim())) {
                    setStatusMessage(false,"Error: Please enter a valid client ID");
                }
                else {
                    employee.setName(nameView.getText().toString());
                    //employee.setUserName(usernameView.getText().toString());
                    employee.setPassword(passView.getText().toString());
                    employee.setPhone(phoneView.getText().toString());
                    employee.setEmailAddress(emailView.getText().toString());
                    employee.setClientID(clientIDView.getText().toString());
                    //employee.setResumeUrl(selectedPDF.getText().toString());
                    user.setRadius(radiusView.getText().toString());
                    user.getLatitude();
                    user.getLatitude();
                    // updates to db
                    updateToDatabase(employee, user);
                    //updateLocationToDatabase(user);
                }

            }
        });
        // set button to refresh profile fields on click


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(checkSelfPermission(Manifest.permission.CAMERA) ==
                            PackageManager.PERMISSION_DENIED ||
                            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                                    PackageManager.PERMISSION_DENIED ){
                        //request permission
                        String [] permission  = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permission, PERMISSION_CODE);
                    }
                    else{
                        //permission granted
                        openCamera();


                    }
                }
                else{
                    openCamera();
                }
            }
        });

        selectResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(EmployeeProfile.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    selectPDF();
                }
                else {
                    ActivityCompat.requestPermissions(EmployeeProfile.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 9);
                }
            }
        });

        uploadResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pdf != null) {
                    uploadFile(pdf);
                }
                else {
                    Toast.makeText(EmployeeProfile.this, "Please select a file", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void selectPDF() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 86);
    }

    private void uploadFile(Uri pdf) {
        progress = new ProgressDialog(this);
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setTitle("Uploading file...");
        progress.setProgress(0);
        progress.show();

        String fileName = System.currentTimeMillis() + "";
        StorageReference storageReference = storage.getReference();

        storageReference.child("Uploads").child(fileName).putFile(pdf).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                if (taskSnapshot.getMetadata().getReference() != null) {
                    Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                    result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();
                            DatabaseReference reference = database.getReferenceFromUrl("https://group-6-a830d-default-rtdb.firebaseio.com/Employee");

                            reference.child(userName).child("resumeUrl").setValue(url).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(EmployeeProfile.this, "File successfully uploaded", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Toast.makeText(EmployeeProfile.this, "File failed to upload", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
                }
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                int currentProgress = (int) (100*snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                progress.setProgress(currentProgress);
            }
        });
    }

    public void setViews(){
        statusView = findViewById(R.id.employeeStatusLabel);
        nameView = findViewById(R.id.applicantName);
        usernameView = findViewById(R.id.applicantUserName);
        passView = findViewById(R.id.applicantMessage);
        phoneView = findViewById(R.id.applicantPhoneNum);
        emailView = findViewById(R.id.applicantEmail);
        radiusView = findViewById(R.id.applicantRadius);
        clientIDView = findViewById(R.id.ClientIDInput);

       // selectedPDF = findViewById(R.id.selectedPDF);
        clientIDView = findViewById(R.id.ClientIDInput);

        showRating = findViewById(R.id.employeeRating);
        submitButton = (Button) findViewById(R.id.accept);
    }


    public void updateToDatabase(Employee employee, UserLocation user){
        // save object user to database to Firebase
        employeeRef= FirebaseDatabase.getInstance().getReferenceFromUrl("https://group-6-a830d-default-rtdb.firebaseio.com/Employee/" + username);

        Map<String, Object> updates = new HashMap<>();
        //updates.put("userName", employee.getUserName());
        updates.put("password", employee.getPassword());
        updates.put("email", employee.getEmail());
        updates.put("name", employee.getName());
        updates.put("phone", employee.getPhone());
        updates.put("description", employee.getDescription());
        //updates.put("resumeUrl", employee.getResumeUrl());
        updates.put("clientID", employee.getClientID());
        // Add radius to the database
        employeeRef.updateChildren(updates);
        // below sets entirely new employee object
        // employeeRef.setValue(employee);

        //Location
        Map<String, Object> locationUpdates = new HashMap<>();
        locationUpdates.put("latitude", user.getLatitude());
        locationUpdates.put("longitude", user.getLongitude());
        locationUpdates.put("radius", user.getRadius());

        employeeRef.child("Location").updateChildren(locationUpdates);
        employeeRef.child("Location").setValue(user);

        setStatusMessage(true, "Profile updated to database!");
    }

    public void refreshPage(){
        // save object user to database to Firebase
        employeeRef= FirebaseDatabase.getInstance().getReferenceFromUrl("https://group-6-a830d-default-rtdb.firebaseio.com/Employee");
        dbReadEmployee(employeeRef);
        //statusView.setText("Profile changes refreshed");
    }

    public void loadProfile(){
        nameView.setText(name);
        usernameView.setText(username);
        passView.setText(password);
        phoneView.setText(phone);
        emailView.setText(email);
        radiusView.setText(radius);
        clientIDView.setText(clientID);

        //selectedPDF.setText(resume);
    }

    public UserLocation getEmployeeLocation(){
        return user;
    }

    /**
     * Function: Method to set the rating in the textView
     * Parameters: double rating
     * Return: void
     */
    private void setShowRatingView(double rating){
        if(rating == 0.0){
            showRating.setText("No Ratings have been given yet");
        } else {
            showRating.setText(String.valueOf(rating));
        }
    }


    //code from loginPage
    //Read data from dataBase and retrieve employee information
    public void dbReadEmployee(DatabaseReference db){

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> employeeItr = dataSnapshot.getChildren().iterator();
                //Read data from data base.
                while (employeeItr.hasNext()) {
                    //assume there will always be at least one employer
                    Employee employee = employeeItr.next().getValue(Employee.class);
                    //need to check against correct value to retrieve the correct location
                    if (employee.getUserName().equals(validEmployee[0])){
                        user = new UserLocation();
                        user = dataSnapshot.child(validEmployee[0]).child("Location").getValue(UserLocation.class);
                        if (user != null) {
                            radius = user.getRadius();
                        }
                        username = employee.getUserName();
                        password = employee.getPassword();
                        phone = employee.getPhone();
                        email = employee.getEmail();
                        name = employee.getName();
                        description = employee.getDescription();
                        resume = employee.getResumeUrl();
                        clientID = employee.getClientID();
                        rating = dataSnapshot.child(validEmployee[0]).child("Rating").
                                getValue(Double.class);
                        if (rating == null) {
                            rating = 0.0;
                        }
                        break;
                    }
                }
                loadProfile();
                setShowRatingView(rating);
            }

                @Override
                public void onCancelled(DatabaseError databaseError) {


                }
            });
        }

    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the camera");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        //Camera intent
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);
    }

    //handle permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case PERMISSION_CODE:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    //granted to use camera
                    openCamera();
                }
                else{
                    //permission was denied
                    Toast.makeText(this, "No permission granted", Toast.LENGTH_SHORT).show();
                }
            }
        }
        if (requestCode == 9 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectPDF();
        }
        else {
            Toast.makeText(EmployeeProfile.this, "Please grant permission", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            imageView.setImageURI(image_uri);
        }
        if (requestCode == 86 && resultCode == RESULT_OK && data != null) {
            pdf = data.getData();
            //selectedPDF.setText(data.getData().getLastPathSegment());
        }
        else {
            Toast.makeText(EmployeeProfile.this, "Please select a file", Toast.LENGTH_SHORT).show();
        }
    }



    public static boolean isNameEmpty(String name) {
        return name.isEmpty();
    }

    public static boolean isEmailEmpty (String email) {
        return email.isEmpty();
    }

    public static boolean isPhoneNumEmpty (String phoneNum) {
        return phoneNum.isEmpty();
    }

    public static boolean isPasswordEmpty (String password) {
        return password.isEmpty();
    }


    protected static boolean isClientIDEmpty (String clientID) {
        return clientID.isEmpty();
    }

    protected static boolean isRadiusInRange (String radius) {
        if (Integer.valueOf(radius) >= 1 && Integer.valueOf(radius) <= 25 && !radius.isEmpty()) {
            return true;
        }
        return false;
    }

    public static boolean isRadiusEmpty (String radius) {
        return radius.isEmpty();
    }

    public void setStatusMessage(Boolean success, String message) {
        TextView statusLabel = findViewById(R.id.employeeStatusLabel);
        if (success) {
            statusLabel.setTextColor(Color.parseColor("#4CAF50"));
        } else {
            statusLabel.setTextColor(Color.RED);
        }
        statusLabel.setText(message);
    }


}
