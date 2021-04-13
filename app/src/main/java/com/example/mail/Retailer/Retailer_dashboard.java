package com.example.mail.Retailer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.example.mail.R;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class Retailer_dashboard extends AppCompatActivity {
    private ChipNavigationBar chip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer_dashboard);

        chip=findViewById(R.id.retailerBottomNavigation);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,new RetailerBottomHome()).commit();
        bottomMenu();
    }
    private void bottomMenu() {
        chip.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment=null;
                switch (i){
                    case R.id.retailerBottomNavigationHome:
                        fragment=new RetailerBottomHome();
                        break;
                    case R.id.retailerBottomNavigationPlus:
                        fragment=new RetailerBottomPlus();
                        break;
                    case R.id.retailerBottomNavigationAcceptOrders:
                        fragment=new RetailerBottomAcceptOrders();
                        break;
                    case R.id.retailerBottomNavigationAccount:
                        fragment=new RetailerBottomAccount();
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,fragment).commit();
            }
        });
    }
}