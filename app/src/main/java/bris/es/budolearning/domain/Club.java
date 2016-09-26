package bris.es.budolearning.domain;

import org.json.JSONObject;

public class Club extends GenericObject {

    String nombre;
    String descripcion;
    String direccion;
    String localidad;
    String email;
    String telefono;
    String web;
    String profesor;

    public Club(){};

    public Club(JSONObject json) {
        try {            setId(json.getInt("id"));        } catch (Exception e) {        }
        try {            setNombre(json.getString("nombre"));        } catch (Exception e) {        }
        try {            setDescripcion(json.getString("descripcion"));        } catch (Exception e) {        }
        try {            setDireccion(json.getString("direccion"));        } catch (Exception e) {        }
        try {            setLocalidad(json.getString("localidad"));        } catch (Exception e) {        }
        try {            setEmail(json.getString("email"));        } catch (Exception e) {        }
        try {            setTelefono(json.getString("telefono"));        } catch (Exception e) {        }
        try {            setWeb(json.getString("web"));        } catch (Exception e) {        }
        try {            setWeb(json.getString("profesor"));        } catch (Exception e) {        }
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
    public String getLocalidad() {
        return localidad;
    }
    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getTelefono() {
        return telefono;
    }
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    public String getWeb() {
        return web;
    }
    public void setWeb(String web) {
        this.web = web;
    }
    public String getProfesor() {
        return profesor;
    }
    public void setProfesor(String profesor) {
        this.profesor = profesor;
    }
}
