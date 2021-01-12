package com.elorrieta.euskomet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class RecuperarContra extends AppCompatActivity {

    EditText idUsuariotxt, ContraNueva, RepeContraNueva, PalabraClave;
    TextView TVcn,TVrcn;
    Button button8,button3;

    String clave = null;
    String usuario = null;
    String contraseña = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_contra);

        idUsuariotxt = (EditText) findViewById(R.id.usuariotext);
        PalabraClave = (EditText) findViewById(R.id.Clavetext);
        ContraNueva = (EditText) findViewById(R.id.idContraNuevatxt);
        RepeContraNueva = (EditText) findViewById(R.id.idRepeContratxt);

        TVcn = (TextView) findViewById(R.id.idContraNueva);
        TVrcn = (TextView) findViewById(R.id.idRepeContra);

        button3 = (Button) findViewById(R.id.button3);
        button8 = (Button) findViewById(R.id.button8);
    }


    public void recuperar(View view) throws NoSuchAlgorithmException {

        String Usuario = idUsuariotxt.getText().toString();
        String Clave = PalabraClave.getText().toString();
        String Contranueva1 = ContraNueva.getText().toString();
        String Repecontranueva = RepeContraNueva.getText().toString();


        MainActivity.prefe = getSharedPreferences("usuarios", Context.MODE_PRIVATE);
        clave = MainActivity.prefe.getString(Usuario+"Palabra","");
        usuario = MainActivity.prefe.getString(Usuario+"nombre","");
        contraseña = MainActivity.prefe.getString(Usuario+"contra","");


        if(Usuario.isEmpty()||Clave.isEmpty()){
            Toast.makeText(this, "Alguno de los campos están vacios", Toast.LENGTH_SHORT).show();
        }else {

            MessageDigest md = MessageDigest.getInstance("SHA");
            byte dataBytes[] = Clave.getBytes();
            md.update(dataBytes);

            byte array[] = md.digest();
            String texto2 = "";
            for (byte b : array) {
                texto2 += b;
            }

            if (Usuario.equals(usuario)) {
                if (texto2.equals(clave)) {

                    ContraNueva.setVisibility(View.VISIBLE);
                    RepeContraNueva.setVisibility(View.VISIBLE);
                    TVcn.setVisibility(View.VISIBLE);
                    TVrcn.setVisibility(View.VISIBLE);
                    button8.setVisibility(view.VISIBLE);
                    button3.setVisibility(view.INVISIBLE);


                }else{
                    Toast.makeText(this, "Usuarios no existe", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this, "Error palabra clave", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void volver(View view){
        finish();
        Intent volver = new Intent (this, MainActivity.class);
        startActivity(volver);
    }

    public void guardar(View view){
        finish();
        Intent guardar = new Intent (this, MainActivity.class);
        startActivity(guardar);
    }

    public void guardar2(View v) throws NoSuchAlgorithmException {
        String Usuario = idUsuariotxt.getText().toString();
        String Clave = PalabraClave.getText().toString();
        String Contranueva1 = ContraNueva.getText().toString();
        String Repecontranueva = RepeContraNueva.getText().toString();


        MainActivity.prefe = getSharedPreferences("usuarios", Context.MODE_PRIVATE);
        clave = MainActivity.prefe.getString(Usuario + "Palabra", "");
        usuario = MainActivity.prefe.getString(Usuario + "nombre", "");
        contraseña = MainActivity.prefe.getString(Usuario + "contra", "");


        if (Contranueva1.isEmpty() || Repecontranueva.isEmpty()) {
            Toast.makeText(this, "Campos vacios", Toast.LENGTH_SHORT).show();
        } else {
            if (Contranueva1.equals(Repecontranueva)) {
                MessageDigest md2 = MessageDigest.getInstance("SHA");
                byte dataBytes2[] = Contranueva1.getBytes();
                md2.update(dataBytes2);

                byte array2[] = md2.digest();
                String texto3 = "";
                for (byte b : array2) {
                    texto3 += b;
                }

                if(contraseña.equals(texto3)){
                    Toast.makeText(this, "No puede ser la contraseña antigua", Toast.LENGTH_SHORT).show();
                }else {

                    SharedPreferences.Editor editor = MainActivity.prefe.edit();
                    editor.putString(Usuario + "contra", texto3).commit();

                    Toast.makeText(this, "Contraseña cambiada", Toast.LENGTH_SHORT).show();

                    finish();
                    Intent guardar = new Intent(this, MainActivity.class);
                    startActivity(guardar);

                }
            } else {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            }
        }

    }
}