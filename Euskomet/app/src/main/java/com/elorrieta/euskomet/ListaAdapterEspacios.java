package com.elorrieta.euskomet;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class ListaAdapterEspacios extends RecyclerView.Adapter<ListaAdapterEspacios.MiViewHolder> {
    private ArrayList<EspaciosNaturales> datosEspacios = new ArrayList<EspaciosNaturales>();
    private OnItemClickListenerE listener;
    private String tipo;

    public class MiViewHolder extends RecyclerView.ViewHolder {
        public TextView nombreEspacios;

        public MiViewHolder(View view) {
            super(view);
            if (tipo.equals("Mapita")) {
                nombreEspacios = (TextView) view.findViewById(R.id.tvNombre);
            } else if (tipo.equals("Medalla")) {
                nombreEspacios = (TextView) view.findViewById(R.id.nombreTop);
            }
        }
    }

    public ListaAdapterEspacios(ArrayList<EspaciosNaturales> datosEspacios, String tipo, OnItemClickListenerE onItemClickListener) {
        this.datosEspacios = datosEspacios;
        this.listener = onItemClickListener;
        this.tipo = tipo;
    }

    @Override
    public void onBindViewHolder(@NonNull ListaAdapterEspacios.MiViewHolder holder, int position) {
        EspaciosNaturales e = datosEspacios.get(position);
        holder.nombreEspacios.setText((CharSequence) datosEspacios.get(position).getNombre());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(e);
            }
        });
    }

    @Override
    public int getItemCount() {
        return datosEspacios.size();
    }

    @Override
    public MiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = null;
        if (tipo.equals("Mapita")) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.linea,parent, false);
        } else if (tipo.equals("Medalla")) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.linea2,parent, false);
        }
        return new ListaAdapterEspacios.MiViewHolder(v);
    }



}