package com.example.androidbar.model;

import java.io.Serializable;

public class LineaComandaId implements Serializable {

    private int numeroLinea;
    private int numeroComanda;

    public int getNumeroLinea() {
        return numeroLinea;
    }

    public void setNumeroLinea(int numeroLinea) {
        this.numeroLinea = numeroLinea;
    }

    public int getNumeroComanda() {
        return numeroComanda;
    }

    public void setNumeroComanda(int numeroComanda) {
        this.numeroComanda = numeroComanda;
    }
}
