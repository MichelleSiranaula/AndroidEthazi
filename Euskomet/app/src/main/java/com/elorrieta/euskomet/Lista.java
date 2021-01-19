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

    private ConnectivityManager connectivityManager = null;
    private Spinner spinner;
    private RecyclerView oRecyclerView;
    private ListaAdapter oListaAdapter = null;

    ArrayList<Municipio> datosMuni = new ArrayList<Municipio>();
    ArrayList<Provincia> datosProv = new ArrayList<Provincia>();
    ArrayList<Municipio> datosMuniProvB = new ArrayList<Municipio>();
    ArrayList<Municipio> datosMuniProvG = new ArrayList<Municipio>();
    ArrayList<Municipio> datosMuniProvA = new ArrayList<Municipio>();

    boolean info = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);

        conectarOnClick(null);

        try {
            ArrayList<Object> arrObject = new ArrayList<Object>();
            arrObject = conectarMuni();
            for (int i=0;i<arrObject.size();i++) {
                datosMuni.add((Municipio) arrObject.get(i));
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        oRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        oRecyclerView.setLayoutManager(llm);

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

        /*ListaAdapter oContactoAdapter = new ListaAdapter(arrMuni, new OnItemClickListener() {
            @Override public void onItemClick(Municipio item) {
                //Toast.makeText(this, "Nombre : " + item.getNombre(), Toast.LENGTH_LONG).show();
                Toast.makeText(this, "nombre", Toast.LENGTH_LONG).show();
            }
        });*/
    }

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        String selec=spinner.getSelectedItem().toString();
        if (selec.equals("Bizkaia")) {
            oListaAdapter = new ListaAdapter(datosMuniProvB, new OnItemClickListener() {
                @Override
                public void onItemClick(Municipio item) {
                    siguiente();
                }
            });

        } else if (selec.equals("Gipuzkoa")) {
            oListaAdapter = new ListaAdapter(datosMuniProvG,new OnItemClickListener() {
                @Override
                public void onItemClick(Municipio item) {
                    siguiente();
                }
            });
        } else if (selec.equals("Araba")) {
            oListaAdapter = new ListaAdapter(datosMuniProvA,new OnItemClickListener() {
                @Override
                public void onItemClick(Municipio item) {
                    siguiente();
                }
            });
        }

        oRecyclerView.setAdapter(oListaAdapter);
    }

    //PARA IR A LA PANTALLA DE LISTA
    public void siguiente(){
        finish();
        Intent volver = new Intent (this, Info.class);
        startActivity(volver);
    }

    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
    }

    public void volver(View view){
        finish();
        Intent volver = new Intent (this, menuprincipal.class);
        startActivity(volver);
    }

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
                spinner.setAdapter(adapter);
        } else {
            Toast.makeText(getApplicationContext(), "ERROR_NO_INTERNET", Toast.LENGTH_SHORT).show();
        }
    }

    private ArrayList<Object> conectarProvincia() throws InterruptedException {
        ClientThread clientThread = new ClientThread("SELECT * FROM provincia", "Provincia");
        Thread thread = new Thread(clientThread);
        thread.start();
        thread.join();
        return clientThread.getDatos();
    }

    private ArrayList<Object> conectarMuni() throws InterruptedException {

        ClientThread clientThread = new ClientThread("SELECT * FROM municipio", "Municipio");
        Thread thread = new Thread(clientThread);
        thread.start();
        thread.join();
        return clientThread.getDatos();
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

