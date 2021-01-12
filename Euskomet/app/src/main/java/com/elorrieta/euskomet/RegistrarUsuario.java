package com.elorrieta.euskomet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.security.NoSuchAlgorithmException;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class RegistrarUsuario extends AppCompatActivity {

    EditText UsuarioNombre, ContraNueva, RepeContraNueva, Localidad, PalabraClave;
    String claveUsuario = "holatiocomoestas";
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


    public void crear(View View) throws NoSuchAlgorithmException {

        String usuario = UsuarioNombre.getText().toString();
        String contraseñaNueva1 = ContraNueva.getText().toString();
        String contraseñaNueva2 = RepeContraNueva.getText().toString();
        String localidad = Localidad.getText().toString();
        String Clave = PalabraClave.getText().toString();

        MainActivity.prefe = getSharedPreferences("usuarios", Context.MODE_PRIVATE);
        String d= MainActivity.prefe.getString(usuario, "");


        if (usuario.equals("") || contraseñaNueva1.equals("") || contraseñaNueva2.equals("")) {
            Toast.makeText(this, "Algún campo está vacio", Toast.LENGTH_SHORT).show();
        } else {
            if(MainActivity.prefe.contains(usuario)){
                Toast.makeText(this, "El usuario "+usuario+" ya está registrado", Toast.LENGTH_SHORT).show();
            }else{
                if (contraseñaNueva1.equals(contraseñaNueva2)) {

                        MainActivity.prefe = getSharedPreferences("usuarios", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = MainActivity.prefe.edit();


                        editor.putString(usuario+"nombre",usuario).commit();
                        editor.putString(usuario+"contra", contraseñaNueva1).commit();
                        editor.putString(usuario+"Palabra",Clave).commit();


                        Toast.makeText(this, "Usuario registrado", Toast.LENGTH_LONG).show();
                        finish();
                        Intent volver = new Intent(this, MainActivity.class);
                        startActivity(volver);
                    }else {
                    Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();

                }
            }
        }
    }



    public void volver(View view){
        finish();
        Intent volver = new Intent (this, MainActivity.class);
        startActivity(volver);
    }
}