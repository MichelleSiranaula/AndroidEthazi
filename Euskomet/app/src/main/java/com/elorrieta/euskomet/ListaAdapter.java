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

    public class MiViewHolder extends RecyclerView.ViewHolder {
        public TextView nombreMuni;

        public MiViewHolder(View view) {
            super(view);
            nombreMuni = (TextView) view.findViewById(R.id.tvNombre);
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
