package com.example.gavi.gopher;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EditSettingsActivity extends AppCompatActivity {

    private EditText nameText;
    private Button saveButton;
    private SharedPreferences myPrefs;

    //save name to shared preferences
    View.OnClickListener save = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SharedPreferences.Editor peditor = myPrefs.edit();
            peditor.putString(Constants.USER_NAME, nameText.getText().toString());
            peditor.commit();
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //enable back button
        setTitle("Settings");

        myPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        nameText = (EditText) findViewById(R.id.nameText);
        nameText.setText(myPrefs.getString(Constants.USER_NAME, Constants.DEFAULT_NAME));
        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(save);
    }
}
