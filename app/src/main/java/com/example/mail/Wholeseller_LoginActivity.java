package com.example.mail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Wholeseller_LoginActivity extends AppCompatActivity {
    private EditText password1;
    private EditText emailliddd;
    private Button login;
    private TextView noacc, forgotT;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wholeseller__login);

        emailliddd = findViewById(R.id.emaillidddd);
        password1 = findViewById(R.id.password1);
        login = findViewById(R.id.login);
        noacc = findViewById(R.id.noacc);
        forgotT = findViewById(R.id.forgotT);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setCanceledOnTouchOutside(false);


        noacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Wholeseller_LoginActivity.this, Wholeseller_register.class));
            }
        });

        forgotT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Wholeseller_LoginActivity.this, Wholeseller_forgotpassword.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginwholeseller();
            }
        });
    }

    private String Email, Password;

    private void loginwholeseller() {
        Email = emailliddd.getText().toString().trim();
        Password = password1.getText().toString().trim();

        if (TextUtils.isEmpty(Email)) {
            Toast.makeText(this, "Enter Bussiness Id", Toast.LENGTH_SHORT);
            return;
        }

        if (TextUtils.isEmpty(Password)) {
            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT);
            return;
        }
        progressDialog.setMessage("Logging In...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(Email, Password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        makeMeOnline();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void makeMeOnline() {
        progressDialog.setMessage("Checking User...");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("Online", "true");

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Wholeseller");
        ref.child("Email").updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        gotoshop();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                    }
                });
    }

    private void gotoshop() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Wholeseller");
        ref.orderByChild("wholesellerid").equalTo("wholesellerId")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        startActivity(new Intent(Wholeseller_LoginActivity.this, Wholeseller_activity.class));
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        startActivity(new Intent(Wholeseller_LoginActivity.this, Wholeseller_register.class));
                        finish();
                    }


                });


    }
}