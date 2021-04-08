package com.example.mail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

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

import static android.widget.Toast.*;

public class Wholeseller_register extends AppCompatActivity implements LocationListener {
    private ImageButton backbutt, gpssbutt;
    private Button signUpbt;
    private EditText confirm_password, password1, delevid, wholid, emaillidddd,  busstyyp, PhoneNumber,
            bussnm, younm,addlll, cityyid, stateid, countrid;
    private static final int Location_Request_code = 100;

    private String[] locationPermissions;


    private LocationManager locationManager;
    private Double latitude = 0.0, longitude = 0.0;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wholeseller_register);
        addlll=findViewById(R.id.addlll);
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


        locationPermissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};

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

        gpssbutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkLocationPermission()) {
                    detectlocation();
                } else {
                    requestLocationPermission();
                }

            }
        });

        signUpbt.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                String wholesellerId = wholid.getText().toString().trim();
                String password = password1.getText().toString().trim();
                inputData();


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
                                Toast.makeText(Wholeseller_register.this, ""+e.getMessage(), LENGTH_SHORT).show();
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
                hashMap.put("password", ""+password);
                hashMap.put("address", "" + address);
                hashMap.put("deliveryfree", "" + deliveryfee);
                hashMap.put("latitude", "" + latitude);
                hashMap.put("longitude", "" + longitude);
                hashMap.put("accounttype", "wholeseller");
                hashMap.put("online", "true");
                hashMap.put("wholesellerid", ""+wholesellerId);
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

        });

    }
        private void detectlocation () {
            Toast.makeText(this, "Please Wait...", LENGTH_LONG).show();
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
              findAddress();
    }

        private boolean checkLocationPermission () {
            boolean result = ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) == (PackageManager.PERMISSION_GRANTED);
            return result;
        }
    private void requestLocationPermission () {
        ActivityCompat.requestPermissions(this, locationPermissions, Location_Request_code);
            }


    @Override
    public void onLocationChanged (@NonNull Location location){
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        findAddress();
    }

    private void findAddress () {
        Geocoder geocoder;

        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(longitude, latitude, 1);
            String address = addresses.get(0).getAddressLine(0);
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();

            //setaddress
            countrid.setText(country);
            stateid.setText(state);
            cityyid.setText(city);
            addlll.setText(address);
            Log.d("success", "findAddress: ");


        } catch (Exception e) {
            makeText(this, "Error", LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
    }

    @Override
    public void onProviderDisabled (@NonNull String provider){
        makeText(this, "Please Enable Location", LENGTH_SHORT).show();
    }


    @Override
        public void onRequestPermissionsResult ( int requestCode, @NonNull String[] permissions,
        @NonNull int[] grantResults){
            switch (requestCode) {
                case Location_Request_code: {
                    if (grantResults.length > 0) {
                        boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                        if (locationAccepted) {
                            detectlocation();
                        } else {
                            makeText(this, "Location permission neccessary", LENGTH_SHORT).show();
                        }

                    }
                }
                break;
            }
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }





    }
