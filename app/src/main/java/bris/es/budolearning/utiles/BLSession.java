package bris.es.budolearning.utiles;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import bris.es.budolearning.domain.Articulo;
import bris.es.budolearning.domain.Club;
import bris.es.budolearning.domain.Curso;
import bris.es.budolearning.domain.Disciplina;
import bris.es.budolearning.domain.Fichero;
import bris.es.budolearning.domain.Grado;
import bris.es.budolearning.domain.Puntos;
import bris.es.budolearning.domain.Recurso;
import bris.es.budolearning.domain.Usuario;
import bris.es.budolearning.domain.UsuarioDisciplinaGrado;
import bris.es.budolearning.domain.VideoEspecial;
import bris.es.budolearning.fragments.FragmentTabsDisciplinas;
import bris.es.budolearning.slidingtabs.PagerItem;

public class BLSession implements Serializable{

    private static BLSession instance;

    protected Integer idUser;
    private Usuario usuario;

    protected List<Disciplina> disciplinas;
    protected Disciplina disciplina;
    protected List<Grado> grados;
    protected Grado grado;
    protected List<Recurso> recursos;
    protected Recurso recurso;
    protected List<Fichero> ficheros;
    protected Fichero fichero;

    protected Articulo articulo;
    protected List<Articulo> articulos;
    protected Curso curso;
    protected List<Curso> cursos;
    protected Club club;
    protected List<Club> clubes;
    protected List<VideoEspecial> videosEspeciales;
    protected VideoEspecial videoEspecial;

    private List<Usuario> alumnos;

    private List<Fichero> ficherosDescargados;

    private boolean autoLogin = true;

    private int posicionAlumno;
    private List<UsuarioDisciplinaGrado> usuarioDisciplinaGrados;

    private List<PagerItem> tabsDisciplinas;

    private Date fechaFichero;

    private int puntos;

    private List<Puntos> listPuntos;

    private boolean modoPublicidad;

    public List<PagerItem> getTabsDisciplinas() {
        return tabsDisciplinas;
    }

    public void recargarTabs(int posicion){
        FragmentTabsDisciplinas.mViewPager.setAdapter(FragmentTabsDisciplinas.mFragmentPagerAdapter);
        FragmentTabsDisciplinas.mViewPager.setCurrentItem(posicion);
        FragmentTabsDisciplinas.mSlidingTabLayout.setViewPager(FragmentTabsDisciplinas.mViewPager);
        FragmentTabsDisciplinas.mFragmentPagerAdapter.notifyDataSetChanged();
    }

    public void setTabsDisciplinas(List<PagerItem> tabsDisciplinas) {
        this.tabsDisciplinas = tabsDisciplinas;
    }

    protected BLSession() {
        // Exists only to defeat instantiation.
        cursos = new ArrayList<>();
        ficherosDescargados = new ArrayList<>();
    }

    public static void setInstance(BLSession blsession){
        instance = blsession;
    }

    public static BLSession getInstance() {
        if (instance == null) {
            instance = new BLSession();
        }
        return instance;
    }

    public static void inicializar(){
        BLSession.getInstance().setIdUser(null);
        BLSession.getInstance().setUsuario(null);
        BLSession.getInstance().setCursos(new ArrayList<Curso>());
        BLSession.getInstance().setClubes(new ArrayList<Club>());
        BLSession.getInstance().setAlumnos(new ArrayList<Usuario>());
        BLSession.getInstance().setFicherosDescargados(new ArrayList<Fichero>());
        BLSession.getInstance().setModoPublicidad(true);

        BLSession.getInstance().setDisciplinas(new ArrayList<Disciplina>());
        BLSession.getInstance().setGrados(new ArrayList<Grado>());
        BLSession.getInstance().setRecursos(new ArrayList<Recurso>());
        BLSession.getInstance().setFicheros(new ArrayList<Fichero>());
    }

