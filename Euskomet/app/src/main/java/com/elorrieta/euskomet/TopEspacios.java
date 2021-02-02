package com.elorrieta.euskomet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

public class TopEspacios extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spinnerTopE;
    private RecyclerView oRecyclerViewE;
    private ListaAdapterEspacios oListaAdapter = null;
    public static Integer cod_espacio = 0;
    public static ArrayList<EspaciosNaturales> arrayEspacios = new ArrayList<EspaciosNaturales>();

    private ArrayList<EspaciosNaturales> datosEspacios = new ArrayList<EspaciosNaturales>();
    private ArrayList<Provincia> datosProv = new ArrayList<Provincia>();

    private ArrayList<EspaciosNaturales> topBizkaia = new ArrayList<EspaciosNaturales>();
    private ArrayList<TopFavEspacios> topFavBizkaia = new ArrayList<TopFavEspacios>();

    private ArrayList<EspaciosNaturales> topGipuzkoa = new ArrayList<EspaciosNaturales>();
    private ArrayList<TopFavEspacios> topFavGipuzkoa = new ArrayList<TopFavEspacios>();

    private ArrayList<EspaciosNaturales> topAraba = new ArrayList<EspaciosNaturales>();
    private ArrayList<TopFavEspacios> topFavAraba = new ArrayList<TopFavEspacios>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_espacios);

        //SPINNER
        spinnerTopE = findViewById(R.id.spinnerTopE);
        spinnerTopE.setOnItemSelectedListener(this);

        try {
            llenarSpinnerProv();
            llenarMunicipios();
            //BIZKAIA
            llenarTopFavBizkaia();
            llenarTopBizkaia();
            //GIPUZKOA
            llenarTopFavGipuzkoa();
            llenarTopGipuzkoa();
            //ARABA
            llenarTopFavAraba();
            llenarTopAraba();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        //RECYCLERVIEW
        oRecyclerViewE = (RecyclerView) findViewById(R.id.recyclerViewE);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        oRecyclerViewE.setLayoutManager(llm);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selec= spinnerTopE.getSelectedItem().toString();
        if (selec.equals("Bizkaia")) {
            oListaAdapter = new ListaAdapterEspacios(topBizkaia, "Medalla", new OnItemClickListenerE() {
                @Override
                public void onItemClick(EspaciosNaturales item) {
                    cod_espacio=item.getCod_enatural();
                    arrayEspacios = topBizkaia;
                    siguiente();
                }
            });
        } else if (selec.equals("Gipuzkoa")) {
            oListaAdapter = new ListaAdapterEspacios(topGipuzkoa, "Medalla", new OnItemClickListenerE() {
                @Override
                public void onItemClick(EspaciosNaturales item) {
                    cod_espacio=item.getCod_enatural();
                    arrayEspacios = topGipuzkoa;
                    siguiente();
                }
            });
        } else if (selec.equals("Araba")) {
            oListaAdapter = new ListaAdapterEspacios(topAraba, "Medalla", new OnItemClickListenerE() {
                @Override
                public void onItemClick(EspaciosNaturales item) {
                    cod_espacio=item.getCod_enatural();
                    arrayEspacios = topAraba;
                    siguiente();
                }
            });
        }
        oRecyclerViewE.setAdapter(oListaAdapter);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //LLENAR EL SPINNER DE PROVINCIAS
    public void llenarSpinnerProv() throws InterruptedException {
        ArrayList<Object> arrObject = new ArrayList<Object>();
        ArrayList<String> listaNombProv = new ArrayList<String>();

        arrObject = conectarProvincia();
        for (int i=0;i<arrObject.size();i++) {
            datosProv.add((Provincia) arrObject.get(i));
            listaNombProv.add(datosProv.get(i).getNombreProv());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String> (this, android.R.layout.simple_spinner_item, listaNombProv);
        spinnerTopE.setAdapter(adapter);
    }

    //LLENAR ARRAY CON TODOS LOS MUNICIPIOS
    public void llenarMunicipios() throws InterruptedException {
        ArrayList<Object> arrObject = new ArrayList<Object>();
        arrObject = conectarEspacios();
        for (int i=0;i<arrObject.size();i++) {
            datosEspacios.add((EspaciosNaturales) arrObject.get(i));
        }
    }

    //LLENAR ARRAY DE TOP MUNI FAV DE BIZKAIA
    public void llenarTopFavBizkaia() throws InterruptedException {
        ArrayList<Object> arrObject = new ArrayList<Object>();
        arrObject = favBizkaia();
        for (int i=0;i<arrObject.size();i++) {
            topFavBizkaia.add((TopFavEspacios) arrObject.get(i));
        }
    }

    //LLENAR ARRAY MUNI FAV DE BIZKAIA
    public void llenarTopBizkaia() throws InterruptedException {
        for (int i=0;i<topFavBizkaia.size();i++) {
            for (int j=0;j<datosEspacios.size();j++) {
                if (topFavBizkaia.get(i).getCod_espacios() == datosEspacios.get(j).getCod_enatural()) {
                    topBizkaia.add(datosEspacios.get(j));
                }
            }
        }
    }

    //LLENAR ARRAY DE TOP MUNI FAV DE GIPUZKOA
    public void llenarTopFavGipuzkoa() throws InterruptedException {
        ArrayList<Object> arrObject = new ArrayList<Object>();
        arrObject = favGipuzkoa();
        for (int i=0;i<arrObject.size();i++) {
            topFavGipuzkoa.add((TopFavEspacios) arrObject.get(i));
        }
    }

    //LLENAR ARRAY MUNI FAV DE GIPUZKOA
    public void llenarTopGipuzkoa() throws InterruptedException {
        for (int i=0;i<topFavGipuzkoa.size();i++) {
            for (int j=0;j<datosEspacios.size();j++) {
                if (topFavGipuzkoa.get(i).getCod_espacios() == datosEspacios.get(j).getCod_enatural()) {
                    topGipuzkoa.add(datosEspacios.get(j));
                }
            }
        }
    }

    //LLENAR ARRAY DE TOP MUNI FAV DE ARABA
    public void llenarTopFavAraba() throws InterruptedException {
        ArrayList<Object> arrObject = new ArrayList<Object>();
        arrObject = favAraba();
        for (int i=0;i<arrObject.size();i++) {
            topFavAraba.add((TopFavEspacios) arrObject.get(i));
        }
    }

    //LLENAR ARRAY MUNI FAV DE ARABA
    public void llenarTopAraba() throws InterruptedException {
        for (int i=0;i<topFavAraba.size();i++) {
            for (int j=0;j<datosEspacios.size();j++) {
                if (topFavAraba.get(i).getCod_espacios() == datosEspacios.get(j).getCod_enatural()) {
                    topAraba.add(datosEspacios.get(j));
                }
            }
        }
    }

    //TODAS LAS PROVINCIAS
    private ArrayList<Object> conectarProvincia() throws InterruptedException {
        ClientThreadSelect clientThreadSelect = new ClientThreadSelect("SELECT * FROM provincia", "Provincia");
        Thread thread = new Thread(clientThreadSelect);
        thread.start();
        thread.join();
        return clientThreadSelect.getDatos();
    }

    //TODOS LOS MUNICIPIOS
    private ArrayList<Object> conectarEspacios() throws InterruptedException {
        ClientThreadSelect clientThreadSelect = new ClientThreadSelect("SELECT DISTINCT e.* FROM espacios_naturales e, municipio m, muni_espacios me WHERE e.cod_enatural = me.cod_enatural AND m.cod_muni = me.cod_muni order by e.cod_enatural", "Espacios2");
        Thread thread = new Thread(clientThreadSelect);
        thread.start();
        thread.join();
        return clientThreadSelect.getDatos();
    }

    //PARA SACAR LOS MUNICIPIOS MAS FAVORITOS DE BIZKAIA
    private ArrayList<Object> favBizkaia() throws InterruptedException {
        ClientThreadSelect clientThreadSelect = new ClientThreadSelect("SELECT COUNT(fav_espacios.cod_enatural), fav_espacios.cod_enatural FROM fav_espacios, muni_espacios, municipio WHERE muni_espacios.cod_enatural = fav_espacios.cod_enatural AND muni_espacios.cod_muni = municipio.cod_muni AND municipio.cod_prov = 1 GROUP BY fav_espacios.cod_enatural ORDER BY COUNT(fav_espacios.cod_enatural) DESC", "TopEsp");
        Thread thread = new Thread(clientThreadSelect);
        thread.start();
        thread.join();
        return clientThreadSelect.getDatos();
    }

    //PARA SACAR LOS MUNICIPIOS MAS FAVORITOS DE BIZKAIA
    private ArrayList<Object> favGipuzkoa() throws InterruptedException {
        ClientThreadSelect clientThreadSelect = new ClientThreadSelect("SELECT COUNT(fav_espacios.cod_enatural), fav_espacios.cod_enatural FROM fav_espacios, muni_espacios, municipio WHERE muni_espacios.cod_enatural = fav_espacios.cod_enatural AND muni_espacios.cod_muni = municipio.cod_muni AND municipio.cod_prov = 2 GROUP BY fav_espacios.cod_enatural ORDER BY COUNT(fav_espacios.cod_enatural) DESC", "TopEsp");
        Thread thread = new Thread(clientThreadSelect);
        thread.start();
        thread.join();
        return clientThreadSelect.getDatos();
    }

    //PARA SACAR LOS MUNICIPIOS MAS FAVORITOS DE BIZKAIA
    private ArrayList<Object> favAraba() throws InterruptedException {
        ClientThreadSelect clientThreadSelect = new ClientThreadSelect("SELECT COUNT(fav_espacios.cod_enatural), fav_espacios.cod_enatural FROM fav_espacios, muni_espacios, municipio WHERE muni_espacios.cod_enatural = fav_espacios.cod_enatural AND muni_espacios.cod_muni = municipio.cod_muni AND municipio.cod_prov = 3 GROUP BY fav_espacios.cod_enatural ORDER BY COUNT(fav_espacios.cod_enatural) DESC", "TopEsp");
        Thread thread = new Thread(clientThreadSelect);
        thread.start();
        thread.join();
        return clientThreadSelect.getDatos();
    }

    //PARA IR A LA PANTALLA DE INFO ESPACIOS
    public void siguiente(){
        finish();
        Intent siguiente = new Intent (this, InfoEspacios.class);
        siguiente.putExtra("VolverE", "Top");
        startActivity(siguiente);
    }

    public void volver(View view) {
        finish();
        Intent siguiente = new Intent (this, menuprincipal.class);
        startActivity(siguiente);
    }

}