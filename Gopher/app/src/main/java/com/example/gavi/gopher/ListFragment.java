package com.example.gavi.gopher;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ListFragment extends Fragment {

    View view;

    public ListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_list, container, false);

        super.onCreate(savedInstanceState);

        int userType = getUserType(this.getActivity().getApplicationContext());
        if (userType == Constants.FOODIE) {

            List<ListItem> newLists = new ArrayList<ListItem>();

            newLists.add(new ListItem("Grilled Cheese", 7.99 , "Rordon Gamsay", "33rd and North Charles Street", 4.5, 0.3, true, true, false));
            newLists.add(new ListItem("Mac and Cheese", 8.99, "Fobby Blay", "30th and St.Paul's Street", 4.0, 0.3, true, true, false));
            newLists.add(new ListItem("Garlic Tomato", 9.99, "Katherine Liu", "1E 31St Baltimore", 4.9, 0.5, true, false, true));
            newLists.add(new ListItem("Seafood Pizza", 6.99, "Susie Wu", "2801 Univ Pkwy", 4.8, 0.6, true, true, true));

            ArrayAdapter<ListItem> adapter = new ListViewAdapter(getActivity().getApplicationContext(), R.layout.listview_item_layout, newLists);
            final ListView list = (ListView) v.findViewById(R.id.item_listViewTwo);
            list.setAdapter(adapter);

            list.setClickable(true);
            System.out.println("Clickable!!");

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {


                    ListItem food = (ListItem) parent.getAdapter().getItem(position);

                    Intent intent = new Intent(getActivity(), FoodDetails.class);

                    intent.putExtra("KEY_title", food.getTitle());
                    intent.putExtra("KEY_address", food.getAddress());
                    intent.putExtra("KEY_chefname", food.getChefName());
                    intent.putExtra("KEY_rating", Double.toString(food.getRating()));
                    intent.putExtra("KEY_price", Double.toString(food.getPrice()));
                    intent.putExtra("KEY_distance", Double.toString(food.getDistance()));
                    intent.putExtra("KEY_glut", food.getGluten());
                    intent.putExtra("KEY_nut", food.getNut());
                    intent.putExtra("KEY_dairy", food.getDairy());

                    startActivity(intent);

                    System.out.println("intent!!");


                }
            });


        } else {

            List<Human> humanList = new ArrayList<Human>();

            humanList.add(new Human("Gavi R", "Malone Hall", 5.0, 0.3, true, false, false));
            humanList.add(new Human("Will Y", "Mattin Center", 5.0, 0.5, true, true, false));
            humanList.add(new Human("Kathy W", "Garland Hall", 5.0, 0.6, true, true, true));
            humanList.add(new Human("Sue L", "Hodson Hall", 5.0, 0.8, true, false, true));

            ArrayAdapter<Human> adp = new HumanAdapter(getActivity().getApplicationContext(), R.layout.cook_listview_item, humanList);
            ListView humans = (ListView) v.findViewById(R.id.item_listViewTwo);
            humans.setAdapter(adp);

            humans.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    //lalala

                }
            });

        }


        return v;
    }

    public static int getUserType(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(Constants.USER_TYPE, Constants.FOODIE);
    }
}