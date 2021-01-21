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

    private GoogleMap mapa;
    private LatLng oMunicipio = null;
    private String nombre="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        codMuni = extras.getInt("codMunicipio");
        datosMuni = (ArrayList<Municipio>) getIntent().getSerializableExtra("arrayMunicipios");

        for (int i=0;i<datosMuni.size();i++) {
            if (datosMuni.get(i).getCod_muni() == codMuni) {
                oMunicipio = new LatLng(datosMuni.get(i).getLongitud(), datosMuni.get(i).getLatitud());
                nombre = datosMuni.get(i).getNombre();
            }
        }

        setContentView(R.layout.activity_google_maps);
        // Obtenemos el mapa de forma asíncrona (notificará cuando esté listo)
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.mapa);
        mapFragment.getMapAsync(this);
    }

    public void onMapReady(GoogleMap googleMap) {
        mapa = googleMap;
        mapa.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mapa.getUiSettings().setZoomControlsEnabled(true); // Oculta loscontroles de zoom.
        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(oMunicipio, 15));
        mapa.addMarker(new MarkerOptions()
                .position(oMunicipio)
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