package com.elorrieta.euskomet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

public class Historico extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Integer codMuni = Lista.cod_muni;
    Spinner spinnerEstacion, spinnerFech;
    ArrayList<Estacion> datosEstacion = new ArrayList<Estacion>();
    ArrayList<CalidadAire> datosCalidadAire = new ArrayList<CalidadAire>();

    int codEstacion = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico);

        spinnerEstacion = findViewById(R.id.spinnerEstacion);
        spinnerEstacion.setOnItemSelectedListener(this);

        spinnerFech = findViewById(R.id.spinnerFech);
        spinnerFech.setOnItemSelectedListener(this);

        llenarSpinnerEstacion();

    }

    //LLENAR SPINNER ESTACION
    public void llenarSpinnerEstacion() {
        ArrayList<Object> arrObject = new ArrayList<Object>();
        ArrayList<String> listaNombEstacion = new ArrayList<String>();

        try {
            arrObject = conectarEstacion();
            for (int i=0;i<arrObject.size();i++) {
                datosEstacion.add((Estacion) arrObject.get(i));
                listaNombEstacion.add(datosEstacion.get(i).getNombreEstacion());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String> (this, android.R.layout.simple_spinner_item, listaNombEstacion);
        spinnerEstacion.setAdapter(adapter);
    }

    //ITEM SELECTED DEL SPINNER
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selecE = spinnerEstacion.getSelectedItem().toString();
        // PARA SABER EL CODIGO DE ESTACION SELECIONADO EN EL SPINNER ESTACION
        for (int i=0;i<datosEstacion.size();i++) {
            if (selecE == datosEstacion.get(i).getNombreEstacion()) {
               codEstacion =  datosEstacion.get(i).getCod_estacion();
            }
        }

        // LLENAR EL SPINNER FECHA HORA
        llenarDatosCalidadAire();
        ArrayList<String> fechaHora = new ArrayList<String>();
        for (int i=0;i<datosCalidadAire.size();i++) {
            //if (codEstacion == datosEstacion.get(i).getCod_estacion()) {
                fechaHora.add(datosCalidadAire.get(i).getCalidad());
            //}
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String> (this, android.R.layout.simple_spinner_item, fechaHora);
        spinnerFech.setAdapter(adapter);

        //String selecF = spinnerFech.getSelectedItem().toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //LLENAR DATOS CALIDAD AIRE
    public void llenarDatosCalidadAire() {
        ArrayList<Object> arrObject = new ArrayList<Object>();
        ArrayList<String> listaFechaHora = new ArrayList<String>();

        try {
            arrObject = conectarCalidadAire();
            for (int i=0;i<arrObject.size();i++) {
                datosCalidadAire.add((CalidadAire) arrObject.get(i));
                listaFechaHora.add(datosCalidadAire.get(i).getCalidad());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //SELECT PARA LAS ESTACIONES
    private ArrayList<Object> conectarEstacion() throws InterruptedException {
        ClientThreadSelect clientThreadSelect = new ClientThreadSelect("SELECT e.cod_estacion, e.nombre, e.cod_muni FROM estaciones e, municipio m WHERE e.cod_muni = m.cod_muni AND e.cod_muni ="+ codMuni +"", "Estacion");
        Thread thread = new Thread(clientThreadSelect);
        thread.start();
        thread.join();
        return clientThreadSelect.getDatos();
    }

    //SELECT PARA LA CALIDAD DE AIRE
    private ArrayList<Object> conectarCalidadAire() throws InterruptedException {
        ClientThreadSelect clientThreadSelect = new ClientThreadSelect("SELECT ca.* FROM calidad_aire ca, estaciones e WHERE e.cod_estacion = ca.cod_estacion AND ca.cod_estacion ="+ codEstacion +"", "CalidadAire");
        Thread thread = new Thread(clientThreadSelect);
        thread.start();
        thread.join();
        return clientThreadSelect.getDatos();
    }

    public void volver(View view){
        finish();
        Intent volver = new Intent (this, Info.class);
        startActivity(volver);
    }


}