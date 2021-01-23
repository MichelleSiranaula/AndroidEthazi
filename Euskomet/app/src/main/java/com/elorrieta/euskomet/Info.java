package com.elorrieta.euskomet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Info extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    ImageView imagen;
    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    String nombreFoto = timeStamp + ".jpg";
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private ConnectivityManager connectivityManager = null;
    String imagenString;
    double latitud = 0;
    CheckBox cbFavorito;
    boolean existe;

    Integer codMuni = Lista.cod_muni;
    ArrayList<Municipio> datosMuni = Lista.arrayMuni;
    TextView txtNombreMuni, txtInfoMuni;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        byte [] encodeByte= null;
        Bitmap bitmap = null;
        InputStream inputStream = null;
        imagen = findViewById(R.id.imagen);
        txtNombreMuni = findViewById(R.id.txtNombreEspacio);
        txtInfoMuni = findViewById(R.id.txtInfoEspacio);
        txtInfoMuni.setMovementMethod(new ScrollingMovementMethod());
        cbFavorito = findViewById(R.id.cbFavorito);
        cbFavorito.setOnCheckedChangeListener(this);

        try {
            existe = existeDB();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (existe == true) {
            cbFavorito.setChecked(true);
        } else {
            cbFavorito.setChecked(false);
        }

        for (int i=0;i<datosMuni.size();i++) {
            if (datosMuni.get(i).getCod_muni() == codMuni) {
                txtNombreMuni.setText(datosMuni.get(i).getNombre());
                txtInfoMuni.setText(datosMuni.get(i).getDescripcion());
                latitud = datosMuni.get(i).getLatitud();

                /*encodeByte=Base64.decode(datosMuni.get(i).getFoto(),Base64.DEFAULT);
                inputStream  = new ByteArrayInputStream(encodeByte);
                bitmap  = BitmapFactory.decodeStream(inputStream);

                encodeByte = Base64.decode(datosMuni.get(i).getFoto(),Base64.DEFAULT);
                bitmap = BitmapFactory.decodeByteArray(encodeByte,0,encodeByte.length);

                encodeByte = Base64.decode(datosMuni.get(i).getFoto(),Base64.URL_SAFE);
                bitmap = BitmapFactory.decodeByteArray(encodeByte,0,encodeByte.length);
                imagen.setImageBitmap(bitmap);*/
            }
        }
    }

    //ON CHECKEDCHANGED DEL COMBOBOX
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        try {
            if (isChecked) {
                if (existe == false) {
                    insertarFav();
                    refrescar();
                }
            } else {
                if (existe == true) {
                    borrarFav();
                    refrescar();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    //REFRESCAR LA PAGINA
    public void refrescar() {
        Intent intent = new Intent(this, Info.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivityForResult(intent, 0);
        overridePendingTransition(0,0);

    }

    //PARA HACER FOTOS
    public void hacerFoto() {
        Intent intento1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File foto = new File(getExternalFilesDir(null), nombreFoto);
        startActivityForResult(intento1, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imagen.setImageBitmap(imageBitmap);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] imagenB = stream.toByteArray();
            imagenString = String.valueOf(Base64.encode(imagenB, Base64.DEFAULT));
            Log.i("foto", imagenString);

            conectarOnClick();
        }
    }

    //PARA GUARDAR LA FOTO EN LA BBDD
    public void conectarOnClick() {
        if (isConnected()) {
            try {
                insertarFoto();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(), "ERROR_NO_INTERNET", Toast.LENGTH_SHORT).show();
        }
    }

    private void insertarFoto() throws InterruptedException {
        ClientThread clientThread = new ClientThread("UPDATE municipio set foto='" + imagenString + "' where cod_muni ="+ codMuni +"");
        Thread thread = new Thread(clientThread);
        thread.start();
        thread.join();
    }

    //INSERTAR EN LA TABLA FAV_MUNICIPIO
    private void insertarFav() throws InterruptedException {
        ClientThread clientThread = new ClientThread("INSERT into fav_municipio (cod_muni,cod_usuario) values ("+ codMuni +","+ MainActivity.codUsuario +")");
        Thread thread = new Thread(clientThread);
        thread.start();
        thread.join();
    }

    //BORRAR DE LA TABLA FAV_MUNICIPIO
    private void borrarFav() throws InterruptedException {
        ClientThread clientThread = new ClientThread("DELETE FROM fav_municipio WHERE cod_muni='" + codMuni + "' AND cod_usuario ="+ MainActivity.codUsuario +"");
        Thread thread = new Thread(clientThread);
        thread.start();
        thread.join();
    }

    //SELECT PARA SABER SI EXISTE EN LA TABLA DE FAV_MUNICIPIO
    private boolean existeDB() throws InterruptedException {
        ClientThreadSimple clientThread = new ClientThreadSimple("SELECT * FROM fav_municipio WHERE cod_muni='" + codMuni + "' AND cod_usuario ="+ MainActivity.codUsuario +"");
        Thread thread = new Thread(clientThread);
        thread.start();
        thread.join();
        return clientThread.getExiste();
    }

    //PARA SABER SI ESTA CONECTADO
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

    //ACTIONBAR
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
        if (id == R.id.mapa) {
            finish();
            if (latitud != 0) {
                abrirMapa(datosMuni,codMuni, "Municipios");
            } else {
                Toast.makeText(this, "No podemos mostras la ubicacion", Toast.LENGTH_LONG).show();
            }
            return true;
        }
        if (id == R.id.share) {
            Toast.makeText(this, "Aquí podremos compartir.", Toast.LENGTH_LONG).show();
            return true;
        }
        if (id == R.id.camara) {
            hacerFoto();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //PARA ABRIR EL MAPA
    public void abrirMapa(ArrayList<Municipio> datosMuni, int codMuni, String mapaM){
        Intent mapa = new Intent (this, GoogleMaps.class);
        mapa.putExtra("arrayMunicipios", datosMuni);
        mapa.putExtra("codMunicipio", codMuni);
        mapa.putExtra("mapa", mapaM);
        startActivity(mapa);
    }

    //PARA IR A LA PANTALLA DE LISTA
    public void volver(View view){
        finish();
        Intent volver = new Intent (this, Lista.class);
        startActivity(volver);
    }

}