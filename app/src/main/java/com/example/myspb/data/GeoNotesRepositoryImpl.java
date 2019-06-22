package com.example.myspb.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myspb.domain.model.Coordinates;
import com.example.myspb.domain.model.Note;
import com.example.myspb.domain.repository.GeoNotesRepository;

import java.util.ArrayList;
import java.util.List;

public class GeoNotesRepositoryImpl implements GeoNotesRepository {

    private final DBHelper dbHelper;

    public GeoNotesRepositoryImpl(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Override
    public List<? extends Note> findByName(String name) {
        String sql = "SELECT * FROM myNotes WHERE note LIKE '%" + name + "%';";
        return getNotesByQuery(sql);
    }

    @Override
    public Note findOneByCoordinates(Coordinates coordinates) {
        String sql = "SELECT * FROM myNotes WHERE latitude = " + coordinates.getLat()
                + " and longitude = " + coordinates.getLon() + ";";

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try (Cursor cursor = db.rawQuery(sql, null)) {
            if (cursor.moveToFirst()) {
                int noteColIndex = cursor.getColumnIndex("note");
                int idColIndex = cursor.getColumnIndex("id");

                return new Note(cursor.getInt(idColIndex), coordinates, cursor.getString(noteColIndex));
            }
        }

        return null;
    }

    @Override
    public Note findOneById(int id) {
        String sql = "SELECT * FROM myNotes WHERE id = " + id + ";";

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try (Cursor cursor = db.rawQuery(sql, null)) {
            if (cursor.moveToFirst()) {
                int noteColIndex = cursor.getColumnIndex("note");
                int latColIndex = cursor.getColumnIndex("latitude");
                int lngColIndex = cursor.getColumnIndex("longitude");

                Coordinates coordinates =
                        new Coordinates(cursor.getDouble(latColIndex), cursor.getDouble(lngColIndex));

                return new Note(id, coordinates, cursor.getString(noteColIndex));
            }
        }

        return null;
    }

    @Override
    public void saveNote(Note note) {
        ContentValues cv = new ContentValues();
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        cv.put("latitude", note.getCoordinates().getLat());
        cv.put("longitude", note.getCoordinates().getLon());
        cv.put("note", note.getText());

        db.insert("myNotes", null, cv);
    }

    @Override
    public void deleteById(int noteId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("myNotes", "id = " + noteId, null);
    }

    @Override
    public void updateNote(int id, String text) {
        ContentValues cv = new ContentValues();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String[] forID = new String[] {Integer.toString(id)};

        cv.put("note", text);
        db.update("myNotes", cv, "id = ?", forID);
    }

    private List<? extends Note> getNotesByQuery(String sql) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try (Cursor cursor = db.rawQuery(sql, null)) {
            List<Note> notes = new ArrayList<>();
            if (cursor.moveToFirst()) {
                int latColIndex = cursor.getColumnIndex("latitude");
                int lngColIndex = cursor.getColumnIndex("longitude");
                int noteColIndex = cursor.getColumnIndex("note");
                int idColIndex = cursor.getColumnIndex("id");
                do {
                    Coordinates coordinates = new Coordinates(
                            cursor.getDouble(latColIndex),
                            cursor.getDouble(lngColIndex)
                    );
                    notes.add(
                            new Note(
                                    cursor.getInt(idColIndex),
                                    coordinates,
                                    cursor.getString(noteColIndex)
                            )
                    );
                } while (cursor.moveToNext());
            }
            cursor.close();
            return notes;
        }
    }
}
