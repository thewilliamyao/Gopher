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
    private Button leftButton;
    private Button rightButton;

    private String userid;
    private User cook;
    private Activity thisActivity;

    private static final String MARK_READY = "Mark Ready";
    private static final String MARK_NOT_READY = "Mark Not Ready";
    private static final String MARK_PICKED_UP = "Mark Picked Up";
    private static final String DELETE = "Delete Meal";
    private static final String CANCEL = "Cancel Order";


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
        Firebase.setAndroidContext(getActivity());

        //init UI
        titleText = (TextView) view.findViewById(R.id.title);
        priceText = (TextView) view.findViewById(R.id.price);
        descriptionText = (TextView) view.findViewById(R.id.description);
        leftButton = (Button) view.findViewById(R.id.leftButton);
        rightButton = (Button) view.findViewById(R.id.rightButton);

        //setup data
        setData();

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
                String mealid = dataSnapshot.child("mealSellingID").getValue().toString();
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
    private void loadMeal(final String mealid) {
        final Firebase mealRef = Modules.connectDB(getActivity(), "/meals/" + mealid);
        mealRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Meal meal = dataSnapshot.getValue(Meal.class);

                if (meal != null) {

                    //setup left button
                    if (!meal.isReady()) {
                        mealNotReady(mealid);
                    } else {
                        mealReady(mealid);
                    }

                    //setup right button
                    if (meal.isBought()) {
                        mealBought(mealid);
                    } else {
                        mealNotBought(mealid, mealRef);
                    }

                    //set UI data
                    titleText.setText(meal.getTitle());
                    priceText.setText("$" + String.format("%.2f", meal.getPrice()));
                    descriptionText.setText(meal.getDescription());
                }


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

    //When a meal is not ready
    private void mealNotReady(final String mealid) {
        leftButton.setText(MARK_READY);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Firebase mealRef = Modules.connectDB(getActivity(), "/meals/" + mealid);
                mealRef.child("ready").setValue(true);
                mealReady(mealid);
            }
        });

    }

    private void mealReady(final String mealid) {
        leftButton.setText(MARK_NOT_READY);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Firebase mealRef = Modules.connectDB(getActivity(), "/meals/" + mealid);
                mealRef.child("ready").setValue(false);
                mealNotReady(mealid);
            }
        });

    }

    private void mealBought(final String mealid) {
        rightButton.setText(MARK_PICKED_UP);
        rightButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDarkCook));
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalizePurchase(mealid);
            }
        });
    }

    //a user has not bought the meal
    private void mealNotBought (final String mealid, final Firebase mealRef) {
        rightButton.setText(DELETE);
        rightButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryFoodie));
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Modules.connectDB(getActivity(), "/meals/" + mealid).removeValue();

                //remove selling id
                SharedPreferences myPrefs =  PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                userid = myPrefs.getString(Constants.USER_ID, "");
                Firebase userRef = Modules.connectDB(getActivity(), "/users/" + userid);
                userRef.child("mealSellingID").setValue("");
            }
        });
    }

    //meal has been picked up - finalize purchase
    private void finalizePurchase(final String mealid) {

        //sweet alert
        new SweetAlertDialog(thisActivity, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Mark picked up?")
                .setContentText("Only confirm if a Foodie has picked up and paid for the meal.")
                .setConfirmText("Yes")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {

                        //update and remove meal data
                        updateStats(mealid);
                        updateIDs(mealid);

                        //confirmation dialogue
                        sDialog
                                .setTitleText("Meal purchase finalized!")
                                .showCancelButton(false)
                                .setContentText("")
                                .setConfirmText("OK")
                                .setConfirmClickListener(null)
                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                    }
                })
                .setCancelText("No")
                .setCancelClickListener(null)
                .show();





    }

    //remove buyer and seller id then delete meal
    private void updateIDs(String mealid) {

        //remove buying and selling ids for buyer and seller
        Firebase mealRef = Modules.connectDB(getActivity(), "/meals/" + mealid);
        mealRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if ((dataSnapshot.child("buyerID").getValue() != null) &&
                        (dataSnapshot.child("sellerID").getValue() != null)) {

                    Log.d("log", "inside");

                    //get ids for buyer and seller
                    final String buyerID = dataSnapshot.child("buyerID").getValue().toString();
                    final String sellerID = dataSnapshot.child("sellerID").getValue().toString();

                    //remove ids
                    (Modules.connectDB(getActivity(), "/users/" + buyerID + "/mealBuyingID")).setValue("");
                    (Modules.connectDB(getActivity(), "/users/" + sellerID + "/mealSellingID")).setValue("");
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });

        //remove meal
        mealRef.removeValue();
    }



    //update stats for users when meal is bought
    private void updateStats(String mealid) {
        Firebase mealRef = Modules.connectDB(getActivity(), "/meals/" + mealid);
        mealRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if ((dataSnapshot.child("buyerID").getValue() != null) &&
                        (dataSnapshot.child("sellerID").getValue() != null)) {

                    //get ids for buyer and seller
                    final String buyerID = dataSnapshot.child("buyerID").getValue().toString();
                    final String sellerID = dataSnapshot.child("sellerID").getValue().toString();

                    //update seller stats
                    Firebase sellerRef = Modules.connectDB(getActivity(), "/meals_bought/" + sellerID);
                    sellerRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child("mealsSold").getValue() != null) { //value set
                                int mealsSold = Integer.parseInt(dataSnapshot.child("mealsSold").getValue().toString());
                                mealsSold++;
                                Modules.connectDB(getActivity(), "/meals_bought/" + sellerID + "/mealsSold").setValue(mealsSold);
                            } else { //value not set; set to 1
                                Modules.connectDB(getActivity(), "/meals_bought/" + sellerID + "/mealsSold").setValue(1);
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {}
                    });


                    //update buyer stats
                    Firebase buyerRef = Modules.connectDB(getActivity(), "/meals_bought/" + buyerID);
                    buyerRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child("mealsBought").getValue() != null) { //value set
                                int mealsBought = Integer.parseInt(dataSnapshot.child("mealsBought").getValue().toString());
                                mealsBought++;
                                Modules.connectDB(getActivity(), "/meals_bought/" + buyerID + "/mealsBought").setValue(mealsBought);
                            } else { //value not set; set to 1
                                Modules.connectDB(getActivity(), "/meals_bought/" + buyerID + "/mealsBought").setValue(1);
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {}
                    });
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) { }
        });
    }



}