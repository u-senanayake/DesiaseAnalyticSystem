package com.alumaworks.udayanga.diseaseanalytic;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.Parse;
import com.parse.ParseObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private RatingBar ratingBar;
    private Button buttonGetLocation, buttonSendLocation;
    private EditText editLattitude, editTextLongitude;
    private TextView city;

    //get location variables
    private LocationManager locationManager = null;
    private LocationListener locationListener = null;
    private ProgressBar progressBar = null;

    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
        //set display always portrait
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //start progressbar will not work
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        addItemSpinner();



    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.


            //voided because map was removed
         //   mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
           //         .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

    public void onGetLocationButtonClickListner(View view) {
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        editLattitude = (EditText) findViewById(R.id.editLattitude);
        editTextLongitude = (EditText) findViewById(R.id.editTextLongitude);
        buttonGetLocation = (Button) findViewById(R.id.buttonGetLocation);
        buttonGetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check GPS enable
                ContentResolver contentResolver = getBaseContext().getContentResolver();
                boolean gpgStatus = Settings.Secure.isLocationProviderEnabled(contentResolver, LocationManager.GPS_PROVIDER);

                if (gpgStatus) {//if GPS enable
                    progressBar.setVisibility(View.VISIBLE);//start progress bar working
                    locationListener = new getMyLocation();//call to getMyLocation class
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);



                } else {//GPS enable Aleart
                    AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                    builder.setMessage("Please enable GPS")
                            .setCancelable(false)
                            .setTitle("GPS Status")
                            .setPositiveButton("Turn ON",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Intent myIntent = new Intent(
                                                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                            startActivity(myIntent);
                                            dialog.cancel();

                                        }
                                    })
                            .setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });
    }

    public void onSendButtonClick(View view) {//send data to online database
        Parse.initialize(this, "eHVWp9eSv1VnZqMnIKGAvTnnADc8EeaGdVe0SH41", "m4UFyOkG7fM9kqE9r1Gt1ffLe7IpbLgY3V3Fcbgt");
        AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
        builder.setMessage("Send data to the system?" +
                "\nRating :" + ratingBar.getRating() +
                "\n Lattitude :" + editLattitude.getText().toString() +
                "\n Longitude :" + editTextLongitude.getText().toString()+
                "\n Disease :"+spinner.getSelectedItem().toString())
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //get time and date
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmm");
                        String currentDateAndTime = simpleDateFormat.format(new Date());

                        String rating = String.valueOf(ratingBar.getRating());
                        String latitude = editLattitude.getText().toString();
                        String longitude = editTextLongitude.getText().toString();
                        String disease=spinner.getSelectedItem().toString();

                        ParseObject userObject = new ParseObject("location");
                        userObject.put("rating", rating);
                        userObject.put("latitude", latitude);
                        userObject.put("longitude", longitude);
                        userObject.put("disease",disease);
                        userObject.put("date",currentDateAndTime);
                        userObject.saveInBackground();

                        Toast.makeText(MapsActivity.this, "Successfully send", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.setTitle("Alert");
        alertDialog.show();

    }

    private class getMyLocation implements LocationListener {
        private Location location;

        @Override
        public void onLocationChanged(Location location) {
            city=(TextView)findViewById(R.id.cityName);
            this.location = location;
            progressBar.setVisibility(View.INVISIBLE);
            //set edit text's text to latitude and longitude

            String latitude=String.valueOf(location.getLatitude());
            String longitude=String.valueOf(location.getLongitude());

            editLattitude.setText(latitude);
            editTextLongitude.setText(longitude);

            Toast.makeText(MapsActivity.this,"\nLatitude :" + latitude +
                    "\nLongitude :" + longitude
                    , Toast.LENGTH_SHORT).show();
//city name
            String cityName = null;
            Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
            List<Address> addresses;
            try {
                addresses = gcd.getFromLocation(location.getLatitude(),
                        location.getLongitude(), 1);
                if (addresses.size() > 0)
                    System.out.println(addresses.get(0).getLocality());
                cityName = addresses.get(0).getLocality();
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            city.setText(cityName);


        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    }

   private void addItemSpinner(){
       spinner = (Spinner) findViewById(R.id.spinner);
       List<String> list = new ArrayList<String>();
       list.add("<Select Item>");
       list.add("Dengue");
       list.add("Malaria");
       list.add("list 3");
       ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
               android.R.layout.simple_spinner_item, list);
       dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       spinner.setAdapter(dataAdapter);

   }

}