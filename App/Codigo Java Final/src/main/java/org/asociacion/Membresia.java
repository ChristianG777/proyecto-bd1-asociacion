package org.asociacion;

import java.time.LocalDate;

public class Membresia {
    public String idMembresia;
    public String idMiembro;
    public String tipo;
    public double costo;
    public LocalDate fechaInicio;
    public LocalDate fechaFin;
    public String estado;

    public Membresia(String idMembresia, String idMiembro, String tipo, double costo, LocalDate fechaInicio, LocalDate fechaFin, String estado) {
        this.idMembresia = idMembresia;
        this.idMiembro = idMiembro;
        this.tipo = tipo;
        this.costo = costo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = estado;
    }
}