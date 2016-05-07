package com.example.gavi.gopher;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.squareup.picasso.Picasso;

import jp.wasabeef.blurry.Blurry;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExpandedMarkerFragment extends Fragment {

    private TextView nameText;
    private TextView addressText;
    private TextView priceText;
    private ImageView imageView;
    private View view;
    private Button detailButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_expanded_marker, container, false);
        nameText = (TextView) view.findViewById(R.id.firstName);
        addressText = (TextView) view.findViewById(R.id.address);
        imageView = (ImageView) view.findViewById(R.id.imageView);
        priceText = (TextView) view.findViewById(R.id.price);
        detailButton = (Button) view.findViewById(R.id.detailButton);

        //set title and icon
        if (getActivity() instanceof CookMainActivity) {
            nameText.setText("Find Foodies nearby!");
            detailButton.setBackgroundResource(R.drawable.ic_add_white_24dp);

        } else {
            nameText.setText("Select a meal nearby!");
        }


            //cook detail button
        if (getActivity() instanceof CookMainActivity) {
            detailButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //check if already selling meal
                    SharedPreferences myPrefs =  PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                    String userID = myPrefs.getString(Constants.USER_ID, "");
                    Firebase mealID = Modules.connectDB(getActivity(), "/users/" + userID);
                    mealID.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            User user = snapshot.getValue(User.class);
                            if (user.getMealSellingID().equals("")) {
                                Intent intent = new Intent(getActivity(), NewMealActivity.class);
                                startActivity(intent);
                            } else {
                                Snackbar.make(view, "You can only post one meal at a time!", Snackbar.LENGTH_LONG)
                                        .setAction("Pending Meals", pendingMeals)
                                        .show();
                            }
                        }
                        @Override
                        public void onCancelled(FirebaseError firebaseError) {}
                    });
                }
            });
        } else { //Foodie detail button
            detailButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(view, "Not yet implemented", Snackbar.LENGTH_LONG)
                            .show();
                }
            });
        }



        nameText.setText("Select a meal nearby!");

//        ViewGroup background = (ViewGroup) view.findViewById(R.id.background);
//        Blurry.with(getActivity())
//                .radius(10)
//                .sampling(8)
//                .color(Color.argb(66, 255, 255, 0))
//                .async()
//                .animate(500)
//                .onto(background);
//

        return view;
    }

    View.OnClickListener pendingMeals = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CookMainActivity.mViewPager.setCurrentItem(2);
        }
    };

    public void setName(String name) {
        nameText.setText(name);
    }
    public void setPrice(String price) {
        priceText.setText(price);
    }
    public void setAddress(String address) {
        addressText.setText(address);
    }

    public void setImageView(int id) {
        imageView.setImageResource(id);
        Picasso.with(getContext())
                .load(id)
                .resize(100, 100)
                .centerCrop()
                .into(imageView);
    }

}
