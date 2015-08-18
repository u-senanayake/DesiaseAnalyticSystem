package com.alumaworks.udayanga.diseaseanalytic;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;


public class LogingActivity extends ActionBarActivity {

    EditText editTextUserName,editTextPassword;
    Button login_button,sign_up_button;
    SharedPreferences sharedPreferences;
    private boolean connection=false;
    private ProgressBar progressBar=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loging);
        sharedPreferences = getSharedPreferences("UserData",MODE_PRIVATE);

        editTextUserName=(EditText)findViewById(R.id.editTextUserName);
        editTextPassword=(EditText)findViewById(R.id.editTextPassword);
        progressBar =(ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        connection=isInternetConnected(getApplication());
        if(connection){
            Login();
        }
        else {
          AlertDialog.Builder builder=new AlertDialog.Builder(LogingActivity.this);
            builder.setMessage("Please enable data connection")
                    .setCancelable(false)
                    .setTitle("Data Connection Status")
                    .setPositiveButton("Turn ON",
                            new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialog,int id){
                                    Intent myIntent=new Intent(
                                            Settings.ACTION_WIRELESS_SETTINGS);
                                    startActivity(myIntent);
                                    dialog.cancel();
                                }
                            })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialog,int id){
                                    dialog.cancel();
                                    Login();
                                }
                            });
            AlertDialog alert=builder.create();
            alert.show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_loging, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClickLoginButton(View view) {
        progressBar.setVisibility(View.VISIBLE);
        connection=isInternetConnected(getApplication());
        if(connection){
            Log.d("LOGIN", "Login button was called");

            Parse.initialize(this, "eHVWp9eSv1VnZqMnIKGAvTnnADc8EeaGdVe0SH41", "m4UFyOkG7fM9kqE9r1Gt1ffLe7IpbLgY3V3Fcbgt");

            editTextUserName=(EditText)findViewById(R.id.editTextUserName);
            editTextPassword=(EditText)findViewById(R.id.editTextPassword);

            final String user_name = editTextUserName.getText().toString();
            final String password = editTextPassword.getText().toString();

            Log.d("LOGIN", "Username : " + user_name + ", Password : " + password);
            ParseQuery<ParseObject> query = ParseQuery.getQuery("user");
            query.whereEqualTo("user_name", user_name);

            query.getFirstInBackground(new GetCallback<ParseObject>() {
                public void done(ParseObject object, ParseException e) {
                    if (object == null) {
                        Toast.makeText(getApplicationContext(), "Object is null", Toast.LENGTH_SHORT).show();
                    } else {
                        String testUserName=object.getString("user_name");
                        String testPassword=object.getString("password");
                        if(testUserName.equals(user_name)&&testPassword.equals(password)) {
                            Intent intent = new Intent(LogingActivity.this, MapsActivity.class);//call the second acyivity
                            //create xml to save data
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            editor.putBoolean("hasloggedinPreviously",true);
                            editor.putString("username",user_name);
                            editor.commit();
                            startActivity(intent);
                        }else{
                            Toast.makeText(getApplicationContext(), "Wrong username & password", Toast.LENGTH_SHORT).show();

                        }
                    }
                }
            });



        }
        else {

            progressBar.setVisibility(View.INVISIBLE);
            AlertDialog.Builder builder=new AlertDialog.Builder(LogingActivity.this);
            builder.setMessage("Please enable data connection")
                    .setCancelable(false)
                    .setTitle("Data Connection Status")
                    .setPositiveButton("Turn ON",
                            new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialog,int id){
                                    Intent myIntent=new Intent(
                                            Settings.ACTION_WIRELESS_SETTINGS);
                                    startActivity(myIntent);
                                    dialog.cancel();

                                }
                            })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialog,int id){
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert=builder.create();
            alert.show();
        }
    }
    public void onClickSignUpButton(View view) {
        Intent intent = new Intent(LogingActivity.this, SignupActivity.class);
        startActivity(intent);

    }

    public static boolean isInternetConnected (Context ctx) {
        ConnectivityManager connectivityMgr = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connectivityMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        // Check if wifi or mobile network is available or not. If any of them is
        // available or connected then it will return true, otherwise false;
        if (wifi != null) {
            if (wifi.isConnected()) {
                return true;
            }
        }
        if (mobile != null) {
            if (mobile.isConnected()) {
                return true;
            }
        }
        return false;
    }
    private  void Login(){
        boolean hasloggedinPreviously=sharedPreferences.getBoolean("hasloggedinPreviously",false);
        String username=sharedPreferences.getString("username","dummyUsername");
        if(hasloggedinPreviously){
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
            finish();

        }}

}
