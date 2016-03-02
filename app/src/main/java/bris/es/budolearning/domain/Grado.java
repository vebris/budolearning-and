package bris.es.budolearning.domain;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class Grado extends GenericObject{

    private String nombre;
    private String descripcion;
    private List<Recurso> recursos;

    public Grado(){}
    public Grado(JSONObject json, boolean completo) {
        try{
            setId(json.getInt("id"));}catch(Exception e){
            Log.e(getClass().getCanonicalName(), "setId " + this.getClass().getCanonicalName());
        }
        try{setNombre(json.getString("nombre"));}catch(Exception e){
            Log.e(getClass().getCanonicalName(), "setNombre " + this.getClass().getCanonicalName());
        }
        try{setDescripcion(json.getString("descripcion"));}catch(Exception e){
            Log.e(getClass().getCanonicalName(), "setDescripcion " + this.getClass().getCanonicalName());
        }
        try{
            if(completo) setRecursos(json.getJSONArray("recursos"));
        }catch(Exception e){
            Log.e(getClass().getCanonicalName(), "setRecursos " + this.getClass().getCanonicalName());
        }
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
    public List<Recurso> getRecursos() {
        return recursos;
    }
    public Recurso getRecursos(int idRecurso){
        for(Recurso r: getRecursos()){
            if(r.getId() == idRecurso)
                return r;
        }
        return null;
    }
    public void setRecursos(JSONArray recursos) {
        this.recursos = new ArrayList<>();
        for(int i=0;i<recursos.length();i++){
            try{
                this.recursos.add(new Recurso(recursos.getJSONObject(i)));
            }catch(Exception e){
                Log.e(getClass().getCanonicalName(), "ERROR setRecursos: " + recursos.toString());
            }
        }
    }
    public Recurso getRecurso(int idRecurso){
        for(Recurso g: getRecursos()){
            if(g.getId() == idRecurso)
                return g;
        }
        return null;
    }
}


