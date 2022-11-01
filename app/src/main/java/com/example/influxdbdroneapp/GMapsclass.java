package com.example.influxdbdroneapp;



import java.lang.*;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class GMapsclass {
    static Marker userLocationMarker;
    public static GoogleMap mMap;
    public static double old_latitude = 0;
    public static double old_longtitude = 0;
    public static double latituden = 0;
    public static double longtituden = 0;



    private static float bearingcalc(double newlatitude, double newlongtitude, double oldlatitude, double oldlongtitude){
        double X =  Math.cos(newlatitude) *Math.cos(newlatitude-oldlatitude);
        double Y = Math.cos(oldlatitude*Math.sin(newlatitude)-Math.sin(oldlatitude*X));
        return (float) (Math.atan2(X,Y)*(180/3.14));
    }

    public static void setUserLocationMarker(double latitude, double longtitude) {

        // marker link <a href='https://pngtree.com/so/quadcopter'>quadcopter png from pngtree.com/</a>

        LatLng latLng = new LatLng(latitude,longtitude);
        if (userLocationMarker == null) {
            //Create a new marker
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.png_drone));
            markerOptions.rotation(bearingcalc(latitude, longtitude, GMapsclass.old_latitude, GMapsclass.old_longtitude));
            markerOptions.anchor((float) 0.5, (float) 0.5);
            userLocationMarker = mMap.addMarker(markerOptions);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
        } else  {

            //use the previously created marker

            userLocationMarker.setPosition(latLng);
            userLocationMarker.setRotation(bearingcalc(latitude, longtitude,GMapsclass.old_latitude, GMapsclass.old_longtitude));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
        }



    }

}
