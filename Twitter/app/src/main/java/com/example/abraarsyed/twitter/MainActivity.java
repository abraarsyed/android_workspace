package com.example.abraarsyed.twitter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {


    Button _loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences prefs = getSharedPreferences("codelearn_twitter", MODE_PRIVATE);
        String savedUsername = prefs.getString("key_for_username", null);
        String savedPassword = prefs.getString("key_for_password", null);

        //check if the current Activity needs to be skipped {
        //if(savedUsername != null && savedPassword != null) {
          //  Intent intent = new Intent(this, TweetListActivity.class);
            //startActivity(intent);
            //finish();
        //}
        //}

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _loginBtn = ( Button ) findViewById(R.id.btn_login);
        _loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //This is a comment which does no good to your code. Feel free to remove it after you copy paste.
                //When the button is clicked, the control will come to this method.
                //To demonstrate this, let us try changing the label of the Button from 'Login' to 'I am clicked'


                EditText username = ( EditText ) findViewById(R.id.fld_username);
                EditText password = ( EditText ) findViewById(R.id.fld_pwd);

                String usernameValue = username.getText().toString();
                String passwordValue = password.getText().toString();

                Log.d("Codelearn", "username caught - " + usernameValue);
                Log.d("Codelearn", "password caught - " + passwordValue);

                SharedPreferences prefs = getSharedPreferences("codelearn_twitter", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("key_for_username", usernameValue);
                editor.putString("key_for_password",passwordValue);
                editor.commit();

                //Add the following lines
                if(usernameValue.isEmpty() || passwordValue.isEmpty()) {

                    Toast.makeText(MainActivity.this,"Fill both the fields",Toast.LENGTH_LONG).show();

                }
                else {
                    Intent intent;
                    intent = new Intent(MainActivity.this, TweetListActivity.class);
                    startActivity(intent);

                }

            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
