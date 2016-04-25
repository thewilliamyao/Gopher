package com.example.gavi.gopher;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class UserSwitcherActivity extends AppCompatActivity {

    private boolean foodie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        foodie = true;
        chooseUI();
    }

    private void chooseUI() {
        if (foodie) {
            Intent intent = new Intent(this, FoodieMainActivity.class);
            startActivity(intent);
        } else {
//            Intent intent = new Intent(this, FoodieMainActivity.class);
//            startActivity(intent);
        }
    }

}
