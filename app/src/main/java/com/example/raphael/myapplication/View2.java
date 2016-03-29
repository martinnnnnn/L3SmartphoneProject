package com.example.raphael.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.gms.maps.model.Marker;

/**
 * Created by Raphael on 28/03/2016.
 */
public class View2 extends LinearLayout{

    public View2() {
        super.();


        final Marker m = arg0;

        final EditText input = new EditText(MapsActivity.this);

        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setText(Integer.toString(allMarkersMap.get(arg0).getSpeed()));
        input.setSelection(input.getText().length());

        final Button deleteBtn = new Button(MapsActivity.this);
        deleteBtn.setText("Supprimer");
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m.hideInfoWindow();
                points.remove(allMarkersMap.get(m).getId());
                allMarkersMap.remove(m);
                m.remove();
                updatePolyline();
            }
        });

        input.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                String text = input.getText().toString();
                if (text.isEmpty()) {
                    text = "0";
                }
                allMarkersMap.get(m).setSpeed(Integer.parseInt(text));
                //Rouvrir pour mettre a jour le contenu
                m.hideInfoWindow();
                m.showInfoWindow();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        LinearLayout lnl = new LinearLayout(MapsActivity.this);
        lnl.setOrientation(LinearLayout.VERTICAL);
        lnl.setLayoutParams(lp);
        lnl.addView(input);
        lnl.addView(deleteBtn);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MapsActivity.this);
        alertDialog.setTitle("Vitesse");
        alertDialog.setMessage("Entrez vitesse en noeuds: ");
        alertDialog.setView(lnl); // uncomment this line
        alertDialog.show();
    }
}
