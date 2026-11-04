package org.asociacion;

import java.time.LocalDate;

public class Miembro {
    public String id;
    public String nombres;
    public String apellidos;
    public String cedula;
    public String telefono;
    public String direccion;
    public LocalDate fechaIngreso;
    public String estado;

    public Miembro(String id, String nombres, String apellidos, String cedula, String telefono, String direccion, LocalDate fechaIngreso, String estado) {
        this.id = id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.cedula = cedula;
        this.telefono = telefono;
        this.direccion = direccion;
        this.fechaIngreso = fechaIngreso;
        this.estado = estado;
    }
}