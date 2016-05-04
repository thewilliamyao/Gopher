package com.example.gavi.gopher;

import android.Manifest;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fasterxml.jackson.databind.Module;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MapFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback, LocationListener {

    private View view;
    private MapView mapView;
    private GoogleMap map;
    private ExpandedMarkerFragment expandedMarkerFrag;
    private Marker mSelectedMarker;
    private HashMap<Marker, Integer> images;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        images = new HashMap<Marker, Integer>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_map, container, false);


        // Gets the MapView from the XML layout and creates it
        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        //add profile header fragment
        if (expandedMarkerFrag == null) {
            expandedMarkerFrag = new ExpandedMarkerFragment();
            getChildFragmentManager().beginTransaction().add(
                    R.id.expanded_marker_container, expandedMarkerFrag
            ).commit();
        }

        return view;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;

        //UI settings
        map.getUiSettings().setAllGesturesEnabled(true);
        map.getUiSettings().setZoomGesturesEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);

        //request location permission
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
            map.getUiSettings().setMyLocationButtonEnabled(true);
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[] { Manifest.permission.ACCESS_FINE_LOCATION }, 0);
        }

        //add static coordinate data
        LatLng coordinate = null;
        MapData coordinates = new MapData();
        for (MapData.Coordinate coord: coordinates.coordinates) {
            coordinate = new LatLng(coord.xCoordinate, coord.yCoordinate);
            Marker marker = map.addMarker(new MarkerOptions()
                    .position(coordinate)
                    .title(coord.title)
                    .snippet(coord.address));
            images.put(marker, coord.image); //update images
        }
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 13)); //center camera on last coordinate

        //set expanded view info on marker select
        map.setOnMarkerClickListener(updateExpandedView);

    }

    //show details in expanded marker view
    GoogleMap.OnMarkerClickListener updateExpandedView = new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {

            //update expanded view
            expandedMarkerFrag.setName(marker.getTitle());
            expandedMarkerFrag.setAddress(marker.getSnippet());
            expandedMarkerFrag.setImageView(images.get(marker));

            //set selected state
            if (null != mSelectedMarker) {
                mSelectedMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            }
            mSelectedMarker = marker;
            mSelectedMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
            return true;
        }
    };


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



    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }
}
