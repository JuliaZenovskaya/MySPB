package com.example.myspb;

import android.content.Context;

import com.example.myspb.data.DBHelper;
import com.example.myspb.data.GeoNotesRepositoryImpl;
import com.example.myspb.domain.repository.GeoNotesRepository;

/**
 * It supposed to be a DI
 * It should be replaced with Dagger2
 */
public class DiStorage {

    private static DiStorage ourInstance = null;

    public static DiStorage getInstance() {
        return ourInstance;
    }

    private final GeoNotesRepository geoNotesRepository;

    private DiStorage(Context ctx) {
        geoNotesRepository = new GeoNotesRepositoryImpl(new DBHelper(ctx));
    }

    static void createInstance(Context ctx) {
        ourInstance = new DiStorage(ctx);
    }

    public GeoNotesRepository getGeoNotesRepository() {
        return geoNotesRepository;
    }
}
