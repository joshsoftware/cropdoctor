package com.example.cropdoctor;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//import static com.example.cropdoctor.Classifier.LOGGER;

public class CropDetection extends AppCompatActivity  {

    private List<Movie> movieList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MoviesAdapter mAdapter;
    private static final Logger LOGGER = new Logger();
    // Define the pic id
    private static final int pic_id = 123;
    private Classifier classifier;
    // Define the button and imageview type variable
    ImageView camera_open_id;
    ImageView gallery;
    ImageView click_image_id;
    private Bitmap photo = null;
    private Integer sensorOrientation=100;
    public static final int MAX_LINES = 3;
    public static final String TWO_SPACES = " ";
    Context context;
    ReadMoreTextView textView;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_crop_detection);

        Bundle extras = getIntent().getExtras();
        byte[] byteArray = extras.getByteArray("EXTRA_SESSION_ID");

        Bitmap photo = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

             click_image_id=findViewById(R.id.img1);
             textView=(ReadMoreTextView)findViewById(R.id.txt1);
            // BitMap is data structure of image file
            // which stor the image in memory
        //windowActionBar=false
       Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);

     //   getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       // getSupportActionBar().setDisplayShowHomeEnabled(true);




            Classifier.Device device = Classifier.Device.CPU;
            final int  numThreads = -1;
            if (classifier != null) {
                classifier.close();
                classifier = null;
            }
            try {
                LOGGER.d(
                        "Creating classifier (device=%s, numThreads=%d)", device, numThreads);
                classifier = Classifier.create(this, device, numThreads);

                final List<Classifier.Recognition> results =
                        classifier.recognizeImage(photo, sensorOrientation);

                if (results != null && results.size() >= 3) {
                    Classifier.Recognition recognition = results.get(0);
                    if (recognition != null) {
                        if (recognition.getTitle() != null) {
                            Toast.makeText(getApplicationContext(), recognition.getTitle(), Toast.LENGTH_LONG).show();
                            getSupportActionBar().setTitle(recognition.getTitle());
                        }
                        if (recognition.getConfidence() != null)
                            Toast.makeText(getApplicationContext(),String.format("%.2f", (100 * recognition.getConfidence())),Toast.LENGTH_LONG).show();

                    }
                    // Set the image in imageview for display
                    click_image_id.setImageBitmap(photo);

                } }catch (IOException e) {
                LOGGER.e(e, "Failed to create classifier.");
                Toast.makeText(getApplicationContext(),"Failed to create classifier."+e,Toast.LENGTH_LONG).show();
            }



        final String myReallyLongText = "In general, a plant becomes diseased when it is continuously disturbed by some causal agent that results in an abnormal physiological process that disrupts the plant’s normal structure, growth, function, or other activities. This interference with one or more of a plant’s essential physiological or biochemical systems elicits characteristic pathological conditions or symptoms.\n" +
                "\n";

        textView.setText(myReallyLongText);
        textView.setColorClickableText(R.color.colorPrimaryDark)
        ;


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new MoviesAdapter(movieList);

        recyclerView.setHasFixedSize(true);

        // vertical RecyclerView
        // keep movie_list_row.xml width to `match_parent`
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        // horizontal RecyclerView
        // keep movie_list_row.xml width to `wrap_content`
        // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setLayoutManager(mLayoutManager);

        // adding inbuilt divider line
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        // adding custom divider line with padding 16dp
        // recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.HORIZONTAL, 16));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(mAdapter);

        // row click listener
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Movie movie = movieList.get(position);
                Toast.makeText(getApplicationContext(), movie.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        prepareMovieData();



    }

    private void prepareMovieData() {
        Movie movie = new Movie("Safex Chemicals", "Fungicide", "2045 Rs");
        movieList.add(movie);

        movie = new Movie("Cureal", "Fungicide", "343 Rs");
        movieList.add(movie);

        movie = new Movie("Valueman", "Organic Pest Control", "200 Rs");
        movieList.add(movie);

        movie = new Movie("Folio Gold", "Fungicide", "450 Rs");
        movieList.add(movie);

        movie = new Movie("BLAST OFF -TRICYCLOZOLE", "Fungicide", "220 Rs");
        movieList.add(movie);

        movie = new Movie("Plantomycin", "Bactericide", "775 Rs");
        movieList.add(movie);

        movie = new Movie("Curzate", "Fungicide", "2045 Rs");
        movieList.add(movie);

        movie = new Movie("Amistar", "Fungicide", "200 Rs");
        movieList.add(movie);



              // notify adapter about data set changes
        // so that it will render the list with new data
        mAdapter.notifyDataSetChanged();
    }

        }