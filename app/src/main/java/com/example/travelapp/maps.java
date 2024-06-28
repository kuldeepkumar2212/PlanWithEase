package com.example.travelapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class maps extends AppCompatActivity {

    EditText start,destination;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        start = findViewById(R.id.start);
        destination = findViewById(R.id.destination);
        submit = findViewById(R.id.button);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Geocoder geocoder = new Geocoder(maps.this);
                List<Address> addressList = new ArrayList<>();
                try {
                    addressList.addAll( geocoder.getFromLocationName(start.getText().toString(),1));
                    addressList.addAll( geocoder.getFromLocationName(destination.getText().toString(),1));
                    if(addressList!=null){
                        double startlat= addressList.get(0).getLatitude();
                        double startlon = addressList.get(0).getLongitude();
                        double destlat= addressList.get(1).getLatitude();
                        double destlon = addressList.get(1).getLongitude();
                        System.out.println("start "+ startlat+"  "+startlon);
                        System.out.println("destination "+ destlat+"  "+destlon);
                        /*Uri gmmIntentUri = Uri.parse("http://maps.google.com/maps?saddr=" + startlat + "," + startlon + "&daddr=" + destlat + "," + destlon);

                        // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);

                        // Specify the package to use (Google Maps)
                        mapIntent.setPackage("com.google.android.apps.maps");

                        // Verify that the Google Maps app is installed before starting the intent
                        if (mapIntent.resolveActivity(getPackageManager()) != null) {
                            startActivity(mapIntent);
                        }*/

                        String mapUri = "https://maps.google.com/maps?saddr=" + startlat + "," + startlon + "&daddr=" + destlat + "," + destlon;
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mapUri));
                        startActivity(intent);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        });


    }
}