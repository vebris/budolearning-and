package bris.es.budolearning.domain;

import org.json.JSONObject;
import java.util.Date;

public class Estadistica extends GenericObject {

    private int idFichero;
    private String descFichero;
    private int visualizaciones;
    private int tamanoTotal;
    private Date fecha;

    public Estadistica(JSONObject json) {
        try {            setIdFichero(json.getInt("idFichero"));        } catch (Exception e) {        }
        try {            setDescFichero(json.getString("descFichero"));        } catch (Exception e) {        }
        try {            setVisualizaciones(json.getInt("totalNumero"));        } catch (Exception e) {        }
        try {            setTamanoTotal(json.getInt("totalTamano"));        } catch (Exception e) {        }
        try {            setFecha(json.getLong("ultimaVisualizacion"));        } catch (Exception e) {        }
    }

    public int getVisualizaciones() {        return visualizaciones;    }
    public void setVisualizaciones(int visualizaciones) {        this.visualizaciones = visualizaciones;    }
    public int getTamanoTotal() {
        return tamanoTotal;
    }
    public void setTamanoTotal(int tamanoTotal) {
        this.tamanoTotal = tamanoTotal;
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
    public int getIdFichero() {
        return idFichero;
    }
    public void setIdFichero(int idFichero) {
        this.idFichero = idFichero;
    }

    public String getDescFichero() {
        return descFichero;
    }

    public void setDescFichero(String descFichero) {
        this.descFichero = descFichero;
    }
}
