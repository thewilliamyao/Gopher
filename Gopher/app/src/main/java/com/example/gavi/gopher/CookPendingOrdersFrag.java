package com.example.gavi.gopher;


import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * A simple {@link Fragment} subclass.
 */
public class CookPendingOrdersFrag extends Fragment {

    private View view;
    private TextView titleText;
    private TextView priceText;
    private TextView descriptionText;
    private Button markReadyButton;
    private Button markPickedUpButton;

    private String userid;
    private User cook;
    private Activity thisActivity;

    public CookPendingOrdersFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_cook_pending_orders, container, false);

        //init vars
        thisActivity = getActivity();

        //init UI
        titleText = (TextView) view.findViewById(R.id.title);
        priceText = (TextView) view.findViewById(R.id.price);
        descriptionText = (TextView) view.findViewById(R.id.description);
        markPickedUpButton = (Button) view.findViewById(R.id.markPickedUp);
        markReadyButton = (Button) view.findViewById(R.id.markReady);

        //setup data
        setData();

        //set listeners
//        markPickedUpButton.setOnClickListener(markPickedUp);
//        markReadyButton.setOnClickListener(markReady);

        return view;
    }

    //setup data on page
    private void setData() {

        //get userID
        SharedPreferences myPrefs =  PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        userid = myPrefs.getString(Constants.USER_ID, "");
        Firebase userRef = Modules.connectDB(getActivity(), "/users/" + userid);

        //get user ID -> meal ID to get meal
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String mealid = dataSnapshot.child("mealBuyingID").getValue().toString();
                if (!mealid.equals("")) {
                    loadMeal(mealid);
                } else {
                    if ((view.findViewById(R.id.contentFrame)).getVisibility() == View.VISIBLE) {
                        toggleEmptyOrders();
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });
    }

    //load meal
    private void loadMeal(String mealid) {
        Firebase mealRef = Modules.connectDB(getActivity(), "/meals/" + mealid);
        mealRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Meal meal = dataSnapshot.getValue(Meal.class);

                //set UI data
                titleText.setText(meal.getTitle());
                priceText.setText("$" + String.format("%.2f", meal.getPrice()));
                descriptionText.setText(meal.getDescription());

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });
    }


    //show message if no meals have been ordered
    private void toggleEmptyOrders() {
        if ((view.findViewById(R.id.contentFrame)).getVisibility() == View.GONE) {
            (view.findViewById(R.id.contentFrame)).setVisibility(View.VISIBLE);
            (view.findViewById(R.id.emptyFrame)).setVisibility(View.GONE);
        } else {
            (view.findViewById(R.id.contentFrame)).setVisibility(View.GONE);
            (view.findViewById(R.id.emptyFrame)).setVisibility(View.VISIBLE);
        }

    }



}