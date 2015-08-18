package com.alumaworks.udayanga.diseaseanalytic;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseObject;

public class SignupActivity extends ActionBarActivity {

    EditText editTextName,editTextEmail,editTextArea,editTextUserName;
    Button buttonSignUp;
    TextView textViewName,textViewEmail,textViewArea,textViewUserName,textViewNameErr,textViewEmailError,textViewAreaError,textViewUserNameError;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        textViewNameErr=(TextView)findViewById(R.id.textViewNameError);
        textViewNameErr.setVisibility(View.INVISIBLE);
        textViewEmailError=(TextView)findViewById(R.id.textViewEmailErr);
        textViewEmailError.setVisibility(View.INVISIBLE);
        textViewAreaError=(TextView)findViewById(R.id.textViewAreaErro);
        textViewAreaError.setVisibility(View.INVISIBLE);
        textViewUserNameError=(TextView)findViewById(R.id.textViewUserNameError);
        textViewUserNameError.setVisibility(View.INVISIBLE);



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

        textViewNameErr=(TextView)findViewById(R.id.textViewNameError);
        textViewEmailError=(TextView)findViewById(R.id.textViewEmailErr);
        textViewAreaError=(TextView)findViewById(R.id.textViewAreaErro);
        textViewUserNameError=(TextView)findViewById(R.id.textViewUserNameError);

        textViewName=(TextView)findViewById(R.id.textViewName);
        textViewEmail=(TextView)findViewById(R.id.textViewEmail);
        textViewArea=(TextView)findViewById(R.id.textViewArea);
        textViewUserName=(TextView)findViewById(R.id.textViewUserName);

        String name = editTextName.getText().toString();
        String email = editTextEmail.getText().toString();
        String area = editTextArea.getText().toString();
        String username=editTextUserName.getText().toString();

        if(name.equals("")||name.equals(null)){
            textViewNameErr.setVisibility(View.VISIBLE);
            textViewName.setVisibility(View.INVISIBLE);
            textViewName.setText("");

        }
        else{
            textViewName.setVisibility(View.VISIBLE);
            textViewName.setText("Name");
            textViewNameErr.setVisibility(View.INVISIBLE);

        }
        if(email.equals("")||email.equals(null)){
            textViewEmailError.setVisibility(View.VISIBLE);
            textViewEmail.setVisibility(View.INVISIBLE);
            textViewEmail.setText("");
        }
        else{
            textViewEmailError.setVisibility(View.INVISIBLE);
            textViewEmail.setVisibility(View.VISIBLE);
            textViewEmail.setText("E-Mail");

        }
        if(area.equals("")||area.equals(null)){
            textViewAreaError.setVisibility(View.VISIBLE);
            textViewArea.setVisibility(View.INVISIBLE);
            textViewArea.setText("");
        }
        else{
            textViewAreaError.setVisibility(View.INVISIBLE);
            textViewArea.setVisibility(View.VISIBLE);
            textViewArea.setText("Area");

        }
        if(username.equals("") ||username.equals(null)){
            textViewUserNameError.setVisibility(View.VISIBLE);
            textViewUserName.setVisibility(View.INVISIBLE);
            textViewUserName.setText("");
        }
        else{
            textViewUserNameError.setVisibility(View.INVISIBLE);
            textViewUserName.setVisibility(View.VISIBLE);
            textViewUserName.setText("User Name");

        }
        if (name!=""&&email!=""&&area!=""&&username!="") {
            ParseObject userObject = new ParseObject("requested_account");
            userObject.put("name", name);
            userObject.put("e_mail", email);
            userObject.put("area", area);
            userObject.put("user_name", username);
            userObject.saveInBackground();

            Log.d("REGISTER", "Name : " + name + ", E Mail : " + email +
                    ", Area : " + area + "User Name :" + username);
            Toast.makeText(SignupActivity.this, "Successfully send", Toast.LENGTH_SHORT).show();
        }
    }

}
