package com.example.gavi.gopher;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import mehdi.sakout.dynamicbox.DynamicBox;

public class LoginActivity extends AppCompatActivity {

    private EditText password;
    private Firebase firebase;
    private Button loginButton;
    private View background;
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
//        ((EditText) findViewById(R.id.emailText)).setText("gavi@gmail.com");
//        ((EditText) findViewById(R.id.passwordText)).setText("test");

        background = (View) findViewById(R.id.background);


    }

    @Override
    protected void onResume() {
        (findViewById(R.id.avloadingIndicatorView)).setVisibility(View.GONE);
        super.onResume();
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
                //hide keyboard and show loading screen
                View view = thisActivity.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                //show loading screen
                (findViewById(R.id.avloadingIndicatorView)).bringToFront();
                (findViewById(R.id.avloadingIndicatorView)).setVisibility(View.VISIBLE);

                firebase.authWithPassword(email, password, new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        storeID(authData.getUid());
                        UserSwitcherActivity.chooseUI(getApplicationContext(), (thisActivity));
                    }
                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        (findViewById(R.id.avloadingIndicatorView)).setVisibility(View.GONE);
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
