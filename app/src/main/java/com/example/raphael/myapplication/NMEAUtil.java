package com.example.raphael.myapplication;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Raphael on 28/03/2016.
 */
public class NMEAUtil {

    public static ArrayList<String> toNMEA(Map<Marker, Waypoint> list) {
        ArrayList<String> nmeas = new ArrayList<String>();
        String nmea;

        Marker key;
        Waypoint value;
        LatLng pos;

        //Map triée par les id
        Map<Marker, Waypoint> myMap = WaypointComparator.sortByValue(list);

        for(Map.Entry<Marker, Waypoint> entry : myMap.entrySet()) {
            key = entry.getKey();
            value = entry.getValue();
            pos = key.getPosition();

            nmea = DD2NMEA(pos.latitude, pos.longitude, value.getSpeed());
            nmeas.add(nmea);
        }

        return nmeas;
    }

    public static String DD2NMEA(double lat, double lng, int speed)
    {
        String nmea = "";
        double lata = Math.abs(lat);

        double latd = Math.floor(lata);

        double latm = (lata - latd) * 60;
        String lath = lat > 0 ? "N" : "S";
        double lnga = Math.abs(lng);

        double lngd = Math.floor(lnga);

        double lngm = (lnga - lngd) * 60;
        String lngh = lng > 0 ? "E" : "W";

        NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
        DecimalFormat df = (DecimalFormat)nf;
        df.applyPattern("00");

        NumberFormat nf2 = NumberFormat.getNumberInstance(Locale.ENGLISH);
        DecimalFormat df2 = (DecimalFormat)nf2;
        df2.applyPattern("000");

        NumberFormat nf3 = NumberFormat.getNumberInstance(Locale.ENGLISH);
        DecimalFormat df3 = (DecimalFormat)nf3;
        df3.applyPattern("00.0000");

        nmea += "$GPRMC,,,";
        nmea += df.format(latd) + df3.format(latm) + "," + lath + ",";
        nmea += df2.format(lngd) + df3.format(lngm) + "," + lngh + ",";
        nmea += speed;
        nmea += ",,,,,,";

        return nmea;
    }
}
