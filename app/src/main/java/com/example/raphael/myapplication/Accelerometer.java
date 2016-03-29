package com.example.raphael.myapplication;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.DecimalFormat;

/**
 * Created by alari_000 on 28/03/2016.
 */
public class Accelerometer implements SensorEventListener {

    private SensorManager sensorManager;
    double ax,ay,az;   // these are the acceleration in x,y and z axis
    SeekBar seekBar;
    SeekBar seekBar2;
    TextView text;
    TextView text2;
    Button arret;
    Button home;
    boolean canDirige;
    double vitesseMax;
    double vitesse;
    double rotation;

    public Accelerometer(Activity activity, Context context) {
        seekBar = (SeekBar)activity.findViewById(R.id.seekbar);
        seekBar2 = (SeekBar)activity.findViewById(R.id.seekbar2);
        text = (TextView)activity.findViewById(R.id.text);
        text2 = (TextView)activity.findViewById(R.id.text2);
        arret = (Button)activity.findViewById(R.id.button2);
        home = (Button)activity.findViewById(R.id.button);
        canDirige = true;
        vitesseMax = 30;

        arret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               canDirige =! canDirige;
                ax = 0;
                ay = 0;
                az = 0;
                text2.setText("0");
                text.setText("0");
                seekBar.setProgress(0);
                seekBar2.setProgress(5);
            }
        });

        sensorManager=(SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER && canDirige ==true) {
            ax = event.values[0];
            ay = event.values[1];
            az = event.values[2];
            vitesse = vitesseMax - (ay/10*vitesseMax);

            rotation += ax*90/10;

            if(vitesse < 0) vitesse = 0;
            else if(vitesse > vitesseMax) vitesse = vitesseMax;

            seekBar.setProgress(10 - ((int) ay));
            seekBar2.setProgress(10 - ((((int) ax) + 10) / 2));
            DecimalFormat df = new DecimalFormat("#.#");
            text2.setText(df.format(ax));
            text.setText(df.format(vitesse));

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}

