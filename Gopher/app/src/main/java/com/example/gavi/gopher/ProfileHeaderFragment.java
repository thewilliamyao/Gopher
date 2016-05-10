package com.example.gavi.gopher;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
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
//            intent.putExtra("prof_pic", encodedProfilePic);
                startActivityForResult(intent, 0);
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
        myPrefs =  PreferenceManager.getDefaultSharedPreferences(
                getActivity().getApplicationContext());
        nameText = (TextView) view.findViewById(R.id.firstName);
        settingsButton = (ImageButton) view.findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(editSettings);

        profPic = (ImageView) view.findViewById(R.id.profPic);

        setupSwitch();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setName();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setName();
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
    private void setName() {
        SharedPreferences myPrefs =  PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        String userID = myPrefs.getString(Constants.USER_ID, "null");
        Firebase firebase = Modules.connectDB(getActivity(), "/users");
        Query queryRef = firebase.orderByKey().equalTo(userID);

        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                currUser = snapshot.getValue(User.class);
                nameText.setText(currUser.getFirstName() + " " + currUser.getLastName());

//                encodedProfilePic = user.getProfilePic();
//                Bitmap decodedImage = Modules.decodeBase64(user.getProfilePic());
//                profPic.setImageBitmap(decodedImage);

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(FirebaseError firebaseError) {}

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            protected Object clone() throws CloneNotSupportedException {return super.clone();}
        });
    }
}
