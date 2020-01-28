package com.example.android.myfavlocalesapp.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "locations")
public class LocationEntity {

    @PrimaryKey(autoGenerate = true)
    private int locationId;

    @ColumnInfo(name = "location_title")
    private String locationTitle;

    @ColumnInfo(name = "location_longitude")
    private double longitude;

    @ColumnInfo(name = "location_latitude")
    private double latitude;

    public LocationEntity(int locationId, String locationTitle, double longitude, double latitude) {
        this.locationId = locationId;
        this.locationTitle = locationTitle;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    @Ignore
    public LocationEntity(String locationTitle, double longitude, double latitude) {
        this.locationTitle = locationTitle;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public String getLocationTitle() {
        return locationTitle;
    }

    public void setLocationTitle(String locationTitle) {
        this.locationTitle = locationTitle;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
