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
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import bris.es.budolearning.R;
import bris.es.budolearning.domain.Disciplina;
import bris.es.budolearning.domain.Fichero;
import bris.es.budolearning.domain.Grado;
import bris.es.budolearning.domain.Recurso;
import bris.es.budolearning.domain.Usuario;
import bris.es.budolearning.domain.adapter.CustomRecyclerAdapter;
import bris.es.budolearning.task.JsonPeticion;
import bris.es.budolearning.task.TaskDisciplina;
import bris.es.budolearning.task.TaskGrado;
import bris.es.budolearning.task.TaskRecurso;
import bris.es.budolearning.utiles.BLSession;
import bris.es.budolearning.task.TaskFichero;
import bris.es.budolearning.utiles.Utiles;

public class FragmentFicheroDetalle extends FragmentAbstract {

    private TextView ficheroId;
    private EditText ficheroDescripcion;
    private EditText ficheroFecha;
    private CheckBox ficheroActivo;
    private EditText ficheroCoste;

    private TaskDisciplina taskDisciplina;
    private TaskGrado taskGrado;
    private TaskRecurso taskRecurso;
    private TaskFichero taskFichero;


    private RecyclerView disciplina;
    private RecyclerView grado;
    private RecyclerView recurso;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_fichero_detalle, container, false);
        taskFichero = new TaskFichero(getActivity(), this);
        taskDisciplina = new TaskDisciplina(getActivity(), this);
        taskGrado = new TaskGrado(getActivity(), this);
        taskRecurso = new TaskRecurso(getActivity(), this);

        ficheroId = (TextView) view.findViewById(R.id.ficheroId);
        ficheroDescripcion = (EditText)view.findViewById(R.id.ficheroDescripcion);
        ficheroFecha = (EditText)view.findViewById(R.id.ficheroFecha);
        ficheroActivo = (CheckBox) view.findViewById(R.id.ficheroActivo);
        ficheroCoste = (EditText)view.findViewById(R.id.ficheroCoste);

        disciplina = (RecyclerView) view.findViewById(R.id.dr_Disciplina_ReciclerView);
        grado = (RecyclerView) view.findViewById(R.id.dr_Grado_ReciclerView);
        recurso = (RecyclerView) view.findViewById(R.id.dr_Recurso_ReciclerView);

        // Asignar los Layouts a los ReciclerView
        disciplina.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        grado.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recurso.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        final Fichero fichero = BLSession.getInstance().getFichero();
        if(fichero != null && fichero.getId() > 0) {
            ficheroId.setText(String.valueOf(fichero.getId()));
            ficheroDescripcion.setText(fichero.getDescripcion());
            if(fichero.getFecha() != null)
                ficheroFecha.setText(Utiles.getDateFormatDMA().format(fichero.getFecha()));
            else
                ficheroFecha.setText(Utiles.getDateFormatDMA().format(new Date()));

            ficheroActivo.setChecked(fichero.getActivo()>0);
            ficheroCoste.setText(String.valueOf(fichero.getCoste()));
        }


        ficheroFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                if(fichero != null && fichero.getFecha() != null)
                    c.setTime(fichero.getFecha());
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
                                ficheroFecha.setText(Utiles.getDateFormatDMA().format(cc.getTime()));
                            }
                        },
                        c.get(Calendar.YEAR),
                        c.get(Calendar.MONTH),
                        c.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cargarComboDisciplinas();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btn_menu_guardar:
                Fichero fichero = BLSession.getInstance().getFichero();

                if(fichero == null) fichero = new Fichero();

                if(ficheroId.getText().length()>0) {
                    fichero.setId(Integer.parseInt(ficheroId.getText().toString()));
                }
                fichero.setDescripcion(ficheroDescripcion.getText().toString());
                try {
                    fichero.setFecha(Utiles.getDateFormatDMA().parse(ficheroFecha.getText().toString()));
                }catch(Exception e){
                    fichero.setFecha(new Date());
                }
                fichero.setCoste(Integer.parseInt(ficheroCoste.getText().toString()));
                fichero.setActivo(ficheroActivo.isChecked()?1:0);

                if(fichero.getId() == 0) {
                    fichero.setTamano("0");
                    fichero.setFecha(new Date());
                    taskFichero.insert(BLSession.getInstance().getUsuario(), fichero);
                } else {
                    taskFichero.update(BLSession.getInstance().getUsuario(), fichero);
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        visualizarMenus(menu, false, false, false, false, true, false, false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void cargarComboDisciplinas(){
        taskDisciplina.list(BLSession.getInstance().getUsuario(), null, disciplina, new CustomRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Object item, View view) {
                BLSession.getInstance().setDisciplina((Disciplina) item);
                BLSession.getInstance().setGrados(((Disciplina)item).getGrados());
                cargarComboGrado();
                getView().findViewById(R.id.dr_Grado).setVisibility(View.VISIBLE);
            }
        });
        getView().findViewById(R.id.dr_Grado).setVisibility(View.INVISIBLE);
        getView().findViewById(R.id.dr_Recurso).setVisibility(View.INVISIBLE);
    }
    private void cargarComboGrado(){
        taskGrado.list(BLSession.getInstance().getUsuario(),BLSession.getInstance().getDisciplina(), grado,
                new CustomRecyclerAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Object item, View view) {
                        BLSession.getInstance().setGrado((Grado) item);
                        BLSession.getInstance().setRecursos(((Grado)item).getRecursos());
                        cargarComboRecurso();
                        getView().findViewById(R.id.dr_Recurso).setVisibility(View.VISIBLE);
                    }
                });
        getView().findViewById(R.id.dr_Recurso).setVisibility(View.INVISIBLE);
    }

    private void cargarComboRecurso(){
        JsonPeticion peticion = new JsonPeticion();
        peticion.setUser(new Usuario(BLSession.getInstance().getUsuario()));
        peticion.setDisciplina(BLSession.getInstance().getDisciplina());
        peticion.setGrado(BLSession.getInstance().getGrado());

        taskRecurso.list(BLSession.getInstance().getUsuario(), peticion, recurso,
                new CustomRecyclerAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Object item, View view) {
                        BLSession.getInstance().setRecurso((Recurso) item);

                        taskFichero.updateRecurso(BLSession.getInstance().getUsuario(), BLSession.getInstance().getFichero(), (Recurso) item);
                    }
                }
        );
    }


}