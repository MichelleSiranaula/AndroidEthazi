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

public class MainActivity extends AppCompatActivity {

    private EditText etUsuario, etContraseña;
    public static SharedPreferences prefe;
    public static String GuardarCont,GuardarUsu;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUsuario = (EditText)findViewById(R.id.idUsuarioTxt);
        etContraseña = (EditText)findViewById(R.id.etContraseña);
        imageView = findViewById(R.id.imageView);
        mover_Animation();

    }

    public void logear(View v) {

        String usuario = etUsuario.getText().toString();
        String contraseña = etContraseña.getText().toString();

        prefe = getSharedPreferences("usuarios", Context.MODE_PRIVATE);
        GuardarCont = prefe.getString(usuario+"contra", "");
        GuardarUsu = prefe.getString(usuario+"nombre", "");

        if (etUsuario.getText().toString().isEmpty() || etContraseña.getText().toString().isEmpty()) {
            Toast.makeText(this,"Algún campo está vacio", Toast.LENGTH_LONG).show();
        } else {
            if (GuardarUsu.equals(usuario)) {
                if (GuardarCont.equals(contraseña)) {
                    Toast.makeText(this, "SESIÓN INICIADA", Toast.LENGTH_LONG).show();
                    siguiente(null);
                } else {
                    Toast.makeText(this,"CONTRASEÑA INCORRECTA", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this,getResources().getString(R.string.maLogearUsuM), Toast.LENGTH_LONG).show();
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

}