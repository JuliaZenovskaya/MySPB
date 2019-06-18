package com.example.myspb;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Objects;

public class LookNote extends AppCompatActivity {

    private Double latitude;
    private Double longitude;
    private TextView text;
    private int currentID;
    DBHelper dbHelper;

    public static final String LAT = "LAT";
    public static final String LNG = "LNG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_note);

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        latitude = Double.parseDouble(Objects.requireNonNull(bundle.getString(LAT)));
        longitude = Double.parseDouble(Objects.requireNonNull(bundle.getString(LNG)));

        text = findViewById(R.id.text);

        dbHelper = new DBHelper(this);

        searchText();
    }

    public void searchText(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("myNotes", null, null, null, null, "latitude = " + latitude + ", longitude = " + longitude, null);
        int colIndex = cursor.getColumnIndex("note");
        int idIndex = cursor.getColumnIndex("id");
        if (cursor.moveToFirst()) {
            text.setText(cursor.getString(colIndex));
            currentID = cursor.getInt(idIndex);
        }
    }

    public void onClickDelete(View view) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("myNotes", "id = " + currentID, null);

        Intent startMapIntent = new Intent(LookNote.this, MapsActivity.class);
        startActivity(startMapIntent);
    }

    public void onClickExit(View view) {
        Intent startMapIntent = new Intent(LookNote.this, MapsActivity.class);
        startActivity(startMapIntent);
    }

    public void onClickEdit(View view) {

    }
}
