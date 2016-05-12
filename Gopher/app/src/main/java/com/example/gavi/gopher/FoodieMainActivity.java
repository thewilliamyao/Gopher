package com.example.gavi.gopher;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.maps.SupportMapFragment;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class FoodieMainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodie_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        //set tab icons
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_place_white_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_list_white_24dp);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_local_grocery_store_white_24dp);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_person_white_24dp);

        //load persistent meal notification
        Firebase.setAndroidContext(this);
        mealStatus();
    }

    //setup data on page
    private void mealStatus() {

        //get userID
        SharedPreferences myPrefs =  PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String userid = myPrefs.getString(Constants.USER_ID, "");
        Firebase userRef = Modules.connectDB(this, "/users/" + userid + "/mealBuyingID");

        //get user ID -> meal ID to get meal
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                    String mealid = dataSnapshot.getValue().toString();
                    if (!mealid.equals("")) {
                        loadMealStatus(mealid);
                    } else {
                        noMeal();
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });
    }

    //load meal
    private void loadMealStatus(final String mealid) {
        final Firebase mealRef = Modules.connectDB(this, "/meals/" + mealid + "/ready");
        final Activity mainActivity = this;
        mealRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                    boolean ready = Boolean.parseBoolean(dataSnapshot.getValue().toString());
                    if (ready) {
                        mealReady(mainActivity);
                    } else {
                        mealNotReady(mainActivity);
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });
    }

    //show status bar and alert that meal is ready
    private void mealReady(Activity mainActivity) {
        TextView statusBar = (TextView) findViewById(R.id.mealStatus);
        statusBar.setVisibility(View.VISIBLE);
        statusBar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryCook));
        statusBar.setText("Your meal is now ready!");

        new SweetAlertDialog(mainActivity, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Your meal is now ready!")
                .setContentText("Head over to the Cook's residence to pick up your meal!")
                .show();
    }

    //show status bar and alert that meal is not ready
    private void mealNotReady(Activity mainActivity) {
        TextView statusBar = (TextView) findViewById(R.id.mealStatus);
        statusBar.setVisibility(View.VISIBLE);
        statusBar.setBackgroundColor(getResources().getColor(R.color.dark_purple));
        statusBar.setText("Your meal isn't ready!");

        new SweetAlertDialog(mainActivity, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Your meal isn't ready!")
                .setContentText("Wait for your cook to mark that your meal is ready.")
                .show();
    }

    private void noMeal() {
        (findViewById(R.id.mealStatus)).setVisibility(View.GONE);
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0: return new FoodieMap();
                case 1: return new ListFragment();
                case 2: return new FoodiePendingFrag();
                case 3: return new ProfileFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }

    }
}
