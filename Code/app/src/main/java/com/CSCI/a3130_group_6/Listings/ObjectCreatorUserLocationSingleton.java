package com.CSCI.a3130_group_6.Listings;

import com.CSCI.a3130_group_6.HelperClases.UserLocation;

public interface ObjectCreatorUserLocationSingleton {
    UserLocation userLocation = null;
    public UserLocation getUserLocation();
}
