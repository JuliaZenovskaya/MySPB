package com.example.myspb.view;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.example.myspb.DiStorage;
import com.example.myspb.R;
import com.example.myspb.data.DBHelper;
import com.example.myspb.domain.model.Coordinates;
import com.example.myspb.domain.model.Note;
import com.example.myspb.domain.repository.GeoNotesRepository;

import java.util.Objects;

public class EditNote extends AppCompatActivity {

    private Note currentNote;

    private GeoNotesRepository geoNotesRepository;
    private EditText text;
    private int currentID;

    public static final String CURRENTID = "CURRENTID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        currentID = Integer.parseInt(Objects.requireNonNull(bundle.getString(CURRENTID)));

        geoNotesRepository = DiStorage.getInstance().getGeoNotesRepository();
        text = findViewById(R.id.editNote);
        currentNote = findCurrentNote(bundle);
        showPreviousText();
    }

    public void showPreviousText(){
        text.setText(currentNote.getText());
    }

    public void onClickSave(View view) {
        geoNotesRepository.updateNote(currentID, text.getText().toString());
        Intent startMapIntent = new Intent(EditNote.this, MapsActivity.class);
        startActivity(startMapIntent);
    }

    public void onClickExit(View view) {
        Intent startMapIntent = new Intent(EditNote.this, MapsActivity.class);
        startActivity(startMapIntent);
    }

    private Note findCurrentNote(Bundle bundle) {
        int currentId = Integer.parseInt(Objects.requireNonNull(bundle.getString(CURRENTID)));
        geoNotesRepository = DiStorage.getInstance().getGeoNotesRepository();

        return geoNotesRepository.findOneById(currentId);
    }
}
