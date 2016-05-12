package com.example.gavi.gopher;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class FoodDetails extends AppCompatActivity {

    private TextView titleText;
    private TextView priceText;
    private TextView descriptionText;

    private String mealid;
    private Activity thisActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);
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
        double price = data.getDoubleExtra("price", 0);
        String description = data.getStringExtra("description");
        mealid = data.getStringExtra("id");

        //set text views
        titleText.setText(title);
        priceText.setText("$" + String.format("%.2f", price));
        descriptionText.setText(description);

        //set listeners
        (findViewById(R.id.order)).setOnClickListener(orderMeal);

    }

    View.OnClickListener orderMeal = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            //sweet alert
            new SweetAlertDialog(thisActivity, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Order this meal?")
                    .setConfirmText("Yes")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {

                            //store meal buying ID for user
                            SharedPreferences myPrefs =  PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            String userID = myPrefs.getString(Constants.USER_ID, "");
                            final Firebase userRef = Modules.connectDB(thisActivity, "/users/" + userID);
                            userRef.child("mealBuyingID").setValue(mealid);

                            //store meal buyingID in meal
                            final Firebase mealRef = Modules.connectDB(thisActivity, "/meals/" + mealid);
                            mealRef.child("buyerID").setValue(userID);

                            //TODO: Need to send alert to cook

                            //chnage meal bought flag to true
                            Modules.connectDB(thisActivity, "/meals/" + mealid).child("bought").setValue(true);

                            //confirmation dialogue
                            sDialog
                                    .setTitleText("Meal Ordered!")
                                    .setContentText("Your Cook will notify you when your meal is ready.")
                                    .showCancelButton(false)
                                    .setConfirmText("OK")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            thisActivity.finish();
                                        }
                                    })
                                    .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                        }
                    })
                    .setCancelText("No")
                    .setCancelClickListener(null)
                    .show();

        }
    };

}
