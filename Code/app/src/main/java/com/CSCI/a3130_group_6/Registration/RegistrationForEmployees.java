package com.CSCI.a3130_group_6.Registration;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.util.PatternsCompat;

import com.CSCI.a3130_group_6.EmployeePackage.ObjectCreatorEmployeeImplementation;
import com.CSCI.a3130_group_6.EmployeePackage.ObjectCreatorEmployeeSingleton;
import com.CSCI.a3130_group_6.Listings.AddListingMap;
import com.CSCI.a3130_group_6.EmployeePackage.Employee;
import com.CSCI.a3130_group_6.HelperClases.ImageCapture;
import com.CSCI.a3130_group_6.Listings.Listing;
import com.CSCI.a3130_group_6.HelperClases.PermissionUtil;
import com.CSCI.a3130_group_6.Listings.ObjectCreatorListingSingleton;
import com.CSCI.a3130_group_6.R;
import com.CSCI.a3130_group_6.HelperClases.UserLocation;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class RegistrationForEmployees extends AppCompatActivity implements View.OnClickListener {
    EditText name, username, password, vpassword, phone, email, inputRadius, paypalId;
    Listing list;
    TextInputLayout selfDef;
    private Employee employee;
    Button homeBt, submitBt, employeeBt, imageBtn, uploadResume, selectResume, addLocationButton;
    //creating buttons and display variables
    TextView registrationStatus;
    DatabaseReference employerRef = null;
    TextView employeeUsernameError;
    TextView statusLabel;
    TextView currentLocationView;
    DatabaseReference employeeRef = null;
    Employee employees;

    ImageView imageToUpload;
    Button bUploadImage;
    EditText uploadImage;
    private static final int RESULT_LOAD_IMAGE = 1;
    FirebaseStorage storage;
    FirebaseDatabase database;
    ProgressDialog progress;
    private Uri image_uri;
    private Uri pdf;
    ImageView imageView;

    CheckExistingUserName user;
    AddListingMap location;
    LatLng currentLocation;
    Context context;
    Activity activity;
    LocationManager manager;
    UserLocation exactAddress;
    LatLng userCurrentLocation;
    String radius;
    String resumeUrl;

    ObjectCreatorEmployeeSingleton objectCreator;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);

        ImageButton imageButton = findViewById(R.id.imageButton);

        imageToUpload = (ImageView) findViewById(R.id.imageToUpload);
        ImageView downloadedImage = (ImageView) findViewById(R.id.downloadedImage);

        bUploadImage = (Button) findViewById(R.id.bUploadImage);
        Button bDownloadedImage = (Button) findViewById(R.id.bDownloadedImage);

        uploadImage = (EditText) findViewById(R.id.etloadImageName);
        EditText downloadImageName = (EditText) findViewById(R.id.etDownloadName);

        uploadResume = findViewById(R.id.uploadResume);
        selectResume = findViewById(R.id.seeResume);

        name = findViewById(R.id.name);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        vpassword = findViewById(R.id.vpassword);
        phone = findViewById(R.id.phone);        //assigning the variables to its associated variable on th view
        email = findViewById(R.id.email);
        submitBt = findViewById(R.id.Submit1);
        employeeBt = findViewById(R.id.Employer);
        homeBt = findViewById(R.id.home2);
        statusLabel = findViewById(R.id.statusLabel);
        addLocationButton = findViewById(R.id.addLocationButton);
        inputRadius = findViewById(R.id.inputRadius);
        paypalId = findViewById(R.id.payPalId);
        employeeBt.setOnClickListener(this);
        homeBt.setOnClickListener(this);
        submitBt.setOnClickListener(this);
        addLocationButton.setOnClickListener(this);
        employeeUsernameError = findViewById(R.id.employeeUserError);
        context = RegistrationForEmployees.this;
        activity = RegistrationForEmployees.this;
        currentLocationView = findViewById(R.id.currentLocationView);

        exactAddress = new UserLocation();
        imageBtn = findViewById(R.id.Image);


        user = new CheckExistingUserName();

        location = new AddListingMap();
        user.validateUsername(username, employeeUsernameError);

        employeeBt.setOnClickListener(this);
        homeBt.setOnClickListener(this);
        submitBt.setOnClickListener(this);
        imageBtn.setOnClickListener(this);
        uploadResume.setOnClickListener(this);
        selectResume.setOnClickListener(this);
        imageButton.setOnClickListener(this);

        // to ask for permissions from user to share location
        checkPermissions();

        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();

        // get data from database
        employeeRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://group-6-a830d-default-rtdb.firebaseio.com/Employee");

        objectCreator = new ObjectCreatorEmployeeImplementation();
        employees = objectCreator.getEmployee();
    }

    public boolean isUserNameEmpty() {
        return getInputUserName().equals("");
    }

    public boolean isNameEmpty() {
        return getName().equals("");
    }

    public boolean isPasswordEmpty() {
        return getInputPassword().equals("");
    }

    public boolean isVerifyPasswordEmpty() {
        return vpassword.getText().toString().trim().equals("");
    }

    public boolean isPhoneEmpty() {
        return getPhoneNumber().equals("");
    }

    public boolean isValidEmail(String email) {
        return PatternsCompat.EMAIL_ADDRESS.matcher(email).matches();
    }
    public boolean isPasswordMatched() {
        return (getInputPassword().equals(getInputVpassword()));
    }

    public String getInputVpassword() {
        return vpassword.getText().toString().trim();
    }

    public boolean isPasswordMatched(String password, String vPassword) {
        return (password.equals(vPassword));
    }
    /**
     * Function: method to check if all the registration input fields are valid
     * Parameters:
     * Returns: boolean
     *
     */
    public boolean validRegistrationInformation() {

        boolean validRegistrationInformation = !isUserNameEmpty() && !isPasswordEmpty() && !isVerifyPasswordEmpty() && !isNameEmpty()
                && !isPhoneEmpty() && isValidEmail(getInputEmailAddress());

        return validRegistrationInformation;
    }

    /**
     * Function: Method to save employee information to the database
     * Parameters: Object Employee
     * Returns: boolean
     *
     */
    public void saveEmployeeToDataBase(Object Employee) {
        //save object user to database to Firebase
        employees.setUserName(getInputUserName());
        employees.setPassword(getInputPassword());
        employees.setEmailAddress(getInputEmailAddress());
        employees.setPhone(getPhoneNumber());
        employees.setName(getName());
        employees.setClientID(getClientID());
        employeeRef.child(employees.getUserName()).setValue(Employee);
        UserLocation present = new UserLocation(userCurrentLocation.latitude, userCurrentLocation.longitude, getInputRadius());
        employeeRef.child(employees.getUserName()).child("Location").setValue(present);
    }

    public String getInputUserName() {
        return username.getText().toString().trim();
    }

    public String getInputPassword() {
        return password.getText().toString().trim();
    }

    public String getInputEmailAddress() {
        return email.getText().toString().trim();
    }

    public String getPhoneNumber() {
        return phone.getText().toString().trim();
    }

    public String getName() {
        return name.getText().toString().trim();
    }

    public String getSelfDescription() { return selfDef.toString(); }


    public String getClientID(){
        return paypalId.getText().toString();
    }

    /*
    Changing pages to see employer registration
     */
    public void switchToEmployer() {
        Intent employer = new Intent(this, RegistrationForEmployers.class);
        startActivity(employer);
    }
    public void switchtoImage() {
        Intent Image = new Intent(this, ImageCapture.class);
        startActivity(Image);
    }

    /*
    Switch to login page
     */
    public void switchToHome() {
        Intent back = new Intent(this, LoginPage.class);
        startActivity(back);
    }

    /**
     * Function: Method to create a Toast
     * Parameters:
     * Returns: boolean
     *
     */
    private void createToast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.show();
    }

    public boolean isEmptyLocation(String location) {
        return location.isEmpty();
    }


     // *******************
     // Map Code Start
     // Code for map has been taken from tutorials on Google Map Integration
     // *******************

    public void checkPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            checkLocationPermission(activity, context, location.LOCATION_PERMISSION, location.LOCATION_PREF);
        } else {
        }
    }

    public void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    /**
     * Function: Method to ask user for sharing their location
     * Parameters:
     * Returns: void
     *
     */
    private void checkLocationPermission(final Activity activity, final Context context, final String Permission, final String prefName) {

        PermissionUtil.checkPermission(activity, context, Permission, prefName,
                new PermissionUtil.PermissionAskListener() {
                    @Override
                    public void onPermissionAsk() {
                        ActivityCompat.requestPermissions(activity,
                                new String[]{Permission},
                                location.MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                    }

                    @Override
                    public void onPermissionPreviouslyDenied() {
                        //show a dialog explaining is permission denied previously , but app require it and then request permission

                        showToast("Permission previously Denied.");

                        ActivityCompat.requestPermissions(activity,
                                new String[]{Permission},
                                location.MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                    }

                    @Override
                    public void onPermissionDisabled() {
                        // permission check box checked and permission denied previously .
                        askUserToAllowPermissionFromSetting();
                    }

                    @Override
                    public void onPermissionGranted() {
                        showToast("Permission Granted.");
                    }
                });
    }

    /**
     * Function: Method to ask and take user to Settings menu to setup location permissions
     * Parameters:
     * Returns: void
     *
     */
    private void askUserToAllowPermissionFromSetting() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set title
        alertDialogBuilder.setTitle("Permission Required:");

        // set dialog message
        alertDialogBuilder
                .setMessage("Kindly allow Permission from App Setting, without this permission app could not show maps.")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, location.MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                        showToast("Permission forever Disabled.");
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }


    LocationListener listener = new LocationListener() {

        @Override
        public void onLocationChanged(@NonNull Location locate) {
            currentLocation = new LatLng(locate.getLatitude(), locate.getLongitude());
            userCurrentLocation = currentLocation;

            try {
                getAddressFromLocation(currentLocation);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

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
        currentLocationView.setText(exactAddress.getAddress());
    }

    /**
     * Function: Method to get the current location
     * Parameters:
     * Returns: void
     *
     */
    // method to get the current location
    public void getCurrentLocation() {
        manager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            return;
        }
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000,
                5, listener);
    }

    // *******************
    // Map Code End
    // Code for map has been taken from tutorials on Google Map Integration
    // *******************

    public String getInputRadius(){
        return inputRadius.getText().toString();
    }

    public String getInputLocation() {return currentLocationView.getText().toString(); }

    /**
     * Function: Method to check if the radius is a valid number
     * Parameters: String
     * Returns: boolean
     *
     */
    // method to check if radius is a valid number
    public boolean validateRadius(String radius){
        int check = 0;
        try {
            check = Integer.valueOf(radius);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * Function: Method to check if the radius is in a valid range
     * Parameters: String
     * Returns: boolean
     *
     */
    // method to check if radius is in a valid range
    public boolean validateRadiusRange(String radius){
        int check= Integer.valueOf(radius);
        boolean checkRange = check < 0 || check> 25;
        if(checkRange) {
            return false;
        }
        return true;
    }

    public boolean isEmptyRadius(String radius){
        return radius.isEmpty();
    }

    public void onClick(View v) {

        if (R.id.Submit1 ==v.getId()){//when the submit button is clicked, add employee
            if(!validRegistrationInformation()){
                createToast("Empty or invalid registration information");
            }
            else if(user.checkUserNameError(employeeUsernameError)){
                createToast("Please change the username");
            }
            else if(!isPasswordMatched(getInputPassword(), getInputVpassword())){//when the password and verification password are not matched
                statusLabel.setText("password is not matched");
            }
            else if(isEmptyLocation(getInputLocation()) && isEmptyRadius(getInputRadius())){
                createToast("Please add location and radius");
            }
            else if(isEmptyLocation(getInputLocation()) && !isEmptyRadius(getInputRadius())){
                createToast("Please add location.");
            }
            else if(!isEmptyLocation(getInputLocation()) && isEmptyRadius(getInputRadius())){
                createToast("Please add radius.");
            }
            else if(!validateRadius(getInputRadius())){
                createToast("Radius not a number.");
            }
            else if(!validateRadiusRange(getInputRadius())){
                createToast("Radius should be between 1 and 25");
            }
            else if(getClientID()==null || getClientID().equals("")){
                statusLabel.setText("PayPal ID must be input");
                createToast("Paypal ID must be input");
            }
            else {
                employees.setUserName(getInputUserName());
                employees.setPassword(getInputPassword());
                employees.setEmailAddress(getInputEmailAddress());
                employees.setPhone(getPhoneNumber());
                employees.setName(getName());
                employees.setClientID(getClientID());
                exactAddress.setRadius(getInputRadius());
                employees.setResumeUrl(resumeUrl);
                saveEmployeeToDataBase(employees);
                switchToHome();
            }
        }
        else if(R.id.addLocationButton == v.getId()){
            // add method to get the current lat long
            getCurrentLocation();
        }
        switch (v.getId()) {
            case R.id.home2:
                switchToHome();
                break;

            case R.id.Employer:
                switchToEmployer();
                break;

            case R.id.Image:
                switchtoImage();
                break;

            case R.id.imageButton:
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
                break;
        }
        switch (v.getId()) {
            case R.id.imageToUpload:
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
                if (RESULT_LOAD_IMAGE == RESULT_OK) {
                    imageToUpload.setImageURI(image_uri);
                };
                break;

            case R.id.bUploadImage:

                break;

            case R.id.bDownloadedImage:

                break;
        }
        selectResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(RegistrationForEmployees.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    selectPDF();
                } else {
                    ActivityCompat.requestPermissions(RegistrationForEmployees.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 9);
                }

            }
        });
        uploadResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //make sure information is filled out before user can upload resume
                if (pdf != null) {
                    uploadFile(pdf);
                } else {
                    Toast.makeText(RegistrationForEmployees.this, "Please fill out the information before uploading Resume.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    static final int REQUEST_IMAGE_CAPTURE = 101;
    public static final int GET_FROM_GALLERY = 1;
    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;

    private void selectPDF() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 86);
    }
    //upload pdf file
    private void uploadFile(Uri pdf) {
        progress = new ProgressDialog(this);
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setTitle("Uploading file...");
        progress.setProgress(0);
        progress.show();

        String fileName = System.currentTimeMillis() + "";
        DatabaseReference listing = FirebaseDatabase.getInstance().getReferenceFromUrl("https://group-6-a830d-default-rtdb.firebaseio.com/Employee");
        StorageReference storageReference = storage.getReference();
        StorageTask<UploadTask.TaskSnapshot> taskSnapshotStorageTask = storageReference.child("Uploads").child(fileName).putFile(pdf).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                if (taskSnapshot.getMetadata().getReference() != null) {
                    Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                    result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();
                            resumeUrl = result.getResult().toString();
                            System.out.println(resumeUrl);
                            Toast.makeText(RegistrationForEmployees.this, "File successfully uploaded", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                int currentProgress = (int) (100 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                progress.setProgress(currentProgress);
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
            Toast.makeText(RegistrationForEmployees.this, "Please grant permission", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            image_uri = data.getData();
            try {
                imageToUpload.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), image_uri));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == 86 && resultCode == RESULT_OK && data != null) {
            pdf = data.getData();
        }
        else {
            Toast.makeText(RegistrationForEmployees.this, "Please select a file", Toast.LENGTH_SHORT).show();
        }
    }




}


