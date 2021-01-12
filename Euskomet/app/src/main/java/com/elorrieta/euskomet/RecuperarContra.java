package com.elorrieta.euskomet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RecuperarContra extends AppCompatActivity {

    EditText idUsuariotxt, ContraNueva, RepeContraNueva, PalabraClave;
    TextView TVcn,TVrcn;

    String respuesta = null;
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

    }


    public void recuperar(View view){

        String Usuario = idUsuariotxt.getText().toString();
        String Clave = PalabraClave.getText().toString();
        String Contranueva1 = ContraNueva.getText().toString();
        String Repecontranueva = RepeContraNueva.getText().toString();


        MainActivity.prefe = getSharedPreferences("usuarios", Context.MODE_PRIVATE);
        respuesta = MainActivity.prefe.getString(Usuario+"Palabra","");
        usuario = MainActivity.prefe.getString(Usuario+"nombre","");
        contraseña = MainActivity.prefe.getString(Usuario+"contra","");


        if(Usuario.isEmpty()||Clave.isEmpty()){
            Toast.makeText(this, "Alguno de los campos están vacios", Toast.LENGTH_SHORT).show();
        }else{

            if(Usuario.equals(usuario)) {
                if (Clave.equals(respuesta)) {

                    Toast.makeText(this, "El usuario " + Usuario + "tiene como contraseña"+contraseña, Toast.LENGTH_SHORT).show();
                    ContraNueva.setVisibility(View.VISIBLE);
                    RepeContraNueva.setVisibility(View.VISIBLE);
                    TVcn.setVisibility(View.VISIBLE);
                    TVrcn.setVisibility(View.VISIBLE);



                } else {
                    Toast.makeText(this, "Falla clave", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this, "Falla usu", Toast.LENGTH_SHORT).show();
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

}