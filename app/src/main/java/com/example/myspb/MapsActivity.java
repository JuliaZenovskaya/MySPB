package com.example.myspb;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ImageButton btnAdd;
    LatLng latLng;
    DBHelper dbHelper;
    Marker markerForAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        dbHelper = new DBHelper(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLngBounds SPB = new LatLngBounds(
                new LatLng(59.77, 29.98), new LatLng(60.14, 30.54));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SPB.getCenter(), 10));
        mMap.getUiSettings().setZoomControlsEnabled(true);
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
                Intent startLookIntent = new Intent(MapsActivity.this, LookNote.class);
                startLookIntent.putExtra(LookNote.LAT, Double.toString(marker.getPosition().latitude));
                startLookIntent.putExtra(LookNote.LNG, Double.toString(marker.getPosition().longitude));
                startActivity(startLookIntent);
                return true;
            }
        });

        showMarkers();
    }

    public void onClickSearch(View view) {
    }

    public void onClickAddNewNotePartTwo(View view) {
        latLng = markerForAdd.getPosition();
        Intent startAddIntent = new Intent(this, AddNewNote.class);
        startAddIntent.putExtra(AddNewNote.LATITUDE, Double.toString(latLng.latitude));
        startAddIntent.putExtra(AddNewNote.LONGITUDE, Double.toString(latLng.longitude));
        startActivity(startAddIntent);
    }

    public void onClickAddNewNote(View view) {
        btnAdd = view.findViewById(R.id.btnAddNewNote);
        btnAdd.setVisibility(View.INVISIBLE);

        LatLng latLng = new LatLng(60d, 30.3d);
        markerForAdd = mMap.addMarker(new MarkerOptions().position(latLng).draggable(true));

    }

    public void onClickLocation(View view) {
    }

    public void showMarkers(){
        LatLng newlatlng;
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.query("myNotes", null, null, null, null, null, null);

        if (cursor.moveToFirst()){
            int latColIndex = cursor.getColumnIndex("latitude");
            int lngColIndex = cursor.getColumnIndex("longitude");

            do {
                newlatlng = new LatLng(cursor.getDouble(latColIndex), cursor.getDouble(lngColIndex));

                mMap.addMarker(new MarkerOptions().position(newlatlng).draggable(false));
            } while (cursor.moveToNext());

        }
        cursor.close();

    }
}
