package com.example.raphael.myapplication;

import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.internal.zzf;

/**
 * Created by Raphael on 22/03/2016.
 */
public class Waypoint{
    private static int lastId = -1;

    private int id;
    private int speed;

    Waypoint(int s) {
        this.lastId++;
        id = this.lastId;
        speed = s;
    }

    //Pour le chargement des données
    Waypoint(int i, int s) {
        id = i;
        speed = s;
        this.lastId = i;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setId(int i) {
        id = i;
    }

    public int getId() {
        return id;
    }

    public static void resetId() {
        lastId = -1;
    }

    public static void stepBackId() {
        lastId--;
    }
}
