package com.example.raphael.myapplication;

import android.os.AsyncTask;

import java.io.DataOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by Thibault on 29/03/2016.
 */
public class AsyncEnvoi extends AsyncTask<Void , Void, Void> {

    Socket socket;
    DataOutputStream dOut;
    static Stack<String> trames;
    protected Void doInBackground(Void... params) {
        try {
            System.out.println("tedst zqdzqd");
            //socket = new Socket("10.0.2.2", 7575);
            //dOut = new DataOutputStream(socket.getOutputStream());
            System.out.println("tedst 1");
            ServerSocket serverSocket = new ServerSocket(7575);
            Socket clientSocket = serverSocket.accept();
            PrintWriter out =
                    new PrintWriter(clientSocket.getOutputStream(), true);
            System.out.println("tedst ebnvoi 2");
            int ii = 2;
            while(ii <  3) {
                out.println("$GPRMC,053740.000,A,,,,,2.69,79.65,100106,,,A*53");
                Thread.sleep(200);
            }

            int i=2;
            int indexMessage =0;
            while(i<3) {

                if(true  || trames!=null && trames.size()>0) {

                    /*
                    $GPRMC       : type de trame
                    053740.000   : heure UTC exprimée en hhmmss.sss : 5h 37m 40s
                    A            : état A=données valides, V=données invalides
                    2503.6319    : Latitude exprimée en ddmm.mmmm : 25°03.6319' = 25°03'37,914"
                    N            : indicateur de latitude N=nord, S=sud
                    12136.0099   : Longitude exprimée en dddmm.mmmm : 121°36.0099' = 121°36'00,594"
                    E            : indicateur de longitude E=est, W=ouest
                    2.69         : vitesse sur le fond en nœuds (2,69 kn = 3,10 mph = 4,98 km/h)
                    79.65        : route sur le fond en degrés
                    100106       : date exprimée en qqmmaa : 10 janvier 2006
                    ,            : déclinaison magnétique en degrés (souvent vide pour un GPS)
                    ,            : sens de la déclinaison E=est, W=ouest (souvent vide pour un GPS)
                    A            : mode de positionnement A=autonome, D=DGPS, E=DR
                    *53          : somme de contrôle de parité au format hexadécimal2
                     */


                    // Send the third message
                    dOut.writeByte(0);
                    dOut.writeUTF("$GPRMC,053740.000,A,,,,,2.69,79.65,100106,,,A*53");
                    System.out.println("tedst ebnvoi");
                    //dOut.writeUTF("This is the third type of message (Part 2).");
                    dOut.flush(); // Send off the data
                }


            }
            dOut.writeByte(-1);
            dOut.flush();
            dOut.close();
        }
        catch(Exception E){
            System.out.println("error"+E);
        }
        return null;
    }
}
