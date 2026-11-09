package com.cablelatino.tv;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout pantalla = new LinearLayout(this);
        pantalla.setOrientation(LinearLayout.VERTICAL);
        pantalla.setGravity(Gravity.CENTER);
        pantalla.setPadding(24, 24, 24, 24);
        pantalla.setBackgroundColor(Color.BLACK);

        TextView titulo = new TextView(this);
        titulo.setText("CABLE LATINO TV");
        titulo.setTextSize(30);
        titulo.setTextColor(Color.RED);
        titulo.setGravity(Gravity.CENTER);

        TextView mensaje = new TextView(this);
        mensaje.setText("Bienvenido a Cable Latino TV");
        mensaje.setTextSize(18);
        mensaje.setTextColor(Color.WHITE);
        mensaje.setGravity(Gravity.CENTER);

        pantalla.addView(titulo);
        pantalla.addView(mensaje);

        setContentView(pantalla);
    }
}
