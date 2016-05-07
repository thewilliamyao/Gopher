package com.example.gavi.gopher;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class NewMealActivity extends AppCompatActivity {

    private Button postButton;
    private EditText titleText;
    private EditText priceText;
    private EditText descriptionText;
    private Activity thisActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_meal);
        thisActivity = this;
        setTitle("Cook New Meal");

        //setup UI
        titleText = (EditText) findViewById(R.id.title);
        priceText = (EditText) findViewById(R.id.price);
        descriptionText = (EditText) findViewById(R.id.description);
        postButton = (Button) findViewById(R.id.post);

        //listeners
        postButton.setOnClickListener(postMeal);
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
                String userID = myPrefs.getString(Constants.USER_ID, "");
                final Firebase userRef = Modules.connectDB(thisActivity, "/users/" + userID);

                //get user
                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        String address = user.getAddress();
                        Meal meal = new Meal(title, Double.parseDouble(priceStr), description, address);

                        //push the new meal
                        Firebase mealsRef = Modules.connectDB(thisActivity, "/meals");
                        Firebase mealRef = mealsRef.push();
                        mealRef.setValue(meal);

                        //store the meal id for the user
                        userRef.child("mealSellingID").setValue(mealRef.getKey());
                        finish();

                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {}
                });
            }
        }
    };

}
