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
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListaEspacios extends AppCompatActivity implements AdapterView.OnItemSelectedListener,AdapterView.OnItemClickListener {
    public static int cod_espacios = 0;

    private ConnectivityManager connectivityManager = null;
    private Spinner spinnerProv, spinnerFiltros;
    private RecyclerView oRecyclerView;
    private ListaAdapterEspacios oListaAdapter = null;

    ArrayList<String> arrayFiltros = new ArrayList<String>();
    ArrayList<Provincia> datosProv = new ArrayList<Provincia>();

    ArrayList<EspaciosNaturales> datosEspacios = new ArrayList<EspaciosNaturales>();
    ArrayList<EspaciosNaturales> datosEspaciosB = new ArrayList<EspaciosNaturales>();
    ArrayList<EspaciosNaturales> datosEspaciosG = new ArrayList<EspaciosNaturales>();
    ArrayList<EspaciosNaturales> datosEspaciosA = new ArrayList<EspaciosNaturales>();

    ArrayList<EspaciosNaturales> espaciosFav = new ArrayList<EspaciosNaturales>();

    //array que se pasa a la clase INFO
    public static ArrayList<EspaciosNaturales> arrayEspacios = new ArrayList<EspaciosNaturales>();

    boolean info = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_espacios);

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

        //METER DATOS EN EL ARRAY espaciosFav
        try {
            ArrayList<Object> arrObject = new ArrayList<Object>();
            arrObject = conectarEspaciosFav();
            for (int i=0;i<arrObject.size();i++) {
                espaciosFav.add((EspaciosNaturales) arrObject.get(i));
                Log.i("LOGI FOR", "HOLA");
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

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
        String selecF= spinnerFiltros.getSelectedItem().toString();
        //CUANDO SELECCIONA PROVINCIAS EN EL SPINNER FILTROS
        if (selecF.equals("Provincias")) {
            spinnerProv.setEnabled(true);
            String selec= spinnerProv.getSelectedItem().toString();
            if (selec.equals("Bizkaia")) {
                oListaAdapter = new ListaAdapterEspacios(datosEspaciosB, "Mapita", new OnItemClickListenerE() {
                    @Override
                    public void onItemClick(EspaciosNaturales item) {
                        cod_espacios=item.getCod_enatural();
                        Log.i("cod_ESPACIOS", cod_espacios+"");
                        arrayEspacios = datosEspaciosB;
                        siguiente();
                    }
                });

            } else if (selec.equals("Gipuzkoa")) {
                oListaAdapter = new ListaAdapterEspacios(datosEspaciosG, "Mapita",new OnItemClickListenerE() {
                    @Override
                    public void onItemClick(EspaciosNaturales item) {
                        cod_espacios=item.getCod_enatural();
                        arrayEspacios = datosEspaciosG;
                        siguiente();
                    }
                });
            } else if (selec.equals("Araba")) {
                oListaAdapter = new ListaAdapterEspacios(datosEspaciosA, "Mapita",new OnItemClickListenerE() {
                    @Override
                    public void onItemClick(EspaciosNaturales item) {
                        cod_espacios=item.getCod_enatural();
                        arrayEspacios = datosEspaciosA;
                        siguiente();
                    }
                });
            }
            oRecyclerView.setAdapter(oListaAdapter);
        } else if (selecF.equals("Favoritos")) {
            Log.i("SIZE", espaciosFav.size()+"");
            spinnerProv.setEnabled(false);
            oListaAdapter = new ListaAdapterEspacios(espaciosFav, "Mapita",new OnItemClickListenerE() {
                @Override
                public void onItemClick(EspaciosNaturales item) {
                    cod_espacios=item.getCod_enatural();
                    arrayEspacios = espaciosFav;
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
        Intent siguiente = new Intent (this, InfoEspacios.class);
        siguiente.putExtra("VolverE", "Lista");
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
        getMenuInflater().inflate(R.menu.menulista, menu);
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

        if (id == R.id.info) {
            Toast.makeText(this, getResources().getString(R.string.talde_3), Toast.LENGTH_LONG).show();
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
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }
    }

    private ArrayList<Object> conectarProvincia() throws InterruptedException {
        ClientThreadSelect clientThreadSelect = new ClientThreadSelect("SELECT * FROM provincia", "Provincia");
        Thread thread = new Thread(clientThreadSelect);
        thread.start();
        thread.join();
        return clientThreadSelect.getDatos();
    }

    private ArrayList<Object> conectarEspacios() throws InterruptedException {

        ClientThreadSelect clientThreadSelect = new ClientThreadSelect("SELECT DISTINCT e.*, m.cod_prov FROM espacios_naturales e, municipio m, muni_espacios me WHERE e.cod_enatural = me.cod_enatural AND m.cod_muni = me.cod_muni order by e.cod_enatural, m.cod_prov", "Espacios");
        Thread thread = new Thread(clientThreadSelect);
        thread.start();
        thread.join();
        return clientThreadSelect.getDatos();
    }

    private ArrayList<Object> conectarEspaciosFav() throws InterruptedException {
        ClientThreadSelect clientThreadSelect = new ClientThreadSelect("SELECT e.* FROM espacios_naturales e, fav_espacios fe WHERE fe.cod_enatural = e.cod_enatural AND fe.cod_usuario ="+ MainActivity.codUsuario +"","Espacios2");
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
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_comunicacion), Toast.LENGTH_SHORT).show();
        }
        return ret;
    }
}
