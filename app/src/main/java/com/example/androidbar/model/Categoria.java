package com.example.androidbar.model;

import androidx.annotation.NonNull;

public class Categoria {

    private int categoriaId;
    private String nombreCategoria;
    private String descripcionCategoria;

    public int getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(int categoriaId) {
        this.categoriaId = categoriaId;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public String getDescripcionCategoria() {
        return descripcionCategoria;
    }

    public void setDescripcionCategoria(String descripcionCategoria) {
        this.descripcionCategoria = descripcionCategoria;
    }

    @NonNull
    @Override
    public String toString() {
        return getNombreCategoria();
    }
}
