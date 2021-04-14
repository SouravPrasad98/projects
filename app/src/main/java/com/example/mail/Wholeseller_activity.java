package com.example.mail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Wholeseller_activity extends AppCompatActivity {

    private TextView bussnm,profile_name;
    private ImageButton logoutbt, addproduct;
    private ImageView profileIv;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wholeseller_activity);

        bussnm = findViewById(R.id.bussnm);
        logoutbt = findViewById(R.id.logoutbt);
        addproduct = findViewById(R.id.addproduct);
        profile_name = findViewById(R.id.profile_name);
        profileIv = findViewById(R.id.profileIv);
        firebaseAuth = FirebaseAuth.getInstance();
        checkuser();

        logoutbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                checkuser();
            }
        });

        addproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Wholeseller_activity.this,Wholeseller_addproduct.class));

            }
        });

    }

    private void checkuser(){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        Log.d("checkuserAyush", "checkuser: " + user.toString());
        if(user==null){
            startActivity(new Intent(Wholeseller_activity.this, Choice_Role.class));
            finish();
        }
        else
        {
            loadmyinfo();
        }

    }

    private void loadmyinfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Wholeseller");
        ref.orderByChild("uid").equalTo(firebaseAuth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            String name = "" + ds.child("bussinessname").getValue();
                            String accountype = "" + ds.child("accounttype").getValue();
                            String profilename = ""+ ds.child("name").getValue();
                            String profileimage= "" + ds.child("profileimage").getValue();

                            bussnm.setText(name + "("+accountype+")");
                            profile_name.setText(profilename);
                            try{
                                Picasso.get().load(profileimage).placeholder(R.drawable.ic_baseline_person_24).into(profileIv);
                            }
                            catch (Exception e){
                                profileIv.setImageResource(R.drawable.ic_baseline_person_24);
                            }

                        }
                    }

                    @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(Wholeseller_activity.this, "error", Toast.LENGTH_SHORT).show();

                    }


                });
    }
}

