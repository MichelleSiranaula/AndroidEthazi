package com.elorrieta.euskomet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

    private EditText etUsuario, etContraseña;
    public static SharedPreferences prefe;
    public static String GuardarCont,GuardarUsu;
    private ImageView imageView;
    ArrayList<usuarios> usuarioarr = new ArrayList<usuarios>();
    String nombrearr,contrarr,parr;

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

        prefe = getSharedPreferences("usuarios", Context.MODE_PRIVATE);
        GuardarCont = prefe.getString(usuario + "contra", "");
        GuardarUsu = prefe.getString(usuario + "nombre", "");

        if (etUsuario.getText().toString().isEmpty() || etContraseña.getText().toString().isEmpty()) {
            Toast.makeText(this, "Algún campo está vacio", Toast.LENGTH_LONG).show();
        } else {

            ArrayList<Object> arrObject = new ArrayList<Object>();
            arrObject = usuar();
            for (int i = 0; i < arrObject.size(); i++) {
                usuarioarr.add((usuarios) arrObject.get(i));
            }

            for (int i = 0; i < usuarioarr.size(); i++) {

                nombrearr = usuarioarr.get(i).getNombre().toString();
                contrarr = usuarioarr.get(i).getContraseña().toString();
                parr = usuarioarr.get(i).getP_clave().toString();

                if (usuario.equals(nombrearr)) {

                    MessageDigest md2 = MessageDigest.getInstance("SHA");
                    byte dataBytes2[] = contraseña.getBytes();
                    md2.update(dataBytes2);

                    byte array2[] = md2.digest();
                    String texto3 = "";
                    for (byte b : array2) {
                        texto3 += b;
                    }

                    if (texto3.equals(contrarr)) {
                        Toast.makeText(this, "SESIÓN INICIADA", Toast.LENGTH_LONG).show();
                        siguiente(null);

                    } else {
                        Toast.makeText(this, "CONTRASEÑA INCORRECTA", Toast.LENGTH_LONG).show();
                    }
                }
                }
            if(!usuario.equals(nombrearr)){
                Toast.makeText(this, "USUARIO NO REGISTRADO", Toast.LENGTH_LONG).show();
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

    public void siguiente(View view){
        finish();
        Intent sig = new Intent (this, menuprincipal.class);
        startActivity(sig);
    }

    public void mover_Animation() {
        Animation oAnimacion = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.mover_derecha);
        imageView.startAnimation(oAnimacion);
    }

    private ArrayList<Object> usuar() throws InterruptedException {
        ClientThread clientThread = new ClientThread("SELECT * FROM usuario", "usuarios");
        Thread thread = new Thread(clientThread);
        thread.start();
        thread.join();
        return clientThread.getDatos();
    }


    public void init(){


    }

}