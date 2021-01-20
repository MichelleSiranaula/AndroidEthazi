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

    public class MiViewHolder extends RecyclerView.ViewHolder {
        public TextView nombreEspacios;
        public TextView descripcionEspacios;

        public MiViewHolder(View view) {
            super(view);
            nombreEspacios = (TextView) view.findViewById(R.id.tvNombre);
            descripcionEspacios = (TextView) view.findViewById(R.id.tvDescripcion);
        }
    }

    public ListaAdapterEspacios(ArrayList<EspaciosNaturales> datosEspacios, OnItemClickListenerE onItemClickListener) {
        this.datosEspacios = datosEspacios;
        this.listener = onItemClickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull ListaAdapterEspacios.MiViewHolder holder, int position) {
        EspaciosNaturales e = datosEspacios.get(position);
        holder.nombreEspacios.setText((CharSequence) datosEspacios.get(position).getNombre());
        holder.descripcionEspacios.setText((CharSequence) datosEspacios.get(position).getDescripcion());
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.linea,parent, false);
        return new ListaAdapterEspacios.MiViewHolder(v);
    }



}