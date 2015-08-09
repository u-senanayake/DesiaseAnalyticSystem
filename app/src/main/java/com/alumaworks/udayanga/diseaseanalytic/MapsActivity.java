package com.alumaworks.udayanga.diseaseanalytic;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.Parse;
import com.parse.ParseObject;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private RatingBar ratingBar;
    private Button buttonGetLocation,buttonSendLocation;
    private EditText editLattitude,editTextLongitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
   }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

    public void onGetLocationButtonClickListner(View view){
        ratingBar=(RatingBar)findViewById(R.id.ratingBar);
        editLattitude=(EditText)findViewById(R.id.editLattitude);
        editTextLongitude=(EditText)findViewById(R.id.editTextLongitude);

        buttonGetLocation=(Button)findViewById(R.id.buttonGetLocation);
        buttonGetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MapsActivity.this, String.valueOf(
                        "Rating :"+ratingBar.getRating())+
                        "\nLatitude :"+editLattitude.getText().toString()+
                        "\nLongitude :"+editTextLongitude.getText().toString()
                        , Toast.LENGTH_SHORT).show();

            }
        });
    }
    public void onSendButtonClick(View view){
        Parse.initialize(this, "eHVWp9eSv1VnZqMnIKGAvTnnADc8EeaGdVe0SH41", "m4UFyOkG7fM9kqE9r1Gt1ffLe7IpbLgY3V3Fcbgt");
        AlertDialog.Builder builder=new AlertDialog.Builder(MapsActivity.this);
        builder.setMessage("Send data to the system?"+"Rating :"+ratingBar.getRating()+
                "\n Lattitude :"+editLattitude.getText().toString()+
                "\n Longitude :"+editTextLongitude.getText().toString())
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String rating=String.valueOf(ratingBar.getRating());
                        String latitude = editLattitude.getText().toString();
                        String longitude = editTextLongitude.getText().toString();

                        ParseObject userObject = new ParseObject("location");
                        userObject.put("rating",rating);
                        userObject.put("latitude", latitude);
                        userObject.put("longitude", longitude);
                        userObject.saveInBackground();

                        Log.d("REGISTER", "Latitude : " + latitude + ", Longitude : " + longitude);
                        Toast.makeText(MapsActivity.this,"Successfully send",Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog=builder.create();
        alertDialog.setTitle("Alert");
        alertDialog.show();

    }
}
