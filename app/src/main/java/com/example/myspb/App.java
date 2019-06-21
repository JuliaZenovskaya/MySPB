package com.example.myspb;

import android.app.Application;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DiStorage.createInstance(this);
    }
}
