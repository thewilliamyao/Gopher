package com.example.gavi.gopher;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_list, container, false);

        List<ListItem> newLists = new ArrayList<ListItem>();

        newLists.add(new ListItem("Grilled Cheese", 7.99 , "Rordon Gamsay", "33rd and North Charles Street", 4.5, 0.3, true, true, false));
        newLists.add(new ListItem("Mac and Cheese", 8.99, "Fobby Blay", "30th and St.Paul's Street", 4.0, 0.3, true, true, false));
        newLists.add(new ListItem("Garlic Tomato", 9.99, "Katherine Liu", "1E 31St Baltimore", 4.9, 0.5, true, false, true));
        newLists.add(new ListItem("Seafood Pizza", 6.99, "Susie Wu", "2801 Univ Pkwy", 4.8, 0.6, true, true, true));

        ArrayAdapter<ListItem> adapter = new ListViewAdapter(getActivity().getApplicationContext(), R.layout.listview_item_layout, newLists);
        ListView list = (ListView) v.findViewById(R.id.item_listViewTwo);
        list.setAdapter(adapter);

        return v;
    }

}