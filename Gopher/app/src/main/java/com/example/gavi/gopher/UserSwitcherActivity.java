package com.example.gavi.gopher;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class UserSwitcherActivity extends AppCompatActivity {

    private SharedPreferences myPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int userType = myPrefs.getInt(Constants.USER_TYPE, Constants.FOODIE);
        if (userType == Constants.COOK) {
            Intent intent = new Intent(this, CookMainActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, FoodieMainActivity.class);
            startActivity(intent);
        }
    }

    public static void toggleUserType(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int userType = prefs.getInt(Constants.USER_TYPE, Constants.FOODIE);

        if (userType == Constants.FOODIE) {
            userType = Constants.COOK;
        } else {
            userType = Constants.FOODIE;
        }
        SharedPreferences.Editor peditor = prefs.edit();
        peditor.putInt(Constants.USER_TYPE, userType);
        peditor.commit();

    }

}
