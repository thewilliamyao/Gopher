package com.example.gavi.gopher;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;

//TODO: Add dialogue when user tries to create an exiting account

public class SignupActivity extends AppCompatActivity {

    private EditText password;
    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private Button signupButton;
    private Activity thisActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        thisActivity = this;
        Firebase.setAndroidContext(this);

        //init Edit texts
        password = (EditText) findViewById(R.id.passwordText);
        firstName = (EditText) findViewById(R.id.firstNameText);
        lastName = (EditText) findViewById(R.id.lastNameText);
        email = (EditText) findViewById(R.id.emailText);

        //style
        password = (EditText) findViewById(R.id.passwordText);
        password.setTypeface( Typeface.DEFAULT );

        //signup button setup
        signupButton = (Button) findViewById(R.id.signupButton);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });

        //change to login
        ((Button) findViewById(R.id.loginButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thisActivity.finish();
            }
        });
    }

    private boolean submit() {
        String emailStr = email.getText().toString();
        String passwordStr = password.getText().toString();
        String fname = firstName.getText().toString();
        String lname = lastName.getText().toString();

        //validate
        if (email.equals("") | password.equals("") || (fname.equals("") || (lname.equals("")))) {
            YoYo.with(Techniques.Shake)
                    .duration(700)
                    .playOn(signupButton);
            return false;
        }

        Intent intent = new Intent(this, SignupAddressActivity.class);
        intent.putExtra("email", emailStr);
        intent.putExtra("password", passwordStr);
        intent.putExtra("fname", fname);
        intent.putExtra("lname", lname);
        startActivity(intent);
        return true;
    }
}

