package com.alumaworks.udayanga.diseaseanalytic;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.Parse;
import com.parse.ParseObject;

public class SignupActivity extends ActionBarActivity {

    EditText editTextName,editTextEmail,editTextArea,editTextUserName;
    Button buttonSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_signup, menu);
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
    public void onRegisterButtonClick(View view){

        Parse.initialize(this, "eHVWp9eSv1VnZqMnIKGAvTnnADc8EeaGdVe0SH41", "m4UFyOkG7fM9kqE9r1Gt1ffLe7IpbLgY3V3Fcbgt");

        editTextName=(EditText)findViewById(R.id.editTextName);
        editTextEmail=(EditText)findViewById(R.id.editTextEmail);
        editTextArea=(EditText)findViewById(R.id.editTextArea);
        editTextUserName=(EditText)findViewById(R.id.editTextUserName);

        String name = editTextName.getText().toString();
        String email = editTextEmail.getText().toString();
        String area = editTextArea.getText().toString();
        String username=editTextUserName.getText().toString();

        ParseObject userObject = new ParseObject("requested_account");
        userObject.put("name", name);
        userObject.put("e_mail", email);
        userObject.put("area", area);
        userObject.put("user_name",username);
        userObject.saveInBackground();

        Log.d("REGISTER", "Name : " + name + ", E Mail : " + email +
                ", Area : " + area+"User Name :"+username);
    }

}
