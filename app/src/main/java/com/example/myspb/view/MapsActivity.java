package com.example.myspb.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.myspb.DiStorage;
import com.example.myspb.R;
import com.example.myspb.domain.model.Coordinates;
import com.example.myspb.domain.model.Note;
import com.example.myspb.domain.repository.GeoNotesRepository;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final int PERMISSION_REQUEST_CODE = 123;
    private EditText searchText;
    private boolean added = true;
    LatLng latLng;
    Marker markerForAdd;

    private GeoNotesRepository geoNotesRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPermission();
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        geoNotesRepository = DiStorage.getInstance().getGeoNotesRepository();

        searchText = findViewById(R.id.searchOnMap);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLngBounds SPB = new LatLngBounds(
                new LatLng(59.77, 29.98), new LatLng(60.14, 30.54));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SPB.getCenter(), 10));
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
            }

            @Override
            public void onMarkerDrag(Marker marker) {
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (added) {
                    Intent startLookIntent = new Intent(MapsActivity.this, LookNote.class);
                    startLookIntent.putExtra(LookNote.LAT, Double.toString(marker.getPosition().latitude));
                    startLookIntent.putExtra(LookNote.LNG, Double.toString(marker.getPosition().longitude));
                    startActivity(startLookIntent);
                }
                return true;
            }
        });

        showPlaces();
    }

    public void onClickSearch(View view) {
        showPlaces();
    }

    public void onClickAddNewNotePartTwo(View view) {
        latLng = markerForAdd.getPosition();
        added = true;
        Intent startAddIntent = new Intent(this, AddNewNote.class);
        startAddIntent.putExtra(AddNewNote.LATITUDE, Double.toString(latLng.latitude));
        startAddIntent.putExtra(AddNewNote.LONGITUDE, Double.toString(latLng.longitude));
        startActivity(startAddIntent);
    }

    public void onClickAddNewNote(View view) {
        added = false;
        ImageButton btnAdd = view.findViewById(R.id.btnAddNewNote);
        btnAdd.setVisibility(View.INVISIBLE);

        Location myLocation = mMap.getMyLocation();
        LatLng latLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
        markerForAdd = mMap.addMarker(new MarkerOptions().position(latLng)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .draggable(true));

    }

    public void showPlaces() {
        mMap.clear();
        List<? extends Note> notes = geoNotesRepository.findByName(searchText.getText().toString());
        for (Note note : notes) {
            Coordinates coordinates = note.getCoordinates();
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(new LatLng(coordinates.getLat(), coordinates.getLon()));
            mMap.addMarker(markerOptions);
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        boolean allowed = false;

        if (requestCode == PERMISSION_REQUEST_CODE) {
            allowed = true;
        }

        if (allowed) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
            }
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
        }

    }
}


