package com.example.gavi.gopher;


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


/**
 * A simple {@link Fragment} subclass.
 */
public class ExpandedMarkerFragment extends Fragment {

    private static double x = -34.1;
    private static double y = 151.1;
    Firebase lastAdded;

    private TextView nameText;
    private TextView addressText;
    private ImageView imageView;
    private View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_expanded_marker, container, false);
        nameText = (TextView) view.findViewById(R.id.firstName);
        addressText = (TextView) view.findViewById(R.id.addressText);
        imageView = (ImageView) view.findViewById(R.id.imageView);

        //DELETE THIS
        ((Button) view.findViewById(R.id.addButton)).setOnClickListener(addMeal);
        ((Button) view.findViewById(R.id.removeButton)).setOnClickListener(removeMeal);

        return view;
    }

    public void setName(String name) {
        nameText.setText(name);
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


    View.OnClickListener addMeal = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Firebase firebase = Modules.connectDB(getActivity(), "/meals");
            Firebase newMeal = firebase.push();
            Meal toAdd = new Meal("Chicken", 10.00, "desc", x, y);
            x += 0.1;
            y += 0.1;
            newMeal.setValue(toAdd);
            lastAdded = newMeal;
        }
    };

    View.OnClickListener removeMeal = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Firebase firebase = Modules.connectDB(getActivity(), "/meals");
            if (lastAdded != null) {
                lastAdded.removeValue();
            }
        }
    };

}
