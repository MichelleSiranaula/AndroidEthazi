package com.elorrieta.euskomet;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.BreakIterator;
import java.util.ArrayList;


public class ListaAdapter extends RecyclerView.Adapter<ListaAdapter.MiViewHolder> {
    private ArrayList<Municipio> datosMuni = new ArrayList<Municipio>();
    private OnItemClickListener listener;
    private String tipo;

    public class MiViewHolder extends RecyclerView.ViewHolder {
        public TextView nombreMuni;

        public MiViewHolder(View view) {
            super(view);
            if (tipo.equals("Mapita")) {
                nombreMuni = (TextView) view.findViewById(R.id.tvNombre);
            } else if (tipo.equals("Medalla")) {
                nombreMuni = (TextView) view.findViewById(R.id.nombreTop);
            }

        }
    }

    public ListaAdapter(ArrayList<Municipio> datosMuni, String tipo, OnItemClickListener onItemClickListener) {
        this.datosMuni = datosMuni;
        this.listener = onItemClickListener;
        this.tipo = tipo;
    }

    @Override
    public void onBindViewHolder(MiViewHolder holder, int position) {
        Municipio m = datosMuni.get(position);
        holder.nombreMuni.setText((CharSequence) datosMuni.get(position).getNombre());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(m);
            }
        });

    }
    @Override
    public int getItemCount() {
        return datosMuni.size();
    }

    @Override
    public MiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = null;
        if (tipo.equals("Mapita")) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.linea,parent, false);
        } else if (tipo.equals("Medalla")) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.linea2,parent, false);
        }

        return new MiViewHolder(v);
    }
}
