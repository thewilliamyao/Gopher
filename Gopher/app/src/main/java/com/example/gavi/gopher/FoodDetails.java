package com.example.gavi.gopher;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class FoodDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("here here");
        setContentView(R.layout.activity_food_details);
        Intent intent = getIntent();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView foodname = ((TextView)findViewById(R.id.title));
        TextView cookname = ((TextView)findViewById(R.id.cookname));
        TextView price = ((TextView)findViewById(R.id.price));
        TextView address = ((TextView)findViewById(R.id.address));
        TextView rating = ((TextView)findViewById(R.id.rating));
        TextView dairy = ((TextView)findViewById(R.id.dairy));
        TextView gluten = ((TextView)findViewById(R.id.glut));
        TextView nut = ((TextView)findViewById(R.id.nut));

/* Get values from Intent */
        String title  = intent.getStringExtra("KEY_title");
        String chefname  = intent.getStringExtra("KEY_chefname");
        String pric = intent.getStringExtra("KEY_price");
        String addr  = intent.getStringExtra("KEY_address");
        String rate = intent.getStringExtra("KEY_rating");
        boolean dai = intent.getBooleanExtra("Key_dairy", true);
        boolean glut = intent.getBooleanExtra("Key_gluten", true);
        boolean nu = intent.getBooleanExtra("Key_nut", true);

        foodname.setText(title);
        cookname.setText(chefname);
        price.setText("$" + pric);
        address.setText(addr);
        rating.setText(rate);


        if (dai) {
            dairy.setText("•Dairy Free");
        }

        if (glut) {
            gluten.setText("•Gluten Free");
        }

        if (nu) {
            nut.setText("•Nut Free");
        }

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

}
