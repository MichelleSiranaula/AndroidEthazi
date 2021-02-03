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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.ls.LSInput;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InfoEspacios extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    ImageView imagen;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private ConnectivityManager connectivityManager = null;
    double latitud = 0;
    CheckBox cbFavorito;
    boolean existe;

    String rutaFoto;
    File foto;
    Bitmap imageBitmap = null;
    Button btnFAtrasEsp, btnFAdelanteEsp;
    ArrayList<Bitmap> arrBitmap = new ArrayList<Bitmap>();
    Integer kontFotos = 0;

    Integer codEspacios = ListaEspacios.cod_espacios;
    ArrayList<EspaciosNaturales> datosEspacios = ListaEspacios.arrayEspacios;
    TextView txtNombreEspacios, txtInfoEspacios;

    String volverA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_espacios);

        imagen = findViewById(R.id.imagen);
        txtNombreEspacios = findViewById(R.id.txtNombreEspacio);
        txtInfoEspacios = findViewById(R.id.txtInfoEspacio);
        txtInfoEspacios.setMovementMethod(new ScrollingMovementMethod());
        cbFavorito = findViewById(R.id.cbFavorito);
        cbFavorito.setOnCheckedChangeListener(this);
        btnFAtrasEsp = findViewById(R.id.btnFAtrasEsp);
        btnFAtrasEsp.setEnabled(false);
        btnFAdelanteEsp = findViewById(R.id.btnFAdelanteEsp);

        volverA = getIntent().getExtras().get("VolverE").toString();

        if (codEspacios == 0 && datosEspacios.size() == 0) {
            codEspacios = TopEspacios.cod_espacio;
            datosEspacios = TopEspacios.arrayEspacios;
        }

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

        for (int i = 0; i< datosEspacios.size(); i++) {
            if (datosEspacios.get(i).getCod_enatural() == codEspacios) {
                txtNombreEspacios.setText(datosEspacios.get(i).getNombre());
                txtInfoEspacios.setText(datosEspacios.get(i).getDescripcion());
                latitud = datosEspacios.get(i).getLatitud();
            }
        }

        try {
            arrBitmap = sacarFoto();
            if (arrBitmap.size() != 0) {
                imagen.setImageBitmap(arrBitmap.get(kontFotos));
            } else {
                btnFAdelanteEsp.setEnabled(false);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    //BOTON FOTO ADELANTE
    public void siguenteFoto(View view) {
        if (arrBitmap.size()-1 > kontFotos) {
            kontFotos++;
            imagen.setImageBitmap(arrBitmap.get(kontFotos));
            btnFAtrasEsp.setEnabled(true);
        }

        if (arrBitmap.size()-1 == kontFotos) {
            btnFAdelanteEsp.setEnabled(false);
        }
    }

    //BOTON FOTO ATRAS
    public void fotoAnterior(View view) {
        if (kontFotos > 0) {
            kontFotos --;
            imagen.setImageBitmap(arrBitmap.get(kontFotos));
            btnFAdelanteEsp.setEnabled(true);
        }

        if (kontFotos == 0) {
            btnFAtrasEsp.setEnabled(false);
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
        Intent intent = new Intent(this, InfoEspacios.class);
        intent.putExtra("VolverE",getIntent().getExtras().get("VolverE").toString());
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
            if (arrBitmap.size() == 0) {
                imagen.setImageBitmap(imageBitmap);
                btnFAdelanteEsp.setEnabled(false);
                btnFAtrasEsp.setEnabled(false);
            } else {
                btnFAdelanteEsp.setEnabled(true);
            }
            arrBitmap.add(imageBitmap);

            try {
                insertarFoto();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    //PARA GUARDAR LA FOTO EN LA BBDD
    private void insertarFoto() throws InterruptedException {
        ClientThreadFoto clientThread = new ClientThreadFoto("INSERT INTO foto_espacios (imagen_e,cod_enatural,cod_usuario) values (?,"+  codEspacios +","+ MainActivity.codUsuario +")","InsertF");
        clientThread.setFoto(foto);
        Thread thread = new Thread(clientThread);
        thread.start();
        thread.join();
    }

    //PARA SACAR LA FOTO EN LA BBDD
    private ArrayList<Bitmap> sacarFoto() throws InterruptedException {
        ClientThreadFoto clientThread = new ClientThreadFoto("SELECT imagen_e FROM foto_espacios WHERE cod_enatural = "+ codEspacios +" AND cod_usuario ="+ MainActivity.codUsuario +"","SelectF");
        Thread thread = new Thread(clientThread);
        thread.start();
        thread.join();
        return clientThread.getarrBitmap();
    }

    //INSERTAR EN LA TABLA FAV_ESPACIOS
    private void insertarFav() throws InterruptedException {
        ClientThread clientThread = new ClientThread("INSERT into fav_espacios (cod_enatural,cod_usuario) values ("+ codEspacios +","+ MainActivity.codUsuario +")");
        Thread thread = new Thread(clientThread);
        thread.start();
        thread.join();
    }

    //BORRAR DE LA TABLA FAV_ESPACIOS
    private void borrarFav() throws InterruptedException {
        ClientThread clientThread = new ClientThread("DELETE FROM fav_espacios WHERE cod_enatural='" + codEspacios + "' AND cod_usuario ="+ MainActivity.codUsuario +"");
        Thread thread = new Thread(clientThread);
        thread.start();
        thread.join();
    }

    //SELECT PARA SABER SI EXISTE EN LA TABLA DE FAV_ESPACIOS
    private boolean existeDB() throws InterruptedException {
        ClientThreadSimple clientThread = new ClientThreadSimple("SELECT * FROM fav_espacios WHERE cod_enatural='" + codEspacios + "' AND cod_usuario ="+ MainActivity.codUsuario +"");
        Thread thread = new Thread(clientThread);
        thread.start();
        thread.join();
        return clientThread.getExiste();
    }

    //SELECT PARA LAS ESTACIONES
    private boolean conectarEstacion() throws InterruptedException {
        ClientThreadSimple clientThreadSelect = new ClientThreadSimple("SELECT e.cod_estacion, e.nombre, e.cod_muni FROM estaciones e, municipio m, muni_espacios me WHERE e.cod_muni = m.cod_muni AND me.cod_muni = m.cod_muni AND me.cod_enatural ="+ codEspacios +"");
        Thread thread = new Thread(clientThreadSelect);
        thread.start();
        thread.join();
        return clientThreadSelect.getExiste();
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
            if (latitud != 0) {
                abrirMapa("Espacios");
            } else {
                Toast.makeText(this, "No podemos mostras la ubicacion", Toast.LENGTH_LONG).show();
            }
            return true;
        }
        if (id == R.id.share) {
            compartirTexto();
            return true;
        }
        if (id == R.id.camara) {
            hacerFoto();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //COMPARTIR CON OTRAS APPS
    public void compartirTexto(){
        Intent sendI = new Intent();
        sendI.setAction(Intent.ACTION_SEND);
        sendI.putExtra(Intent.EXTRA_TEXT, txtNombreEspacios.getText());
        sendI.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendI, null);
        startActivity(shareIntent);
    }

    //PARA ABRIR EL MAPA
    public void abrirMapa(String mapaE){
        Intent mapa = new Intent (this, GoogleMaps.class);
        mapa.putExtra("mapa", mapaE);
        mapa.putExtra("VolverE", volverA);
        startActivity(mapa);
    }

    //PARA IR A LA PANTALLA DE LISTA
    public void volver(View view){
        finish();
        Intent volver = null;
        if (volverA.equals("Lista")) {
            volver = new Intent (this, ListaEspacios.class);
        } else if (volverA.equals("Top")) {
            volver = new Intent (this, TopEspacios.class);
        }
        startActivity(volver);
    }

    //PARA IR A LA PANTALLA HISTORICO ESPACIOS
    public void siguiente(View view) throws InterruptedException {
        boolean existe = conectarEstacion();
        if (existe == true) {
            finish();
            Intent siguiente = new Intent (this, HistoricoEspacios.class);
            siguiente.putExtra("VolverE", volverA);
            startActivity(siguiente);
        } else {
            Toast.makeText(this, "No hay información disponible.", Toast.LENGTH_LONG).show();
        }
    }

}