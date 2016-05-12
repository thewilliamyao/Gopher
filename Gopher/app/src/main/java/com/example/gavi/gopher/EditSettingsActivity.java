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
import android.widget.ImageButton;
import android.widget.ImageView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

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
    private ImageButton userImage;
    private Activity activity;
    private String encodedImage;

    private static int PICK_IMAGE_REQUEST = 1;
    private Bitmap decoded;

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
                Address a = Modules.addressToCoordinate(newAddress, activity);
                if (a == null) {
                    invalid = true;
                }
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

                savePhoto(userID);
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
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Modules.setUserUI(this);
        setContentView(R.layout.activity_edit_settings);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //enable back button
        setTitle("Settings");

        //init variables
        activity = this;
        decoded = null;
        Firebase.setAndroidContext(this);

        //init UI
        fName = (EditText) findViewById(R.id.firstName);
        lName = (EditText) findViewById(R.id.lastName);
        street = (EditText) findViewById(R.id.street);
        city = (EditText) findViewById(R.id.city);
        zipcode = (EditText) findViewById(R.id.zipcode);
        userImage = (ImageButton) findViewById(R.id.userImage);

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

        //try to get photo
        loadImage();

        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(save);

//        userImage.setOnClickListener(addPhoto);
    }

    //save profile photo to firebase
    private void savePhoto(String userid) {
        if (decoded != null) {
            Log.d("log", "inside save photo");
            Firebase imagesRef = Modules.connectDB(activity, "/profile_images");
            Firebase imagesChildRef = imagesRef.child(userid);

            // Add some data to the new location
            Map<String, String> imageMap = new HashMap<String, String>();
            imageMap.put("image", Modules.encodeToBase64(decoded, Bitmap.CompressFormat.JPEG, 100));
            imagesChildRef.setValue(imageMap);
        }
    }

    //load profile picture
    private void loadImage() {
//        userImage.setImageBitmap(null);   //remove placeholder icon
//        findViewById(R.id.avloadingIndicatorView).setVisibility(View.VISIBLE); //set loading animation
//        Firebase imageRef = Modules.connectDB(getActivity(), "/profile_images/" + userID);
//        imageRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (!dataSnapshot.hasChildren()) {
//                    view.findViewById(R.id.avloadingIndicatorView).setVisibility(View.GONE); //set loading animation
//                    profPic.setImageDrawable(getResources().getDrawable(R.drawable.ic_person_grey_70dp));
//                } else {
//                    for (DataSnapshot postSnap: dataSnapshot.getChildren()) {
//                        encodedProfilePic = postSnap.getValue().toString();
//                        view.findViewById(R.id.avloadingIndicatorView).setVisibility(View.GONE); //set loading animation
//                        profPic.setImageBitmap(Modules.decodeBase64(encodedProfilePic));
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) { }
//        });
    }



    //set image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                decoded = bitmap;
                userImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
