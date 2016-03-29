package com.example.raphael.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    public static String ipServer = "";
    public static String portServer = "";
    private EditText port;
    private EditText ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        ip = new EditText(this);
        ip.setText("10.0.2.2");
        port = new EditText(this);
        port.setText(String.valueOf(1234));
        Button connexion = new Button(this);
        connexion.setText("Connexion");

        layout.addView(ip);
        layout.addView(port);
        layout.addView(connexion);

        connexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ipServer = ip.getText().toString();
                portServer = port.getText().toString();

                Intent intent = new Intent(v.getContext(),MapsActivity.class);
                startActivity(intent);
            }
        });

        this.setContentView(layout);
    }
}
