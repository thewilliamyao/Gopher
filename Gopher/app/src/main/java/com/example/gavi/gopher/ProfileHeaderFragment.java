package com.example.gavi.gopher;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileHeaderFragment extends Fragment {

    private View view;
    private TextView nameText;
    private Button settingsButton;
    private Switch userSwitch;
    private SharedPreferences myPrefs;

    //edit the settings for the header bar
    View.OnClickListener editSettings = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), EditSettingsActivity.class);
            startActivity(intent);
        }
    };

    //Toggle the user type
    View.OnClickListener switchUser = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            UserSwitcherActivity.toggleUserType(getActivity().getApplicationContext());
//            Intent intent = new Intent(getContext(), UserSwitcherActivity.class);
//            startActivity(intent);
            UserSwitcherActivity.chooseUI(getActivity().getApplicationContext(), getActivity());
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile_header, container, false);
        myPrefs =  PreferenceManager.getDefaultSharedPreferences(
                getActivity().getApplicationContext());
        nameText = (TextView) view.findViewById(R.id.nameText);
        settingsButton = (Button) view.findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(editSettings);
        setupSwitch();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        nameText.setText(myPrefs.getString(Constants.USER_NAME, Constants.DEFAULT_NAME));
    }

    //setup the switch based on the user type
    private void setupSwitch() {
        userSwitch = (Switch) view.findViewById(R.id.userSwitch);
        userSwitch.setOnClickListener(switchUser);
        int userType = myPrefs.getInt(Constants.USER_TYPE, Constants.FOODIE);
        if (userType == Constants.FOODIE) {
            userSwitch.setText(R.string.foodie_title);
            userSwitch.setChecked(true);
        } else {
            userSwitch.setText(R.string.cook_title);
            userSwitch.setChecked(false);
        }
    }
}
