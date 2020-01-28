package com.example.android.myfavlocalesapp.database;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(version = 1, entities = {LocationEntity.class}, exportSchema = false)
public abstract class LocationDatabase extends RoomDatabase {

    private static LocationDatabase locationDatabaseInstance;

    public abstract LocationDAO locationDAO();

    public static LocationDatabase getInstance(Context context){
        if(locationDatabaseInstance == null){
            locationDatabaseInstance = Room.databaseBuilder(
                    context, LocationDatabase.class, "locations"
            ).build();
        }
        return locationDatabaseInstance;
    }

}
