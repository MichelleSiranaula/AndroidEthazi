package com.elorrieta.euskomet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Historico extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico);
    }

    public void volver(View view){
        finish();
        Intent volver = new Intent (this, Info.class);
        startActivity(volver);
    }
}