package com.example.mail;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.location.Criteria;
import android.location.Location;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.content.DialogInterface;
import android.location.LocationListener;
import android.location.LocationManager;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import io.grpc.okhttp.internal.framed.FrameReader;

import static android.widget.Toast.*;

public class Wholeseller_register extends AppCompatActivity implements LocationListener {
    private ImageButton backbutt, gpssbutt;
    private ImageView profileIv;
    private Button signUpbt;
    private EditText confirm_password, password1, delevid, wholid, emaillidddd, busstyyp, PhoneNumber,
            bussnm, younm;
    private TextView addlll, cityyid, stateid, countrid, UserSignUp, retailerSignUp;

    private static final int Location_Request_code = 100;
    private static final int Camera_Request_code = 200;
    private static final int Storage_Request_code = 300;
    private static final int IMAGE_PICK_GALLERY_CODE = 400;
    private static final int IMAGE_PICK_CAMERA_CODE = 500;



    private String[] locationPermissions;
    private String[] cameraPermissions;
    private String[] storagePermissions;

    private Uri image_uri;

    private LocationManager locationManager;
    private double latitude, longitude;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wholeseller_register);
        addlll = findViewById(R.id.addlll);
       // profileIv = findViewById(R.id.profileIv);
        gpssbutt = findViewById(R.id.gpssbutt);
        backbutt = findViewById(R.id.backbutt);
        signUpbt = findViewById(R.id.signUpbt);
        confirm_password = findViewById(R.id.confirm_password);
        password1 = findViewById(R.id.password1);
        delevid = findViewById(R.id.delevid);
        wholid = findViewById(R.id.wholid);
        emaillidddd = findViewById(R.id.emaillidddd);
        cityyid = findViewById(R.id.cityyid);
        stateid = findViewById(R.id.stateid);
        countrid = findViewById(R.id.countrid);
        busstyyp = findViewById(R.id.busstyyp);
        PhoneNumber = findViewById(R.id.PhoneNumber);
        bussnm = findViewById(R.id.bussnm);
        younm = findViewById(R.id.younm);
        profileIv= findViewById(R.id.profileIv);
        UserSignUp = findViewById(R.id.UserSignUp);
        retailerSignUp = findViewById(R.id.retailerSignUp);

        locationPermissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};


        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setCanceledOnTouchOutside(false);


        backbutt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        retailerSignUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(Wholeseller_register.this, Retailer_register.class));
                return;
            }
        });

        UserSignUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(Wholeseller_register.this, User_register.class));
                return;
            }
        });

        profileIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePickDialog();
            }
        });

        gpssbutt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkLocationPermission()){
                    detectLocation();
                }
                else {
                    requestLocationPermission();
                }

            }
        });

        signUpbt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String wholesellerId = wholid.getText().toString().trim();
                String password = password1.getText().toString().trim();
                inputData();


            }
        });
    }

    private void showImagePickDialog() {
        String[] options = {"Camera" , "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick Image")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0){
                            //camera clicked
                            if(checkCameraPermission()){
                                pickFromCamera();

                            }
                            else{
                                requestCameraPermission();

                            }
                        }
                        else{

                            if(checkStoragePermission()){
                                pickFromGallery();

                            }
                            else{
                                requestStoragePermission();

                            }
                            //gallery clicked
                        }

                    }
                })
                .show();
    }

    private void pickFromGallery(){
        Intent intent= new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);
    }

    private void pickFromCamera(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Temp_Image Title");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Temp_Image Description");

        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE);
    }

    private void detectLocation() {
        Toast.makeText(this, "Please wait...", LENGTH_LONG).show();

        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }
    private void findAddress() {

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try{
            addresses = geocoder.getFromLocation(latitude, longitude, 1);

            String address = addresses.get(0).getAddressLine(0);
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();

            cityyid.setText(city);
            stateid.setText(state);
            countrid.setText(country);
            addlll.setText(address);



        }
        catch (Exception e){
            Toast.makeText(this, "error", LENGTH_SHORT).show();
        }
    }
    private boolean checkLocationPermission(){
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestLocationPermission(){
        ActivityCompat.requestPermissions(this, locationPermissions, Location_Request_code);

    }

    private boolean checkStoragePermission(){
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission(){
        ActivityCompat.requestPermissions(this, storagePermissions, Storage_Request_code);
    }

    private boolean checkCameraPermission(){
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }
    private void requestCameraPermission(){
        ActivityCompat.requestPermissions(this, cameraPermissions, Camera_Request_code);
    }




    @Override
    public void onLocationChanged(@NonNull Location location) {
    latitude = location.getLatitude();
    longitude = location.getLongitude();

    findAddress();

    }



    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
Toast.makeText(this, "Please enable Location",LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case Location_Request_code:{
                if(grantResults.length > 0){
                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if(locationAccepted){
                        detectLocation();

                    }
                    else{
                        Toast.makeText(this, "Location permission necessary", LENGTH_SHORT).show();

                    }
                }
            }
            break;
            case Camera_Request_code:{
                if(grantResults.length > 0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted && storageAccepted){
                        pickFromCamera();

                    }
                    else{
                        Toast.makeText(this, "Camera permission necessary", LENGTH_SHORT).show();

                    }
                }
            }
            break;
            case Storage_Request_code:{
                if(grantResults.length > 0){
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if(storageAccepted){
                        pickFromGallery();

                    }
                    else{
                        Toast.makeText(this, "Storage permission necessary", LENGTH_SHORT).show();

                    }
                }
            }
            break;


        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
     if(resultCode==RESULT_OK){
         if(requestCode == IMAGE_PICK_GALLERY_CODE){
             image_uri = data.getData();
             profileIv.setImageURI(image_uri);
         }
         else if (requestCode == IMAGE_PICK_CAMERA_CODE){
             profileIv.setImageURI(image_uri);
         }
     }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String fullname, phonenumber, deliveryfee, country, state, city, address, email, password, confirmpssd, bussinessname,
            bussinesscategory, wholesellerId;

    private void inputData() {
        fullname = younm.getText().toString().trim();
        bussinessname = bussnm.getText().toString().trim();
        bussinesscategory = busstyyp.getText().toString().trim();
        phonenumber = PhoneNumber.getText().toString().trim();
        country = countrid.getText().toString().trim();
        state = stateid.getText().toString().trim();
        city = cityyid.getText().toString().trim();
        address = addlll.getText().toString().trim();
        email = emaillidddd.getText().toString().trim();
        password = password1.toString().trim();
        confirmpssd = confirm_password.getText().toString().trim();
        wholesellerId = wholid.getText().toString().trim();
        deliveryfee = delevid.getText().toString().trim();

        if (TextUtils.isEmpty(fullname)) {
            Toast.makeText(getApplicationContext(), "Enter Name", LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(bussinessname)) {
            Toast.makeText(getApplicationContext(), "Enter Bussiness Name", LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(bussinesscategory)) {
            Toast.makeText(getApplicationContext(), "Enter Bussiness Category", LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(phonenumber)) {
            Toast.makeText(getApplicationContext(), "Enter Phone Number", LENGTH_SHORT).show();
            return;

        }
        if (TextUtils.isEmpty(deliveryfee)) {
            Toast.makeText(getApplicationContext(), "Enter Delivery Fee", LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(getApplicationContext(), "Invalid Email", LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(getApplicationContext(), "Password must be atleast 6 characters long", LENGTH_SHORT).show();
            return;

        }
        if (!(password.equals(confirmpssd))) {
            Toast.makeText(getApplicationContext(), "Password doesn't match", LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(wholesellerId)) {
            Toast.makeText(getApplicationContext(), "Enter WholesellerID", LENGTH_SHORT).show();
            return;
        }
        createAccount();


    }

    private void createAccount() {
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        saverFirebaseData();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(Wholeseller_register.this, "" + e.getMessage(), LENGTH_SHORT).show();
                    }
                });
    }

    private void saverFirebaseData() {
        progressDialog.setMessage("Saving Account Info");
        String timestamp = "" + System.currentTimeMillis();
        HashMap<String, Object> hashMap = new HashMap<>();
        String uid = firebaseAuth.getUid();
        hashMap.put("uid", "" + uid);
        hashMap.put("email", "" + email);
        hashMap.put("name", "" + fullname);
        hashMap.put("bussinessname", "" + bussinessname);
        hashMap.put("bussinesscategory", "" + bussinesscategory);
        hashMap.put("phonenumber", "" + phonenumber);
        hashMap.put("country", "" + country);
        hashMap.put("state", "" + state);
        hashMap.put("city", "" + city);
        hashMap.put("password", "" + password);
        hashMap.put("address", "" + address);
        hashMap.put("deliveryfree", "" + deliveryfee);
        hashMap.put("latitude", "" + latitude);
        hashMap.put("longitude", "" + longitude);
        hashMap.put("accounttype", "wholeseller");
        hashMap.put("online", "true");
        hashMap.put("wholesellerid", "" + wholesellerId);
        hashMap.put("shopopen", "true");
        hashMap.put("timestamp", "" + timestamp);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Wholeseller");
        ref.child("wholesellerid").setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.dismiss();
                        startActivity(new Intent(Wholeseller_register.this, Wholeseller_activity.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        startActivity(new Intent(Wholeseller_register.this, Wholeseller_activity.class));
                        finish();

                    }
                });

    }


}

















