package com.example.android.myfavlocalesapp.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Observable;

@Dao
public interface LocationDAO {

    @Query("SELECT * FROM locations")
    Observable<List<LocationEntity>> getAllLocations();

    /*@Query("SELECT * FROM locations WHERE location_title = :locationTitle")
    Observable<LocationDatabase> getLocation(String locationTitle);*/

    @Insert
    void insertLocation(LocationEntity locationEntity);

    @Delete
    void deleteLocation(LocationEntity locationEntity);

    @Update
    void updateLocation(LocationEntity locationEntity);

}
