package com.example.weatherdetection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements LocationListener {
    TextView tv, weather, city, sunset;
    String url = "api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={your api key}";
    String api = "66f6506457a0509978fe47a89d8f9188";
    protected LocationManager locationManager;
    String lat;
    String provider;
    ImageView img;
    protected String latitude="80", longitude="80";
    private static final int STORAGE_PERMISSION_CODE =1000;
    Uri image_uri;
    private static final int IMAGE_CAPTURE_CODE=1001, IMAGE_PICK_CODE=1000, PERMISSION_CODE=100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.tv);
        weather = (TextView) findViewById(R.id.weather);
        city = (TextView) findViewById(R.id.city);
        sunset = (TextView) findViewById(R.id.sunset);
        img=(ImageView)findViewById(R.id.image);
        //Runtime permissions
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            },100);
        }


        try {

            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            // locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,5,MainActivity.this);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, MainActivity.this);
            locationManager. requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, MainActivity.this);
            Location oldLocation = locationManager.getLastKnownLocation(provider);

            if (oldLocation != null)  {
                // Log.v(TAG, "Got Old location");
                latitude = Double.toString(oldLocation.getLatitude());
                longitude = Double.toString(oldLocation.getLongitude());

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        // connect to the GPS location service


        Retrofit retrofit=new Retrofit.Builder().baseUrl("https://api.openweathermap.org/data/2.5/").addConverterFactory(GsonConverterFactory.create()).build();
        weatherinterface wi=retrofit.create(weatherinterface.class);
        Call<Example> example= wi.getweather(latitude,longitude,api);
        example.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                if(response.code()==404)
                {
                    Toast.makeText(getApplicationContext(),"Invalid city",Toast.LENGTH_LONG).show();
                }
                else if(!(response.isSuccessful()))
                {
//                    Toast.makeText(getApplicationContext(),response.code(),Toast.LENGTH_LONG).show();
                }

                Example mydata=response.body();
                Main main=mydata.getMain();
                Double temp=main.getTemp();
                Integer temperature=(int)(temp-273.15);
                tv.setText(temperature + " \u2103");

                // ArrayList<Object> l=mydata.getWeather();
                //  Weather w= (Weather) l.get(0);
                String des=mydata.getWeather().get(0).getDescription();
              //  Toast.makeText(getApplicationContext(),mydata.sys.toString(),Toast.LENGTH_LONG).show();
                String ct=mydata.getSys().getCountry();
                double st=mydata.getSys().getSunset();

                //city.setText(stringLatitude+ ", "+stringLatitude);
                sunset.setText("sunset at "+ unixTimeStampToDateTime(mydata.getSys().getSunset()));
                weather.setText(des);

             //   Picasso.get()
               //         .load(getImage(mydata.getWeather().get(0).getIcon()))
                 //       .into(img);

            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"oops   "+t.getMessage(),Toast.LENGTH_LONG).show();
                Log.e("error",t.getMessage());
            }
        });


        ImageView buttonRequest = findViewById(R.id.button);
        ImageView mChooseBtn = findViewById(R.id.button2);
        buttonRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
                {
                    if(checkSelfPermission(Manifest.permission.CAMERA)==PackageManager.PERMISSION_DENIED ||
                            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_DENIED
                    ){
                        String[] permission = {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permission,STORAGE_PERMISSION_CODE);
                    }
                    else{
                        openCamera();
                    }
                }
                else{
                    openCamera();
                }
            }


        });
        mChooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_DENIED){
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions,PERMISSION_CODE);
                    }
                    else{
                        pickImageFromGallery();
                    }
                }
                else{
                    pickImageFromGallery();

                }
            }
        });

    }

    private void openCamera() {
        ContentValues values=new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION,"From camera");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        startActivityForResult(cameraIntent,IMAGE_CAPTURE_CODE);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case STORAGE_PERMISSION_CODE:{
                if(grantResults.length>0 &&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    openCamera();
                }
                else {
                    Toast.makeText(this,"Permission denied",Toast.LENGTH_SHORT).show();
                }
            }
            break;
            case PERMISSION_CODE:{
                if(grantResults.length>0 &&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    pickImageFromGallery();
                }
                else {
                    Toast.makeText(this,"Permission denied",Toast.LENGTH_SHORT).show();
                }
            }
            break;



        }
    }
    @SuppressLint("LongLogTag")
    public static String getImage(String icon){
        Log.e("hhhhhhhhhhhhhhhhhhhhhhhhhhhh",String.format("http://openweathermap.org/img/w/%s.png",icon));
        return String.format("http://openweathermap.org/img/w/%s.png",icon);
    }
    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_PICK_CODE);
    }

    public static String unixTimeStampToDateTime(double unixTimeStamp){
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Date date = new Date();
        date.setTime((long)unixTimeStamp*1000);
        return dateFormat.format(date);
    }
    @SuppressLint("MissingPermission")
    public void getWeather(View v){


    }

    @Override
    public void onLocationChanged(@NonNull final Location location) {

        latitude= String.valueOf(location.getLatitude());
        longitude= String.valueOf(location.getLongitude());
       // Toast.makeText(getApplicationContext(),latitude+" "+longitude,Toast.LENGTH_LONG).show();
       // Log.e("hhhhhhhhhhhhhhhhhhhhhhh",latitude+" "+longitude);

        Retrofit retrofit=new Retrofit.Builder().baseUrl("https://api.openweathermap.org/data/2.5/").addConverterFactory(GsonConverterFactory.create()).build();
        weatherinterface wi=retrofit.create(weatherinterface.class);
        Call<Example> example= wi.getweather(latitude,longitude,api);
        example.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                if(response.code()==404)
                {
                    Toast.makeText(getApplicationContext(),"Invalid city",Toast.LENGTH_LONG).show();
                }
                else if(!(response.isSuccessful()))
                {
//                    Toast.makeText(getApplicationContext(),response.code(),Toast.LENGTH_LONG).show();
                }
                Geocoder gcd = new Geocoder(getApplicationContext(), Locale.getDefault());
                List<Address> addresses = null;
                try {
                    addresses = gcd.getFromLocation(location.getLatitude(),location.getLongitude(), 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Example mydata=response.body();
                Main main=mydata.getMain();
                Double temp=main.getTemp();
                Integer temperature=(int)(temp-273.15);
                tv.setText(temperature + " \u2103");

                // ArrayList<Object> l=mydata.getWeather();
                //  Weather w= (Weather) l.get(0);
                String des=mydata.getWeather().get(0).getDescription();
         //       Toast.makeText(getApplicationContext(),mydata.sys.toString(),Toast.LENGTH_LONG).show();
                String ct=mydata.getSys().getCountry();
                double st=mydata.getSys().getSunset();



                if (addresses.size() > 0) {
                   // System.out.println();
                    city.setText(addresses.get(0).getLocality() + ", "+ct);
                }
                else {
                    // do your stuff
                }
                sunset.setText("sunset at "+ unixTimeStampToDateTime(mydata.getSys().getSunset()));
                weather.setText(des);

            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"oops   "+t.getMessage(),Toast.LENGTH_LONG).show();
                Log.e("error",t.getMessage());
            }

        });

    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }
}