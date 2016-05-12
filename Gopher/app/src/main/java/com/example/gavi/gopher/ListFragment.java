package com.example.gavi.gopher;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 */
public class ListFragment extends Fragment {

    View view;

    public ListFragment() {
        // Required empty public constructor
    }

    HashMap<String, Meal> idtomeal = new HashMap<String, Meal>();
    HashMap<String, User> idtouser = new HashMap<String, User>();
    HashMap<String, Integer> keytoindex = new HashMap<String, Integer>();
    HashMap<String, Integer> keytoindex2 = new HashMap<String, Integer>();
    private List<ListItem> newLists = new ArrayList<ListItem>();
    private List<Human> humanList = new ArrayList<Human>();
    private ArrayAdapter<ListItem> adapter;
    private ArrayAdapter<Human> adp;
    private String fullname;
    private int counter = 0;
    private AdapterView p = null;
    private int po = 0;
    private View vi = null;
    FloatingActionButton fab1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_list, container, false);

        fab1 = (FloatingActionButton) v.findViewById(R.id.fab);



        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(getActivity()); //need to do this in every file using firebase

        int userType = getUserType(this.getActivity().getApplicationContext());

        if (userType == Constants.FOODIE) {
            newLists.clear();
            fab1.hide();
            loadMeals();
            adapter = new ListViewAdapter(getActivity().getApplicationContext(), R.layout.listview_item_layout, newLists);


//            for (int i = 0; i < newLists.size();i++) {
//                System.out.println("new" + newLists.get(i).toString());
//            }
            System.out.println();
            final ListView list = (ListView) v.findViewById(R.id.item_listViewTwo);
            list.setAdapter(adapter);
            list.setClickable(true);
            System.out.println("Clickable!!");



            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    p = parent;
                    po = position;
                    vi = view;
                    SharedPreferences myPrefs =  PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                    String userID = myPrefs.getString(Constants.USER_ID, "");
                    Firebase mealID = Modules.connectDB(getActivity(), "/users/" + userID + "/mealBuyingID");
                    mealID.addListenerForSingleValueEvent(new ValueEventListener() {
                        public void onDataChange(DataSnapshot snapshot) {
                            if (snapshot.getValue().equals("")) { //no meal bought
                                ListItem food = (ListItem) p.getAdapter().getItem(po);
                                String theid = food.getId();
                                Meal mmeal = idtomeal.get(theid);
                                System.out.println("List" + food.getTitle());
                                Intent intent = new Intent(getActivity(), FoodDetails.class);


                                intent.putExtra("title", mmeal.getTitle());
                                intent.putExtra("price", mmeal.getPrice());
                                intent.putExtra("description", mmeal.getDescription());
                                intent.putExtra("id", mmeal.getId());

                                startActivity(intent);

                                System.out.println("intent!!");

                            } else {
                                Snackbar.make(vi, "You can only buy one meal at a time!", Snackbar.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {}
                    });
                }
            });


        } else {

            humanList.clear();
            loadUsers();
            adp = new HumanAdapter(getActivity().getApplicationContext(), R.layout.cook_listview_item, humanList);
            ListView humans = (ListView) v.findViewById(R.id.item_listViewTwo);
            humans.setAdapter(adp);
            humans.setClickable(false);

            fab1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vi = getView();
                    //check if already selling meal
                    SharedPreferences myPrefs =  PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                    String userID = myPrefs.getString(Constants.USER_ID, "");
                    Firebase mealID = Modules.connectDB(getActivity(), "/users/" + userID);
                    mealID.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            User user = snapshot.getValue(User.class);
                            if (user.getMealSellingID().equals("")) {
                                Intent intent = new Intent(getActivity(), NewMealActivity.class);
                                startActivity(intent);
                            } else {
                                Snackbar.make(vi, "You can only buy one meal at a time!", Snackbar.LENGTH_LONG).show();
                            }
                        }
                        @Override
                        public void onCancelled(FirebaseError firebaseError) {}
                    });
                }
            });
        }

            return v;
    }
//
//    public void onPause() {
//        super.onPause();
//        humanList.clear();
//        newLists.clear();
//    }
//
//    public void onStop() {
//        super.onStop();
//        humanList.clear();
//        newLists.clear();
//    }


    private void loadMeals() {

        Firebase mealsRef = Modules.connectDB(getActivity(), "/meals");
        mealsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                //cast data to meal
                final Meal meal = dataSnapshot.getValue(Meal.class);
                //check if meal is bought
                if (!meal.isBought()) {
                    //add to array
                    Firebase cookRef = Modules.connectDB(getActivity(), "/users/" + meal.getSellerID());
                    cookRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null) {
                                String fname = dataSnapshot.child("firstName").getValue().toString();
                                String lname = dataSnapshot.child("lastName").getValue().toString();
                                fullname = fname + " " + lname;
//                                names.add(fullname);
                                ListItem mm = new ListItem(meal.getTitle(), meal.getPrice(), fullname, meal.getAddress(), "close by", meal.getId());
                                newLists.add(mm);
                                idtomeal.put(meal.getId(), meal);
//                                newLists.add(mm);
                                keytoindex.put(meal.getId(), newLists.indexOf(mm));
                                adapter.notifyDataSetChanged();
                            }

//                            ListItem mm = new ListItem(meal.getTitle(), meal.getPrice(), fullname , meal.getAddress(), 0.3);
//                            System.out.println("!!" + names.size());
//                            newLists.add(mm);
//                            keytoindex.put(meal.getId(), newLists.indexOf(mm));
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                        }
                    });

