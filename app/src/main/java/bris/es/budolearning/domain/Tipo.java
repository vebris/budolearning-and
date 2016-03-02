package bris.es.budolearning.domain;

import java.io.Serializable;

public class Tipo implements Serializable {
    int id;
    String nombre;
    String descripcion;

    public Tipo(){}
    public Tipo(String nombre){this.nombre = nombre;}
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
