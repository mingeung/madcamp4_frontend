package com.example.madcamp4_frontend;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.maps);

        mapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        LatLng seoul = new LatLng(37.5665, 126.9780);
        mMap.addMarker(new MarkerOptions()
                .position(seoul)
                .title("서울 마커"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(seoul));
        ((MainActivity) requireActivity()).setLocationUpdateListener(new MainActivity.LocationUpdateListener() {
            @Override
            public void onLocationUpdate(double latitude, double longitude) {
                LatLng userLocation = new LatLng(latitude, longitude);
                mMap.clear();  // 기존 마커 제거
                mMap.addMarker(new MarkerOptions()
                        .position(userLocation)
                        .title("사용자 위치"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));
            }
        });

    }
}