    public void setCursos(List<Curso> cursos) { this.cursos = cursos;}
    public List<Curso> getCursos() {
        return cursos;
    }
    public void setClubes(List<Club> clubes) { this.clubes = clubes;}
    public List<Club> getClubes() {
        return clubes;
    }
    public void setFicherosDescargados(List<Fichero> ficherosDescargados) { this.ficherosDescargados = ficherosDescargados;}
    public List<Fichero> getFicherosDescargados() { return ficherosDescargados; }
    public Usuario getUsuario() {
        return usuario;
    }
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        ficherosDescargados = new ArrayList<>();
        for(Disciplina d:usuario.getDisciplinas()){
            for(Grado g:d.getGrados()){
                for(Recurso r:g.getRecursos()){
                    for(Fichero f: r.getFicheros()){
                        ficherosDescargados.add(f);
                    }
                }
            }
        }
    }

    public Integer getIdUser() {
        return idUser;
    }
    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public boolean isAutoLogin() {
        return this.autoLogin;
    }
    public void setAutoLogin(boolean autoLogin) {
        this.autoLogin = autoLogin;
    }

    public List<Usuario> getAlumnos() {
        return alumnos;
    }
    public void setAlumnos(List<Usuario> alumnos) {
        this.alumnos = alumnos;
    }

    public int getPosicionAlumno() {        return posicionAlumno;    }
    public void setPosicionAlumno(int posicionAlumno) {        this.posicionAlumno = posicionAlumno;    }

    public List<UsuarioDisciplinaGrado> getUsuarioDisciplinaGrados() {
        return usuarioDisciplinaGrados;
    }
    public void setUsuarioDisciplinaGrados(List<UsuarioDisciplinaGrado> usuarioDisciplinaGrados) {
        this.usuarioDisciplinaGrados = usuarioDisciplinaGrados;
    }

    public Articulo getArticulo() {
        return articulo;
    }
    public void setArticulo(Articulo articulo) {
        this.articulo = articulo;
    }
    public List<Articulo> getArticulos() {
        return articulos;
    }
    public void setArticulos(List<Articulo> articulos) {
        this.articulos = articulos;
    }

    public Date getFechaFichero() {
        return fechaFichero;
    }
    public void setFechaFichero(Date fechaFichero) {
        this.fechaFichero = fechaFichero;
    }
    public Curso getCurso() {
        return curso;
    }
    public void setCurso(Curso curso) {
        this.curso = curso;
    }
    public Club getClub() {
        return club;
    }
    public void setClub(Club club) {
        this.club = club;
    }

    public Fichero getFichero() {
        return fichero;
    }
    public void setFichero(Fichero fichero) {
        this.fichero = fichero;
    }
    public List<VideoEspecial> getVideosEspeciales() {
        return videosEspeciales;
    }
    public void setVideosEspeciales(List<VideoEspecial> videosEspeciales) {
        this.videosEspeciales = videosEspeciales;
    }
    public VideoEspecial getVideoEspecial() {
        return videoEspecial;
    }
    public void setVideoEspecial(VideoEspecial videoEspecial) {
        this.videoEspecial = videoEspecial;
    }

    public int getPuntos() {
        return puntos;
    }
    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }
    public boolean isModoPublicidad() {
        return modoPublicidad;
    }
    public void setModoPublicidad(boolean modoPublicidad) {
        this.modoPublicidad = modoPublicidad;
    }
    public List<Puntos> getListPuntos() {
        return listPuntos;
    }
    public void setListPuntos(List<Puntos> listPuntos) {
        this.listPuntos = listPuntos;
    }
    public Disciplina getDisciplina() {
        return disciplina;
    }
    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }
    public List<Disciplina> getDisciplinas() {
        return disciplinas;
    }
    public void setDisciplinas(List<Disciplina> disciplinas) {
        this.disciplinas = disciplinas;
    }
    public List<Grado> getGrados() {
        return grados;
    }
    public void setGrados(List<Grado> grados) {
        this.grados = grados;
    }
    public Grado getGrado() {
        return grado;
    }
    public void setGrado(Grado grado) {
        this.grado = grado;
    }
    public List<Recurso> getRecursos() {
        return recursos;
    }
    public void setRecursos(List<Recurso> recursos) {
        this.recursos = recursos;
    }
    public Recurso getRecurso() {
        return recurso;
    }
    public void setRecurso(Recurso recurso) {
        this.recurso = recurso;
    }
    public List<Fichero> getFicheros() {
        return ficheros;
    }
    public void setFicheros(List<Fichero> ficheros) {
        this.ficheros = ficheros;
    }
}