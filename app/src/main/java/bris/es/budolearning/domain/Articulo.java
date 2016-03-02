package bris.es.budolearning.domain;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;

public class Articulo extends GenericObject {

    private Boolean activo;
    private Date fecha;
    private String titulo;
    private String autor;
    private Boolean visibleUsuarios;

    public Articulo(){}

    public Articulo(JSONObject json) {
        try {            setId(json.getInt("id"));        } catch (JSONException e) {Log.d(getClass().getCanonicalName(), "Id");        }
        try {            setActivo(json.getBoolean("activo"));        } catch (JSONException e) {Log.d(getClass().getCanonicalName(), "Activo");        }
        try {            setVisibleUsuarios(json.getBoolean("visibleUsuarios"));        } catch (JSONException e) {Log.d(getClass().getCanonicalName(), "visibleUsuarios");        }
        try {            setFecha(new Date(json.getLong("fecha")));        } catch (JSONException e) {Log.d(getClass().getCanonicalName(), "Fecha");        }
        try {            setTitulo(json.getString("titulo"));        } catch (JSONException e) {Log.d(getClass().getCanonicalName(), "Titulo");        }
        try {            setAutor(json.getString("autor"));        } catch (JSONException e) {  Log.d(getClass().getCanonicalName(), "Autor");      }
    }

    public Boolean getActivo() {
        return activo;
    }
    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
    public Date getFecha() {
        return fecha;
    }
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public String getAutor() {
        return autor;
    }
    public void setAutor(String autor) {
        this.autor = autor;
    }
    public Boolean getVisibleUsuarios() {
        return visibleUsuarios;
    }
    public void setVisibleUsuarios(Boolean visibleUsuarios) {
        this.visibleUsuarios = visibleUsuarios;
    }
}
