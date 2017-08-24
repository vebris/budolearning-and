package bris.es.budolearning.domain;

import android.util.Log;

import org.json.JSONObject;

public class Club extends GenericObject {

    private String nombre;
    private String descripcion;
    private String direccion;
    private String localidad;
    private String email;
    private String telefono;
    private String web;
    private String profesor;
    private String gradoProfesor;

    public Club(){}

    public Club(JSONObject json) {
        try {setId(json.getInt("id"));                          } catch (Exception e) {
            Log.d(this.getClass().toString(),"Error parser id");}
        try {setNombre(json.getString("nombre"));               } catch (Exception e) {Log.d(this.getClass().toString(),"Error parser nombre");}
        try {setDescripcion(json.getString("descripcion"));     } catch (Exception e) {Log.d(this.getClass().toString(),"Error parser descripcion");}
        try {setDireccion(json.getString("direccion"));         } catch (Exception e) {Log.d(this.getClass().toString(),"Error parser direccion");}
        try {setLocalidad(json.getString("localidad"));         } catch (Exception e) {Log.d(this.getClass().toString(),"Error parser localidad");}
        try {setEmail(json.getString("email"));                 } catch (Exception e) {Log.d(this.getClass().toString(),"Error parser email");}
        try {setTelefono(json.getString("telefono"));           } catch (Exception e) {Log.d(this.getClass().toString(),"Error parser telefono");}
        try {setWeb(json.getString("web"));                     } catch (Exception e) {Log.d(this.getClass().toString(),"Error parser web");}
        try {setProfesor(json.getString("profesor"));           } catch (Exception e) {Log.d(this.getClass().toString(),"Error parser profesor");}
        try {setGradoProfesor(json.getString("gradoProfesor")); } catch (Exception e) {Log.d(this.getClass().toString(),"Error parser gradoProfesor");}
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
    public String getGradoProfesor() {
        return gradoProfesor;
    }
    public void setGradoProfesor(String gradoProfesor) {this.gradoProfesor = gradoProfesor;}
}
