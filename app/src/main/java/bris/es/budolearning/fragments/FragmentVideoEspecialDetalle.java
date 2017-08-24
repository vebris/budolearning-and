package bris.es.budolearning.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import bris.es.budolearning.R;
import bris.es.budolearning.domain.Club;
import bris.es.budolearning.domain.Disciplina;
import bris.es.budolearning.domain.Fichero;
import bris.es.budolearning.domain.Grado;
import bris.es.budolearning.domain.Recurso;
import bris.es.budolearning.domain.Usuario;
import bris.es.budolearning.domain.VideoEspecial;
import bris.es.budolearning.domain.adapter.CustomRecyclerAdapter;
import bris.es.budolearning.task.TaskClub;
import bris.es.budolearning.task.TaskUsuario;
import bris.es.budolearning.task.TaskVideoEspecial;
import bris.es.budolearning.utiles.BLSession;
import bris.es.budolearning.utiles.Utiles;
import bris.es.budolearning.utiles.UtilesDialog;

public class FragmentVideoEspecialDetalle extends FragmentAbstract {

    private TextView videoEspecialId;
    private Spinner clubes;
    private CheckBox usarAlumno;
    private Spinner alumno;
    private RecyclerView disciplina;
    private RecyclerView grado;
    private RecyclerView recurso;
    private RecyclerView fichero;
    private TextView inicio;
    private TextView fin;

    private TaskVideoEspecial taskVideoEspecial;
    private TaskUsuario taskUsuario;

    static Disciplina disciplinaL;
    static Grado gradoL;
    static Recurso recursoL;
    static Fichero ficheroL;

