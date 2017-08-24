package bris.es.budolearning.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import bris.es.budolearning.R;
import bris.es.budolearning.domain.Disciplina;
import bris.es.budolearning.domain.Grado;
import bris.es.budolearning.domain.Recurso;
import bris.es.budolearning.domain.Usuario;
import bris.es.budolearning.domain.adapter.CustomRecyclerAdapter;
import bris.es.budolearning.task.JsonPeticion;
import bris.es.budolearning.task.TaskDisciplina;
import bris.es.budolearning.task.TaskFichero;
import bris.es.budolearning.task.TaskGrado;
import bris.es.budolearning.task.TaskRecurso;
import bris.es.budolearning.task.TaskVideoEspecial;
import bris.es.budolearning.utiles.BLSession;
import bris.es.budolearning.utiles.UtilesDialog;

public class FragmentDisciplinaReducida extends FragmentAbstract{

    private TaskVideoEspecial taskVideoEspecial;
    private TaskDisciplina taskDisciplina;
    private TaskGrado taskGrado;
    private TaskRecurso taskRecurso;
    private TaskFichero taskFichero;

    private RecyclerView disciplina;
    private RecyclerView grado;
    private RecyclerView recurso;
    private RecyclerView fichero;


    @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_disciplina_reducida, container, false);

            taskDisciplina = new TaskDisciplina(getActivity(), this);
            taskGrado = new TaskGrado(getActivity(), this);
            taskRecurso = new TaskRecurso(getActivity(), this);
            taskFichero = new TaskFichero(getActivity(), this);
            taskVideoEspecial = new TaskVideoEspecial(getActivity(), this);

            disciplina = (RecyclerView) view.findViewById(R.id.dr_Disciplina_ReciclerView);
            grado = (RecyclerView) view.findViewById(R.id.dr_Grado_ReciclerView);
            recurso = (RecyclerView) view.findViewById(R.id.dr_Recurso_ReciclerView);
            fichero = (RecyclerView) view.findViewById(R.id.dr_Fichero_ReciclerView);

            // Asignar los Layouts a los ReciclerView
            disciplina.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
            grado.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
            recurso.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
            fichero.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

            setHasOptionsMenu(true);

            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            return view;
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            recargar(savedInstanceState);
        }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        visualizarMenus(menu, true, true, false , false, false, false, false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.btn_menu_upload:
                BLSession.getInstance().setFichero(null);
                UtilesDialog.createQuestionYesNo(getActivity(),
                        "COMPARTIR FICHEROS",
                        getString(R.string.fichero_subida),
                        "Aceptar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogo1, int id) {
                                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                intent.addCategory(Intent.CATEGORY_OPENABLE);
                                intent.setType("video/mp4");
                                intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[] {"video/mp4", "application/pdf"});

                                getActivity().startActivityForResult(Intent.createChooser(intent, getActivity().getResources().getString(R.string.select_archive)), FragmentAbstract.CHOOSER_VIDEO);
                            }
                        },
                        "Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogo1, int id) {
                                dialogo1.dismiss();
                            }
                        }
                ).show();
                return true;
            case R.id.btn_menu_extra_video:
                taskVideoEspecial.listUsuario(BLSession.getInstance().getUsuario(), null, getActivity());
                return true;
            case R.id.btn_menu_recargar:
                taskGrado.borrarList();
                taskGrado.list(BLSession.getInstance().getUsuario(), BLSession.getInstance().getDisciplina(), getView().findViewById(R.id.gradoListView));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

        public void recargar(Bundle savedInstanceState){
            cargarComboDisciplinas();
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
        getView().findViewById(R.id.dr_Fichero).setVisibility(View.INVISIBLE);
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
        getView().findViewById(R.id.dr_Fichero).setVisibility(View.INVISIBLE);
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
                    BLSession.getInstance().setFicheros(((Recurso)item).getFicheros());
                    cargarComboFichero();
                    getView().findViewById(R.id.dr_Fichero).setVisibility(View.VISIBLE);
                }
            }
        );
        getView().findViewById(R.id.dr_Fichero).setVisibility(View.INVISIBLE);
    }
    private void cargarComboFichero(){
        if(BLSession.getInstance().getRecurso() != null) {
            taskFichero.list(BLSession.getInstance().getUsuario(), BLSession.getInstance().getRecurso(), getView().findViewById(R.id.dr_Fichero_ReciclerView));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

}
