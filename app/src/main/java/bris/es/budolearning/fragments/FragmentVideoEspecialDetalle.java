package bris.es.budolearning.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import bris.es.budolearning.task.TaskClub;
import bris.es.budolearning.task.TaskUsuario;
import bris.es.budolearning.task.TaskVideoEspecial;
import bris.es.budolearning.utiles.BLSession;
import bris.es.budolearning.utiles.Utiles;

public class FragmentVideoEspecialDetalle extends FragmentAbstract {

    private TextView videoEspecialId;
    private Spinner clubes;
    private CheckBox usarAlumno;
    private Spinner alumno;
    private Spinner disciplina;
    private Spinner grado;
    private Spinner recurso;
    private Spinner fichero;
    private TextView inicio;
    private TextView fin;

    private TaskVideoEspecial taskVideoEspecial;
    private TaskClub taskClub;
    private TaskUsuario taskUsuario;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_video_especial_detalle, container, false);
        taskVideoEspecial = new TaskVideoEspecial(getActivity(), this);
        taskClub = new TaskClub(getActivity(), this);
        taskUsuario = new TaskUsuario(getActivity(), this);

        videoEspecialId = (TextView) view.findViewById(R.id.videoEspecialId);
        clubes = (Spinner) view.findViewById(R.id.videoEspecialClub);
        usarAlumno = (CheckBox) view.findViewById(R.id.videoEspecialUsarUsuario);
        alumno = (Spinner) view.findViewById(R.id.videoEspecialAlumno);

        disciplina = (Spinner) view.findViewById(R.id.videoEspecialDisciplina);
        grado = (Spinner) view.findViewById(R.id.videoEspecialGrado);
        recurso = (Spinner) view.findViewById(R.id.videoEspecialRecurso);
        fichero = (Spinner) view.findViewById(R.id.videoEspecialFichero);

        inicio = (EditText)view.findViewById(R.id.videoEspecialInicio);
        fin = (EditText)view.findViewById(R.id.videoEspecialFin);

        final VideoEspecial ve = BLSession.getInstance().getVideoEspecial();

        clubes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(ve.getUsuario() != null)
                    buscarAlumnos(ve.getUsuario().getId());
                else
                    buscarAlumnos(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });


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

        if(ve.getClub() != null) {
            taskClub.setNombreClub(ve.getClub().getNombre());

            if(ve.getUsuario() != null && ve.getUsuario().getId() > 0) {
                usarAlumno.setChecked(true);
                alumno.setEnabled(true);
            } else {
                usarAlumno.setChecked(false);
                alumno.setEnabled(false);
            }

            inicio.setText(Utiles.getDateFormatDMA().format(ve.getInicio()));
            fin.setText(Utiles.getDateFormatDMA().format(ve.getFin()));

            boolean cargado = false;
            idxDisciplina = 0;
            for(Disciplina d: BLSession.getInstance().getUsuario().getDisciplinas()){
                idxGrado = 0;
                for(Grado g:d.getGrados()){
                    idxRecurso = 0;
                    for(Recurso r: g.getRecursos()){
                        idxFichero = 0;
                        for(Fichero f: r.getFicheros()){
                            if(ve.getFichero().getId() == f.getId()){
                                cargarComboDisciplinas();
                                cargarComboGrado(d);
                                cargarComboRecurso(g);
                                cargarComboFichero(r);
                                cargado = true;
                            }
                            if(cargado) break;
                            idxFichero++;
                        }
                        if(cargado) break;
                        idxRecurso++;
                    }
                    if(cargado) break;
                    idxGrado++;
                }
                if(cargado) break;
                idxDisciplina++;
            }
        } else {
            cargarComboDisciplinas();
            cargarComboGrado(disciplinas.get(0));
        }

        if("PROFESOR".equalsIgnoreCase(BLSession.getInstance().getUsuario().getRol())) {
            taskClub.setFijarClub(true);
            taskClub.setNombreClub(BLSession.getInstance().getUsuario().getProfesor().getNombre());
        } else {
            taskClub.setFijarClub(false);
            if(ve != null && ve.getClub() != null)
                taskClub.setNombreClub(ve.getClub().getNombre());
        }
        taskClub.list(BLSession.getInstance().getUsuario(), null, clubes);

        disciplina.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                idxDisciplina = position;
                cargarComboGrado(disciplinas.get(position));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        grado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                idxGrado = position;
                cargarComboRecurso(grados.get(position));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });
        recurso.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                idxRecurso = position;
                cargarComboFichero(recursos.get(position));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        fichero.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                idxFichero = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        usarAlumno.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    alumno.setVisibility(View.VISIBLE);
                    if(ve.getUsuario() != null) {
                        buscarAlumnos(ve.getUsuario().getId());
                    } else {
                        buscarAlumnos(0);
                    }
                } else {
                    alumno.setVisibility(View.INVISIBLE);
                }
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

    int idxFichero;
    int idxRecurso;
    int idxGrado;
    int idxDisciplina;
    List<Disciplina> disciplinas = new ArrayList<>();
    List<Grado> grados = new ArrayList<>();
    List<Recurso> recursos = new ArrayList<>();
    List<Fichero> ficheros = new ArrayList<>();

    private void cargarComboDisciplinas(){
        disciplinas = new ArrayList<>();
        List<String> txtDisciplina = new ArrayList<>();
        for(Disciplina d: BLSession.getInstance().getUsuario().getDisciplinas()) {
            txtDisciplina.add(d.getNombre());
            disciplinas.add(d);
        }

        ArrayAdapter<String> dataAdapterDisciplina = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, txtDisciplina);
        dataAdapterDisciplina.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        disciplina.setAdapter(dataAdapterDisciplina);
        if (idxDisciplina >= 0 && idxDisciplina < txtDisciplina.size())
            disciplina.setSelection(idxDisciplina);

    }
    private void cargarComboGrado(Disciplina d){
        grados = new ArrayList<>();
        List<String> txtGrados = new ArrayList<>();
        for(Grado g: d.getGrados()) {
            txtGrados.add(g.getNombre());
            grados.add(g);
        }

        ArrayAdapter<String> dataAdapterGrado = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, txtGrados);
        dataAdapterGrado.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        grado.setAdapter(dataAdapterGrado);
        grado.setSelection(idxGrado);
        if (idxGrado >= 0 && idxGrado < txtGrados.size())
            grado.setSelection(idxGrado);

    }
    private void cargarComboRecurso(Grado g){
        recursos = new ArrayList<>();
        List<String> txtRecurso = new ArrayList<>();
        for(Recurso r: g.getRecursos()) {
            txtRecurso.add(r.getNombre());
            recursos.add(r);
        }

        ArrayAdapter<String> dataAdapterGrado = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, txtRecurso);
        dataAdapterGrado.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        recurso.setAdapter(dataAdapterGrado);
        if (idxRecurso >= 0 && idxRecurso < txtRecurso.size())
            recurso.setSelection(idxRecurso);

    }
    private void cargarComboFichero(Recurso r){
        ficheros = new ArrayList<>();
        List<String> txtFicheros = new ArrayList<>();
        for(Fichero f: r.getFicheros()) {
            if(f.getCoste() == 0 || BLSession.getInstance().getUsuario().getRol().equalsIgnoreCase("ADMINISTRADOR")) {
                txtFicheros.add(f.getDescripcion());
                ficheros.add(f);
            }
        }

        ArrayAdapter<String> dataAdapterFichero = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, txtFicheros);
        dataAdapterFichero.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fichero.setAdapter(dataAdapterFichero);

        if (idxFichero >= 0 && idxFichero < txtFicheros.size())
            fichero.setSelection(idxFichero);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.registrar_usuarioBtnGuardar:
                VideoEspecial videoEspecial = BLSession.getInstance().getVideoEspecial();

                if(videoEspecial == null) videoEspecial = new VideoEspecial();

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

                Integer idClub = BLSession.getInstance().getClubes().get(clubes.getSelectedItemPosition()).getId();
                Club c = new Club();
                c.setId(idClub);
                videoEspecial.setClub(c);

                Fichero fichero = null;
                int idx = -1;
                for(Fichero fAux:recursos.get(idxRecurso).getFicheros()){
                    if(fAux.getCoste() == 0 || BLSession.getInstance().getUsuario().getRol().equalsIgnoreCase("ADMINISTRADOR")){
                        idx++;
                    }
                    if(idx == idxFichero){
                        fichero = fAux;
                    }
                }

                videoEspecial.setFichero(fichero);

                if(usarAlumno.isChecked()) {
                    Integer idUsuario = BLSession.getInstance().getAlumnos().get(alumno.getSelectedItemPosition()).getId();
                    Usuario usuario = new Usuario();
                    usuario.setId(idUsuario);
                    videoEspecial.setUsuario(usuario);
                } else {
                    videoEspecial.setUsuario(null);
                }

                if(videoEspecial.getId() == 0) {
                    taskVideoEspecial.insert(BLSession.getInstance().getUsuario(), videoEspecial);
                } else {
                    taskVideoEspecial.update(BLSession.getInstance().getUsuario(), videoEspecial);
                }

                return true;
            case R.id.registrar_usuarioBtnActivar:
                taskVideoEspecial.delete(BLSession.getInstance().getUsuario(), BLSession.getInstance().getVideoEspecial());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if(BLSession.getInstance().getVideoEspecial().getId() == 0) {
            inflater.inflate(R.menu.menu_guardar, menu);
        } else {
            inflater.inflate(R.menu.menu_usuario_modificar, menu);
            menu.findItem(R.id.registrar_usuarioBtnActivar).setIcon(android.R.drawable.ic_delete);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

}