    VideoEspecial videoEspecial;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_video_especial_detalle, container, false);
        taskVideoEspecial = new TaskVideoEspecial(getActivity(), this);
        TaskClub taskClub = new TaskClub(getActivity(), this);
        taskUsuario = new TaskUsuario(getActivity(), this);

        videoEspecialId = (TextView) view.findViewById(R.id.videoEspecialId);
        clubes = (Spinner) view.findViewById(R.id.videoEspecialClub);
        usarAlumno = (CheckBox) view.findViewById(R.id.videoEspecialUsarUsuario);
        alumno = (Spinner) view.findViewById(R.id.videoEspecialAlumno);

        disciplina = (RecyclerView) view.findViewById(R.id.videoEspecial_Disciplina_ReciclerView);
        grado = (RecyclerView) view.findViewById(R.id.videoEspecial_Grado_ReciclerView);
        recurso = (RecyclerView) view.findViewById(R.id.videoEspecial_Recurso_ReciclerView);
        fichero = (RecyclerView) view.findViewById(R.id.videoEspecial_Fichero_ReciclerView);

        inicio = (EditText)view.findViewById(R.id.videoEspecialInicio);
        fin = (EditText)view.findViewById(R.id.videoEspecialFin);


        // Asignar los Layouts a los ReciclerView
        disciplina.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        grado.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recurso.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        fichero.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));


        videoEspecial = BLSession.getInstance().getVideoEspecial();

        // Asignar los datos a cada View.
        disciplinaL = null;
        gradoL = null;
        recursoL = null;
        ficheroL = null;


        ////////////////////////////////////////////////////////////////////////////////////////////
        // Asignar los datos al View: ALUMNO
        ////////////////////////////////////////////////////////////////////////////////////////////
        if(videoEspecial.getClub() != null) {
            taskClub.setNombreClub(videoEspecial.getClub().getNombre());

            if (videoEspecial.getUsuario() != null && videoEspecial.getUsuario().getId() > 0) {
                usarAlumno.setChecked(true);
                alumno.setEnabled(true);
            } else {
                usarAlumno.setChecked(false);
                alumno.setEnabled(false);
            }
        }

        ////////////////////////////////////////////////////////////////////////////////////////////
        // Asignar los datos al View: CLUB
        ////////////////////////////////////////////////////////////////////////////////////////////
        if("PROFESOR".equalsIgnoreCase(BLSession.getInstance().getUsuario().getRol())) {
            taskClub.setFijarClub(true);
            taskClub.setNombreClub(BLSession.getInstance().getUsuario().getProfesor().getNombre());
        } else {
            taskClub.setFijarClub(false);
            if(videoEspecial.getClub() != null)
                taskClub.setNombreClub(videoEspecial.getClub().getNombre());
        }
        taskClub.list(BLSession.getInstance().getUsuario(), null, clubes);

        ////////////////////////////////////////////////////////////////////////////////////////////
        // Asignar los datos a los View de Fechas
        ////////////////////////////////////////////////////////////////////////////////////////////
        if(videoEspecial.getInicio() != null)  inicio.setText(Utiles.getDateFormatDMA().format(videoEspecial.getInicio()));
        if(videoEspecial.getFin()    != null)  fin.setText(Utiles.getDateFormatDMA().format(videoEspecial.getFin()));



        // Buscar los datos para la carga de los RecycleView
        if(videoEspecial.getFichero() != null) {
            for (Disciplina d : BLSession.getInstance().getUsuario().getDisciplinas()) {
                for (Grado g : d.getGrados()) {
                    for (Recurso r : g.getRecursos()) {
                        for (Fichero f : r.getFicheros()) {
                            if (videoEspecial.getFichero().getId() == f.getId()) {
                                disciplinaL = d;
                                gradoL = g;
                                recursoL = r;
                                ficheroL = f;
                            }
                            if (ficheroL != null) break;
                        }
                        if (recursoL != null) break;
                    }
                    if (gradoL != null) break;
                }
                if (disciplinaL != null) break;
            }
        }

        // Asignar los datos al View: FICHERO
        if(recursoL != null) cargarComboFichero(recursoL, ficheroL);
        // Asignar los datos al View: RECURSO
        if(gradoL != null) cargarComboRecurso(gradoL, recursoL);
        // Asignar los datos al View: GRADO
        if(disciplinaL != null) cargarComboGrado(disciplinaL, gradoL);
        // Asignar los datos al View: DISCIPLINA
        cargarComboDisciplinas(disciplinaL);

        ////////////////////////////////////////////////////////////////////////////////////////////
        // EVENTOS
        ////////////////////////////////////////////////////////////////////////////////////////////
        inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                if (BLSession.getInstance().getVideoEspecial().getInicio() != null)
                    c.setTime(BLSession.getInstance().getVideoEspecial().getInicio());
                else
                    c.setTime(new Date());
                new DatePickerDialog(view.getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                Calendar cc = Calendar.getInstance();
                                cc.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                cc.set(Calendar.MONTH, monthOfYear);
                                cc.set(Calendar.YEAR, year);
                                inicio.setText(Utiles.getDateFormatDMA().format(cc.getTime()));
                            }
                        },
                        c.get(Calendar.YEAR),
                        c.get(Calendar.MONTH),
                        c.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        fin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                if (BLSession.getInstance().getVideoEspecial().getFin() != null)
                    c.setTime(BLSession.getInstance().getVideoEspecial().getFin());
                else
                    c.setTime(new Date());
                new DatePickerDialog(view.getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                Calendar cc = Calendar.getInstance();
                                cc.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                cc.set(Calendar.MONTH, monthOfYear);
                                cc.set(Calendar.YEAR, year);
                                fin.setText(Utiles.getDateFormatDMA().format(cc.getTime()));
                            }
                        },
                        c.get(Calendar.YEAR),
                        c.get(Calendar.MONTH),
                        c.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        usarAlumno.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    alumno.setEnabled(true);
                    if(videoEspecial.getUsuario() != null) {
                        buscarAlumnos(videoEspecial.getUsuario().getId());
                    } else {
                        buscarAlumnos(0);
                    }
                } else {
                    alumno.setEnabled(false);
                }
            }
        });

        clubes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(videoEspecial.getUsuario() != null)
                    buscarAlumnos(videoEspecial.getUsuario().getId());
                else
                    buscarAlumnos(0);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

        setHasOptionsMenu(true);
        return view;
    }

    private void buscarAlumnos(int idAlumno){
        BLSession.getInstance().setAlumnos(new ArrayList<Usuario>());
        if(usarAlumno.isChecked()) {
            Club club = BLSession.getInstance().getUsuario().getProfesor();
            if (BLSession.getInstance().getUsuario().getRol().equalsIgnoreCase("ADMINISTRADOR")) {
                for (Club c : BLSession.getInstance().getClubes()) {
                    if (c.getNombre().equalsIgnoreCase(clubes.getSelectedItem().toString())) {
                        club = c;
                        break;
                    }
                }
            } else {
                club = BLSession.getInstance().getUsuario().getProfesor();
            }
            taskUsuario.setIdAlumno(idAlumno);
            taskUsuario.list(BLSession.getInstance().getUsuario(), club, alumno);
        }
    }

    private void cargarComboDisciplinas(final Disciplina disciplinaL){
        FragmentVideoEspecialDetalle.disciplinaL = disciplinaL;
        FragmentVideoEspecialDetalle.gradoL = null;
        FragmentVideoEspecialDetalle.recursoL = null;
        FragmentVideoEspecialDetalle.ficheroL = null;

        List<Disciplina> disciplinas = new ArrayList<Disciplina>();
        int id=0;
        int idSelected = -1;
        for(Disciplina d: BLSession.getInstance().getUsuario().getDisciplinas()) {
            disciplinas.add(d);
            if(disciplinaL != null && d.getId() == disciplinaL.getId()) {
                idSelected = id;
            }
            id++;
        }
        CustomRecyclerAdapter adapter = new CustomRecyclerAdapter(disciplinas, getActivity(), disciplinaL,
                new CustomRecyclerAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Object item, View view) {
                        cargarComboGrado((Disciplina)item, null);
                        cargarComboRecurso(null, null);
                        cargarComboFichero(null, null);

                        FragmentVideoEspecialDetalle.disciplinaL = (Disciplina) item;
                        FragmentVideoEspecialDetalle.gradoL = null;
                        FragmentVideoEspecialDetalle.recursoL = null;
                        FragmentVideoEspecialDetalle.ficheroL = null;
                    }
                });
        disciplina.setAdapter(adapter);
        disciplina.scrollToPosition(idSelected);

    }
    private void cargarComboGrado(Disciplina d, Grado gradoL){
        FragmentVideoEspecialDetalle.gradoL = gradoL;
        FragmentVideoEspecialDetalle.recursoL = null;
        FragmentVideoEspecialDetalle.ficheroL = null;

        List<Grado> grados = new ArrayList<Grado>();
        int id=0;
        int idSelected = -1;
        if(d != null) {
            for (Grado g : d.getGrados()) {
                grados.add(g);
                if(gradoL != null && g.getId() == gradoL.getId()) {
                    idSelected = id;
                }
                id++;
            }
        }

        CustomRecyclerAdapter adapter = new CustomRecyclerAdapter(grados, getActivity(), gradoL,
                new CustomRecyclerAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Object item, View view) {
                        cargarComboRecurso((Grado) item, null);
                        cargarComboFichero(null, null);

                        FragmentVideoEspecialDetalle.gradoL = (Grado) item;
                        FragmentVideoEspecialDetalle.recursoL = null;
                        FragmentVideoEspecialDetalle.ficheroL = null;
                    }
                });
        grado.setAdapter(adapter);
        grado.scrollToPosition(idSelected);

    }
    private void cargarComboRecurso(Grado g, Recurso recursoL){
        FragmentVideoEspecialDetalle.recursoL = recursoL;
        FragmentVideoEspecialDetalle.ficheroL = null;

        List<Recurso> recursos = new ArrayList<Recurso>();
        int id=0;
        int idSelected = -1;
        if(g != null) {
            for (Recurso r : g.getRecursos()) {
                recursos.add(r);
                if(recursoL != null && r.getId() == recursoL.getId()) {
                    idSelected = id;
                }
                id++;
            }
        }
        CustomRecyclerAdapter adapter = new CustomRecyclerAdapter(recursos, getActivity(), recursoL,
                new CustomRecyclerAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Object item, View view) {
                        cargarComboFichero((Recurso) item, null);
                        FragmentVideoEspecialDetalle.recursoL = (Recurso) item;
                        FragmentVideoEspecialDetalle.ficheroL = null;
                    }
                });
        recurso.setAdapter(adapter);
        recurso.scrollToPosition(idSelected);

    }
    private void cargarComboFichero(Recurso r, Fichero ficheroL){
        FragmentVideoEspecialDetalle.ficheroL = ficheroL;

        List<Fichero> ficheros = new ArrayList<Fichero>();
        int id=0;
        int idSelected = -1;
        if(r != null) {
            for (Fichero f : r.getFicheros()) {
                if (BLSession.getInstance().getUsuario().getRol().equalsIgnoreCase("ADMINISTRADOR")) {
                    ficheros.add(f);
                    if(ficheroL != null && f.getId() == ficheroL.getId()){
                        idSelected = id;
                    }
                }
                id++;
            }
        }
        CustomRecyclerAdapter adapter = new CustomRecyclerAdapter(ficheros, getActivity(), ficheroL,
                new CustomRecyclerAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Object item, View view) {
                        FragmentVideoEspecialDetalle.ficheroL = (Fichero) item;
                    }
                });
        fichero.setAdapter(adapter);
        fichero.scrollToPosition(idSelected);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btn_menu_guardar:

                if(ficheroL == null) {
                    UtilesDialog.createErrorMessage(getActivity(),"Error", "Debe elegir un fichero.").show();
                    return true;
                } else {
                    videoEspecial.setFichero(ficheroL);
                }

                if(videoEspecialId.getText().length()>0) {
                    videoEspecialId.setId(Integer.parseInt(videoEspecialId.getText().toString()));
                }
                try {
                    videoEspecial.setInicio(Utiles.getDateFormatDMA().parse(inicio.getText().toString()));
                }catch(Exception e){
                    videoEspecial.setInicio(new Date());
                }
                try {
                    videoEspecial.setFin(Utiles.getDateFormatDMA().parse(fin.getText().toString()));
                }catch(Exception e){
                    videoEspecial.setFin(new Date());
                }

                if(clubes.getSelectedItemPosition() > 0) {
                    Integer idClub = BLSession.getInstance().getClubes().get(clubes.getSelectedItemPosition() - 1).getId();
                    Club c = new Club();
                    c.setId(idClub);
                    videoEspecial.setClub(c);
                } else {
                    return false;
                }

                if(usarAlumno.isChecked()) {
                    if(alumno.getSelectedItemPosition() > 0) {
                        Integer idUsuario = BLSession.getInstance().getAlumnos().get(alumno.getSelectedItemPosition()-1).getId();
                        Usuario usuario = new Usuario();
                        usuario.setId(idUsuario);
                        videoEspecial.setUsuario(usuario);
                    } else {
                        videoEspecial.setUsuario(null);
                    }
                } else {
                    videoEspecial.setUsuario(null);
                }

                if(videoEspecial.getId() == 0) {
                    taskVideoEspecial.insert(BLSession.getInstance().getUsuario(), videoEspecial);
                } else {
                    taskVideoEspecial.update(BLSession.getInstance().getUsuario(), videoEspecial);
                }

                return true;
            case R.id.btn_menu_activar:
                taskVideoEspecial.delete(BLSession.getInstance().getUsuario(), BLSession.getInstance().getVideoEspecial());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        if(BLSession.getInstance().getVideoEspecial().getId() == 0) {
            visualizarMenus(menu, false, false, false, false, true, false, false);
        } else {
            visualizarMenus(menu, false, false, false, true, true, false, false);
            menu.findItem(R.id.btn_menu_activar).setIcon(android.R.drawable.ic_delete);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

}