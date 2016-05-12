package com.example.gavi.gopher;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.Firebase;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class FoodieDetails extends AppCompatActivity {

    private TextView titleText;
    private TextView priceText;
    private TextView descriptionText;

    private String mealid;
    private Activity thisActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodie_details);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //init vars
        thisActivity = this;
        Firebase.setAndroidContext(this);

        //init UI
        titleText = (TextView) findViewById(R.id.title);
        priceText = (TextView) findViewById(R.id.price);
        descriptionText = (TextView) findViewById(R.id.description);

        //get data
        Intent data = getIntent();
        String title = data.getStringExtra("title");
        String email = data.getStringExtra("email");
        String description = data.getStringExtra("description");

        //set text views
        titleText.setText(title);
        priceText.setText(email);
        descriptionText.setText(description);


    }


}
