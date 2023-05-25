package com.example.androidbar.model;

public class LineaComandaDTO {

    public final LineaComanda lineaComanda;
    public final String nombreArticulo;

    public LineaComandaDTO(LineaComanda lineaComanda, String nombreArticulo) {
        this.lineaComanda = lineaComanda;
        this.nombreArticulo = nombreArticulo;
    }

    public String getNombreArticulo() {
        return nombreArticulo;
    }

    public int getCantidad(){
        return lineaComanda.getCantidad();
    }

    public float getPrecio(){
        return lineaComanda.getPrecio();
    }

    public LineaComanda getLineaComanda() {
        return lineaComanda;
    }
}