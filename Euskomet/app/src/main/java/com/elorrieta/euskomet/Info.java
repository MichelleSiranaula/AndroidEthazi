package com.elorrieta.euskomet;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class Info extends AppCompatActivity {

    ImageView imageView3,imageView4, imagen;
    boolean fav = false;
    private final String CARPETA_RAIZ = "storage/emulated/0/Pictures";
    private final String RUTA_IMAGEN = CARPETA_RAIZ + "misFotos";
    private String path="";
    private int COD_FOTO = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        imageView3 = findViewById(R.id.imageView3);
        imageView4 = findViewById(R.id.imageView4);
        imageView4.setVisibility(View.INVISIBLE);
        imageView3.setVisibility(View.VISIBLE);

        imagen = findViewById(R.id.imagen);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }

    }

    /*public void hacerFoto(View view) {
        File fileImagen = new File(Environment.getExternalStorageState(), CARPETA_RAIZ);
        boolean isCreada = fileImagen.exists();
        String nombreImagen = (System.currentTimeMillis()/1000) + ".jpg";

        if (isCreada == false) {
            isCreada = fileImagen.mkdirs();
        }
        if (isCreada == true) {
            nombreImagen = (System.currentTimeMillis()/1000) + ".jpg";
        }

        //Environment.getExternalStorageState() +
        path = File.separator + CARPETA_RAIZ + File.separator + nombreImagen;
        File imagenF = new File(path);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imagenF));

        MediaScannerConnection.scanFile(this, new String[]{path}, null, new MediaScannerConnection.OnScanCompletedListener() {
            @Override
            public void onScanCompleted(String path, Uri uri) {
                Log.i("Ruta de almacenamiento", "Path :" + path);
            }
        });

        Bitmap bitmap = BitmapFactory.decodeFile(path);
        imagen.setImageBitmap(bitmap);
    }*/

    public void hacerFoto(View view) {
        File fileImagen = new File(Environment.getExternalStorageState(), CARPETA_RAIZ);
        boolean isCreada = fileImagen.exists();
        String nombreImagen = "";

        if (isCreada == false) {
            isCreada = fileImagen.mkdirs();
        }
        if (isCreada == true) {
            nombreImagen = (System.currentTimeMillis()/1000) + ".jpg";
        }

        //Environment.getExternalStorageState() +
        path = Environment.getExternalStorageState() + File.separator + CARPETA_RAIZ + File.separator + nombreImagen;
        File imagenF = new File(path);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String authorities = this.getPackageName() + ".provider";
            Uri imageUri = FileProvider.getUriForFile(this, authorities, imagenF);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imagenF));
        }
        startActivityForResult(intent, COD_FOTO);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (COD_FOTO == 20) {
                MediaScannerConnection.scanFile(this, new String[]{path}, null, new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String path, Uri uri) {

                    }
                });

                Bitmap bitmap = BitmapFactory.decodeFile(path);
                imagen.setImageBitmap(bitmap);
            }
        }
    }


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
            Toast.makeText(this, "Aquí iría el mapa.", Toast.LENGTH_LONG).show();
            return true;
        }
        if (id == R.id.share) {
            Toast.makeText(this, "Aquí podremos compartir.", Toast.LENGTH_LONG).show();
            return true;
        }
        if (id == R.id.camara) {
            hacerFoto(null);
            //Toast.makeText(this, "Aquí irá la cámara.", Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    public void volver(View view){
        finish();
        Intent volver = new Intent (this, Lista.class);
        startActivity(volver);
    }

    public void mostrarfav(View view){
        imageView3.setVisibility(View.INVISIBLE);
        imageView4.setVisibility(View.VISIBLE);
        fav=true;
    }

    public void quitarfav(View view){
        imageView3.setVisibility(View.VISIBLE);
        imageView4.setVisibility(View.INVISIBLE);
        fav = false;
    }

}