package com.example.cropdoctor;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements FirstFragment.onSomeEventListener {

    Toolbar toolbar ;
    Location loc=null;
    BottomNavigationView navigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button bt;
        bt=findViewById(R.id.fab);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(loc==null)
                {
                    Toast.makeText(getApplicationContext(),"The application is trying to \nfet" +
                            "ch your location plz wait and try after a while",Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),"hhhhh  "+String.valueOf(loc.getLatitude()) +
                            String.valueOf(loc.getLongitude()),Toast.LENGTH_LONG).show();

                    Intent hg = new Intent(MainActivity.this, Shops.class);
                    Bundle b = new Bundle();
                    b.putDouble("dataone",loc.getLatitude());
                    b.putDouble("datatwo",loc.getLongitude());
                     hg.putExtras(b);
                    startActivity(hg);
                }
            }});

        loadFragment(new FirstFragment());


        //windowActionBar=false
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) navigation.getChildAt(0);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        toolbar.setTitle("Shop");
       // MainActivity.this.setTitle("shop");

        for (int i = 0; i < menuView.getChildCount(); i++) {
            final View iconView = menuView.getChildAt(i).findViewById(com.google.android.material.R.id.icon);
            final ViewGroup.LayoutParams layoutParams = iconView.getLayoutParams();
            final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            // set your height here
            layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 42, displayMetrics);
            // set your width here
            layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 42, displayMetrics);
            iconView.setLayoutParams(layoutParams);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        MenuItem homeItem = navigation.getMenu().getItem(0);
        if(navigation.getSelectedItemId () != R.id.navigation_shop)
        {
            navigation.setSelectedItemId(R.id.navigation_shop);
        }
        else
        {
            super.onBackPressed();
        }
        return true;
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_shop:
                    toolbar.setTitle("Plant Disease Detection");
                    fragment = new FirstFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_gifts:
                    toolbar.setTitle("Calender");
                    fragment = new SecondFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_cart:
                    toolbar.setTitle("Side Businesses");
                    fragment = new ThirdFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_profile:
                    toolbar.setTitle("Other");
                    fragment = new FourthFragment();
                    loadFragment(fragment);
                    return true;

            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void someEvent(Location l) {
       // Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
        loc=l;
    }

}