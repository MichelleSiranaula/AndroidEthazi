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
import java.util.ArrayList;

public class RegistrarUsuario extends AppCompatActivity {

    EditText UsuarioNombre, ContraNueva, RepeContraNueva, Localidad, PalabraClave;
    String claveUsuario = "holatiocomoestas";
    ArrayList<Usuarios> usuarioarr = new ArrayList<Usuarios>();
    ArrayList<String> nombrearr = new ArrayList<String>();

    boolean encontrado = false;
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
        String Clave = PalabraClave.getText().toString();

        if (usuario.equals("") || contraseñaNueva1.equals("") || contraseñaNueva2.equals("") || Clave.equals("")) {
            Toast.makeText(this, getResources().getString(R.string.campo_vacio), Toast.LENGTH_SHORT).show();
        } else {
            ArrayList<Object> arrObject = new ArrayList<Object>();
            arrObject = usuarnombre();
            for (int i = 0; i < arrObject.size(); i++) {
                usuarioarr.add((Usuarios) arrObject.get(i));
            }


            for (int i = 0; i < usuarioarr.size(); i++) {

                nombrearr.add( usuarioarr.get(i).getNombre().toString());

                if (usuario.equals(nombrearr.get(i))) {
                    Toast.makeText(this, getResources().getString(R.string.el_usuario) +" "+ usuario +" "+ getResources().getString(R.string.ya_registrado), Toast.LENGTH_SHORT).show();
                    encontrado = true;
                }
            }

            if (encontrado==false) {

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

                    usuarioInsert(usuario, texto2, texto3);

                    Toast.makeText(this, getResources().getString(R.string.usu_registrado), Toast.LENGTH_LONG).show();
                    finish();
                    Intent volver = new Intent(this, MainActivity.class);
                    startActivity(volver);
                } else {
                    Toast.makeText(this, getResources().getString(R.string.contra_no_coincide), Toast.LENGTH_SHORT).show();

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

    private ArrayList<Object> usuarnombre() throws InterruptedException {
        ClientThreadSelect clientThreadSelect = new ClientThreadSelect("SELECT * FROM usuario", "usuarios");
        Thread thread = new Thread(clientThreadSelect);
        thread.start();
        thread.join();
        return clientThreadSelect.getDatos();
    }
}