//                    ListItem mm = new ListItem(meal.getTitle(), meal.getPrice(), fullname, meal.getAddress(), 0.3);
//                    newLists.add(mm);
//                    keytoindex.put(meal.getId(), newLists.indexOf(mm));
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                Meal meal = dataSnapshot.getValue(Meal.class);

                if (keytoindex.containsKey(meal.getId())) {
                    //index = hashmap.get(meal.getId())
                    //update array at arr[index]
                    Integer index = keytoindex.get(meal.getId());
                    ListItem newmm = new ListItem(meal.getTitle(), meal.getPrice(), fullname, meal.getAddress(), "close by", meal.getId());
                    newLists.set(index, newmm);
                    idtomeal.put(meal.getId(), meal);
                }
                //cast data to meal
//                final Meal meal = dataSnapshot.getValue(Meal.class);
//                System.out.println("#" + meal.getTitle());
//                //check if meal is bought
//                if (!meal.isBought()) {
//                    //add to array
//                    Firebase cookRef = Modules.connectDB(getActivity(), "/users/" + meal.getSellerID());
//                    cookRef.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            if (dataSnapshot.getValue() != null) {
//                                String fname = dataSnapshot.child("firstName").getValue().toString();
//                                String lname = dataSnapshot.child("lastName").getValue().toString();
//                                fullname = fname + " " + lname;
//                                System.out.println("##" + fullname);
////                                names.add(fullname);
//                                ListItem mm = new ListItem(meal.getTitle(), meal.getPrice(), fullname, meal.getAddress(), 0.3, meal.getId());
//                                System.out.println("###" + meal.getTitle());
//                                newLists.add(mm);
//                                idtomeal.put(meal.getId(), meal);
//                                adapter.notifyDataSetChanged();
////                                ListItem mm = new ListItem(meal.getTitle(), meal.getPrice(), fullname , meal.getAddress(), 0.3);
////                                newLists.add(mm);
////                                keytoindex.put(meal.getId(), newLists.indexOf(mm));
//                            }
//
////                            ListItem mm = new ListItem(meal.getTitle(), meal.getPrice(), fullname , meal.getAddress(), 0.3);
////                            System.out.println("!!" + names.size());
////                            newLists.add(mm);
////                            keytoindex.put(meal.getId(), newLists.indexOf(mm));
//                        }
//
//                        @Override
//                        public void onCancelled(FirebaseError firebaseError) {
//                        }
//                    });
//
////                    ListItem mm = new ListItem(meal.getTitle(), meal.getPrice(), fullname, meal.getAddress(), 0.3);
////                    newLists.add(mm);
////                    keytoindex.put(meal.getId(), newLists.indexOf(mm));
//                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

//                Meal meal = dataSnapshot.getValue(Meal.class);
//
//                if (keytoindex.containsKey(meal.getId())) {
//                    //index = hashmap.get(meal.getId())
//                    //update array at arr[index]
//                    Integer index = keytoindex.get(meal.getId());
//                    newLists.remove(index);
//                }
                final List<ListItem> temp = new ArrayList<ListItem>();
                final HashMap<String, Integer> temphash = new HashMap<String, Integer>();
                final HashMap<String, Meal> tempidtomeal = new HashMap<String, Meal>();
                //cast data to meal
                final Meal meal = dataSnapshot.getValue(Meal.class);
                System.out.println("#" + meal.getTitle());
                //check if meal is bought
                if (!meal.isBought()) {
                    //add to array
                    Firebase cookRef = Modules.connectDB(getActivity(), "/users/" + meal.getSellerID());
                    cookRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null) {
                                String fname = dataSnapshot.child("firstName").getValue().toString();
                                String lname = dataSnapshot.child("lastName").getValue().toString();
                                fullname = fname + " " + lname;
                                System.out.println("##" + fullname);
//                                names.add(fullname);
                                ListItem mm = new ListItem(meal.getTitle(), meal.getPrice(), fullname, meal.getAddress(), "close by", meal.getId());
                                System.out.println("###" + meal.getTitle());
                                temp.add(mm);
                                tempidtomeal.put(meal.getId(), meal);
                                temphash.put(meal.getId(), temp.indexOf(mm));
                                newLists = temp;
                                keytoindex = temphash;
                                idtomeal = tempidtomeal;
                                adapter.notifyDataSetChanged();
                                tempidtomeal.clear();
                                temphash.clear();
                                temp.clear();
//                                ListItem mm = new ListItem(meal.getTitle(), meal.getPrice(), fullname , meal.getAddress(), 0.3);
//                                newLists.add(mm);
//                                keytoindex.put(meal.getId(), newLists.indexOf(mm));
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                        }
                    });

//                    ListItem mm = new ListItem(meal.getTitle(), meal.getPrice(), fullname, meal.getAddress(), 0.3);
//                    newLists.add(mm);
//                    keytoindex.put(meal.getId(), newLists.indexOf(mm));
                }

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

    }


    private void loadUsers() {

        Firebase mealsRef = Modules.connectDB(getActivity(), "/users");
        mealsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                //cast data to meal
                User user = dataSnapshot.getValue(User.class);
                Human hh = new Human(user.getFirstName() + " " + user.getLastName(), user.getAddress(), user.getEmail(), "close by", user.getId());
                humanList.add(hh);
                adp.notifyDataSetChanged();
//                idtouser.put(user.getId(), user);
                keytoindex2.put(user.getId(), humanList.indexOf(hh));

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                User user = dataSnapshot.getValue(User.class);

                if (keytoindex2.containsKey(user.getId())) {
                    Integer index = keytoindex2.get(user.getId());
                    Human newmm = new Human(user.getFirstName() + " " + user.getLastName(), user.getAddress(), user.getEmail(), "close by", user.getId());
                    humanList.set(index, newmm);
//                    idtouser.put(user.getId(), user);
                }

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {


            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

    }

    public static int getUserType(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(Constants.USER_TYPE, Constants.FOODIE);
    }

}
