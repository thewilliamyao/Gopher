package com.example.gavi.gopher;

import android.Manifest;

import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.ViewUtils;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;

/**
 * Created by grawson2 on 5/4/16.
 */
public class FoodieMap extends SupportMapFragment implements OnMapReadyCallback, LocationListener {



    private View view;
    private MapView mapView;
    private GoogleMap map;
    private ExpandedMarkerFragment expandedMarkerFrag;
    private Marker mSelectedMarker;
    private HashMap<Marker, Meal> meals;
    private HashMap<String, Marker> markers;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        meals = new HashMap<>();
        markers = new HashMap<>();

        //encapsulate map view inside custom view
        FrameLayout mapView = (FrameLayout) super.onCreateView(inflater, container, savedInstanceState);
        View frag = inflater.inflate(R.layout.fragment_map, null);
        mapView.addView(frag);

        //map callback
        getMapAsync(this);

        //add profile header fragment
        if (expandedMarkerFrag == null) {
            expandedMarkerFrag = new ExpandedMarkerFragment();
            getChildFragmentManager().beginTransaction().add(
                    R.id.expanded_marker_container, expandedMarkerFrag
            ).commit();
        }

        return mapView;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;

        // Add a marker in Sydney, Australia, and move the camera.
        LatLng sydney = new LatLng(-34, 151);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 8));


        //request location permission
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
            map.getUiSettings().setMyLocationButtonEnabled(true);
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[] { Manifest.permission.ACCESS_FINE_LOCATION }, 0);
        }

        map.setOnMarkerClickListener(updateExpandedView);
        loadMeals(map);

    }

    //show details in expanded marker view
    GoogleMap.OnMarkerClickListener updateExpandedView = new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {

            Meal meal = meals.get(marker);

            if (meal != null) {

                //update expanded view
                expandedMarkerFrag.setName(meal.getTitle());
                expandedMarkerFrag.setAddress("$" +  String.format("%.2f", meal.getPrice()) );

                //set selected state
                if (null != mSelectedMarker) {
                    mSelectedMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                }
                mSelectedMarker = marker;
                mSelectedMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

            }
            return true;
        }
    };

    private void loadMeals(final GoogleMap map) {
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
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                removeMarker(dataSnapshot);
            }

            @Override
            protected Object clone() throws CloneNotSupportedException {return super.clone();}
        });
    }

    //add a meal marker
    private void addMarker(DataSnapshot data) {
        Meal meal = data.getValue(Meal.class);
        LatLng coordinate = new LatLng(meal.getxCoordinate(), meal.getyCoordinate());
        Marker marker = map.addMarker(new MarkerOptions()
                .position(coordinate));

        //add to hashmaps
        markers.put(data.getKey(), marker);
        meals.put(marker, meal);
    }


    @Override
    public void onLocationChanged(Location location) {
        //request location permission
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LatLng coordinate = new LatLng(location.getLatitude(), location.getLongitude() );
            map.addMarker(new MarkerOptions()
                    .title("Current Location")
                    .position(coordinate)
            );
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 13));
        }
    }

    //remove marker from map
    private void removeMarker(DataSnapshot data) {
        String key = data.getKey();
        Marker toRemove = markers.get(key);
        toRemove.remove();
        meals.remove(toRemove);
        markers.remove(key);
    }

    @Override
    public void onProviderDisabled(String s) {}

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {}

    @Override
    public void onProviderEnabled(String s) {}


}
