package com.elorrieta.euskomet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class ListaEspacios extends AppCompatActivity implements AdapterView.OnItemSelectedListener,AdapterView.OnItemClickListener{

    private ConnectivityManager connectivityManager = null;
    private Spinner spinner;
    private RecyclerView oRecyclerView;
    private ListaAdapter oListaAdapter = null;

    ArrayList<Provincia> datosProv = new ArrayList<Provincia>();

    ArrayList<EspaciosNaturales> datosEspacios = new ArrayList<EspaciosNaturales>();
    ArrayList<EspaciosNaturales> datosEspaciosB = new ArrayList<EspaciosNaturales>();
    ArrayList<EspaciosNaturales> datosEspaciosG = new ArrayList<EspaciosNaturales>();
    ArrayList<EspaciosNaturales> datosEspaciosA = new ArrayList<EspaciosNaturales>();

    boolean info = true;

    Bundle extras = getIntent().getExtras();
    String accion = extras.getString("accion");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);

        conectarOnClick(null);

        //METER DATOS EN EL ARRAY datosEspacios
        try {
            ArrayList<Object> arrObject = new ArrayList<Object>();
            arrObject = conectarEspacios();
            for (int i=0;i<arrObject.size();i++) {
                datosEspacios.add((EspaciosNaturales) arrObject.get(i));
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i=0;i<datosEspacios.size();i++) {
            if (datosEspacios.get(i).getCod_prov()==1) {
                datosEspaciosB.add(datosEspacios.get(i));
            }
            if (datosEspacios.get(i).getCod_prov()==2) {
                datosEspaciosG.add(datosEspacios.get(i));
            }
            if (datosEspacios.get(i).getCod_prov()==3) {
                datosEspaciosA.add(datosEspacios.get(i));
            }
        }

        //RECYCLERVIEW
        oRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        oRecyclerView.setLayoutManager(llm);


    }

    //
    //ITEM SELECTED DEL SPINNER
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        String selec=spinner.getSelectedItem().toString();
        if (selec.equals("Bizkaia")) {
            oListaAdapter = new ListaAdapter(datosEspaciosB, new OnItemClickListener() {
                @Override
                public void onItemClick(EspaciosNaturales item) {
                    siguiente(datosEspaciosB, item.getCod_enatural());
                }
            });

        } else if (selec.equals("Gipuzkoa")) {
            oListaAdapter = new ListaAdapter(datosEspaciosG,new OnItemClickListener() {
                @Override
                public void onItemClick(EspaciosNaturales item) {
                    siguiente(datosEspaciosG, item.getCod_enatural());
                }
            });
        } else if (selec.equals("Araba")) {
            oListaAdapter = new ListaAdapter(datosEspaciosA,new OnItemClickListener() {
                @Override
                public void onItemClick(EspaciosNaturales item) {
                    siguiente(datosEspaciosA, item.getCod_enatural());
                }
            });
        }
        oRecyclerView.setAdapter(oListaAdapter);
    }

    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
    }

    //PARA IR A LA PANTALLA DE LISTA
    public void siguiente(ArrayList<EspaciosNaturales> arrayEspacios, int codEspacios){
        finish();
        Intent siguiente = new Intent (this, Info.class);
        siguiente.putExtra("arrayEspacios", arrayEspacios);
        siguiente.putExtra("codMunicipio", codEspacios);
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

    private ArrayList<Object> conectarEspacios() throws InterruptedException {

        ClientThread clientThread = new ClientThread("SELECT e.*, m.cod_prov FROM espacios_naturales e, municipio m, muni_espacios me WHERE e.cod_enatural = me.cod_enatural AND m.cod_muni = me.cod_muni order by e.cod_enatural, m.cod_prov", "Espacios");
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
}