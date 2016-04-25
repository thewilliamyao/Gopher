package com.example.gavi.gopher;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

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

        return view;
    }

}
