package com.example.fyrflyuser.modelClasses;

import com.google.firebase.database.DataSnapshot;

public class LatLngDriver {
   private  String id ;
    private String latitude , longitude  ,latLng;

    public String getLatLng() {
        return latLng;
    }

    public void setLatLng(String latLng) {
        this.latLng = latLng;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getStringitude() {
        return longitude;
    }

    public void setStringitude(String longitude) {
        this.longitude = longitude;
    }



    public void parseData(DataSnapshot dataSnapshot) {

        id = dataSnapshot.getKey();

        if (dataSnapshot.child("latitude").getValue() != null) {
            latitude = (String) dataSnapshot.child("latitude").getValue();
        }
        if (dataSnapshot.child("longitude").getValue() != null) {
            latLng = (String) dataSnapshot.child("longitude").getValue();
        } if (dataSnapshot.child("latLng").getValue() != null) {
            latLng = (String) dataSnapshot.child("latLng").getValue();
        }

    }

}
