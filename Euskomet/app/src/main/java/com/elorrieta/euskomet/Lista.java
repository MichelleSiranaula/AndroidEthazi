package com.elorrieta.euskomet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class Lista extends AppCompatActivity implements AdapterView.OnItemSelectedListener,AdapterView.OnItemClickListener {
    public static int cod_muni=0;

    private ConnectivityManager connectivityManager = null;
    private Spinner spinnerProv, spinnerFiltros;
    private RecyclerView oRecyclerView;
    private ListaAdapter oListaAdapter = null;

    ArrayList<String> arrayFiltros = new ArrayList<String>();
    ArrayList<Provincia> datosProv = new ArrayList<Provincia>();

    ArrayList<Municipio> datosMuni = new ArrayList<Municipio>();
    ArrayList<Municipio> datosMuniProvB = new ArrayList<Municipio>();
    ArrayList<Municipio> datosMuniProvG = new ArrayList<Municipio>();
    ArrayList<Municipio> datosMuniProvA = new ArrayList<Municipio>();

    ArrayList<Municipio> muniFav = new ArrayList<Municipio>();

    //array que se pasa a la clase INFO
    public static ArrayList<Municipio> arrayMuni = new ArrayList<Municipio>();

    boolean info = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        spinnerProv = findViewById(R.id.spinnerProv);
        spinnerProv.setOnItemSelectedListener(this);

        spinnerFiltros = findViewById(R.id.spinnerFiltros);
        spinnerFiltros.setOnItemSelectedListener(this);

        //PARA LLENAR EL SPINNER DE PROVINCIAS
        conectarOnClick(null);

        //PARA LLENAR EL SPINNER DE FILTROS
        arrayFiltros.add("Provincias");
        arrayFiltros.add("Favoritos");

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String> (this, android.R.layout.simple_spinner_item, arrayFiltros);
        spinnerFiltros.setAdapter(adapter2);

        //METER DATOS EN EL ARRAY muniFav
        try {
            ArrayList<Object> arrObject = new ArrayList<Object>();
            arrObject = conectarMuniFav();
            for (int i=0;i<arrObject.size();i++) {
                muniFav.add((Municipio) arrObject.get(i));
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //METER DATOS EN EL ARRAY datosMuni
        try {
            ArrayList<Object> arrObject = new ArrayList<Object>();
            arrObject = conectarMuni();
            for (int i=0;i<arrObject.size();i++) {
                datosMuni.add((Municipio) arrObject.get(i));
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i=0;i<datosMuni.size();i++) {
            if (datosMuni.get(i).getCod_prov()==1) {
                datosMuniProvB.add(datosMuni.get(i));
            }
            if (datosMuni.get(i).getCod_prov()==2) {
                datosMuniProvG.add(datosMuni.get(i));
            }
            if (datosMuni.get(i).getCod_prov()==3) {
                datosMuniProvA.add(datosMuni.get(i));
            }
        }

        //RECYCLERVIEW
        oRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        oRecyclerView.setLayoutManager(llm);
    }

    //ITEM SELECTED DEL SPINNER
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        String selecF= spinnerFiltros.getSelectedItem().toString();
        //CUANDO SELECCIONA PROVINCIAS EN EL SPINNER FILTROS
        if (selecF.equals("Provincias")) {
            spinnerProv.setEnabled(true);
            String selecP = spinnerProv.getSelectedItem().toString();
            if (selecP.equals("Bizkaia")) {
                oListaAdapter = new ListaAdapter(datosMuniProvB, new OnItemClickListener() {
                    @Override
                    public void onItemClick(Municipio item) {
                        cod_muni=item.getCod_muni();
                        arrayMuni = datosMuniProvB;
                        siguiente();
                    }
                });
            } else if (selecP.equals("Gipuzkoa")) {
                oListaAdapter = new ListaAdapter(datosMuniProvG,new OnItemClickListener() {
                    @Override
                    public void onItemClick(Municipio item) {
                        cod_muni=item.getCod_muni();
                        arrayMuni = datosMuniProvG;
                        siguiente();
                    }
                });
            } else if (selecP.equals("Araba")) {
                oListaAdapter = new ListaAdapter(datosMuniProvA,new OnItemClickListener() {
                    @Override
                    public void onItemClick(Municipio item) {
                        cod_muni=item.getCod_muni();
                        arrayMuni = datosMuniProvA;
                        siguiente();
                    }
                });
            }
            oRecyclerView.setAdapter(oListaAdapter);
        } else if (selecF.equals("Favoritos")) {
            spinnerProv.setEnabled(false);
            oListaAdapter = new ListaAdapter(muniFav,new OnItemClickListener() {
                @Override
                public void onItemClick(Municipio item) {
                    cod_muni=item.getCod_muni();
                    arrayMuni = muniFav;
                    siguiente();
                }
            });
            oRecyclerView.setAdapter(oListaAdapter);
        }


    }

    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
    }

    //PARA IR A LA PANTALLA DE LISTA
    public void siguiente(){
        finish();
        Intent siguiente = new Intent (this, Info.class);
        startActivity(siguiente);
    }

    //VOLVER AL MENU PRINCIPAL
    public void volver(View view){
        finish();
        Intent volver = new Intent (this, menuprincipal.class);
        startActivity(volver);
    }

    //BARRA DE ACCIONES
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.casa) {
            finish();
            Intent volver = new Intent (this, menuprincipal.class);
            startActivity(volver);
            return true;
        }
        if (id == R.id.share) {
            Toast.makeText(this, "En creación", Toast.LENGTH_LONG).show();
            return true;
        }
        if (id == R.id.mapa) {
            Toast.makeText(this, "En creación", Toast.LENGTH_LONG).show();
            return true;
        }
        if (id == R.id.camara) {
            Toast.makeText(this, "En creación", Toast.LENGTH_LONG).show();
            return true;
        }
         return super.onOptionsItemSelected(item);
    }

    //CONEXION CON LA BASE DE DATOS
    public void conectarOnClick(View v) {
        ArrayList<Object> arrObject = new ArrayList<Object>();
        ArrayList<String> listaNombProv = new ArrayList<String>();

        if (isConnected()) {
            try {
                arrObject = conectarProvincia();
                for (int i=0;i<arrObject.size();i++) {
                    datosProv.add((Provincia) arrObject.get(i));
                    listaNombProv.add(datosProv.get(i).getNombreProv());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
                ArrayAdapter<String> adapter = new ArrayAdapter<String> (this, android.R.layout.simple_spinner_item, listaNombProv);
                spinnerProv.setAdapter(adapter);
        } else {
            Toast.makeText(getApplicationContext(), "ERROR_NO_INTERNET", Toast.LENGTH_SHORT).show();
        }
    }

    private ArrayList<Object> conectarProvincia() throws InterruptedException {
        ClientThreadSelect clientThreadSelect = new ClientThreadSelect("SELECT * FROM provincia", "Provincia");
        Thread thread = new Thread(clientThreadSelect);
        thread.start();
        thread.join();
        return clientThreadSelect.getDatos();
    }

    private ArrayList<Object> conectarMuni() throws InterruptedException {

        ClientThreadSelect clientThreadSelect = new ClientThreadSelect("SELECT * FROM municipio", "Municipio");
        Thread thread = new Thread(clientThreadSelect);
        thread.start();
        thread.join();
        return clientThreadSelect.getDatos();
    }

    private ArrayList<Object> conectarMuniFav() throws InterruptedException {
        ClientThreadSelect clientThreadSelect = new ClientThreadSelect("SELECT m.* FROM municipio m, fav_municipio fm WHERE fm.cod_muni=" + cod_muni + " AND fm.cod_usuario ="+ MainActivity.codUsuario +"","Municipio");
        Thread thread = new Thread(clientThreadSelect);
        thread.start();
        thread.join();
        return clientThreadSelect.getDatos();
    }

    public boolean isConnected() {
        boolean ret = false;
        try {
            connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if ((networkInfo != null) && (networkInfo.isAvailable()) && (networkInfo.isConnected()))
                ret = true;
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error_comunicación", Toast.LENGTH_SHORT).show();
        }
        return ret;
    }
}

