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

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText password;
    private Firebase firebase;
    private Button loginButton;
    Activity thisActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        thisActivity = this;

        //init Firebase
        Firebase.setAndroidContext(this); //global firebase context
        firebase = new Firebase("https://gopher-uima.firebaseIO.com"); //reference variable

        //style
        password = (EditText) findViewById(R.id.passwordText);
        password.setTypeface( Typeface.DEFAULT );

        //login button setup
        loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(login);

        //change to signup
        ((Button) findViewById(R.id.signupButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signupIntent = new Intent(thisActivity, SignupActivity.class);
                startActivity(signupIntent);
            }
        });

        //DELETE ME
        ((EditText) findViewById(R.id.emailText)).setText("gavi@gmail.com");
        ((EditText) findViewById(R.id.passwordText)).setText("test");

    }

    View.OnClickListener login = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String email = ((EditText) findViewById(R.id.emailText)).getText().toString();
            String password = ((EditText) findViewById(R.id.passwordText)).getText().toString();

            //validate
            if (email.equals("") | password.equals("")) {
                YoYo.with(Techniques.Shake)
                        .duration(700)
                        .playOn(loginButton);
            } else {
                firebase.authWithPassword(email, password, new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        System.out.println("User ID: " + authData.getUid() + ", Provider: " + authData.getProvider());
                        storeID(authData.getUid());
                        UserSwitcherActivity.chooseUI(getApplicationContext(), (thisActivity));
                    }
                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        YoYo.with(Techniques.Shake)
                                .duration(700)
                                .playOn(loginButton);
                    }
                });
            }
        }
    };

    //store user id in shared prefs for global access
    private void storeID(String id) {
        SharedPreferences myPrefs =  PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor peditor = myPrefs.edit();
        peditor.putString(Constants.USER_ID, id);
        peditor.commit();
    }



}
