package com.elorrieta.euskomet;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
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

    Integer codMuni = 0;
    ArrayList<Municipio> datosMuni = new ArrayList<Municipio>();

    Integer codEspacios = 0;
    ArrayList<EspaciosNaturales> datosEspacios = new ArrayList<EspaciosNaturales>();

    private GoogleMap mapa;
    private LatLng oSitio = null;
    private String nombre="";
    private String quien="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        codMuni = extras.getInt("codMunicipio");
        datosMuni = (ArrayList<Municipio>) getIntent().getSerializableExtra("arrayMunicipios");

        codEspacios = extras.getInt("codEspacios");
        datosEspacios = (ArrayList<EspaciosNaturales>) getIntent().getSerializableExtra("arrayEspacios");

        quien = extras.getString("mapa");

        if (quien.equals("Municipios")) {
            for (int i=0;i<datosMuni.size();i++) {
                if (datosMuni.get(i).getCod_muni() == codMuni) {
                    oSitio = new LatLng(datosMuni.get(i).getLongitud(), datosMuni.get(i).getLatitud());
                    nombre = datosMuni.get(i).getNombre();
                }
            }
        } else if (quien.equals("Espacios")) {
            for (int i=0;i<datosEspacios.size();i++) {
                if (datosEspacios.get(i).getCod_enatural() == codEspacios) {
                    oSitio = new LatLng(datosEspacios.get(i).getLongitud(), datosEspacios.get(i).getLatitud());
                    nombre = datosEspacios.get(i).getNombre();
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


}