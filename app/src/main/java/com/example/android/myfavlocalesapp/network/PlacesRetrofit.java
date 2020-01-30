package com.example.android.myfavlocalesapp.network;


import com.example.android.myfavlocalesapp.model.PlacesResponse;
import com.example.android.myfavlocalesapp.util.Constants;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class PlacesRetrofit {

    private final String BASE_URL = "https://maps.googleapis.com";

    private MapsInterface mapsInterface;

    public PlacesRetrofit() {
        mapsInterface = create(getInstance());
    }

    private Retrofit getInstance() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private MapsInterface create(Retrofit retrofit) {
        return retrofit.create(MapsInterface.class);
    }

    public Observable<PlacesResponse> getLocationsNearby(
            String latlong,
            int radius,
            String type,
            String keyword
    ) {
        return mapsInterface.getPlaceFromLocation(latlong, radius, type, keyword, Constants.API_KEY);
    }
}
