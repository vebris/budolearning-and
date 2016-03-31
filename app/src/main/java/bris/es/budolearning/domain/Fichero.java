package bris.es.budolearning.domain;

import android.util.Log;

import org.json.JSONObject;

import java.util.Date;

public class Fichero extends GenericObject{

    String descripcion;
    String nombreFichero;
    String extension;
    Date fecha;
    Date fechaModificacion;
    boolean activo;
    String tamano;
    long visitas;
    int coste;

    public Fichero(){}
    public Fichero(JSONObject json) {
        try{
            setId(json.getInt("id"));
        }catch(Exception e){
            Log.d(this.getClass().getCanonicalName(), "Error crear " + this.getClass().getCanonicalName() + "\n id " + json.toString(), e);
        }
        try{
            setDescripcion(json.getString("descripcion"));
        }catch(Exception e){
            setDescripcion("");
        }
        try{
            setNombreFichero(json.getString("nombreFichero"));
        }catch(Exception e){
            setNombreFichero("");
        }
        try{
            setExtension(json.getString("extension"));
        }catch(Exception e){
            setExtension(".mp4");
        }
        try{
            if(json.get("fecha") != null)
                setFecha(new Date(json.getLong("fecha")));
        }catch(Exception e){
            Log.d(this.getClass().getCanonicalName(), "Error crear " + this.getClass().getCanonicalName() + "\n fecha " + json.toString(), e);
        }
        try{
            if(json.get("fechaModificacion") != null && !json.get("fechaModificacion").toString().equals("null")) {
                setFechaModificacion(new Date(json.getLong("fechaModificacion")));
            }
        }catch(Exception e){
            Log.d(this.getClass().getCanonicalName(), "Error crear " + this.getClass().getCanonicalName() + "\n fechaModificacion " + json.toString(), e);
        }
        try{
            setActivo(json.getBoolean("activo"));
        }catch(Exception e){
            Log.d(this.getClass().getCanonicalName(), "Error crear " + this.getClass().getCanonicalName() + "\n activo " + json.toString(), e);
        }
        try{
            setTamano(json.getString("tamano"));
        }catch(Exception e){
            Log.d(this.getClass().getCanonicalName(), "Error crear " + this.getClass().getCanonicalName() + "\n tamano " + json.toString(), e);
        }
        try{
            setVisitas(json.getLong("visitas"));
        }catch(Exception e){
            Log.d(this.getClass().getCanonicalName(), "Error crear " + this.getClass().getCanonicalName() + "\n visitas " + json.toString(), e);
        }
        try{
            setCoste(json.getInt("coste"));
        }catch(Exception e){
            Log.d(this.getClass().getCanonicalName(), "Error crear " + this.getClass().getCanonicalName() + "\n coste " + json.toString(), e);
        }
    }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public void setNombreFichero(String nombreFichero) {
        this.nombreFichero = nombreFichero;
    }
    public String getExtension() { return extension; }
    public void setExtension(String extension) {
        this.extension = extension;
    }
    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    public void setFechaModificacion(Date fechaModificacion) { this.fechaModificacion = fechaModificacion; }
    public String getTamano() {
        return tamano;
    }
    public void setTamano(String tamano) {
        this.tamano = tamano;
    }
    public long getVisitas() {
        return visitas;
    }
    public void setVisitas(long visitas) {
        this.visitas = visitas;
    }
    public int getCoste() {
        return coste;
    }
    public void setCoste(int coste) {
        this.coste = coste;
    }


    @Override
    public boolean equals(Object o) {
        return o instanceof Fichero && id==((Fichero)o).getId();
    }
}
