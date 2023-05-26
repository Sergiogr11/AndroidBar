package com.example.androidbar.model;

import java.io.Serializable;

public class LineaComanda implements Serializable {
    private LineaComandaId lineaComandaId;
    private int cantidad;
    private float precio;
    private int articuloId;

    public LineaComandaId getLineaComandaId() {
        return lineaComandaId;
    }

    public void setLineaComandaId(LineaComandaId lineaComandaId) {
        this.lineaComandaId = lineaComandaId;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public int getArticuloId() {
        return articuloId;
    }

    public void setArticuloId(int articuloId) {
        this.articuloId = articuloId;
    }
}
