package bris.es.budolearning.domain;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class Disciplina extends GenericObject{

    String nombre;
    String descripcion;
    List<Grado> grados;

    public Disciplina(){}
    public Disciplina(JSONObject json, boolean completo) {
        try{setId(json.getInt("id"));}catch(Exception e){
            Log.e(this.getClass().getCanonicalName(), "Error crear " + this.getClass().getCanonicalName());
        }
        try{setDescripcion(json.getString("descripcion"));}catch(Exception e){
            Log.e(this.getClass().getCanonicalName(), "Error crear " + this.getClass().getCanonicalName());
        }
        try{setNombre(json.getString("nombre"));}catch(Exception e){
            Log.e(this.getClass().getCanonicalName(), "Error crear " + this.getClass().getCanonicalName());
        }
        try{setGrados(json.getJSONArray("grados"), completo);}catch(Exception e){
            Log.e(this.getClass().getCanonicalName(), "Error crear " + this.getClass().getCanonicalName());
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
    public List<Grado> getGrados() {
        return grados;
    }

    public void setGrados(JSONArray grados, boolean completo) {
        this.grados = new ArrayList<>();
        for(int i=0;i<grados.length();i++){
            try{
                this.grados.add(new Grado(grados.getJSONObject(i), completo));
            }catch(Exception e){
                Log.e(this.getClass().getCanonicalName(), "Error setGrados: " + grados.toString());
            }
        }
    }
    public Grado getGrado(int idGrado){
        for(Grado g: getGrados()){
            if(g.getId() == idGrado)
                return g;
        }
        return null;
    }
}


