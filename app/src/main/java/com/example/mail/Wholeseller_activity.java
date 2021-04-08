package com.example.mail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Wholeseller_activity extends AppCompatActivity {

    private TextView bussnm;
    private ImageButton logoutbt;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wholeseller_activity);

        bussnm = findViewById(R.id.bussnm);
        logoutbt = findViewById(R.id.logoutbt);

        firebaseAuth = FirebaseAuth.getInstance();

        logoutbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                checkuser();
            }
        });

    }

    private void checkuser(){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user==null){
            startActivity(new Intent(Wholeseller_activity.this, Choice_Role.class));
            finish();
        }

    }

    private void loadmyinfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Wholeseller");
        ref.orderByChild("wholesellerid").equalTo("wholesellerId")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            String name = "" + ds.child("bussinessname").getValue();
                            String accountype = "" + ds.child("accounttype").getValue();

                            bussnm.setText(name + "("+accountype+")");

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(Wholeseller_activity.this, "error", Toast.LENGTH_SHORT).show();

                    }


                });
    }
}

