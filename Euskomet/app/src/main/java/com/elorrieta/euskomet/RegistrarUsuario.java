package com.elorrieta.euskomet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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


    public void crear(View View) throws NoSuchAlgorithmException, InterruptedException {

        String usuario = UsuarioNombre.getText().toString();
        String contraseñaNueva1 = ContraNueva.getText().toString();
        String contraseñaNueva2 = RepeContraNueva.getText().toString();
        String localidad = Localidad.getText().toString();
        String Clave = PalabraClave.getText().toString();

        MainActivity.prefe = getSharedPreferences("usuarios", Context.MODE_PRIVATE);
        String d= MainActivity.prefe.getString(usuario+"nombre", "");


        if (usuario.equals("") || contraseñaNueva1.equals("") || contraseñaNueva2.equals("") || Clave.equals("")) {
            Toast.makeText(this, "Algún campo está vacio", Toast.LENGTH_SHORT).show();
        } else {
            if(MainActivity.prefe.contains(d)){
                Toast.makeText(this, "El usuario "+usuario+" ya está registrado", Toast.LENGTH_SHORT).show();
            }else{
                if (contraseñaNueva1.equals(contraseñaNueva2)) {

                        MessageDigest md = MessageDigest.getInstance("SHA");
                        byte dataBytes[] = contraseñaNueva1.getBytes();
                        md.update(dataBytes);
                        byte array[] = md.digest();
                        String texto2 = "";
                        for (byte b : array) {
                         texto2 += b;
                        }

                        MessageDigest md2 = MessageDigest.getInstance("SHA");
                        byte dataBytes2[] = Clave.getBytes();
                        md2.update(dataBytes2);

                        byte array2[] = md2.digest();
                        String texto3 = "";
                        for (byte b : array2) {
                            texto3 += b;
                        }

                        usuarioInsert(usuario,texto2,texto3);

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

    private void usuarioInsert(String usuario,String texto2,String texto3) throws InterruptedException {
        ClientThread clientThread = new ClientThread("INSERT into usuario (nombre,contraseña, p_clave) values ('"+usuario+"','"+texto2+"','"+texto3+"')");
        Thread thread = new Thread(clientThread);
        thread.start();
        thread.join();
    }

    public void volver(View view){
        finish();
        Intent volver = new Intent (this, MainActivity.class);
        startActivity(volver);
    }
}