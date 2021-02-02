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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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
    Button btnFAtrasMuni, btnFAdelanteMuni;
    ArrayList<Bitmap> arrBitmap = new ArrayList<Bitmap>();
    Integer kontFotos = 0;

    Integer codMuni = Lista.cod_muni;
    ArrayList<Municipio> datosMuni = Lista.arrayMuni;
    TextView txtNombreMuni, txtInfoMuni;

    String volverA = "";

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
        btnFAtrasMuni = findViewById(R.id.btnFAtrasMuni);
        btnFAtrasMuni.setEnabled(false);
        btnFAdelanteMuni = findViewById(R.id.btnFAdelanteMuni);

        volverA = getIntent().getExtras().get("Volver").toString();

        if (codMuni == 0 && datosMuni.size() == 0) {
            codMuni = TopMunicipio.cod_muni;
            datosMuni = TopMunicipio.arrayMuni;
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

        for (int i=0;i<datosMuni.size();i++) {
            if (datosMuni.get(i).getCod_muni() == codMuni) {
                txtNombreMuni.setText(datosMuni.get(i).getNombre());
                txtInfoMuni.setText(datosMuni.get(i).getDescripcion());
                latitud = datosMuni.get(i).getLatitud();
            }
        }

        try {
            arrBitmap = sacarFoto();
            if (arrBitmap.size() != 0) {
                imagen.setImageBitmap(arrBitmap.get(kontFotos));
            } else {
                btnFAdelanteMuni.setEnabled(false);
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
            btnFAtrasMuni.setEnabled(true);
        }

        if (arrBitmap.size()-1 == kontFotos) {
            btnFAdelanteMuni.setEnabled(false);
        }
    }

    //BOTON FOTO ATRAS
    public void fotoAnterior(View view) {
        if (kontFotos > 0) {
            kontFotos --;
            imagen.setImageBitmap(arrBitmap.get(kontFotos));
            btnFAdelanteMuni.setEnabled(true);
        }

        if (kontFotos == 0) {
            btnFAtrasMuni.setEnabled(false);
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
            if (arrBitmap.size() == 0) {
                imagen.setImageBitmap(imageBitmap);
                btnFAdelanteMuni.setEnabled(false);
                btnFAtrasMuni.setEnabled(false);
            } else {
                btnFAdelanteMuni.setEnabled(true);
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
        ClientThreadFoto clientThread = new ClientThreadFoto("INSERT INTO foto_municipio (imagen,cod_usuario,cod_muni) values (?,"+  MainActivity.codUsuario +","+ codMuni +")","InsertF");
        clientThread.setFoto(foto);
        Thread thread = new Thread(clientThread);
        thread.start();
        thread.join();
    }

    //PARA SACAR LA FOTO EN LA BBDD
    private ArrayList<Bitmap> sacarFoto() throws InterruptedException {
        ClientThreadFoto clientThread = new ClientThreadFoto("SELECT imagen FROM foto_municipio WHERE cod_muni = "+ codMuni +" AND cod_usuario ="+ MainActivity.codUsuario +"","SelectF");
        Thread thread = new Thread(clientThread);
        thread.start();
        thread.join();
        return clientThread.getarrBitmap();
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

    //SELECT PARA LAS ESTACIONES
    private boolean conectarEstacion() throws InterruptedException {
        ClientThreadSimple clientThreadSelect = new ClientThreadSimple("SELECT e.cod_estacion, e.nombre, e.cod_muni FROM estaciones e, municipio m WHERE e.cod_muni = m.cod_muni AND e.cod_muni ="+ codMuni +"");
        Thread thread = new Thread(clientThreadSelect);
        thread.start();
        thread.join();
        return clientThreadSelect.getExiste();
    }

    //COMPARTIR CON OTRAS APPS
    public void compartirTexto(){
        Intent sendI = new Intent();
        sendI.setAction(Intent.ACTION_SEND);
        sendI.putExtra(Intent.EXTRA_TEXT, txtNombreMuni.getText());
        sendI.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendI, null);
        startActivity(shareIntent);
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
                abrirMapa("Municipios");
            } else {
                Toast.makeText(this, "No podemos mostrar la ubicacion", Toast.LENGTH_LONG).show();
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

    //PARA ABRIR EL MAPA
    public void abrirMapa(String mapaM){
        Intent mapa = new Intent (this, GoogleMaps.class);
        mapa.putExtra("mapa", mapaM);
        mapa.putExtra("Volver", volverA);

        startActivity(mapa);
    }

    //PARA IR A LA PANTALLA DE LISTA
    public void volver(View view){
        finish();
        Intent volver = null;
        if (volverA.equals("Lista")) {
            volver = new Intent (this, Lista.class);
        } else if (volverA.equals("Top")) {
            volver = new Intent (this,  TopMunicipio.class);
        }
        startActivity(volver);
    }

    //PARA IR A LA PANTALLA HISTORICO
    public void siguiente(View view) throws InterruptedException {
        boolean existe = conectarEstacion();
        if (existe == true) {
            finish();
            Intent siguiente = new Intent (this, Historico.class);
            startActivity(siguiente);
        } else {
            Toast.makeText(this, "No hay información disponible.", Toast.LENGTH_LONG).show();
        }
    }
}