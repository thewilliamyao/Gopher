package com.example.gavi.gopher;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class PendingOrdersFragment extends Fragment {

    protected static List<ListItem> newLists;

    public static final int MENU_ITEM_EDITVIEW = Menu.FIRST;
    public static final int MENU_ITEM_DELETE = Menu.FIRST + 1;
    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        v = inflater.inflate(R.layout.fragment_pending_orders, container, false);

        /*List<ListItem> newLists = new ArrayList<ListItem>();

        newLists.add(new ListItem("Grilled Cheese", 7.99 , "Rordon Gamsay", "33rd and North Charles Street", 4.5, 0.5, true, true, false));
        newLists.add(new ListItem("Mac and Cheese", 8.99, "Fobby Blay", "30th and St.Paul's Street", 4.0, 0.3, true, false, false));

        ArrayAdapter<ListItem> adapter = new ListAdapter(getActivity().getApplicationContext(), R.layout.list_item_layout, newLists);
        ListView list = (ListView) v.findViewById(R.id.item_listView);
        System.out.println("About to fail");
        list.setAdapter(adapter);
        System.out.println("About to fail");*/

        //registerClickCallback();
        loadPending();


        return v;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        /*final Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();*/
        return true;

    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        // create menu in code instead of in xml file (xml approach preferred)
        menu.setHeaderTitle("Options");

        // Add menu items
        menu.add(0, MENU_ITEM_EDITVIEW, 0, "Edit");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        super.onContextItemSelected(item);

        AdapterView.AdapterContextMenuInfo menuInfo;
        menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = menuInfo.position; // position in array adapter

        switch (item.getItemId()) {
            case MENU_ITEM_EDITVIEW: {
                System.out.println("Hello");
                return false;
            }
        }
        return false;
    }

    private void loadPending() {
        SharedPreferences myPrefs =  PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        String userID = myPrefs.getString(Constants.USER_ID, "");
        Firebase firebase = Modules.connectDB(getActivity(), "/users" + "/" + userID);


        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println(snapshot);
                User user = snapshot.getValue(User.class);

                String mealID;
                SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                int userType = myPrefs.getInt(Constants.USER_TYPE, Constants.FOODIE);
                if (userType == Constants.FOODIE) {
                    mealID = user.getMealBuyingID();
                } else {
                    mealID = user.getMealSellingID();
                }

                if (mealID != null) {
                    Firebase mealbase = Modules.connectDB(getActivity(), "/meals" + "/" + mealID);
                    mealbase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            Meal meal = snapshot.getValue(Meal.class);

                            TextView title = (TextView) v.findViewById(R.id.food_title);
                            title.setText(meal.getTitle());

                            /*TextView chefName = (TextView) itemView.findViewById(R.id.chef_name);
                               chefName.setText(meal.getChefName());*/

                            TextView price = (TextView) v.findViewById(R.id.price);
                            price.setText(Double.toString(meal.getPrice()));

                            TextView address = (TextView) v.findViewById(R.id.address);
                            address.setText(meal.getAddress());

                        }


                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                            System.out.println("The read failed: " + firebaseError.getMessage());
                        }
                    });
                } else {
                    System.out.println("No meals to display.");
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }


}

