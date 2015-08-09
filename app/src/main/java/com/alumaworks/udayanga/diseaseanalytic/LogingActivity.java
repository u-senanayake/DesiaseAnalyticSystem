package com.alumaworks.udayanga.diseaseanalytic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loging);

        sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        editTextUserName = (EditText) findViewById(R.id.editTextUserName);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        //make if user logged previosly,log automaticaly
        boolean hasloggedinPreviously = sharedPreferences.getBoolean("hasloggedinPreviously", false);
        String user_name = sharedPreferences.getString("user_name", "dummyUsername");
        if (hasloggedinPreviously) {
            Intent intent = new Intent(LogingActivity.this, MapsActivity.class);
            startActivity(intent);
            finish();

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
                    Log.d("score", "Retrieved the object.");
                    if(object.getString("password").equals(password)){
                        //save login data
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("hasLoggedinPreviously", true);
                        editor.putString("user_name", user_name);
                        editor.commit();
                        //start new activity
                        Intent intent = new Intent(LogingActivity.this, MapsActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(getApplicationContext(), "Wrong username & password", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });




    }
    public void onClickSignUpButton(View view) {
        Intent intent = new Intent(LogingActivity.this, SignupActivity.class);
        startActivity(intent);

    }
}
