package bris.es.budolearning.domain;

import java.io.Serializable;

public class UsuarioDisciplinaGrado implements Serializable{

    private int idDisciplina;
    private String descDisciplina;
    private int idGrado;
    private String descGrado;

    public int getIdDisciplina() {
        return idDisciplina;
    }

    public void setIdDisciplina(int idDisciplina) {
        this.idDisciplina = idDisciplina;
    }

    public String getDescDisciplina() {
        return descDisciplina;
    }

    public void setDescDisciplina(String descDisciplina) {
        this.descDisciplina = descDisciplina;
    }

    public int getIdGrado() {
        return idGrado;
    }

    public void setIdGrado(int idGrado) {
        this.idGrado = idGrado;
    }

    public String getDescGrado() {
        return descGrado;
    }

    public void setDescGrado(String descGrado) {
        this.descGrado = descGrado;
    }
}
