package com.example.influxdbdroneapp;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;

import java.time.Instant;

public class influxdbclass {

    static String token = "";
    static String url ="";
    static String org = "";
    static String bucket = "";

    public static void init(String url, String org, String bucket, String token){
        influxdbclass.url = url;
        influxdbclass.org = org;
        influxdbclass.bucket = bucket;
        influxdbclass.token = token;

    }
    public static InfluxDBClient Clientinitialization(){
        return(InfluxDBClientFactory.create(url, token.toCharArray(), org, bucket));
    }
    public static void pushint(WriteApiBlocking writeApi, String measurement, String tag, String tag2, String field, Integer value) {





        Point point = Point.measurement(measurement)
                .addTag(tag, tag2)
                .addField(field, value)
                .time(Instant.now(), WritePrecision.MS);

        writeApi.writePoint(point);
    }
    public static void pushfloat(WriteApiBlocking writeApi, String measurement, String tag, String tag2, String field, Float value) {


        Point point = Point.measurement(measurement)
                .addTag(tag, tag2)
                .addField(field, value)
                .time(Instant.now(), WritePrecision.MS);

        writeApi.writePoint(point);
    }

    public static void pushdouble(WriteApiBlocking writeApi, String measurement, String tag, String tag2, String field, Double value) {


        Point point = Point.measurement(measurement)
                .addTag(tag, tag2)
                .addField(field, value)
                .time(Instant.now(), WritePrecision.MS);

        writeApi.writePoint(point);
    }

}
