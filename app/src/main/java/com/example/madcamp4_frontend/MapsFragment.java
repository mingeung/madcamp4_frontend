package com.example.madcamp4_frontend;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private boolean isStarted = false;
    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.maps);
        mapFragment.getMapAsync(this);

        // Button
        Button clickMeButton = view.findViewById(R.id.clickMeButton);
        clickMeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle button click
                isStarted = !isStarted; // Toggle the state

                if (isStarted) {
                    Log.d("MapsFragment", "Button Clicked! - Status: start");
                    clickMeButton.setText("Stop");
                    startLocationUpdates();
                } else {
                    Log.d("MapsFragment", "Button Clicked! - Status: stop");
                    clickMeButton.setText("Start");
                    stopLocationUpdates();
                }

                // You can perform any action here when the button is clicked
            }
        });

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng seoul = new LatLng(37.5665, 126.9780);
        mMap.addMarker(new MarkerOptions().position(seoul).title("서울 마커"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(seoul));

        // Initialize location manager and listener
        locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();

                // Handle the received location
                Log.d("MapsFragment", "Received location update - Latitude: " + latitude + ", Longitude: " + longitude);

                // Call the custom method to handle location updates
                onLocationUpdate(latitude, longitude);

                // Update map or perform any other action with the received location
                LatLng userLocation = new LatLng(latitude, longitude);
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(userLocation).title("사용자 위치"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };
    }

    private void startLocationUpdates() {
        // 위치 권한 확인
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // Request location updates
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    1000, // Update every 1 second
                    1,   // Update when the distance is 1 meter
                    locationListener);
        } else {
            // Handle the case where location permission is not granted
            Log.e("MapsFragment", "Location permission not granted");
        }
    }

    private void stopLocationUpdates() {
        // Remove location updates
        locationManager.removeUpdates(locationListener);
    }

    // Custom method to handle location updates
    private void onLocationUpdate(double latitude, double longitude) {
        // Implement your logic here for handling location updates
    }
}
