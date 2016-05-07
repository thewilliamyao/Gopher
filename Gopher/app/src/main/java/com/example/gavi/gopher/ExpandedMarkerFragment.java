package com.example.gavi.gopher;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.squareup.picasso.Picasso;

import jp.wasabeef.blurry.Blurry;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExpandedMarkerFragment extends Fragment {


    Firebase lastAdded;

    private TextView nameText;
    private TextView addressText;
    private TextView priceText;
    private ImageView imageView;
    private View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_expanded_marker, container, false);
        nameText = (TextView) view.findViewById(R.id.firstName);
        addressText = (TextView) view.findViewById(R.id.address);
        imageView = (ImageView) view.findViewById(R.id.imageView);
        priceText = (TextView) view.findViewById(R.id.price);

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


//    View.OnClickListener addMeal = new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            Firebase firebase = Modules.connectDB(getActivity(), "/meals");
//            Firebase newMeal = firebase.push();
//            Meal toAdd = new Meal("Chicken", 10.00, "desc", "3900 north Charles st, Baltimore 21218");
//            newMeal.setValue(toAdd);
//            lastAdded = newMeal;
//        }
//    };
//
//    View.OnClickListener removeMeal = new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            Firebase firebase = Modules.connectDB(getActivity(), "/meals");
//            if (lastAdded != null) {
//                lastAdded.removeValue();
//            }
//        }
//    };

}
