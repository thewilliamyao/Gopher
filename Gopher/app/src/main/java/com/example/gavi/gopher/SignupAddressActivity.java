package com.example.gavi.gopher;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.io.IOException;
import java.util.Map;

public class SignupAddressActivity extends AppCompatActivity {

    private Firebase myFirebaseRef;
    private EditText streetText;
    private EditText cityText;
    private EditText zipcodeText;
    private Button signupButton;
    private Activity thisActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_address);

        thisActivity = this;

        //init Firebase
        Firebase.setAndroidContext(this); //global firebase context
        myFirebaseRef = Modules.connectDB(this, "");

        //set signup button
        signupButton = (Button) findViewById(R.id.signupButton);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
            }
        });

        //init UI
        streetText = (EditText) findViewById(R.id.street);
        cityText = (EditText) findViewById(R.id.city);
        zipcodeText = (EditText) findViewById(R.id.zipcode);
    }


    private boolean createUser() {
        String street = streetText.getText().toString();
        String city = cityText.getText().toString();
        String zipcode = zipcodeText.getText().toString();
        final String address = street + ", " + city + " " + zipcode;

        //validate
        if (street.equals("") | city.equals("") | zipcode.equals("") ) {
            shakeSignup();
            return false;
        }

        //validate
        if (!validAddress(address)) {
            shakeSignup();
        }

        //get intent data for user
        Intent data = getIntent();
        final String fname = data.getStringExtra("fname");
        final String lname = data.getStringExtra("lname");
        final String email = data.getStringExtra("email");
        final String password = data.getStringExtra("password");

        //create user
        myFirebaseRef.createUser(email, password, new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                String id = result.get("uid").toString();
                User newUser = new User(id, fname, lname, email, address, "", "", "");
                storeUser(id, newUser);
                storeID(id);
                UserSwitcherActivity.chooseUI(getApplicationContext(), (thisActivity));
            }
            @Override
            public void onError(FirebaseError firebaseError) {
                shakeSignup();
            }
        });
        return true;
    }

    private boolean validAddress(String address) {
        try {
            Address a = Modules.addressToCoordinate(address, this);
            if (a != null) {
                return true;
            }
        } catch (IOException e) {
            return false;
        }
        return false;
    }

    //store the new user's data in firebase
    private void storeUser(String id, User user) {
        Firebase alanRef = myFirebaseRef.child("users").child(id);
        alanRef.setValue(user);
    }

    //store user id in shared prefs for global access
    private void storeID(String id) {
        SharedPreferences myPrefs =  PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor peditor = myPrefs.edit();
        peditor.putString(Constants.USER_ID, id);
        peditor.commit();

    }

    private void shakeSignup() {
        YoYo.with(Techniques.Shake)
                .duration(700)
                .playOn(signupButton);
        System.out.println("Error creating user");
    }
}
