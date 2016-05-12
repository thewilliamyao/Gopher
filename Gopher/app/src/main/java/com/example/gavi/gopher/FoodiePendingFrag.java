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
public class FoodiePendingFrag extends Fragment {

    private View view;
    private TextView titleText;
    private TextView priceText;
    private TextView descriptionText;
    private TextView cookText;
    private TextView addressText;
    private TextView readyText;

    private String userid;
    private User cook;
    private Activity thisActivity;

    private static final String READY = "MEAL READY";
    private static final String NOT_READY = "MEAL NOT READY";


    public FoodiePendingFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_foodie_pending, container, false);

        //init vars
        thisActivity = getActivity();
        Firebase.setAndroidContext(getActivity());

        //init UI
        titleText = (TextView) view.findViewById(R.id.title);
        priceText = (TextView) view.findViewById(R.id.price);
        descriptionText = (TextView) view.findViewById(R.id.description);
        cookText = (TextView) view.findViewById(R.id.cookName);
        addressText = (TextView) view.findViewById(R.id.address);
        readyText = (TextView) view.findViewById(R.id.readyText);

        //setup data
        setData();

        //set listeners
        (view.findViewById(R.id.cancelOrder)).setOnClickListener(cancelOrder);


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

                if (dataSnapshot.child("mealBuyingID").getValue() != null) {
                    String mealid = dataSnapshot.child("mealBuyingID").getValue().toString();
                    if (!mealid.equals("")) {

                        if ((view.findViewById(R.id.contentFrame)).getVisibility() == View.GONE) {
                            toggleEmptyOrders();
                        }
                        loadMeal(mealid);
                    } else {
                        if ((view.findViewById(R.id.contentFrame)).getVisibility() == View.VISIBLE) {
                            toggleEmptyOrders();
                        }
                    }
                }


            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });
    }

    //load meal
    private void loadMeal(String mealid) {

        final Firebase mealRef = Modules.connectDB(getActivity(), "/meals/" + mealid);
        mealRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Meal meal = dataSnapshot.getValue(Meal.class);

                if (meal != null) {
                    if (meal.isReady()) {
                        mealReady();
                    } else {
                        mealNotReady();
                    }

                    //set UI data
                    titleText.setText(meal.getTitle());
                    priceText.setText("$" + String.format("%.2f", meal.getPrice()));
                    descriptionText.setText(meal.getDescription());

                    loadCook(meal.getSellerID());
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });
    }

    //load cook details
    private void loadCook(String cookid) {
        Firebase mealRef = Modules.connectDB(getActivity(), "/users/" + cookid);
        mealRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cook = dataSnapshot.getValue(User.class);
                cookText.setText(cook.getFirstName() + " " + cook.getLastName());
                addressText.setText(cook.getAddress());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });
    }

    //remove buying id from user
    //TODO: Mark that meal is no longer bought and display on map (need new meal var)
    View.OnClickListener cancelOrder = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if ((cook != null) && (!userid.equals(""))) {

                //confirm canceling meal
                new SweetAlertDialog(thisActivity, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Cancel your order?")
                        .setConfirmText("Yes")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {

                                //update values in database and show empty view
                                Firebase userRef = Modules.connectDB(getActivity(), "/users/" + userid);
                                userRef.child("mealBuyingID").setValue("");
                                toggleEmptyOrders();
                                sDialog.dismiss();

                                //change meal bought flag to false
                                Firebase boughtRef = Modules.connectDB(getActivity(), "/meals/" + cook.getMealSellingID()).child("bought");
                                        if (boughtRef != null) {
                                            boughtRef.setValue(false);
                                        }

                            }
                        })
                        .setCancelText("No")
                        .setCancelClickListener(null)
                        .show();
            }
        }
    };

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

    //meal is ready
    private void mealReady() {
        readyText.setText(READY);
        readyText.setTextColor(getResources().getColor(R.color.colorPrimaryCook));
    }

    //meal not ready
    private void mealNotReady() {
        readyText.setText(NOT_READY);
        readyText.setTextColor(getResources().getColor(R.color.colorPrimaryFoodie));
    }

}
