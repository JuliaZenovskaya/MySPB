package com.example.myspb.domain.repository;

import com.example.myspb.domain.model.Coordinates;
import com.example.myspb.domain.model.Note;

import java.util.List;

public interface GeoNotesRepository {

    List<? extends Note> findByName(String name);

    Note findOneByCoordinates(Coordinates coordinates);

    void saveNote(Note note);

    void deleteById(int noteId);
}
