package com.example.myspb;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.myspb.Model.DBHelper;

import java.util.Objects;

public class EditNote extends AppCompatActivity {

    private EditText text;
    private int currentID;
    DBHelper dbHelper;

    public static final String CURRENTID = "CURRENTID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        currentID = Integer.parseInt(Objects.requireNonNull(bundle.getString(CURRENTID)));

        text = findViewById(R.id.editNote);

        dbHelper = new DBHelper(this);
        showPreviousText();
    }

    public void showPreviousText(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "SELECT * FROM myNotes WHERE id = " + currentID + ";";
        String forText = "";
        Cursor cursor = db.rawQuery(sql,null);
        if (cursor.moveToFirst()) {
            int colIndex = cursor.getColumnIndex("note");
            forText = cursor.getString(colIndex);
        }
        cursor.close();
        text.setText(forText);
    }

    public void onClickSave(View view) {
        updateNote();
        Intent startMapIntent = new Intent(EditNote.this, MapsActivity.class);
        startActivity(startMapIntent);
    }

    public void onClickExit(View view) {
        Intent startMapIntent = new Intent(EditNote.this, MapsActivity.class);
        startActivity(startMapIntent);
    }

    public void updateNote(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("note", text.getText().toString());
        String[] forID = new String[] {Integer.toString(currentID)};
        db.update("myNotes", cv, "id = ?", forID);
    }
}
