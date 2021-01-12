package com.elorrieta.euskomet;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;


public class ListaAdapter extends RecyclerView.Adapter<ListaAdapter.MiViewHolder> {
    private List<Municipio> muniList;
    private DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    private AdapterView.OnItemClickListener listener;

    public class MiViewHolder extends RecyclerView.ViewHolder {
        public TextView nombreMuni;
        public TextView descripcionMuni;
        public ImageView imgenFav;

        public MiViewHolder(View view) {
            super(view);
            nombreMuni = (TextView) view.findViewById(R.id.tvNombreMuni);
            descripcionMuni = (TextView) view.findViewById(R.id.tvDescripcionMuni);
            imgenFav = (ImageView) view.findViewById(R.id.ivFavMuni);
        }
    }

    public ListaAdapter(List<Municipio> contactLists, AdapterView.OnItemClickListener listener) {
        this.muniList = contactLists;
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(MiViewHolder holder, int position) {
        final Municipio m = muniList.get(position);
        holder.nombreMuni.setText(m.getNombre());
        holder.descripcionMuni.setText(m.getDescripcion());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //listener.onItemClick(m);
            }
        });

    }
    @Override
    public int getItemCount() {
        return muniList.size();
    }

    @Override
    public MiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.linea,parent, false);
        return new MiViewHolder(v);
    }
}
