package bris.es.budolearning.fragments;

import android.content.DialogInterface;
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
import bris.es.budolearning.slidingtabs.PagerItem;
import bris.es.budolearning.task.TaskFichero;
import bris.es.budolearning.task.TaskRecurso;
import bris.es.budolearning.utiles.BLSession;
import bris.es.budolearning.utiles.Utiles;
import bris.es.budolearning.utiles.UtilesDialog;

public class FragmentRecursos extends FragmentAbstract {

    private TaskRecurso taskRecurso;
    private FragmentRecursos este;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recursos, container, false);
        este = this;
        taskRecurso = new TaskRecurso(getActivity(), this);

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
        if (Utiles.esSoloAdmin()) {
            inflater.inflate(R.menu.menu_recargar_anadir, menu);
        } else {
            inflater.inflate(R.menu.menu_recargar, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.menu_nuevo:
                BLSession.getInstance().setRecurso(new Recurso());
                taskRecurso.select(BLSession.getInstance().getUsuario(), BLSession.getInstance().getRecurso());
                return true;
            case R.id.menu_recargar:
                taskRecurso.borrarList();
                taskRecurso.list(BLSession.getInstance().getUsuario(), BLSession.getInstance().getDisciplina(), BLSession.getInstance().getGrado(), getView().findViewById(R.id.recursoListView));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void recargar(){
        taskRecurso.list(BLSession.getInstance().getUsuario(), BLSession.getInstance().getDisciplina(), BLSession.getInstance().getGrado(), getView().findViewById(R.id.recursoListView));
    }
}
