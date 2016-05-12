package com.example.gavi.gopher;

import android.Manifest;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.ViewUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by grawson2 on 5/4/16.
 */
public class FoodieMap extends SupportMapFragment implements OnMapReadyCallback, LocationListener {

    private BitmapDescriptor markerIcon;
    private BitmapDescriptor selectedMarkerIcon;
    private View view;
    private MapView mapView;
    private GoogleMap map;
    private ExpandedMarkerFragment expandedMarkerFrag;
    private Marker mSelectedMarker;
    private static HashMap<Marker, Meal> meals = new HashMap<>(); //maps marker to meal
    private static HashMap<String, Marker> markers = new HashMap<>(); //maps meal id to marker

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //encapsulate map view inside custom view
        FrameLayout mapView = (FrameLayout) super.onCreateView(inflater, container, savedInstanceState);
        View frag = inflater.inflate(R.layout.fragment_map, null);
        mapView.addView(frag);

        Firebase.setAndroidContext(getActivity());

        //set custom markers
        if (markerIcon == null && selectedMarkerIcon == null) {
            markerIcon = customMarker(R.drawable.meal_marker);
            selectedMarkerIcon = customMarker(R.drawable.meal_marker_selected);
        }

        //add profile header fragment
        if (expandedMarkerFrag == null) {
            Log.d("log", "new expanded view");
            expandedMarkerFrag = new ExpandedMarkerFragment();
            getChildFragmentManager().beginTransaction().add(
                    R.id.expanded_marker_container, expandedMarkerFrag
            ).commit();
        }


        return mapView;
    }

    @Override
    public void onResume() {
        super.onResume();

        //map callback
        if (this.map == null) {
            getMapAsync(this);
        } else {
            reloadMarkers();
        }

    }

    @Override
    public void onDestroyView() {
        mSelectedMarker = null;
        super.onDestroyView();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;

        // move camera to current location
        //request location permission
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
            map.getUiSettings().setMyLocationButtonEnabled(true);

            //zoom in on current location
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String provider = locationManager.getBestProvider(criteria, true);
            Location location = locationManager.getLastKnownLocation(provider);
            if(location!=null){
                onLocationChanged(location);
            }
//                locationManager.requestLocationUpdates(provider, 20000, 0, this);

            map.setOnMarkerClickListener(updateExpandedView);
            loadMeals();
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[] { Manifest.permission.ACCESS_FINE_LOCATION }, 0);
        }


    }

    private void reloadMarkers() {
        map.clear();
        mSelectedMarker = null;

        HashMap <String, Marker> tempMarkers = new HashMap<>();
        HashMap <Marker, Meal> tempMeals = new HashMap<>();

        for (Map.Entry<String, Marker> entry: markers.entrySet()) {
            LatLng coordinate = entry.getValue().getPosition();
            Marker newMarker = map.addMarker(new MarkerOptions()
                    .position(coordinate)
                    .icon(markerIcon)
            );

            //add to temp hashmaps
            tempMarkers.put(entry.getKey(), newMarker);
            tempMeals.put(newMarker, meals.get(entry.getValue()));
        }

        //update hashmaps
        markers = tempMarkers;
        meals = tempMeals;
    }

    //show details in expanded marker view
    GoogleMap.OnMarkerClickListener updateExpandedView = new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {

            final Marker markerRef = marker;
            Meal meal = meals.get(marker);

            if (meal != null) {

                //update expanded view
                expandedMarkerFrag.setName(meal.getTitle());
                expandedMarkerFrag.setPrice("$" +  String.format("%.2f", meal.getPrice()) );
                expandedMarkerFrag.setAddress(meal.getAddress());
                expandedMarkerFrag.setUser(meal);

//                //set image
//                expandedMarkerFrag.startAnim();
//                Firebase imagePath = Modules.connectDB(getActivity(), "/meal_images/" + meal.getId());
//                imagePath.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        if (mSelectedMarker == markerRef) {
//
//                            //no data
//                            if (!dataSnapshot.hasChildren()) {
//                                expandedMarkerFrag.stopAnim();
//                                expandedMarkerFrag.setDefaultImage();
//                            } else {
//                                for (DataSnapshot postSnap: dataSnapshot.getChildren()) {
//                                    String encoded = postSnap.getValue().toString();
//                                    expandedMarkerFrag.stopAnim();
//                                    expandedMarkerFrag.setImage(Modules.decodeBase64(encoded));
//                                }
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(FirebaseError firebaseError) {}
//                });

                //set selected state
                if (null != mSelectedMarker) {
                    mSelectedMarker.setIcon(markerIcon);
                }
                mSelectedMarker = marker;
                mSelectedMarker.setIcon(selectedMarkerIcon);

            }
            return true;
        }
    };

    private void loadMeals() {
        Firebase firebase = Modules.connectDB(getActivity(), "/meals");
        firebase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                addMarker(snapshot);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(FirebaseError firebaseError) {}

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                String key = data.getKey();
//                Marker toRemove = markers.get(key);
//                toRemove.remove();
//                meals.remove(toRemove);
//                markers.remove(key);
//
//                Log.d("log", dataSnapshot.toString());

                clearExpandedView();
                removeMarker(dataSnapshot);
                addMarker(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                clearExpandedView();
                removeMarker(dataSnapshot);
            }

            @Override
            protected Object clone() throws CloneNotSupportedException {return super.clone();}
        });
    }

    //add a meal marker
    private void addMarker(DataSnapshot data) {
        Meal meal = data.getValue(Meal.class);

        if (!meal.isBought()) { //only add to map is meal is not yet bought
            try {

                if (meal.getAddress() != null) {
                    Address a = Modules.addressToCoordinate(meal.getAddress(), getActivity());
                    LatLng coordinate = new LatLng(a.getLatitude(), a.getLongitude());
                    Marker marker = map.addMarker(new MarkerOptions()
                            .position(coordinate)
                            .icon(markerIcon)
                    );

                    //add to hashmaps
                    markers.put(data.getKey(), marker);
                    meals.put(marker, meal);
                }

            } catch (IOException e) {
                //error
            }
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        //request location permission
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LatLng coordinate = new LatLng(location.getLatitude(), location.getLongitude() );
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 16));
        }
    }

    private void clearExpandedView() {
        mSelectedMarker = null;
        expandedMarkerFrag.setEmptyTitle();
        expandedMarkerFrag.setPrice("");
        expandedMarkerFrag.setAddress("");
    }

    //remove marker from map
    private void removeMarker(DataSnapshot data) {
        String key = data.getKey();
        if (markers.containsKey(key)) {
            Marker toRemove = markers.get(key);
            toRemove.remove();
            meals.remove(toRemove);
            markers.remove(key);
        }
    }

    private BitmapDescriptor customMarker(int id) {
        Drawable d = getResources().getDrawable(id); // programatically create drawable
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(d.getIntrinsicWidth(), d.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        d.draw(canvas);
        return  BitmapDescriptorFactory.fromBitmap(bitmap);

    }

    @Override
    public void onProviderDisabled(String s) {}

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {}

    @Override
    public void onProviderEnabled(String s) {}


}
