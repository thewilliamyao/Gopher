package com.example.gavi.gopher;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;

import com.firebase.client.Firebase;
import com.firebase.client.core.Context;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;


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

    //convert an address to a Coordinate
    public static Address addressToCoordinate(String address, Activity activity) throws IOException {

        if (address != null) {
            Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
            String textToSearch = address;
            List<Address> fromLocationName = null;

            fromLocationName = geocoder.getFromLocationName(textToSearch, 1);
            if (fromLocationName != null && fromLocationName.size() > 0) {
                Address a = fromLocationName.get(0);
                return a;
            }

        }
        return null;
    }

    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

//    String myBase64Image = encodeToBase64(myBitmap, Bitmap.CompressFormat.JPEG, 100);
//    Bitmap myBitmapAgain = decodeBase64(myBase64Image);


}
