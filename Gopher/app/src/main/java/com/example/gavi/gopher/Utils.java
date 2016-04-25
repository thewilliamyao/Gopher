package com.example.gavi.gopher;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by Gavi on 4/25/16.
 */
public class Utils {

    private static int currTheme;

    public static void changeTheme(Activity activity, int theme) {
        currTheme = theme;
        Intent intent = new Intent(activity, activity.getClass());
        activity.finish();
        activity.startActivity(intent);
    }

    public static void onActivityCreateSetTheme(Activity activity) {
        activity.setTheme(R.style.CookTheme);
    }
}
