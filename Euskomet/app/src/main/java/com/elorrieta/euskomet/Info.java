package com.elorrieta.euskomet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Info extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    ImageView imagen;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private ConnectivityManager connectivityManager = null;
    double latitud;
    CheckBox cbFavorito;
    boolean existe;

    String rutaFoto;
    File foto;
    Bitmap imageBitmap = null;

    Integer codMuni = Lista.cod_muni;
    ArrayList<Municipio> datosMuni = Lista.arrayMuni;
    TextView txtNombreMuni, txtInfoMuni;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

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
            }
        }

        try {
            imageBitmap = sacarFoto();
            if (imageBitmap != null) {
                imagen.setImageBitmap(imageBitmap);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
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
        File fotoArchivo = null;
        try {
            fotoArchivo = crearFoto();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (fotoArchivo != null) {
            Uri fotoUri = FileProvider.getUriForFile(this,"com.elorrieta.euskomet.fileprovider",fotoArchivo);
            intento1.putExtra(MediaStore.EXTRA_OUTPUT,fotoUri);
            startActivityForResult(intento1, REQUEST_IMAGE_CAPTURE);
        }
    }

    public File crearFoto() throws IOException {
        String nomFoto = "foto_";
        File dir = getExternalFilesDir(null);
        foto = File.createTempFile(nomFoto,".jpg",dir);
        rutaFoto = foto.getAbsolutePath();
        return foto;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            imageBitmap = BitmapFactory.decodeFile(rutaFoto);
            imagen.setImageBitmap(imageBitmap);
            try {
                insertarFoto();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //PARA GUARDAR LA FOTO EN LA BBDD
    private void insertarFoto() throws InterruptedException {
        ClientThreadFoto clientThread = new ClientThreadFoto("INSERT INTO foto_municipio (imagen,cod_usuario,cod_muni) values (?,"+  MainActivity.codUsuario +","+ codMuni +")","InsertF");
        clientThread.setFoto(foto);
        Thread thread = new Thread(clientThread);
        thread.start();
        thread.join();
    }

    //PARA SACAR LA FOTO EN LA BBDD
    private Bitmap sacarFoto() throws InterruptedException {
        ClientThreadFoto clientThread = new ClientThreadFoto("SELECT imagen FROM foto_municipio WHERE cod_muni = "+ codMuni +" AND cod_usuario ="+ MainActivity.codUsuario +"","SelectF");
        Thread thread = new Thread(clientThread);
        thread.start();
        thread.join();
        return clientThread.getBitmap();
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

    //COMPARTIR CON OTRAS APPS
    public void compartirConApp() {

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
                abrirMapa("Municipios");
            } else {
                Toast.makeText(this, "No podemos mostrar la ubicacion", Toast.LENGTH_LONG).show();
                refrescar();
            }

            return true;
        }
        if (id == R.id.share) {
            Toast.makeText(this, "Aquí podremos compartir", Toast.LENGTH_LONG).show();
            return true;
        }
        if (id == R.id.camara) {
            if (imageBitmap == null) {
                hacerFoto();
            } else {
                Toast.makeText(this, "Ya tienes una foto disponible", Toast.LENGTH_LONG).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //PARA ABRIR EL MAPA
    public void abrirMapa(String mapaM){
        Intent mapa = new Intent (this, GoogleMaps.class);
        mapa.putExtra("mapa", mapaM);
        startActivity(mapa);
    }

    //PARA IR A LA PANTALLA DE LISTA
    public void volver(View view){
        finish();
        Intent volver = new Intent (this, Lista.class);
        startActivity(volver);
    }

    public void siguiente(View view) {
        finish();
        Intent siguiente = new Intent (this, Historico.class);
        startActivity(siguiente);
    }
}