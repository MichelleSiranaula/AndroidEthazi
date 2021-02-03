package com.elorrieta.euskomet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static int codUsuario = 0;

    private EditText etUsuario, etContraseña;
    public static SharedPreferences prefe;
    private ImageView imageView;
    ArrayList<Usuarios> usuarioarr = new ArrayList<Usuarios>();
    String nombrearr,contrarr,parr;
    String usuarioCon="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUsuario = (EditText)findViewById(R.id.idUsuarioTxt);
        etContraseña = (EditText)findViewById(R.id.etContraseña);
        imageView = findViewById(R.id.imageView);
        mover_Animation();

    }

    public void logear(View v) throws NoSuchAlgorithmException, InterruptedException {

        String usuario = etUsuario.getText().toString();
        String contraseña = etContraseña.getText().toString();

        if (etUsuario.getText().toString().isEmpty() || etContraseña.getText().toString().isEmpty()) {
            Toast.makeText(this, getResources().getString(R.string.campo_vacio), Toast.LENGTH_LONG).show();
        } else {

            ArrayList<Object> arrObject = new ArrayList<Object>();
            arrObject = usuar();
            for (int i = 0; i < arrObject.size(); i++) {
                usuarioarr.add((Usuarios) arrObject.get(i));
            }

            for (int i = 0; i < usuarioarr.size(); i++) {

                codUsuario = usuarioarr.get(i).getCod_usuario();
                nombrearr = usuarioarr.get(i).getNombre();
                contrarr = usuarioarr.get(i).getContraseña();
                parr = usuarioarr.get(i).getP_clave();

                if (usuario.equals(nombrearr)) {
                    usuarioCon = nombrearr;
                    MessageDigest md2 = MessageDigest.getInstance("SHA");
                    byte dataBytes2[] = contraseña.getBytes();
                    md2.update(dataBytes2);

                    byte array2[] = md2.digest();
                    String texto3 = "";
                    for (byte b : array2) {
                        texto3 += b;
                    }

                    if (texto3.equals(contrarr)) {
                        Toast.makeText(this, getResources().getString(R.string.sesion_iniciada), Toast.LENGTH_LONG).show();
                        siguiente();
                        break;
                    } else {
                        Toast.makeText(this, getResources().getString(R.string.contra_incorrecta), Toast.LENGTH_LONG).show();
                    }

                }
                }
            if(!usuario.equals(nombrearr)){
                Toast.makeText(this, getResources().getString(R.string.usu_no_registrado), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void Registrar(View view){
        finish();
        Intent Registrar = new Intent (this, RegistrarUsuario.class);
        startActivity(Registrar);
    }

    public void recu(View view){
        finish();
        Intent recu = new Intent (this, RecuperarContra.class);
        startActivity(recu);
    }

    public void siguiente(){
        finish();
        Intent sig = new Intent (this, menuprincipal.class);
        startActivity(sig);
    }

    public void mover_Animation() {
        Animation oAnimacion = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.mover_derecha);
        imageView.startAnimation(oAnimacion);
    }

    private ArrayList<Object> usuar() throws InterruptedException {
        ClientThreadSelect clientThreadSelect = new ClientThreadSelect("SELECT * FROM usuario", "usuarios");
        Thread thread = new Thread(clientThreadSelect);
        thread.start();
        thread.join();
        return clientThreadSelect.getDatos();
    }

}