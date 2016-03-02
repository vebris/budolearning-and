package bris.es.budolearning.domain;

import org.json.JSONObject;

import java.util.Date;

public class Puntos extends GenericObject {

    private String tipo;
    private int idUsuario;
    private Date fecha;
    private int puntos;
    private int idFichero;
    private String fichero;

    public Puntos(JSONObject json) {
        try {            setId(json.getInt("id"));        } catch (Exception e) {        }
        try {            setIdUsuario(json.getInt("idUsuario"));        } catch (Exception e) {        }
        try {            setIdFichero(json.getInt("idFichero"));        } catch (Exception e) {        }
        try {            setTipo(json.getString("tipo"));        } catch (Exception e) {        }
        try {            setPuntos(json.getInt("puntos"));        } catch (Exception e) {        }
        try {            setFecha(json.getLong("fecha"));        } catch (Exception e) {        }
        try {            setFichero(json.getString("fichero"));        } catch (Exception e) {        }
    }

    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public int getIdUsuario() {
        return idUsuario;
    }
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
    public Date getFecha() {
        return fecha;
    }
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    public void setFecha(Long fecha) {
        this.fecha = new Date(fecha);
    }
    public int getPuntos() {
        return puntos;
    }
    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }
    public int getIdFichero() {
        return idFichero;
    }
    public void setIdFichero(int idFichero) {
        this.idFichero = idFichero;
    }
    public String getFichero() {
        return fichero;
    }
    public void setFichero(String fichero) {
        this.fichero = fichero;
    }
}
