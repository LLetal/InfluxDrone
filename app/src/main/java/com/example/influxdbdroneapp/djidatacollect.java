package com.example.influxdbdroneapp;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.widget.TextView;

import androidx.annotation.NonNull;

import dji.common.error.DJIError;
import dji.keysdk.BatteryKey;
import dji.keysdk.KeyManager;
import dji.keysdk.callback.GetCallback;
import dji.sdk.flightcontroller.FlightController;
import dji.sdk.products.Aircraft;
import dji.sdk.sdkmanager.DJISDKManager;

public class djidatacollect {
    static String drone_name = DJISDKManager.getInstance().getProduct().getModel().toString();
    static FlightController flightController = ((Aircraft) DJISDKManager.getInstance().getProduct()).getFlightController();
    final static BatteryKey Batt_perc = BatteryKey.create(BatteryKey.CHARGE_REMAINING_IN_PERCENT);
    final static BatteryKey Voltage = BatteryKey.create(BatteryKey.VOLTAGE);
    final static BatteryKey Current = BatteryKey.create(BatteryKey.CURRENT);
    public static Float velocityX(){
        return(flightController.getState().getVelocityX());
    }
    public static Float velocityY(){
        return(flightController.getState().getVelocityY());
    }
    public static Float velocityZ(){
        return(flightController.getState().getVelocityZ());
    }
    public static Integer flight_time(){
        return(flightController.getState().getFlightTimeInSeconds());
    }
    public static Integer battery_current(){
        final Integer[] output = {0};
        KeyManager.getInstance().getValue(Current, new GetCallback() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(final @NonNull Object o) {
                if (o instanceof Integer) {
                    Integer current = (Integer) o;
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    output[0] =  current;
                }

            }

            @Override
            public void onFailure(@NonNull DJIError djiError) {
            }
        });
        return(output[0]);
    }
    public static Integer battery_voltage(){
        final Integer[] output = {0};
        KeyManager.getInstance().getValue(Voltage, new GetCallback() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(final @NonNull Object o) {
                if (o instanceof Integer) {
                    Integer voltage = (Integer) o;
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    output[0] = voltage;
                }
            }

            @Override
            public void onFailure(@NonNull DJIError djiError) {
            }
        });
        return(output[0]);
    }
    public static Integer battery_percent(){
        final Integer[] output = {0};
        KeyManager.getInstance().getValue(Batt_perc, new GetCallback() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(final @NonNull Object o) {
                if (o instanceof Integer) {
                    Integer perc = (Integer) o;
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    output[0] = perc;

                }
            }

            @Override
            public void onFailure(@NonNull DJIError djiError) {
            }
        });
        return(output[0]);
    }
}
