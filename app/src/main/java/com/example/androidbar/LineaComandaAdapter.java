package com.example.androidbar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.androidbar.model.LineaComandaDTO;

import java.util.List;


public class LineaComandaAdapter extends RecyclerView.Adapter<LineaComandaAdapter.ViewHolder> {

    private List<LineaComandaDTO> lineasComanda;
    private OnItemClickListener listener;

    public LineaComandaAdapter(List<LineaComandaDTO> lineasComanda, OnItemClickListener listener) {
        this.lineasComanda = lineasComanda;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LineaComandaDTO lineaComanda = lineasComanda.get(position);
        holder.tvNombreArticulo.setText(lineaComanda.getNombreArticulo());
        holder.tvCantidad.setText(String.valueOf(lineaComanda.getCantidad()));
        holder.tvPrecio.setText(String.format("%.2f €", lineaComanda.getPrecio()));
    }

    @Override
    public int getItemCount() {
        return lineasComanda.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombreArticulo;
        TextView tvCantidad;
        TextView tvPrecio;
        Button btnEliminar;

        public ViewHolder(View itemView, OnItemClickListener listener) {
            super(itemView);
            tvNombreArticulo = itemView.findViewById(R.id.tvNombreArticulo);
            tvCantidad = itemView.findViewById(R.id.tvCantidad);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);

            itemView.setOnClickListener(v -> listener.onItemClick(getAdapterPosition()));
            btnEliminar.setOnClickListener(v -> listener.onDeleteClick(getAdapterPosition()));
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick(int position);
    }
}