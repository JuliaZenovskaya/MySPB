package com.example.myspb.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.example.myspb.DiStorage;
import com.example.myspb.R;
import com.example.myspb.domain.model.Coordinates;
import com.example.myspb.domain.model.Note;
import com.example.myspb.domain.repository.GeoNotesRepository;

import java.util.Objects;

public class AddNewNote extends AppCompatActivity {

    private Double lng;
    private Double lat;
    private EditText noteText;

    private GeoNotesRepository geoNotesRepository;

    public static final String LATITUDE = "LATITUDE";
    public static final String LONGITUDE = "LONGITUDE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_note);

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        lat = Double.parseDouble(Objects.requireNonNull(bundle.getString(LATITUDE)));
        lng = Double.parseDouble(Objects.requireNonNull(bundle.getString(LONGITUDE)));

        noteText = findViewById(R.id.newNote);

        geoNotesRepository = DiStorage.getInstance().getGeoNotesRepository();
    }

    public void onClickSave(View view) {
        Coordinates coordinates = new Coordinates(lat, lng);
        Note note = new Note(coordinates, noteText.getText().toString());

        geoNotesRepository.saveNote(note);

        Intent startMapIntent = new Intent(AddNewNote.this, MapsActivity.class);
        startActivity(startMapIntent);
    }

    public void onClickExit(View view) {
        Intent startMapIntent = new Intent(AddNewNote.this, MapsActivity.class);
        startActivity(startMapIntent);
    }
}
