package com.example.myspb;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Objects;

public class AddNewNote extends AppCompatActivity {

    private Double lng;
    private Double lat;
    private EditText noteText;
    DBHelper dbHelper;

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

        dbHelper = new DBHelper(this);
    }

    public void onClickSave(View view) {
        ContentValues cv = new ContentValues();
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        cv.put("latitude", lat);
        cv.put("longitude", lng);
        cv.put("note", noteText.getText().toString());

        db.insert("myNotes", null, cv);

        Intent startMapIntent = new Intent(AddNewNote.this, MapsActivity.class);
        startActivity(startMapIntent);
    }

    public void onClickExit(View view) {
        Intent startMapIntent = new Intent(AddNewNote.this, MapsActivity.class);
        startActivity(startMapIntent);
    }
}
