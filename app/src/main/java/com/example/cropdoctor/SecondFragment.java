package com.example.cropdoctor;



import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.util.Objects;

public class SecondFragment extends Fragment {

    String str;
    final int STORAGE_PERMISSION_CODE =1000;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View RootView = inflater.inflate(R.layout.fragment_second, container, false);
        final EditText ed1;
        ed1=RootView.findViewById(R.id.ed);
        final TextView tv1;

        Button bt;
        bt=RootView.findViewById(R.id.button);
        str=ed1.getText().toString();

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
                {
                    if(ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.READ_CALENDAR)== PackageManager.PERMISSION_DENIED ||
                            ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.WRITE_CALENDAR)==PackageManager.PERMISSION_DENIED
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
        return RootView;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    private void openCalendar(String s1) {
        if(!s1.isEmpty()){
            Intent intent=new Intent(Intent.ACTION_INSERT);
            intent.setData(CalendarContract.Events.CONTENT_URI);
            intent.putExtra(CalendarContract.Events.TITLE,s1);
            if (intent.resolveActivity(getActivity().getPackageManager())!= null) {
                startActivity(intent);
            }
            else{
                Toast.makeText(getContext(),"There is no calendar app installed here",Toast.LENGTH_SHORT).show();

            }
        }
        else if(s1.isEmpty()){
            Toast.makeText(getContext(),"Enter Event Name",Toast.LENGTH_SHORT).show();
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case STORAGE_PERMISSION_CODE:{
                if(grantResults.length>0 &&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    if(!str.isEmpty()){
                        openCalendar(str);}
                    else if(str.isEmpty()){
                        Toast.makeText(getContext(),"Enter Event Name",Toast.LENGTH_SHORT).show();
                    }

                }
                else {
                    Toast.makeText(getContext(),"Permission denied",Toast.LENGTH_SHORT).show();
                }
            }

        }
    }
}