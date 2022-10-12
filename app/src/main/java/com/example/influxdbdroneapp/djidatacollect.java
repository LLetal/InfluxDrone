package com.example.influxdbdroneapp;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.dji.mapkit.lbs.provider.GPSLocationProvider;

import java.util.Locale;

import dji.common.error.DJIError;
import dji.common.flightcontroller.FlightControllerState;
import dji.common.remotecontroller.GPSData;
import dji.keysdk.BatteryKey;
import dji.keysdk.DJIKey;
import dji.keysdk.KeyManager;
import dji.keysdk.RemoteControllerKey;
import dji.keysdk.callback.GetCallback;
import dji.sdk.flightcontroller.FlightController;
import dji.sdk.products.Aircraft;
import dji.sdk.sdkmanager.DJISDKManager;

public class djidatacollect {
    static String drone_name = DJISDKManager.getInstance().getProduct().getModel().toString();
    static FlightController flightController = ((Aircraft) DJISDKManager.getInstance().getProduct()).getFlightController();
    final static DJIKey GPSkey = RemoteControllerKey.create(RemoteControllerKey.GPS_DATA);
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
    public static Integer battery_current(Integer init_current){
        final Integer[] output = {init_current};
        KeyManager.getInstance().getValue(Current, new GetCallback() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(final @NonNull Object o) {
                if (o instanceof Integer) {
                    Integer current = (Integer) o;
                    if (current ==0){
                        output[0]=init_current;
                    }
                    else {
                        output[0] = current;
                        djidatacollect.last_current=current;
                    }
                }

            }

            @Override
            public void onFailure(@NonNull DJIError djiError) {
            }
        });
        return(output[0]);
    }
    public static Integer battery_voltage(Integer init_volt){
        final Integer[] output = {init_volt};
        KeyManager.getInstance().getValue(Voltage, new GetCallback() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(final @NonNull Object o) {
                if (o instanceof Integer) {
                    Integer voltage = (Integer) o;
                    if (voltage ==0){
                        output[0]=init_volt;
                    }
                    else {
                        output[0] = voltage;
                        djidatacollect.last_voltage=voltage;
                    }
                }
            }

            @Override
            public void onFailure(@NonNull DJIError djiError) {
            }
        });
        return(output[0]);
    }
    public static Integer battery_percent(Integer init_perc){
        final Integer[] output = {init_perc};
        KeyManager.getInstance().getValue(Batt_perc, new GetCallback() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(final @NonNull Object o) {
                if (o instanceof Integer) {
                    Integer perc = (Integer) o;
                    if (perc ==0) {
                        output[0]=init_perc;
                    }
                    else {
                        output[0] = perc;
                        djidatacollect.last_percent=perc;

                    }
                }
            }

            @Override
            public void onFailure(@NonNull DJIError djiError) {
            }
        });
        return(output[0]);
    }
    public static double[] GPS_location(){

        double output_latitude = flightController.getState().getAircraftLocation().getLatitude();
        double output_longtitude = flightController.getState().getAircraftLocation().getLongitude();
        double output_altitude = flightController.getState().getAircraftLocation().getAltitude();
        double[] output_list;
        output_list = new double[]{output_latitude,output_longtitude, output_altitude};
        return(output_list);

    }

    static Integer last_voltage = 0;
    static Integer last_current = 0;
    static Integer last_percent = 0;

}
