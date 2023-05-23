package com.example.androidbar.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Comanda implements Serializable {

    private int numeroComanda;
    private float precioTotal;
    private long fechaHoraApertura;
    private long fechaHoraCierre;
    private int numeroComensales;
    private int usuarioId;
    private int mesaId;

    public int getNumeroComanda() {
        return numeroComanda;
    }

    public void setNumeroComanda(int numeroComanda) {
        this.numeroComanda = numeroComanda;
    }

    public float getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(float precioTotal) {
        this.precioTotal = precioTotal;
    }

    public long getFechaHoraApertura() {
        return fechaHoraApertura;
    }

    public void setFechaHoraApertura(long fechaHoraApertura) {
        this.fechaHoraApertura = fechaHoraApertura;
    }

    public long getFechaHoraCierre() {
        return fechaHoraCierre;
    }

    public void setFechaHoraCierre(long fechaHoraCierre) {
        this.fechaHoraCierre = fechaHoraCierre;
    }

    public int getNumeroComensales() {
        return numeroComensales;
    }

    public void setNumeroComensales(int numeroComensales) {
        this.numeroComensales = numeroComensales;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public int getMesaId() {
        return mesaId;
    }

    public void setMesaId(int mesaId) {
        this.mesaId = mesaId;
    }
}
