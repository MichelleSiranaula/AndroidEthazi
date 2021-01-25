package com.elorrieta.euskomet;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;


public class GoogleMaps extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mapa;
    private LatLng oSitio = null;
    private String nombre="";
    private String quien="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        quien = extras.getString("mapa");

        if (quien.equals("Municipios")) {
            for (int i=0;i<Lista.arrayMuni.size();i++) {
                if (Lista.arrayMuni.get(i).getCod_muni() == Lista.cod_muni) {
                    oSitio = new LatLng(Lista.arrayMuni.get(i).getLatitud(), Lista.arrayMuni.get(i).getLongitud());
                    nombre = Lista.arrayMuni.get(i).getNombre();
                }
            }
        } else if (quien.equals("Espacios")) {
            for (int i=0;i<ListaEspacios.arrayEspacios.size();i++) {
                if (ListaEspacios.arrayEspacios.get(i).getCod_enatural() == ListaEspacios.cod_espacios) {
                    oSitio = new LatLng(ListaEspacios.arrayEspacios.get(i).getLatitud(), ListaEspacios.arrayEspacios.get(i).getLongitud());
                    nombre = ListaEspacios.arrayEspacios.get(i).getNombre();
                }
            }
        }

        setContentView(R.layout.activity_google_maps);
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.mapa);
        mapFragment.getMapAsync(this);
    }

    public void onMapReady(GoogleMap googleMap) {
        mapa = googleMap;
        mapa.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mapa.getUiSettings().setZoomControlsEnabled(true);
        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(oSitio, 15));
        mapa.addMarker(new MarkerOptions()
                .position(oSitio)
                .title(nombre)
                .icon(BitmapDescriptorFactory
                        .fromResource(android.R.drawable.ic_menu_compass))
                .anchor(0.5f, 0.5f));
    }

    public void addMarker(View view) {
        mapa.addMarker(new MarkerOptions().position(
                mapa.getCameraPosition().target));
    }

    //PARA IR A LA PANTALLA DE INFO
    public void volver(View view){
        finish();
        Intent volver = null;
        if (quien.equals("Municipios")) {
            volver = new Intent (this, Info.class);
        } else if (quien.equals("Espacios")) {
            volver = new Intent (this, InfoEspacios.class);
        }
        startActivity(volver);
    }

}