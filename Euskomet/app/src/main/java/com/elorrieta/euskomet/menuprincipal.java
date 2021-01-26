package com.elorrieta.euskomet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class menuprincipal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menuprincipal);
        getSupportActionBar().show();

    }

    public void siguienteMuni(View view){
        finish();
        Intent siguiente = new Intent (this, Lista.class);
        startActivity(siguiente);
    }

    public void siguienteEspacios(View view){
        finish();
        Intent siguiente = new Intent (this, ListaEspacios.class);
        startActivity(siguiente);
    }
    
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menusen, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.casa) {
            Intent volver = new Intent (this, MainActivity.class);
            startActivity(volver);
            return true;
        }
        if (id == R.id.info) {
            Toast.makeText(this, "Somos el DreamTeam real.", Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}