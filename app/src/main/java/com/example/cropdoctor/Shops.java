package com.example.cropdoctor;


import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Shops extends AppCompatActivity {
        TextView t1,t2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shops);
        Bundle bundle = getIntent().getExtras();
        Double lat=bundle.getDouble("dataone");
        Double lon=bundle.getDouble("datatwo");
        t1=findViewById(R.id.lat);
        t2=findViewById(R.id.lon);
        Toast.makeText(getApplicationContext(),lat+"\n"+lon,Toast.LENGTH_LONG).show();
        t1.setText(String.valueOf(lat));
        t2.setText(String.valueOf(lon));
    }
}