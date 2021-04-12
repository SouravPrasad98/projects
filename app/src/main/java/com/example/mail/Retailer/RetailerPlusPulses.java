package com.example.mail.Retailer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.ebanx.swipebtn.OnStateChangeListener;
import com.ebanx.swipebtn.SwipeButton;
import com.example.mail.R;
import com.example.mail.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RetailerPlusPulses extends AppCompatActivity {
    private ElegantNumberButton moong, rajma , arhar, chole;

    private int moongq=0,rajmaq=0,arharq=0,choleq=0;
    private EditText moongp,rajmap,arharp,cholep;
    private SwipeButton pulseButton;
    private TextView back;
    private FirebaseDatabase rootNode;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer_plus_pulses);
        moong=(ElegantNumberButton)findViewById(R.id.moongRetailerPulse);
        rajma=(ElegantNumberButton)findViewById(R.id.rajmaRetailerPulse);
        arhar=(ElegantNumberButton)findViewById(R.id.arharRetailerPulse);
        chole=(ElegantNumberButton)findViewById(R.id.choleRetailerPulse);
        pulseButton=(SwipeButton) findViewById(R.id.buttonRetailerPulses);
        moongp=(EditText)findViewById(R.id.moongRetailerPulsePrice);
        rajmap=(EditText)findViewById(R.id.rajmaRetailerPulsePrice);
        arharp=(EditText)findViewById(R.id.arharRetailerPulsePrice);
        cholep=(EditText)findViewById(R.id.choleRetailerPulsePrice);
        back=(TextView)findViewById(R.id.retailerPulsesBack);

        user= FirebaseAuth.getInstance().getCurrentUser();
        userID=user.getUid();


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        moong.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                moongq= Integer.parseInt(moong.getNumber());
            }
        });
        rajma.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                rajmaq= Integer.parseInt(rajma.getNumber());
            }
        });
        chole.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                choleq= Integer.parseInt(chole.getNumber());
            }
        });
        arhar.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                arharq= Integer.parseInt(arhar.getNumber());
            }
        });

        pulseButton.setOnStateChangeListener(new OnStateChangeListener() {
            @Override
            public void onStateChange(boolean active) {


                rootNode=FirebaseDatabase.getInstance();
                reference=rootNode.getReference("Retailer/"+userID);
                int mp= Integer.parseInt(moongp.getText().toString().trim());
                int rp= Integer.parseInt(rajmap.getText().toString().trim());
                int ap= Integer.parseInt(arharp.getText().toString().trim());
                int cp= Integer.parseInt(cholep.getText().toString().trim());


                RetailerAddItemHelper ob=new RetailerAddItemHelper(mp,moongq,1,"Moong");
                reference.child("Moong").setValue(ob);
                RetailerAddItemHelper ob2=new RetailerAddItemHelper(rp,rajmaq,1,"Rajma");
                reference.child("Rajma").setValue(ob2);
                RetailerAddItemHelper ob3=new RetailerAddItemHelper(cp,choleq,1,"Chole");
                reference.child("Chole").setValue(ob3);
                RetailerAddItemHelper ob4=new RetailerAddItemHelper(ap,arharq,1,"Arhar");
                reference.child("Arhar").setValue(ob4);

                Toast.makeText(getBaseContext(),"Successfully Added",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}