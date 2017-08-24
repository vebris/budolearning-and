package bris.es.budolearning.domain;

import android.util.Log;

import org.json.JSONObject;

import java.util.Date;

public class Curso extends GenericObject {

    private String nombre;
    private String descripcion;
    private String direccion;
    private String profesor;
    private String precios;
    private Date inicio;
    private Date fin;

    public Curso(){}
    public Curso(JSONObject json) {
        try {setId(json.getInt("id"));                      } catch (Exception e) {Log.d(this.getClass().toString(),"Error parser id");}
        try {setNombre(json.getString("nombre"));           } catch (Exception e) {Log.d(this.getClass().toString(),"Error parser nombre");}
        try {setDescripcion(json.getString("descripcion")); } catch (Exception e) {Log.d(this.getClass().toString(),"Error parser descripcion");}
        try {setDireccion(json.getString("direccion"));     } catch (Exception e) {Log.d(this.getClass().toString(),"Error parser direccion");}
        try {setProfesor(json.getString("profesor"));       } catch (Exception e) {Log.d(this.getClass().toString(),"Error parser profesor");}
        try {setPrecios(json.getString("precios"));         } catch (Exception e) {Log.d(this.getClass().toString(),"Error parser precios");}
        try {setInicio(new Date(json.getLong("inicio")));   } catch (Exception e) {Log.d(this.getClass().toString(),"Error parser inicio");}
        try {setFin(new Date(json.getLong("fin")));         } catch (Exception e) {Log.d(this.getClass().toString(),"Error parser fin");}
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
