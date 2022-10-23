package com.example.influxdbdroneapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.WriteApiBlocking;


public class ButtonActivity extends AppCompatActivity implements OnMapReadyCallback{
    public volatile Boolean bolswitch = false;
    public static InfluxDBClient setup_client = null;

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
        double coordarray[];
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
            influxdbclass.pushdouble(writeApi, "Dji", djidatacollect.drone_name,"Geo_metrics", "Langtitude", djidatacollect.latitude());
            influxdbclass.pushdouble(writeApi, "Dji", djidatacollect.drone_name,"Geo_metrics", "Altitude", djidatacollect.GPS_location()[2]);
            GMapsclass.old_longtitude = djidatacollect.longtitude();
            GMapsclass.old_latitude = djidatacollect.latitude();
        }
    };

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
            GMapsclass.latituden = 0;
            GMapsclass.longtituden = 0;

        }
    };
    public void map_loop() {
        while (bolswitch) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            GMapsclass.setUserLocationMarker(GMapsclass.latituden, GMapsclass.longtituden);

        }

    }

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