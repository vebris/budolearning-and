package bris.es.budolearning.domain;

import org.json.JSONObject;

import java.util.Date;

public class VideoEspecial extends GenericObject{


    Fichero fichero;
    Club club;
    Usuario usuario;
    Date inicio;
    Date fin;


    public VideoEspecial(){}
    public VideoEspecial(JSONObject json) {
        try {setId(json.getInt("id"));        } catch (Exception e) {        }
        try {setInicio(new Date(json.getLong("inicio")));        } catch (Exception e) {        }
        try {setFin(new Date(json.getLong("fin")));        } catch (Exception e) {        }
        try {setFichero(new Fichero(json.getJSONObject("fichero")));        } catch (Exception e) {        }
        try {setClub(new Club(json.getJSONObject("club")));        } catch (Exception e) {        }
        try {setUsuario(new Usuario(json.getJSONObject("usuario"), false));        } catch (Exception e) {        }
    }

    public Fichero getFichero() {
        return fichero;
    }
    public void setFichero(Fichero fichero) {
        this.fichero = fichero;
    }
    public Club getClub() {
        return club;
    }
    public void setClub(Club club) {
        this.club = club;
    }
    public Usuario getUsuario() {
        return usuario;
    }
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    public Date getInicio() {
        return inicio;
    }
    public void setInicio(Date inicio) {
        this.inicio = inicio;
    }
    public Date getFin() {
        return fin;
    }
    public void setFin(Date fin) {
        this.fin = fin;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof VideoEspecial)
            return id==((VideoEspecial)o).getId();
        return false;
    }
}
