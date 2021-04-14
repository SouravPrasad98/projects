package com.example.mail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.PatternMatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class Wholeseller_forgotpassword extends AppCompatActivity {
    private ImageButton backbutt;
    private EditText emailliddd;
    private Button login;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wholeseller_forgotpassword);

        backbutt = findViewById(R.id.backbutt);
        emailliddd = findViewById(R.id.emaillidddd);
        login = findViewById(R.id.login);

        firebaseAuth= FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setCanceledOnTouchOutside(false);

        backbutt.setOnClickListener((v) -> {onBackPressed();});
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recoverpassword();
            }
        });


    }



    private   String email;
    private void recoverpassword() {
        email= emailliddd.getText().toString().trim();
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this, "Invalid mail",Toast.LENGTH_SHORT ).show();
            return;
        }

        progressDialog.setMessage("Sending instructions to reset Password");
        progressDialog.show();

        firebaseAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.dismiss();
                        Toast.makeText(Wholeseller_forgotpassword.this, "Password reset Instructions sent", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(Wholeseller_forgotpassword.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });



    }
    }

