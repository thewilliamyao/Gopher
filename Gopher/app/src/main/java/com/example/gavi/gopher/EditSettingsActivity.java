package com.example.gavi.gopher;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.firebase.client.Firebase;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EditSettingsActivity extends AppCompatActivity {

    private EditText fName;
    private EditText lName;
    private EditText street;
    private EditText city;
    private EditText zipcode;
    private Button saveButton;
    private Button addPhotoButton;
    private ImageView userImage;
    private Activity activity;

    //save to firebase
    View.OnClickListener save = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String fname = fName.getText().toString().trim();
            String lname = lName.getText().toString().trim();
            String streetStr = street.getText().toString().trim();
            String cityStr = city.getText().toString().trim();
            String zipcodeStr = zipcode.getText().toString().trim();

            //check empty fields
            boolean invalid = false;
            String[] fields = { fname, lname, streetStr, cityStr, zipcodeStr };
            for (String field: fields) {
                if (field.equals("")) {
                    invalid = true;
                }
            }

            //validate address
            String newAddress = streetStr + ", " + cityStr + " " + zipcodeStr;
            try {
                Modules.addressToCoordinate(newAddress, activity);
            } catch (IOException e) {
                invalid = true;
            }

            if (!invalid) {
                SharedPreferences myPrefs =  PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String userID = myPrefs.getString(Constants.USER_ID, "null");
                Firebase firebase = Modules.connectDB(activity, "/users");
                Firebase userRef = firebase.child(userID);
                Map<String, Object> name = new HashMap<String, Object>();
                name.put("firstName", fName.getText().toString());
                name.put("lastName", lName.getText().toString());
                name.put("address", newAddress);
                userRef.updateChildren(name);
                finish();

            } else {
                Snackbar.make(saveButton, "A field is empty, or the entered address is invalid.", Snackbar.LENGTH_LONG).show();
            }
        }
    };

    //Add a profile photo
    View.OnClickListener addPhoto = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(
                    Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, 1);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        Modules.setUserUI(this);
        setContentView(R.layout.activity_edit_settings);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //enable back button
        setTitle("Settings");

        fName = (EditText) findViewById(R.id.firstName);
        lName = (EditText) findViewById(R.id.lastName);
        street = (EditText) findViewById(R.id.street);
        city = (EditText) findViewById(R.id.city);
        zipcode = (EditText) findViewById(R.id.zipcode);
        userImage = (ImageView) findViewById(R.id.userImage);

        //get data
        Intent data = getIntent();
        fName.setText(data.getStringExtra("first_name"));
        lName.setText(data.getStringExtra("last_name"));
        String temp = (data.getStringExtra("address"));
        try {
            Address address = Modules.addressToCoordinate(temp, this);
            street.setText(address.getAddressLine(0));
            city.setText(address.getLocality());
            zipcode.setText(address.getPostalCode());
        } catch (Exception e) {
            //handle error
        }

//        String encodedImage = data.getStringExtra("prof_pic");
//        Bitmap decodedImage = Modules.decodeBase64(encodedImage);
//        userImage.setImageBitmap(decodedImage);

        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(save);

        addPhotoButton = (Button) findViewById(R.id.addPhotoButton);
        addPhotoButton.setOnClickListener(addPhoto);
    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        // TODO Auto-generated method stub
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode) {
//
//            case 0:
//                if (resultCode == RESULT_OK) {
//                    Uri targetUri = data.getData();
//                    //             textTargetUri.setText(targetUri.toString());
//                    Bitmap bitmap;
//                    try {
//                        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
//                        userImage.setImageBitmap(bitmap);
//
//
//                    } catch (FileNotFoundException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//                }
//                break;
//
//        }
//
//    }



    //get results from selected user photo
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            try {
                //encode image
                Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage));
                String encodedImage = Modules.encodeToBase64(bitmap, Bitmap.CompressFormat.JPEG, 30);

                //get user ID and store image in firebase
                SharedPreferences myPrefs =  PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String userID = myPrefs.getString(Constants.USER_ID, "");
                Firebase user = Modules.connectDB(this, "/users/" + userID);
                user.child("profilePic").setValue(encodedImage);

                userImage.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                //handle
            }

            // String picturePath contains the path of selected Image
        }
    }
}
