package com.example.gavi.gopher;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class UserSwitcherActivity extends AppCompatActivity {

    private SharedPreferences mylogiPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chooseUI(getApplicationContext(), this);
    }

    public static void toggleUserType(Context context) {
        int userType = getUserType(context);

        if (userType == Constants.FOODIE) {
            userType = Constants.COOK;
        } else {
            userType = Constants.FOODIE;
        }
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor peditor = prefs.edit();
        peditor.putInt(Constants.USER_TYPE, userType);
        peditor.commit();
    }

    public static int getUserType(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(Constants.USER_TYPE, Constants.FOODIE);
    }

    public static void chooseUI(Context context, Activity activity) {
        int userType = getUserType(context);
        if (userType == Constants.COOK) {
            context.setTheme(R.style.CookTheme);
            Intent intent = new Intent(activity, CookMainActivity.class);
            activity.startActivity(intent);
//            activity.finish();
        } else {
            context.setTheme(R.style.FoodieTheme);
            Intent intent = new Intent(activity, FoodieMainActivity.class);
            activity.startActivity(intent);
//            activity.finish();

        }
    }

}
