package bris.es.budolearning.domain;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Recurso extends GenericObject{

    private String nombre;
    private Tipo tipo;
    private boolean enPrograma;
    private int numFicheros;
    private List<Fichero> ficheros;

    public Recurso(){}
    public Recurso(JSONObject json) {
        try {
            setId(json.getInt("id"));
        } catch (Exception e) {
            Log.e(this.getClass().getCanonicalName(), "Error crear " + this.getClass().getCanonicalName());
        }
        try {
            setNombre(json.getString("nombre"));
        } catch (Exception e) {
            Log.e(this.getClass().getCanonicalName(), "Error crear " + this.getClass().getCanonicalName());
        }
        try {
            setEnPrograma(json.getBoolean("enPrograma"));
        } catch (Exception e) {
            Log.e(this.getClass().getCanonicalName(), "Error crear " + this.getClass().getCanonicalName());
        }
        try {
            Tipo tipo = new Tipo();
            JSONObject jsonTipo = json.getJSONObject("tipo");
            tipo.setId(jsonTipo.getInt("id"));
            tipo.setNombre(jsonTipo.getString("nombre"));
            tipo.setDescripcion(jsonTipo.getString("descripcion"));
            setTipo(tipo);
        } catch (Exception e) {
            Log.e(this.getClass().getCanonicalName(), "Error crear " + this.getClass().getCanonicalName());
        }
        try {
            setNumFicheros(json.getInt("numFicheros"));
        } catch (Exception e) {
            Log.e(this.getClass().getCanonicalName(), "Error crear " + this.getClass().getCanonicalName());
        }
        try {
            setFicheros(json.getJSONArray("ficheros"));
        } catch (Exception e) {
            Log.e(this.getClass().getCanonicalName(), "Error crear " + this.getClass().getCanonicalName());
        }
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public Tipo getTipo() {
        return tipo;
    }
    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }
    public boolean isEnPrograma() {
        return enPrograma;
    }
    public void setEnPrograma(boolean enPrograma) {
        this.enPrograma = enPrograma;
    }
    public int getNumFicheros() {
        return numFicheros;
    }
    public void setNumFicheros(int numFicheros) {
        this.numFicheros = numFicheros;
    }
    public List<Fichero> getFicheros() {
        return ficheros;
    }
    public void setFicheros(JSONArray ficheros) {
        this.ficheros = new ArrayList<>();
        for(int i=0;i<ficheros.length();i++){
            try{
                this.ficheros.add(new Fichero(ficheros.getJSONObject(i)));
            }catch(Exception e){
                Log.e(this.getClass().getCanonicalName(), "Error setFicheros " + ficheros.toString());
            }
        }
        Collections.sort(this.ficheros, new Comparator<Fichero>() {
            @Override
            public int compare(Fichero lhs, Fichero rhs) {
                return rhs.getFecha().compareTo(lhs.getFecha());
            }
        });
    }
}


