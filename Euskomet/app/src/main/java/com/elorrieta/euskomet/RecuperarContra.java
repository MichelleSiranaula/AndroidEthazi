package com.elorrieta.euskomet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class RecuperarContra extends AppCompatActivity {

    EditText idUsuariotxt, ContraNueva, RepeContraNueva, PalabraClave;
    TextView TVcn,TVrcn;
    Button button8,button3;
    String nombrearr,contrarr,parr,texto3,Usuario;

    String clave = null;
    String usuario = null;
    String contraseña = null;

    ArrayList<Usuarios> usuarioarr = new ArrayList<Usuarios>();

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


    public void recuperar(View view) throws NoSuchAlgorithmException, InterruptedException {

        Usuario = idUsuariotxt.getText().toString();
        String Clave = PalabraClave.getText().toString();
        String Contranueva1 = ContraNueva.getText().toString();
        String Repecontranueva = RepeContraNueva.getText().toString();


        if (Usuario.isEmpty() || Clave.isEmpty()) {
            Toast.makeText(this, "Alguno de los campos están vacios", Toast.LENGTH_SHORT).show();
        } else {

            ArrayList<Object> arrObject = new ArrayList<Object>();
            arrObject = usuar();
            for (int i = 0; i < arrObject.size(); i++) {
                usuarioarr.add((Usuarios) arrObject.get(i));
            }

            for (int i = 0; i < usuarioarr.size(); i++) {

                nombrearr = usuarioarr.get(i).getNombre().toString();
                parr = usuarioarr.get(i).getP_clave().toString();

                MessageDigest md = MessageDigest.getInstance("SHA");
                byte dataBytes[] = Clave.getBytes();
                md.update(dataBytes);

                byte array[] = md.digest();
                String texto2 = "";
                for (byte b : array) {
                    texto2 += b;
                }

                if (Usuario.equals(nombrearr)) {
                    if (texto2.equals(parr)) {

                        ContraNueva.setVisibility(View.VISIBLE);
                        RepeContraNueva.setVisibility(View.VISIBLE);
                        TVcn.setVisibility(View.VISIBLE);
                        TVrcn.setVisibility(View.VISIBLE);
                        button8.setVisibility(view.VISIBLE);
                        button3.setVisibility(view.INVISIBLE);
                        break;

                    } else {
                        Toast.makeText(this, "Palabra clave no coincide", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            if(!Usuario.equals(nombrearr)){
                Toast.makeText(this, "Usuario no existe", Toast.LENGTH_SHORT).show();
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

private ArrayList<Object> usuar() throws InterruptedException {
        ClientThread clientThread = new ClientThread("SELECT * FROM usuario", "usuarios");
        Thread thread = new Thread(clientThread);
        thread.start();
        thread.join();
        return clientThread.getDatos();
        }



    public void guardar2(View v) throws NoSuchAlgorithmException, InterruptedException {

        String Usuario = idUsuariotxt.getText().toString();
        String Clave = PalabraClave.getText().toString();
        String Contranueva1 = ContraNueva.getText().toString();
        String Repecontranueva = RepeContraNueva.getText().toString();


        if (Contranueva1.isEmpty() || Repecontranueva.isEmpty()) {
            Toast.makeText(this, "Campos vacios", Toast.LENGTH_SHORT).show();
        } else {

            ArrayList<Object> arrObject = new ArrayList<Object>();
            arrObject = usuar();
            for (int i = 0; i < arrObject.size(); i++) {
                usuarioarr.add((Usuarios) arrObject.get(i));
            }

            for (int i = 0; i < usuarioarr.size(); i++) {


                nombrearr = usuarioarr.get(i).getNombre().toString();
                contrarr = usuarioarr.get(i).getContraseña().toString();

                if (Usuario.equals(nombrearr)) {
                    if (Contranueva1.equals(Repecontranueva)) {

                        MessageDigest md2 = MessageDigest.getInstance("SHA");
                        byte dataBytes2[] = Contranueva1.getBytes();
                        md2.update(dataBytes2);

                        byte array2[] = md2.digest();
                        texto3 = "";
                        for (byte b : array2) {
                            texto3 += b;
                        }

                        if (texto3.equals(contrarr)) {
                            Toast.makeText(this, "No puede ser la contraseña antigua", Toast.LENGTH_SHORT).show();
                        } else {

                            usuarcamcontr();

                            Toast.makeText(this, "Contraseña cambiada", Toast.LENGTH_SHORT).show();

                            finish();
                            Intent guardar = new Intent(this, MainActivity.class);
                            startActivity(guardar);

//                            break;

                        }
                    } else {
                        Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }

    }

    private ArrayList<Object> usuarcamcontr() throws InterruptedException {
        ClientThreadInsert clientThreadInsert = new ClientThreadInsert("UPDATE usuario set contraseña='" + texto3 + "' where nombre='"+Usuario+"'");
        Thread thread = new Thread(clientThreadInsert);
        thread.start();
        thread.join();
        return clientThreadInsert.getDatos();
    }
}