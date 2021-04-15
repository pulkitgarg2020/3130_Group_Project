package com.CSCI.a3130_group_6.Listings;

public class ObjectCreatorListingImplementation implements ObjectCreatorListingSingleton {
    Listing list= null;

    @Override
    public Listing getListingObject() {
        return new Listing();
    }
}
