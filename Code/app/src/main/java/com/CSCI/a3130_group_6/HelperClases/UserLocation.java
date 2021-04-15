package com.CSCI.a3130_group_6.HelperClases;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Class to extract the exact address using Latitude and Longitude
 *
 * @author  Pulkit Garg, Han Yang
 */
public class UserLocation {
    Geocoder geocoder;
    List<Address> addresses;
    Double latitude;
    Double longitude;
    String  radius;
    Activity activity;
    LatLng object;
    String address;
    private final static double AVERAGE_RADIUS_OF_EARTH_KM = 6371;

    public UserLocation(LatLng obj, Activity activity) {
        this.object = obj;
        this.latitude = obj.latitude;
        this.longitude = obj.longitude;
        this.activity = activity;
    }

    public UserLocation(Double latitude, Double longitude, String radius){
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
    }

    public UserLocation(){ }

    /**
     * Function: Method to create exact address using latitude and longitude
     * Parameters:
     * Returns: void
     *
     */
    public void createAddress() throws IOException {
        geocoder = new Geocoder(activity,Locale.getDefault());
        addresses = geocoder.getFromLocation(latitude, longitude, 1);
        address = addresses.get(0).getAddressLine(0);
    }

    /**
     * Function: Method to calculate distance between two Latitudes and longitudes
     * Parameters: double venueLat, double venueLng
     * @return: double
     * References: https://bit.ly/2OQoVd3
     */
    public double calculateDistanceInKilometer(double venueLat, double venueLng) {

        double latDistance = Math.toRadians(this.latitude - venueLat);
        double lngDistance = Math.toRadians(this.longitude - venueLng);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(this.latitude)) * Math.cos(Math.toRadians(venueLat))
                * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return (AVERAGE_RADIUS_OF_EARTH_KM * c);
    }

    public void setRadius(String radius){
        this.radius = radius;
    }

    public void setObject(LatLng obj){
        this.object = obj;
    }

    public void setActivity(Activity activity){
        this.activity = activity;
    }

    public String getAddress() {
        return address;
    }

    public Geocoder getGeocoder() {
        return geocoder;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getRadius() {
        return radius;
    }

    public Activity getActivity() {
        return activity;
    }

    public LatLng getObject() {
        return object;
    }
}
