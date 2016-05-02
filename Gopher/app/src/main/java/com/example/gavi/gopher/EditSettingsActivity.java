package com.example.gavi.gopher;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.firebase.client.Firebase;

import java.util.HashMap;
import java.util.Map;

public class EditSettingsActivity extends AppCompatActivity {

    private EditText fName;
    private EditText lName;
    private Button saveButton;
    private Button addPhotoButton;
    private ImageView userImage;
    private Activity activity;

    //save name to firebase
    View.OnClickListener save = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SharedPreferences myPrefs =  PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String userID = myPrefs.getString(Constants.USER_ID, "null");
            Firebase firebase = Modules.connectDB(activity, "/users");
            Firebase userRef = firebase.child(userID);
            Map<String, Object> name = new HashMap<String, Object>();
            name.put("firstName", nameText.getText().toString());
            userRef.updateChildren(name);
            finish();
        }
    };

    //Add a profile photo
    View.OnClickListener addPhoto = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(
                    Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, 1);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        Modules.setUserUI(this);
        setContentView(R.layout.activity_edit_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //enable back button
        setTitle("Settings");

        fName = (EditText) findViewById(R.id.firstName);
        lName = (EditText) findViewById(R.id.firstName);
        nameText.setText(myPrefs.getString(Constants.USER_NAME, Constants.DEFAULT_NAME));
        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(save);

        userImage = (ImageView) findViewById(R.id.userImage);
        addPhotoButton = (Button) findViewById(R.id.addPhotoButton);
        addPhotoButton.setOnClickListener(addPhoto);
    }

    //get results from selected user photo
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            userImage.setImageURI(selectedImage);

            // String picturePath contains the path of selected Image
        }
    }
}
