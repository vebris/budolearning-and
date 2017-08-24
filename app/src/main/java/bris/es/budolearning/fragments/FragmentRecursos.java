package bris.es.budolearning.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import bris.es.budolearning.R;
import bris.es.budolearning.domain.Recurso;
import bris.es.budolearning.domain.Usuario;
import bris.es.budolearning.slidingtabs.PagerItem;
import bris.es.budolearning.task.JsonPeticion;
import bris.es.budolearning.task.TaskFichero;
import bris.es.budolearning.task.TaskRecurso;
import bris.es.budolearning.task.TaskVideoEspecial;
import bris.es.budolearning.utiles.BLSession;
import bris.es.budolearning.utiles.Utiles;
import bris.es.budolearning.utiles.UtilesDialog;

public class FragmentRecursos extends FragmentAbstract {

    private TaskRecurso taskRecurso;
    private TaskVideoEspecial taskVideoEspecial;
    private FragmentRecursos este;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recursos, container, false);
        este = this;
        taskRecurso = new TaskRecurso(getActivity(), this);
        taskVideoEspecial = new TaskVideoEspecial(getActivity(), this);

        ListView mRecursoView = (ListView) view.findViewById(R.id.recursoListView);
        mRecursoView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int posicion, long arg3) {

                Recurso r = BLSession.getInstance().getRecursos().get(posicion);

                new TaskFichero(getActivity(), este).borrarList();

                BLSession.getInstance().setRecurso(r);

                int POS = 3;
                while(BLSession.getInstance().getTabsDisciplinas().size()>POS){
                    BLSession.getInstance().getTabsDisciplinas().remove(POS);
                }
                BLSession.getInstance().getTabsDisciplinas().add(new PagerItem(FragmentFicheros.class, r.getNombre()));
                BLSession.getInstance().recargarTabs(POS);

            }

        });

        if(Utiles.esSoloAdmin()) {
            mRecursoView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                    final String[] items = {"Modificar"};
                    final Recurso recurso = BLSession.getInstance().getRecursos().get(position);
                    BLSession.getInstance().setRecurso(recurso);
                    UtilesDialog.createListDialog(este.getActivity(), "Opciones", items,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int item) {
                                    switch (item) {
                                        case 0:
                                            taskRecurso.select(BLSession.getInstance().getUsuario(), recurso);
                                            break;
                                    }
                                }
                            }).show();
                    return true;
                }
            });
        }

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recargar();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        visualizarMenus(menu, true, false, Utiles.esSoloAdmin(), false, false, true, false);
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
            case R.id.btn_menu_nuevo:
                BLSession.getInstance().setRecurso(new Recurso());
                taskRecurso.select(BLSession.getInstance().getUsuario(), BLSession.getInstance().getRecurso());
                return true;
            case R.id.btn_menu_recargar:
                taskRecurso.borrarList();
               recargar();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void recargar(){
        JsonPeticion peticion = new JsonPeticion();
        peticion.setUser(new Usuario(BLSession.getInstance().getUsuario()));
        peticion.setDisciplina(BLSession.getInstance().getDisciplina());
        peticion.setGrado(BLSession.getInstance().getGrado());
        taskRecurso.list(BLSession.getInstance().getUsuario(), peticion, getView().findViewById(R.id.recursoListView));
    }
}
