package com.elorrieta.euskomet;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class DialogoFav extends DialogFragment {

    AlertDialog.Builder Builder;
    RespuestaDialogo respusta;
    boolean elegido = false;

    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Builder = new AlertDialog.Builder(getActivity());
        Builder.setTitle("Alerta");
        Builder.setMessage("Quieres añadir a favoritos?");

        Builder.setPositiveButton("Bai", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                elegido = true;
            }
        });


        Builder.setNegativeButton("Ez", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                elegido = false;
            }
        });

        return Builder.create();
    }

    public void onAttach(Context contecto) {
        super.onAttach(contecto);
        respusta = (RespuestaDialogo) contecto;
    }
}
