package com.example.gavi.gopher;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by Gavi on 4/25/16.
 */
public class Utils {

    private static int sTheme;
    public final static int COOK_THEME = 0;
    public final static int FOODIE_THEME = 1;

    /**
     * Set the theme of the Activity, and restart it by creating a new Activity of the same type.
     */
    public static void changeToTheme(Activity activity, int theme) {
        sTheme = theme;
        activity.finish();
        activity.startActivity(new Intent(activity, activity.getClass()));
    }

    /**
     * Set the theme of the activity, according to the configuration.
     */
    public static void onActivityCreateSetTheme(Activity activity) {
        switch (sTheme) {
            default:
            case COOK_THEME:
                activity.setTheme(R.style.CookTheme);
                break;
            case FOODIE_THEME:
                activity.setTheme(R.style.FoodieTheme);
                break;
        }
    }

    public static void pickTheme(Activity activity) {
        activity.setTheme(R.style.CookTheme);
        activity.finish();
        activity.startActivity(new Intent(activity, activity.getClass()));
    }
}
