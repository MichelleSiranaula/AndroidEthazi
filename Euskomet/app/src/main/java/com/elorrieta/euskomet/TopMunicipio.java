package com.elorrieta.euskomet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class TopMunicipio extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spinnerTop;
    private RecyclerView oRecyclerView;
    private ListaAdapter oListaAdapter = null;
    public static Integer cod_muni = 0;
    public static ArrayList<Municipio> arrayMuni = new ArrayList<Municipio>();

    private ArrayList<Municipio> datosMuni = new ArrayList<Municipio>();
    private ArrayList<Provincia> datosProv = new ArrayList<Provincia>();

    private ArrayList<Municipio> topBizkaia = new ArrayList<Municipio>();
    private ArrayList<TopFavMunicipio> topFavBizkaia = new ArrayList<TopFavMunicipio>();

    private ArrayList<Municipio> topGipuzkoa = new ArrayList<Municipio>();

    private ArrayList<Municipio> topAraba = new ArrayList<Municipio>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_municipio);

        //SPINNER
        spinnerTop = findViewById(R.id.spinnerTop);
        spinnerTop.setOnItemSelectedListener(this);

        try {
            llenarSpinnerProv();
            llenarMunicipios();
            llenarTopFavBizkaia();
            llenarTopBizkaia();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        //RECYCLERVIEW
        oRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewTop);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        oRecyclerView.setLayoutManager(llm);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selec= spinnerTop.getSelectedItem().toString();
        if (selec.equals("Bizkaia")) {
            oListaAdapter = new ListaAdapter(topBizkaia, new OnItemClickListener() {
                @Override
                public void onItemClick(Municipio item) {
                    cod_muni=item.getCod_muni();
                    arrayMuni = topBizkaia;
                }
            });
        } else if (selec.equals("Gipuzkoa")) {

        } else if (selec.equals("Araba")) {

        }
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
        spinnerTop.setAdapter(adapter);
    }

    //LLENAR ARRAY CON TODOS LOS MUNICIPIOS
    public void llenarMunicipios() throws InterruptedException {
        ArrayList<Object> arrObject = new ArrayList<Object>();
        arrObject = conectarMuni();
        for (int i=0;i<arrObject.size();i++) {
            datosMuni.add((Municipio) arrObject.get(i));
        }
    }

    //LLENAR ARRAY DE TOP MUNI FAV DE BIZKAIA
    public void llenarTopFavBizkaia() throws InterruptedException {
        ArrayList<Object> arrObject = new ArrayList<Object>();
        arrObject = favBizkaia();
        for (int i=0;i<arrObject.size();i++) {
            topFavBizkaia.add((TopFavMunicipio) arrObject.get(i));
            Log.i("TOPFAV_CANT", topFavBizkaia.get(i).getCant()+"");
            Log.i("TOPFAV_COD_MUNI", topFavBizkaia.get(i).getCod_muni()+"");

        }
    }

    //LLENAR ARRAY MUNI FAV DE BIZKAIA
    public void llenarTopBizkaia() throws InterruptedException {
        for (int i=0;i<topFavBizkaia.size();i++) {
            if (topFavBizkaia.get(i).getCod_muni() == datosMuni.get(i).getCod_muni()) {
                topBizkaia.add(datosMuni.get(i));
                Log.i("COD_MUNI", topBizkaia.get(0).getCod_muni()+"");
                Log.i("CONT", topBizkaia.get(0).getNombre()+"");
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
    private ArrayList<Object> conectarMuni() throws InterruptedException {
        ClientThreadSelect clientThreadSelect = new ClientThreadSelect("SELECT * FROM municipio", "Municipio");
        Thread thread = new Thread(clientThreadSelect);
        thread.start();
        thread.join();
        return clientThreadSelect.getDatos();
    }

    //PARA SACAR LOS MUNICIPIOS MAS FAVORITOS DE BIZKAIA
    private ArrayList<Object> favBizkaia() throws InterruptedException {
        ClientThreadSelect clientThreadSelect = new ClientThreadSelect("SELECT COUNT(fm.cod_muni), fm.cod_muni FROM fav_municipio fm, municipio m WHERE fm.cod_muni = m.cod_muni AND m.cod_prov = 1 GROUP BY m.cod_muni ORDER BY COUNT(fm.cod_muni) DESC", "Top");
        Thread thread = new Thread(clientThreadSelect);
        thread.start();
        thread.join();
        return clientThreadSelect.getDatos();
    }

    //PARA SACAR LOS MUNICIPIOS MAS FAVORITOS DE GIPUZKOA
    private ArrayList<Object> favGipuzkoa() throws InterruptedException {
        ClientThreadSelect clientThreadSelect = new ClientThreadSelect("SELECT COUNT(fm.cod_muni), fm.cod_muni FROM fav_municipio fm, municipio m WHERE fm.cod_muni = m.cod_muni AND m.cod_prov = 2", "Top");
        Thread thread = new Thread(clientThreadSelect);
        thread.start();
        thread.join();
        return clientThreadSelect.getDatos();
    }
    //PARA SACAR LOS MUNICIPIOS MAS FAVORITOS DE ARABA
    private ArrayList<Object> favAraba() throws InterruptedException {
        ClientThreadSelect clientThreadSelect = new ClientThreadSelect("SELECT COUNT(fm.cod_muni), fm.cod_muni FROM fav_municipio fm, municipio m WHERE fm.cod_muni = m.cod_muni AND m.cod_prov = 3", "Top");
        Thread thread = new Thread(clientThreadSelect);
        thread.start();
        thread.join();
        return clientThreadSelect.getDatos();
    }


}