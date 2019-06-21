package com.example.myspb.domain.model;

public class Note {

    private final int id;
    private final Coordinates coordinates;
    private final String text;

    public Note(int id, Coordinates coordinates, String text) {
        this.id = id;
        this.coordinates = coordinates;
        this.text = text;
    }

    public Note(Coordinates coordinates, String text) {
        this(-1, coordinates, text); // -1 or 0, if SQLite starts counting from 1
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public String getText() {
        return text;
    }

    public int getId() {
        return id;
    }
}
