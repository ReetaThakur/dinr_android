package com.godynamo.dinr.controller;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.godynamo.dinr.R;
import com.godynamo.dinr.db.DinrSession;
import com.godynamo.dinr.model.Opening;
import com.godynamo.dinr.model.Restaurant;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dankovassev on 15-01-26.
 */
public class FragmentMap extends Fragment implements OnMapReadyCallback {
    MapView mapView;
    GoogleMap map;
    View v;

    Bundle si;

    ArrayList<Marker> markers;

    final static int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 11;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container,
                savedInstanceState);
        v = inflater.inflate(R.layout.fragment_map, container, false);


        mapView = (MapView) v.findViewById(R.id.mapView);
        mapView.onCreate(si);


        // Gets to GoogleMap from the MapView and does initialization stuff
        if (map == null) {
            mapView.getMapAsync(this);
        }

        markers = new ArrayList<Marker>();


        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    public void updateMarkers() {

        if (map != null) {
            map.clear();

            for (Marker currentMarker : markers) {
                currentMarker.remove();
            }
            markers.clear();

            boolean addRestaurant = false;

            List<Restaurant> restaurants = DinrSession.getInstance().getRestaurants();

            if (restaurants == null) {
                Intent intent = new Intent(getActivity(), ActivitySplash.class);
                startActivity(intent);
                getActivity().finish();
            }

            for (Restaurant restaurant : restaurants) {
                if (restaurant.getOpenings().size() > 0) {

                    for (Opening o : restaurant.getOpenings()) {
                        if (o.getState().equalsIgnoreCase("free")) {
                            addRestaurant = true;
                        }
                    }

                    if (addRestaurant) {
                        addRestaurant = false;
                        markers.add(map.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(restaurant.getLatitude()), Double.parseDouble(restaurant.getLongitude()))).title(restaurant.getName())));
                    }
                }
            }

            if (DinrSession.getInstance().getSelectedCity() != null) {

                if (DinrSession.getInstance().getSelectedCity().getLatitude() != null &&
                        DinrSession.getInstance().getSelectedCity().getLongitude() != null) {

                    Double cityLat = Double.valueOf(DinrSession.getInstance().getSelectedCity().getLatitude());
                    Double cityLong = Double.valueOf(DinrSession.getInstance().getSelectedCity().getLongitude());

                    CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(new LatLng(cityLat, cityLong), 10);
                    map.moveCamera(cu);
                }


            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        setUpMap();
    }

    public void setUpMap() {

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            List<Restaurant> restaurants = DinrSession.getInstance().getRestaurants();

            if (restaurants == null) {
                Intent intent = new Intent(getActivity(), ActivitySplash.class);
                startActivity(intent);
                getActivity().finish();
            }

            for (Marker currentMarker : markers) {
                currentMarker.remove();
            }
            markers.clear();

            for (Restaurant restaurant : restaurants) {
                if (restaurant.getOpenings().size() > 0) {
                    markers.add(map.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(restaurant.getLatitude()), Double.parseDouble(restaurant.getLongitude()))).title(restaurant.getName())));
                }
            }

            map.setMyLocationEnabled(true);

            map.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                @Override
                public void onMyLocationChange(Location location) {
                    if (((ActivityMain) getActivity()).location) {
                        ((ActivityMain) getActivity()).location = false;

                        updateMarkers();

                        if (DinrSession.getInstance().getSelectedCity() != null) {

                            Double cityLat = Double.valueOf(DinrSession.getInstance().getSelectedCity().getLatitude());
                            Double cityLong = Double.valueOf(DinrSession.getInstance().getSelectedCity().getLongitude());

                            CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(new LatLng(cityLat, cityLong), 10);
                            map.moveCamera(cu);

                        } else {
                            CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 10);
                            map.moveCamera(cu);
                        }

                        ((ActivityMain) getActivity()).refreshData();
                    }
                }
            });

            // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
            MapsInitializer.initialize(this.getActivity());

            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location == null) {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

            if (location == null) {
                location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            }

            if (location != null) {

                if (DinrSession.getInstance().getSelectedCity() != null) {

                    Double cityLat = Double.valueOf(DinrSession.getInstance().getSelectedCity().getLatitude());
                    Double cityLong = Double.valueOf(DinrSession.getInstance().getSelectedCity().getLongitude());

                    CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(new LatLng(cityLat, cityLong), 10);
                    map.moveCamera(cu);

                } else {
                    CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 10);
                    map.moveCamera(cu);
                }
            }
        }else{
            askForPermission();
        }
    }

    private void askForPermission(){
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    setUpMap();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }
}