package com.example.gavi.gopher;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private String userid;
    private TextView gopherView;
    private TextView soldView;
    private TextView boughtView;
    private TextView hintView1;
    private TextView hintView2;
    private TextView hintView3;
    private Firebase defRef;
    private Firebase userRef;


    View view;

    public ProfileFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_profile, container, false);

        //add profile header fragment
        getChildFragmentManager().beginTransaction().add(
                R.id.profile_header_container, new ProfileHeaderFragment()
        ).commit();

        userRef = Modules.connectDB(getActivity(), "/meals_bought/" + userid);
        defRef = Modules.connectDB(getActivity(), "meals_bought");

        gopherView = (TextView) view.findViewById(R.id.gopher_points_field);
        soldView = (TextView) view.findViewById(R.id.meals_sold_field);
        boughtView = (TextView) view.findViewById(R.id.meals_bought_field);
        hintView1 = (TextView) view.findViewById(R.id.hint1);
        hintView2 = (TextView) view.findViewById(R.id.hint2);
        hintView3 = (TextView) view.findViewById(R.id.hint3);

        int userType = ListFragment.getUserType(this.getActivity().getApplicationContext());
        if (userType == Constants.FOODIE) {
            //do nothing
        } else {
            hintView1.setVisibility(View.VISIBLE);
            hintView2.setVisibility(View.VISIBLE);
            hintView3.setVisibility(View.VISIBLE);
        }

        setData();

        return view;
    }


    //setup data on page
    private void setData() {

        //get userID
        SharedPreferences myPrefs =  PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        userid = myPrefs.getString(Constants.USER_ID, "");

        //get user ID -> meal ID to get meal
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int GopherPoint;
                int mealsSold;
                int mealsBought;
                String mealsSoldString;
                Object a = dataSnapshot.child("mealsSold").getValue();
                if (a != null) {
                    mealsSoldString = a.toString();
                } else {
                    mealsSoldString = "";

                    Map<String, String> newEntryData = new HashMap<String, String>();
                    newEntryData.put("mealsSold", "0");
                    newEntryData.put("mealsBought", "0");

                    Map<String, Map<String, String>> newEntry = new HashMap<String, Map<String, String>>();
                    newEntry.put(userid, newEntryData);

                    defRef.setValue(newEntry);

                }

                String mealsBoughtString = dataSnapshot.child("mealsBought").getValue().toString();
                System.out.println(mealsSoldString + " and " + mealsBoughtString);
                if (mealsSoldString.equals("")) {
                    mealsSold = 0;
                } else {
                    mealsSold = Integer.parseInt(mealsSoldString);
                }
                if (mealsBoughtString.equals("")) {
                    mealsBought = 0;
                } else {
                    mealsBought = Integer.parseInt(mealsBoughtString);
                }

                GopherPoint = (mealsBought * 1) + (mealsSold * 5);
                gopherView.setText("" + GopherPoint);
                soldView.setText("" + mealsSold);
                boughtView.setText("" + mealsBought);


            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }

}
