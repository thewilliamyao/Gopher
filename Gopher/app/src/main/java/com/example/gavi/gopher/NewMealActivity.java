package com.example.gavi.gopher;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class NewMealActivity extends AppCompatActivity {

    private Button postButton;
    private EditText titleText;
    private EditText priceText;
    private EditText descriptionText;
    private Activity thisActivity;
    private ImageView image;
    private Bitmap decoded;

    private static int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_meal);
        thisActivity = this;
        setTitle("Cook New Meal");
        Firebase.setAndroidContext(this);

        //setup UI
        titleText = (EditText) findViewById(R.id.title);
        priceText = (EditText) findViewById(R.id.price);
        descriptionText = (EditText) findViewById(R.id.description);
        postButton = (Button) findViewById(R.id.post);
        image = (ImageView) findViewById(R.id.image);

        //listeners
        postButton.setOnClickListener(postMeal);
//        image.setOnClickListener(setImage);
    }

    //save new meal to database
    View.OnClickListener postMeal = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final String title = titleText.getText().toString();
            final String priceStr = priceText.getText().toString();
            final String description = descriptionText.getText().toString();

            //validate
            if (title.equals("") | priceStr.equals("") | description.equals("")) {
                YoYo.with(Techniques.Shake)
                    .duration(700)
                    .playOn(postButton);
            } else {

                //get user firebase ref
                SharedPreferences myPrefs =  PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                final String userID = myPrefs.getString(Constants.USER_ID, "");
                final Firebase userRef = Modules.connectDB(thisActivity, "/users/" + userID);

                //get user
                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        String address = user.getAddress();
                        Meal meal = new Meal(title, Double.parseDouble(priceStr), description, address, "", userID, "");

                        //push the new meal
                        Firebase mealsRef = Modules.connectDB(thisActivity, "/meals");
                        Firebase mealRef = mealsRef.push();
                        mealRef.setValue(meal);
                        mealRef.child("id").setValue(mealRef.getKey());

                        //store the meal id for the user
                        userRef.child("mealSellingID").setValue(mealRef.getKey());
                        finish();

                        //push meal image
                        if (decoded != null) {
                            Firebase imagesRef = Modules.connectDB(thisActivity, "/meal_images");
                            Firebase imagesChildRef = imagesRef.child(mealRef.getKey());

                            // Add some data to the new location
                            Map<String, String> imageMap = new HashMap<String, String>();
                            imageMap.put("image", Modules.encodeToBase64(decoded, Bitmap.CompressFormat.JPEG, 100));
                            imagesChildRef.setValue(imageMap);

                        }

                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {}
                });
            }
        }
    };

    //open image gallery
    View.OnClickListener setImage = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        }
    };

    //set image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                decoded = bitmap;
                image.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
