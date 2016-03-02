package bris.es.budolearning.domain;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class Usuario extends GenericObject {

    private String login;
    private String password;

    private String nombre;
    private String apellido1;
    private String apellido2;
    private String dni;
    private String mail;
    private String rol;

    private Boolean activo;

    private String direccion;
    private String localidad;
    private String telefono;

    private Club profesor;
    private Club entrena;

    private List<Disciplina> disciplinas;

    private int puntos;

    public Usuario(){}
    public Usuario(JSONObject json, boolean completo) {
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
            setApellido1(json.getString("apellido1"));
        } catch (Exception e) {
            Log.e(this.getClass().getCanonicalName(), "Error crear " + this.getClass().getCanonicalName());
        }
        try {
            setApellido2(json.getString("apellido2"));
        } catch (Exception e) {
            Log.e(this.getClass().getCanonicalName(), "Error crear " + this.getClass().getCanonicalName());
        }
        try {
            setLogin(json.getString("login"));
        } catch (Exception e) {
            Log.e(this.getClass().getCanonicalName(), "Error crear " + this.getClass().getCanonicalName());
        }
        try {
            setPassword(json.getString("password"));
        } catch (Exception e) {
            Log.e(this.getClass().getCanonicalName(), "Error crear " + this.getClass().getCanonicalName());
        }
        try {
            setRol(json.getString("rol"));
        } catch (Exception e) {
            Log.e(this.getClass().getCanonicalName(), "Error crear " + this.getClass().getCanonicalName());
        }
        try {
            setActivo(json.getBoolean("activo"));
        } catch (Exception e) {
            Log.e(this.getClass().getCanonicalName(), "Error crear " + this.getClass().getCanonicalName());
        }
        try {
            setDni(json.getString("dni"));
        } catch (Exception e) {
            Log.e(this.getClass().getCanonicalName(), "Error crear " + this.getClass().getCanonicalName());
        }
        try {
            setDireccion(json.getString("direccion"));
        } catch (Exception e) {
            Log.e(this.getClass().getCanonicalName(), "Error crear " + this.getClass().getCanonicalName());
        }
        try {
            setLocalidad(json.getString("localidad"));
        } catch (Exception e) {
            Log.e(this.getClass().getCanonicalName(), "Error crear " + this.getClass().getCanonicalName());
        }
        try {
            setMail(json.getString("mail"));
        } catch (Exception e) {
            Log.e(this.getClass().getCanonicalName(), "Error crear " + this.getClass().getCanonicalName());
        }
        try {
            setTelefono(json.getString("telefono"));
        } catch (Exception e) {
            Log.e(this.getClass().getCanonicalName(), "Error crear " + this.getClass().getCanonicalName());
        }
        try {
            setProfesor(new Club(json.getJSONObject("profesor")));
        } catch (Exception e) {
            Log.e(this.getClass().getCanonicalName(), "Error crear " + this.getClass().getCanonicalName());
        }
        try {
            setEntrena(new Club(json.getJSONObject("entrena")));
        } catch (Exception e) {
            Log.e(this.getClass().getCanonicalName(), "Error crear " + this.getClass().getCanonicalName());
        }
        try {
            setDisciplinas(json.getJSONArray("disciplinas"), completo);
        } catch (Exception e) {
            try {
                setDisciplinas(json.getJSONArray("disciplinaGrados"), completo);
            } catch(Exception e2) {
                Log.e(this.getClass().getCanonicalName(), "Error crear " + this.getClass().getCanonicalName());
            }
        }
        try {
            setPuntos(json.getInt("puntos"));
        } catch (Exception e) {
            Log.e(this.getClass().getCanonicalName(), "Error crear " + this.getClass().getCanonicalName());
        }
    }

    public Usuario (Usuario u){
        id=u.getId();
        login = u.getLogin();
        password = u.getPassword();
        nombre=u.getNombre();
        apellido1=u.getApellido1();
        apellido2=u.getApellido2();
        dni = u.getDni();
        mail = u.getMail();
        rol = u.getRol();
        activo = u.getActivo();
        direccion = u.getDireccion();
        localidad = u.getLocalidad();
        telefono = u.getTelefono();
        profesor = u.getProfesor();
        entrena = u.getEntrena();
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getApellido1() {
        return apellido1;
    }
    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }
    public String getApellido2() {
        return apellido2;
    }
    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }
    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getRol() {
        return rol;
    }
    public void setRol(String rol) {
        this.rol = rol;
    }
    public Boolean getActivo() {
        return activo;
    }
    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
    public String getDni() {
        return dni;
    }
    public void setDni(String dni) {
        this.dni = dni;
    }
    public String getDireccion() {
        return direccion;
    }
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    public String getLocalidad() {
        return localidad;
    }
    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }
    public String getMail() {
        return mail;
    }
    public void setMail(String mail) {
        this.mail = mail;
    }
    public String getTelefono() {
        return telefono;
    }
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    public Club getProfesor() {
        return profesor;
    }
    public void setProfesor(Club profesor) {
        this.profesor = profesor;
    }
    public Club getEntrena() {
        return entrena;
    }
    public void setEntrena(Club entrena) {
        this.entrena = entrena;
    }
    public int getPuntos() {
        return puntos;
    }
    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }
    public List<Disciplina> getDisciplinas() { return disciplinas; }
    public void setDisciplinas(List<Disciplina> disciplinas) { this.disciplinas = disciplinas; }

    public void setDisciplinas(JSONArray disciplinas, boolean completo) {
        this.disciplinas= new ArrayList<>();
        for(int i=0;i<disciplinas.length();i++){
            try{this.disciplinas.add(new Disciplina(disciplinas.getJSONObject(i), completo));}catch(Exception e){}
        }
    }

    public Disciplina getDisciplina(int idDisciplina){
        for(Disciplina d: getDisciplinas()){
            if(d.getId() == idDisciplina)
                return d;
        }
        return null;
    }
}


