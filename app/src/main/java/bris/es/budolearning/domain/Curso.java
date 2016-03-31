package bris.es.budolearning.domain;

import org.json.JSONObject;

import java.util.Date;

public class Curso extends GenericObject {

    String nombre;
    String descripcion;
    String direccion;
    String profesor;
    String precios;
    Date inicio;
    Date fin;

    public Curso(){}
    public Curso(JSONObject json) {
        try {            setId(json.getInt("id"));        } catch (Exception e) {        }
        try {            setNombre(json.getString("nombre"));        } catch (Exception e) {        }
        try {            setDescripcion(json.getString("descripcion"));        } catch (Exception e) {        }
        try {            setDireccion(json.getString("direccion"));        } catch (Exception e) {        }
        try {            setProfesor(json.getString("profesor"));        } catch (Exception e) {        }
        try {            setPrecios(json.getString("precios"));        } catch (Exception e) {        }
        try {            setInicio(new Date(json.getLong("inicio")));        } catch (Exception e) {        }
        try {            setFin(new Date(json.getLong("fin")));        } catch (Exception e) {        }
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
    public String getDireccion() {
        return direccion;
    }
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    public Date getFin() {
        return fin;
    }
    public void setFin(Date fin) {
        this.fin = fin;
    }
    public Date getInicio() {
        return inicio;
    }
    public void setInicio(Date inicio) {
        this.inicio = inicio;
    }
    public String getProfesor() {
        return profesor;
    }
    public void setProfesor(String profesor) {
        this.profesor = profesor;
    }
    public String getPrecios() {        return precios;}
    public void setPrecios(String precios) { this.precios = precios; }
}
