package com.example.android.myfavlocalesapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.android.myfavlocalesapp.database.LocationDatabase;
import com.example.android.myfavlocalesapp.database.LocationEntity;
import com.example.android.myfavlocalesapp.model.GoogleLocation;
import com.example.android.myfavlocalesapp.network.PlacesRetrofit;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LocationViewModel extends AndroidViewModel {
    private PlacesRetrofit placesRetrofit = new PlacesRetrofit();
    private LocationDatabase locationDatabase;

    public LocationViewModel(@NonNull Application application) {
        super(application);
        locationDatabase = LocationDatabase.getInstance(application);
    }
    public Single<GoogleLocation> getGoogleLocations(String latLng, int radius,
                                                     String searchType, String searchKeyword) {

       return placesRetrofit.getLocationsNearby(latLng, radius, searchType, searchKeyword).subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread());
    }
    Observable<List<LocationEntity>> getLocations(){
        return locationDatabase.locationDAO().getAllLocations();
    }

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
