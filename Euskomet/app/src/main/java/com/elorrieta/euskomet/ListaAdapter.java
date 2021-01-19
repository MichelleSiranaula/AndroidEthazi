package com.elorrieta.euskomet;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;


public class ListaAdapter extends RecyclerView.Adapter<ListaAdapter.MiViewHolder> {
    private ArrayList<Municipio> datosMuni = new ArrayList<Municipio>();
    //private AdapterView.OnItemClickListener listener;
    private OnItemClickListener listener;

    /*public ListaAdapter(ArrayList<Municipio> datosMuniProvB, OnItemClickListener onItemClickListener) {
    }*/

    public class MiViewHolder extends RecyclerView.ViewHolder {
        public TextView nombreMuni;
        public TextView descripcionMuni;

        public MiViewHolder(View view) {
            super(view);
            nombreMuni = (TextView) view.findViewById(R.id.tvNombreMuni);
            descripcionMuni = (TextView) view.findViewById(R.id.tvDescripcionMuni);
        }
    }

    public ListaAdapter(ArrayList<Municipio> datosMuni, OnItemClickListener onItemClickListener) {
        this.datosMuni = datosMuni;
        this.listener = onItemClickListener;
    }

    @Override
    public void onBindViewHolder(MiViewHolder holder, int position) {
        Municipio m = datosMuni.get(position);
        holder.nombreMuni.setText((CharSequence) datosMuni.get(position).getNombre());
        holder.descripcionMuni.setText((CharSequence) datosMuni.get(position).getDescripcion());
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.linea,parent, false);
        return new MiViewHolder(v);
    }
}
