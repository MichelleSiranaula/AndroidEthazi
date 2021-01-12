package com.elorrieta.euskomet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class RecuperarContra extends AppCompatActivity {

    EditText UsuarioNombre, ContraNueva, RepeContraNueva, PalabraClave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_contra);

        UsuarioNombre = (EditText) findViewById(R.id.idUsuarioTxt);
        PalabraClave = (EditText) findViewById(R.id.idPalaClavetxt);
    }


    public void recuperar(View view){
        String usuario = UsuarioNombre.getText().toString();
        String palabra = PalabraClave.getText().toString();


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