package com.example.calender_final;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    String str;
    final int STORAGE_PERMISSION_CODE =1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText ed1;
        ed1=findViewById(R.id.ed);
        final TextView tv1;
        tv1=findViewById(R.id.tv);
        Button bt;
        bt=findViewById(R.id.button);
        str=ed1.getText().toString();
     /*   bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!tv1.getText().toString().isEmpty()){
                    Intent intent=new Intent(Intent.ACTION_INSERT);
                    intent.setData(CalendarContract.Events.CONTENT_URI);
                    intent.putExtra(CalendarContract.Events.TITLE,ed1.getText().toString());
                    if (intent.resolveActivity(getPackageManager())!= null) {
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(MainActivity.this,"There is no calendar app installed here",Toast.LENGTH_SHORT).show();

                    }
                }
                else{
                    Toast.makeText(MainActivity.this,"Enter Event Name",Toast.LENGTH_SHORT).show();
                }
            }
        });*/
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
                {
                    if(checkSelfPermission(Manifest.permission.READ_CALENDAR)== PackageManager.PERMISSION_DENIED ||
                            checkSelfPermission(Manifest.permission.WRITE_CALENDAR)==PackageManager.PERMISSION_DENIED
                    ){
                        String[] permission = {Manifest.permission.WRITE_CALENDAR,Manifest.permission.READ_CALENDAR};
                        requestPermissions(permission,STORAGE_PERMISSION_CODE);
                    }
                    else{
                        openCalendar(ed1.getText().toString());
                    }
                }
                else{
                    openCalendar(ed1.getText().toString());
                }
            }


        });


    }

    private void openCalendar(String s1) {
        if(!s1.isEmpty()){
            Intent intent=new Intent(Intent.ACTION_INSERT);
            intent.setData(CalendarContract.Events.CONTENT_URI);
            intent.putExtra(CalendarContract.Events.TITLE,s1);
            if (intent.resolveActivity(getPackageManager())!= null) {
                startActivity(intent);
            }
            else{
                Toast.makeText(MainActivity.this,"There is no calendar app installed here",Toast.LENGTH_SHORT).show();

            }
        }
        else if(s1.isEmpty()){
            Toast.makeText(MainActivity.this,"Enter Event Name",Toast.LENGTH_SHORT).show();
        }
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case STORAGE_PERMISSION_CODE:{
                if(grantResults.length>0 &&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    if(!str.isEmpty()){
                    openCalendar(str);}
                    else if(str.isEmpty()){
                        Toast.makeText(MainActivity.this,"Enter Event Name",Toast.LENGTH_SHORT).show();
                    }

                }
                else {
                    Toast.makeText(this,"Permission denied",Toast.LENGTH_SHORT).show();
                }
            }

        }
    }
}