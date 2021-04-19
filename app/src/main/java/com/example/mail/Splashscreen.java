package com.example.mail;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;

public class Splashscreen extends AppCompatActivity {
private FirebaseAuth firebaseAuth;
private DataSnapshot dataSnapshot;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splashscreen);
        new Handler().postDelayed(new Runnable() {

                                      @Override
                                      public void run() {

                                          {
                                              startActivity(new Intent(Splashscreen.this,MainActivity.class));
                                              finish();
                                          }

                                      }
                                  },
                2000);
    }

   /* private void checkusertype() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Wholeseller");
        ref.orderByChild("uid").equalTo(firebaseAuth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            String account_type = "" + ds.child("accounttype").getValue();
                            if (account_type.equals("wholeseller")) {
                                startActivity(new Intent(Splashscreen.this, Wholeseller_activity.class));
                                finish();
                            } else {

                                startActivity(new Intent(Splashscreen.this, Login.class));
                                finish();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {


                    }
                });


    }*/
}


