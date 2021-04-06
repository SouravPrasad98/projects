package com.example.mail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Registration extends AppCompatActivity  {
    private FirebaseAuth mAuth;
    private Button  Register;
    private EditText Fusername , Femail, Fpassword , Fmobilenumber;
    private ProgressBar progressbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mAuth = FirebaseAuth.getInstance();
        Register =(Button) findViewById(R.id.Register);
        Fusername = findViewById(R.id.Username);
        Femail = findViewById(R.id.Email);
        Fpassword = findViewById(R.id.Password);
        Fmobilenumber = findViewById(R.id.PhoneNumber);

        progressbar = findViewById(R.id.progressBar);


        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterUser();
            }
        });
    }




    private void RegisterUser() {
        String username = Fusername.getText().toString().trim();
        String email = Femail.getText().toString().trim();
        String password = Fpassword.getText().toString().trim();
        String mobile = Fmobilenumber.getText().toString().trim();
        if(username.isEmpty())
        {
            Fusername.setError("Full name plz!!");
            Fusername.requestFocus();
            return;

        }
        if(email.isEmpty())
        {
            Femail.setError("Email plz!!");
            Femail.requestFocus();
            return;

        }    if(password.isEmpty())
        {
            Fpassword.setError("Password plz!!");
            Fpassword.requestFocus();
            return;

        }    if(mobile.isEmpty())
        {
            Fmobilenumber.setError("Mobile Number plz!!");
            Fmobilenumber.requestFocus();
            return;

        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Femail.setError("Please Valid email!!");
            Femail.requestFocus();
            return;

        }
        progressbar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user = new User(username, email, mobile );
                            FirebaseDatabase.getInstance().getReference("User")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(Registration.this, "You almost joined the party....Ala thats left is verification",Toast.LENGTH_LONG).show();
                                        progressbar.setVisibility(View.GONE);
                                    }
                                    else
                                        Toast.makeText(Registration.this,"Failed !!Try again Dude",Toast.LENGTH_LONG).show();
                                    progressbar.setVisibility(View.GONE);
                                }
                            });
                        }
                        else
                        {
                            Toast.makeText(Registration.this,"Failed !!Try again Dude1",Toast.LENGTH_LONG).show();
                            progressbar.setVisibility(View.GONE);
                        }

                    }
                });

    }
}