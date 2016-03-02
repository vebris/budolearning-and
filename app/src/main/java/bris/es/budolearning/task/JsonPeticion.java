package bris.es.budolearning.task;

public class JsonPeticion {

    private Object data;

    private Object user;
    private Object club;
    private Object disciplina;
    private Object grado;
    private Object recurso;
    private Object fichero;
    private Object visualizaciones;
    private Object puntos;
    private int version;

    public Object getUser() {
        return user;
    }
    public void setUser(Object user) {
        this.user = user;
    }
    public Object getData() {
        return data;
    }
    public void setData(Object data) {
        this.data = data;
    }
    public Object getClub() {
        return club;
    }
    public void setClub(Object club) {
        this.club = club;
    }
    public int getVersion() {
        return version;
    }
    public void setVersion(int version) {
        this.version = version;
    }
    public Object getDisciplina() {
        return disciplina;
    }
    public void setDisciplina(Object disciplina) {
        this.disciplina = disciplina;
    }
    public Object getGrado() {
        return grado;
    }
    public void setGrado(Object grado) {
        this.grado = grado;
    }
    public Object getVisualizaciones() {
        return visualizaciones;
    }
    public void setVisualizaciones(Object visualizaciones) {
        this.visualizaciones = visualizaciones;
    }
    public Object getRecurso() {
        return recurso;
    }
    public void setRecurso(Object recurso) {
        this.recurso = recurso;
    }
    public Object getFichero() {
        return fichero;
    }
    public void setFichero(Object fichero) {
        this.fichero = fichero;
    }
    public Object getPuntos() {
        return puntos;
    }
    public void setPuntos(Object puntos) {
        this.puntos = puntos;
    }
}

