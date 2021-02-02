package com.elorrieta.euskomet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.ArrayList;

public class TopEspacios extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_espacios);
    }

    //TODAS LAS PROVINCIAS
    private ArrayList<Object> conectarProvincia() throws InterruptedException {
        ClientThreadSelect clientThreadSelect = new ClientThreadSelect("SELECT * FROM provincia", "Provincia");
        Thread thread = new Thread(clientThreadSelect);
        thread.start();
        thread.join();
        return clientThreadSelect.getDatos();
    }

    //TODOS LOS MUNICIPIOS
    private ArrayList<Object> conectarEspacios() throws InterruptedException {
        ClientThreadSelect clientThreadSelect = new ClientThreadSelect("SELECT * FROM espacios_naturales", "Espacios");
        Thread thread = new Thread(clientThreadSelect);
        thread.start();
        thread.join();
        return clientThreadSelect.getDatos();
    }

    //PARA SACAR LOS MUNICIPIOS MAS FAVORITOS DE BIZKAIA
    private ArrayList<Object> favBizkaia() throws InterruptedException {
        ClientThreadSelect clientThreadSelect = new ClientThreadSelect("SELECT COUNT(fav_espacios.cod_enatural), fav_espacios.cod_enatural FROM fav_espacios, muni_espacios, municipio WHERE muni_espacios.cod_enatural = fav_espacios.cod_enatural AND muni_espacios.cod_muni = municipio.cod_muni GROUP BY fav_espacios.cod_enatural ORDER BY COUNT(fav_espacios.cod_enatural) DESC", "Top");
        Thread thread = new Thread(clientThreadSelect);
        thread.start();
        thread.join();
        return clientThreadSelect.getDatos();
    }
}