package bris.es.budolearning.domain;

import android.util.Log;
import org.json.JSONObject;
import java.util.Date;

public class Fichero extends GenericObject{

    private String descripcion;
    private String extension;
    private Date fecha;
    private int activo;
    private String tamano;
    private int coste;
    private int propio;
    private int segundos;

    public Fichero(){}
    public Fichero(JSONObject json) {
        try{setId(json.getInt("id"));}catch(Exception e){Log.d(this.getClass().getCanonicalName(), "Error crear " + this.getClass().getCanonicalName() + "\n id " + json.toString(), e);}
        try{setDescripcion(json.getString("descripcion"));}catch(Exception e){setDescripcion("");}
        try{setExtension(json.getString("extension"));}catch(Exception e){setExtension(".mp4");}
        try{if(json.get("fecha") != null) setFecha(new Date(json.getLong("fecha")));}catch(Exception e){Log.d(this.getClass().getCanonicalName(), "Error crear " + this.getClass().getCanonicalName() + "\n fecha " + json.toString(), e);}
        try{setActivo(json.getBoolean("activo")?1:0);}catch(Exception e){Log.d(this.getClass().getCanonicalName(), "Error crear " + this.getClass().getCanonicalName() + "\n activo " + json.toString(), e);}
        try{setTamano(json.getString("tamano"));}catch(Exception e){Log.d(this.getClass().getCanonicalName(), "Error crear " + this.getClass().getCanonicalName() + "\n tamano " + json.toString(), e);}
        try{setCoste(json.getInt("coste"));}catch(Exception e){Log.d(this.getClass().getCanonicalName(), "Error crear " + this.getClass().getCanonicalName() + "\n coste " + json.toString(), e);}
        try{setPropio(json.getBoolean("propio")?1:0);}catch(Exception e){Log.d(this.getClass().getCanonicalName(), "Error crear " + this.getClass().getCanonicalName() + "\n propio " + json.toString(), e);}
        try{setSegundos(json.getInt("segundos"));}catch(Exception e){Log.d(this.getClass().getCanonicalName(), "Error crear " + this.getClass().getCanonicalName() + "\n segundos " + json.toString(), e);}
    }


    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public String getExtension() { return extension; }
    public void setExtension(String extension) {
        this.extension = extension;
    }
    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    public String getTamano() {
        return tamano;
    }
    public void setTamano(String tamano) {
        this.tamano = tamano;
    }
    public int getCoste() {
        return coste;
    }
    public void setCoste(int coste) {
        this.coste = coste;
    }
    public int getPropio() {
        return propio;
    }
    public void setPropio(int propio) {
        this.propio = propio;
    }
    public int getActivo() { return activo; }
    public void setActivo(int activo) {
        this.activo = activo;
    }
    public int getSegundos() {
        return segundos;
    }
    public void setSegundos(int segundos) {
        this.segundos = segundos;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Fichero && id==((Fichero)o).getId();
    }
}
