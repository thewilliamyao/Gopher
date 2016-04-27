package com.example.gavi.gopher;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

/**
 * Created by Gavi on 4/25/16.
 */
public class Modules {

    //set the theme based on the user type
    public static void setUserUI(Activity activity) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        int userType = prefs.getInt(Constants.USER_TYPE, Constants.FOODIE);
        if (userType == Constants.FOODIE) {
            activity.setTheme(R.style.FoodieTheme);
        } else {
            activity.setTheme(R.style.CookTheme);
        }
    }

}
