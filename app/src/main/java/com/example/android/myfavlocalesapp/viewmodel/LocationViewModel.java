package com.example.android.myfavlocalesapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.android.myfavlocalesapp.database.LocationDatabase;
import com.example.android.myfavlocalesapp.database.LocationEntity;

import java.util.List;

import io.reactivex.Observable;

public class LocationViewModel extends AndroidViewModel {

    private LocationDatabase locationDatabase;

    public LocationViewModel(@NonNull Application application) {
        super(application);
        locationDatabase = LocationDatabase.getInstance(application);
    }

    Observable<List<LocationEntity>> getLocations(){
        return locationDatabase.locationDAO().getAllLocations();
    }

    /*Observable<LocationDatabase> getLocation(String locationTitleName){
        return locationDatabase.locationDAO().getLocation(locationTitleName);
    }*/

    void addLocation(LocationEntity locationEntity){
        locationDatabase.locationDAO().insertLocation(locationEntity);
    }

    void updateLocation(LocationEntity locationEntity){
        locationDatabase.locationDAO().updateLocation(locationEntity);
    }

    void deleteLocation(LocationEntity locationEntity){
        locationDatabase.locationDAO().deleteLocation(locationEntity);
    }
}
