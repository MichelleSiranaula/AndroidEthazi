package com.elorrieta.euskomet;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class Historico extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    Integer codMuni = Lista.cod_muni;
    Spinner spinnerEstacion, spinnerHoras;
    private EditText etFecha;
    private TextView txtCalidadAire;
    ArrayList<Estacion> datosEstacion = new ArrayList<Estacion>();
    ArrayList<CalidadAire> datosCalidadAire = new ArrayList<CalidadAire>();
    ArrayList<String> arrFechas = new ArrayList<String>();

    int codEstacion = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico);

        etFecha = (EditText)findViewById(R.id.etFecha);
        etFecha.setOnClickListener(this);
        txtCalidadAire = findViewById(R.id.txtCalidadAire);
        spinnerEstacion = findViewById(R.id.spinnerEstacion);
        spinnerEstacion.setOnItemSelectedListener(this);
        spinnerHoras = findViewById(R.id.spinnerHora);
        spinnerHoras.setOnItemSelectedListener(this);

        llenarSpinnerEstacion();
        llenarSpinnerHoras();

    }

    //LLENAR SPINNER HORAS
    public void llenarSpinnerHoras() {
        arrFechas.add("00:00:00");
        arrFechas.add("01:00:00");
        arrFechas.add("02:00:00");
        arrFechas.add("03:00:00");
        arrFechas.add("04:00:00");
        arrFechas.add("05:00:00");
        arrFechas.add("06:00:00");
        arrFechas.add("07:00:00");
        arrFechas.add("08:00:00");
        arrFechas.add("09:00:00");
        arrFechas.add("10:00:00");
        arrFechas.add("11:00:00");
        arrFechas.add("12:00:00");
        arrFechas.add("13:00:00");
        arrFechas.add("14:00:00");
        arrFechas.add("15:00:00");
        arrFechas.add("16:00:00");
        arrFechas.add("17:00:00");
        arrFechas.add("18:00:00");
        arrFechas.add("19:00:00");
        arrFechas.add("20:00:00");
        arrFechas.add("21:00:00");
        arrFechas.add("22:00:00");
        arrFechas.add("23:00:00");

        ArrayAdapter<String> adapter = new ArrayAdapter<String> (this, android.R.layout.simple_spinner_item, arrFechas);
        spinnerHoras.setAdapter(adapter);
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
            if (selecE.equals(datosEstacion.get(i).getNombreEstacion())) {
               codEstacion =  datosEstacion.get(i).getCod_estacion();
            }
        }

        llenarDatosCalidadAire();
        String selecH = spinnerHoras.getSelectedItem().toString();
        String fechaHora = etFecha.getText().toString()+" "+selecH;

        for (int i=0;i<datosCalidadAire.size();i++) {
            if (fechaHora.equals(datosCalidadAire.get(i).getFecha_hora())) {
                if (datosCalidadAire.get(i).getFecha_hora().length() == 0) {
                    txtCalidadAire.setText("Sin datos");
                } else {
                    txtCalidadAire.setText(datosCalidadAire.get(i).getCalidad());
                }
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //LLENAR DATOS CALIDAD AIRE
    public void llenarDatosCalidadAire() {
        ArrayList<Object> arrObject = new ArrayList<Object>();

        try {
            arrObject = conectarCalidadAire();
            for (int i=0;i<arrObject.size();i++) {
                datosCalidadAire.add((CalidadAire) arrObject.get(i));
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
        ClientThreadSelect clientThreadSelect = new ClientThreadSelect("SELECT * FROM calidad_aire WHERE cod_estacion ="+ codEstacion +"", "CalidadAire");
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

    //PARA EL CALENDARIO
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.etFecha:
                showDatePickerDialog();
                break;
        }
    }

    private void showDatePickerDialog() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because January is zero
                String selectedDate = "";
                if (String.valueOf(month).length() == 1 && String.valueOf(day).length() == 1) {
                    selectedDate = year + "-0" + (month+1) + "-0" + day;
                } else if (String.valueOf(month).length() == 1 && String.valueOf(day).length() == 2) {
                    selectedDate = year + "-0" + (month+1) + "-" + day;
                } else if (String.valueOf(month).length() == 2 && String.valueOf(day).length() == 1) {
                    selectedDate = year + "-" + (month+1) + "-" + "0" + day;
                }

                etFecha.setText(selectedDate);
            }
        });

        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
}