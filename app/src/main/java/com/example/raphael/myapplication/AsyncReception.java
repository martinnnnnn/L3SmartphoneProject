package com.example.raphael.myapplication;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

//package com.example.thibault.projetdevdur;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
/**
 * Created by Thibault on 15/03/2016.
 */

public class AsyncReception extends AsyncTask<Void , Void, Void> {
    ServerSocket serverSocket;
    Socket socket;

    public static float f_latitude=0;
    public static float f_longitude=0;
    public static float f_vitesse=0;
    public static float f_boussole=0;
    public static boolean receptionHasStarted = false;

    public static String s_vitesse;
    @Override
    protected Void doInBackground(Void... params) {

        Socket socket=new Socket();
        BufferedReader in;
        PrintWriter out;
        int i=3;
        try {
            System.out.println(" try ");
            socket = new Socket(MainActivity.ipServer, Integer.parseInt(MainActivity.portServer));

            String s_enTete;
            String s_latitude;
            String s_longitude;
            String s_boussole;

            String NordOuSud;
            String EstOuOuest;

            String []tabString;

            while(i<5) {
                in = new BufferedReader (new InputStreamReader(socket.getInputStream()));
                String line = in.readLine();


                while( line != null) {
                    try {

                        tabString= line.split(",");
                        s_enTete = new String(tabString[0]);

                        if (s_enTete.equals("$GPGLL")) { // Il s'agit de la ligne contenant les informations de localisation gps

                            s_latitude = new String(tabString[1]);
                            s_latitude = s_latitude.replace(',', '.');
                            f_latitude = Float.parseFloat(s_latitude);

                            s_longitude = new String(tabString[3]);
                            s_longitude = s_longitude.replace(',', '.');
                            f_longitude = Float.parseFloat(s_longitude);

                            NordOuSud = new String(tabString[2]);
                            EstOuOuest = new String(tabString[4]);

                            if(NordOuSud == "S")
                                f_latitude = -f_latitude;
                            if(EstOuOuest == "W")
                                f_longitude = -f_longitude;

                            f_latitude = (float) ((float) Math.floor(f_latitude / 100)+(f_latitude/100-Math.floor(f_latitude/100))*10/6);
                            f_longitude = (float) ((float) Math.floor(f_longitude / 100)+(f_longitude/100-Math.floor(f_longitude/100))*10/6);

                        }
                        if (s_enTete.equals("$GPRMC")) {
                            s_vitesse = new String(tabString[7]);
                            s_boussole = new String(tabString[8]);


                            s_vitesse = s_vitesse.replace(',', '.');
                            f_vitesse = Float.parseFloat(s_vitesse);


                            s_boussole = s_boussole.replace(',', '.');
                            f_boussole = Float.parseFloat(s_boussole);

                            System.out.println("Latitude = " + -f_latitude + " Longitude : " + f_longitude + " vitesse : " + f_vitesse + " angle de direction : " + f_boussole);
                            receptionHasStarted = true;
                        }
                    }
                    catch(Exception E){
                        System.out.println(E);
                    }
                    // read the next line
                    line = in.readLine();
                }
            }
            socket.close();
        } catch (UnknownHostException e) {

            System.out.println("catch1");
            e.printStackTrace();

        }catch (IOException e) {

            System.out.println(" catch2 ");
            e.printStackTrace();
        }
        System.out.println("end");
        return null;
    }
}