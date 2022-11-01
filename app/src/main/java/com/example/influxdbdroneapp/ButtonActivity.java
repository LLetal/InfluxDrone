package com.example.influxdbdroneapp;

import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.WriteApiBlocking;

import java.util.Random;


public class ButtonActivity extends AppCompatActivity implements OnMapReadyCallback{
    public volatile Boolean bolswitch = false;
    public static InfluxDBClient setup_client = null;
    Random source_r = new Random();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        GMapsclass.mMap = googleMap;
        GMapsclass.mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        LatLng sydney = new LatLng(-33.852, 151.211);
        googleMap.addMarker(new MarkerOptions()
                .position(sydney)
                .title("Marker in Sydney").icon(BitmapDescriptorFactory.fromResource(R.drawable.png_drone)));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }



    Runnable data_collection = () -> {
        WriteApiBlocking writeApi = setup_client.getWriteApiBlocking();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        djidatacollect.last_percent = djidatacollect.battery_percent(0);
        djidatacollect.last_voltage = djidatacollect.battery_voltage(0);
        djidatacollect.last_current = djidatacollect.battery_current(0);
        while (bolswitch){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            influxdbclass.pushfloat(writeApi, "Dji", djidatacollect.drone_name, "Basic_metrics", "VelocityX", djidatacollect.velocityX());
            influxdbclass.pushfloat(writeApi, "Dji", djidatacollect.drone_name, "Basic_metrics", "VelocityY", djidatacollect.velocityY());
            influxdbclass.pushfloat(writeApi, "Dji", djidatacollect.drone_name, "Basic_metrics", "VelocityZ", djidatacollect.velocityZ());
            influxdbclass.pushint(writeApi, "Dji", djidatacollect.drone_name, "Basic_metrics", "FlightTime", djidatacollect.flight_time());
            influxdbclass.pushint(writeApi, "Dji", djidatacollect.drone_name, "Battery_metrics", "Battery_percentage", djidatacollect.battery_percent(djidatacollect.last_percent));
            influxdbclass.pushint(writeApi, "Dji", djidatacollect.drone_name, "Battery_metrics", " Battery_current", djidatacollect.battery_current(djidatacollect.last_current));
            influxdbclass.pushint(writeApi, "Dji", djidatacollect.drone_name, "Battery_metrics", "Battery_voltage", djidatacollect.battery_voltage(djidatacollect.last_voltage));
            influxdbclass.pushdouble(writeApi, "Dji", djidatacollect.drone_name,"Geo_metrics", "Longtitude", djidatacollect.longtitude());
            influxdbclass.pushdouble(writeApi, "Dji", djidatacollect.drone_name,"Geo_metrics", "Latitude", djidatacollect.latitude());
            influxdbclass.pushdouble(writeApi, "Dji", djidatacollect.drone_name,"Geo_metrics", "Altitude", djidatacollect.GPS_location()[2]);
            GMapsclass.latituden = djidatacollect.latitude();
            GMapsclass.longtituden = djidatacollect.longtitude();
            UIupdate();
            GMapsclass.old_longtitude = djidatacollect.longtitude();
            GMapsclass.old_latitude = djidatacollect.latitude();
        }
    };
    protected void UIupdate() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                GMapsclass.setUserLocationMarker(GMapsclass.latituden, GMapsclass.longtituden);
            }
        });

    }
    protected double Random_latitude(double range_min, double range_max){
        return(range_min + (range_max-range_min)*source_r.nextDouble());
    }
    protected double Random_longtitude(double range_min, double range_max){
        return(range_min + (range_max-range_min)*source_r.nextDouble());
    }

    public int debug_fun(){
        return(0);
    }
    Runnable debug = () -> {
        WriteApiBlocking writeApi = setup_client.getWriteApiBlocking();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        while(bolswitch){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            influxdbclass.pushint(writeApi, "Dji", "Debug_drone", "Debug_cycle", "debug", debug_fun());
            influxdbclass.pushint(writeApi, "Dji", "Debug_drone", "Debug_cycle", "debug", debug_fun());
            influxdbclass.pushint(writeApi, "Dji", "Debug_drone", "Debug_cycle", "debug", debug_fun());
            GMapsclass.latituden = Random_latitude(49.4762831, 17.4361861);
            GMapsclass.longtituden = Random_longtitude(49.4712217, 17.4515069);
            UIupdate();
        }
    };



    @SuppressLint("SetTextI18n")
    public void OnClick_collect(View view) {
        //Thread myThread = new Thread(debug_fun);
        Thread myThread = new Thread(data_collection);
        Button maintext = findViewById(R.id.mainbutton);
        if (!bolswitch){
            maintext.setText("Data is being sent");
            bolswitch=true;
            myThread.start();
        }
        else {
            maintext.setText("Click for data");
            myThread.interrupt();
            bolswitch=false;
        }
    }



}