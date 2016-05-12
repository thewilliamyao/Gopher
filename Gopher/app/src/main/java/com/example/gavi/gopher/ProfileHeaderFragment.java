package com.example.gavi.gopher;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileHeaderFragment extends Fragment {

    private View view;
    private TextView nameText;
    private ImageButton settingsButton;
    private RadioButton cookToggle;
    private RadioButton foodieToggle;
    private SharedPreferences myPrefs;
    private ImageView profPic;
    private String encodedProfilePic;
    private User currUser;


    //edit the settings for the header bar
    View.OnClickListener editSettings = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (currUser != null) {
                Intent intent = new Intent(getActivity(), EditSettingsActivity.class);
                intent.putExtra("first_name", currUser.getFirstName());
                intent.putExtra("last_name", currUser.getLastName());
                intent.putExtra("address", currUser.getAddress());
                startActivity(intent);
            }

        }
    };


    //Toggle the user type
    View.OnClickListener switchUser = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            UserSwitcherActivity.toggleUserType(getActivity().getApplicationContext());
            UserSwitcherActivity.chooseUI(getActivity().getApplicationContext(), getActivity());
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile_header, container, false);

        //init vars
        myPrefs =  PreferenceManager.getDefaultSharedPreferences(
                getActivity().getApplicationContext());
        Firebase.setAndroidContext(getActivity());

        //set UI
        nameText = (TextView) view.findViewById(R.id.firstName);
        settingsButton = (ImageButton) view.findViewById(R.id.settingsButton);
        profPic = (ImageView) view.findViewById(R.id.profPic);

        //set listeners
        settingsButton.setOnClickListener(editSettings);

        //set data
        setupSwitch();
        setData();

        return view;
    }

    @Override
    public void onDestroyView() {
//        profPic.setImageBitmap(null);
        super.onDestroyView();
        Log.d("log", "Prof Header view destroyed");

    }


    //setup the switch based on the user type
    private void setupSwitch() {
        foodieToggle = (RadioButton) view.findViewById(R.id.foodieToggle);
        cookToggle = (RadioButton) view.findViewById(R.id.cookToggle);

        int userType = myPrefs.getInt(Constants.USER_TYPE, Constants.FOODIE);
        if (userType == Constants.FOODIE) {
            cookToggle.setTextColor(getResources().getColor(R.color.dark_beige));
            cookToggle.setOnClickListener(switchUser);
            foodieToggle.setChecked(true);
            cookToggle.setChecked(false);
        } else {
            foodieToggle.setTextColor(getResources().getColor(R.color.dark_beige));
            foodieToggle.setOnClickListener(switchUser);
            cookToggle.setChecked(true);
            foodieToggle.setChecked(false);
        }
    }

    //setup user data
    private void setData() {
        SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        String userID = myPrefs.getString(Constants.USER_ID, "null");
        Firebase userRef = Modules.connectDB(getActivity(), "/users/" + userID);

        //set user info
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currUser = dataSnapshot.getValue(User.class);
                nameText.setText(currUser.getFirstName() + " " + currUser.getLastName());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) { }
        });

        //set profile photo
//        profPic.setImageBitmap(null);   //remove placeholder icon
//        view.findViewById(R.id.avloadingIndicatorView).setVisibility(View.VISIBLE); //set loading animation
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

//        Picasso.with(getContext())
//                .load("http://www.sobeys.com/wp-content/uploads/2015/04/hero-garofalo-pasta.jpg")
//                .resize(100, 100)
//                .centerCrop()
//                .into(profPic);

    }
}
