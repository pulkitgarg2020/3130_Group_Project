package com.CSCI.a3130_group_6.Listings;

import com.CSCI.a3130_group_6.HelperClases.UserLocation;

public class ObjectCreatorUserLocationImplementation implements ObjectCreatorUserLocationSingleton {
    UserLocation userLocation = null;

    @Override
    public UserLocation getUserLocation() {
        return new UserLocation();
    }
}
