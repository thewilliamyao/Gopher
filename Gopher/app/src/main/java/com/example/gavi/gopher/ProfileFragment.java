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

    private Firebase defRef;
    private Firebase userRef;
    private Firebase upRef;


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

        Firebase.setAndroidContext(getActivity());

        defRef = Modules.connectDB(getActivity(), "/meals_bought");

        gopherView = (TextView) view.findViewById(R.id.gopher_points_field);
        soldView = (TextView) view.findViewById(R.id.meals_sold_field);
        boughtView = (TextView) view.findViewById(R.id.meals_bought_field);


        setData();

        return view;
    }


    //setup data on page
    private void setData() {

        //get userID
        SharedPreferences myPrefs =  PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        userid = myPrefs.getString(Constants.USER_ID, "");

        userRef = Modules.connectDB(getActivity(), "/meals_bought/" + userid);
        //get user ID -> meal ID to get meal
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int GopherPoint;
                int mealsSold;
                int mealsBought;
                String mealsSoldString;
                String mealsBoughtString;
                Object a = dataSnapshot.child("mealsSold").getValue();
                if (a != null) {
                    System.out.println("not NULL");
                    mealsSoldString = a.toString();
                    mealsBoughtString = dataSnapshot.child("mealsBought").getValue().toString();
                    System.out.println(mealsSoldString + " and " + mealsBoughtString);
                    mealsSold = Integer.parseInt(mealsSoldString);
                    mealsBought = Integer.parseInt(mealsBoughtString);

                    GopherPoint = (mealsBought * 1) + (mealsSold * 5);
                    gopherView.setText("" + GopherPoint);
                    soldView.setText("" + mealsSold);
                    boughtView.setText("" + mealsBought);

                } else {


                    Map<String, String> newEntryData = new HashMap<String, String>();
                    newEntryData.put("mealsSold", "0");
                    newEntryData.put("mealsBought", "0");

                    /*Map<String, Map<String, String>> newEntry = new HashMap<String, Map<String, String>>();
                    newEntry.put(userid, newEntryData);*/
                    upRef = defRef.child(userid);
                    upRef.setValue(newEntryData);

                    /*Map<String, Object> mealsSoldMap = new HashMap<String, Object>();
                    mealsSoldMap.put("nickname", "Alan The Machine");*/

                    gopherView.setText("0");
                    soldView.setText("0");
                    boughtView.setText("0");

                }



            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }

}
