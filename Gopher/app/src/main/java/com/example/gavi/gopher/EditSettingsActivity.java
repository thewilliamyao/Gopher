package com.example.gavi.gopher;

import android.app.Activity;
import android.content.Intent;
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

    View.OnClickListener save = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("name", nameText.getText().toString());
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //enable back button
        setTitle("Settings");

        nameText = (EditText) findViewById(R.id.nameText);
        nameText.setText(getIntent().getStringExtra("name"));
        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(save);
    }
}
