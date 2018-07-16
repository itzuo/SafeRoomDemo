package com.zxj.saferoom;

import android.app.Application;

import com.zxj.saferoom.db.AppDatabase;

public class App extends Application {
    private static App instance;
    public static App getInstance(){
        return instance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public AppDatabase getDatabase() {
        return AppDatabase.getInstance(this);
    }
}
