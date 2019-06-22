package com.example.myspb.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.myspb.DiStorage;
import com.example.myspb.R;
import com.example.myspb.domain.model.Coordinates;
import com.example.myspb.domain.model.Note;
import com.example.myspb.domain.repository.GeoNotesRepository;

import java.util.Objects;

public class LookNote extends AppCompatActivity {

    private Note currentNote;

    private GeoNotesRepository geoNotesRepository;

    public static final String LAT = "LAT";
    public static final String LNG = "LNG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_note);

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;

        currentNote = findCurrentNote(bundle);

        ((TextView) findViewById(R.id.text))
                .setText(currentNote.getText());
    }

    public void onClickDelete(View view) {
        geoNotesRepository.deleteById(currentNote.getId());
        Intent startMapIntent = new Intent(LookNote.this, MapsActivity.class);
        startActivity(startMapIntent);
    }

    public void onClickExit(View view) {
        Intent startMapIntent = new Intent(LookNote.this, MapsActivity.class);
        startActivity(startMapIntent);
    }

    public void onClickEdit(View view) {
        Intent startEditIntent = new Intent(LookNote.this, EditNote.class);
        startEditIntent.putExtra(EditNote.CURRENTID, Integer.toString(currentNote.getId()));
        startActivity(startEditIntent);
    }

    private Note findCurrentNote(Bundle bundle) {
        double latitude = Double.parseDouble(Objects.requireNonNull(bundle.getString(LAT)));
        double longitude = Double.parseDouble(Objects.requireNonNull(bundle.getString(LNG)));

        Coordinates coordinates = new Coordinates(latitude, longitude);
        geoNotesRepository = DiStorage.getInstance().getGeoNotesRepository();
        return geoNotesRepository.findOneByCoordinates(coordinates);
    }
}
