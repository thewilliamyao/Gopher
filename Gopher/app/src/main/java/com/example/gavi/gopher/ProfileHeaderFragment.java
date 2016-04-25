package com.example.gavi.gopher;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileHeaderFragment extends Fragment {

    private View view;
    private TextView nameText;
    private Button settingsButton;

    View.OnClickListener editSettings = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), EditSettingsActivity.class);
            intent.putExtra("name", nameText.getText());
            startActivityForResult(intent, 1);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile_header, container, false);
        nameText = (TextView) view.findViewById(R.id.nameText);
        settingsButton = (Button) view.findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(editSettings);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK) {
                System.out.println("NAME: " + data.getExtras().getString("name"));
                nameText.setText(data.getStringExtra("name"));
            }
        }
    }
}
