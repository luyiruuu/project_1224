package com.example.project_1224;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class ViewLocation extends AppCompatActivity implements OnMapReadyCallback {

    private List<LatLng> selectedLocations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_location);

        // 使用靜態變數直接獲取資料
        selectedLocations = bill.selectedLocations;

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        if (selectedLocations == null || selectedLocations.isEmpty()) {
            return;
        }

        for (LatLng location : selectedLocations) {
            googleMap.addMarker(new MarkerOptions().position(location));
        }

        LatLng firstLocation = selectedLocations.get(0);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstLocation, 13));
    }
}
