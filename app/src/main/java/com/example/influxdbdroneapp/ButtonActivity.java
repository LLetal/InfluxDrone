package com.example.influxdbdroneapp;

import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.WriteApiBlocking;


public class ButtonActivity extends AppCompatActivity {
    public volatile Boolean bolswitch = false;
    public static InfluxDBClient setup_client = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button);
    }

    Runnable data_collection = new Runnable() {
        @Override
        public void run() {
            WriteApiBlocking writeApi = setup_client.getWriteApiBlocking();
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
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
                influxdbclass.pushint(writeApi, "Dji", djidatacollect.drone_name, "Battery_metrics", "Battery_percentage", djidatacollect.battery_percent());
                influxdbclass.pushint(writeApi, "Dji", djidatacollect.drone_name, "Battery_metrics", " Battery_current", djidatacollect.battery_current());
                influxdbclass.pushint(writeApi, "Dji", djidatacollect.drone_name, "Battery_metrics", "Battery_voltage", djidatacollect.battery_voltage());
            }
        }

    };
    Runnable debug = new Runnable() {
        @Override
        public void run() {
            WriteApiBlocking writeApi = setup_client.getWriteApiBlocking();
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            while(bolswitch){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                influxdbclass.pushint(writeApi, "Dji", "Debug_drone", "Debug_cycle", "debug", 2);
                influxdbclass.pushint(writeApi, "Dji", "Debug_drone", "Debug_cycle", "debug", 2);
                influxdbclass.pushint(writeApi, "Dji", "Debug_drone", "Debug_cycle", "debug", 2);
                influxdbclass.pushint(writeApi, "Dji", "Debug_drone", "Debug_cycle", "debug", 2);
                influxdbclass.pushint(writeApi, "Dji", "Debug_drone", "Debug_cycle", "debug", 2);
                influxdbclass.pushint(writeApi, "Dji", "Debug_drone", "Debug_cycle", "debug", 2);
                influxdbclass.pushint(writeApi, "Dji", "Debug_drone", "Debug_cycle", "debug", 2);

            }
        }
    };
    @SuppressLint("SetTextI18n")
    public void OnClick_collect(View view) {
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