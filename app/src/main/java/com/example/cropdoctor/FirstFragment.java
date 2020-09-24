package com.example.cropdoctor;


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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class FirstFragment extends Fragment{


    TextView tv, weather, city, sunset;
    String api = "66f6506457a0509978fe47a89d8f9188";
    ImageView img;
    private static final int STORAGE_PERMISSION_CODE =1000;
    Uri image_uri;
    private static final int IMAGE_CAPTURE_CODE=1001, IMAGE_PICK_CODE=1000, PERMISSION_CODE=100;
    Location location; // location

    double x, y;
    Timer timer;
    LocationManager lm;
    boolean gps_enabled = false;
    boolean network_enabled = false;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View RootView = inflater.inflate(R.layout.fragment_first, container, false);
        tv = (TextView) RootView.findViewById(R.id.tv);
        weather = (TextView) RootView.findViewById(R.id.weather);
        city = (TextView) RootView.findViewById(R.id.city);
        sunset = (TextView) RootView.findViewById(R.id.sunset);
        img = (ImageView) RootView.findViewById(R.id.image);


        lm = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);


        gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);


        if (!gps_enabled && !network_enabled) {
            Context context = getContext();
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, "nothing is enabled", duration);
            toast.show();

        }



        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }
    if (gps_enabled)
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
                    locationListenerGps);
        if (network_enabled)
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,
                    locationListenerNetwork);
        timer = new Timer();
        timer.schedule(new GetLastLocation(), 20000);

        ImageView buttonRequest = RootView.findViewById(R.id.button);
        ImageView mChooseBtn =RootView. findViewById(R.id.button2);
        buttonRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
                {
                    if(getActivity().checkSelfPermission(Manifest.permission.CAMERA)==PackageManager.PERMISSION_DENIED ||
                           getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_DENIED
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
                    if(getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_DENIED){
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

            return RootView;
        }

    LocationListener locationListenerGps = new LocationListener() {
        public void onLocationChanged(Location location) {
            timer.cancel();
            x = location.getLatitude();
            y = location.getLongitude();
            getweather(x,y);
            lm.removeUpdates(this);
            lm.removeUpdates(locationListenerNetwork);

            Context context = getContext();
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, "gps enabled " + x + "\n" + y, duration);
            toast.show();
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };
    LocationListener locationListenerNetwork = new LocationListener() {
        public void onLocationChanged(Location location) {
            timer.cancel();
            x = location.getLatitude();
            y = location.getLongitude();
            getweather(x,y);
            lm.removeUpdates(this);
            lm.removeUpdates(locationListenerGps);

            Context context = getContext();
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, "network enabled" + x + "\n" + y, duration);
            toast.show();
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    class GetLastLocation extends TimerTask {
        @Override
        public void run() {
            lm.removeUpdates(locationListenerGps);
            lm.removeUpdates(locationListenerNetwork);

            Location net_loc = null, gps_loc = null;
            if (gps_enabled)
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }gps_loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(network_enabled)
                net_loc=lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            //if there are both values use the latest one
            if(gps_loc!=null && net_loc!=null){
                if(gps_loc.getTime()>net_loc.getTime())
                {x = gps_loc.getLatitude();
                    y = gps_loc.getLongitude();
                    getweather(x,y);
                    Context context = getContext();
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, "gps lastknown "+x + "\n" + y, duration);
                    toast.show();
                }
                else
                {x = net_loc.getLatitude();
                    y = net_loc.getLongitude();
                    getweather(x,y);
                    Context context = getContext();
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, "network lastknown "+x + "\n" + y, duration);
                    toast.show();

                }

            }

            if(gps_loc!=null){
                {x = gps_loc.getLatitude();
                    y = gps_loc.getLongitude();
                    getweather(x,y);
                    Context context = getContext();
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, "gps lastknown "+x + "\n" + y, duration);
                    toast.show();
                }

            }
            if(net_loc!=null){
                {x = net_loc.getLatitude();
                    y = net_loc.getLongitude();
                    getweather(x,y);
                    Context context = getContext();
                    int duration = Toast.LENGTH_SHORT;
                    //   Toast toast = Toast.makeText(context, "network lastknown "+x + "\n" + y, duration);
                    // toast.show();

                }
            }
            Context context = getContext();
            int duration = Toast.LENGTH_SHORT;
            // Toast toast = Toast.makeText(context, "no last know avilable", duration);
            //toast.show();

        }
    }
    public void onViewCreated (@NonNull View view, Bundle savedInstanceState){
            super.onViewCreated(view, savedInstanceState);


        }


        private void openCamera () {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, "New Picture");
            values.put(MediaStore.Images.Media.DESCRIPTION, "From camera");
            image_uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
            startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);
        }


        @Override
        public void onRequestPermissionsResult ( int requestCode, @NonNull String[] permissions,
        @NonNull int[] grantResults){
            switch (requestCode) {
                case STORAGE_PERMISSION_CODE: {
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        openCamera();
                    } else {
                        Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
                case PERMISSION_CODE: {
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        pickImageFromGallery();
                    } else {
                        Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                    }
                }
                break;


            }
        }
        @SuppressLint("LongLogTag")
        public static String getImage (String icon){
            Log.e("hhhhhhhhhhhhhhhhhhhhhhhhhhhh", String.format("http://openweathermap.org/img/w/%s.png", icon));
            return String.format("http://openweathermap.org/img/w/%s.png", icon);
        }
        private void pickImageFromGallery () {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, IMAGE_PICK_CODE);
        }

        public static String unixTimeStampToDateTime ( double unixTimeStamp){
            DateFormat dateFormat = new SimpleDateFormat("HH:mm");
            Date date = new Date();
            date.setTime((long) unixTimeStamp * 1000);
            return dateFormat.format(date);
        }



    public void getweather(final double lat, final double lon){

        try {

            Toast.makeText(getContext(),String.valueOf(lat)+String.valueOf(lon),Toast.LENGTH_LONG).show();
            Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.openweathermap.org/data/2.5/").addConverterFactory(GsonConverterFactory.create()).build();
            weatherinterface wi = retrofit.create(weatherinterface.class);
            Call<Example> example = wi.getweather(String.valueOf(lat), String.valueOf(lon), api);
            example.enqueue(new Callback<Example>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(Call<Example> call, Response<Example> response) {
                    if (response.code() == 404) {
                        Toast.makeText(getContext(), "Invalid city", Toast.LENGTH_LONG).show();
                    } else if (!(response.isSuccessful())) {
//                    Toast.makeText(getApplicationContext(),response.code(),Toast.LENGTH_LONG).show();
                    }

                    Example mydata = response.body();
                    Main main = mydata.getMain();
                    Double temp = main.getTemp();
                    Integer temperature = (int) (temp - 273.15);
                    tv.setText(temperature + " \u2103");

                    // ArrayList<Object> l=mydata.getWeather();
                    //  Weather w= (Weather) l.get(0);
                    String des = mydata.getWeather().get(0).getDescription();
                    //  Toast.makeText(getApplicationContext(),mydata.sys.toString(),Toast.LENGTH_LONG).show();
                    String ct = mydata.getSys().getCountry();
                    Toast.makeText(getContext(), ct, Toast.LENGTH_LONG).show();
                    double st = mydata.getSys().getSunset();

                    //city.setText(stringLatitude+ ", "+stringLatitude);
                    sunset.setText("sunset at " + unixTimeStampToDateTime(st));
                    weather.setText(des);
                    Geocoder gcd = new Geocoder(getContext(), Locale.getDefault());
                    List<Address> addresses = null;
                    try {
                        addresses = gcd.getFromLocation(lat, lon, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (addresses.size() > 0) {
                        // System.out.println();
                        city.setText(addresses.get(0).getLocality() + ", " + ct);
                    }
                }
                @Override
                public void onFailure(Call<Example> call, Throwable t) {
                    Toast.makeText(getContext(), "oops   " + t.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("error", t.getMessage());
                }
                });


            }
        catch (Exception e) {
            e.printStackTrace();
        }
        // }
    }
}

