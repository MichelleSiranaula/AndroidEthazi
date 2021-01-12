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

public class Lista extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ConnectivityManager connectivityManager = null;
    private Spinner spinner;
    RecyclerView oRecyclerView;

    ArrayList<Municipio> datosMuni = new ArrayList<Municipio>();
    ArrayList<Provincia> datosProv = new ArrayList<Provincia>();
    //ArrayList<Object> arrObject = new ArrayList<Object>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        spinner = findViewById(R.id.spinner);

        conectarOnClick(null);

        /*try {
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

        ListaAdapter oContactoAdapter = new ListaAdapter(datosMuni,this);
        /*ListaAdapter oContactoAdapter = new ListaAdapter(arrMuni, new OnItemClickListener() {
            @Override public void onItemClick(Municipio item) {
                //Toast.makeText(this, "Nombre : " + item.getNombre(), Toast.LENGTH_LONG).show();
                Toast.makeText(this, "nombre", Toast.LENGTH_LONG).show();
            }
        });*/
       // oRecyclerView.setAdapter(oContactoAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
    }

    public void volver(View view){
        finish();
        Intent volver = new Intent (this, menuprincipal.class);
        startActivity(volver);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menusen, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.logout) {
            finish();
            Intent volver = new Intent (this, MainActivity.class);
            startActivity(volver);
            return true;
        }
        if (id == R.id.info) {
            Toast.makeText(this, "Somos el DreamTeam real.", Toast.LENGTH_LONG).show();
            return true;
        }
         return super.onOptionsItemSelected(item);
    }

    public void conectarOnClick(View v) {
        if (isConnected()) {
            try {
                ArrayList<Object> arrObject = new ArrayList<Object>();
                arrObject = conectarProvincia();
                for (int i=0;i<arrObject.size();i++) {
                    datosProv.add((Provincia) arrObject.get(i));
                    System.out.println(datosProv.get(i));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
                ArrayAdapter<Provincia> adapter = new ArrayAdapter<Provincia> (this, android.R.layout.simple_spinner_item, datosProv);
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

