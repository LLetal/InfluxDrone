package com.example.influxdbdroneapp;

import android.graphics.Color;
import android.location.Location;
import java.lang.*;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class GMapsclass {
    Marker userLocationMarker;
    private GoogleMap mMap;
    public static double[] old_coord = djidatacollect.GPS_location();
    public static double[] new_coord = djidatacollect.GPS_location();

    private float bearingcalc(double[] old_location, double[] newlocation){
        double X =  Math.cos(newlocation[0]) *Math.cos(newlocation[0]-old_location[0]);
        double Y = Math.cos(old_location[0]*Math.sin(newlocation[0])-Math.sin(old_location[0]*X));
        return (float) (Math.atan2(X,Y)*(180/3.14));
    }

    private void setUserLocationMarker(double[] location) {

        // marker link <a href='https://pngtree.com/so/quadcopter'>quadcopter png from pngtree.com/</a>

        LatLng latLng = new LatLng(location[0], location[1]);
        if (userLocationMarker == null) {
            //Create a new marker
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.Png_drone));
            markerOptions.rotation(bearingcalc(GMapsclass.old_coord, GMapsclass.new_coord));
            markerOptions.anchor((float) 0.5, (float) 0.5);
            userLocationMarker = mMap.addMarker(markerOptions);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
        } else  {

            //use the previously created marker

            userLocationMarker.setPosition(latLng);
            userLocationMarker.setRotation(bearingcalc(GMapsclass.old_coord,GMapsclass.new_coord));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
        }


    }

}
