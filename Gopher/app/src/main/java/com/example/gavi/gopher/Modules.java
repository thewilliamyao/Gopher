package com.example.gavi.gopher;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.firebase.client.Firebase;
import com.firebase.client.core.Context;


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

    public static Firebase connectDB(Activity activity, String path) {
        Firebase.setAndroidContext(activity); //global firebase context
        return (new Firebase("https://gopher-uima.firebaseIO.com" + path)); //reference variable
    }

}
