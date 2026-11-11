package com.cablelatino.tv;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.*;
import android.media.MediaPlayer;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;
import androidx.media3.common.PlaybackException;
import android.content.Context;
import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class MainActivity extends Activity {
    LinearLayout pantalla, canales;
    EditText entrada;
    PlayerView video;
ExoPlayer reproductor;
    ArrayList<String> nombres = new ArrayList<>();
    ArrayList<String> enlaces = new ArrayList<>();

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        pantalla = new LinearLayout(this);
        pantalla.setOrientation(1);
        pantalla.setPadding(16,16,16,16);
        pantalla.setBackgroundColor(Color.BLACK);

        TextView titulo = new TextView(this);
        titulo.setText("CABLE LATINO TV");
        titulo.setTextColor(Color.RED);
        titulo.setTextSize(26);
        titulo.setGravity(Gravity.CENTER);
        pantalla.addView(titulo);

        entrada = new EditText(this);
        entrada.setHint("Pega tu URL M3U");
        entrada.setSingleLine(true);
        entrada.setTextColor(Color.WHITE);
        entrada.setHintTextColor(Color.LTGRAY);
        pantalla.addView(entrada);

        Button cargar = new Button(this);
        cargar.setText("CARGAR CANALES");
        pantalla.addView(cargar);

    
reproductor = new ExoPlayer.Builder(this).build();

video = new PlayerView(this);
video.setPlayer(reproductor);
video.setUseController(true);

pantalla.addView(video,
    new LinearLayout.LayoutParams(
        -1, 500
    )
);

        
        
        

        ScrollView scroll = new ScrollView(this);
        canales = new LinearLayout(this);
        canales.setOrientation(1);
        scroll.addView(canales);
        pantalla.addView(scroll);
        setContentView(pantalla);

        cargar.setOnClickListener(v -> cargarLista());
    }

    void cargarLista() {
        String url = entrada.getText().toString().trim();
        if (!url.startsWith("https://") && !url.startsWith("http://")) {
            Toast.makeText(this, "Introduce una URL valida", Toast.LENGTH_LONG).show();
            return;
        }
        canales.removeAllViews();
        new Thread(() -> {
            try {
                HttpURLConnection c = (HttpURLConnection)new URL(url).openConnection();
                c.setConnectTimeout(10000);
                c.setReadTimeout(15000);
                BufferedReader r = new BufferedReader(new InputStreamReader(c.getInputStream()));
                ArrayList<String> ns = new ArrayList<>();
                ArrayList<String> es = new ArrayList<>();
                String linea, nombre = "";
                while ((linea = r.readLine()) != null) {
                    linea = linea.trim();
                    if (linea.startsWith("#EXTINF")) {
                        int pos = linea.lastIndexOf(",");
                        nombre = pos >= 0 ? linea.substring(pos+1).trim() : "Canal";
                    } else if (linea.startsWith("http://") || linea.startsWith("https://")) {
                        ns.add(nombre.isEmpty() ? "Canal" : nombre);
                        es.add(linea);
                        nombre = "";
                    }
                }
                r.close();
                runOnUiThread(() -> mostrar(ns, es));
            } catch (Exception ex) {
                runOnUiThread(() -> Toast.makeText(this, "Error al cargar M3U: " + ex.getMessage(), Toast.LENGTH_LONG).show());
            }
        }).start();
    }

    
void mostrar(ArrayList<String> ns, ArrayList<String> es) {
    canales.removeAllViews();

    if (ns.isEmpty()) {
        Toast.makeText(this, "No se encontraron canales",
            Toast.LENGTH_LONG).show();
        return;
    }

    mostrarLote(ns, es, 0);
}

void mostrarLote(ArrayList<String> ns,
                 ArrayList<String> es, int inicio) {
    int fin = Math.min(inicio + 25, ns.size());

    for (int i = inicio; i < fin; i++) {
        final String enlace = es.get(i);

        Button boton = new Button(this);
        boton.setText(ns.get(i));
        canales.addView(boton);

        boton.setOnClickListener(v -> {
            
reproductor.stop();
reproductor.clearMediaItems();
reproductor.setMediaItem(MediaItem.fromUri(enlace));
reproductor.prepare();
reproductor.play();

            
        });
    }

    if (fin < ns.size()) {
        Button mas = new Button(this);
        mas.setText("CARGAR MÁS CANALES");
        canales.addView(mas);

        mas.setOnClickListener(v -> {
            canales.removeView(mas);
            mostrarLote(ns, es, fin);
        });
    }
}

        
        
            
            
            
        
            
            
    
        
        
    

@Override
protected void onDestroy() {
    if (reproductor != null) {
        reproductor.release();
    }
    super.onDestroy();
}

}
