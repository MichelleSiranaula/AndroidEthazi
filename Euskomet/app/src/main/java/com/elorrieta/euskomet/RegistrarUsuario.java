package com.elorrieta.euskomet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegistrarUsuario extends AppCompatActivity {

    EditText UsuarioNombre, ContraNueva, RepeContraNueva, Localidad, PalabraClave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_usuario);
        UsuarioNombre = (EditText) findViewById(R.id.idUsuarioTxt);
        ContraNueva = (EditText) findViewById(R.id.idContratxt);
        RepeContraNueva = (EditText) findViewById(R.id.idRepetirContraText);
        Localidad = (EditText) findViewById(R.id.idLocalidadText);
        PalabraClave = (EditText) findViewById(R.id.idPClaveTxt);
    }


    public void crear(View View) {

        String usuario = UsuarioNombre.getText().toString();
        String contraseñaNueva1 = ContraNueva.getText().toString();
        String contraseñaNueva2 = RepeContraNueva.getText().toString();
        String localidad = Localidad.getText().toString();
        String Clave = PalabraClave.getText().toString();

        if ( contraseñaNueva1.equals(contraseñaNueva2) ) {
            MainActivity.prefe = getSharedPreferences("usuarios", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = MainActivity.prefe.edit();
            SharedPreferences.Editor editor2 = MainActivity.prefe.edit();
            editor.putString(usuario, contraseñaNueva1).commit();

            Toast.makeText(this, "Usuario registrado", Toast.LENGTH_LONG).show();
            finish();
            Intent volver = new Intent (this, MainActivity.class);
            startActivity(volver);
        }

    }

    public void volver(View view){
        finish();
        Intent volver = new Intent (this, MainActivity.class);
        startActivity(volver);
    }
}